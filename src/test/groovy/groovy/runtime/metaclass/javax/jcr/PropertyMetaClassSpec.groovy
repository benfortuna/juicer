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

import org.mnode.juicer.AbstractJcrSpec

class PropertyMetaClassSpec extends AbstractJcrSpec {

	def 'verify property value is appended'() {
		setup: 'add a new node'
		def node = session.rootNode.addNode('testNode')
		
		and: 'set a property value'
		node['testProperty'] = 'testValue'
		
		expect: 'assert property value is appended and returned'
//		assert node['testProperty'].values.length == 1
		node['testProperty'] << 'testValue2'
		assert node['testProperty'].values.length == 2
	}
    
    def 'verify nodes property is supported for a single reference'() {
        setup: 'add a new node'
        def node = session.rootNode.addNode('testNode')
        def referenced = session.rootNode.addNode('referencedNode')
        referenced.addMixin('mix:versionable')
        
        and: 'set a property value'
        node['testReference'] = referenced
        
        expect: 'assert referenced node is returned'
        assert node['testReference'].nodes[0].isSame(referenced)
    }
    
    def 'verify nodes property is supported for a multiple references'() {
        setup: 'add a new node'
        def node = session.rootNode.addNode('testNode')
        def referenced = session.rootNode.addNode('referencedNode')
        referenced.addMixin('mix:versionable')
        def referenced2 = session.rootNode.addNode('referencedNode2')
        referenced2.addMixin('mix:versionable')

        and: 'set a property value'
        node['testReference'] = [referenced, referenced2]
        
        expect: 'assert referenced node is returned'
        assert node['testReference'].nodes[0].isSame(referenced)
        assert node['testReference'].nodes[1].isSame(referenced2)
    }
}
