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
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CharacterStringMultiple;

/**
 * Create CI_Address model
 * Physical and email address at which the organization or individual may be contacted.
 * 
 * @version 2.0.1 - January 2011
 * @author 	Marzia Grasso
 */

public class CI_Address extends CI {
	
	/** Constants declaration */
 	protected iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Messages declaration */
 	protected iso19115Messages messages = GWT.create(iso19115Messages.class);
	
	/** deliveryPoint control declaration */
	CharacterStringMultiple deliveryPointObj = new CharacterStringMultiple(constants.deliveryPoint(), "", false, CheckFunctions.normal);
	
	/** city control declaration */
	CharacterString cityObj = new CharacterString(constants.city(), "", false, CheckFunctions.normal, true);
	
	/** administrativeArea control declaration */
	CharacterString administrativeAreaObj = new CharacterString(constants.administrativeArea(), "", false, CheckFunctions.normal, true);
	
	/** postalCode control declaration */
	CharacterString postalCodeObj = new CharacterString(constants.postalCode(), "", false, CheckFunctions.normal, true);
	
	/** country control declaration */
	CharacterString countryObj = new CharacterString(constants.country(), "", false, CheckFunctions.normal, true);
	
	/** electronicMailAddress control declaration */
	CharacterStringMultiple electronicMailAddressObj = new CharacterStringMultiple(constants.electronicMailAddress(), "contact", false, CheckFunctions.electronicMailAddress);
	
	/** 
     * constructor CI_Address model 
     * 
     * @param label		{@link String} = the header
     * @param required	{@link Boolean} = if true, it is required
     * @param multiple	{@link Boolean} = if true, it could be added more than ones
     * 
     * @return	the widget composed by CI_Address fields
     */
	public CI_Address(String label, boolean required, boolean multiple) {
		super(label, required, multiple);		
		fieldsGroup.add(deliveryPointObj);
		fieldsGroup.add(cityObj);
		fieldsGroup.add(administrativeAreaObj);
		fieldsGroup.add(postalCodeObj);
		fieldsGroup.add(countryObj);
		fieldsGroup.add(electronicMailAddressObj);		
	}
	
	@Override
	public void myCheck() {
		if (this.getParent().isVisible()) {
			deliveryPointObj.myCheck();
			cityObj.myCheck();
			administrativeAreaObj.myCheck();
			postalCodeObj.myCheck();
			countryObj.myCheck();
			electronicMailAddressObj.myCheck();			
		}
	}
	
	@Override
	public void setFormName(String name) {
		super.setFormName(name);
		deliveryPointObj.setFormName(name + ".deliverypoint[1].characterstring[1]");		
		cityObj.setFormName(name + ".city[1].characterstring[1]");
		administrativeAreaObj.setFormName(name + ".administrativearea[1].characterstring[1]");
		postalCodeObj.setFormName(name + ".postalcode[1].characterstring[1]");
		countryObj.setFormName(name + ".country[1].characterstring[1]");
		electronicMailAddressObj.setFormName(name + ".electronicmailaddress[1].characterstring[1]");
	}	
}