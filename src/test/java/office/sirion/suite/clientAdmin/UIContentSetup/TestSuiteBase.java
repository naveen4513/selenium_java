package office.sirion.suite.clientAdmin.UIContentSetup;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("===============================================================================================");
		Logger.debug("===============================================================================================");
		Logger.debug("Checking Runmode of Client UI Content Setup Suite");
		if(!TestUtil.isSuiteRunnable(suiteXls, "Client UI Content Setup Suite")) {
			Logger.debug("Skipped Client UI Content Setup Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Client UI Content Setup Suite is set to NO. So Skipping all tests in Client UI Content Setup Suite");
			}
		}
	
	@AfterSuite
	public void checkSuiteClosure() {
		//closeBrowser();
		}
	}