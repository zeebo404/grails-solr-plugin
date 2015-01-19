package com.zeebo.grails.solr

class SolrTestDomainObject {

	static constraints = {
	}

	long id

	String customString
	int customInt
	long customLong
	float customFloat
	double customDouble
	boolean customBoolean
	Date customDate

	@Override
	public java.lang.String toString() {
		return "SolrTestDomainObject{" +
				"id=" + id +
				", customString='" + customString + '\'' +
				", customInt=" + customInt +
				", customLong=" + customLong +
				", customFloat=" + customFloat +
				", customDouble=" + customDouble +
				", customBoolean=" + customBoolean +
				", customDate=" + customDate +
				'}';
	}
}
