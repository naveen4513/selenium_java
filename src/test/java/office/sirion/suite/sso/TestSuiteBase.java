package office.sirion.suite.sso;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("=============================================================================================");
		Logger.debug("=============================================================================================");
		Logger.debug("Checking Runmode of SSO Suite");
		if(!TestUtil.isSuiteRunnable(suiteXls, "SSO Suite")) {
			Logger.debug("Skipped SSO Suite as the runmode was set to NO");
			throw new SkipException("Runmode of SSO Suite is set to NO. So skipping all tests in SSO Suite");
			}
		}
	
	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
		}
	}