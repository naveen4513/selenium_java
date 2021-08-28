package office.sirion.suite.clientAdmin.workflowConfiguration;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;

import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;


public class TestSuiteBase extends TestBase {
	@BeforeSuite
	public void checkSuiteSkip() throws Exception {
		initialize();
		Logger.debug("Checking Runmode of Client Workflow Configuration Suite");
		if(!TestUtil.isSuiteRunnable(suiteXls, "Client Workflow Configuration Suite")) {
			Logger.debug("Skipped Client Workflow Configuration Suite as the runmode was set to NO");
			throw new SkipException("Runmode of Client Workflow Configuration Suite set to NO. So Skipping all tests in Client Workflow Configuration Suite");
			}
		}
	
	@AfterSuite
	public void checkSuiteClosure() {
		closeBrowser();
		}
	}