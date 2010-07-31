package org.mnode.juicer;

import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


class JcrNodeCategoryTest extends AbstractJcrTest {
    
    @Test
    void testCreateNode() {
        def node = session.rootNode.addNode('testCreateNode')
        
        assert node.path == '/testCreateNode'
    }
    
    @Test
    void testCreateProperty() {
        def node = session.rootNode.addNode('testCreateProperty')
        use(JcrNodeCategory) {
//            node['test'] = 'Test Property'
            node.setProperty 'test', 'Test Property'
        
            assert node['test'].string == 'Test Property'
        }
    }
    
    @Test
    void testGetNodeAt() {
        def node = session.rootNode.addNode('testGetNodeAt')
        node.addNode('childnode1')
        node.addNode('childnode2')
        node.addNode('childnode3')
        use(JcrNodeCategory) {
            assert node[0].name == 'childnode1'
            assert node[1].name == 'childnode2'
            assert node[2].name == 'childnode3'
        }
    }
}
