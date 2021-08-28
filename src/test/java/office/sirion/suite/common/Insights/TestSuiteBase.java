package office.sirion.suite.common.Insights;

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
		Logger.debug("Checking Runmode of Insights Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "Insights Suite")) {
			Logger.debug("Skipped Insights Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Insights Suite is set to NO. So skipping all tests in Insights Suite");
			}
		}

	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
		}
	}