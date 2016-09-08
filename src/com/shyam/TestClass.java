package com.shyam;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.constants.Constants;
import com.shyam.helper.DataScraper;
import com.shyam.model.Person;

public class TestClass {

	// this code does not work with firefox 47
	// the code worked with firefox version 27 released in October 2015
	static Logger logger = Logger.getLogger(TestClass.class.getName());

	public static void main(String[] args) {
		logger.info("start");
		Set<Person> setOfPersons = new TreeSet<Person>();
		String url = "https://raw.githubusercontent.com/jashyaman/DOMParsing/master/sourceDOM.html";
		WebDriver driver = new FirefoxDriver();
		WebDriverWait wait = new WebDriverWait(driver, 3);
		Timeouts timeout = driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
		String baseUrl = "http://domparsing-shyammohan.rhcloud.com/domparsing/destpage.html";
		logger.info("baseUrl : " + baseUrl);
		driver.get(baseUrl);

		timeout.implicitlyWait(5, TimeUnit.SECONDS);

		String actualTitle = driver.getTitle();

		// TEST TO VALIDATE TITLE CORRECTNESS

		if (actualTitle.contentEquals(Constants.WEBPAGE_TITLE)) {
			logger.info("title Test Passed!");
		} else {
			logger.warning("title Test Failed");
		}
		WebElement body = driver.findElement(By.tagName("body"));
		WebElement root = driver.findElement(By.tagName("div"));

		if (root != null) {
			logger.info("root is not null");
		}

		// check if classes of div tag are present as required

		WebElement square = root.findElement(By.tagName("div"));
		if (square.getAttribute("class").contains("square default hover_basic")) {
			logger.info("CSS Check Passed!");
		} else {
			logger.warning("CSS check Failed");
		}

		// click the div
		square.click();
		timeout.implicitlyWait(10, TimeUnit.SECONDS);

		// fetch position of tag from left and top
		WebElement div2id = body.findElement(By.cssSelector("#div2id"));
		if (div2id.getCssValue("display").equals("table-cell")) {
			logger.info("div2id element visible correctly");
		} else {
			logger.warning("div2id element not visible correctly");
			;
		}
		Point div2id_pos = div2id.getLocation();
		logger.info("for element with id : #div2id :>: x pos : " + div2id_pos.x + "\ny pos : " + div2id_pos.y);

		// count the number of rows of divs present in div element
		List<WebElement> divList = div2id.findElements(By.cssSelector("div.size1"));
		int rowCount = divList.size();
		logger.info("number of elements in table : " + rowCount);

		// somewhere around this time, prepare a data structure will all the
		// data that needs to be
		// validated for correctness against the div innerHTMLs

		try {
			setOfPersons = DataScraper.fetchDataFromUrl(url);
		} catch (MalformedURLException e) {
			logger.severe("MalformedURLException" + e.getMessage());
		} catch (InterruptedException e) {
			logger.severe("InterruptedException" + e.getMessage());
		} catch (ExecutionException e) {
			logger.severe("ExecutionException" + e.getMessage());
		} catch (IOException e) {
			logger.severe("IOException" + e.getMessage());
		}
		Iterator<Person> iterator = setOfPersons.iterator();
		for (int i = 0; i < rowCount && iterator.hasNext(); i++) {
			Person person = iterator.next();

			divList.get(i).click();
			logger.info("for element with class : " + divList.get(i).getAttribute("class") + " :>: \nx pos : "
					+ divList.get(i).getLocation().x + "\ny pos : " + divList.get(i).getLocation().y);
			timeout.implicitlyWait(3, TimeUnit.SECONDS);
			WebElement cell2 = divList.get(i).findElement(By.xpath("//*[@id=\"" + (i + 2) + "\"]"));

			if (cell2.getText().equals(String.format(Constants.TEMPLATE1, person.getName()))) {
				logger.info("validated row " + i + " col 01");
			} else {
				logger.warning("issue validating row " + i + " col 01");
			}

			cell2.click();
			timeout.implicitlyWait(3, TimeUnit.SECONDS);

			WebElement cell3 = divList.get(i).findElement(By.xpath("//*[@id=\"fs1_" + (i + 2) + "\"]"));

			String cmp_cell3 = String.format(Constants.TEMPLATE2, person.getPosition(), person.getOffice());

			if (cell3.getText().trim().equals(cmp_cell3.trim())) {
				logger.info("validated row " + i + " col 02");
			} else {
				logger.warning("issue validating row " + i + " col 02");
			}

			cell3.click();
			timeout.implicitlyWait(3, TimeUnit.SECONDS);

			WebElement cell4 = divList.get(i).findElement(By.xpath("//*[@id=\"fs2_" + (i + 2) + "\"]"));

			String cmp_cell4 = String.format(Constants.TEMPLATE3, displayWithFormat(person.getSalary()),
					person.getAge());
			if (cell4.getText().trim().equals(cmp_cell4)) {
				logger.info("validated row " + i + " col 03");
			} else {
				logger.warning("issue validating row " + i + " col 03");
			}

			cell4.click();
			timeout.implicitlyWait(3, TimeUnit.SECONDS);

			WebElement cell5 = divList.get(i).findElement(By.xpath("//*[@id=\"fs3_" + (i + 2) + "\"]"));

			String cmp_cell5 = String.format(Constants.TEMPLATE4, person.getStartDate());
			logger.info(cmp_cell5);
			if (cell5.getText().trim().equals(cmp_cell5.trim())) {
				logger.info("validated row " + i + " col 04");
			} else {
				logger.warning("issue validating row " + i + " col 04");
				break;
			}
			try {
				cell5.click();
				if (wait.until(ExpectedConditions.alertIsPresent()) == null) {
					logger.warning("Alert was not present");
				} else {
					logger.info("Alert was present");
					
				}
			} finally {
				try {
					Alert alert = driver.switchTo().alert();
					if ("E.N.D".equals(alert.getText())) {
						logger.info("alert displayed properly");
						alert.accept();
					} else {
						logger.info("alert did not have proper text");
					}
				} catch (NoAlertPresentException q) {
					logger.severe(q.getMessage());
				}
			}

		}

		logger.info("closing driver");
		driver.close();
		System.exit(0);

	}

	private static String displayWithFormat(Double salary) {
		Locale locale = Locale.US;
		NumberFormat numberFormatter = NumberFormat.getNumberInstance(locale);
		String strToReturn = numberFormatter.format(salary);

		return strToReturn;
	}
}
