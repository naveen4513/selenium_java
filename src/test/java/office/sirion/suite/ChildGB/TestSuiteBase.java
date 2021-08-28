package office.sirion.suite.ChildGB;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("Checking Runmode of Child GB Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "Child GB Suite")) {
			Logger.debug("Skipped Child GB Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Child GB Suite set to NO. So Skipping all tests in Suite");
			}
		}

	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
		}
	}