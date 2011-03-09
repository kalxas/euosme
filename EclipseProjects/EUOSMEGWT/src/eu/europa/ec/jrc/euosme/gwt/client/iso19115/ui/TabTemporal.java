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
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.CI_Date;
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.TM_Primitive;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CIMultiple;


/**
 * Create the tab Temporal
 * 
 * @version 3.0 - February 2011
 * @author 	Marzia Grasso
 */
public class TabTemporal extends Composite {
	
	private static TabTemporalUiBinder uiBinder = GWT.create(TabTemporalUiBinder.class);
	interface TabTemporalUiBinder extends UiBinder<Widget, TabTemporal> {}
	
	/** Constants declaration */
	private iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** title of the tab */
	@UiField(provided = true)
	Label titleLabel = new Label(constants.temporalTitle());
	
	/** summary of the tab */
	@UiField(provided = true)
	HTML summaryHTML = new HTML(constants.temporalSummary());
	
	/** Date control declaration */
	TM_Primitive extentObj = new TM_Primitive(constants.temporalExtent(), false, true,"");
	@UiField(provided = true)
	CIMultiple extentContainerObj = new CIMultiple(constants.temporalExtent(), extentObj, false, "temporalextent");
	
	/** Date of publication control declaration */
	@UiField(provided = true)
	CI_Date publicationDateObj = new CI_Date(constants.publicationDate(), false, false,"");
	
	/** Date of last revision control declaration */
	@UiField(provided = true)
	CI_Date revisionDateObj = new CI_Date(constants.revisionDate(), false, false,"");
	
	/** Date of last revision control declaration */
	@UiField(provided = true)
	CI_Date creationDateObj = new CI_Date(constants.creationDate(), false, false,"");
	
	/** Note on mandatory fields */
	@UiField(provided = true)
	Label mandatoryFieldLabel = new Label("(*) " + constants.mandatoryField());
	
	/** 
    * constructor TabTemporal
    * 
    * @return	the widget composed by the Temporal Tab
    */
	public TabTemporal() {
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
		extentObj.myCheck();
		publicationDateObj.myCheck();
		revisionDateObj.myCheck();
		creationDateObj.myCheck();
	}	
	
	/**
	 * Set the IDs of the contained objects 
	 */
	public void setFormName() {
		extentObj.setFormName("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType + "[1].extent[1].ex_extent[1].temporalelement[1].ex_temporalextent[1]");
		extentContainerObj.setFormName("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType + "[1].extent[1].ex_extent[1].temporalelement[1]");	
		publicationDateObj.setFormName("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType + "[1].citation[1].ci_citation[1].date[1].ci_date[1]");
		revisionDateObj.setFormName("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType + "[1].citation[1].ci_citation[1].date[2].ci_date[1]");
		creationDateObj.setFormName("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType + "[1].citation[1].ci_citation[1].date[3].ci_date[1]");
	}
	
	/**
	 * Show or hide elements according to the selected {@link AppModes}
	 */
	public void setInterface() {
		if (EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.GEOSS.toString()) || EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.GEOPORTAL.toString()) || EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.RDSI.toString())) {
			publicationDateObj.dateObj.setRequired(false);
			revisionDateObj.dateObj.setRequired(false);
		}
		publicationDateObj.setInterface(0);
		revisionDateObj.setInterface(1);
		creationDateObj.setInterface(2);
		publicationDateObj.removeDisclosure();
		publicationDateObj.dateObj.setLabel(constants.publicationDate());
		revisionDateObj.removeDisclosure();
		revisionDateObj.dateObj.setLabel(constants.revisionDate());
		creationDateObj.removeDisclosure();
		creationDateObj.dateObj.setLabel(constants.creationDate());
	}
}