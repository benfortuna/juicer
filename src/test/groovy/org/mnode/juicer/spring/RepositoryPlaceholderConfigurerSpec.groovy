package org.mnode.juicer.spring

import javax.jcr.version.Version;

import org.mnode.juicer.AbstractJcrSpec;

import spock.lang.Specification

class RepositoryPlaceholderConfigurerSpec extends AbstractJcrSpec {

	RepositoryPlaceholderConfigurer configurer
	
	def setup() {
		configurer = [session: session, version: 'configNode-1.0']
	}
	
	def 'resolve placeholder'() {
		setup: 'initialise nodes'
		javax.jcr.Node configNode = session.rootNode << 'configNode'
		configNode.addMixin('mix:versionable')
		configNode['property1'] = 'value1'
		configNode.session.save()
		Version version = session.workspace.versionManager.checkin(configNode.path)
		version.containingHistory.addVersionLabel(version.name, 'configNode-1.0', false)
		//
		session.workspace.versionManager.checkout(configNode.path)
		configNode['property1'] = 'value2'
		configNode.session.save()
		version = session.workspace.versionManager.checkin(configNode.path)
		version.containingHistory.addVersionLabel(version.name, 'configNode-1.0.1', false)

		
		expect: 'verify version'
//		assert session.workspace.versionManager.getVersionHistory(configNode.path).getVersionByLabel('configNode-1.0').frozenNode['property1'].string == 'value1'
		assert configurer.resolvePlaceholder('/configNode.property1', null) == 'value1'
		
		assert new RepositoryPlaceholderConfigurer(session: session, version: 'configNode-1.0.1').resolvePlaceholder('/configNode.property1', null) == 'value2'
	}
}
