package office.sirion.suite.action;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("Checking Runmode of Action Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "Action Suite")) {
			Logger.debug("Skipped Action Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Action Suite set to NO. So Skipping all tests in Action Suite");
			}
		}
	
	@AfterSuite
	public void checkSuiteClosure() {
	  closeBrowser();
		}
	}
