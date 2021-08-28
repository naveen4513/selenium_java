package office.sirion.suite.supplier;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("===========================================================================================================");
		Logger.debug("===========================================================================================================");
		Logger.debug("Checking Runmode of Supplier Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "Supplier Suite")) {
			Logger.debug("Skipped Supplier Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Supplier Suite is set to NO. So Skipping all tests in Supplier Suite");
		}
	}

	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
	}
}