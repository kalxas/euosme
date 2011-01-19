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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;

import eu.europa.ec.jrc.euosme.gwt.client.MyResources;
import eu.europa.ec.jrc.euosme.gwt.client.Utilities;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;

/**
 * Contains the implementation of the callback class from GEMET Web service
 * 
 * @version 3.0 - January 2011
 * @author 	Marzia Grasso 
 */
public class SuggestListCallback implements AsyncCallback <String> {
	
	/** the list box to populate */
	static ListBox myList;
	
	/** Constants declaration */
	private iso19115Constants constants = GWT.create(iso19115Constants.class);
	
    /* (non-Javadoc)
     * @see com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
     */
    public void onSuccess(String result) {
    	if (result == null)
    		return;
    	if (result.equalsIgnoreCase("AUTHENTICATIONFAILED")) {
    		myList.clear();
        	myList.addItem(constants.repositoryServiceFailed(), "");
        	myList.getElement().getStyle().setColor("red");        	
    	}
    	else Utilities.setSuggestList(result, myList);
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
     */
    public void onFailure(Throwable caught) {
    	Utilities.setSuggestList(MyResources.INSTANCE.repositoryList().getText(), myList);    	
    }
	
    /**
     * @param newList	{@link ListBox} = the list box to populate
     */
    public static void setList(ListBox newList) {    	
		myList = newList;		
	}   
}