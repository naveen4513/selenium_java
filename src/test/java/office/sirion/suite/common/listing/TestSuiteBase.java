package office.sirion.suite.common.listing;

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
		Logger.debug("Checking Runmode of Common Listing Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "Common Listing Suite")) {
			Logger.debug("Skipped Common Listing Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Common Listing Suite set to NO. so skipping all tests in Common Listing Suite");
			}
		}

	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
		}
	}