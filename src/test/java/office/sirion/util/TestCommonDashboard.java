package office.sirion.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import office.sirion.base.TestBase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class TestCommonDashboard extends TestBase {
	static LinkedList<String> columnList = new LinkedList<String>();
	static String[] str;
	static LinkedList<String> listColumnData;
	
	public static List<String> getChartList() {
		List<String> chartIDList = new ArrayList<String>();
		
		List<WebElement> deselectedChartsList = driver.findElement(By.className("chartSelectionBody")).findElement(By.tagName("ul")).findElements(By.tagName("li"));
		Iterator<WebElement> itrDeselectedChartsList = deselectedChartsList.iterator();

		while (itrDeselectedChartsList.hasNext()) {
			WebElement chartElement = itrDeselectedChartsList.next();
			
			String chartListID = chartElement.getAttribute("id");
			chartIDList.add(chartListID);
			}
		
		return chartIDList;
		}
	
	public static void deSelectChartList() {
		List<WebElement> selectedChartsList = driver.findElement(By.className("chartSelected")).findElement(By.tagName("ul")).findElements(By.tagName("li"));
		if (!selectedChartsList.isEmpty()) {
			Iterator<WebElement> itrSelectedChartsList = selectedChartsList.iterator();
			while (itrSelectedChartsList.hasNext()) {
				WebElement selectedChartElement = itrSelectedChartsList.next();
				if (selectedChartElement.findElement(By.tagName("input")).isSelected())
					selectedChartElement.findElement(By.tagName("input")).click();
				}
			}
		}
	
	public static int getFilterSize() {
		List<WebElement> filterList = driver.findElement(By.className("filterOl")).findElement(By.className("attr-content"))
											.findElements(By.cssSelector("div.left-filter.MSwidth"));
		return filterList.size();
		}

	public static String getFilterType(int filterIndex) {
		List<WebElement> filterList = driver.findElement(By.className("filterOl")).findElement(By.className("attr-content"))
											.findElements(By.cssSelector("div.left-filter.MSwidth"));
		String filterType;
		
		if (filterList.get(filterIndex).getAttribute("rel").equalsIgnoreCase("Stakeholders"))
			filterType = filterList.get(filterIndex).findElement(By.tagName("h3")).getAttribute("class");
		else
			filterType = filterList.get(filterIndex).findElement(By.tagName("div")).getAttribute("class");

		return filterType.trim();
		}

	public static String getFilterName(int filterIndex) {
		List<WebElement> filterList = driver.findElement(By.className("filterOl")).findElement(By.className("attr-content"))
											.findElements(By.cssSelector("div.left-filter.MSwidth"));
		
		String filterName = filterList.get(filterIndex).findElement(By.tagName("input")).getAttribute("value");
		return filterName.trim();
		}

	public static int getFilterOptionsCount (int filterIndex) {
		List<WebElement> filterList = driver.findElement(By.className("filterOl")).findElement(By.className("attr-content"))
											.findElements(By.cssSelector("div.left-filter.MSwidth"));
		
		List<WebElement> filterOptionsList = filterList.get(filterIndex).findElement(By.tagName("select")).findElements(By.tagName("option"));

		return filterOptionsList.size();
		}
	
	public static String[] selectFilterOptions (int filterIndex, String filterCount) throws InterruptedException {
		String[] optionsArray = null;

		List<WebElement> filterList = driver.findElement(By.className("filterOl")).findElement(By.className("attr-content"))
											.findElements(By.cssSelector("div.left-filter.MSwidth"));

		new Actions(driver).moveToElement(filterList.get(filterIndex)).build().perform();

		int optionsCount = getFilterOptionsCount(filterIndex);

		filterList.get(filterIndex).findElement(By.tagName("input")).click();
		filterList.get(filterIndex).findElement(By.tagName("button")).click();
		
		if (getFilterType(filterIndex).equalsIgnoreCase("selectExtended")) {
			if (optionsCount==0)
				return null;
			
			int selectedOptions = 0;
			if (optionsCount > Integer.valueOf(filterCount))
				selectedOptions = Integer.valueOf(filterCount);
			else
				selectedOptions = optionsCount;
			
			optionsArray = new String[selectedOptions];

			List<WebElement> elementOptionsWindowList = driver.findElements(By.cssSelector("div.ui-multiselect-menu.ui-widget.ui-widget-content.ui-corner-all"));
			Iterator<WebElement> itrElementOptionsWindowList = elementOptionsWindowList.iterator();
			
			WebElement filterOptionsView = null;
			while (itrElementOptionsWindowList.hasNext()) {
				filterOptionsView = itrElementOptionsWindowList.next();
				
				if (filterOptionsView.getAttribute("style").contains("display: block;"))
					break;
				}
			
			if (filterOptionsView==null)
				return optionsArray;
			
			filterOptionsView.findElement(By.cssSelector("div.ui-widget-header.ui-corner-all.ui-multiselect-header.ui-helper-clearfix"))
					.findElement(By.tagName("ul"))
					.findElement(By.className("ui-multiselect-none"))
					.click();

			List<WebElement> elementList = filterOptionsView.findElement(By.cssSelector("ul.ui-multiselect-checkboxes.ui-helper-reset")).findElements(By.tagName("li"));
			List<WebElement> elementOptionsList = new ArrayList<WebElement>();
			for (WebElement elementOptions : elementList) {
				if (elementOptions.getAttribute("class").equalsIgnoreCase(" attributeCheckbox dataValues"))
					elementOptionsList.add(elementOptions);
				}

			for (int filterOptions = 0; filterOptions < selectedOptions; filterOptions++) {
				if (!elementOptionsList.get(filterOptions).findElement(By.tagName("label")).findElement(By.tagName("input")).isSelected())
					elementOptionsList.get(filterOptions).findElement(By.tagName("label")).findElement(By.tagName("input")).click();
				
				optionsArray[filterOptions] = elementOptionsList.get(filterOptions).findElement(By.tagName("label")).findElement(By.tagName("span")).getText();
				}

			filterList.get(filterIndex).findElement(By.tagName("button")).click();
			Thread.sleep(2000);
			
			return optionsArray;
			}

		return optionsArray;
		}
	
	public static WebElement getBlockElement(String chartType) {

		if (chartType.equalsIgnoreCase("")) {
			WebElement elementSVG = driver.findElement(By.id("chartContainerOverlay")).findElement(By.tagName("svg"));
			List<WebElement> gTagsList = elementSVG.findElements(By.tagName("g"));
			Iterator<WebElement> iteratorTags = gTagsList.iterator();

			String hotClass = null;
			while (iteratorTags.hasNext()) {
				WebElement gElement = iteratorTags.next();
				if (gElement.getAttribute("class").contains("-hot")) {
					hotClass = gElement.getAttribute("class");
					break;
					}
				}

			WebElement rectBeforeElement = elementSVG.findElement(By.className(hotClass));
			List<WebElement> gTagsBeforeList = rectBeforeElement.findElements(By.tagName("g"));
			Iterator<WebElement> iteratorBeforeTags = gTagsBeforeList.iterator();
			
			String colHotClass = null;
			while (iteratorBeforeTags.hasNext()) {
				WebElement gRectElement = iteratorBeforeTags.next();
				if (gRectElement.getAttribute("class").contains("-col-hot")) {
					colHotClass = gRectElement.getAttribute("class");
					break;
					}
				}

			WebElement rectElement = elementSVG.findElement(By.className(hotClass)).findElement(By.className(colHotClass));
			return rectElement;
			}
		
		else if (chartType.equalsIgnoreCase("Doughnut Chart")) {
			WebElement elementSVG = driver.findElement(By.id("chartContainerOverlay")).findElement(By.tagName("svg"));
			List<WebElement> gTagsList = elementSVG.findElements(By.tagName("g"));
			Iterator<WebElement> iteratorTags = gTagsList.iterator();

			String dataClass = null;
			while (iteratorTags.hasNext()) {
				WebElement gElement = iteratorTags.next();
				if (gElement.getAttribute("class").contains("-dataset")) {
					dataClass = gElement.getAttribute("class");
					break;
					}
				}

			WebElement pathBeforeElement = elementSVG.findElement(By.className(dataClass));

			return pathBeforeElement;
			}
		
		return null;
		}
	
	public static int getFilterColumnNumber(String str) {
		List<WebElement> rowHeader = driver.findElement(By.className("tabularDataDiv")).findElement(By.className("dataTables_scrollHead"))
				.findElement(By.tagName("tr")).findElements(By.tagName("th"));

		try {
			for (int m = 0; m < rowHeader.size(); m++) {
				Thread.sleep(1000);
				new Actions(driver).moveToElement(rowHeader.get(m)).perform();
				if (rowHeader.get(m).getText().equalsIgnoreCase(str)) {
					Logger.debug("Column Name for " + str	+ " -- is Present at Index -- " + m);
					return m;
					}
			}
		} catch (Exception e) {
			Logger.debug("There is such Column Name as -- " + str);
			return -1;
		}
		return -1;
	}

	public static LinkedList<String> getListData(int columnNumber) {
		WebElement listTable = driver.findElement(By.className("tabularDataDiv")).findElement(By.className("dataTables_scrollBody")).findElement(By.tagName("tbody"));
		List<WebElement> rows = listTable.findElements(By.tagName("tr"));
		Iterator<WebElement> i = rows.iterator();
		
		while (i.hasNext()) {
			WebElement row = i.next();
			List<WebElement> columns = row.findElements(By.tagName("td"));
			Iterator<WebElement> j = columns.iterator();
			
			int count = 0;
			String columnValue;
			
			while (j.hasNext()) {
				WebElement column = j.next();
				if (count == columnNumber) {
					columnValue = column.getText();
					columnList.add(columnValue);
					break;
					}
				count++;
				}
			}
		return columnList;
		}
	
	public static LinkedList<String> getColumnData(int columnNumber) throws InterruptedException {
		WebElement e;
		
		do {
			listColumnData = getListData(columnNumber);
			
			e = driver.findElement(By.id("showDataTable_paginate")).findElement(By.id("showDataTable_next"));
			str = e.getAttribute("class").split(" ");
			
			if (str.length == 2) {
				e.click();
			    //
				fluentWaitMethod(driver.findElement(By.className("tabularDataDiv")).findElement(By.className("dataTables_scrollBody")));
				}
			} while (str.length == 2);

		return listColumnData;
		}
}