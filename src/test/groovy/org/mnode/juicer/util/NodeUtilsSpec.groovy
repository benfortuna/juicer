package org.mnode.juicer.util

import org.mnode.juicer.AbstractJcrSpec

class NodeUtilsSpec extends AbstractJcrSpec {

	def 'test traverse node'() {
		setup: 'create a new node and children'
		def node = session.rootNode.addNode('testTraverseNode')
		node.addNode('child1')
		node.addNode('child1')
		node = node.addNode('child3')
		node.addNode('subchild')
		
		and:
		NodeUtils.traverse(session.rootNode) {
			println 'node:'
			println it.name
		}
	}
	
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
}
