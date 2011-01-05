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
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import eu.europa.ec.jrc.euosme.gwt.client.EUOSMEGWT;
import eu.europa.ec.jrc.euosme.gwt.client.Utilities;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;

/**
 * Contains the implementation of the callback class from GEMET Web service
 * 
 * @version 2.0 - October 2010
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
    	else processResponse(result);
    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
     */
    public void onFailure(Throwable caught) {
    	if (EUOSMEGWT.repositoryAvailable) {
    		EUOSMEGWT.repositoryAvailable=false;
    		String msg=caught.getMessage();
    		if (msg != null)
    			Window.alert(constants.repositoryServiceFailed() + " " + msg);
    	}	
    }
    
    /**
     * @param response	{@link String} = the result to parse
     */
    protected void processResponse(String response) {
    	myList.clear();
    	myList.addItem(constants.selectValue(), "");
    	
    	Document messageDom = XMLParser.parse(response);

        NodeList nodes = messageDom.getElementsByTagName("result");
        Map<String,String> definitions = new LinkedHashMap<String, String>();
        for (int i = 0; i < nodes.getLength(); i++) {
			Node currentNode = nodes.item(i);
			String definition_en = "";
			String definition_lang = "";
			String definition_uri = "";
			for (int j = 0; j < currentNode.getChildNodes().getLength(); j++) {
				Node currentItem = currentNode.getChildNodes().item(j);
				// uri concept
				if (currentItem.getNodeName().equalsIgnoreCase("binding") && currentItem.getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase("v")) {
					if (currentItem.getChildNodes().getLength() >= 0) {
						if (currentItem.getChildNodes().item(0).getNodeName().equalsIgnoreCase("uri"))
							definition_uri = currentItem.getChildNodes().item(0).getFirstChild().getNodeValue();	    						
					}
				}
				// literal in @en
				if (currentItem.getNodeName().equalsIgnoreCase("binding") && currentItem.getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase("l")) {
					if (currentItem.getChildNodes().getLength() >= 0) {
						if (currentItem.getChildNodes().item(0).getNodeName().equalsIgnoreCase("literal"))
							definition_en = currentItem.getChildNodes().item(0).getFirstChild().getNodeValue();
					}
				}
				// literal in client language
				if (currentItem.getNodeName().equalsIgnoreCase("binding") && currentItem.getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase("a")) {
					if (currentItem.getChildNodes().getLength() >= 0) {
						if (currentItem.getChildNodes().item(0).getNodeName().equalsIgnoreCase("literal"))
							definition_lang = currentItem.getChildNodes().item(0).getFirstChild().getNodeValue();
					}
				}  
			}
			if (!definition_en.isEmpty()) {
				String definition;
				definition = definition_en; 
				if (!definition_lang.isEmpty() && definition_lang != definition) definition = definition_lang;
				definitions.put(definition,definition_uri);						
			}
		}
        if (definitions.size() >=0) {
    		definitions = Utilities.sortMapByKey(definitions);
    		Iterator<String> i = definitions.keySet().iterator();
    		while (i.hasNext()) {
	    		String definition = i.next();
	    		String definition_uri = definitions.get(definition);
	    		myList.addItem(definition,definition_uri);    			
		    }	    		
    	}
	}
	
    /**
     * @param newList	{@link ListBox} = the list box to populate
     */
    public static void setList(ListBox newList) {    	
		myList = newList;		
	}   
}