package org.mnode.juicer;

import javax.jcr.observation.Event;
import javax.jcr.observation.EventListener;

import org.junit.Test;
import org.mnode.juicer.slurper.JcrNodeGPath;

import static org.junit.Assert.*;

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
        session.workspace.observationManager.addEventListener({ events ->
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
    }
}
