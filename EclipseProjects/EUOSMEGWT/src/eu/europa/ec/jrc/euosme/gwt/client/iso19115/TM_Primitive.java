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

import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Messages;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CI;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.DateImpl;

/**
 * Create TM_PRIMITIVE model
 * TM_Primitive is class representing a non-decomposed element of geometry or topology. 
 * This ISO class is fully documented in ISO 19108
 * 
 * @version 3.0 - November 2010
 * @author 	Marzia Grasso
 */
public class TM_Primitive extends CI {
	
	/** Constants declaration */
 	protected iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Messages declaration */
	protected iso19115Messages messages = GWT.create(iso19115Messages.class);
	
	/** Date control declaration */
	DateImpl beginDateObj =	new DateImpl(constants.beginDate(), "temporalExtent", true);
	
	/** Date control declaration */
	DateImpl endDateObj =	new DateImpl(constants.endDate(), "temporalExtent", true);
	
	/** 
     * constructor TM_Primitive model
     * 
     * @param label		{@link String} = the header
     * @param required	{@link Boolean} = if true, it is required
     * @param multiple	{@link Boolean} = if true, it could be added more than ones
     * 
     * @return	the widget composed by TM_Primitive fields
     */
	public TM_Primitive(String label, boolean required, boolean multiple) {
		super(label, required, multiple);		
		fieldsGroup.add(beginDateObj);
		fieldsGroup.add(endDateObj);		
	}
	
	@Override
	public void myCheck() {
		super.myCheck();
		if (this.getParent().isVisible()) {
			beginDateObj.myCheck();
			endDateObj.myCheck();
			myError.setVisible(false);
			if (endDateObj.getMyValue().toString().isEmpty() || beginDateObj.getMyValue().toString().isEmpty()) {
				myError.setText(constants.mandatoryFieldCombined2());
				myError.setVisible(true);
			}
		}		
	}
	
	@Override
	public void setFormName(String name) {
		super.setFormName(name);
		beginDateObj.setFormName(name + ".extent[1].timeperiod[1].beginposition[1]");
		endDateObj.setFormName(name + ".extent[1].timeperiod[1].endposition[1]");				
	}
}