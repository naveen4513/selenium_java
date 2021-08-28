package office.sirion.suite.wor;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("Checking Runmode of WOR Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "WOR Suite")) {
			Logger.debug("Skipped WOR Suite as the runmode was set to NO");
			throw new SkipException("Runmode of WOR Suite set to NO. So Skipping all tests in WOR Suite");
		}
	}

	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
	}
}