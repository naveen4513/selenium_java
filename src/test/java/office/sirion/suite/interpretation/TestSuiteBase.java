package office.sirion.suite.interpretation;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("Checking Runmode of Interpretation Suite");
		
		if(!TestUtil.isSuiteRunnable(suiteXls, "Interpretation Suite")) {
			Logger.debug("Skipped Interpretation Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Interpretation Suite set to NO. So Skipping all tests in Suite");
			}
		}
	
	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
		}
	}
