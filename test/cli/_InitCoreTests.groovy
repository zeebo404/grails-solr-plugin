import grails.test.AbstractCliTestCase

class _InitCoreTests extends AbstractCliTestCase {
	protected void setUp() {
		super.setUp()
	}

	protected void tearDown() {
		super.tearDown()
	}

	void test_InitCore() {

		execute(["init-core"])

		assertEquals 0, waitForProcess()
		verifyHeader()
	}
}
