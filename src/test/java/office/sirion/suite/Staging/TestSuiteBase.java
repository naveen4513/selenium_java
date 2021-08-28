package office.sirion.suite.Staging;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase{
    // check if the suite ex has to be skiped
	@BeforeSuite
	public void checkSuiteSkip() throws Exception{
		initialize();
		Logger.debug("Checking Runmode of Staging Suite");
		if(!TestUtil.isSuiteRunnable(suiteXls, "Staging Suite")){
			Logger.debug("Skipped Staging Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Staging Suite set to no. So Skipping all tests in Staging Suite");
		}
		
	}
	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
	}
}
