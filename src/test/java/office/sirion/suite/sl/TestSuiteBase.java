package office.sirion.suite.sl;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("=================================================================================================================================");
		Logger.debug("=================================================================================================================================");
		Logger.debug("Checking Runmode of SL Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "SL Suite")) {
			Logger.debug("Skipped SL Suite as the runmode was set to NO");
			throw new SkipException("Runmode of SL Suite is set to NO. So Skipping all tests in SL Suite");
		}
	}

	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
	}
}