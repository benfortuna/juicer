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
package org.mnode.juicer.util

import groovy.transform.WithWriteLock

import javax.jcr.Binary;
import javax.jcr.Value

class NodeUtils {

	static def traverse(javax.jcr.Node node, Closure closure) {
		node.nodes.each {
			traverse(it, closure)
		}
		closure.call(node)
	}
	
	static leftShift(javax.jcr.Node node, Object value) {
		if (String.class.isAssignableFrom(value.class) && node.hasNode(value)) {
			return node.getNode(value)
		}
		else {
			return node.addNode(value)
		}
	}

	@WithWriteLock
	static void setProperty(Object node, String propertyName, Object value) {
		try {
			node.setProperty(propertyName, value)
		}
		catch (MissingPropertyException e) {
            if (value instanceof ArrayList) {
                def values = []
                value.each {
                    values << node.session.valueFactory.createValue(it)
                }
                node.setProperty propertyName, values as Value[]
            } else if (InputStream.class.isAssignableFrom(value.class)) {
				Binary binary = node.session.valueFactory.createBinary(value)
				node.setProperty propertyName, binary
            } else {
                node.setProperty propertyName, value
            }
		}
	}
}
