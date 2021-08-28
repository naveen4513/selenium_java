package office.sirion.suite.common.dashboard;

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
		Logger.debug("Checking Runmode of Common Dashboard Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "Common Dashboard Suite")) {
			Logger.debug("Skipped Common Dashboard Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Common Dashboard Suite is set to NO. So skipping all tests in Common Dashboard Suite");
			}
		}

	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
		}
	}