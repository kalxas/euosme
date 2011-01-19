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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;

import eu.europa.ec.jrc.euosme.gwt.client.EUOSMEGWT;
import eu.europa.ec.jrc.euosme.gwt.client.Utilities;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;

/**
 * Contains the implementation of the callback class from Codelist service
 * 
 * @version 4.0 - January 2011
 * @author 	Marzia Grasso 
 */
public class CodeListRpcCallback implements AsyncCallback <String> {
	
	/** List box that corresponds to the code list*/
	ListBox myList;
	
	/** List of suggestions */
	MultiWordSuggestOracle myListOracle;
	
	/** True if the type of list includes suggestions */
	Boolean typeOfListIsOracle = false;
	
	/** True to order the list alphabetically */
	Boolean orderList = true;
	
	/** Name of the code list */
	private String myCodeListName;
	
	/** Default value */
	private String myDefaultValue;

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
	 */
	public void onSuccess(String result) {
    	if (result == null)
    		return;
    	if (typeOfListIsOracle) 
    		Utilities.setCodeList(result, myListOracle, orderList);
    	else	
    		Utilities.setCodeList(result, myList, myDefaultValue, orderList);
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
     */
    public void onFailure(Throwable caught) {
    	if (EUOSMEGWT.codelistAvailable) {
    		EUOSMEGWT.codelistAvailable=false;
    		EUOSMEGWT.rpcCodeList=false;
    		iso19115Constants constants = GWT.create(iso19115Constants.class);
    		String msg=caught.getMessage();
    		if (msg != null)
    			Window.alert(constants.codelistServiceFailed() + " " + msg);
    	}	
    	// set OFF-LINE code lists
    	if (typeOfListIsOracle) 
    		Utilities.setCodeList(Utilities.getResourceCodeList(myCodeListName), myListOracle, orderList);
    	else	
    		Utilities.setCodeList(Utilities.getResourceCodeList(myCodeListName), myList, myDefaultValue, orderList);    	
    }
	
    /**
     * @param myList			{@link ListBox} = the list box to populate
     * @param myDefaultValue	{@link String} = the default value
     */
    public void setList(ListBox myList, String myDefaultValue) {    	
		this.myList = myList;
		this.myDefaultValue = myDefaultValue;
		typeOfListIsOracle = false;
	}

	/**
	 * @param codeListName	{@link String} = the name of the code list
	 */
	public void setCodeListName(String codeListName) {
		this.myCodeListName=codeListName;		
	}

	/**
	 * @param myListOracle	{@link MultiWordSuggestOracle} = the suggestions object
	 */
	public void setList(MultiWordSuggestOracle myListOracle) {
		this.myListOracle = myListOracle;
		typeOfListIsOracle = true;
	}

	public void setOrderList(boolean order) {
		this.orderList = order;		
	}
}