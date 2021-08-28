package office.sirion.suite.vendorHierarchy;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
	@BeforeSuite(groups={"Regression", "Smoke"})
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("===========================================================================================================");
		Logger.debug("===========================================================================================================");
		Logger.debug("Checking Runmode of Vendor Hierarchy Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "Vendor Hierarchy Suite")) {
			Logger.debug("Skipped Vendor Hierarchy Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Vendor Hierarchy Suite is set to NO. So Skipping all tests in Vendor Hierarchy Suite");
			}
		}
	
	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
		}
	}