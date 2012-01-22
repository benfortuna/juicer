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
package org.mnode.juicer;

import javax.jcr.observation.Event;
import javax.jcr.observation.EventListener;

import org.junit.Ignore;
import org.junit.Test;
import org.mnode.juicer.slurper.JcrNodeGPath;

import static org.junit.Assert.*;

@Ignore
class JcrNodeGPathTest extends AbstractJcrTest {
    
    @Test
    void testToString() {
        def node = session.rootNode.addNode('testToString')
        node.addNode('child1')
        
        def gpath = new JcrNodeGPath(session.rootNode)
        assert gpath.testToString.child1.toString() == '/testToString/child1'
    }

    @Test
    void testGetProperty() {
        javax.jcr.Node node = session.rootNode.addNode('testGetProperty')
        node = node.addNode('child1')
        node.setProperty 'prop', 'test'
        
        def gpath = new JcrNodeGPath(session.rootNode)
        assert gpath.testGetProperty.child1.'@prop'.string == 'test'
        assert gpath.testGetProperty.child1['@prop'].string == 'test'
    }

    @Test
    void testSetProperty() {
        def eventCount = 0
        session.workspace.observationManager.addEventListener({ events ->
            eventCount++
            println 'event fired'
            for (event in events) {
                println "${event.path} - ${event.type}"
            }
        } as EventListener, 1|2|4|8|16|32, '/', true, null, null, false)
        
        if (session.rootNode.hasNode('testSetProperty')) {
            session.rootNode.getNode('testSetProperty').remove()
        }
        
        javax.jcr.Node node = session.rootNode.addNode('testSetProperty')
        node = node.addNode('child1')

        def gpath = new JcrNodeGPath(session.rootNode)
        gpath.testSetProperty.child1['prop'] = 'test'
        assert gpath.testSetProperty.child1.'@prop'.string == 'test'
        
        session.save()
        gpath.testSetProperty.child1['longprop'] = 1
        assert gpath.testSetProperty.child1.'@longprop'.long == 1
        
        session.save()
        gpath.testSetProperty.child1['longprop'] = 1
        
        session.save()
        // shouldn't get events for the last property change..
        assert eventCount == 2
    }
}
