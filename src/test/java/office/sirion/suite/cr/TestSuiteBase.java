package office.sirion.suite.cr;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("================================================================================");
		Logger.debug("================================================================================");
		Logger.debug("Checking Runmode of CR Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "CR Suite")) {
			Logger.debug("Skipped CR Suite as the runmode was set to NO");
			throw new SkipException("Runmode of CR Suite is set to NO. So Skipping all tests in CR Suite");
		}
	}

	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
	}
}