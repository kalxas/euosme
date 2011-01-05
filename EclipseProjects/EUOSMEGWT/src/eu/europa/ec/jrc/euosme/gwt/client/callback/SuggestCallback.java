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
import com.google.gwt.user.client.ui.TreeItem;
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
 * @version 2.0 - December 2010
 * @author 	Marzia Grasso 
 */
public class SuggestCallback implements AsyncCallback <String> {
	
	/** the list box to populate */
	TreeItem myList;
	
	/** Constants declaration */
	private iso19115Constants constants = GWT.create(iso19115Constants.class);
	
    /* (non-Javadoc)
     * @see com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
     */
    public void onSuccess(String result) {
    	if (result == null)
    		return;
    	processResponse(result);
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
     * Populate the list with the definitions from the RPC
     * 
     * @param response	{@link String} = the JSON result of the RPC
     */
    protected void processResponse(String response) {
    	
    	Document messageDom = XMLParser.parse(response);

        NodeList nodes = messageDom.getElementsByTagName("result");
        Map<String,String> definitions = new LinkedHashMap<String, String>();
        for (int i = 0; i < nodes.getLength(); i++) {
			Node currentNode = nodes.item(i);
			String definition_en = "";
			String definition_lang = "";
			String definition_uri = "";
			String definition_avuri = "";
			for (int j = 0; j < currentNode.getChildNodes().getLength(); j++) {
				Node currentItem = currentNode.getChildNodes().item(j);
				// uri concept
				if (currentItem.getNodeName().equalsIgnoreCase("binding") && currentItem.getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase("c")) {
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
				// literal in language
				if (currentItem.getNodeName().equalsIgnoreCase("binding") && currentItem.getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase("a")) {
					if (currentItem.getChildNodes().getLength() >= 0) {
						if (currentItem.getChildNodes().item(0).getNodeName().equalsIgnoreCase("literal"))
							definition_lang = currentItem.getChildNodes().item(0).getFirstChild().getNodeValue();
					}
				}  
				// does it have narrower?
				if (currentItem.getNodeName().equalsIgnoreCase("binding") && currentItem.getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase("d")) {
					if (currentItem.getChildNodes().getLength() >= 0) {
						if (currentItem.getChildNodes().item(0).getNodeName().equalsIgnoreCase("uri"))
							definition_avuri = currentItem.getChildNodes().item(0).getFirstChild().getNodeValue();
					}
				}
			}
			if (!definition_en.isEmpty()) {
				String definition;
				definition = definition_en; 
				if (!definition_lang.isEmpty() && definition_lang != definition) definition = definition_lang;
				if (!definition_avuri.isEmpty()) definition_uri = "1" + definition_uri;
				else definition_uri = "0" + definition_uri;
				definitions.put(definition,definition_uri);					
			}
		}
        if (definitions.size() >=0) {
    		definitions = Utilities.sortMapByKey(definitions);
    		Iterator<String> i = definitions.keySet().iterator();
    		while (i.hasNext()) {
	    		String definition = i.next();
	    		String definition_uri = definitions.get(definition);
	    		TreeItem s = new TreeItem();
				if (!definition_uri.isEmpty()) s.setTitle(definition_uri.substring(1));
				s.setText(definition);				
				if (definition_uri.substring(0,1).equalsIgnoreCase("1")) s.addItem(constants.loading());
				myList.addItem(s);	    				
		    }
    		if (myList.getChild(0) != null)
    			if (myList.getChild(0).getTitle().equalsIgnoreCase(constants.loading())) myList.getChild(0).remove();
    		myList.getTree().ensureSelectedItemVisible();
    	}
	}
	
    /**
     * @param myTreeItem	{@link TreeItem} = the tree item to populate
     */
    public void setList(TreeItem myTreeItem) {    	
		this.myList = myTreeItem;		
	}   
}