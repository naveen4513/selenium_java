package office.sirion.suite.bulkCreate;

import office.sirion.base.TestBase;
import office.sirion.util.TestUtil;
import org.testng.SkipException;
import org.testng.annotations.BeforeSuite;

public class TestSuiteBase extends TestBase {
    // check if the suite ex has to be skiped
    @BeforeSuite
    public void checkSuiteSkip() throws Exception {
        initialize();
        Logger.debug("Checking Runmode of Bulk Create Suite");
        if (!TestUtil.isSuiteRunnable(suiteXls, "Bulk Create Suite")) {
            Logger.debug("Skipped Bulk Operations Suite as the runmode was set to NO");
            throw new SkipException("Runmode of Bulk Operations Suite set to NO,so Skipping all tests in Bulk Create Suite");
        }

    }
}
