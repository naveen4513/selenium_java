package office.sirion.suite.PagePerformance;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("Checking Runmode of Page Performance Test Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "PagePerformance Suite")) {
			Logger.debug("Skipped Page Performance Test Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Page Performance Test Suite set to NO. So Skipping all tests in Page Performance Test Suite");
			}
		}
	
	@AfterSuite
	public void checkSuiteClosure() {
	  closeBrowser();
		}
	}
