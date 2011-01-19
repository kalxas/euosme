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

import java.util.Map;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import eu.europa.ec.jrc.euosme.gwt.client.EUOSMEGWT;

/**
 * Contains the implementation of the callback class from init configuration
 * 
 * @version 1.1 - January 2011
 * @author 	Marzia Grasso 
 */
public class InitRpcCallback implements AsyncCallback <Map<String,String>> {
	
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
	 */
	public void onSuccess(Map<String,String> result) {
    	if (result == null)
    		return;
    	
    	// Default metadata type loaded
    	if (result.get("metadataType")!=null)
    		EUOSMEGWT.metadataType = result.get("metadataType");
    	// Set the profile used, it enable or disable elements to be shown
    	if (result.get("appMode")!=null)
    	   	EUOSMEGWT.appMode = result.get("appMode");
    	// true, the application uses the online code lists; false, the application uses the local resources
    	if (result.get("rpcCodeList")!=null)
    	   	EUOSMEGWT.rpcCodeList = Boolean.parseBoolean(result.get("rpcCodeList"));
    	// the type of maps to show: google or openlayers
    	if (result.get("apiMapstraction")!=null)
    	   	EUOSMEGWT.apiMapstraction = result.get("apiMapstraction");
    	// true, all the disclosure panels are opened; false, the disclosure panels are opened only if the content is required
    	if (result.get("showAll")!=null)
    	   	EUOSMEGWT.showAll = Boolean.parseBoolean(result.get("showAll"));
    	if (result.get("rpcRepository")!=null)
    	   	EUOSMEGWT.rpcRepository = Boolean.parseBoolean(result.get("rpcRepository"));
    	
    	EUOSMEGWT.startup();
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
     */
    public void onFailure(Throwable caught) {
    	String msg=caught.getMessage();
    	if (msg != null)
    		Window.alert(msg);    	    	
    }
}