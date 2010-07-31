package org.mnode.juicer

import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;

abstract class AbstractJcrTest {
    
    protected static Session session
    
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

}
