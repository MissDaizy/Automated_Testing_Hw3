package hw3_package;

import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.Before;
import org.junit.After;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.JavascriptExecutor;
import java.util.*;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Sheet;


import org.apache.logging.log4j.*;

//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DeleteItemFromCart {

	public WebDriver driver;
	public Map<String, Object> vars;
	Logger logger = LogManager.getLogger(SearchProduct.class);
	JavascriptExecutor js;

	Sheet thsSheet;
	private WebElement removeButton;


	@Before
	public void setUp() throws IOException {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\rivka\\Downloads\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();
		js = (JavascriptExecutor) driver;
		vars = new HashMap<String, Object>();

		ReadExcl objExcelFile = new ReadExcl();

		objExcelFile.readExcel("exlFiles", "del.xls", "sheet1");
		thsSheet = ReadExcl.getsheet();
		driver.get("https://atid.store/");
		driver.manage().window().maximize();
	}

	@After
	public void tearDown() {
		driver.quit();
	}

	@Test
	public void simple() {

		int rowCount = ReadExcl.getRowcount();

		int testCaseNum = 1;

		for (int i = 0; i < rowCount + 1; i++) {

			Row row = thsSheet.getRow(i);
			logger.debug(row.toString());

			// Create a loop to print cell values in a row

			for (int j = 0; j < row.getLastCellNum(); j++) {
				// Click on open store
				driver.findElement(By.xpath("//*[@id=\"menu-item-45\"]/a")).click();
				// Click on item in store
				driver.findElement(By.xpath("//*[@id=\"main\"]/div/ul/li[1]")).click();

				// Edit number of items in cart
				driver.findElement(By.xpath("//*[@id=\"quantity_6277a5bc8e7ce\"]")).click();

				driver.findElement(By.xpath("//*[@id=\"quantity_6277a5bc8e7ce\"]")).clear();

				if (row.getCell(j).getCellType().name().equals("String"))
					driver.findElement(By.xpath("//*[@id=\"quantity_6277a5bc8e7ce\"]"))
							.sendKeys((row.getCell(j).getStringCellValue()));

				else {
					driver.findElement(By.xpath("//*[@id=\"quantity_6277a5bc8e7ce\"]"))
							.sendKeys(new DataFormatter().formatCellValue(row.getCell(j)));
				}

				// Click on Add To Cart
				driver.findElement(By.xpath("//*[@id=\"product-160\"]/div[2]/form/button")).click();

				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Click on VIEW CART
				driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[1]/div/a")).click();
				
				// Click on REMOVE
				removeButton = driver.findElement(By.xpath("//*[@id=\"post-39\"]/div/div/div/section[2]/div/div/div/div/div/div/div/form/table/tbody/tr[1]/td[1]/a"));
				driver.findElement(By.xpath("//*[@id=\"post-39\"]/div/div/div/section[2]/div/div/div/div/div/div/div/form/table/tbody/tr[1]/td[1]/a")).click();				
				
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				testCase(testCaseNum);
				testCaseNum++;
			}
		}		

		testCase(testCaseNum);

	}

	private void testCase(int testCaseNum) {
		switch (testCaseNum) {
		case 1:
			itemSuccessfullyDeleted(testCaseNum);
			break;
		case 2:
			deleteWhenEmpty(testCaseNum);
			break;

		default:
			break;
		}
	}

	private void deleteWhenEmpty(int testCaseNum) {
		// Click on item in store
		driver.findElement(By.xpath("//*[@id=\"menu-item-45\"]/a")).click();
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Click on VIEW CART
		driver.findElement(By.xpath("//*[@id=\"ast-site-header-cart\"]/div[1]/a/span")).click();

		String message = driver.findElement(By.xpath("//*[@id=\"post-39\"]/div/div/div/section[2]/div/div/div/div/div/div/div/p[1]")).getText().toString();

		//if the cart is empty
		if (message.contains("Your cart is currently empty")) {

			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			
			//if remove button exists
			if (driver.findElements(By.id("//*[@id=\"post-39\"]/div/div/div/section[2]/div/div/div/div/div/div/div/form/table/tbody/tr[1]/td[1]/a" ))
					.size() != 0) {
				logger.debug("Test number " + testCaseNum + " has not passed");
			} else {
				logger.debug("Test number " + testCaseNum + " has passed");
			}		
		}
	}
				
	
	private void itemSuccessfullyDeleted(int testCaseNum) {
		String message = driver.findElement(By.xpath("//*[@id=\"post-39\"]/div/div/div/section[2]/div/div/div/div/div/div/div/div/p")).getText().toString();

		if (message.contains("Your cart is currently empty")) {
			logger.debug("Test number " + testCaseNum + " has passed");
		}

		else {
			logger.debug("Test number " + testCaseNum + " has not passed");
		}
	}


	public static void main(String args[]) {
		JUnitCore junit = new JUnitCore();
		junit.addListener(new TextListener(System.out));
		org.junit.runner.Result result = junit.run(AddingItemToCart.class); // Replace "SampleTest" with the name of your
																			// class
		if (result.getFailureCount() > 0) {
			System.out.println("Test failed.");
			System.exit(1);
		} else {
			System.out.println("Test finished successfully.");
			System.exit(0);
		}
	}

}
