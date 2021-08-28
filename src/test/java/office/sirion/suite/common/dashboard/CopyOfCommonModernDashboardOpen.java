package office.sirion.suite.common.dashboard;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import office.sirion.util.TestCommonDashboard;
import office.sirion.util.TestUtil;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import testlink.api.java.client.TestLinkAPIResults;

public class CopyOfCommonModernDashboardOpen extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(common_dashboard_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(common_dashboard_suite_xls, this.getClass().getSimpleName());
		}
	
	@Test(dataProvider = "getTestData")
	public void CommonModernDashboardOpenTest (
			) throws InterruptedException, IOException {

		count++;
		if(!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data is set to NO " +count);

		int[] chartReportListArray = {19};
		
		List<Integer> chartReportIDList = new ArrayList<Integer>();
		for (int index = 0; index < chartReportListArray.length; index++) {
			chartReportIDList.add(chartReportListArray[index]);
			}

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));
	    
	    Logger.debug("Executing Test Case Modern Dashboard Open --- ");
		
	    driver.get(CONFIG.getProperty("endUserURL"));
	    fluentWaitMethod(driver.findElement(By.id("globalView")));


		// Click on Select Charts icon
	    fluentWaitMethod(driver.findElement(By.id("priorityListModernView")));
	    driver.findElement(By.id("priorityListModernView")).click();

	    // de-Select Selected Charts
	    TestCommonDashboard.deSelectChartList();
	    
	    // Get List of Charts to be Selected
	    List<String> chartListID = TestCommonDashboard.getChartList();
		Iterator<String> itrchartListID = chartListID.iterator();

		while (itrchartListID.hasNext()) {
			String chartID = itrchartListID.next();
	    
			String selectedDashboardName = driver.findElement(By.className("chartSelectionBody")).findElement(By.id(chartID)).getText();

			driver.findElement(By.className("chartSelectionBody")).findElement(By.id(chartID)).findElement(By.className("chartListClass")).click();

			// Click on Apply and Save button
			driver.findElement(By.className("chartSelectionHeader")).findElement(By.id("savePreferences")).click();

			String chartIDShowPage = chartID.replaceAll("li_", "");
		    fluentWaitMethod(driver.findElement(By.id("chartContainer" + chartIDShowPage)));
		    
		    try {
				takeScreenShotAs(this.getClass().getSimpleName(), selectedDashboardName.toUpperCase());
			} catch (IOException e) {
				e.printStackTrace();
			}

		    Assert.assertEquals(driver.findElement(By.id("chartContainerTable")).findElement(By.className("chartTitle")).getText().toUpperCase(), selectedDashboardName.toUpperCase());
		    Logger.debug("Test Case Modern Dashboard Open For --- " + selectedDashboardName.toUpperCase() + " --- is Completed Successfully");

		    if (CONFIG.getProperty("dashboardDownload").equalsIgnoreCase("Yes")) {
			    if (driver.findElement(By.className("c-subHead")).findElements(By.className("moreOptions")).size()!=0) {
	    			String dashboardGraphNameBefore = selectedDashboardName.toUpperCase().replace(" '", " ");
	    			String dashboardGraphName = dashboardGraphNameBefore.toUpperCase().replace(" ", "_");
	    			
		    		if (!chartReportIDList.contains(Integer.valueOf(chartIDShowPage))) {
		    			File fileSrc = new File(System.getProperty("user.home")+"//Downloads//"+dashboardGraphName+".png");

						if (fileSrc.exists())
							fileSrc.delete();

						new Actions(driver).moveToElement(driver.findElement(By.className("c-subHead"))).clickAndHold().build().perform();
						driver.findElement(By.linkText("Download Graph")).click();


		    			if (driver.findElements(By.className("alertdialog-icon")).size()!=0) {
					    	Assert.assertEquals(driver.findElement(By.className("alertdialog-icon")).getText(), "Download action may take some time, please wait.");
		    				driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();
		    				}

		    			if (fileSrc.exists()) {
			    			File destFolder = new File(System.getProperty("user.dir")+"//Downloaded Data//"+new SimpleDateFormat("dd_MMMM_yyyy").format(Calendar.getInstance().getTime()).toString() + "//Common Dashboards//Modern Dashboard");
			    			File fileDest = new File(destFolder + "//" + dashboardGraphName + ".png");
			    			
			    			if (fileDest.exists())
			    				fileDest.delete();
			    			
			    			FileUtils.moveFile(fileSrc, fileDest);
			    			}

		    		    Logger.debug("Test Case Modern Dashboard Download Graph For --- " + selectedDashboardName.toUpperCase() + " --- is Completed Successfully");
		    		    }
	    		    
	    		    String dashboardDataName = selectedDashboardName.toUpperCase().replace(" '", " ");
	    			File fileExcelSrc = new File(System.getProperty("user.home")+"//Downloads//"+dashboardDataName.toUpperCase()+".xlsx");

					if (fileExcelSrc.exists())
						fileExcelSrc.delete();

					new Actions(driver).moveToElement(driver.findElement(By.className("c-subHead"))).clickAndHold().build().perform();
		    		if (chartReportIDList.contains(Integer.valueOf(chartIDShowPage)))
						driver.findElement(By.linkText("Download Data")).click();
		    		else
		    			driver.findElement(By.linkText("Download Graph With Data")).click();


	    			if (driver.findElements(By.className("alertdialog-icon")).size()!=0) {
				    	Assert.assertEquals(driver.findElement(By.className("alertdialog-icon")).getText(), "Download action may take some time, please wait.");
	    				driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();
	    				}
	    			
	    			if (fileExcelSrc.exists()) {
		    			File destFolder = new File(System.getProperty("user.dir")+"//Downloaded Data//"+new SimpleDateFormat("dd_MMMM_yyyy").format(Calendar.getInstance().getTime()).toString() + "//Common Dashboards//Modern Dashboard");

		    			File fileDest = new File(destFolder + "//" + dashboardDataName.toUpperCase() +".xlsx");
		    			
		    			if (fileDest.exists())
		    				fileDest.delete();
		    			
		    			FileUtils.moveFile(fileExcelSrc, fileDest);
		    			
		    			try {
		    				FileInputStream inputstream = new FileInputStream(fileDest);
		    				Workbook workbook = WorkbookFactory.create(inputstream);
		    				Assert.assertEquals(workbook.getSheetName(0), "Data");

		    				inputstream.close();
			    		    Logger.debug("Test Case Modern Dashboard Download Graph With Data For --- " + selectedDashboardName.toUpperCase() + " --- is Completed Successfully");
			    		    } catch (Exception e) {
			    				Logger.error(e.getMessage());
			    				}
		    			}
	    			else {
		    		    Logger.error("Test Case Modern Dashboard Download Graph With Data For --- " + selectedDashboardName.toUpperCase() + " --- is Completed Unsuccessfully");	    				
	    			}
			    }
		    }

		    fluentWaitMethod(driver.findElement(By.id("priorityListModernView")));
		    driver.findElement(By.id("priorityListModernView")).click();

		    // de-Select Selected Charts
		    TestCommonDashboard.deSelectChartList();
		    }
	    
	    Logger.debug("Test Case Dashboard Open is Completed Successfully--- ");
        driver.get(CONFIG.getProperty("endUserURL"));
	    }

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());
		
			if(testResult.getStatus()==ITestResult.SKIP)
				TestUtil.reportDataSetResult(common_dashboard_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");

			else if(testResult.getStatus()==ITestResult.FAILURE) {
				isTestPass=false;
				TestUtil.reportDataSetResult(common_dashboard_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
				result= "Fail";
				}
			
			else if (testResult.getStatus()==ITestResult.SUCCESS) {
				TestUtil.reportDataSetResult(common_dashboard_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
				result= "Pass";
				}
			}

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(common_dashboard_suite_xls, "Test Cases", TestUtil.getRowNum(common_dashboard_suite_xls, this.getClass().getSimpleName()),"PASS");
		else
			TestUtil.reportDataSetResult(common_dashboard_suite_xls, "Test Cases", TestUtil.getRowNum(common_dashboard_suite_xls, this.getClass().getSimpleName()),"FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(common_dashboard_suite_xls, this.getClass().getSimpleName());
		}
	}