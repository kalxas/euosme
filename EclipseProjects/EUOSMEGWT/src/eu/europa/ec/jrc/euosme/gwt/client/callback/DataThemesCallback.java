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

import java.util.Iterator;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;

import eu.europa.ec.jrc.euosme.gwt.client.EUOSMEGWT;
import eu.europa.ec.jrc.euosme.gwt.client.Utilities;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;

/**
 * Contains the implementation of the callback class from INSPIRE Data Themes service
 * 
 * @version 2.1 - November 2010
 * @author 	Marzia Grasso 
 */
public class DataThemesCallback implements AsyncCallback <Map<String, String>> {
	
	/** the list box that shows data themes */
	static ListBox myList;
	
	/** popup panel to help the user */
	static PopupPanel myHelp = new PopupPanel(true,false);
	
	/** Constants declaration */
	private iso19115Constants constants = GWT.create(iso19115Constants.class);
	
    /* (non-Javadoc)
     * @see com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
     */
    public void onSuccess(Map<String, String> result) {
    	if (result == null)
    		return;
    	processResponse(result);
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
     */
    public void onFailure(Throwable caught) {
    	if (EUOSMEGWT.datathemesAvailable) {
    		EUOSMEGWT.datathemesAvailable=false;
    		String msg=caught.getMessage();
    		if (msg != null)
    			Window.alert(constants.dataThemesServiceFailed() + " " + msg);
    	}
	}
    
    /**
     * Populate the list box with a collection of strings
     * 
     * @param definitions	{@link Map} = collection of strings
     */
    protected void processResponse(Map<String, String> definitions) {
    	String myHelpString = "";
    	myList.clear();
    	myList.addItem(constants.selectValue(), "");
    	if (definitions.size() >=0) {
    		definitions = Utilities.sortMapByKey(definitions);
    		Iterator<String> i = definitions.keySet().iterator();
    		while (i.hasNext()) {
	    		String definition_label = i.next();
	    		String definition = definitions.get(definition_label);
	    		myList.addItem(definition_label,definition_label);
	    		myHelpString +="<p><b>" + definition_label + "</b></p><p>" + definition + "</p>";
		    }	    		
    	}	 
    	myHelp.setWidget(new HTML(myHelpString));
	}
	
    /**
     * @param newList	{@link ListBox} = the list box to populate
     * @param newHelp	{@link PopupPanel} = the information panel
     */
    public static void setList(ListBox newList, PopupPanel newHelp) {    	
		myList = newList;
		myHelp = newHelp;
	}    
}