package office.sirion.suite.contractTemplate;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("===============================================================================================================");
		Logger.debug("===============================================================================================================");
		Logger.debug("Checking Runmode of Contract Template Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "Contract Template Suite")) {
			Logger.debug("Skipped Contract Template Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Contract Template Suite is set to NO. So Skipping all tests in Contract Template Suite");
		}
	}

	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
	}
}