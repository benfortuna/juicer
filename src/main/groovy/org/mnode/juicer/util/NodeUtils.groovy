package org.mnode.juicer.util

class NodeUtils {

	static def traverse(javax.jcr.Node node, Closure closure) {
		node.nodes.each {
			traverse(it, closure)
		}
		closure.call(node)
	}
}
