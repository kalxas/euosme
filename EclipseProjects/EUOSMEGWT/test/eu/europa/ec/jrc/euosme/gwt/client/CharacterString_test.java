package eu.europa.ec.jrc.euosme.gwt.client;

import com.google.gwt.junit.client.GWTTestCase;

import eu.europa.ec.jrc.euosme.gwt.client.widgets.CharacterString;

public class CharacterString_test extends GWTTestCase {

	public void testEmptyRequiredNormal() {
		// Type empty string, check required and of type normal -> WARNING
		CharacterString testObj = new CharacterString("CI_OnlineResource","CI_OnlineResource",true, CheckFunctions.normal);
		testObj.myCheck();
		assertTrue(testObj.myError.isVisible());		
	}
	
	public void testEmptyNotRequiredNormal() {
		// Type empty string, check not required and of type normal -> NO WARNING
		CharacterString testObj = new CharacterString("CI_OnlineResource","CI_OnlineResource",false,CheckFunctions.normal);
		testObj.myCheck();
		assertTrue(!testObj.myError.isVisible());		
	}
	
	public void testEmptyRequiredEmail() {
		// Type empty string, check required and of type email -> WARNING
		CharacterString testObj = new CharacterString("CI_OnlineResource","CI_OnlineResource",true,CheckFunctions.electronicMailAddress);
		testObj.myCheck();
		assertTrue(testObj.myError.isVisible());		
	}
	
	public void testEmptyNotRequiredEmail() {
		// Type empty string, check not required and of type email -> NO WARNING
		CharacterString testObj = new CharacterString("CI_OnlineResource","CI_OnlineResource",false,CheckFunctions.electronicMailAddress);
		testObj.myCheck();
		assertTrue(!testObj.myError.isVisible());		
	}
	
	@Override
	public String getModuleName() {
		return "eu.europa.ec.jrc.euosme.gwt.EUOSMEGWT";
	}

}
