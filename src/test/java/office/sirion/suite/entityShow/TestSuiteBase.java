package office.sirion.suite.entityShow;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("---------------------------------------------------------------------------------------------------------------------");
		Logger.debug("---------------------------------------------------------------------------------------------------------------------");
		Logger.debug("Checking Runmode of Entity Show Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "Entity Show Suite")) {
			Logger.debug("Skipped Entity Show Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Entity Show Suite is set to NO. So Skipping all tests in Entity Show Suite");
			}
		}
	
	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
		}
	}