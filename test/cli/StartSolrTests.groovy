import grails.test.AbstractCliTestCase

class StartSolrTests extends AbstractCliTestCase {
	protected void setUp() {
		super.setUp()
	}

	protected void tearDown() {
		super.tearDown()
	}

	void testStartSolr() {

		execute(["start-solr"])

		assertEquals 0, waitForProcess()
		verifyHeader()
	}
}
