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

package eu.europa.ec.jrc.euosme.gwt.client.callback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;

import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;

/**
 * Contains the implementation of the callback class from Validation service
 * 
 * @version 1.1 - December 2010
 * @author 	Marzia Grasso 
 */
public class ValidationRpcCallback implements AsyncCallback <String>, RequestCallback {
	
	/** Constants declaration */
	private iso19115Constants constants = GWT.create(iso19115Constants.class);
	
    /* (non-Javadoc)
     * @see com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
     */
    public void onSuccess(String result) {
    	if (result == null)
    		return;
    	getOldValidationMsg(result);
    }
    
    public void getNewValidationMsg(String result) {
    	String warningDialog = "";
    	
		HTML resultHTM = new HTML(result);
		int nrSchema = 0; 
		int nrInspire = 0;
		
		String smrSchema ="";
		String smrInspire ="";
		
		NodeList<Element> errorElements = resultHTM.getElement().getElementsByTagName("ul");
		for (int j=0;j<errorElements.getLength();j++) {
			String typeOfError = errorElements.getItem(j).getClassName();
			
			if (typeOfError.equals("schemaError")) {
				Element schemaErrorElement = errorElements.getItem(j).getFirstChildElement(); 
				while (schemaErrorElement != null) {					
					String warning = "<li>" + schemaErrorElement.getInnerHTML() + "</li>";
					smrSchema += warning;
					nrSchema++;
					schemaErrorElement = schemaErrorElement.getNextSiblingElement();
				}				
			}
			else if (typeOfError.equals("inspireError")) {
				Element schemaErrorElement = errorElements.getItem(j).getFirstChildElement(); 
				while (schemaErrorElement != null) {					
					String warning = "<li>" + schemaErrorElement.getInnerHTML() + "</li>";
					smrInspire += warning;
					nrInspire++;
					schemaErrorElement = schemaErrorElement.getNextSiblingElement();
				}				
			} 
		}		
		warningDialog = "<p>ISO Schema errors: " + nrSchema + "</p><ul>" + smrSchema + "</ul> " + 
		                "<p>INSPIRE validation errors: " + nrInspire + "</p><ul>" + smrInspire + "</ul>";
    	final DialogBox resultValidation = new DialogBox(false,false);
    	resultValidation.setHTML(warningDialog);
    	resultValidation.add(new Button(constants.close(), new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				resultValidation.hide();				
			}}));
    	resultValidation.center();
    	resultValidation.setSize("400","300");
    	try {
    		resultValidation.getElement().getElementsByTagName("div").getItem(0).getStyle().setOverflow(Overflow.AUTO);
    	}
    	catch (Exception ex) {	}    	
    }
    
    public void getOldValidationMsg(String result) {
    	String warningDialog = "";
		HTML resultHTM = new HTML(result);
    	NodeList<Element> wrongElements = resultHTM.getElement().getElementsByTagName("li");
    	int nrWrongs = 0;
    	for (int i=0;i<wrongElements.getLength();i++) {
    		String wrongElement = wrongElements.getItem(i).getInnerHTML();
    		if (wrongElement.contains("XPath:")) {
    			nrWrongs+=1;
	    		String warning = "<li>" + wrongElement.split("XPath:")[0] + "</li>";
	    		warningDialog += warning;
	    		String xpath = wrongElement.split("XPath:")[1].trim();
	    		xpath = xpath.replace("/*:", ".");
	    		xpath = xpath.replace("[namespace-uri()='http://www.isotc211.org/2005/gmd']", "");
	    		xpath = xpath.replace("[]","");
	    		xpath = "error-" + xpath.substring(1).toLowerCase();
	    		try {
	    			Element divError = DOM.getElementById(xpath);
	    			divError.setNodeValue(warning);
	    			divError.getStyle().clearDisplay();
	    		}
	    		catch (Exception ex) { }
    		}    		
    	}
    	warningDialog = "<p>Incorrect elements: " + nrWrongs + "</p><ul>" + warningDialog + "</ul>";
    	final DialogBox resultValidation = new DialogBox(false,false);
    	resultValidation.setHTML(warningDialog);
    	resultValidation.add(new Button(constants.close(), new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				resultValidation.hide();				
			}}));
    	resultValidation.center();
    	resultValidation.setSize("400","300");
    	try {
    		resultValidation.getElement().getElementsByTagName("div").getItem(0).getStyle().setOverflow(Overflow.AUTO);
    	}
    	catch (Exception ex) {	} 
    }
    
    /* (non-Javadoc)
     * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
     */
    @Override
    public void onFailure(Throwable caught) {    	
    }

	/* (non-Javadoc)
	 * @see com.google.gwt.http.client.RequestCallback#onError(com.google.gwt.http.client.Request, java.lang.Throwable)
	 */
	@Override
	public void onError(Request request, Throwable exception) {
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.http.client.RequestCallback#onResponseReceived(com.google.gwt.http.client.Request, com.google.gwt.http.client.Response)
	 */
	@Override
	public void onResponseReceived(Request request, Response response) {
		String ret = response.getText();
		Window.alert(ret);
	}	
}