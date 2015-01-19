import grails.test.AbstractCliTestCase

class SolrIndexDomainTests extends AbstractCliTestCase {
	protected void setUp() {
		super.setUp()
	}

	protected void tearDown() {
		super.tearDown()
	}

	void testSolrIndexDomain() {

		execute(["solr-index-domain"])

		assertEquals 0, waitForProcess()
		verifyHeader()
	}
}
