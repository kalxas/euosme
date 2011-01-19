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
import eu.europa.ec.jrc.euosme.gwt.client.EUOSMEGWT;
import eu.europa.ec.jrc.euosme.gwt.client.AppModes;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Messages;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CI;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CIMultiple;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CharacterString;

/**
 * Create CI_Citation model
 * Citation data for the resource
 * 
 * @version 4.0.1 - January 2011
 * @author 	Marzia Grasso
 */
public class CI_Citation extends CI {
	
	/** Constants declaration */
 	protected iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Messages declaration */
 	protected iso19115Messages messages = GWT.create(iso19115Messages.class);
	
	/** title control declaration */
	CharacterString titleObj = new CharacterString(constants.title(), "", true, CheckFunctions.normal, true);
	
	/** alternateTitle control declaration */
	CharacterString alternateTitleObj = new CharacterString(constants.alternateTitle(), "", false, CheckFunctions.normal, true);
	
	/** identifier control declaration */
	MD_Identifier identifierObj =	new MD_Identifier(constants.md_identifier(),false, true);
	CIMultiple identifierContainerObj = new CIMultiple(constants.md_identifier(), identifierObj, false);
	
	/** identifier control declaration */
	CI_Date dateObj =	new CI_Date(constants.date(),true, true);
	CIMultiple dateContainerObj = new CIMultiple(constants.date(), dateObj, true);

	/** 
     * constructor CI_Citation model
     * 
     * @param label		{@link String} = the header
     * @param required	{@link Boolean} = if true, it is required
     * @param multiple	{@link Boolean} = if true, it could be added more than ones
     * 
     * @return	the widget composed by CI_Citation fields
     */
	public CI_Citation(String label, boolean required, boolean multiple) {
		super(label, required, multiple);		
		fieldsGroup.add(titleObj);
		fieldsGroup.add(alternateTitleObj);
		fieldsGroup.add(identifierContainerObj);
		fieldsGroup.add(dateContainerObj);		
		setInterface(-1);
	}
	
	@Override
	public void myCheck() {
		if (this.getParent().isVisible()) {
			if(titleObj.isVisible()) titleObj.myCheck();
			if(alternateTitleObj.isVisible()) alternateTitleObj.myCheck();
			if(identifierObj.isVisible()) identifierObj.myCheck();	
			if(dateObj.isVisible()) dateObj.myCheck();			
		}
	}
	
	@Override
	public void setFormName(String name) {
		super.setFormName(name);
		titleObj.setFormName(name + ".title[1].characterstring[1]");		
		alternateTitleObj.setFormName(name + ".alternatetitle[1].characterstring[1]");
		identifierObj.setFormName(name + ".identifier[1].rs_identifier[1]");
		identifierContainerObj.setFormName(name + ".identifier[1]");
		dateObj.setFormName(name + ".date[1].ci_date[1]");
		dateContainerObj.setFormName(name + ".date[1]");		
	}
	
	@Override
	public void setInterface(int i) {
		if (EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.GEOPORTAL.toString())) {
			identifierObj.setRequired(true);
			identifierContainerObj.setRequired(true);
			if (i==0) { // TAB Identification
				alternateTitleObj.setVisible(false);
				titleObj.setLabel(constants.resourceTitle());
				titleObj.setHelpAnchor("resourcetitle");
				// remove dates because are in conflict with tab temporal (publication date has the same xpath)
				dateObj.setVisible(false);
				dateContainerObj.setVisible(false);
				dateContainerObj.removeFromParent();
				dateObj.removeFromParent();				
			}
			if (i==1) { // TAB keyword
				alternateTitleObj.setVisible(false);
				identifierObj.setVisible(false);
				identifierContainerObj.setVisible(false);
				dateObj.removeDisclosure();
				dateObj.dateObj.setLabel(constants.referenceDate());
				dateObj.dateObj.setHelpAnchor("vocabulary");
				dateObj.dateTypeObj.setHelpAnchor("vocabulary");
				dateObj.setMultiple(false);
				fieldsGroup.add(dateObj);
				titleObj.setHelpAnchor("vocabulary");
				dateContainerObj.setVisible(false);
			}
			if (i==2) { // TAB conformity
				alternateTitleObj.setVisible(false);
				identifierObj.setVisible(false);
				identifierContainerObj.setVisible(false);
				dateContainerObj.setVisible(false);
				dateObj.setMultiple(false);
				dateObj.dateObj.setHelpAnchor("specification");
				dateObj.removeDisclosure();
				fieldsGroup.add(dateObj);
				titleObj.setLabel(constants.specification());
				titleObj.setHelpAnchor("specification");
			}
		}	// TAB Identification
		else if (i==0) {
			titleObj.setLabel(constants.resourceTitle());
			alternateTitleObj.setLabel(constants.resourceAlternateTitle());
			identifierContainerObj.setLabel(constants.resourceIdentifier());
			identifierObj.setLabel(constants.resourceIdentifier());
		}		
	}
}