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

import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.DQ_Element;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CIMultiple;

/**
 * Create the tab Conformity
 * 
 * @version 3.0 - February 2011
 * @author 	Marzia Grasso
 */
public class TabConformity extends Composite {
	
	private static TabConformityUiBinder uiBinder = GWT.create(TabConformityUiBinder.class);
	interface TabConformityUiBinder extends UiBinder<Widget, TabConformity> {}
	
	/** Constants declaration */
	private iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** title of the tab */
	@UiField(provided = true)
	Label titleLabel = new Label(constants.conformityTitle());
	
	/** summary of the tab */
	@UiField(provided = true)
	HTML summaryHTML = new HTML(constants.conformitySummary());
	
	/** report declaration */
	DQ_Element reportObj =	new DQ_Element(constants.conformity(),false, true,"");
	@UiField(provided = true)
	CIMultiple reportContainerObj = new CIMultiple(constants.conformity(), reportObj, false,"");
	
	/** Note on mandatory fields */
	@UiField(provided = true)
	Label mandatoryFieldLabel = new Label("(*) " + constants.mandatoryField());
	
	/** 
    * constructor TabConformity
    * 
    * @return	the widget composed by the Conformity Tab
    */
	public TabConformity() {
		// initialize widget
		initWidget(uiBinder.createAndBindUi(this));
		// set style
		titleLabel.removeStyleName("gwt-Label");
		// set form names
		setFormName();
	}

	/**
	 * This is called to make a client (first) check of the contained fields
	 */
	public void myCheck() {
		reportObj.myCheck();		
	}
	
	/**
	 * Set the IDs of the contained objects 
	 */
	public void setFormName() {
		reportObj.setFormName("md_metadata[1].dataqualityinfo[1].dq_dataquality[1].report[1].dq_domainconsistency[1]");
		reportContainerObj.setFormName("md_metadata[1].dataqualityinfo[1].dq_dataquality[1].report[1]");
	}
}