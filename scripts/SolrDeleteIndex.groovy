includeTargets << grailsScript("_GrailsInit") <<
		new File('scripts/SolrStop.groovy')

target(deleteSolrIndex: 'Delete Solr Index') {
	depends 'solrstop'

	sleep 1000

	def indexDir = "${grailsSettings.config.solr.root}/solr/data"

	ant.delete dir: indexDir
	println "Deleted $indexDir"
}

setDefaultTarget deleteSolrIndex