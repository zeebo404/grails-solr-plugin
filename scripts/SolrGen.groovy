import groovy.io.FileType

includeTargets << grailsScript("_GrailsInit") << new File('scripts/_Install.groovy')

def confFileExtension = '.conf'

def generateConfigXml = { force ->
	GroovyShell shell = new GroovyShell()
	def imports = 'import groovy.xml.MarkupBuilder\n'
	def header = 'StringWriter sw = new StringWriter()\nMarkupBuilder mu = new MarkupBuilder(sw)\nmu.'
	def footer = '\nsw.toString()'

	def root = new File("$basedir/grails-app/conf/solr")
	def rootOut = new File(grailsSettings.config.solr.root)

	root.eachFileRecurse(FileType.FILES) {
		if (it.name.endsWith(confFileExtension)) {
			def text = it.text

			def solrconfigOut = new File(rootOut, "${it.absolutePath.replace(root.absolutePath, '').replace(confFileExtension, '')}.xml")

			if (force || !solrconfigOut.exists()) {

				if (text) {
					def script = imports + header + text + footer
					def result = shell.evaluate(script)

					solrconfigOut.parentFile.mkdirs()
					solrconfigOut.withWriter {
						it.println result
					}
				}
			}
		}
	}

	root.eachFileRecurse(FileType.FILES) {
		if (!it.name.endsWith(confFileExtension)) {
			def out = new File(rootOut, it.absolutePath.replace(root.absolutePath, ''))
			if (force || !out.exists()) {
				ant.copy file: it, tofile: out
			}
		}
	}
}

target(solrGen: 'Generates the Solr configuration XML from the config scripts') {
	depends 'initSolrGroovy'

	generateConfigXml true
}

target(softSolrGen: 'Generates the Solr configuration only if they do not exist') {
	depends 'initSolrGroovy'

	generateConfigXml false
}

setDefaultTarget(solrGen)
