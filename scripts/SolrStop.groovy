import org.codehaus.gant.GantState

includeTargets << grailsScript("_GrailsInit")

target(solrstop: 'Stops the running Solr server') {

	println 'Stopping Solr...'
	ant.java(jar: "${grailsSettings.config.solr.home}/start.jar", fork:true) {
		jvmarg value:"-DSTOP.PORT=${grailsSettings.config.solr.stopPort}"
		jvmarg value:"-DSTOP.KEY=${grailsSettings.config.solr.stopKey}"
		arg value:'--stop'
	}
}

setDefaultTarget(solrstop)
