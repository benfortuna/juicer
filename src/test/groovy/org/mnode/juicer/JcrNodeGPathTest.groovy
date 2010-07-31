package org.mnode.juicer;

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
        javax.jcr.Node node = session.rootNode.addNode('testSetProperty')
        node = node.addNode('child1')

        def gpath = new JcrNodeGPath(session.rootNode)
        gpath.testSetProperty.child1['prop'] = 'test'
        assert gpath.testSetProperty.child1.'@prop'.string == 'test'
    }
}
