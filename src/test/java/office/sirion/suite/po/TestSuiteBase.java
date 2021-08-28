package office.sirion.suite.po;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("Checking Runmode of Purchase Order Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "PO Suite")) {
			Logger.debug("Skipped Purchase Order Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Purchase Order Suite set to NO. So Skipping all tests in Purchase Order Suite");
		}
	}

	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
	}
}