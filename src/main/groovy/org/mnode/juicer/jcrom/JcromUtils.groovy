package org.mnode.juicer.jcrom

import org.jcrom.Jcrom

class JcromUtils {

	static Jcrom jcrom
	
	static def asType(javax.jcr.Node node, Class<?> clazz) {
		if (jcrom) {
			jcrom.fromNode(clazz, node)
		}
		else {
			node.asType(clazz)
		}
	}
	
	static def addNode(javax.jcr.Node node, Object value) {
		if (jcrom && !String.class.isAssignableFrom(value.class)) {
			jcrom.addNode(node, value)
		}
		else {
			node.addNode(value)
		}
	}
}
