/**
 * Copyright (c) 2011, Ben Fortuna
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
