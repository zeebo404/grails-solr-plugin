package com.zeebo.grails.solr

import org.apache.solr.client.solrj.SolrQuery

/**
 * User: Eric
 * Date: 1/17/15
 */
class SolrSearcher {

	def domainClass

	def ctx

	def search(String query) {
		search(new SolrQuery(query))
	}

	def search(SolrQuery query) {

		def solrService = ctx.getBean('solrService')
		def server = solrService.server
		query.addFilterQuery("${SolrDomainObject.FIELD_TYPE}:${domainClass.name}")

		return solrService.search(query)
	}
}
