package office.sirion.suite.clause;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("===============================================================================================================");
		Logger.debug("===============================================================================================================");
		Logger.debug("Checking Runmode of Clause Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "Clause Suite")) {
			Logger.debug("Skipped Clause Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Clause Suite is set to NO. So Skipping all tests in Clause Suite");
		}
	}

	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
	}
}