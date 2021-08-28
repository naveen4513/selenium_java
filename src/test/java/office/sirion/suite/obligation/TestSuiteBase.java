package office.sirion.suite.obligation;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("==============================================================================================");
		Logger.debug("==============================================================================================");
		Logger.debug("Checking Runmode of Obligation Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "Obligation Suite")) {
			Logger.debug("Skipped Obligation Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Obligation Suite is set to NO. So Skipping all tests in Obligation Suite");
			}
	}

	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
		}
	}
