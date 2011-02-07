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
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CodeListFreeMultiple;

/**
 * Create MD_Constraints model
 * Restrictions on the access and use of a resource or metadata
 * 
 * @version 3.0 - January 2011
 * @author 	Marzia Grasso
 */
public class MD_Constraints extends CI {
	
	/** Constants declaration */
	protected iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Messages declaration */
	protected iso19115Messages messages = GWT.create(iso19115Messages.class);
	
	/** Use limitation declaration */
	public CodeListFreeMultiple useLimitationObj = new CodeListFreeMultiple(constants.useLimitation(), "condition", false, "12", "", true, false);
	
	/** 
     * constructor MD_Constraints model
     * 
     * @param label		{@link String} = the header
     * @param required	{@link Boolean} = if true, it is required
     * @param multiple	{@link Boolean} = if true, it could be added more than ones
     * 
     * @return	the widget composed by MD_Constraints fields
     */
	public MD_Constraints(String label, boolean required, boolean multiple) {
		super(label, required, multiple);		
		fieldsGroup.add(useLimitationObj);		
	}
	
	@Override
	public void myCheck() {
		useLimitationObj.myCheck();				
	}
	
	@Override
	public void setFormName(String name) {
		super.setFormName(name);
		useLimitationObj.setFormName(name + ".uselimitation[1].characterstring[1]");
		myError.getElement().setId("error-" + name);
	}
	
	@Override
	public void setInterface(int i) {		
	}
}