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
package org.mnode.juicer

import javax.jcr.Session


class JcrNodeCategorySpec extends AbstractJcrSpec {
	
	def 'verify node is correctly renamed'() {
		setup: 'create a new node'
        def node = session.rootNode.addNode('testRenameNode')
		
		and: 'rename node'
		use(JcrNodeCategory) {
			node.rename('testNodeIsRenamed')
		}
		
		expect: 'check node is renamed as expected'
		assert session.getNode('/testNodeIsRenamed')
	}
	
	def 'verify node is correctly moved'() {
		setup: 'create a new node'
        def node = session.rootNode.addNode('testMoveNode')
		
		and: 'create another'
		def node2 = session.rootNode.addNode('testMoveNode2')
		
		and: 'move the first node to be a child of the second'
		use(JcrNodeCategory) {
			node.move(node2.path)
		}
		
		expect: 'check node is moved as expected'
		assert session.getNode('/testMoveNode2/testMoveNode')
	}

	def 'verify property is set correctly'() {
		setup: 'create a new node'
		def node = session.rootNode.addNode('testSetProperty')
		
		and: 'set a property value'
//		use(JcrNodeCategory) {
			node['aProperty'] = 'test'
//		}
		
		expect: 'the property has been set'
		assert node.getProperty('aProperty').value.string == 'test'
	}

}
