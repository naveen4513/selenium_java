package office.sirion.suite.ToDo;

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
		Logger.debug("Checking Runmode of ToDo Suite");
		if(!TestUtil.isSuiteRunnable(suiteXls, "ToDo Suite")){
			Logger.debug("Skipped Todo Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Todo Suite set to no. So Skipping all tests in Todo Suite");
		}
		
	}
	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
	}
}
