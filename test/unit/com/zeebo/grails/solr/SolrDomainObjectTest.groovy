package com.zeebo.grails.solr

class SolrDomainObjectTest extends GroovyTestCase {

	def testObject

	def id = 1

	void setUp() {
		testObject = new SolrDomainObject(domainObject: new SolrTestDomainObject(id: id))
	}

	void testGetId() {
		assert testObject.id == "${SolrTestDomainObject.name}-$id"
	}

	void testGetSolrFieldName() {
		assert testObject.getSolrFieldName('customString') == 'customString_s'
	}
}
