package office.sirion.suite.childDNO;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("Checking Runmode of Child Obligation Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "Child Obligation Suite")) {
			Logger.debug("Skipped Child Obligation Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Child Obligation Suite set to NO. So Skipping all tests in Suite");
			}
		}

	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
		}
	}