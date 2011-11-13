/**
 * Copyright (c) 2011, Ben Fortuna
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  o Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 *  o Neither the name of Ben Fortuna nor the names of any other contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mnode.juicer.query

import javax.jcr.Session
import javax.jcr.query.qom.QueryObjectModelConstants

import org.mnode.juicer.AbstractJcrSpec

class QueryBuilderSpec extends AbstractJcrSpec {

	def 'verify attachments query is generated correctly'() {
		setup:
		def query = new QueryBuilder(session.workspace.queryManager).with {
			query(
				source: selector(nodeType: 'nt:file', name: 'files'),
				constraint: and(
					constraint1: descendantNode(selectorName: 'files', path: '/'),
					constraint2: and(
						constraint1: not(comparison(
							operand1: nodeNamex(selectorName: 'files'),
							operator: QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO,
							operand2: literal(session.valueFactory.createValue('part')))),
						constraint2: not(comparison(
							operand1: nodeNamex(selectorName: 'files'),
							operator: QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO,
							operand2: literal(session.valueFactory.createValue('data'))))))
			)
		}
		
		expect:
		query.statement == "SELECT * FROM [nt:file] AS files WHERE ISDESCENDANTNODE(files, [/]) AND (NOT NAME(files) = 'part') AND (NOT NAME(files) = 'data')"
	}

	def 'verify headers query is generated correctly'() {
		setup:
		def query = new QueryBuilder(session.workspace.queryManager).with {
			query(
				source: selector(nodeType: 'nt:unstructured', name: 'headers'),
				constraint: and(
					constraint1: descendantNode(selectorName: 'headers', path: '/messages'),
					constraint2: and(
						constraint1: comparison(
							operand1: nodeNamex(selectorName: 'headers'),
							operator: QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO,
							operand2: literal(session.valueFactory.createValue('headers'))),
						constraint2: propertyExistence(selectorName: 'headers', propertyName: 'Received')
					)
				)
			)
		}
		
		expect:
		query.statement == "SELECT * FROM [nt:unstructured] AS headers WHERE ISDESCENDANTNODE(headers, [/messages]) AND NAME(headers) = 'headers' AND headers.Received IS NOT NULL"
	}

	def 'verify messages query is generated correctly'() {
		setup:
		def query = new QueryBuilder(session.workspace.queryManager).with {
			query(
				source: leftJoin(source1: selector(nodeType: 'nt:unstructured', name: 'messages'),
					source2: selector(nodeType: 'nt:unstructured', name: 'headers'),
					condition: childNodeJoinCondition(childSelectorName: 'headers', parentSelectorName: 'messages')),
				constraint: and(
					constraint1: descendantNode(selectorName: 'messages', path: '/messages'),
					constraint2: and(
						constraint1: comparison(
							operand1: nodeNamex(selectorName: 'headers'),
							operator: QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO,
							operand2: literal(session.valueFactory.createValue('headers'))),
						constraint2: propertyExistence(selectorName: 'headers', propertyName: 'Received')
					)
				)
			)
		}
		
		expect:
		query.statement == "SELECT * FROM [nt:unstructured] AS messages LEFT OUTER JOIN [nt:unstructured] AS headers ON ISCHILDNODE(headers, messages) WHERE ISDESCENDANTNODE(messages, [/messages]) AND NAME(headers) = 'headers' AND headers.Received IS NOT NULL"
	}
}
