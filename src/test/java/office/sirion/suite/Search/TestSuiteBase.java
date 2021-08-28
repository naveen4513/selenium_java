package office.sirion.suite.Search;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("Checking Runmode of Search Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "Search Suite")) {
			Logger.debug("Skipped Search Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Search Suite set to NO. So Skipping all tests in Search Suite");
			}
		}
	
	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
		}
	}