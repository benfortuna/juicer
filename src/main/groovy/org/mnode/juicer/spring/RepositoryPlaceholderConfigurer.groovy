package org.mnode.juicer.spring

import javax.jcr.version.VersionHistory

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer

class RepositoryPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	javax.jcr.Session session
	
	String version
	
	protected String resolvePlaceholder(String placeholder, Properties props) {
		String[] path = placeholder.split(/\./)
		VersionHistory versionHistory = session.workspace.versionManager.getVersionHistory(path[0])
		javax.jcr.Node node = versionHistory.getVersionByLabel(version).frozenNode
		node[path[1]].string
	}
}
