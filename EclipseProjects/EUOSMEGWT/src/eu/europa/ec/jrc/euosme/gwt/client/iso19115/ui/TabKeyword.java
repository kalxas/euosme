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
import eu.europa.ec.jrc.euosme.gwt.client.AppModes;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.MD_Keywords_INSPIRE;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CIMultiple;

/**
 * Create the tab Keyword
 * 
 * @version 4.0 - February 2011
 * @author 	Marzia Grasso
 */
public class TabKeyword extends Composite {
	
	private static TabKeywordUiBinder uiBinder = GWT.create(TabKeywordUiBinder.class);
	interface TabKeywordUiBinder extends UiBinder<Widget, TabKeyword> {	}
	
	/** Constants declaration */
	private iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** title of the tab */
	@UiField(provided = true)
	Label titleLabel = new Label(constants.keywordTitle());
	
	/** summary of the tab */
	@UiField(provided = true)
	HTML summaryHTML = new HTML(constants.selectKeywordInfo());
	
	/** INSPIRE keywords setting */
	MD_Keywords_INSPIRE keywordsObj =	new MD_Keywords_INSPIRE(constants.keyword(), false, false,"");
	@UiField(provided = true)
	CIMultiple keywordsContainerObj = new CIMultiple(constants.keyword(), keywordsObj, false,"");
	
	/** Note on mandatory fields */
	@UiField(provided = true)
	Label mandatoryFieldLabel = new Label("(*) " + constants.mandatoryField());
	
	/** 
    * constructor TabKeyword
    * 
    * @return	the widget composed by the Keywords Tab
    */
	public TabKeyword() {
		// initialize widget
		initWidget(uiBinder.createAndBindUi(this));
		// set style
		titleLabel.removeStyleName("gwt-Label");
		// set form names
		setFormName();
		// set interface
		setInterface();
		// change summary if the data type is data service
		if (EUOSMEGWT.metadataType.equalsIgnoreCase(DataTypes.DATA_SERVICE.toString())) 
			summaryHTML.setHTML(constants.selectKeywordInfoDataService());
	}

	/**
	 * This is called to make a client (first) check of the contained fields
	 */
	public void myCheck() {
		keywordsObj.myCheck();		
	}	
	
	/**
	 * Set the IDs of the contained objects 
	 */
	public void setFormName() {
		keywordsObj.setFormName("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType + "[1].descriptivekeywords[1].md_keywords[1]");
		keywordsContainerObj.setFormName("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType + "[1].descriptivekeywords[1]");		
	}
	
	/**
	 * Show or hide elements according to the selected {@link AppModes}
	 */
	public void setInterface() {
		if ((EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.GEOSS.toString()) || EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.GEOPORTAL.toString()) || EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.RDSI.toString()))) {
			keywordsObj.setRequired(true);
			keywordsObj.removeDisclosure();
			keywordsObj.setMultiple(false);
			keywordsContainerObj.addButton.setVisible(false);
			keywordsContainerObj.removeDisclosure();
		}		
	}
}