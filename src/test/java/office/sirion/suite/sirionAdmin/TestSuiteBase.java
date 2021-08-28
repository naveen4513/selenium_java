package office.sirion.suite.sirionAdmin;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("Checking Runmode of Sirion Admin Suite");

		if (!TestUtil.isSuiteRunnable(suiteXls, "Sirion Admin Suite")) {
			Logger.debug("Skipped Sirion Admin Suite as the Runmode was set to NO");
			throw new SkipException("Runmode of Sirion Admin Suite is set to NO. So Skipping all tests in Sirion Admin Suite");
		}
	}

	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
	}
}
