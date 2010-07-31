package org.mnode.juicer;

import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


class JcrNodeCategoryTest {

    private static Session session
    
    @BeforeClass
    static void initialise() {
        def configFile = JcrNodeCategoryTest.getResource('/config.xml').toURI()
        def homeDir = new File('target/repository').absolutePath
        def config = RepositoryConfig.create(configFile, homeDir)
        
        def repository = new TransientRepository(config)
        
        session = repository.login(new SimpleCredentials('admin', ''.toCharArray()))
    }
    
    @AfterClass
    static void shutdown() {
        session.logout()
    }
    
    @After
    void tearDown() {
        session.refresh false
    }
    
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
