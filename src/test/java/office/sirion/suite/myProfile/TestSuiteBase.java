package office.sirion.suite.myProfile;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase{

	@BeforeSuite
	public void checkSuiteSkip() throws Exception{
		initialize();
		Logger.debug("Checking Runmode of My Profile Suite");
		if(!TestUtil.isSuiteRunnable(suiteXls, "My Profile Suite")){
			Logger.debug("Skipped My Profile Suite as the runmode was set to NO");
			throw new SkipException("Runmode of My Profile Suite set to no. So Skipping all tests in My Profile Suite");
		}
		
	}
	@AfterSuite
	public void checkSuiteClosure() {
		//closeBrowser();
	}
}
