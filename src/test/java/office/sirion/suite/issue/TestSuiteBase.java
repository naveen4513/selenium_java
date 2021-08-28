package office.sirion.suite.issue;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("Checking Runmode of Issue Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "Issue Suite")) {
			Logger.debug("Skipped Issue Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Issue Suite set to NO. So Skipping all tests in Issue Suite");
			}
		}
	
	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
		}
	}