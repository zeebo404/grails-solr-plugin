includeTargets << grailsScript("_GrailsInit")

def installSolrGroovy = { src ->

	def solrConfDir = "${basedir}/grails-app/conf/solr"

	if (!new File(solrConfDir).exists()) {
		ant.mkdir dir: solrConfDir

		ant.copy(todir: solrConfDir) {
			fileset dir: "${src}/src/solrConfig"
		}
	}
}

target(initSolrGroovy: 'Initialize the Solr core config') {
	installSolrGroovy grailsSolrPluginDir
}

target(installSolrGroovy: 'Installs from the pluginBasedir') {
	installSolrGroovy pluginBasedir
}