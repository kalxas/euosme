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

import eu.europa.ec.jrc.euosme.gwt.client.CIOrientations;
import eu.europa.ec.jrc.euosme.gwt.client.CheckFunctions;
import eu.europa.ec.jrc.euosme.gwt.client.EUOSMEGWT;
import eu.europa.ec.jrc.euosme.gwt.client.AppModes;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Messages;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CI;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CharacterString;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CodeList;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CodeListFree;

/**
 * Create CI_ResponsibleParty model
 * Point of contact(s) associated with the resource(s) or responsible party for the metadata information
 * 
 * @version 4.0.1 - January 2011
 * @author 	Marzia Grasso
 */
public class CI_ResponsibleParty extends CI {
	
	/** Constants declaration */
	protected iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Messages declaration */
	protected iso19115Messages messages = GWT.create(iso19115Messages.class);
	
	/** individualName control declaration */
	CharacterString individualNameObj =	new CharacterString(constants.individualName(), "", false, CheckFunctions.normal, true);
	
	/** organisationName control declaration */
	CodeListFree organisationNameObj = new CodeListFree(constants.organisationName(), "", false, "11","",CheckFunctions.normal, true, true);
	
	/** positionName control declaration */
	CharacterString positionNameObj = new CharacterString(constants.positionName(), "", false, CheckFunctions.normal, true);
	
	/** contactInfo control declaration */
	CI_Contact contactInfoObj =	new CI_Contact(constants.ci_contact(),false, false,"");
			
	/** role control declaration */
	CodeList roleObj =	new CodeList(constants.role(),"",true,"4","", true);
	
	/** 
     * constructor CI_ResponsibleParty model
     * 
     * @param label		{@link String} = the header
     * @param required	{@link Boolean} = if true, it is required
     * @param multiple	{@link Boolean} = if true, it could be added more than ones
     * @param help		{@link String} = the anchor in the help 
     * 
     * @return	the widget composed by CI_ResponsibleParty fields
     */
	public CI_ResponsibleParty(String label, boolean required, boolean multiple, String help) {
		super(label, required, multiple, help, CIOrientations.VERTICAL);	
		fieldsGroup.add(individualNameObj);
		fieldsGroup.add(organisationNameObj);
		fieldsGroup.add(positionNameObj);
		fieldsGroup.add(contactInfoObj);
		fieldsGroup.add(roleObj);
	}
	
	@Override
	public void myCheck() {
		individualNameObj.myCheck();
		organisationNameObj.myCheck();
		positionNameObj.myCheck();
		contactInfoObj.myCheck();
		roleObj.myCheck();
		myError.setVisible(false);
		if (!(EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.GEOPORTAL.toString()) || EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.RDSI.toString())) && organisationNameObj.myTextBox.getValue().isEmpty() && individualNameObj.myTextBox.getValue().isEmpty() && positionNameObj.myTextBox.getValue().isEmpty()) {
			myError.setText(constants.mandatoryFieldCombined());
			myError.setVisible(true);			
		}	
	}
	
	@Override
	public void setFormName(String name) {
		super.setFormName(name);
		individualNameObj.setFormName(name + ".individualname[1].characterstring[1]");
		organisationNameObj.setFormName(name + ".organisationname[1].characterstring[1]");
		positionNameObj.setFormName(name + ".positionname[1].characterstring[1]");
		contactInfoObj.setFormName(name + ".contactinfo[1].ci_contact[1]");
		roleObj.setFormName(name + ".role[1].ci_rolecode[1]");
		myError.getElement().setId("error-" + name);
	}
	
	@Override
	public void setInterface(int i) {
		if (EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.GEOPORTAL.toString()) || EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.RDSI.toString())) {
			organisationNameObj.setRequired(true);
			individualNameObj.setVisible(false);
			positionNameObj.setVisible(false);
			contactInfoObj.setRequired(true);
			contactInfoObj.contactInstructionsObj.setVisible(false);
			contactInfoObj.hoursOfServiceObj.setVisible(false);
			contactInfoObj.onlineResourceObj.setVisible(false);
			contactInfoObj.phoneObj.setVisible(false);
			contactInfoObj.addressObj.electronicMailAddressObj.setRequired(true);
			contactInfoObj.addressObj.setRequired(true);				
			contactInfoObj.addressObj.administrativeAreaObj.setVisible(false);
			contactInfoObj.addressObj.cityObj.setVisible(false);
			contactInfoObj.addressObj.countryObj.setVisible(false);
			contactInfoObj.addressObj.deliveryPointObj.setVisible(false);
			contactInfoObj.addressObj.postalCodeObj.setVisible(false);
			contactInfoObj.addressObj.removeDisclosure();
			contactInfoObj.removeDisclosure();			
			if (i==1) 
				roleObj.setHelpAnchor("role");
			else 
				roleObj.setVisible(false);			
			organisationNameObj.setShowList(false);
		}		
	}
}