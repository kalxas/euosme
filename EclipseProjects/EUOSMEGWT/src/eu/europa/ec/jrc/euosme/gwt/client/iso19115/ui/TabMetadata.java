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
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import eu.europa.ec.jrc.euosme.gwt.client.EUOSMEGWT;
import eu.europa.ec.jrc.euosme.gwt.client.AppModes;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.CI_ResponsibleParty;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CIMultiple;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CodeList;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.DateImpl;

/**
 * Create the tab metadata
 * 
 * @version 4.0 - February 2011
 * @author 	Marzia Grasso
 */
public class TabMetadata extends Composite {
	
	private static TabMetadataUiBinder uiBinder = GWT.create(TabMetadataUiBinder.class);
	interface TabMetadataUiBinder extends UiBinder<Widget, TabMetadata> {	}
	
	/** Constants declaration */
	private iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** title of the tab */
	@UiField(provided = true)
	Label titleLabel = new Label(constants.metadataTitle());
	
	/** summary of the tab */
	@UiField(provided = true)
	HTML summaryHTML = new HTML(constants.metadataSummary());
	
	/** responsiblePartyObj control declaration */
	CI_ResponsibleParty responsiblePartyObj =	new CI_ResponsibleParty(constants.pointOfContact(),true, true,"");
	@UiField(provided = true)
	CIMultiple responsiblePartyContainerObj = new CIMultiple(constants.pointOfContact(), responsiblePartyObj, true, "contact");
	
	/** Date control declaration */
	@UiField(provided = true)
	DateImpl dateStampObj =	new DateImpl(constants.metadataDate(), "metadata_date", false);
	
	/** resource language control declaration */
	@UiField(provided = true)
	CodeList languageObj = new CodeList(constants.metadataLanguage(),"metadata_language",false,"2","eng",true);
	
	/** file identifier control declaration  */
	@UiField(provided = true)
	Hidden fileNameObj = new Hidden();
	
	/** Note on mandatory fields */
	@UiField(provided = true)
	Label mandatoryFieldLabel = new Label("(*) " + constants.mandatoryField());
	
	/** 
    * constructor TabMetadata
    * 
    * @return	the widget composed by the Metadata Tab
    */
	public TabMetadata() {
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
		dateStampObj.myCheck();
		languageObj.myCheck();
	}
	
	/**
	 * Set the IDs of the contained objects 
	 */
	public void setFormName() {
		responsiblePartyObj.setFormName("md_metadata[1].contact[1].ci_responsibleparty[1]");
		responsiblePartyContainerObj.setFormName("md_metadata[1].contact[1]");	
		dateStampObj.setFormName("md_metadata[1].datestamp[1].date[1]");
		languageObj.setFormName("md_metadata[1].language[1].languagecode[1]");
		fileNameObj.setID("md_metadata[1].fileidentifier[1].characterstring[1]");
	}
	
	/**
	 * Show or hide elements according to the selected {@link AppModes}
	 */
	public void setInterface() {
		if (EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.GEOPORTAL.toString()) || EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.RDSI.toString())) {
			languageObj.setRequired(true);
			responsiblePartyContainerObj.setLabel(constants.pointOfContactINSPIRE());
			responsiblePartyObj.setInterface(0);
		}		
	}	
}