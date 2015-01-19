import grails.test.AbstractCliTestCase

class DeleteSolrIndexTests extends AbstractCliTestCase {
	protected void setUp() {
		super.setUp()
	}

	protected void tearDown() {
		super.tearDown()
	}

	void testDeleteSolrIndex() {

		execute(["delete-solr-index"])

		assertEquals 0, waitForProcess()
		verifyHeader()
	}
}
