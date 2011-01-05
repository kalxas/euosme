package eu.europa.ec.jrc.euosme.gwt.client;

import com.google.gwt.junit.client.GWTTestCase;

import eu.europa.ec.jrc.euosme.gwt.client.widgets.CharacterStringMultiple;

public class CharacterStringMultiple_test extends GWTTestCase {

	public void testEmptyRequiredNormal() {
		// Type empty string, check required and of type normal -> WARNING
		CharacterStringMultiple testObj = new CharacterStringMultiple("CI_OnlineResource","organization",true, CheckFunctions.normal);
		testObj.myCheck();
		assertTrue(testObj.myError.isVisible());		
	}
	
	public void testEmptyNotRequiredNormal() {
		// Type empty string, check not required and of type normal -> NO WARNING
		CharacterStringMultiple testObj = new CharacterStringMultiple("CI_OnlineResource","organization",false,CheckFunctions.normal);
		testObj.myCheck();
		assertTrue(!testObj.myError.isVisible());		
	}
	
	public void testEmptyRequiredEmail() {
		// Type empty string, check required and of type email -> WARNING
		CharacterStringMultiple testObj = new CharacterStringMultiple("CI_OnlineResource","organization",true,CheckFunctions.electronicMailAddress);
		testObj.myCheck();
		assertTrue(testObj.myError.isVisible());			
	}
	
	public void testEmptyNotRequiredEmail() {
		// Type empty string, check not required and of type email -> NO WARNING
		CharacterStringMultiple testObj = new CharacterStringMultiple("CI_OnlineResource","organization",false,CheckFunctions.electronicMailAddress);
		testObj.myCheck();
		assertTrue(!testObj.myError.isVisible());		
	}
	
	@Override
	public String getModuleName() {
		return "eu.europa.ec.jrc.euosme.gwt.EUOSMEGWT";
	}

}
