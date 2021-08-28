package office.sirion.suite.cdr;

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
		Logger.debug("Checking Runmode of CDR Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "CDR Suite")) {
			Logger.debug("Skipped CDR Suite as the runmode was set to NO");
			throw new SkipException("Runmode of CDR Suite is set to NO. So Skipping all tests in CDR Suite");
		}
	}

	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
	}
}