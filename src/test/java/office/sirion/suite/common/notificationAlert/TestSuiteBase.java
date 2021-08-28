package office.sirion.suite.common.notificationAlert;

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
		Logger.debug("Checking Runmode of Notification Alert - End-User");
		if (!TestUtil.isSuiteRunnable(suiteXls, "Notification Alert")) {
			Logger.debug("Skipped Notification Alert - End-User Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Notification Alert - End-User Suite is set to NO. So skipping all tests in Notification Alert - End-User Suite");
			}
		}

	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
		}
	}