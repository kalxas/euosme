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
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.CI_ResponsibleParty;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CIMultiple;

/**
 * Create the tab Organization
 * 
 * @version 3.0 - November 2010
 * @author 	Marzia Grasso
 */
public class TabOrganization extends Composite {
	
	private static TabOrganizationUiBinder uiBinder = GWT.create(TabOrganizationUiBinder.class);
	interface TabOrganizationUiBinder extends UiBinder<Widget, TabOrganization> {	}
	
	/** Constants declaration */
	private iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** title of the tab */
	@UiField(provided = true)
	Label titleLabel = new Label(constants.organizationTitle());
	
	/** summary of the tab */
	@UiField(provided = true)
	HTML summaryHTML = new HTML(constants.organizationSummary());
	
	/** responsibleParty control declaration */
	CI_ResponsibleParty responsiblePartyObj =	new CI_ResponsibleParty(constants.ci_responsibleParty(), false, true);
	@UiField(provided = true)
	CIMultiple responsiblePartyContainerObj = new CIMultiple(constants.ci_responsibleParty(), responsiblePartyObj, false);
	
	/** Note on mandatory fields */
	@UiField(provided = true)
	Label mandatoryFieldLabel = new Label("(*) " + constants.mandatoryField());

	/** 
    * constructor TabOrganization
    * 
    * @return	the widget composed by the Organization Tab
    */
	public TabOrganization() {
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
		responsiblePartyObj.myCheck();			
	}	
	
	/**
	 * Set the IDs of the contained objects 
	 */
	public void setFormName() {
		responsiblePartyObj.setFormName("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType + "[1].pointofcontact[1].ci_responsibleparty[1]");
		responsiblePartyContainerObj.setFormName("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType + "[1].pointofcontact[1]");		
	}
	
	/**
	 * Show or hide elements according to the selected {@link AppModes}
	 */
	public void setInterface() {
		if ((EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.GEOPORTAL.toString()))) {
			responsiblePartyObj.setRequired(true);
			responsiblePartyContainerObj.setRequired(true);
			responsiblePartyObj.setInterface(1);
		}		
	}	
}