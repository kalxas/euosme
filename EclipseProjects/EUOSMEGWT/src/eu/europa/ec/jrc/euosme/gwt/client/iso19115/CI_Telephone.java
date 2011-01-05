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
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CI;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CharacterStringMultiple;

/**
 * Create CI_Telephone model
 * Telephone numbers for contacting the responsible individual or organization
 * 
 * @version 2.0 - October 2010
 * @author 	Marzia Grasso
 */
public class CI_Telephone extends CI {
	
	/** Constants declaration */
	protected iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** voice control declaration */
	CharacterStringMultiple voiceObj = new CharacterStringMultiple(constants.voice(),"",false,CheckFunctions.normal);
	
	/** facsimile control declaration */
	CharacterStringMultiple facsimileObj = new CharacterStringMultiple(constants.facsimile(),"",false,CheckFunctions.normal);
	
	/** 
     * constructor CI_Telephone model
     * 
     * @param label		{@link String} = the header
     * @param required	{@link Boolean} = if true, it is required
     * @param multiple	{@link Boolean} = if true, it could be added more than ones
     *  
     * @return	the widget composed by CI_Telephone fields
     */
	public CI_Telephone(String label, boolean required, boolean multiple) {
		super(label, required, multiple);		
		fieldsGroup.add(voiceObj);
		fieldsGroup.add(facsimileObj);
	}
	
	@Override
	public void myCheck() {
		super.myCheck();
		if (this.getParent().isVisible()) {
			voiceObj.myCheck();
			facsimileObj.myCheck();			
		}		
	}
	
	@Override
	public void setFormName(String name) {
		super.setFormName(name);
		voiceObj.setFormName(name + ".voice[1]");
		facsimileObj.setFormName(name + ".facsimile[1]");
	}
}