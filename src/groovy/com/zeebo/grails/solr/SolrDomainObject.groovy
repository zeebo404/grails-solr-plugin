package com.zeebo.grails.solr

import org.apache.solr.common.SolrInputDocument
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler

/**
 * User: Eric
 * Date: 1/17/15
 */
class SolrDomainObject {

	static final def FIELD_TYPE = 'doctype_s'
	static final def FIELD_TITLE = 'title_s'
	static final def IGNORED_PROPS = ['attached', 'errors', 'constraints', 'metaClass', 'log', 'class', 'version', 'id', 'hasMany', 'domainClass']

	static final def TYPE_MAPPING = [
			int: '_i',
			long: '_l',
			boolean: '_b',
			fload: '_f',
			double: '_d',
			'class java.lang.String': '_s',
			'class java.util.Date': '_dt'
	]

	def domainObject

	def getId() {
		"${domainObject.class.name}-${domainObject.id}"
	}

	def ctx

	def index() {
		def server = ctx.getBean('solrService').server

		SolrInputDocument doc = new SolrInputDocument()

		domainObject.properties.each { property ->

			def overrideMethodName = (property.name?.length() > 1) ? "indexSolr${property.name.capitalize()}" : ''
			def overrideMethod = domainObject.metaClass.pickMethod(overrideMethodName)

			if ( !overrideMethod?.invoke(domainObject, doc)) {
				if (domainObject."${property.name}") {
					def fieldName = domainObject.solrFieldName(property.name)

					if (fieldName) {
						def docKey = fieldName
						def docValue = domainObject."${property.name}"

						if (DomainClassArtefactHandler.isDomainClass(docValue.class)) {
							doc.addField(docKey, docValue.solr.id)
						}
						else {
							doc.addField(docKey, docValue)
						}
					}
				}
			}

			doc.addField(FIELD_TYPE, domainObject.class.name)
			doc.addField("id", id)

			if (!doc.getField(FIELD_TITLE)) {
				def solrTitleMethod = domainObject.metaClass.pickMethod('solrTitle')
				def solrTitle = solrTitleMethod?.invoke(domainObject) ?: domainObject.toString()
				doc.addField(FIELD_TITLE, solrTitle)
			}
		}

		server.add(doc)
		server.commit()
	}

	def delete() {
		def server = ctx.getBean('solrService').server

		server.deleteByQuery("id:${id}")
		server.commit()
	}

	def getSolrFieldName(String field) {
		def property = domainObject.class.declaredFields.find { it.name == field }

//		if (!property && field.contains('.')) {
//
//			def obj = domainObject
//			field.split(/\./).with { breadcrumbs ->
//				breadcrumbs[0..<-1].each {
//					obj = obj?."${it}"
//				}
//				property = obj?.class?.declaredFields?.find { it.name == breadcrumbs[-1] }
//			}
//		}

		def solrFieldName = "$field${TYPE_MAPPING["${property?.type}"] ?: ''}"
	}
}
