package office.sirion.suite.common.dashboard;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import office.sirion.util.TestCommonDashboard;
import office.sirion.util.TestUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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

public class CommonModernDashboardFilter extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean isTestPass = true;

	@BeforeTest
	public void checkTestSkip() {
		if (!TestUtil.isTestCaseRunnable(common_dashboard_suite_xls, this
				.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case "
					+ this.getClass().getSimpleName() + " as Runmode set to NO");
			throw new SkipException("Skipping Test Case "
					+ this.getClass().getSimpleName() + " as Runmode set to NO");
		}
		runmodes = TestUtil.getDataSetRunmodes(common_dashboard_suite_xls, this
				.getClass().getSimpleName());
	}

	@Test(dataProvider = "getTestData")
	public void CommonModernDashboardOpenTest(String dashboardID,
			String dashboardType) throws InterruptedException, IOException {

		count++;
		if (!runmodes[count].equalsIgnoreCase("Y"))
			throw new SkipException("Runmode for Test Data is set to NO "
					+ count);

		// Launching Browser
		openBrowser();
		endUserLogin(CONFIG.getProperty("endUserURL"),
				CONFIG.getProperty("endUserUsername"),
				CONFIG.getProperty("endUserPassword"));

		Logger.debug("Executing Test Case Modern Dashboard Filter with ID --- "
				+ dashboardID);

		driver.get(CONFIG.getProperty("endUserURL"));
		// Wait For Page To Get Load
		waitF.until(ExpectedConditions.visibilityOf(driver.findElement(By
				.id("globalView"))));


		// Wait For and Click "Select Charts" icon
		fluentWaitMethod(driver
				.findElement(By.id("priorityListModernView")));
		driver.findElement(By.id("priorityListModernView")).click();

		// Uncheck Selected Charts
		TestCommonDashboard.deSelectChartList();

		// Get List of All Charts
		List<String> chartListID = TestCommonDashboard.getChartList();
		Iterator<String> itrchartListID = chartListID.iterator();

		// Iterator for All Charts List
		while (itrchartListID.hasNext()) {
			String chartID = itrchartListID.next();

			// Check for ChartID with the Given Dashboard ID
			if (chartID.equalsIgnoreCase(dashboardID)) {
				// Get and Select Dashboard by Name
				String selectedDashboardName = driver
						.findElement(By.className("chartSelectionBody"))
						.findElement(By.id(chartID)).getText();
				driver.findElement(By.className("chartSelectionBody"))
						.findElement(By.id(chartID))
						.findElement(By.className("chartListClass")).click();

				// Click "Apply and Save" button
				driver.findElement(By.className("chartSelectionHeader"))
						.findElement(By.id("savePreferences")).click();


				// Take Screenshot after Loading the Dashboard
				try {
					takeScreenShotAs(this.getClass().getSimpleName(),
							selectedDashboardName.toUpperCase());
				} catch (IOException e) {
					e.printStackTrace();
				}

				// Assertion for Dashboard Name on Show Page
				Assert.assertEquals(
						driver.findElement(By.id("chartContainerTable"))
								.findElement(By.className("chartTitle"))
								.getText().toUpperCase(),
						selectedDashboardName.toUpperCase());

				// Check for More Options
				if (driver.findElement(By.className("c-subHead"))
						.findElements(By.className("moreOptions")).size() == 0) {
					Logger.debug("For -- "
							+ selectedDashboardName.toUpperCase()
							+ " -- There is No data to display");
					return;
				}

				// Click "Analyze" Link
				new Actions(driver)
						.moveToElement(
								driver.findElement(By.className("c-subHead")))
						.clickAndHold().build().perform();
				driver.findElement(By.linkText("Analyze")).click();

				fluentWaitMethod(driver
						.findElement(By.id("chartContainerOverlay")));

				// Click "Filter" icon
				driver.findElement(By.cssSelector("li.cDrill.selectClass"))
						.click();
				Thread.sleep(2000);
				fluentWaitMethod(driver
						.findElement(By.className("filterOl")).findElement(
								By.className("attr-content")));

				// Gets Filters Count
				int filtersCount = TestCommonDashboard.getFilterSize();
				Logger.debug("For -- " + selectedDashboardName.toUpperCase()
						+ " -- Filters Count is -- " + filtersCount);

				// Check For Filters Count
				if (filtersCount == 0) {
					org.testng.Assert.fail("Filters Count is --- "
							+ filtersCount);

					driver.get(CONFIG.getProperty("endUserURL"));
					return;
				}

				// Loop For Filters
				for (int filterIndex = 1; filterIndex < filtersCount; filterIndex++) {
					// Gets Filter Name by Index
					String filterName = TestCommonDashboard
							.getFilterName(filterIndex);

					// Gets Filter Type by Index
					String filterType = TestCommonDashboard
							.getFilterType(filterIndex);

					// Check For Filter Type
					if (!(filterType.equalsIgnoreCase("selectExtended"))
							&& !(filterType.equalsIgnoreCase("daterange")))
						continue;

					// Select Filter Options
					Logger.info("Executing Filter For -- " + filterName);
					String[] selectedFilterOptionsArray = TestCommonDashboard
							.selectFilterOptions(filterIndex,
									CONFIG.getProperty("filterOptionsCount"));

					// Click "GO" button
					driver.findElement(By.className("filterOl"))
							.findElement(By.className("filterhead"))
							.findElement(By.id("applyAttributesAndSplit"))
							.click();


					// Gets Block Element Area
					WebElement blockElement = TestCommonDashboard
							.getBlockElement(dashboardType);

					List<WebElement> blockElementsList = null;
					if (dashboardType.equalsIgnoreCase("Stacked Column Chart")) {
						// Gets Block Elements for Stacked ChartChild
						blockElementsList = blockElement.findElements(By
								.tagName("rect"));
						if (blockElementsList.isEmpty()) {
							driver.findElement(By.className("filterOl"))
									.findElement(By.className("filterhead"))
									.findElement(By.id("resetAttr")).click();


							continue;
						}
					}

					if (dashboardType.equalsIgnoreCase("Doughnut Chart")) {
						// Gets Block Elements for Doughnut Chart
						blockElementsList = blockElement.findElements(By
								.tagName("path"));
						if (blockElementsList.isEmpty()) {
							driver.findElement(By.className("filterOl"))
									.findElement(By.className("filterhead"))
									.findElement(By.id("resetAttr")).click();


							continue;
						}
					}

					// Click First Block in the Area
					new Actions(driver).moveToElement(blockElementsList.get(0))
							.click().build().perform();


					new Actions(driver)
							.moveToElement(
									driver.findElement(By
											.className("tabularDataDiv")))
							.build().perform();

					// Gets Column Number of Filter Name under Tabular List
					int columnNumber = TestCommonDashboard
							.getFilterColumnNumber(filterName);

					// Check for Column Number
					if (columnNumber == -1) {
						Logger.error("There is No such Column as --- "
								+ filterName);

						driver.findElement(By.className("filterOl"))
								.findElement(By.className("filterhead"))
								.findElement(By.id("resetAttr")).click();


						continue;
					}

					// Gets List of Entries under Column
					LinkedList<String> listActual = TestCommonDashboard
							.getColumnData(columnNumber);

					int failCount = 0;
					int resultCount = 0;
					if (filterType.equalsIgnoreCase("selectExtended")) {
						for (int jCount = 0; jCount < listActual.size(); jCount++) {
							for (int kCount = 0; kCount < selectedFilterOptionsArray.length; kCount++) {
								if (listActual.get(jCount).contains(
										selectedFilterOptionsArray[kCount])) {
									failCount = 0;
									break;
								} else
									failCount++;
							}
							resultCount = resultCount + failCount;
						}
					}

					listActual.clear();

					driver.findElement(By.className("filterOl"))
							.findElement(By.className("filterhead"))
							.findElement(By.id("resetAttr")).click();

				}
			}
		}

		Logger.debug("Execution of Dashboard Filter is Completed");
		driver.get(CONFIG.getProperty("endUserURL"));
	}

	@AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass()
				.getSimpleName());

		if (testResult.getStatus() == ITestResult.SKIP)
			TestUtil.reportDataSetResult(common_dashboard_suite_xls, this
					.getClass().getSimpleName(), count + 2, "SKIP");

		else if (testResult.getStatus() == ITestResult.FAILURE) {
			isTestPass = false;
			TestUtil.reportDataSetResult(common_dashboard_suite_xls, this
					.getClass().getSimpleName(), count + 2, "FAIL");
			result = "Fail";
		}

		else if (testResult.getStatus() == ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(common_dashboard_suite_xls, this
					.getClass().getSimpleName(), count + 2, "PASS");
			result = "Pass";
		}
	}

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(common_dashboard_suite_xls,
					"Test Cases", TestUtil.getRowNum(
							common_dashboard_suite_xls, this.getClass()
									.getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(common_dashboard_suite_xls,
					"Test Cases", TestUtil.getRowNum(
							common_dashboard_suite_xls, this.getClass()
									.getSimpleName()), "FAIL");
	}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(common_dashboard_suite_xls, this.getClass()
				.getSimpleName());
	}
}