/**
 * Copyright (c) 2010, Ben Fortuna
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
import javax.jcr.SimpleCredentials;
import javax.jcr.query.qom.QueryObjectModelConstants;

import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mnode.juicer.query.QueryBuilder;

class QueryBuilderTest {

	static def session
	
	@BeforeClass
	static void setup() {
		def repoConfig = RepositoryConfig.create(QueryBuilderTest.getResource("/config.xml").toURI(),
			new File(System.getProperty("user.dir"), "repository").absolutePath)
		def repository = new TransientRepository(repoConfig)
		
		session = repository.login(new SimpleCredentials('readonly', ''.toCharArray()))
	}

	@AfterClass	
	static void tearDown() {
		session.logout()
	}
	
	@Test
	void testAttachments() {
		def attachments = new QueryBuilder(session.workspace.queryManager).with {
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
		
		println attachments.statement
	}

	@Test	
	void testReferences() {
		def references = new QueryBuilder(session.workspace.queryManager).with {
			query(
				source: selector(nodeType: 'nt:unstructured', name: 'headers'),
				constraint: and(
					constraint1: descendantNode(selectorName: 'headers', path: '/messages'),
					constraint2: and(
						constraint1: comparison(
							operand1: nodeNamex(selectorName: 'headers'),
							operator: QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO,
							operand2: literal(session.valueFactory.createValue('headers'))),
						constraint2: or(
								constraint1: comparison(
										operand1: propertyValue(selectorName: 'headers', propertyName: 'In-Reply-To'),
										operator: QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO,
										operand2: bindVariable('messageId')
									),
								constraint2: comparison(
										operand1: propertyValue(selectorName: 'headers', propertyName: 'References'),
										operator: QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO,
										operand2: bindVariable('messageId')
									)
							)
						)
					)
			)
		}
		
		println references.statement
	}
}
