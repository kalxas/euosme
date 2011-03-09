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

import eu.europa.ec.jrc.euosme.gwt.client.CheckFunctions;
import eu.europa.ec.jrc.euosme.gwt.client.DataTypes;
import eu.europa.ec.jrc.euosme.gwt.client.EUOSMEGWT;
import eu.europa.ec.jrc.euosme.gwt.client.AppModes;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.CI_Citation;
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.CI_OnlineResource;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CIMultiple;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CharacterStringLong;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CharacterStringMultiple;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CodeList;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CodeListMultiple;

/**
 * Create the tab Identification
 * 
 * @version 4.0 - February 2011
 * @author 	Marzia Grasso
 */
public class TabIdentification extends Composite {
	
	private static TabIdentificationUiBinder uiBinder = GWT.create(TabIdentificationUiBinder.class);
	interface TabIdentificationUiBinder extends UiBinder<Widget, TabIdentification> {	}
	
	/** Constants declaration */
	private iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** title of the tab */
	@UiField(provided = true)
	Label titleLabel = new Label(constants.identificationTitle());
	
	/** summary of the tab */
	@UiField(provided = true)
	HTML summaryHTML = new HTML(constants.identificationSummary());
	
	/** resource declaration */
	@UiField(provided = true)
	CI_Citation CI_CitationObj = new CI_Citation(constants.resource(),true,false,"");
	
	/** abstract control declaration */
	@UiField(provided = true)
	CharacterStringLong abstractObj = new CharacterStringLong(constants.resourceAbstract(), "resourceabstract", true);
	
	/** role control declaration */
	@UiField(provided = true)
	CodeList resourceTypeObj =	new CodeList(constants.resourceType(),"resourcetype",true,"6","dataset",true);

	/** onlineResource control declaration */
	CI_OnlineResource onlineResourceObj =	new CI_OnlineResource(constants.resourceLocator(), false, true,"");
	@UiField(provided = true)
	CIMultiple onlineResourceContainerObj = new CIMultiple(constants.resourceLocator(), onlineResourceObj, false,"resourcelocator");
	
	/** language control declaration */
	@UiField(provided = true)
	CodeListMultiple languageObj =	new CodeListMultiple(constants.resourceLanguage(),"resourcelanguage",true,"2","",true);

	/** coupled resource declaration */
	@UiField(provided = true)
	CharacterStringMultiple operatesOnObj = new CharacterStringMultiple(constants.coupledResource(), "coupledresource", false, CheckFunctions.URL);
	
	/** Note on mandatory fields */
	@UiField(provided = true)
	Label mandatoryFieldLabel = new Label("(*) " + constants.mandatoryField());
	
	/** 
    * constructor TabIdentification
    * 
    * @return	the widget composed by the Identification Tab
    */
	public TabIdentification() {
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
		CI_CitationObj.myCheck();
		abstractObj.myCheck();
		resourceTypeObj.myCheck();
		onlineResourceObj.myCheck();
		languageObj.myCheck();
		operatesOnObj.myCheck();
	}	
	
	/**
	 * Set the IDs of the contained objects 
	 */
	public void setFormName() {
		CI_CitationObj.setFormName("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType + "[1].citation[1].ci_citation[1]");
		abstractObj.setFormName("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType + "[1].abstract[1].characterstring[1]");
		resourceTypeObj.setFormName("md_metadata[1].hierarchylevel[1].md_scopecode[1]");		
		onlineResourceObj.setFormName("md_metadata[1].distributioninfo[1].md_distribution[1].transferoptions[1].md_digitaltransferoptions[1].online[1].ci_onlineresource[1]");
		onlineResourceContainerObj.setFormName("md_metadata[1].distributioninfo[1].md_distribution[1].transferoptions[1].md_digitaltransferoptions[1].online[1]");
		languageObj.setFormName("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType + "[1].language[1].languagecode[1]");
		operatesOnObj.setFormName("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType + "[1].operateson[1]");
	}
	
	/**
	 * Show or hide elements according to the selected {@link AppModes}
	 */
	public void setInterface() {
		if (EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.GEOSS.toString()) || EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.GEOPORTAL.toString()) || EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.RDSI.toString())) {
			CI_CitationObj.setInterface(0);
			if (EUOSMEGWT.metadataType.equalsIgnoreCase(DataTypes.DATA_SERVICE.toString())) languageObj.setVisible(false);
			else operatesOnObj.setVisible(false);
			resourceTypeObj.setVisible(false);
			CI_CitationObj.removeDisclosure();
			//onlineResourceObj.removeDisclosure();
		}
		else operatesOnObj.setVisible(false);
	}
}