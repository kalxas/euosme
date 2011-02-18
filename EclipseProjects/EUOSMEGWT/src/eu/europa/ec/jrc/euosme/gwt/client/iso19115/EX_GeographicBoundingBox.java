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

import eu.europa.ec.jrc.euosme.gwt.client.CIOrientations;
import eu.europa.ec.jrc.euosme.gwt.client.CheckFunctions;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Messages;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CI;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CharacterString;

/**
 * Create EX_GeographicBoundingBox model
 * Geographic position of the dataset
 * 
 * @version 2.0.1 - January 2011
 * @author 	Marzia Grasso
 */

public class EX_GeographicBoundingBox extends CI {
	
	/** Constants declaration */
 	protected iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Messages declaration */
 	protected iso19115Messages messages = GWT.create(iso19115Messages.class);
	
	/** North control declaration */
	public CharacterString northObj = new CharacterString(constants.north(), "", true, CheckFunctions.doublenum, true);
	
	/** East control declaration */
	public CharacterString eastObj = new CharacterString(constants.east(), "", true, CheckFunctions.doublenum, true);
	
	/** South control declaration */
	public CharacterString southObj = new CharacterString(constants.south(), "", true, CheckFunctions.doublenum, true);
	
	/** West control declaration */
	public CharacterString westObj = new CharacterString(constants.west(), "", true, CheckFunctions.doublenum, true);
		
	/** 
     * constructor EX_GeographicBoundingBox model
     * 
     * @param label		{@link String} = the header
     * @param required	{@link Boolean} = if true, it is required
     * @param multiple	{@link Boolean} = if true, it could be added more than ones
     * @param help		{@link String} = the anchor in the help 
     * 
     * @return	the widget composed by EX_GeographicBoundingBox fields
     */
	public EX_GeographicBoundingBox(String label, boolean required, boolean multiple, String help) {
		super(label, required, multiple, help, CIOrientations.VERTICAL);	
		fieldsGroup.add(northObj);
		fieldsGroup.add(eastObj);
		fieldsGroup.add(southObj);
		fieldsGroup.add(westObj);
		northObj.myTextBox.setSize("100px", "16px");
		southObj.myTextBox.setSize("100px", "16px");
		eastObj.myTextBox.setSize("100px", "16px");
		westObj.myTextBox.setSize("100px", "16px");		
	}
	
	@Override
	public void myCheck() {
		if (this.getParent().isVisible()) {
			northObj.myCheck();
			eastObj.myCheck();
			southObj.myCheck();
			westObj.myCheck();				
		}
	}

	@Override
	public void setFormName(String name) {
		super.setFormName(name);
		northObj.setFormName(name + ".northboundlatitude[1].decimal[1]");		
		eastObj.setFormName(name + ".eastboundlongitude[1].decimal[1]");
		southObj.setFormName(name + ".southboundlatitude[1].decimal[1]");
		westObj.setFormName(name + ".westboundlongitude[1].decimal[1]");		
	}
}