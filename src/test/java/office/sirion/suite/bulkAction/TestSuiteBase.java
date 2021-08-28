package office.sirion.suite.bulkAction;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("=============================================================================================");
		Logger.debug("=============================================================================================");
		Logger.debug("Checking Runmode of Bulk Suite");
		if(!TestUtil.isSuiteRunnable(suiteXls, "Bulk Action Suite")) {
			Logger.debug("Skipped Bulk Jobs Suite as the runmode was set to NO");
			throw new SkipException("Runmode of bulk Jobs Suite is set to NO. So skipping all tests in Bulk Suite");
			}
		}
	
	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
		}
	}