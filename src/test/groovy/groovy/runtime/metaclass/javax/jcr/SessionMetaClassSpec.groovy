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
package groovy.runtime.metaclass.javax.jcr

import groovy.util.logging.Log
import groovyx.gpars.GParsPool;

import java.util.concurrent.locks.ReentrantLock

import javax.jcr.RepositoryException
import javax.jcr.Session

import org.mnode.juicer.AbstractJcrSpec

@Log
class SessionMetaClassSpec extends AbstractJcrSpec {

	def 'verify closure is executed with lock'() {
		setup:
		def lock = new ReentrantLock()
		
		and:
		GParsPool.withPool {
			1..100.eachParallel { id ->
				def retVal = session.withLock(lock) {
					rootNode.addNode("testLockedNode$id")
				}
                
                assert retVal.name == "testLockedNode$id"
			}
		}
		
		expect:
		1..100.each {
			assert session.rootNode["testLockedNode$it"]
		}
	}
	
	def 'verify session is saved'() {
		setup:
		def retVal = session.save {
			rootNode.addNode('testSessionSave')
		}
		
		expect:
		!session.hasPendingChanges()
        assert retVal.name == 'testSessionSave'
	}
	
	def 'verify session is NOT saved on exception'() {
		setup:
		try {
			session.save {
				rootNode.addNode('testSessionNotSaved')
				throw new RepositoryException()
			}
		}
		catch (RepositoryException re) {
			log.info "Caught exception $re"
		}
		
		expect:
		!session.hasPendingChanges() && session.rootNode['testSessionNotSaved'] == null
	}
	
	def 'verify session changes are preserved on exception'() {
		setup:
		try {
			session.save(false) {
				rootNode.addNode('testSessionPreserved')
				throw new RepositoryException()
			}
		}
		catch (RepositoryException re) {
			log.info "Caught exception $re"
		}
		
		expect:
		session.hasPendingChanges() && session.rootNode['testSessionPreserved'] != null
	}
}
