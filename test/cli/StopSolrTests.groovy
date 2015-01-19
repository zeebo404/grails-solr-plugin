import grails.test.AbstractCliTestCase

class StopSolrTests extends AbstractCliTestCase {
	protected void setUp() {
		super.setUp()
	}

	protected void tearDown() {
		super.tearDown()
	}

	void testStopSolr() {

		execute(["stop-solr"])

		assertEquals 0, waitForProcess()
		verifyHeader()
	}
}
