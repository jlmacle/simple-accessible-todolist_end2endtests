package jl.com;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * @author 
 * Class testing the user requirement of deleting a category.
 */
public class UserRequirement1 {
	
	WebDriver driver;
	
	@BeforeClass
	public void setup() {
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\jeanl\\Documents\\_SynchronizedFolder_Code\\Programming a good gift to Adelaide Ellie and Liam\\z_webdriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();		
	}
	
	@BeforeMethod
	public void navigate() {
		driver.get("http://localhost:4200");
	}
	
	@Test
    public void createCategory() {
    	boolean isCategoryFound = false;
    	String textToFind= "Protractor test category";
    	
    	driver.findElement(By.id("new_category_input_field")).sendKeys("Protractor test category");
    	driver.findElement(By.id("add_category_button")).click();
    	//The category has been added. The display of the existing categories is being refreshed.
    	driver.get("http://localhost:4200");
    		    	
    	List<WebElement> spanElements = driver.findElements(By.tagName("span"));	    	
    	try {
    		System.out.println("Found "+spanElements.size()+" SPAN elements");
    		for (WebElement spanElement : spanElements) {
	    		String text = spanElement.getText();
				System.out.println("Found text: "+text);
				if (text.equals(textToFind)) {isCategoryFound=true;break;}
				
			}
    	}
    	catch(StaleElementReferenceException e) {
    		System.err.println("Caught a StaleElementReferenceException "
    				+ "while going through the SPAN elements.");
    		e.printStackTrace();
    	}	    	
    	
    	assertThat(isCategoryFound).isEqualTo(true);
    	
    }
	
	@Test
	public void deleteCategory() throws Exception {
		int newCategoryPositionIntheList = 0;
		int categoryPosition = 0;
		String newCategoryDescription = "Protractor test category";
		boolean isCategoryFound = false;
		
		//1. Confirmation that the category was created; registration of its position in the list of span elements    	
    	List<WebElement> spanElements = driver.findElements(By.tagName("span"));	    	
    	try {    		
    		for (WebElement spanElement : spanElements) {
    			categoryPosition++;
	    		String text = spanElement.getText();
				if (text.equals(newCategoryDescription)) {
					newCategoryPositionIntheList = categoryPosition;
					System.out.println("Found the text:"+text+" in position: "+newCategoryPositionIntheList);					
					isCategoryFound=true;
					break;
				}
			}
    	}
    	catch(StaleElementReferenceException e) {
    		System.err.println("Caught a StaleElementReferenceException "
    				+ "while going through the SPAN elements.");
    		e.getMessage();
    		e.printStackTrace();
    	}	 
		    	
    	if(isCategoryFound == true) {
    		System.out.println("The new category has been successfuly created.");
    		//2. deletion of the category created
    			// finding the elements of the class "fas fa-trash-alt"
    		List<WebElement> trashIconElements = driver.findElements(By.className("fa-trash-alt"));
    		System.out.println("Found "+trashIconElements.size()+" elements in trashIconElements.");
    		try {
    			categoryPosition=0;    			
    			for(WebElement trashCanIconElement : trashIconElements) {
    				categoryPosition++;    				
    				System.out.println("Text for trashCanIconElement: "+trashCanIconElement.getText());
    				if (categoryPosition == newCategoryPositionIntheList) {
    					System.out.println("Clicking the trash can icon in position: "+categoryPosition);
    					trashCanIconElement.click();
    					break;
    				}
    				else {System.out.println("Skipping this trash can icon.");}
    			}
    			
    		}
    		catch(StaleElementReferenceException e) {
    			System.err.println("Caught a StaleElementReferenceException"
    					+ "while going through the elements related to a trash can icon.");
    			e.getMessage();
    			e.printStackTrace();    			
    		}    		
    		
    		//4. confirmation of deletion
    		driver.get("http://localhost:4200");
    		spanElements = driver.findElements(By.tagName("span"));
    		System.out.println("Found "+spanElements.size()+" elements in spanElements after deletion.");
    		try {
    			for(WebElement spanElement : spanElements) {
    				String text = spanElement.getText();
    				System.out.println(text);
    				if (text.equals(newCategoryDescription)) {
    					//if the created category can be found the test is failed
    					System.err.println("Found "+newCategoryDescription+" when the test category should have been deleted."
    							+ "The test is failed.");
    					fail();
    				}
    				   				
    			}
    			//otherwise the test is successful 
    			assertTrue(true);
    		}
    		catch(StaleElementReferenceException e) {
    			System.err.println("Caught a StaleElementReferenceException"
    					+ "while going through the elements related to a trash can icon.");
    			e.getMessage();
    			e.printStackTrace();    			
    		}    
    		
    	}
    	
    	else {
    		throw new Exception("Test of category creation failed.");
    	}		
		
	}	
	
	
	@AfterClass
	public void releaseResources() {
		driver.quit();
	}

}
