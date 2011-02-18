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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

import eu.europa.ec.jrc.euosme.gwt.client.CIOrientations;
import eu.europa.ec.jrc.euosme.gwt.client.EUOSMEGWT;
import eu.europa.ec.jrc.euosme.gwt.client.AppModes;
import eu.europa.ec.jrc.euosme.gwt.client.RESTfulWebServiceProxy;
import eu.europa.ec.jrc.euosme.gwt.client.RESTfulWebServiceProxyAsync;
import eu.europa.ec.jrc.euosme.gwt.client.CheckFunctions;
import eu.europa.ec.jrc.euosme.gwt.client.callback.InspireServiceRpcCallback;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Messages;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CI;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CharacterString;

/**
 * Create MD_Identifier model
 * Value uniquely identifying an object within a namespace
 * 
 * @version 3.0.1 - January 2011
 * @author 	Marzia Grasso
 */
public class MD_Identifier extends CI {
	
	/** Constants declaration */
 	protected iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Messages declaration */
 	protected iso19115Messages messages = GWT.create(iso19115Messages.class);
	
	/** code control declaration */
	CharacterString codeObj = new CharacterString(constants.code(), "", true, CheckFunctions.normal, true);
	
	/** button to get a new code */
	Button getCodeButton = new Button();
	
	/** codeSpace control declaration */
	CharacterString codeSpaceObj = new CharacterString(constants.codeSpace(), "", false, CheckFunctions.normal, true);
	
	/** version control declaration */
	CharacterString versionObj = new CharacterString(constants.version(), "", false, CheckFunctions.normal, true);
	
	/** 
     * constructor MD_Identifier model
     * 
     * @param label		{@link String} = the header
     * @param required	{@link Boolean} = if true, it is required
     * @param multiple	{@link Boolean} = if true, it could be added more than ones
     * @param help		{@link String} = the anchor in the help 
     * 
     * @return	the widget composed by MD_Identifier fields
     */
	public MD_Identifier(String label, boolean required, boolean multiple, String help) {
		super(label, required, multiple, help, CIOrientations.HORIZONTAL);	
		fieldsGroup.add(codeObj);
		fieldsGroup.add(codeSpaceObj);
		fieldsGroup.add(versionObj);
		setInterface(-1);
	}

	@Override
	public void myCheck() {
		if (this.getParent().isVisible()) {
			codeObj.myCheck();
			codeSpaceObj.myCheck();
			versionObj.myCheck();	
		}
	}

	@Override
	public void setFormName(String name) {
		super.setFormName(name);
		codeObj.setFormName(name + ".code[1].characterstring[1]");		
		codeSpaceObj.setFormName(name + ".codespace[1].characterstring[1]");
		versionObj.setFormName(name + ".version[1].characterstring[1]");		
	}
	
	@Override
	public void setInterface(int i) {
		if (EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.GEOPORTAL.toString()) || EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.RDSI.toString())) {
			if (i==0) {
				codeObj.setMyValue("Conformity_001");
				codeSpaceObj.setMyValue("INSPIRE");
				codeObj.setVisible(false);
				codeSpaceObj.setVisible(false);
			}
			versionObj.setVisible(false);
		}
		if (EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.RDSI.toString())) {
			getCodeButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					// call service via RPC
					InspireServiceRpcCallback callback = new InspireServiceRpcCallback();
					callback.setCodeObj(codeObj.myTextBox);
					InspireServiceRpcCallback.setType("XML");
					RESTfulWebServiceProxyAsync ls = RESTfulWebServiceProxy.Util.getInstance();
					ls.invokeInspireUUIDService(callback);
					//TODO put the right namespace
					codeSpaceObj.myTextBox.setText("http://");
				}
			});
			getCodeButton.setHTML(constants.getUUID());
			fieldsGroup.add(getCodeButton);
		}
	}
}