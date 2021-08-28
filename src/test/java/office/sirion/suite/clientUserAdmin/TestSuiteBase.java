package office.sirion.suite.clientUserAdmin;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("Checking Runmode of Client User Admin Suite");
		if(!TestUtil.isSuiteRunnable(suiteXls, "Client User Admin Suite")) {
			Logger.debug("Skipped Client User Admin Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Client User Admin Suite set to NO. So Skipping all tests in Client User Admin Suite");
			}
		}
	
	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
		}
	}