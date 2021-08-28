package office.sirion.suite.clientSetupAdmin;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("Checking Runmode of client Setup Admin Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "Client Setup Admin Suite")) {
			Logger.debug("Skipped Client Setup Admin Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Client Setup Admin Suite is set to NO. So Skipping all tests in Client Setup Admin Suite");
			}
		}

	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
		}
	}