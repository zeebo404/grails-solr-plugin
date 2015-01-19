package com.zeebo.grails.solr

import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.impl.HttpSolrServer
import org.apache.solr.client.solrj.response.QueryResponse

class SolrService {

	def grailsApplication

	def servers = [:].withDefault { String url -> new HttpSolrServer(url) }

	static parseSolrId(String solrId) {

		if (solrId && solrId.count('-') == 1) {
			return solrId.split(/\-/).with {
				[class: it[0], id: it[1] as long]
			}
		}
		return null
	}

	static parseSolrFieldName(String solrField) {
		def typeSuffixLength = SolrDomainObject.TYPE_MAPPING.values().find { solrField.endsWith(it) }?.length()
		if (typeSuffixLength) {
			return solrField[0..<typeSuffixLength * -1]
		}
		return solrField
	}

	def getServer() {
		servers[url]
	}

	private def getUrl() {
		grailsApplication.config.solr?.url ?: 'http://localhost:8983/solr/solr'
	}

	def search(SolrQuery query) {

		QueryResponse response = server.query(query)

		def results = []

		response.results.each { doc ->
			def map = [:]

			map.putAll doc

			map.id = parseSolrId(doc.id)?.id
			map.solrDocument = doc
			results << map
		}

		results.metaClass.getDomainObjects = {

			it = it.collect { parseSolrFieldName(it) }
			def ids = [:].withDefault { [] }
			it.each {
				ids[it.class] << it.id
			}
			def objs = [:]
			ids.each { clazz, keys ->
				objs[clazz] = grailsApplication.getDomainClass(clazz.name).getAll(keys)
			}

			def ret = []
			it.each { obj ->
				ret << objs[obj.class].find { it.id == obj.id }
			}
			return ret
		}

		return results
	}

	def parseDomainObjects(results) {

		results.collect { doc ->

			Class docClass = Class.forName(doc.type)

			def constructorArgs = [:]
			doc.entrySet().collect { [parseSolrFieldName(it.key), it.value] } .each { key, value ->
				if (docClass.declaredFields.find { it.name == key && !grailsApplication.isDomainClass(it.type) }) {
					constructorArgs[key] = value
				}
			}

			if (docClass) {
				return docClass.newInstance(constructorArgs)
			}
			return null
		}
	}
}