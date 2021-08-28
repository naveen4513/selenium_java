package office.sirion.suite.commonFilters;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("Checking Runmode of Common Filters Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "Filters Suite")) {
			Logger.debug("Skipped Filter Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Filter Suite set to NO. So Skipping all tests in Filter Suite");
			}
		}
	
	@AfterSuite
	public void checkSuiteClosure() {
	  closeBrowser();
		}
	}
