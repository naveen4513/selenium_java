package office.sirion.suite.docuSign;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("Checking Runmode of DocuSign Suite");
		if (!TestUtil.isSuiteRunnable(suiteXls, "DocuSign Suite")) {
			Logger.debug("Skipped DocuSign Suite as the runmode was set to NO");
			throw new SkipException("Runmode of DocuSign Suite is set to NO. So Skipping all tests in DocuSign Suite");
			}
		}
	
	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
		}
	}