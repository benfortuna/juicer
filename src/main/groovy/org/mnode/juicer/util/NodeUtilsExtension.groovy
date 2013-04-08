package org.mnode.juicer.util

class NodeUtilsExtension {

	static void traverse(javax.jcr.Node self, Closure<Void> closure) {
		NodeUtils.traverse(self, closure)
	}
}
