package office.sirion.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import office.sirion.base.TestBase;

import org.apache.commons.collections4.CollectionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

public class TestSorting extends TestBase {
	static LinkedList<String> columnList = new LinkedList<String>();
	static String[] str;
	static LinkedList<String> listColumnData;
	
	public static LinkedList<String> getListData(int columnNumber) {
		WebElement listTable = driver.findElement(By.className("dataTables_scrollBody")).findElement(By.tagName("tbody"));
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
			listColumnData = TestSorting.getListData(columnNumber);
			
			e = driver.findElement(By.id("cr_paginate")).findElement(By.id("cr_next"));
			str = e.getAttribute("class").split(" ");
			
			if (str.length == 2) {
				Thread.sleep(3500);
				e.click();
			    //
				fluentWaitMethod(driver.findElement(By.className("dataTables_scrollBody")));
				}
			} while (str.length == 2);

		return listColumnData;
		}
	
	public static boolean isTableColumnSorted(LinkedList<String> expected, LinkedList<String> actual) {
		boolean result = false;
		
		Collections.sort(expected, String.CASE_INSENSITIVE_ORDER);
		if (CollectionUtils.isEqualCollection(expected, actual))
			result = true;
		
		return result;
		}
	
	public static boolean isListingEmpty() throws InterruptedException {
	    WebElement listTable = driver.findElement(By.className("dataTables_scrollBody")).findElement(By.tagName("tbody"));
	    List<WebElement> rows = listTable.findElements(By.tagName("tr"));
	    
	    if (rows.size()==1) {
	    	if (rows.get(0).findElement(By.tagName("td")).getAttribute("class").equalsIgnoreCase("dataTables_empty")) {
		        Assert.assertEquals(rows.get(0).findElement(By.tagName("td")).getText(), "No corresponding data available");
	    		return true;
	    		}
	    	else
	    		return false;
	    	}
	    
		return false;
		}
	}