package org.mnode.juicer

import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.core.config.RepositoryConfig;

import spock.lang.Shared;
import spock.lang.Specification;

class JcrNodeCategorySpec extends Specification {

	@Shared Session session
	
	def setupSpec() {
        def configFile = JcrNodeCategorySpec.getResource('/config.xml').toURI()
        def homeDir = new File('target/repository').absolutePath
        def config = RepositoryConfig.create(configFile, homeDir)
        
        def repository = new TransientRepository(config)
        
        session = repository.login(new SimpleCredentials('admin', ''.toCharArray()))
	}
	
	def cleanupSpec() {
		session.logout()
	}
	
	def cleanup() {
		session.refresh false
	}
	
	def 'verify node is correctly renamed'() {
		setup: 'create a new node'
        def node = session.rootNode.addNode('testRenameNode')
		
		and: 'rename node'
		use(JcrNodeCategory) {
			node.rename('testNodeIsRenamed')
		}
		
		expect: 'check node is renamed as expected'
		assert session.getNode('/testNodeIsRenamed')
	}
	
	def 'verify node is correctly moved'() {
		setup: 'create a new node'
        def node = session.rootNode.addNode('testMoveNode')
		
		and: 'create another'
		def node2 = session.rootNode.addNode('testMoveNode2')
		
		and: 'move the first node to be a child of the second'
		use(JcrNodeCategory) {
			node.move(node2.path)
		}
		
		expect: 'check node is moved as expected'
		assert session.getNode('/testMoveNode2/testMoveNode')
	}
}
