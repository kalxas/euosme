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

package eu.europa.ec.jrc.euosme.gwt.client.iso19115.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import eu.europa.ec.jrc.euosme.gwt.client.EUOSMEGWT;
import eu.europa.ec.jrc.euosme.gwt.client.AppModes;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.MD_LegalConstraints;
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.MD_Constraints;

/**
 * Create the tab Constraints
 * 
 * @version 1.1 - November 2010
 * @author 	Marzia Grasso
 */
public class TabConstraints extends Composite {
	
	private static TabConstraintsUiBinder uiBinder = GWT.create(TabConstraintsUiBinder.class);

	interface TabConstraintsUiBinder extends UiBinder<Widget, TabConstraints> {	}
	
	/** Constants declaration */
	private iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** title of the tab */
	@UiField(provided = true)
	Label titleLabel = new Label(constants.constraintsTitle());
	
	/** summary of the tab */
	@UiField(provided = true)
	HTML summaryHTML = new HTML(constants.constraintsSummary());
	
	@UiField(provided = true)
	MD_Constraints useLimitationObj = new MD_Constraints(constants.useLimitation(),true,false);
	
	@UiField(provided = true)
	MD_LegalConstraints constraintsObj =	new MD_LegalConstraints(constants.constraintsTabTitle(),true, true);
	
	/** Note on mandatory fields */
	@UiField(provided = true)
	Label mandatoryFieldLabel = new Label("(*) " + constants.mandatoryField());
	
	/** 
    * constructor TabConstraints
    * 
    * @return	the widget composed by the Constraints Tab
    */
	public TabConstraints() {
		// initialize widget
		initWidget(uiBinder.createAndBindUi(this));
		// set style
		titleLabel.removeStyleName("gwt-Label");
		// set form names
		setFormName();
		// set interface
		setInterface();		
	}

	/**
	 * This is called to make a client (first) check of the contained fields
	 */
	public void myCheck() {
		useLimitationObj.myCheck();
		constraintsObj.myCheck();	
	}
	
	/**
	 * Set the IDs of the contained objects 
	 */
	public void setFormName() {
		useLimitationObj.setFormName("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType + "[1].resourceconstraints[1].md_constraints[1]");
		constraintsObj.setFormName("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType + "[1].resourceconstraints[2].md_legalconstraints[1]");	
	}
	
	/**
	 * Show or hide elements according to the selected {@link AppModes}
	 */
	public void setInterface() {
		if ((EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.GEOPORTAL.toString()) || EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.RDSI.toString()))) {
			constraintsObj.otherConstraintsObj.setRequired(true);			
			constraintsObj.removeDisclosure();
			useLimitationObj.useLimitationObj.setRequired(true);
			useLimitationObj.removeDisclosure();
		}		
	}	
}