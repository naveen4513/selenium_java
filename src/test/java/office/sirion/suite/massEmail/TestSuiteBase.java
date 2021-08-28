package office.sirion.suite.massEmail;

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
		Logger.debug("Checking Runmode of Mass Email Suite");
		if(!TestUtil.isSuiteRunnable(suiteXls, "Mass Email Suite")) {
			Logger.debug("Skipped Mass Email Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Mass Email Suite is set to NO. So skipping all tests in Mass Email Suite");
			}
		}
	
	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
		}
	}