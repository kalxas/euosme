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
import eu.europa.ec.jrc.euosme.gwt.client.widgets.Boolean;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CI;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CharacterString;

/**
 * Create DQ_ConformanceResult model
 * Information about the outcome of evaluating the obtained value (or set of values) against a 
 * specified acceptable conformance quality level
 * 
 * @version 2.0.1 - January 2011
 * @author 	Marzia Grasso
 */

public class DQ_ConformanceResult extends CI {
	
	/** Constants declaration */
 	protected iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Messages declaration */
 	protected iso19115Messages messages = GWT.create(iso19115Messages.class);
	
	/** code control declaration */
	CI_Citation specificationObj = new CI_Citation(constants.specification(), true, false);
	
	/** codeSpace control declaration */
	CharacterString explanationObj = new CharacterString(constants.explanation(), "", true, CheckFunctions.normal, true);
	
	/** degree control declaration */
	Boolean degreeObj = new Boolean(constants.degree(),"degree",true,"");
	
	/** 
     * constructor DQ_ConformanceResult model
     * 
     * @param label		{@link String} = the header
     * @param required	{@link Boolean} = if true, it is required
     * @param multiple	{@link Boolean} = if true, it could be added more than ones
     * 
     * @return	the widget composed by DQ_ConformanceResult fields
     */
	public DQ_ConformanceResult(String label, boolean required, boolean multiple) {
		super(label, required, multiple);		
		fieldsGroup.add(specificationObj);
		fieldsGroup.add(explanationObj);
		fieldsGroup.add(degreeObj);
		setInterface(-1);
	}
	
	@Override
	public void myCheck() {
		if (this.getParent().isVisible()) {
			specificationObj.myCheck();
			explanationObj.myCheck();
			degreeObj.myCheck();
		}
	}

	@Override
	public void setFormName(String name) {
		super.setFormName(name);
		specificationObj.setFormName(name + ".specification[1].ci_citation[1]");		
		explanationObj.setFormName(name + ".explanation[1].characterstring[1]");
		degreeObj.setFormName(name + ".pass[1].boolean[1]");
	}
	
	@Override
	public void setInterface(int i) {
		if (EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.GEOPORTAL.toString())) {
			specificationObj.setInterface(2);
			specificationObj.removeDisclosure();
			explanationObj.setMyValue(constants.explanationValue());
			explanationObj.setVisible(false);			
		}		
	}	
}