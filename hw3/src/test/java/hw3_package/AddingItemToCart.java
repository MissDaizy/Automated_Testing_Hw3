package hw3_package;

import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.naming.spi.DirStateFactory.Result;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.File;

import java.io.FileInputStream;

import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.Workbook;

import org.apache.logging.log4j.*;

//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AddingItemToCart {

	public WebDriver driver;
	public Map<String, Object> vars;
	Logger logger = LogManager.getLogger(SearchProduct.class);
	JavascriptExecutor js;

	Sheet thsSheet;

	@Before
	public void setUp() throws IOException {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\diana\\Downloads\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();
		js = (JavascriptExecutor) driver;
		vars = new HashMap<String, Object>();

		ReadExcl objExcelFile = new ReadExcl();

		objExcelFile.readExcel("exlFiles", "inputAddItemToCart.xls", "sheet1");
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
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				testCase(testCaseNum, row.getCell(j));

				testCaseNum++;
			}
		}
		// testCase(testCaseNum);
	}

	private void testCase(int testCaseNum, Cell cell) {
		switch (testCaseNum) {
		case 1:
			validNumOfItemsInCartCase(testCaseNum, cell);
			break;

		case 2:
			notValidNumOfItemInCartCase(testCaseNum);
			break;

		default:
			break;
		}
	}

	private void notValidNumOfItemInCartCase(int testCaseNum) {
		String message = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[1]/div")).getText().toString();

		if (message.contains("“Anchor Bracelet” has been added to your cart.")) {
			logger.debug("Test number " + testCaseNum + " has not passed");

		}

		else {
			logger.debug("Confirmation message number of quantity is : " + message);
			logger.debug("Test number " + testCaseNum + " has passed");
		}
	}

	private void validNumOfItemsInCartCase(int testCaseNum, Cell cell) {
		String quantity = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[1]/div")).getText().toString();
		logger.debug("Text" + quantity);

		if (quantity.contains("3 × “Anchor Bracelet” have been added to your cart.")) {
			logger.debug("Confirmation message number of quantity is : " + quantity);
			logger.debug("Test number " + testCaseNum + " has passed");
		}

		else
			logger.debug("Test number " + testCaseNum + " has not passed");
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
