/**
 * Copyright (c) 2012, Ben Fortuna
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



import javax.jcr.ValueFactory
import javax.jcr.query.QueryManager
import javax.jcr.query.qom.QueryObjectModelConstants


/**
 * @author Ben
 *
 */
class QueryBuilder extends FactoryBuilderSupport {

    QueryBuilder(QueryManager queryManager) {
        this(queryManager, null)
    }
    
    QueryBuilder(QueryManager queryManager, ValueFactory valueFactory) {
        registerFactory('and', new AndFactory(queryManager: queryManager))
        registerFactory('or', new OrFactory(queryManager: queryManager))
        registerFactory('not', new NotFactory(queryManager: queryManager))
        registerFactory('ascending', new AscendingFactory(queryManager: queryManager))
        registerFactory('bindVariable', new BindVariableFactory(queryManager: queryManager))
        registerFactory('literal', new LiteralFactory(queryManager: queryManager))
        registerFactory('childNode', new ChildNodeFactory(queryManager: queryManager))
        registerFactory('descendantNode', new DescendantNodeFactory(queryManager: queryManager))
        registerFactory('comparison', new ComparisonFactory(queryManager: queryManager, valueFactory: valueFactory))
        registerFactory('descending', new DescendingFactory(queryManager: queryManager))
        registerFactory('fullTextSearch', new FullTextSearchFactory(queryManager: queryManager, valueFactory: valueFactory))
        registerFactory('propertyValue', new PropertyValueFactory(queryManager: queryManager))
        registerFactory('propertyExistence', new PropertyExistenceFactory(queryManager: queryManager))
        registerFactory('nodeNamex', new NodeNameFactory(queryManager: queryManager))
        registerFactory('query', new QueryFactory(queryManager: queryManager))
        registerFactory('selector', new SelectorFactory(queryManager: queryManager))
        registerFactory('leftJoin', new JoinFactory(queryManager: queryManager, joinType: QueryObjectModelConstants.JCR_JOIN_TYPE_LEFT_OUTER))
        registerFactory('rightJoin', new JoinFactory(queryManager: queryManager, joinType: QueryObjectModelConstants.JCR_JOIN_TYPE_RIGHT_OUTER))
        registerFactory('innerJoin', new JoinFactory(queryManager: queryManager, joinType: QueryObjectModelConstants.JCR_JOIN_TYPE_INNER))
        registerFactory('childNodeJoinCondition', new ChildNodeJoinConditionFactory(queryManager: queryManager))
    }
    
}
