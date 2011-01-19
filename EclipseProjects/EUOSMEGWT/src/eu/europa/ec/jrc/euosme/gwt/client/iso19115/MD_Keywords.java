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
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CharacterString;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CodeList;

/**
 * Create MD_Keywords model
 * Provides category keywords, their type, and reference source.
 * 
 * @version 4.0.1 - January 2011
 * @author 	Marzia Grasso
 */
public class MD_Keywords extends CI {
	
	/** Constants declaration */
 	protected iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Messages declaration */
 	protected iso19115Messages messages = GWT.create(iso19115Messages.class);
	
	/** keyword control declaration */
	CharacterString keywordObj = new CharacterString(constants.keywordValue(), "keywordvalue", true, CheckFunctions.normal, false);
	
	/** type control declaration */
	CodeList typeObj =	new CodeList(constants.keywordType(),"",false,"8","",true);
	
	/** thesaurus control declaration */
	CI_Citation thesaurusObj = new CI_Citation(constants.originatingControlledVocabulary(),false,false);
	
	/** 
     * constructor MD_Keywords model
     * 
     * @param label		{@link String} = the header
     * @param required	{@link Boolean} = if true, it is required
     * @param multiple	{@link Boolean} = if true, it could be added more than ones
     * 
     * @return	the widget composed by MD_Keywords fields
     */
	public MD_Keywords(String label, boolean required, boolean multiple) {
		super(label, required, multiple);	
		fieldsGroup.add(keywordObj);
		fieldsGroup.add(typeObj);
		fieldsGroup.add(thesaurusObj);		
		setInterface(-1);			
	}
	
	@Override
	public void myCheck() {
		if (this.getParent().isVisible()) {
			keywordObj.myCheck();
			typeObj.myCheck();
			thesaurusObj.myCheck();	
		}
	}

	@Override
	public void setFormName(String name) {
		super.setFormName(name);
		keywordObj.setFormName(name + ".keyword[1].characterstring[1]");		
		typeObj.setFormName(name + ".type[1].md_keywordtypecode[1]");
		thesaurusObj.setFormName(name + ".thesaurusname[1].ci_citation[1]");	
	}
	
	@Override
	public void setInterface(int i) {
		if (EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.GEOPORTAL.toString())) {
			typeObj.setVisible(false);
		}
		thesaurusObj.setInterface(1);		
	}
}