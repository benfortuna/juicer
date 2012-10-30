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
package groovy.runtime.metaclass.javax.jcr

import javax.jcr.Value;

import groovy.lang.DelegatingMetaClass
import groovy.lang.MetaClass;

class PropertyMetaClass extends DelegatingMetaClass {

	PropertyMetaClass(MetaClass delegate) {
		super(delegate)
	}
	
	Object invokeMethod(Object a_object, String a_methodName, Object[] a_arguments) {
		try {
			super.invokeMethod(a_object, a_methodName, a_arguments)
		}
		catch (MissingMethodException e) {
			if (a_methodName == 'leftShift') {
				leftShift(a_object, a_arguments[0])
			}
			else {
				throw e
			}
		}
	}
	
	javax.jcr.Value[] leftShift(javax.jcr.Property property, Object value) {
		def values = []
		if (property.multiple) {
			values << property.values
		}
		else {
			values << property.value
		}
		values << property.session.valueFactory.createValue(value)
//		property.setValue(values as Value[])
		def propName = property.name
		def parent = property.parent
		property.remove()
		parent[propName] = values as Value[]
		parent[propName].values
	}
}
