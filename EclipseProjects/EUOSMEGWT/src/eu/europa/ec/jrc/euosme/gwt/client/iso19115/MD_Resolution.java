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
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.Label;

import eu.europa.ec.jrc.euosme.gwt.client.AppModes;
import eu.europa.ec.jrc.euosme.gwt.client.CIOrientations;
import eu.europa.ec.jrc.euosme.gwt.client.CheckFunctions;
import eu.europa.ec.jrc.euosme.gwt.client.EUOSMEGWT;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Messages;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CI;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CharacterString;

/**
 * Create MD_Resolution model
 * Level of detail expressed as a scale factor or a ground distance
 * 
 * @version 2.0.1 - January 2011
 * @author 	Marzia Grasso
 */
public class MD_Resolution extends CI {
	
	/** Constants declaration */
 	protected iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Messages declaration */
 	protected iso19115Messages messages = GWT.create(iso19115Messages.class);
	
	/** equivalentScaleObj control declaration */
	CharacterString equivalentScaleObj = new CharacterString(constants.equivalentScale(), "", false, CheckFunctions.integer, true);
	
	/** distanceObj control declaration */
	public Distance distanceObj =	new Distance(constants.distance(), false, false,"");
	
	/** text to help user fill correctly **/ 
	Label helpText = new Label(constants.equivalentScaleHelp());
		
	/** 
     * constructor MD_Resolution model
     * 
     * @param label		{@link String} = the header
     * @param required	{@link Boolean} = if true, it is required
     * @param multiple	{@link Boolean} = if true, it could be added more than ones
     * @param help		{@link String} = the anchor in the help 
     * 
     * @return	the widget composed by MD_Resolution fields
     */
	public MD_Resolution(String label, boolean required, boolean multiple, String help) {
		super(label, required, multiple, help, CIOrientations.VERTICAL);	
		fieldsGroup.add(equivalentScaleObj);
		equivalentScaleObj.myTextBox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				myCheck();				
			}			
		});
		distanceObj.distanceObj.myTextBox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				myCheck();				
			}			
		});
		distanceObj.umObj.myTextBox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				myCheck();				
			}			
		});
		fieldsGroup.add(distanceObj);
		if (EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.RDSI.toString()))
				fieldsGroup.add(helpText);
	}
	
	@Override
	public void myCheck() {
		if (this.getParent().isVisible()) {
			equivalentScaleObj.myCheck();
			distanceObj.myCheck();
			myError.setVisible(false);
			if (!EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.RDSI.toString()) &&
					((equivalentScaleObj.getMyValue().isEmpty() && distanceObj.distanceObj.getMyValue().isEmpty())
							|| (!equivalentScaleObj.getMyValue().isEmpty() && !distanceObj.distanceObj.getMyValue().isEmpty()))) {
				myError.setText(constants.mandatoryFieldCombined3());
				myError.setVisible(true);			
			}
		}
	}

	@Override
	public void setFormName(String name) {
		super.setFormName(name);
		equivalentScaleObj.setFormName(name + ".equivalentscale[1].md_representativefraction[1].denominator[1].integer[1]");		
		distanceObj.setFormName(name + ".distance[1]");
		myError.getElement().setId("error-" + name);
	}
}