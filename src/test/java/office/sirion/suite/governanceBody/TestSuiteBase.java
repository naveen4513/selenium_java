package office.sirion.suite.governanceBody;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("Checking Runmode of GB Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "GB Suite")) {
			Logger.debug("Skipped GB Suite as the runmode was set to NO");
			throw new SkipException("Runmode of GB Suite set to NO. So Skipping all tests in GB Suite");
		}
	}

	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
	}
}
