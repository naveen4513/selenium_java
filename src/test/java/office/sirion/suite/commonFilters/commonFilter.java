package office.sirion.suite.commonFilters;

import office.sirion.util.CommonTestFiltering;
import office.sirion.util.TestSorting;
import office.sirion.util.TestUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class commonFilter extends TestSuiteBase {
	String result = null;
	String runmodes[] = null;
	static int count = -1;
	static boolean fail = true;
	static boolean skip = false;
	static boolean isTestPass = true;
    static LinkedList<String> listExpected;
    int resultCount;
    int filterTestStatus;
    public static List<WebElement> filtersContent;
    public static List<String> filtername=new ArrayList<>();
    public static String filterType;
    public static String[] filterOptions;

	
	@BeforeTest
	public void checkTestSkip() throws InterruptedException {
		if (!TestUtil.isTestCaseRunnable(filters_suite_xls, this.getClass().getSimpleName())) {
			Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as runmode set to NO");
			}
		runmodes = TestUtil.getDataSetRunmodes(filters_suite_xls, this.getClass().getSimpleName());
	}

	@Test(dataProvider = "getTestData")
	public void commonFilterTest (String listName, String filternames) throws InterruptedException {
		
		count++;
		if (!runmodes[count].equalsIgnoreCase("Y")) {
			skip = true;
			throw new SkipException("Runmode for Test Data Set to NO -- " + count);
			}
		
		openBrowser();
	    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

       // driver.get(CONFIG.getProperty("endUserURL"));

        Thread.sleep(5000);

        try {
            if (driver.findElement(By.xpath("//div[@id='wm-shoutout-118767']")).isDisplayed()) {
                locateElementBy("cf_walkme_popup").click();
            }
        }catch (NoSuchElementException e){
            System.out.println("no such element: Unable to locate element: {method:xpath,selector://div[@id='wm-shoutout-118767']}");
        }

	    fluentWaitMethod(driver.findElement(By.xpath("//*[@id='quick-nav']/div/ul[2]")));

		WebElement we = driver.findElement(By.xpath("//*[@id='quick-nav']/div/ul[2]"));

		Actions ac = new Actions(driver);
		ac.moveToElement(we).build().perform();

		/*if(listName.equalsIgnoreCase("")||listName.equalsIgnoreCase("")||listName.equalsIgnoreCase("")){

        }
*/
		locateElementBy("group_quick_link").click();
		locateElementBy("actions_quick_link").click();
		locateElementBy("cf_search_box").click();
        locateElementBy("cf_filter_icon").click();
        locateElementBy("cf_listing_page_title");
        Logger.debug("Listing Page of -- Actions -- opened Successfully");

        String listingNameShowPage = locateElementBy("cf_listing_name").getText();
        Logger.info("Listing Show Page Name is -- " +listingNameShowPage);

        // Check for Listing Data
        if (CommonTestFiltering.checkTableDataPresence()) {
            Logger.info("For Actions -- Listing there is No Corresponding Data Available");

            driver.get(CONFIG.getProperty("endUserURL"));
            return;
        }

        int filtersCount = CommonTestFiltering.getFilterCount();
        Logger.info("Total Filters Count is = " +filtersCount);

        //System.out.println(CommonTestFiltering.getFiltername());



        // Check for Filters Count
        if (filtersCount==0) {
            org.testng.Assert.fail("For Actions -- Listing there are No Corresponding Filters Available");

            driver.get(CONFIG.getProperty("endUserURL"));
            return;
        }

        // Loop for Filters


        String[] name =filternames.split(";");

        for(String filter: name){
            filtername.add(filter);
        }

        for (String names:filtername) {
            // Clicking on Filter Button
            int filterIndex = CommonTestFiltering.getFiltername().indexOf(names);
            driver.findElement(By.className("listingPanelLeft")).findElement(By.className("filterToggle")).click();
            Thread.sleep(2000);

            filtersContent = driver.findElement(By.id("filterDiv"))
                    .findElement(By.id("filter"))
                    .findElement(By.className("filter-content"))
                    .findElements(By.className("filters-new"));

            //String filterName = CommonTestFiltering.getFiltername(filterIndex,filtersContent);
            System.out.println("Filters Name at Index -- " + filterIndex + " -- is = " + names);

            filterType = CommonTestFiltering.getFilterType(filterIndex);
            System.out.println("Filters Type at Index -- " + filterIndex + " -- is = " + filterType);

            // Check For Filter Type
            if (!(filterType.equalsIgnoreCase("multi-sel ng-scope ng-isolate-scope")) && !(filterType.equalsIgnoreCase("daterange sirion2 ng-scope"))) {
                driver.findElement(By.className("listingPanelLeft")).findElement(By.cssSelector("li.filterToggle.selected")).click();
                Thread.sleep(2000);

            } else if ((filterType.equalsIgnoreCase("daterange sirion2 ng-scope"))) {
                driver.findElement(By.className("listingPanelLeft")).findElement(By.cssSelector("li.filterToggle.selected")).click();
                Thread.sleep(2000);

            } else if (filterType.equalsIgnoreCase("multi-sel ng-scope ng-isolate-scope")) {
                driver.findElement(By.className("listingPanelLeft")).findElement(By.cssSelector("li.filterToggle.selected")).click();
                Thread.sleep(2000);

            }

            // Selection of Filter Options
            filterOptions = CommonTestFiltering.selectFilterOptions(filterIndex, convertStringToInteger(CONFIG.getProperty("filterOptionsCount")), filterType);

            if (filterOptions == null) {
                Logger.info("For Filter -- " + names + " -- Selected Options List is EMPTY");

                driver.navigate().refresh();
                //
                fluentWaitMethod(driver.findElement(By.className("dataTables_scrollBody")));

                continue;
            }

        }

            // Clicking on Filter Button
            driver.findElement(By.id("filterDiv")).findElement(By.id("filter"))
                    .findElement(By.className("filterhead")).findElement(By.cssSelector("input.filter.filter-buttons")).click();
            //

            //driver.findElement(By.className("listingPanelLeft")).findElement(By.cssSelector("li.filterToggle.selected")).click();
            //

            // Check for Listing Data
            if (CommonTestFiltering.checkTableDataPresence()) {
                Logger.info("For Filter -- " + filtername + " -- There is no Corresponding Data Available");

                driver.navigate().refresh();
                //
                fluentWaitMethod(driver.findElement(By.className("dataTables_scrollBody")));

            }

            driver.findElement(By.className("listingPanelLeft")).findElement(By.className("columnsGroupButton")).click();
            Thread.sleep(2000);

            for(String names:filtername) {
                try {
                    if (driver.findElements(By.cssSelector("li.ng-scope.selected_item")).size() != 0) ;
                    List<WebElement> element = driver.findElements(By.cssSelector("li.ng-scope.selected_item"));
                    List<WebElement> elementOptionsList = new ArrayList<WebElement>();
                    for ( WebElement elementOptions : element ) {
                        elementOptions.findElement(By.tagName("label")).findElement(By.tagName("input")).click();
                    }

                    List<WebElement> element1 = driver.findElements(By.cssSelector("div.listColumns>ul>li"));

                    for ( WebElement elementOptions : element1 ) {
                        //String[] name = filterName.split("\n");
                        if (elementOptions.findElement(By.tagName("label")).getText().toLowerCase().equalsIgnoreCase(names.toLowerCase())) {
                            elementOptions.findElement(By.tagName("label")).findElement(By.tagName("input")).click();
                            break;
                        }
                    }

                    driver.findElement(By.cssSelector("a.marginLeft15.positive.btn.floatNone.sirion-button.ng-binding")).click();

                    listExpected = TestSorting.getColumnData(CommonTestFiltering.getFilterColumnNumber(names.toLowerCase()));

                    int failCount = 0;
                    if (filterType.equalsIgnoreCase("multi-sel ng-scope ng-isolate-scope")) {
                        for (int jCount=0; jCount<listExpected.size(); jCount++) {
                            for (int kCount=0; kCount<filterOptions.length; kCount++) {
                                if (listExpected.get(jCount).contains(filterOptions[kCount])) {
                                    failCount=0;
                                    break;
                                }
                                else
                                    failCount++;
                            }
                            resultCount = resultCount+failCount;
                        }

                    }

                    else if (filterType.equalsIgnoreCase("daterange sirion2 ng-scope")) {
                        for (int jCount=0; jCount<listExpected.size(); jCount++) {
                            if (CommonTestFiltering.checkDateValidator(listExpected.get(jCount), filterOptions[0], filterOptions[1]))
                                failCount=0;
                            else
                                failCount++;
                            resultCount = resultCount+failCount;
                        }
                    }

                    if (resultCount>0) {
                        Logger.error("For Actions -- Filter -- " +names+ " -- is not working");
                        filterTestStatus = filterTestStatus+resultCount;
                        resultCount=0;
                        break;
                    }
                    else
                        Logger.debug("For Actions -- Filter Status for -- " + names + " -- TRUE");

                    listExpected.clear();

                    driver.navigate().refresh();
                    //
                    fluentWaitMethod(driver.findElement(By.className("dataTables_scrollBody")));

                } catch (NoSuchElementException e) {

                }
            }

           // new Select(locateElementBy("entity_listing_page_display_dropdown_link")).selectByVisibleText(CONFIG.getProperty("maxEntityListingDropdown"));
            //




        if (filterTestStatus>0)
            org.testng.Assert.fail("For Actions --- Filter is not working");
        else
            Logger.debug("For Actions -- Filter is Working as Expected");

        driver.get(CONFIG.getProperty("endUserURL"));
	    }



    @AfterMethod
	public void reportDataSetResult(ITestResult testResult) throws IOException {
		takeScreenShotOnFailure(testResult.getStatus(), this.getClass().getSimpleName());

		if(testResult.getStatus()==ITestResult.SKIP)
			TestUtil.reportDataSetResult(filters_suite_xls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(testResult.getStatus()==ITestResult.FAILURE) {
			isTestPass=false;
			TestUtil.reportDataSetResult(filters_suite_xls, this.getClass().getSimpleName(), count+2, "FAIL");
			result= "Fail";
		}
		else if (testResult.getStatus()== ITestResult.SUCCESS) {
			TestUtil.reportDataSetResult(filters_suite_xls, this.getClass().getSimpleName(), count+2, "PASS");
			result= "Pass";
		}
		try {
			for (int i = 2; i <= filters_suite_xls.getRowCount(this.getClass().getSimpleName()); i++) {
				String testCaseID = filters_suite_xls.getCellData(this.getClass().getSimpleName(), "testCaseID", i);
				if (!testCaseID.equalsIgnoreCase("")) {
					updateRun(convertStringToInteger(testCaseID), new Exception().toString(), result);
				}
			}
		}catch(Exception e){
			Logger.debug(e);
		}
	}

	@AfterTest
	public void reportTestResult() {
		if (isTestPass)
			TestUtil.reportDataSetResult(filters_suite_xls, "Test Cases", TestUtil.getRowNum(filters_suite_xls, this.getClass().getSimpleName()), "PASS");
		else
			TestUtil.reportDataSetResult(filters_suite_xls, "Test Cases", TestUtil.getRowNum(filters_suite_xls, this.getClass().getSimpleName()), "FAIL");
		}

	@DataProvider
	public Object[][] getTestData() {
		return TestUtil.getData(filters_suite_xls, this.getClass().getSimpleName());
		}
	}
