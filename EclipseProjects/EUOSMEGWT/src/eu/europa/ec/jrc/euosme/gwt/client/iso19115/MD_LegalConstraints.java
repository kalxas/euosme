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
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CI;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CharacterStringMultiple;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CodeListMultiple;

/**
 * Create MD_LegalConstraints model
 * Restrictions and legal prerequisites for accessing and using the resource or metadata
 * 
 * @version 3.0 - December 2010
 * @author 	Marzia Grasso
 */
public class MD_LegalConstraints extends CI {
	
	/** Constants declaration */
 	protected iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Messages declaration */
 	protected iso19115Messages messages = GWT.create(iso19115Messages.class);
	
 	/** Other constraints declaration */
 	public CharacterStringMultiple otherConstraintsObj = new CharacterStringMultiple(constants.otherConstraints(), "limitation", false, CheckFunctions.normal);
 	
 	/** Access constraints declaration */
 	CodeListMultiple accessConstraintsObj =	new CodeListMultiple(constants.accessConstraints(),"limitation",false,"9","otherRestrictions");
	
 	/** Use constraints declaration */
 	CodeListMultiple useConstraintsObj =	new CodeListMultiple(constants.useConstraints(),"",false,"9","");
	 	 	
 	/** 
     * constructor MD_LegalConstraints model
     * 
     * @param label		{@link String} = the header
     * @param required	{@link Boolean} = if true, it is required
     * @param multiple	{@link Boolean} = if true, it could be added more than ones
     * 
     * @return	the widget composed by MD_LegalConstraints fields
     */
	public MD_LegalConstraints(String label, boolean required, boolean multiple) {
		super(label, required, multiple);		
		fieldsGroup.add(accessConstraintsObj);
		fieldsGroup.add(useConstraintsObj);
		fieldsGroup.add(otherConstraintsObj);
		setInterface(-1);
	}
	
	@Override
	public void myCheck() {
		if (this.getParent().isVisible()) {
			accessConstraintsObj.myCheck();
			useConstraintsObj.myCheck();
			myError.setVisible(false);
			if (useConstraintsObj.getMyValues().contains("otherRestrictions".toUpperCase()) || accessConstraintsObj.getMyValues().contains("otherRestrictions".toUpperCase())) {
				if (otherConstraintsObj.getMyValues().isEmpty()) {
					myError.setText(constants.mandatoryFieldCombined6());
					myError.setVisible(true);
				}
			}			
		}
	}

	@Override
	public void setFormName(String name) {
		super.setFormName(name);
		accessConstraintsObj.setFormName(name + ".accessconstraints[1].md_restrictioncode[1]");
		useConstraintsObj.setFormName(name + ".useconstraints[1].md_restrictioncode[1]");
		otherConstraintsObj.setFormName(name + ".otherconstraints[1].characterstring[1]");
		
	}
	
	@Override
	public void setInterface(int i) {
		if ((EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.GEOPORTAL.toString()))) {
			useConstraintsObj.setVisible(false);
			accessConstraintsObj.setVisible(false);	
			otherConstraintsObj.setLabel(constants.accessConstraints());
		}
	}
}