package office.sirion.suite.clientAdmin;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("Checking Runmode of Client Admin Suite");
		if(!TestUtil.isSuiteRunnable(suiteXls, "Client Admin Suite")) {
			Logger.debug("Skipped Client Admin Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Client Admin Suite set to NO. So Skipping all tests in Client Admin Suite");
			}


		}

	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
		driver.quit();
		}
	}