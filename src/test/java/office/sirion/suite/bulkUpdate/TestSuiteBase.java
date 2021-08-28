package office.sirion.suite.bulkUpdate;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.File;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("=============================================================================================");
		Logger.debug("=============================================================================================");
		Logger.debug("Checking Runmode of Bulk Update Suite");
		if(!TestUtil.isSuiteRunnable(suiteXls, "Bulk Update Suite")) {
			Logger.debug("Skipped Bulk Jobs Suite as the runmode was set to NO");
			throw new SkipException("Runmode of bulk Jobs Suite is set to NO. So skipping all tests in Bulk Update Suite");
			}
		}
	
	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
		}
	}