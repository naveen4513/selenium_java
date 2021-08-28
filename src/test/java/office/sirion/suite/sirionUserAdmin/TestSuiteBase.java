package office.sirion.suite.sirionUserAdmin;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("==========================================================================================================");
		Logger.debug("==========================================================================================================");
		Logger.debug("Checking Runmode of User Admin Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "Sirion User Admin Suite")) {
			Logger.debug("Skipped User Admin Suite as the runmode was set to NO");
			throw new SkipException("Runmode of User Admin Suite is set to NO. So Skipping all tests in Sirion User Admin Suite");
			}
		}

	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
		}
	}