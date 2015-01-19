import com.zeebo.grails.solr.SolrDomainObject

import org.apache.solr.client.solrj.SolrServer
import org.apache.solr.common.SolrInputDocument
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.GrailsClassUtils
import org.codehaus.groovy.tools.GroovyClass

class GrailsSolrGrailsPlugin {
	// the plugin version
	def version = "0.1"
	// the version or versions of Grails the plugin is designed for
	def grailsVersion = "2.4 > *"
	// resources that are excluded from plugin packaging
	def pluginExcludes = [
		"grails-app/views/error.gsp"
	]

	// TODO Fill in these fields
	def title = "Grails Solr Plugin" // Headline display name of the plugin
	def author = "Eric Siebeneich"
	def authorEmail = "eric.siebeneich@gmail.com"
	def description = '''\
Provides search capabilities for Grails domain model and more using the excellent Solr
open source search server through the SolrJ library.
'''

	// URL to the plugin's documentation
	def documentation = "https://github.com/zeebo404/grails-solr-plugin/wiki"

	// Extra (optional) plugin metadata

	// License: one of 'APACHE', 'GPL2', 'GPL3'
	def license = "APACHE"

	// Details of company behind the plugin (if there is one)
//	def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

	// Any additional developers beyond the author specified above.
//	def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

	// Location of the plugin's issue tracker.
	def issueManagement = [ system: "Github", url: "https://github.com/zeebo404/grails-solr-plugin/issues" ]

	// Online location of the plugin's browseable source code.
	def scm = [ url: "https://github.com/zeebo404/grails-solr-plugin" ]

//	def doWithWebDescriptor = { xml -> }

//	def doWithSpring = { }

	def doWithDynamicMethods = { ctx ->
		application.domainClasses.each {
			if (GrailsClassUtils.getStaticPropertyValue(it.clazz, 'enableSolrSearch')) {

				it.metaClass.getSolr << { new SolrDomainObject(ctx: ctx, domainObject: delegate) }
			}
		}
	}

	def doWithApplicationContext = { ctx -> }

	def onChange = { event -> }

	def onConfigChange = { event -> }

	def onShutdown = { event -> }
}
