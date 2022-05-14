package hw3_package;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SearchProduct {
	public WebDriver driver;
	public Map<String, Object> vars;
	Logger logger=LogManager.getLogger(SearchProduct.class);
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

		objExcelFile.readExcel("exlFiles", "inputShort.xls", "sheet1");
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

		int testCaseNum=1;

		for (int i = 0; i < rowCount+1 ; i++) {

			Row row = thsSheet.getRow(i);
			logger.debug(row.toString());

			// Create a loop to print cell values in a row

			for (int j = 0; j < row.getLastCellNum(); j++) {
				driver.findElement(
						By.xpath("//*[@id=\"ast-desktop-header\"]/div[1]/div/div/div/div[3]/div[2]/div/div/a")).click();

				driver.findElement(By
						.xpath("//*[@id=\"ast-desktop-header\"]/div[1]/div/div/div/div[3]/div[2]/div/form/label/input"))
						.clear();

				if(row.getCell(j).getCellType().name().equals("String"))
				driver.findElement(By
						.xpath("//*[@id=\"ast-desktop-header\"]/div[1]/div/div/div/div[3]/div[2]/div/form/label/input"))
						.sendKeys((row.getCell(j).getStringCellValue()));

				else
				{
					driver.findElement(By
							.xpath("//*[@id=\"ast-desktop-header\"]/div[1]/div/div/div/div[3]/div[2]/div/form/label/input"))
							.sendKeys(new DataFormatter().formatCellValue(row.getCell(j)));
				}
				
				
				driver.findElement(By
						.xpath("//*[@id=\"ast-desktop-header\"]/div[1]/div/div/div/div[3]/div[2]/div/form/label/input"))
						.sendKeys(Keys.ENTER);
				
				testCase(testCaseNum);
				
				testCaseNum++;
			}
		}
		testCase(testCaseNum);
	}
	
	
	

	private void testCase(int testCaseNum) {
		switch (testCaseNum) {
		case 1:
			searchProductItemExist(testCaseNum);
			break;
			
		case 2:
			searchProductSpacesInput(testCaseNum);
			break;
		
		case 3:
		searchProductItemNotExist(testCaseNum);
			break;
			
		case 4:
			searchProductEmptyInput(testCaseNum);
			break;
			

		default:
			break;
		}
	}

	private void searchProductEmptyInput(int testCaseNum)  {
		String productStr="";
		String previousUrl=driver.getCurrentUrl();
		
		
		driver.findElement(
				By.xpath("//*[@id=\"ast-desktop-header\"]/div[1]/div/div/div/div[3]/div[2]/div/div/a")).click();

		driver.findElement(By
				.xpath("//*[@id=\"ast-desktop-header\"]/div[1]/div/div/div/div[3]/div[2]/div/form/label/input"))
				.clear();

		
		driver.findElement(By
				.xpath("//*[@id=\"ast-desktop-header\"]/div[1]/div/div/div/div[3]/div[2]/div/form/label/input"))
				.sendKeys(productStr);
		
		
		if(previousUrl.equals("https://atid.store/?s="))
			logger.debug("Test number "+testCaseNum+" has passed");
		else
			logger.debug("Test number "+testCaseNum+" has not passed");

	}

	private void searchProductSpacesInput(int testCaseNum) {
		String productStr=driver.findElement(By.xpath("//*[@id=\"main\"]/section")).getText().toString();
		if(productStr.contains("Please try again"))
		{
			logger.debug("Confirmation message : "+productStr);
			logger.debug("Test number "+testCaseNum+" has passed");
		}
		else
			logger.debug("Test number "+testCaseNum+" has not passed");
	}

	private void searchProductItemNotExist(int testCaseNum) {
		String noResult=driver.findElement(By.xpath("//*[@id=\"post-414\"]/div/div/header/h2/a")).getText().toString();
		if(noResult.contains("Please try again"))
		{
			logger.debug("Confirmation message : "+noResult);
			logger.debug("Test number "+testCaseNum+" has passed");
		}
		else
			logger.debug("Test number "+testCaseNum+" has not passed");
				
	}

	private void searchProductItemExist(int testCaseNum) {
		String productStr=driver.findElement(By.xpath("//*[@id=\"post-212\"]/div/div/div[2]/p")).getText().toString();
		
		if(!productStr.contains("Sorry"))
		{
			logger.debug("Confirmation message : "+productStr);
			logger.debug("Test number "+testCaseNum+" has passed");
		}

		else
			logger.debug("Test number "+testCaseNum+" has not passed");
	}

	public static void main(String args[]) {
		JUnitCore junit = new JUnitCore();
		junit.addListener(new TextListener(System.out));
		org.junit.runner.Result result = junit.run(SearchProduct.class); // Replace "SampleTest" with the name of your
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
