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
 * Create Distance model
 * Ground sample distance
 * 
 * @version 2.1.1 - January 2011
 * @author 	Marzia Grasso
 */
public class Distance extends CI {
	
	/** Constants declaration */
 	protected iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Messages declaration */
 	protected iso19115Messages messages = GWT.create(iso19115Messages.class);
	
	/** distance control declaration */
	CharacterString distanceObj = new CharacterString(constants.distance(), "spatialresolution", false, CheckFunctions.integer, true);
	
	/** UM control declaration */
	CharacterString umObj = new CharacterString(constants.UM(), "spatialresolution", false, CheckFunctions.normal, true);
	
	/** 
     * constructor Distance model
     * 
     * @param label		{@link String} = the header
     * @param required	{@link Boolean} = if true, it is required
     * @param multiple	{@link Boolean} = if true, it could be added more than ones
     * @param help		{@link String} = the anchor in the help 
     * 
     * @return	the widget composed by Distance fields
     */
	public Distance(String label, boolean required, boolean multiple, String help) {
		super(label, required, multiple, help, CIOrientations.HORIZONTAL);	
		fieldsGroup.add(distanceObj);
		fieldsGroup.add(umObj);
	}
	
	@Override
	public void myCheck() {
		if (this.getParent().isVisible()) {
			distanceObj.myCheck();
			umObj.myCheck();
			myError.setVisible(false);
			if (!distanceObj.getMyValue().isEmpty() && umObj.getMyValue().isEmpty()) {
				myError.setText(constants.mandatoryFieldCombined4());
				myError.setVisible(true);		
			}
			else if (distanceObj.getMyValue().isEmpty() && !umObj.getMyValue().isEmpty()) {
				myError.setText(constants.mandatoryFieldCombined5());
				myError.setVisible(true);		
			}
		}
	}
	
	@Override
	public void setFormName(String name) {
		super.setFormName(name);
		distanceObj.setFormName(name + ".distance[1]");		
		umObj.setFormName(name + ".distance[1].@uom");
	}	
}