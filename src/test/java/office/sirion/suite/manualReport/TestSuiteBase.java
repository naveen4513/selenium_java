package office.sirion.suite.manualReport;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase{

	@BeforeSuite
	public void checkSuiteSkip() throws Exception{
		initialize();
		Logger.debug("Checking Runmode of Manual Report Suite");
		if(!TestUtil.isSuiteRunnable(suiteXls, "Manual Report Suite")){
			Logger.debug("Skipped Manual Report Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Manual Report Suite set to no. So Skipping all tests in Manual Report Suite");
		}
		
	}
	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
	}
}
