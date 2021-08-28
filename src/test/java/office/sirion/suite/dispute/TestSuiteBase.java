package office.sirion.suite.dispute;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("Checking Runmode of Dispute Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "Dispute Suite")) {
			Logger.debug("Skipped Dispute Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Dispute Suite is set to NO. So Skipping all tests in Dispute Suite");
			}
		}
	
	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
		}
	}