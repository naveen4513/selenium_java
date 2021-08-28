package office.sirion.suite.invoice;

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
		Logger.debug("Checking Runmode of Invoice Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "Invoice Suite")) {
			Logger.debug("Skipped Invoice Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Invoice Suite set to NO. So Skipping all tests in Invoice Suite");
			}
		}

	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
		}
	}