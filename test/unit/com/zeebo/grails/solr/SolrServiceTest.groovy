package com.zeebo.grails.solr

import grails.test.mixin.TestFor
import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.SolrServer
import org.apache.solr.client.solrj.response.QueryResponse
import org.apache.solr.common.SolrDocument
import org.apache.solr.common.SolrDocumentList
import org.codehaus.groovy.grails.commons.DefaultGrailsApplication

import java.text.SimpleDateFormat

@TestFor(SolrService)
class SolrServiceTest extends GroovyTestCase {

	def solrService

	def expectedResults = [
			[id: 1, customString: 'test', customInt: 10, customDate: new SimpleDateFormat('yyyy MM dd').parse('2015 01 17')],
			[id: 2, customString: 'test2', customInt: 90, customDate: new SimpleDateFormat('yyyy MM dd').parse('2015 01 17')],
			[id: 3, customString: 'test3', customInt: -20, customDate: new Date()]
	]

	def mockSolrServer
	def mockSolrDomainObject

	void setUp() {
		solrService = new SolrService()
		mockSolrServer = mockFor(SolrServer)
		solrService.servers = [:].withDefault { mockSolrServer.createMock() }

		mockSolrDomainObject = mockFor(SolrDomainObject)
		mockSolrDomainObject.demand.static.getAll { ids ->
			return expectedResults.findAll { ids.contains(it.id) }
		}

		solrService.grailsApplication = new DefaultGrailsApplication()
	}

	void testParseSolrId() {
		def id = SolrService.parseSolrId('test-1234')

		assert id.class == 'test'
		assert id.id == 1234
	}

	void testParseSolrIdInvalid() {
		def id = SolrService.parseSolrId('invalid')

		assert id == null
	}

	void testParseSolrFieldName() {
		String fieldName = 'test'

		SolrDomainObject.TYPE_MAPPING.values().each {
			assert SolrService.parseSolrFieldName("${fieldName}${it}") == fieldName
		}

		assert SolrService.parseSolrFieldName(fieldName) == fieldName
	}

	void testSearch() {

		mockSolrServer.demand.query { SolrQuery query ->
			createQueryResponseHelper(* expectedResults)
		}

		def results = solrService.search(new SolrQuery('*:*'))

		assert results.size() == expectedResults.size()

		results.eachWithIndex { it, idx ->
			assert it.type == SolrTestDomainObject.name
			assert it.solrDocument

			expectedResults[idx].each { key, value ->
				assert it[key] == value
			}
		}

		mockSolrServer.verify()
	}

	void testParseDomainObject() {

		mockSolrServer.demand.query { SolrQuery query ->
			createQueryResponseHelper(* expectedResults)
		}

		def results = solrService.search(new SolrQuery('*:*'))

		results.domainObjects.eachWithIndex { it, idx ->
			expectedResults[idx].each { key, value ->
				assert it."${key}" == value
			}
		}

		mockSolrServer.verify()
	}

	static createQueryResponseHelper(Object... objects) {

		SolrDocumentList sdl = new SolrDocumentList(numFound: objects.size(), start: 0)
		objects.each {
			SolrDocument doc = new SolrDocument()
			doc.putAll(it)
			doc.id = new SolrDomainObject(domainObject: new SolrTestDomainObject(id: it.id)).id
			doc.type = SolrTestDomainObject.name
			sdl << doc
		}

		QueryResponse response = new QueryResponse()
		response.response = [
				reponseHeader: [status: 0, QTime: 0],
				response: sdl
		]

		return response
	}
}