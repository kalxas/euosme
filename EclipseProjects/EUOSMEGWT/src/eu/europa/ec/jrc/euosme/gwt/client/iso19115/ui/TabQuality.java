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

import eu.europa.ec.jrc.euosme.gwt.client.DataTypes;
import eu.europa.ec.jrc.euosme.gwt.client.EUOSMEGWT;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.MD_Resolution;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CIMultiple;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CharacterStringLong;


/**
 * Create the tab Quality
 * 
 * @version 3.0 - December 2010
 * @author 	Marzia Grasso
 */
public class TabQuality extends Composite {
	
	private static TabQualityUiBinder uiBinder = GWT.create(TabQualityUiBinder.class);
	interface TabQualityUiBinder extends UiBinder<Widget, TabQuality> {	}
	
	/** Constants declaration */
	private iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** title of the tab */
	@UiField(provided = true)
	Label titleLabel = new Label(constants.qualityTitle());
	
	/** summary of the tab */
	@UiField(provided = true)
	HTML summaryHTML = new HTML(constants.qualitySummary());
	
	/** lineage control declaration */
	@UiField(provided = true)
	CharacterStringLong lineageObj = new CharacterStringLong(constants.lineage(), "lineage", false);
	
	MD_Resolution spatialResolutionObj =	new MD_Resolution(constants.spatialResolution(), false, true);
	@UiField(provided = true)
	CIMultiple spatialResolutionContainerObj = new CIMultiple(constants.spatialResolution(), spatialResolutionObj, false);
	
	/** Note on mandatory fields */
	@UiField(provided = true)
	Label mandatoryFieldLabel = new Label("(*) " + constants.mandatoryField());
	
	/** 
    * constructor TabQuality
    * 
    * @return	the widget composed by the Quality Tab
    */
	public TabQuality() {
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
		lineageObj.myCheck();
		spatialResolutionObj.myCheck();
	}
	
	/**
	 * Set the IDs of the contained objects 
	 */
	public void setFormName() {
		lineageObj.setFormName("md_metadata[1].dataqualityinfo[1].dq_dataquality[1].lineage[1].li_lineage[1].statement[1].characterstring[1]");
		spatialResolutionObj.setFormName("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType + "[1].spatialresolution[1].md_resolution[1]");
		spatialResolutionContainerObj.setFormName("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType + "[1].spatialresolution[1]");		
	}
	
	/**
	 * Show or hide elements according to the selected {@link AppModes}
	 */
	public void setInterface() {
		if ((EUOSMEGWT.metadataType.equalsIgnoreCase(DataTypes.DATA_SERVICE.toString()))) {
			lineageObj.setVisible(false);
			spatialResolutionContainerObj.setVisible(false);
			summaryHTML.setHTML(constants.qualityServiceSummary());
		}
		spatialResolutionObj.distanceObj.removeDisclosure();
	}	
}