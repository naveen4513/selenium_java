package office.sirion.suite.Calender;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;
import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase{
    // check if the suite ex has to be skiped
	@BeforeSuite
	public void checkSuiteSkip() throws Exception{
		initialize();
		Logger.debug("Checking Runmode of Calendar Suite");
		if(!TestUtil.isSuiteRunnable(suiteXls, "Calendar Suite")){
			Logger.debug("Skipped Calendar Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Calendar Suite set to no. So Skipping all tests in Calendar Suite");
		}
		
	}
}
