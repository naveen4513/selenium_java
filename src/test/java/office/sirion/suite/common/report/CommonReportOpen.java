package office.sirion.suite.common.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import office.sirion.util.TestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CommonReportOpen extends TestSuiteBase {
	String result = null;
	String runmodes[]=null;
	static int count=-1;
	static boolean isTestPass=true;

	@BeforeTest
	public void checkTestSkip() {
		if(!TestUtil.isTestCaseRunnable(common_report_suite_xls,this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			throw new SkipException("Skipping Test Case "+this.getClass().getSimpleName()+" as Runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(common_report_suite_xls, this.getClass().getSimpleName());
		}

	@Test (dataProvider="getTestData")
	public void CommonReportOpenTest (String entityName, String entityTypeID, String entityReportName, String entityReportID
			)throws InterruptedException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data -- " + entityReportName + " is set to NO " +count);

		int[] chartReportListArray = {13, 14, 17, 18, 19, 20, 21, 22, 23, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
				41, 42, 43, 44, 45, 46, 50, 51, 52, 53, 54, 57, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90,
				91, 92, 93, 94, 95, 96, 100, 101, 151, 152, 153, 201, 202, 203, 204, 205, 206, 261, 264, 275, 281, 282, 315, 324, 330,
				345, 359, 1000, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009, 1010, 1011};
		
		List<Integer> chartReportIDList = new ArrayList<Integer>();
		for (int index = 0; index < chartReportListArray.length; index++)
			chartReportIDList.add(chartReportListArray[index]);

		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

	    Logger.debug("Executing Test Case Report Open for entity -- " +entityName + " and report -- "+ entityReportName);

	    driver.get(CONFIG.getProperty("endUserURL"));
		//
		fluentWaitMethod(driver.findElement(By.id("header")).findElement(By.linkText("Reports")));

		driver.findElement(By.id("header")).findElement(By.linkText("Reports")).click();
		Thread.sleep(8000);
		fluentWaitMethod(driver.findElement(By.id("listTd1")).findElement(By.id("allLists1")));

		String entityNameAtDropdown = driver.findElement(By.xpath(".//*[@id='allLists1']/option[@value='"+convertStringToInteger(entityTypeID.trim())+"']")).getText();

		new Select(driver.findElement(By.id("listTd1")).findElement(By.id("allLists1"))).selectByVisibleText(entityNameAtDropdown);
	    Thread.sleep(8000);
		fluentWaitMethod(driver.findElement(By.id("listTd2")).findElement(By.id("allLists2")));

		// String entityReportNameAtDropdown = driver.findElement(By.xpath(".//*[@id='allLists2']/option[@value='"+convertStringToInteger(entityReportID.trim())+"']")).getText();

		new Select(driver.findElement(By.id("listTd2")).findElement(By.id("allLists2"))).selectByVisibleText(entityReportName);
	    //
		fluentWaitMethod(driver.findElement(By.id("downloadXls")));

		String reportNameOnShowPage = driver.findElement(By.id("downloadXls")).getText();
		Logger.debug(reportNameOnShowPage);

		driver.findElement(By.className("dataTables_scrollBody"));
	    Logger.debug("Report Page of -- " + entityReportName + " -- opened Successfully");

//    	fluentWaitMethod(driver.findElement(By.id("cr"))));
//    	driver.findElement(By.id("cr"));
//
//    	String dataTableInfo = 	driver.findElement(By.id("cr_wrapper")).findElement(By.className("dataTables_info")).getText();
//		String totalEntries = StringUtils.substringBetween(dataTableInfo, "of ", " entries");
//
//    	if (totalEntries.equalsIgnoreCase("0"))
//    		Logger.debug("There is no data Present under Report Name -- " + entityReportName);
//
//    	if (CONFIG.getProperty("reportDownload").equalsIgnoreCase("Yes") && !totalEntries.equalsIgnoreCase("0")) {
//    		Logger.debug("Total Number of Entries Present under Report Name --- " + entityReportName + " --- is " +totalEntries);
//
//    		if (chartReportIDList.contains(Integer.valueOf(convertStringToInteger(entityReportID.trim())))) {
//    			String reportDownloadedGraphName = reportNameOnShowPage.replace(" ", "_");
//
//    			File downloadedGraphFileSrc = new File(System.getProperty("user.home")+"//Downloads//"+reportDownloadedGraphName+".png");
//
//				if (downloadedGraphFileSrc.exists())
//					downloadedGraphFileSrc.delete();
//
//			// Click Download Graph
//				new Actions(driver).moveToElement(driver.findElement(By.id("downloadXls")).findElement(By.id("exportIconForReport"))).clickAndHold().build().perform();
//    			driver.findElement(By.linkText("Download Graph")).click();
//    			//
//
//    			if (driver.findElements(By.className("alertdialog-icon")).size()!=0)
//    				driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();
//
//    			if (downloadedGraphFileSrc.exists()) {
//	    			File downloadedGraphFolderDest = new File(System.getProperty("user.dir")+"//Downloaded Data//"+new SimpleDateFormat("dd_MMMM_yyyy").format(Calendar.getInstance().getTime()).toString() + "//Common Reports//Download Graph//"+entityName);
//	    			File downloadedGraphFileDest = new File(downloadedGraphFolderDest + "//" + reportDownloadedGraphName + ".png");
//	    			
//	    			if (downloadedGraphFileDest.exists())
//	    				downloadedGraphFileDest.delete();
//	    			
//		    		try {
//						FileUtils.moveFile(downloadedGraphFileSrc, downloadedGraphFileDest);
//			    		Logger.debug("Report Graph for -- " + entityReportName +" -- Downloaded and Moved to Project Successfully");
//			    		} catch (IOException e) {
//			    			e.printStackTrace();
//							}
//		    		}
//    			
//    			if (Integer.valueOf(totalEntries)<=Integer.valueOf(CONFIG.getProperty("maxLargeReportLimit"))
//    					&& Integer.valueOf(totalEntries)>Integer.valueOf(CONFIG.getProperty("minLargeReportLimit"))) {
//    	    		Logger.debug("Total Number of Entries Present under Report Name --- " + entityReportName + " --- is within the Maximum Limit");
//
//    	    		// Click Schedule Graph with Data
//    				new Actions(driver).moveToElement(driver.findElement(By.id("downloadXls")).findElement(By.id("exportIconForReport"))).clickAndHold().build().perform();
//        			driver.findElement(By.linkText("Schedule Graph With Data")).click();
//        			//
//
//    	    		if (driver.findElements(By.className("alertdialog-icon")).size()!=0) {
//    	    			Assert.assertEquals(driver.findElement(By.className("alertdialog-icon")).getText(), "Request submitted. You will get notification over e-mail.");
//    	    			driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();
//    	    			}
//    	    		}
//
//    		    else if (Integer.valueOf(totalEntries)>Integer.valueOf(CONFIG.getProperty("maxLargeReportLimit"))) {
//    	    		Logger.debug("Total Number of Entries Present under Report Name --- " + entityReportName + " --- is Above the Maximum Limit");
//
//    	    		new Actions(driver).moveToElement(driver.findElement(By.id("downloadXls")).findElement(By.id("exportIconForReport"))).clickAndHold().build().perform();
//    	    		driver.findElement(By.linkText("Schedule Graph With Data")).click();
//    	    		//
//
//    	    		if (driver.findElements(By.className("alertdialog-icon")).size()!=0) {
//    	    			Assert.assertEquals(driver.findElement(By.className("alertdialog-icon")).getText(), "Your report size has exceeded the limit, please apply filter to reduce records count.");
//    	    			driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();
//    	    			}
//    	    		}					    		
//    			} else {
//        			if (Integer.valueOf(totalEntries)<=Integer.valueOf(CONFIG.getProperty("maxLargeReportLimit"))
//        					&& Integer.valueOf(totalEntries)>Integer.valueOf(CONFIG.getProperty("minLargeReportLimit"))) {
//        	    		Logger.debug("Total Number of Entries Present under Report Name --- " + entityReportName + " --- is within the Maximum Limit");
//
//        	    		// Click Schedule Data
//        				new Actions(driver).moveToElement(driver.findElement(By.id("downloadXls")).findElement(By.id("exportIconForReport"))).clickAndHold().build().perform();
//            			driver.findElement(By.linkText("Schedule Data")).click();
//            			//
//
//        	    		if (driver.findElements(By.className("alertdialog-icon")).size()!=0) {
//        	    			Assert.assertEquals(driver.findElement(By.className("alertdialog-icon")).getText(), "Request submitted. You will get notification over e-mail.");
//        	    			driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();
//        	    			}
//        	    		}
//
//        		    else if (Integer.valueOf(totalEntries)>Integer.valueOf(CONFIG.getProperty("maxLargeReportLimit"))) {
//        	    		Logger.debug("Total Number of Entries Present under Report Name --- " + entityReportName + " --- is Above the Maximum Limit");
//
//        	    		new Actions(driver).moveToElement(driver.findElement(By.id("downloadXls")).findElement(By.id("exportIconForReport"))).clickAndHold().build().perform();
//        	    		driver.findElement(By.linkText("Schedule Data")).click();
//        	    		//
//
//        	    		if (driver.findElements(By.className("alertdialog-icon")).size()!=0) {
//        	    			Assert.assertEquals(driver.findElement(By.className("alertdialog-icon")).getText(), "Your report size has exceeded the limit, please apply filter to reduce records count.");
//        	    			driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();
//        	    			}
//        	    		}					    		
//    			}
//
//    		if (Integer.valueOf(totalEntries)<=Integer.valueOf(CONFIG.getProperty("minLargeReportLimit"))) {
//    			File downloadedExcelFileSrc = new File(System.getProperty("user.home")+"//Downloads//" + reportNameOnShowPage + ".xlsx");
//
//    			if (downloadedExcelFileSrc.exists())
//    				downloadedExcelFileSrc.delete();
//
//  		// Click Download Graph with Data with Selected Columns
//    			new Actions(driver).moveToElement(driver.findElement(By.id("downloadXls")).findElement(By.id("exportIconForReport"))).clickAndHold().build().perform();
//    		//	Thread.sleep(5000);
//    		new Actions(driver).moveToElement(driver.findElement(By.xpath(".//*[@id='drop']/ul/li[2]"))).clickAndHold().build().perform();
//    		//	new Actions(driver).moveToElement(driver.findElement(By.xpath("//li[@class='conditionalShow']//ul[@class='childlist reportChildList']//li[@id='exportXLSelectedReport']"))).clickAndHold().build().perform();
//    		//	new Actions(driver).moveToElement(driver.findElement(By.id("exportXLreport"))).clickAndHold().build().perform();
//    	//		new Actions(driver).moveToElement(driver.findElement(By.id("exportXLSelectedReport"))).clickAndHold().build().perform();
//    		//	new Actions(driver).moveToElement(driver.findElement(By.id("exportIconForReport"))).clickAndHold().build().perform();
//    			driver.findElement(By.id("exportXLSelectedReport/a")).click();
//    			//
//
//    			if (driver.findElements(By.className("alertdialog-icon")).size()!=0)
//    				driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();
//    			
//	    		if (downloadedExcelFileSrc.exists()) {
//	    			File selectedColumnExcelFolderDest = new File(System.getProperty("user.dir")+"//Downloaded Data//"+new SimpleDateFormat("dd_MMMM_yyyy").format(Calendar.getInstance().getTime()).toString() + "//Common Reports//Selected Columns//" + entityName);
//
//	    			File selectedColumnExcelFileDest = new File(selectedColumnExcelFolderDest + "//" + reportNameOnShowPage +".xlsx");
//		    			
//	    			if (selectedColumnExcelFileDest.exists())
//	    				selectedColumnExcelFileDest.delete();
//		    			
//		    		try {
//						FileUtils.moveFile(downloadedExcelFileSrc, selectedColumnExcelFileDest);
//						
//			    		FileInputStream selectedColumnExcelFileInputStream = new FileInputStream(selectedColumnExcelFileDest);
//			    		@SuppressWarnings("resource")
//						XSSFWorkbook selectedColumnExcelWorkbook = new XSSFWorkbook(selectedColumnExcelFileInputStream);
//
//			    		Assert.assertEquals(selectedColumnExcelWorkbook.getSheetName(0), "Data");
//
//			    		selectedColumnExcelFileInputStream.close();
//		    			} catch (IOException e) {
//		    				e.printStackTrace();
//		    				}
//
//		    		Logger.debug("Report Data with Selected Columns for -- " + entityReportName +" -- Downloaded and Moved to Project Successfully");
//		    		}	    			
//	    		// Click Download Graph with Data with All Columns
//	    		new Actions(driver).moveToElement(driver.findElement(By.id("downloadXls")).findElement(By.id("exportIconForReport"))).click().build().perform();
//	    		new Actions(driver).moveToElement(driver.findElement(By.xpath(".//*[@id='drop']/ul/li[2]"))).clickAndHold().build().perform();
//	    		driver.findElement(By.id("exportXL")).click();
//	    		//
//
//	    		if (driver.findElements(By.className("alertdialog-icon")).size()!=0)
//	    			driver.findElement(By.cssSelector("button.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only")).click();
//    			
//	    		if (downloadedExcelFileSrc.exists()) {
//	    			File allColumnExcelFolderDest = new File(System.getProperty("user.dir")+"//Downloaded Data//"+new SimpleDateFormat("dd_MMMM_yyyy").format(Calendar.getInstance().getTime()).toString() + "//Common Reports//All Columns//" + entityName);
//
//	    			File allColumnExcelFileDest = new File(allColumnExcelFolderDest + "//" + reportNameOnShowPage +".xlsx");
//		    			
//	    			if (allColumnExcelFileDest.exists())
//	    				allColumnExcelFileDest.delete();
//		    			
//		    		try {
//						FileUtils.moveFile(downloadedExcelFileSrc, allColumnExcelFileDest);
//
//			    		FileInputStream allColumnExcelFileInputStream = new FileInputStream(allColumnExcelFileDest);
//			    		@SuppressWarnings("resource")
//						XSSFWorkbook allColumnExcelWorkbook = new XSSFWorkbook(allColumnExcelFileInputStream);
//
//			    		Assert.assertEquals(allColumnExcelWorkbook.getSheetName(0), "Data");
//
//			    		allColumnExcelFileInputStream.close();
//		    			} catch (IOException e) {
//		    				e.printStackTrace();
//		    				}
//		    			
//		    		Logger.debug("Report Data with All Columns for -- " + entityReportName +" -- Downloaded and Moved to Project Successfully");
//	    			}
//    			}
//	    			
//	    	Logger.debug("Report Open and Download Test Case for -- " + entityReportName + " -- is Completed");
//	    	}		
    		Logger.debug("Report Open successfuly for -- " + entityReportName);
    	driver.get(CONFIG.getProperty("endUserURL"));}
	    
	
	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(common_report_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(common_report_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
			}
		else if (testResult.getStatus()==ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(common_report_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
			}
		try {
			for (int i = 2; i <= common_report_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = common_report_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
				if (!testCaseID.equalsIgnoreCase("")) {
					updateRun(convertStringToInteger(testCaseID), new java.lang.Exception().toString(), result);
				}
			}
		}catch(Exception e){
			Logger.debug(e);
		}
		}

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(common_report_suite_xls, "Test Cases", TestUtil.getRowNum(common_report_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(common_report_suite_xls, "Test Cases", TestUtil.getRowNum(common_report_suite_xls, this.getClass().getSimpleName()), "FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(common_report_suite_xls, this.getClass().getSimpleName());
	}
}