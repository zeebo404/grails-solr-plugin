import grails.test.AbstractCliTestCase

class SolrGenTests extends AbstractCliTestCase {
	protected void setUp() {
		super.setUp()
	}

	protected void tearDown() {
		super.tearDown()
	}

	void testSolrGen() {

		execute(["solr-gen"])

		assertEquals 0, waitForProcess()
		verifyHeader()
	}
}
