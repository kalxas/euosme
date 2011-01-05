/***LICENSE START
 * Copyright 2011 EUROPEAN UNION
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * 
 * Date: 03 January 2011
 * Authors: Marzia Grasso, Angelo Quaglia, Massimo Craglia
LICENSE END***/

package eu.europa.ec.jrc.euosme.gwt.client.iso19115;

import com.google.gwt.core.client.GWT;

import eu.europa.ec.jrc.euosme.gwt.client.CheckFunctions;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Messages;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CI;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CharacterString;

/**
 * Create CI_Contact model
 * It refers to Information required enabling contact with the responsible person and/or organization
 * 
 * @version 3.0 - October 2010
 * @author 	Marzia Grasso
 */
public class CI_Contact extends CI {
	
	/** Constants declaration */
 	protected iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Messages declaration */
	protected iso19115Messages messages = GWT.create(iso19115Messages.class);
	
	/** phone control declaration */
	CI_Telephone phoneObj = new CI_Telephone(constants.ci_telephone(), false, false);
	
	/** address control declaration */
	CI_Address addressObj = new CI_Address(constants.address(), false, false);		
	
	/** onlineResource control declaration */
	CI_OnlineResource onlineResourceObj = new CI_OnlineResource(constants.ci_onlineResource(), false, false);
	
	/** hoursOfService control declaration */
	CharacterString hoursOfServiceObj = new CharacterString(constants.hoursOfService(), "",false, CheckFunctions.integer);
	
	/** contactInstructions control declaration */
	CharacterString contactInstructionsObj = new CharacterString(constants.contactInstructions(),"", false, CheckFunctions.normal);
	
	/** 
     * constructor CI_Contact model
     * 
     * @param label		{@link String} = the header
     * @param required	{@link Boolean} = if true, it is required
     * @param multiple	{@link Boolean} = if true, it could be added more than ones
     * 
     * @return	the widget composed by CI_Contact fields
     */
	public CI_Contact(String label, boolean required, boolean multiple) {
		super(label, required, multiple);		
		fieldsGroup.add(phoneObj);
		fieldsGroup.add(addressObj);
		fieldsGroup.add(onlineResourceObj);
		fieldsGroup.add(hoursOfServiceObj);
		fieldsGroup.add(contactInstructionsObj);		
	}
	
	@Override
	public void myCheck() {
		super.myCheck();
		if (this.getParent().isVisible()) {
			phoneObj.myCheck();
			addressObj.myCheck();
			onlineResourceObj.myCheck();
			hoursOfServiceObj.myCheck();
			contactInstructionsObj.myCheck();			
		}
	}
	
	@Override
	public void setFormName(String name) {
		super.setFormName(name);
		phoneObj.setFormName(name + ".phone[1].ci_telephone[1]");
		addressObj.setFormName(name + ".address[1].ci_address[1]");
		onlineResourceObj.setFormName(name + ".onlineresource[1].ci_onlineresource[1]");
		hoursOfServiceObj.setFormName(name + ".hoursofservice[1].characterstring[1]");
		contactInstructionsObj.setFormName(name + ".contactinstructions[1].characterstring[1]");		
	}	
}