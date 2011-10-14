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
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import eu.europa.ec.jrc.euosme.gwt.client.CIOrientations;
import eu.europa.ec.jrc.euosme.gwt.client.CheckFunctions;
import eu.europa.ec.jrc.euosme.gwt.client.EUOSMEGWT;
import eu.europa.ec.jrc.euosme.gwt.client.AppModes;
import eu.europa.ec.jrc.euosme.gwt.client.Utilities;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Messages;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CI;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CIMultiple;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CharacterString;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CodeListFree;

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
	MD_Identifier identifierObj =	new MD_Identifier(constants.md_identifier(),false, true,"");
	CIMultiple identifierContainerObj = new CIMultiple(constants.md_identifier(), identifierObj, false,"resourceidentifier");
	
	/** identifier control declaration */
	CI_Date dateObj =	new CI_Date(constants.date(),true, true,"");
	CIMultiple dateContainerObj = new CIMultiple(constants.date(), dateObj, true,"");

	/** List of specification for RDSI AppMode */
	final CodeListFree specificationObj = new CodeListFree(constants.specifications(),"specification",false,"15","",CheckFunctions.normal,true,false);
	
	/** 
     * constructor CI_Citation model
     * 
     * @param label		{@link String} = the header
     * @param required	{@link Boolean} = if true, it is required
     * @param multiple	{@link Boolean} = if true, it could be added more than ones
     * @param help		{@link String} = the anchor in the help
     *  
     * @return	the widget composed by CI_Citation fields
     */
	public CI_Citation(String label, boolean required, boolean multiple, String help) {
		super(label, required, multiple, help, CIOrientations.VERTICAL);	
		fieldsGroup.add(titleObj);
		fieldsGroup.add(alternateTitleObj);
		fieldsGroup.add(identifierContainerObj);
		fieldsGroup.add(dateContainerObj);
		
		specificationObj.myTextBox.getTextBox().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				TreeItem myTreeItem = null;
				myTreeItem = Utilities.getSelectTreeItem(specificationObj.myTextBox.getTextBox().getName());					
				if (myTreeItem!=null) {
					Utilities.ensureItemVisible(myTreeItem);					
					Utilities.setTextTreeItem(myTreeItem,specificationObj.myTextBox.getText());
				}
			}			
		});			

		setInterface(-1);
	}
	
	@Override
	public void myCheck() {
		if (this.getParent().isVisible()) {
			if(titleObj.isVisible()) titleObj.myCheck();
			if(alternateTitleObj.isVisible()) alternateTitleObj.myCheck();
			if(identifierObj.isVisible()) identifierObj.myCheck();	
			if(dateObj.isVisible()) dateObj.myCheck();
			if(specificationObj.isVisible()) specificationObj.myCheck();		
		}
	}
	
	@Override
	public void setFormName(String name) {
		super.setFormName(name);
		titleObj.setFormName(name + ".title[1].characterstring[1]");
		specificationObj.setFormName(name + ".title[1].characterstring[1]");
		alternateTitleObj.setFormName(name + ".alternatetitle[1].characterstring[1]");
		identifierObj.setFormName(name + ".identifier[1].rs_identifier[1]");
		identifierContainerObj.setFormName(name + ".identifier[1]");
		dateObj.setFormName(name + ".date[1].ci_date[1]");
		dateContainerObj.setFormName(name + ".date[1]");		
	}
	
	@Override
	public void setInterface(int i) {
		if (EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.GEOSS.toString()) || EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.GEOPORTAL.toString()) || EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.RDSI.toString())) {
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
				dateObj.setMultiple(false);
				fieldsGroup.add(dateObj);
				dateContainerObj.setVisible(false);
				// do not autoupdate the free keyword field to the tree
				titleObj.setAutoupdate(false);				
			}
			if (i==2) { // TAB conformity
				fieldsGroup.clear();
				fieldsGroup.add(specificationObj);
				fieldsGroup.add(alternateTitleObj);
				fieldsGroup.add(identifierContainerObj);
				fieldsGroup.add(dateContainerObj);
				alternateTitleObj.setVisible(false);
				identifierObj.setVisible(false);
				identifierContainerObj.setVisible(false);
				dateContainerObj.setVisible(false);
				dateObj.setMultiple(false);				
				dateObj.removeDisclosure();
				fieldsGroup.add(dateObj);
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