package office.sirion.suite.contract;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("---------------------------------------------------------------------------------------------------------------------");
		Logger.debug("---------------------------------------------------------------------------------------------------------------------");
		Logger.debug("Checking Runmode of Contract Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "Contract Suite")) {
			Logger.debug("Skipped Contract Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Contract Suite is set to NO. So Skipping all tests in Contract Suite");
			}
		}
	
	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
		}
	}