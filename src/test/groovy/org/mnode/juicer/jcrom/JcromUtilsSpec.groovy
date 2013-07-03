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
package org.mnode.juicer.jcrom

import org.jcrom.Jcrom
import org.jcrom.annotations.JcrName;
import org.jcrom.annotations.JcrPath;
import org.jcrom.annotations.JcrProperty;
import org.mnode.juicer.AbstractJcrSpec
import org.mnode.juicer.jcrom.JcromUtils

import spock.lang.Ignore;

class JcromUtilsSpec extends AbstractJcrSpec {

	def setup() {
		JcromUtils.jcrom = new Jcrom()
		JcromUtils.jcrom.map(JcromTest)
	}
	
	def 'test asType'() {
		setup: 'create a new node and properties'
		def node
		session.save {
			node = rootNode.addNode('testName')
			node['phone'] = 12345
		}
		
		and:
		JcromTest test = JcromUtils.asType(node, JcromTest)
		
		expect:
		test.name == node.name
		test.path == node.path
		test.phone == node['phone'].long
	}
	
	def 'test asType extension'() {
		setup: 'create a new node and properties'
		def node
		session.save {
			node = rootNode.addNode('testName')
			node['phone'] = 12345
		}
	
		and:
		JcromTest test = node as JcromTest
		
		expect:
		test.name == node.name
		test.path == node.path
		test.phone == node['phone'].long
	}
	
	def 'test addNode'() {
		setup: 'create a new class instance'
		JcromTest test = [name: 'testName', path: '/testName']
		test.phone = 12345
		
		and:
		def node = JcromUtils.addNode(session.rootNode, test)
		
		expect:
		test.name == node.name
		test.path == node.path
		test.phone == node['phone'].long
	}
	
	def 'test addNode extension'() {
		setup: 'create a new class instance'
		JcromTest test = [name: 'testName', path: '/testName']
		test.phone = 12345
					
		and:
		def node = session.rootNode.addNode test
						
		expect:
		test.name == node.name
		test.path == node.path
		test.phone == node['phone'].long
	}

	@Ignore
	def 'test extension module'() {
		setup: 'create a new node and children'
		def node = session.rootNode.addNode('testTraverseNode')
		node.addNode('child1')
		node.addNode('child1')
		node = node.addNode('child3')
		node.addNode('subchild')

		and:
		def totalNodeCount = 0
		session.rootNode.testTraverseNode.traverse {
			totalNodeCount++
		}
		
		expect:
		totalNodeCount == 5
	}
	
	static class JcromTest {
		@JcrName String name
		@JcrPath String path
		@JcrProperty int phone
	}
}
