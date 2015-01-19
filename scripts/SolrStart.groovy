includeTargets << grailsScript("_GrailsInit") <<
		new File('scripts/_Install.groovy') <<
		new File('scripts/SolrStop.groovy') <<
		new File('scripts/SolrGen.groovy')

target( solrstart: "Start Solr Jetty Instance") {
	depends 'checkport', 'initSolrGroovy', 'softSolrGen'

	sleep 1000

	if (!ant.project.properties.'solr.running') {
		ant.java(jar: "${grailsSettings.config.solr.home}/start.jar", dir: grailsSettings.config.solr.home, fork:true, spawn:true) {
			jvmarg(value:"-DSTOP.PORT=${grailsSettings.config.solr.stopPort}")
			jvmarg(value:"-DSTOP.KEY=${grailsSettings.config.solr.stopKey}")
			jvmarg(value:"-Dsolr.solr.home=${grailsSettings.config.solr.root}")
			jvmarg value:"-Djetty.port=${grailsSettings.config.solr.port}"
	//		arg(line:"etc/jetty-logging.xml etc/jetty.xml")
		}

		println "Starting Solr - solr.home is ${grailsSettings.config.solr.root}"
		println "-----------"
		println "Solr logs can be found here: ${grailsSettings.config.solr.root}/logs"
		println "Console access: http://localhost:${grailsSettings.config.solr.port}/solr/"
		println "-----------"
	}
	else {
		println 'Solr is already running.'
	}
}

setDefaultTarget solrstart

target(checkport: 'Test port for Solr') {
	condition(property: 'solr.running') {
		socket(server: 'localhost', port: grailsSettings.config.solr.port)
	}
}