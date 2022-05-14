package hw3_package;

import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.Before;
import org.junit.After;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.JavascriptExecutor;
import java.util.*;
import java.io.IOException;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Sheet;

import org.apache.logging.log4j.*;

public class ContactUs {

	public WebDriver driver;
	public Map<String, Object> vars;
	Logger logger = LogManager.getLogger(SearchProduct.class);
	JavascriptExecutor js;

	Sheet thsSheet;

	@Before
	public void setUp() throws IOException {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\rivka\\Downloads\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();
		js = (JavascriptExecutor) driver;
		vars = new HashMap<String, Object>();

		ReadExcl objExcelFile = new ReadExcl();

		objExcelFile.readExcel("exlFiles", "inputOneRow.xls", "sheet1");
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
		String path;

		for (int i = 0; i < rowCount + 1; i++) {

			Row row = thsSheet.getRow(i);
			logger.debug(row.toString());

			// Create a loop to print cell values in a row

			for (int j = 0; j < row.getLastCellNum(); j++) {
				waiting(2000);
				int num = j;
				if(num >= 4 && num <8) {
					num=num-4;
				}
				else if(num >= 8 && num < 12) {
					num=num-8;
				}
				else if(num >=12) {
					num=num-12;
				}
					
				switch (num) {
				case 0:
					// Click on "Contact Us"
					//********************************************************************//
					driver.findElement(By.xpath("//*[@id=\"menu-item-829\"]/a")).click();
					
					// Click on edit text: Name
					//********************************************************************//
					path = "//*[@id=\"wpforms-15-field_0\"]";
					driver.findElement(By.xpath(path)).click();
					driver.findElement(By.xpath(path)).clear();					
					stringOrNum(row, j, path);
					break;

				case 1:
					// Click on edit text: Subject
					//********************************************************************//
					path = "//*[@id=\"wpforms-15-field_5\"]";

					driver.findElement(By.xpath(path)).click();

					driver.findElement(By.xpath(path)).clear();

					stringOrNum(row, j, path);					

					break;
				case 2:
					// Click on edit text: Email
					//********************************************************************//
					path = "//*[@id=\"wpforms-15-field_4\"]";

					driver.findElement(By.xpath(path)).click();

					driver.findElement(By.xpath(path)).clear();
					stringOrNum(row, j, path);					

					break;
				case 3:
					// Click on edit text: Message
					//********************************************************************//
					path = "//*[@id=\"wpforms-15-field_2\"]";

					driver.findElement(By.xpath(path)).click();

					driver.findElement(By.xpath(path)).clear();
					stringOrNum(row, j, path);	
					
					// Click on SEND MESSAGE batten
					//********************************************************************//
					driver.findElement(By.xpath("//*[@id=\"wpforms-submit-15\"]")).click();
					
					// Check result
					//********************************************************************//
					testCase(testCaseNum);
					testCaseNum++;
					break;
				default:
					break;
				}					
			}
		}			
		testCase(testCaseNum);
	}
	
	private void stringOrNum(Row row, int j, String path) {
		if (row.getCell(j).getCellType().name().equals("String"))
			driver.findElement(By.xpath(path))
					.sendKeys((row.getCell(j).getStringCellValue()));

		else {
			driver.findElement(By.xpath(path))
					.sendKeys(new DataFormatter().formatCellValue(row.getCell(j)));
		}	
		
		waiting(500);
	}
	
	private void waiting(int sec) {
		try {
			Thread.sleep(sec);
		} catch (InterruptedException e) {
			logger.debug("Time-out");
			e.printStackTrace();
		}
	}

	private void testCase(int testCaseNum) {
		switch (testCaseNum) {
		case 1:
			waiting(200);
			validTextForContact(testCaseNum);
			break;

		case 2:
			waiting(200);
			fillSpaces(testCaseNum);
			break;
		
		case 3:
			waiting(200);
			fillChars(testCaseNum);
			break;
			
		case 4:
			waiting(200);
			fillNumbers(testCaseNum);
			break;
			
		case 5:
			waiting(200);
			sendEmptyMessage(testCaseNum);
			break;
			
		default:
			break;
		}
	}

	private void sendEmptyMessage(int testCaseNum) {
		waiting(1000);

		// Click on "Contact Us"
		driver.findElement(By.xpath("//*[@id=\"menu-item-829\"]/a")).click();
		
		// Click on SEND MESSAGE batten
		driver.findElement(By.xpath("//*[@id=\"wpforms-submit-15\"]")).click();
		waiting(1000);
		
		// Get the result of searching
		String message = driver.findElement(By.xpath("//*[@id=\"wpforms-15-field_4-error\"]")).getText().toString();

		if (message.contains("Please enter a valid email address.")) {
			waiting(1000);
			logger.debug("Test number " + testCaseNum + " has passed");

		}
		else {
			waiting(1000);
			logger.debug("Test number " + testCaseNum + " has not passed");
		}		
	}

	private void validTextForContact(int testCaseNum) {
		String message = driver.findElement(By.xpath("//*[@id=\"wpforms-confirmation-15\"]/p")).getText().toString();

		if (message.contains("Thanks for contacting us!")) {
			logger.debug("Test number " + testCaseNum + " has passed");

		}

		else {
			logger.debug("Test number " + testCaseNum + " has not passed");
		}
	}
	
	private void 	fillSpaces(int testCaseNum) {
		invalidEmailAddress(testCaseNum);
	}

	private void 	fillChars(int testCaseNum) {
		invalidEmailAddress(testCaseNum);
	}
	
	private void 	fillNumbers(int testCaseNum) {
		invalidEmailAddress(testCaseNum);
	}

	private void invalidEmailAddress(int testCaseNum) {
		String message = driver.findElement(By.xpath("//*[@id=\"wpforms-15-field_4-error\"]")).getText().toString();

		if (message.contains("Please enter a valid email address.")) {
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
