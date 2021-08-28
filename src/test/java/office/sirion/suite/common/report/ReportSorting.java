package office.sirion.suite.common.report;

import java.util.LinkedList;
import java.util.List;

import office.sirion.util.TestSorting;
import office.sirion.util.TestUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import testlink.api.java.client.TestLinkAPIException;

public class ReportSorting extends TestSuiteBase {
  String result = null;
  String runmodes[] = null;
  static int count = -1;
  static boolean fail = true;
  static boolean skip = false;
  static boolean isTestPass = true;
  static LinkedList<String> listExpected;
  static LinkedList<String> listActual;
  static int resultCount = 0;

  @BeforeTest
  public void checkTestSkip() {
    if (!TestUtil.isTestCaseRunnable(common_report_suite_xls, this.getClass().getSimpleName())) {
      Logger.debug("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
      throw new SkipException("Skipping Test Case " + this.getClass().getSimpleName() + " as Runmode set to NO");
    }
    runmodes = TestUtil.getDataSetRunmodes(common_report_suite_xls, this.getClass().getSimpleName());
  }

  @Test(dataProvider = "getTestData")
  public void ListingSortTest(String testCaseId, String entityName, String entityReportName, String entityReportShowPage) throws InterruptedException,
      TestLinkAPIException {
    count++;
    if (!runmodes[count].equalsIgnoreCase("Y")) {
      skip = true;
      throw new SkipException("Runmode for Test Data -- " + entityName + " set to NO " + count);
    }

    // Launch The Browser
    openBrowser();
    endUserLogin(CONFIG.getProperty("endUserURL"), CONFIG.getProperty("endUserUsername"), CONFIG.getProperty("endUserPassword"));

    driver.get(CONFIG.getProperty("endUserURL"));
    //
//    wait_in_report.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.linkText("Reports"))));

    driver.findElement(By.linkText("Reports")).click();
    //
//    wait_in_report.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("allLists1"))));

    new Select(driver.findElement(By.id("allLists1"))).selectByVisibleText(entityName);
    //
//    wait_in_report.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("allLists2"))));

    new Select(driver.findElement(By.id("allLists2"))).selectByVisibleText(entityReportName);
    //
//    wait_in_report.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("downloadXls"))));

    String reportNameShowPage = driver.findElement(By.id("downloadXls")).getText();
    System.out.println("Report Name:- " + reportNameShowPage);
    Assert.assertEquals(reportNameShowPage, entityReportShowPage, "Report Name is -- " + reportNameShowPage + " instead of -- " + entityReportShowPage);

    driver.findElement(By.cssSelector("div.ColVis.TableTools")).findElement(By.cssSelector("button.ColVis_Button.TableTools_Button.ColVis_MasterButton"))
        .click();

    int columnCount = driver.findElement(By.cssSelector("div.ColVis_collection.TableTools_collection"))
        .findElements(By.cssSelector("button.ColVis_Button.TableTools_Button")).size() - 2;

    driver.findElement(By.cssSelector("div.ColVis_collection.TableTools_collection"))
        .findElement(By.cssSelector("button.ColVis_Button.TableTools_Button.ColVis_ShowAll")).click();
    //

    new Select(driver.findElement(By.name("cr_length"))).selectByValue("100");
    //

    for (int i = 0; i < columnCount-1; i++) {
      listExpected = TestSorting.getColumnData(i);

      WebElement e = driver.findElement(By.id("cr_paginate")).findElement(By.id("cr_first"));
      String[] str = e.getAttribute("class").split(" ");
      if (str.length == 2) {
        e.click();
        //
      }

      List<WebElement> rowHeader = driver.findElement(By.className("dataTables_scrollHead")).findElement(By.tagName("tr")).findElements(By.tagName("th"));
      ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", rowHeader.get(i));
      rowHeader.get(i).click();
      //

      listActual = TestSorting.getColumnData(i);

      boolean result = TestSorting.isTableColumnSorted(listExpected, listActual);
      String resultString = String.valueOf(result).toUpperCase();
      String[] columnHeader = rowHeader.get(i).getAttribute("aria-label").split(":");

      System.out.println("Column Header Named --- " + columnHeader[0] + " ---- " + resultString);

      if (!result)
        resultCount++;

      listExpected.clear();
      listActual.clear();
    }

    if (resultCount > 0)
      fail = true;
    else
      fail = false;

    driver.get(CONFIG.getProperty("endUserURL"));
  }

  @AfterMethod
  public void reportDataSetResult() {
    if (skip)
      TestUtil.reportDataSetResult(common_report_suite_xls, this.getClass().getSimpleName(), count + 2, "SKIP");
    else if (fail) {
      isTestPass = false;
      TestUtil.reportDataSetResult(common_report_suite_xls, this.getClass().getSimpleName(), count + 2, "FAIL");
    } else
      TestUtil.reportDataSetResult(common_report_suite_xls, this.getClass().getSimpleName(), count + 2, "PASS");

    skip = false;
    fail = true;
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