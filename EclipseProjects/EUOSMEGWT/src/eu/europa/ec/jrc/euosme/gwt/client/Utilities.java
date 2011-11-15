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

package eu.europa.ec.jrc.euosme.gwt.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.DOMException;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.Text;
import com.google.gwt.xml.client.XMLParser;

import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.ui.MainPanel;

/**
 * Utilities class
 * 
 * @version 5.0 - February 2011
 * @author 	Marzia Grasso
 */
public class Utilities {	
	
	/** Declare Constants */
	private static iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/**
	 * Detect Browser Type
	 * 
	 * @return	{@link String} = the user agent for the current internet browser
	 */
	public static native String getUserAgent() /*-{
		return navigator.userAgent.toLowerCase();
	}-*/;
		
	/**
	 * Change Language, opening a new window and passing the new locale name
	 * 
	 * @param newLanguage	{@link String} = code of the new language
	 */
	public static native void changeLanguage(String newLanguage) /*-{
		var myquery = $wnd.location.search;
		if (myquery.length > 0) {
			myquery = myquery.toLowerCase();
			myquery = myquery.replace("&amp;","&");
			if (myquery.indexOf("&locale=",0) > 0) {
				var x = myquery.indexOf("&locale=",0);
				var y = myquery.indexOf("&",x + "&locale=".length);
				if (y<0) myquery = myquery.substring(0,x) + "&amp;locale=" + newLanguage; 
				else myquery = myquery.substring(0,x) + "&amp;locale=" + newLanguage +  myquery.substring(y,myquery.length);	
			}
			else {
				myquery = myquery + "&amp;locale=" + newLanguage;
			}
		}
		else {
			myquery = "?locale=" + newLanguage;
		}
		$wnd.location = $wnd.location.pathname + myquery;
	}-*/;
	
	/**
	 * @return {@link String} = the current url
	 */
	public static native String getURL() /*-{
		return $wnd.location.pathname + "?locale=";
	}-*/;
	
	/**
	 * Reload window
	 */
	public static native void reload() /*-{
    	$wnd.location.reload();
	}-*/; 
	
	/**
	 * Check String function
	 * 
	 * @param myString			{@link String} = the string to check
	 * @param myCheckFunction	{@link CheckFunctions} = the type of string
	 * 
	 * @return {@link String} = the message
	 */
	public static String checkString(String myString, CheckFunctions myCheckFunction) {
		// Declare constants
		iso19115Constants constants = GWT.create(iso19115Constants.class);
		
		//Check the value if requested
		switch (myCheckFunction) {
			case electronicMailAddress:				  		  
				if (myString.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")==false)
					return constants.invalidMail();
			    break;
			case URL:
				if (myString.matches("(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?")==false) 
					return constants.invalidURL();
				break;
			case integer:
				try {
					Integer.parseInt(myString);
				}
				catch(NumberFormatException nFE) {
					return constants.invalidInteger();
				}
				break;	
			case doublenum:
				try {
					Double.parseDouble(myString);
				}
				catch(NumberFormatException nFE) {
					return constants.invalidDouble();
				}
				break;
			default:
				return "";
			}
		return "";	
	}
	
	/**
	 * Parse XML document and load data into the form widget (using DOM) and load the tree widget
	 * 
	 * @param messageXml	{@link String} = XML original document
	 * @param isNew			{@link Boolean} = true if the document is new
	 * 
	 * @return {@link String} = parsed XML
	 */
	public static String parseMessage(String messageXml, boolean isNew) {
		String ret = "";
		try {
			Document messageDom = XMLParser.parse(messageXml);
			// parse XML and load data
			if (isNew) {
				TreeItem topTreeItem = new TreeItem(messageDom.getNodeName());
	  	    	buildFromNodes(messageDom.getChildNodes(), "",topTreeItem, messageDom);
	  	    	MainPanel.myTree.addItem(topTreeItem);	  	    	
	  	    }
	  	    else {	  	    	
	  	    	ret = buildFromNodesLoad(messageDom.getChildNodes(), "", messageDom);	  	    	
	  	    }
	  	} catch (DOMException e) { }
	  	return ret;
	}
	
	
	/**
	 * Found the interface of the passed XML
	 * 
	 * @param ret	{@link String} = XML to parse
	 * 
	 * @return {@link String} = type of interface (dataset, service, series)
	 */
	public static String foundInterface(String ret) {
		Document messageDom = XMLParser.parse(ret.toLowerCase());
		NodeList scopes = messageDom.getElementsByTagName("md_scopecode");
		String scopeFound="";
		if (scopes.item(0)!=null) {
			String scopeValue = scopes.item(0).getAttributes().getNamedItem("codelistvalue").getNodeValue();
			if (scopeValue.equalsIgnoreCase("service")) scopeFound=DataTypes.DATA_SERVICE.toString();
			if (scopeValue.equalsIgnoreCase("dataset")) scopeFound=DataTypes.DATASET.toString();
			if (scopeValue.equalsIgnoreCase("series")) scopeFound=DataTypes.DATASET_SERIES.toString();
		}
		return scopeFound;
	}
	
	
	/**
	 * Create the {@link Tree} from the metadata XML chosen (dataset, data series, service).
	 * This function uses the standard template in XMLSources package.
	 * 
	 * @param nodes		{@link NodeList} = collection of nodes to iterate
	 * @param parent 	{@link String} = path of the parent node
	 * @param treeItem 	{@link TreeItem} = represents the current node in the {@link Tree}
	 * @param node 		{@link Node} = is the current node
	 */
	private static void buildFromNodes(final NodeList nodes, String parent, final TreeItem treeItem, final Node node) {
        String nodeName;
        String nodeValue;
        Node currentNode = null;
        TreeItem subTreeItem = null;		
		
        for (int i = 0; i < nodes.getLength(); i++) {
        	currentNode = nodes.item(i);
        	
        	if (currentNode.getNodeName().equalsIgnoreCase("#text") || currentNode.getNodeName().equalsIgnoreCase("xml")) continue;
        	
        	// get node name
        	subTreeItem = new TreeItem(currentNode.getNodeName());
        	Integer j = getNodePosition(currentNode, i);
        	nodeName = getNodeName(currentNode, i, parent);      
        	
        	// check the type of node
        	switch (currentNode.getNodeType()) {
            case (Node.TEXT_NODE): 
            	break;
            case (Node.ATTRIBUTE_NODE):
            	break;
            case (Node.ELEMENT_NODE):
            	// get attributes
            	if (currentNode.hasAttributes()) {
            		for(int attr=0;attr<currentNode.getAttributes().getLength();attr++) {
	            		String attributeName = "@" + currentNode.getAttributes().item(attr).getNodeName();
            			TreeItem newAttribute = new TreeItem(attributeName + "=" + currentNode.getAttributes().item(attr).getNodeValue());
	            		newAttribute.setTitle(nodeName + "." + attributeName);
            			subTreeItem.addItem(newAttribute);	
	        		}
            	}	            
            	// continue if there is no children
            	if (currentNode.getFirstChild() == null) {
            		//continue;
            		TreeItem subsubTreeItem = new TreeItem(getEmptyTreeItemValue());
            		subTreeItem.addItem(subsubTreeItem);
            		break;
            	}
            	// get node value
            	nodeValue = currentNode.getFirstChild().getNodeValue();
            	if (nodeValue != null) {
	            	nodeValue = nodeValue.replace("\n", "");
	            	nodeValue = nodeValue.replace("\t", "");
	            	nodeValue = nodeValue.trim();
	            	if (nodeValue.isEmpty()) nodeValue = null;
            	}
            	if (nodeValue!= null) {
            		TreeItem subsubTreeItem = new TreeItem(constants.XMLValue() + nodeValue);
            		subTreeItem.addItem(subsubTreeItem);
            		String ret = valueField(nodeName.toLowerCase(), nodeValue);
            		if (ret.contains("err")) GWT.log("valueField " + nodeName.toLowerCase() + "=" + nodeValue + " >> " + ret, null);
                	if (ret == null && j>1) {
                		int k = nodeName.lastIndexOf("[");
                		String buttonToInvoke = nodeName.substring(0,k) + "[1]";
                		GWT.log("add-" + buttonToInvoke, null);
                		ret = invokeAdd("add-" + buttonToInvoke);                		
                		GWT.log(" >> " + ret, null);                		
                	}
                	else if (ret==null && currentNode.getNodeName().substring(4).equalsIgnoreCase("characterstring")) {
                		int k = nodeName.lastIndexOf("[");
                		String nodeNametocheck = nodeName.substring(0,k);
        				k = nodeNametocheck.lastIndexOf("[");
        				nodeNametocheck = nodeNametocheck.substring(0,k) + "[1].characterstring[1]";
        				ret = valueField(nodeNametocheck, nodeValue);
        				if (ret.contains("err")) GWT.log("valueField " + nodeNametocheck + "=" + nodeValue + " >> " + ret, null);
                	}
                	else if (ret==null && currentNode.getNodeName().substring(4).equalsIgnoreCase("decimal")) {
                		int k = nodeName.lastIndexOf("[");
                		String nodeNametocheck = nodeName.substring(0,k);
        				k = nodeNametocheck.lastIndexOf("[");
        				nodeNametocheck = nodeNametocheck.substring(0,k) + "[1].decimal[1]";
        				ret = valueField(nodeNametocheck, nodeValue);
        				if (ret.contains("err")) GWT.log("valueField " + nodeNametocheck + "=" + nodeValue + " >> " + ret, null);
                	}
            	}
            	// if there are child nodes, iterate through them
            	if (currentNode.hasChildNodes()) {
            		buildFromNodes(currentNode.getChildNodes(), nodeName, subTreeItem, currentNode);    				
            	}
            	break;
            default:            	
            } // end of switch (currentNode.getNodeType())
            // if there is a set of tree items child of the current item, attach to it 
        	if (subTreeItem != null) {
            	subTreeItem.setTitle(nodeName);
            	//WorkAround for Issue 1783: Tree.ensureSelectedItemVisible() not working as specified
            	//subTreeItem.setState(true);
            	subTreeItem.setState(false);
        		treeItem.addItem(subTreeItem);
            	treeItem.setVisible(true);
            	//WorkAround for Issue 1783: Tree.ensureSelectedItemVisible() not working as specified
            	//treeItem.setState(true);
            	treeItem.setState(false);
            }    		
        }// end of for (int i = 0; i < nodes.getLength(); i++)
	}
	
	
	/**
	 * This function is used to load an XML chosen by the user (uploaded from the local machine)
	 * into the {@link Tree}
	 * 
	 * @param nodes		{@link NodeList} = collection of nodes to iterate
	 * @param parent 	{@link String} = path of the parent node
	 * @param node 		{@link Node} = is the current node
	 * 
	 * @return {@link String} = log with messages related to the current loading
	 */
	private static String buildFromNodesLoad(final NodeList nodes, String parent, final Node node) {
        String nodeName;
        String nodeValue;
        Node currentNode = null;
        String ret = "";
        String log = "";
        TreeItem subTreeItem = null;
        TreeItem parentItem = null;
        Boolean isMultiple = false;
        
        for (int i = 0; i < nodes.getLength(); i++) {
        	currentNode = nodes.item(i);        	

        	// Continue if the node is a text or the xml root
        	if (currentNode.getNodeName().equalsIgnoreCase("#comment") || currentNode.getNodeName().equalsIgnoreCase("#text") || currentNode.getNodeName().equalsIgnoreCase("xml")) continue;        	

        	// Get nodename
        	nodeName = getNodeName(currentNode, i, parent);        	
        	
        	// solve problem on ID for legal constraints
        	if (nodeName.startsWith("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType  + "[1].resourceconstraints[1].md_legalconstraints[1]")) nodeName = nodeName.replace(".resourceconstraints[1]",".resourceconstraints[2]");
        	
        	// Create Tree Item if there is not yet and show a message
			subTreeItem = Utilities.getSelectTreeItem(nodeName);
			if (subTreeItem==null && !(currentNode.getNodeName().toLowerCase().endsWith("md_keywords")) && !(currentNode.getNodeName().toLowerCase().endsWith("geographicelement")) && !(nodeName.startsWith("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType + "[1].citation[1].ci_citation[1].date"))) {
				parentItem = Utilities.getSelectTreeItem(parent);
				if (parentItem.getText().startsWith(constants.XMLValue())) {
					parentItem = parentItem.getParentItem();
					parentItem.removeItems();
				}
				if (parentItem!=null) {
					TreeItem newItem = new TreeItem(currentNode.getNodeName());
					newItem.setTitle(nodeName);
					newItem.setState(false);
					//try to get previous element
					try {
						Node previousSib = currentNode.getPreviousSibling();
						if (previousSib.getNodeType()==Node.TEXT_NODE) previousSib = currentNode.getPreviousSibling().getPreviousSibling();
						TreeItem previousItem = Utilities.getSelectTreeItem(getNodeName(previousSib,i-1,parent));
						if (previousItem!=null) 
							if (previousItem.getText().startsWith("#")) parentItem.insertItem(parentItem.getChildIndex(previousItem.getParentItem())+1,newItem);
							else parentItem.insertItem(parentItem.getChildIndex(previousItem)+1,newItem);
						else parentItem.addItem(newItem);
					}
					catch (Exception ex){
						parentItem.addItem(newItem);
					}
					if (currentNode.getNodeType()==Node.ELEMENT_NODE && (currentNode.getFirstChild() != null || currentNode.hasAttributes())) {
		            	if (currentNode.getFirstChild()!=null) {
		            		if (currentNode.hasAttributes()) {
		            			for(int attr=0;attr<currentNode.getAttributes().getLength();attr++) {
			            			newItem.addItem(new TreeItem("@" + currentNode.getAttributes().item(attr).getNodeName() + "=" + currentNode.getAttributes().item(attr).getNodeValue()));	
			            		}
		            		}
		            	}
		            	else
		            		for(int attr=0;attr<currentNode.getAttributes().getLength();attr++) {
		            			newItem.addItem(new TreeItem("@" + currentNode.getAttributes().item(attr).getNodeName() + "=" + currentNode.getAttributes().item(attr).getNodeValue()));	
		            		}
		          	}       
					subTreeItem=newItem;
					//show a message to tell that the data type is not supported but it will remain unchanged
					log += constants.warning() + nodeName + " " + constants.errorDataType1() + "\n";
				} else log += constants.error() + nodeName + " " + constants.errorDataType2() + "\n"; //if parentItem is null, show a message that tell the also it will not be saved
			}
			
			// set keywords
			if (currentNode.getNodeName().toLowerCase().endsWith("md_keywords")) {
				ArrayList<String> myKeywords = new ArrayList<String>();
				String mySource = "";
				String myDate = "";
				String myDateType = "";
				for (int jj = 0; jj < currentNode.getChildNodes().getLength(); jj++) {        			
					if (currentNode.getChildNodes().item(jj).getNodeName().toLowerCase().startsWith("gmd:keyword")) {
						Node keyword = currentNode.getChildNodes().item(jj);
        				//gco:CharacterString
        				for (int kk = 0; kk < keyword.getChildNodes().getLength(); kk++) { 
        					if (keyword.getChildNodes().item(kk).getNodeName().startsWith("gco:CharacterString")) {
        						String myString = null;
        						try {
        							myString = keyword.getChildNodes().item(kk).getFirstChild().getNodeValue();
        						}
        						catch (Exception ex) { }
        						if (!myString.isEmpty()){ 
        							if (!myString.equals(constants.rdsi_rdsi())
        								&& !myString.equals(constants.rdsi_inspire())
        								&& !myString.equals(constants.rdsi_cch()))  
        								myKeywords.add(myString);
        							else {
        								// add to the list (to be saved later)
        								EUOSMEGWT.rdsi_keyword.add(myString); 
        							}
        						}
        					}
        						  	
        				}
        			} //end of if (currentNode.getChildNodes().item(jj).getNodeName().toLowerCase().startsWith("gmd:keyword")) {     			        				
        			if (currentNode.getChildNodes().item(jj).getNodeName().startsWith("gmd:thesaurusName")) { 
        				Node thesaurus = currentNode.getChildNodes().item(jj);
        				for (int kk = 0; kk < thesaurus.getChildNodes().getLength(); kk++) {
        					if (thesaurus.getChildNodes().item(kk).getNodeName().startsWith("gmd:CI_Citation")) {
        						Node citation = thesaurus.getChildNodes().item(kk);
        						for (int xx = 0; xx < citation.getChildNodes().getLength(); xx++) {
	        						//read gmd:thesaurusName/gmd:CI_Citation/gmd:title/gco:CharacterString
		        					if (citation.getChildNodes().item(xx).getNodeName().startsWith("gmd:title")) {
		        						Node citation_title = citation.getChildNodes().item(xx);
		        						for (int qq = 0; qq < citation_title.getChildNodes().getLength(); qq++) {
		        							if (citation_title.getChildNodes().item(qq).getNodeName().startsWith("gco:CharacterString")) {
				        						try {
				        							mySource = citation_title.getChildNodes().item(qq).getFirstChild().getNodeValue();
				        						}
				        						catch (Exception ex) { }
		        							}
			        					}
		        					}//end of if (citation.getChildNodes().item(xx).getNodeName().startsWith("gmd:title")) {
	        						if (citation.getChildNodes().item(xx).getNodeName().startsWith("gmd:date")) {
		        						Node citation_date = citation.getChildNodes().item(xx);
		        						for (int qq = 0; qq < citation_date.getChildNodes().getLength(); qq++) {
		        							if (citation_date.getChildNodes().item(qq).getNodeName().startsWith("gmd:CI_Date")) {
		        								Node ci_date = citation_date.getChildNodes().item(qq);
		        								for (int ww = 0; ww < ci_date.getChildNodes().getLength(); ww++) {
		        									//read gmd:thesaurusName/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date
		        									if (ci_date.getChildNodes().item(ww).getNodeName().startsWith("gmd:date")) {
		        										Node keywordDate = ci_date.getChildNodes().item(ww);
		        										for (int pp = 0; pp < keywordDate.getChildNodes().getLength(); pp++) {
		        											if (keywordDate.getChildNodes().item(pp).getNodeName().startsWith("gco:Date")) {
		        				        						try {
		        				        							myDate = keywordDate.getChildNodes().item(pp).getFirstChild().getNodeValue();
		        				        						}
		        				        						catch (Exception ex) { }
		        											}
		        										}
		        									}// end of if (ci_date.getChildNodes().item(ww).getNodeName().startsWith("gmd:date")) {
		        									//read gmd:thesaurusName/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:dateType/gmd:CI_DateTypeCode/@codeListValue
		        									if (ci_date.getChildNodes().item(ww).getNodeName().startsWith("gmd:dateType")) {
		        										Node keywordDateType = ci_date.getChildNodes().item(ww);
		        										for (int pp = 0; pp < keywordDateType.getChildNodes().getLength(); pp++) {
		        											if (keywordDateType.getChildNodes().item(pp).getNodeName().startsWith("gmd:CI_DateTypeCode")) {
		        				        						try {
		        				        							myDateType = keywordDateType.getChildNodes().item(pp).getAttributes().getNamedItem("codeListValue").getNodeValue();
		        				        						}
		        				        						catch (Exception ex) { }
		        											}
		        										}		        										
		        									}//end of if (ci_date.getChildNodes().item(ww).getNodeName().startsWith("gmd:dateType")) {
		        								}// end of for (int ww = 0; ww < ci_date.getChildNodes().getLength(); ww++) {
		        							}// end of if (citation_date.getChildNodes().item(qq).getNodeName().startsWith("gmd:CI_Date")) {	        						
			        					}//end of for (int qq = 0; qq < citation_date.getChildNodes().getLength(); qq++) {
		        					}//end of if (citation.getChildNodes().item(xx).getNodeName().startsWith("gmd:date")) {
        						}//end of for (int xx = 0; xx < citation.getChildNodes().getLength(); xx++) {
        					}//end of if (thesaurus.getChildNodes().item(kk).getNodeName().startsWith("gmd:CI_Citation")) {
        				}//end of for (int kk = 0; kk < thesaurus.getChildNodes().getLength(); kk++) {        				
        			}//end of if (currentNode.getChildNodes().item(jj).getNodeName().startsWith("gmd:thesaurusName")) {
        		}//for (int jj = 0; jj < currentNode.getChildNodes().getLength(); jj++) {
				//for each gmd:keyword found, add a keyword to the list
				for (int kk = 0; kk < myKeywords.size(); kk++) {
					ret = valueField("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType  + "[1].descriptivekeywords[1].md_keywords[1].keyword[1].characterstring[1]", myKeywords.get(kk));
					ret = valueField("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType  + "[1].descriptivekeywords[1].md_keywords[1].thesaurusname[1].ci_citation[1].title[1].characterstring[1]", mySource);
					ret = valueField("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType  + "[1].descriptivekeywords[1].md_keywords[1].thesaurusname[1].ci_citation[1].date[1].ci_date[1].date[1].date[1]", myDate);
					ret = valueField("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType  + "[1].descriptivekeywords[1].md_keywords[1].thesaurusname[1].ci_citation[1].date[1].ci_date[1].datetype[1].ci_datetypecode[1]", myDateType);
					ret = invokeAdd("newButton-md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType + "[1].descriptivekeywords[1].md_keywords[1]");
				}
				continue;
			}
			
			// set dates
        	if (nodeName.startsWith("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType  + "[1].citation[1].ci_citation[1].date") && nodeName.contains("ci_date")) {
        		String dateValue = "";
        		int dateType = 0;
        		for (int jj = 0; jj < currentNode.getChildNodes().getLength(); jj++) {        			
        			if (currentNode.getChildNodes().item(jj).getNodeName().toLowerCase().startsWith("gmd:datetype")) {
        				for (int kk = 0; kk < currentNode.getChildNodes().item(jj).getChildNodes().getLength(); kk++) { 
        					if (currentNode.getChildNodes().item(jj).getChildNodes().item(kk).getNodeName().toLowerCase().startsWith("gmd:ci_datetypecode")) {
        						if (currentNode.getChildNodes().item(jj).getChildNodes().item(kk).getAttributes().getNamedItem("codeListValue").getNodeValue().equalsIgnoreCase("publication")) dateType = 1;
                				if (currentNode.getChildNodes().item(jj).getChildNodes().item(kk).getAttributes().getNamedItem("codeListValue").getNodeValue().equalsIgnoreCase("revision")) dateType = 2;
                				if (currentNode.getChildNodes().item(jj).getChildNodes().item(kk).getAttributes().getNamedItem("codeListValue").getNodeValue().equalsIgnoreCase("creation")) dateType = 3;        						
        					}
        				}
        			}
        			if (currentNode.getChildNodes().item(jj).getNodeName().toLowerCase().startsWith("gmd:date")) 
        				for (int kk = 0; kk < currentNode.getChildNodes().item(jj).getChildNodes().getLength(); kk++) { 
        					if (currentNode.getChildNodes().item(jj).getChildNodes().item(kk).getNodeName().toLowerCase().startsWith("gco:date")) {
        						dateValue = currentNode.getChildNodes().item(jj).getChildNodes().item(kk).getFirstChild().getNodeValue();
        					}
        				}        				
        		}
        		TreeItem subTreeItemBB = Utilities.getSelectTreeItem("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType  + "[1].citation[1].ci_citation[1].date[" + dateType +  "].ci_date[1].date[1].date[1]");
				if (subTreeItemBB!=null) 
					subTreeItemBB.setText(constants.XMLValue() + dateValue);            		                    			
				ret = valueField("md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType  + "[1].citation[1].ci_citation[1].date[" + dateType +  "].ci_date[1].date[1].date[1]", dateValue);
				continue;
        	}
        	
        	// set geographic element
        	if (currentNode.getNodeName().toLowerCase().endsWith("geographicelement")) {
        		for (int jj = 0; jj < currentNode.getChildNodes().getLength(); jj++) {
        			if (currentNode.getChildNodes().item(jj).getNodeName().toLowerCase().endsWith("ex_geographicboundingbox")) {	
        				Node ex_geographicboundingbox = currentNode.getChildNodes().item(jj);        				
	        			String northValue = null;
	    	        	String eastValue = null;
	    	        	String southValue = null;
	    	        	String westValue = null;    	        	
	        			for (int ii = 0; ii < ex_geographicboundingbox.getChildNodes().getLength(); ii++) {
	        	        	Node currentNodeBB = ex_geographicboundingbox.getChildNodes().item(ii);        	
	        	        	if (currentNodeBB.getNodeName().toLowerCase().endsWith("northboundlatitude")) {
	        	        		for (int kk = 0; kk < currentNodeBB.getChildNodes().getLength(); kk++) {
	        	        			Node currentNodeDecimal = currentNodeBB.getChildNodes().item(kk);  
	        	        			if (currentNodeDecimal.getNodeName().toLowerCase().endsWith("decimal")) {
	        	        				northValue = currentNodeDecimal.getFirstChild().getNodeValue();
	        	        			}
	        	        		}	        	        			
	        	        	}
	        	        	if (currentNodeBB.getNodeName().toLowerCase().endsWith("eastboundlongitude")) {
	        	        		for (int kk = 0; kk < currentNodeBB.getChildNodes().getLength(); kk++) {
	        	        			Node currentNodeDecimal = currentNodeBB.getChildNodes().item(kk);  
	        	        			if (currentNodeDecimal.getNodeName().toLowerCase().endsWith("decimal")) {
	        	        				eastValue = currentNodeDecimal.getFirstChild().getNodeValue();
	        	        			}
	        	        		}
	        	        	} 
	        	        	if (currentNodeBB.getNodeName().toLowerCase().endsWith("southboundlatitude")) {
	        	        		for (int kk = 0; kk < currentNodeBB.getChildNodes().getLength(); kk++) {
	        	        			Node currentNodeDecimal = currentNodeBB.getChildNodes().item(kk);  
	        	        			if (currentNodeDecimal.getNodeName().toLowerCase().endsWith("decimal")) {
	        	        				southValue = currentNodeDecimal.getFirstChild().getNodeValue();
	        	        			}
	        	        		}
	        	        	} 
	        	        	if (currentNodeBB.getNodeName().toLowerCase().endsWith("westboundlongitude")) {
	        	        		for (int kk = 0; kk < currentNodeBB.getChildNodes().getLength(); kk++) {
	        	        			Node currentNodeDecimal = currentNodeBB.getChildNodes().item(kk);  
	        	        			if (currentNodeDecimal.getNodeName().toLowerCase().endsWith("decimal")) {
	        	        				westValue = currentNodeDecimal.getFirstChild().getNodeValue();
	        	        			}
	        	        		}
	        	        	} 
	        			}
	        			if (northValue!=null && eastValue!=null && southValue!=null && westValue!=null) {
	        				TreeItem subTreeItemBB = null;
	        				subTreeItemBB = Utilities.getSelectTreeItem(nodeName + ".ex_geographicboundingbox[1].northboundlatitude[1].decimal[1]");
	        				if (subTreeItemBB!=null) {
	        					if (subTreeItemBB.getText().startsWith(constants.XMLValue()))      				
	            					subTreeItemBB.setText(constants.XMLValue() + northValue);
	                    		else			
	                    			subTreeItemBB.addItem(constants.XMLValue() + northValue);                    			
	        				}
	        				ret = valueField("new-north-md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType  + "[1].extent[1].ex_extent[1].geographicelement[1]", northValue);
	                		
	        				subTreeItemBB = Utilities.getSelectTreeItem(nodeName + ".ex_geographicboundingbox[1].eastboundlongitude[1].decimal[1]");
	                		if (subTreeItemBB!=null) {
	                			if (subTreeItemBB.getText().startsWith(constants.XMLValue()))      				
	                				subTreeItemBB.setText(constants.XMLValue() + eastValue);
	                    		else			
	                    			subTreeItemBB.addItem(constants.XMLValue() + eastValue);                  			
	        				}                		
	                		ret = valueField("new-east-md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType  + "[1].extent[1].ex_extent[1].geographicelement[1]", eastValue);
	                		
	                		subTreeItemBB = Utilities.getSelectTreeItem(nodeName + ".ex_geographicboundingbox[1].southboundlatitude[1].decimal[1]");
	                		if (subTreeItemBB!=null) {
	                			if (subTreeItemBB.getText().startsWith(constants.XMLValue()))      				
	                				subTreeItemBB.setText(constants.XMLValue() + southValue);
	                    		else			
	                    			subTreeItemBB.addItem(constants.XMLValue() + southValue);         			
	        				}                 		
	                		ret = valueField("new-south-md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType  + "[1].extent[1].ex_extent[1].geographicelement[1]", southValue);
	                		
	                		subTreeItemBB = Utilities.getSelectTreeItem(nodeName + ".ex_geographicboundingbox[1].westboundlongitude[1].decimal[1]");
	                		if (subTreeItemBB!=null) {
	                			if (subTreeItemBB.getText().startsWith(constants.XMLValue()))      				
	            					subTreeItemBB.setText(constants.XMLValue() + westValue);
	                    		else			
	                    			subTreeItemBB.addItem(constants.XMLValue() + westValue);                    			
	                		}
	                		ret = valueField("new-west-md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType  + "[1].extent[1].ex_extent[1].geographicelement[1]", westValue);
	                		
	                		ret = invokeAdd("newButton-md_metadata[1].identificationinfo[1]." + MainPanel.identificationInfoSubType  + "[1].extent[1].ex_extent[1].geographicelement[1]");        		
	                		if (ret.contains("err")) {
	                			log += constants.error() + nodeName + " " + constants.errorDataTypeMultiple() + "\n";
	                		}                		
	        			}
        			}
            	}        
        		continue;
        	}
        	// set distance
        	if (currentNode.getNodeName().toLowerCase().endsWith("gmd:distance")) {
        		isMultiple=true;
        		nodeValue = "";
        		try {
        			nodeValue = currentNode.getChildNodes().item(1).getAttributes().getNamedItem("uom").getNodeValue();	
        		}
        		catch (Exception ex){ }        		
        		ret = valueField(nodeName.toLowerCase() + ".distance[1].@uom", nodeValue);
        		if (ret.contains("err")) {
        			log += showError(nodeName + ".@uom",ret);
        		}
        	}    
        	// set corresponding
        	if (nodeName.equalsIgnoreCase("MD_Metadata[1].dateStamp[1].datetime[1]")) {
        		log += constants.errorDateTimeNotSupported() + "\n";
        		nodeName="md_metadata[1].datestamp[1].date[1]";
        	}
        	
        	// set multiple fields        	
        	if (currentNode.getNodeName().toLowerCase().endsWith("md_resolution")) isMultiple=true;
        	if (currentNode.getNodeName().toLowerCase().endsWith("ex_temporalextent")) isMultiple=true;
        	if (currentNode.getNodeName().toLowerCase().endsWith("dq_domainconsistency")) isMultiple=true;
        	if (currentNode.getNodeName().toLowerCase().endsWith("spatialresolution")) isMultiple=true;
        	if (currentNode.getNodeName().toLowerCase().endsWith("dq_element")) isMultiple=true;
        	if (currentNode.getNodeName().toLowerCase().endsWith("tm_primitive")) isMultiple=true;
        	if (currentNode.getNodeName().toLowerCase().endsWith("ci_onlineresource")) isMultiple=true;
        	if (currentNode.getNodeName().toLowerCase().endsWith("rs_identifier")) isMultiple=true;
        	if (currentNode.getNodeName().toLowerCase().endsWith("ci_responsibleparty")) isMultiple=true;
        	
        	// Add element if required (simulating click on add button)
        	int parent_j = 1;
        	if (parent!="") parent_j = Integer.parseInt(parent.substring(parent.lastIndexOf("[")+1,parent.length()-1));
        	if (parent_j>1 && isMultiple==true) {
        		String buttonToInvoke = parent.substring(0,parent.lastIndexOf("[")) + "[1]";
        		GWT.log("add-" + buttonToInvoke);
        		ret = invokeAdd("add-" + buttonToInvoke);        		
        		if (ret.contains("err")) {
        			log += constants.error() + nodeName + " " + constants.errorDataTypeMultiple() + "\n";
        		}
        	}
        	
        	// Get the node value
        	switch (currentNode.getNodeType()) {
            case (Node.TEXT_NODE): 
            	break;
            case (Node.ATTRIBUTE_NODE):
            	break;
            case (Node.ELEMENT_NODE):
           		// check if it's has attributes
        		if (currentNode.hasAttributes()) {
        			for(int attr=0;attr<currentNode.getAttributes().getLength();attr++) {
        				TreeItem subTreeItemAttr = Utilities.getSelectTreeItem(nodeName + ".@" + currentNode.getAttributes().item(attr).getNodeName());
                		if (subTreeItemAttr!=null) subTreeItemAttr.setText("@" + currentNode.getAttributes().item(attr).getNodeName() + "=" + currentNode.getAttributes().item(attr).getNodeValue());
                    	
                		// set specification link 
                    	if (currentNode.getAttributes().item(attr).getNodeName().equalsIgnoreCase("xlink:href")) {       		
                    		ret = valueField(nodeName.toLowerCase() + ".@xlink:href", currentNode.getAttributes().item(attr).getNodeValue());
                    		if (ret.contains("err")) {
                    			log += showError(nodeName + ".@xlink:href",ret);
                    		}
                    	}                 		
            		}
        		}
            
            	if (currentNode.getFirstChild() == null) {
            		if (currentNode.hasAttributes())
            			if (currentNode.getAttributes().getNamedItem("codeListValue")!=null)
            				nodeValue = currentNode.getAttributes().getNamedItem("codeListValue").getNodeValue();
            			else break;
            		else break;
            	}
            	else nodeValue = currentNode.getFirstChild().getNodeValue();
            	if (nodeValue != null) {
	            	nodeValue = nodeValue.replace("\n", "");
	            	nodeValue = nodeValue.replace("\t", "");
	            	nodeValue = nodeValue.trim();
	            	if (nodeValue.isEmpty()) nodeValue = null;
            	}
            	if (nodeValue!= null) {
            		if (subTreeItem.getText().startsWith(constants.XMLValue()))      				
        				subTreeItem.setText(constants.XMLValue() + nodeValue);
            		else			
            			subTreeItem.addItem(constants.XMLValue() + nodeValue);
            		ret = valueField(nodeName.toLowerCase(), nodeValue);
            		if (ret.contains("err=2")) {            			     			
                    	log += showError(nodeName,ret);
            		}
            		if (!ret.isEmpty()) {
            			int k = nodeName.lastIndexOf("[");
                		String nodeNametocheck = nodeName.substring(0,k);
        				k = nodeNametocheck.lastIndexOf("[");
        				nodeNametocheck = nodeNametocheck.substring(0,k) + "[1]." + currentNode.getNodeName().substring(4).toLowerCase() + "[1]";
        				ret = valueField(nodeNametocheck, nodeValue);
        				if (ret.contains("err=2")) {            			     			
                        	log += showError(nodeName,ret);
                		}
                	}
            		if (!ret.isEmpty()) { //WORKAROUND FOR THE BUG: textarea is not valued
            			Element testTextArea = com.google.gwt.dom.client.Document.get().getElementById(nodeName);
            			if (testTextArea!=null) {
            				String tn = testTextArea.getTagName();
            				if (tn.equalsIgnoreCase("textarea")) testTextArea.setInnerText(nodeValue);
            			}
            				
            		}    
            	}
            	if (currentNode.hasChildNodes()) {				
            		log += buildFromNodesLoad(currentNode.getChildNodes(), nodeName, currentNode);            		
            	}
            	break;
            default:            	
            }          
        }
        return log;   
	}
		
	/**
	 * Construct the path of the node
	 * 
	 * @param currentNode	{@link Node} = current node
	 * @param i 			{@link Integer} = get the node starting from this position
	 * @param parent 		{@link String} = the path of the parent
	 * 
	 * @return {@link String} = the path related to the current node
	 */
	private static String getNodeName(Node currentNode, int i, String parent) {
		Integer j = getNodePosition(currentNode, i);        	
		String nodeName = "";
    	if (parent.isEmpty()) nodeName = currentNode.getNodeName().substring(4).toLowerCase() + "[" + j.toString() + "]";
    	else nodeName = parent + "." + currentNode.getNodeName().substring(4).toLowerCase() + "[" + j.toString() + "]";
    	return nodeName;
	}

	/**
	 * Get the position of the node to create the path
	 * 
	 * @param currentNode 	{@link Node} = the current node
	 * @param currentPos 	{@link Integer} = the starting position
	 * 
	 * @return {@link Integer} = position of the node
	 */
	private static Integer getNodePosition(Node currentNode, int currentPos) {
		Node parentNode = currentNode.getParentNode();
		Integer count = 0;
		for (int j = currentPos; j>=0; j--) {
			if (parentNode.getChildNodes().item(j).getNodeName().equals(currentNode.getNodeName())) count++;
		}
		return count;
	}
	
	/**
	 * JSNI function to simulate the button click
	 * 
	 * @param Button 	{@link String} = the ID of the button to click
	 * 
	 * @return {@link String} = err if the button is not in the DOM or if there was some problems; empty if the operation was successfull
	 */
	private native static String invokeAdd(String Button) /*-{
		try {
			var el = $doc.getElementById(Button);
		}
		catch(ex) {
			return "err";	
		}
		try {
			el.click();
		}
		catch(ex) {
			return "err";
		}
		return "";
	}-*/;
		
	/**
	 * JSNI funtion to simulate the setting of the value of a HTML input box
	 * 
	 * @param name 	{@link String} = the ID of the HTML input box (such as a textbox, a select, a textarea)
	 * @param value {@link String} = the value
	 * 
	 * @return {@link String} = err... if the operation was canceled (the element is not in the DOM...); empty if the operation was successfull
	 */
	public native static String valueField(String name, String value) /*-{
	 	try {
	 		var el = $doc.getElementsByName(name);
	 	}
	 	catch(ex) {
			return "err=1";
		}
	 	try {
	 		var myEl = el[0];
	 	}
	 	catch(ex) {
			return "err=1";
		}
		if (myEl==null) {
			myEl = $doc.getElementById(name);
		}
		if (myEl) {
			if (myEl.tagName=="INPUT") {
	 	 		//CharacterString or DateImpl
		 		try {
		 			myEl.value=value;
		 			return "";
				}
				catch(ex) {		 		
		 			return "err=1";
				}
		 	}
			if (myEl.tagName=="SELECT") {
		 		if (myEl.style.display=="none") {
		 			//CharacterStringMultiple
		 			try {
		 				//Try to find the text box to add a new value
						var el = $doc.getElementById("new-" + name);
					}
					catch(ex) {
						myEl.blur();
			 			myEl.style.display="";
				 		var y = $doc.createElement('option');
						y.text=value;
						y.value=value;
						y.selected=true;
						try {
							myEl.add(y,null); // standards compliant						
						}
						catch(ex) {
							myEl.add(y); // IE only
						}				
						myEl.focus(); //this could (sometimes does not work) invoke CharacterStringMultiple.myListBox.addFocusListener()
						myEl.style.display="none";				
						return "";	
					}
					el.value=value;
					try {
						var elButton = $doc.getElementById("newButton-" + name);
						elButton.click();
						return "";	
					}
		 			catch(ex) {
						myEl.blur();
			 			myEl.style.display="";
				 		var y = $doc.createElement('option');
						y.text=value;
						y.value=value;
						y.selected=true;
						try {
							myEl.add(y,null); // standards compliant						
						}
						catch(ex) {
							myEl.add(y); // IE only
						}				
						myEl.focus();
						myEl.style.display="none";				
						return "";	
					}
					return null;	 			
		 		}
		 		else { //else NOT (myEl.style.display=="none")
		 			for (var i=0;i<myEl.length;i++) {
						if (myEl.options[i].value == value) { 
							myEl.options[i].selected = true;
							return "";
						}
		 			}
		 			return "err=2";
		 		} // end of (myEl.style.display=="none") 		
			} // end of (myEl.tagName=="SELECT")
			return "err=1";
		}
		else return "err=1"; // NOT (myEl)					
  	}-*/;
	
	/**
	 * JSNI function to get the value of an input HTML field
	 * 
	 * @param name	{@link String} = the ID of the input field
	 * 
	 * @return {@link String} = empty string, if there were errors; the value of the field if the reading operation was successfull
	 */
	public native static String getValueField(String name) /*-{
	 	try {
	 		var el = $doc.getElementsByName(name);
	 	}
	 	catch(ex) {
			return "";
		}
	 	try {
	 		var myEl = el[0];
	 	}
	 	catch(ex) {
			return "";
		}
		if (myEl==null) {
			myEl = $doc.getElementById(name);
		}
		if (myEl) return myEl.value;	
		else return "";					
	}-*/;
	
	/**
	 * Select a specified item in the {@link Tree}
	 * 
	 * @param name	{@link String} = the path of the item to select
	 */
	public static void selectTreeItem(String name) {
		TreeItem val = null;
		val = getSelectTreeItem(name);
		if (val!=null) {
			val.setSelected(true);
			val.setVisible(true);
			MainPanel.myTree.setSelectedItem(null);
		 	MainPanel.myTree.setSelectedItem(val, false);
		 	MainPanel.myTree.ensureSelectedItemVisible();
		} 	   	    	 
	}
	
	/**
	 * Get a specified {@link TreeItem}
	 * 
	 * @param name	{@link String} = the path of the item
	 * 
	 * @return {@link TreeItem} = the specified tree item or null if the item is not in the tree
	 */
	public static TreeItem getSelectTreeItem(String name) {
		if (name.isEmpty()) return null;
		// if the item is already selected and correspond to the requested name, return it 
		if (MainPanel.myTree.getSelectedItem()!=null) {
			if(MainPanel.myTree.getSelectedItem().getTitle().equalsIgnoreCase(name)) return MainPanel.myTree.getSelectedItem();
		}
		// iterate over the Tree and found the requested item
		Iterator<TreeItem> it = MainPanel.myTree.treeItemIterator();
		while (it.hasNext()) {
	       TreeItem val = it.next();
	       String treeTitle = val.getTitle();
	       if (treeTitle.equalsIgnoreCase(name)) {
	    	   if (val.getChildCount()!=0) {
	    		   for (int i=0;i<val.getChildCount();i++){
	    			   if (val.getChild(i).getText().startsWith(constants.XMLValue())) return(val.getChild(i));   
	    		   }	    		   
	    	   }
	    	   return val;
	       }
		}
		return null;
	}
	
	/**
	 * 
	 * @param title The title of the specification, might include the date
	 * @return the URL of the specification
	 */
	public static String getURLSpecification(String title){
		String ret = "";
		if (title.contains("2008-12-04"))
			//1.Commission Regulation (EC) No 1205/2008 of 3 December 2008 implementing Directive 2007/2/EC of the European Parliament and of the Council as regards metadata
			ret = "http://eur-lex.europa.eu/LexUriServ/LexUriServ.do?uri=CELEX:32008R1205:EN:NOT";
		else if	(title.contains("2009-12-15"))
			//2.Corrigendum to Commission Regulation (EC) No 1205/2008 of 3 December 2008 implementing Directive 2007/2/EC of the European Parliament and of the Council as regards metadata
			ret = "http://eur-lex.europa.eu/LexUriServ/LexUriServ.do?uri=CELEX:32008R1205R%2802%29:EN:NOT";
		else if	(title.contains("2010-12-08")){
		    if (title.contains("2007/2"))	
			//3. Commission Regulation (EU) No 1089/2010 of 23 November 2010 implementing Directive 2007/2/EC of the European Parliament and of the Council as regards interoperability of spatial data sets and services
			ret = "http://eur-lex.europa.eu/LexUriServ/LexUriServ.do?uri=CELEX:32010R1089:EN:NOT";
		    else if	(title.contains("976/2009"))
				//4. Commission Regulation (EU) No 1088/2010 of 23 November 2010 amending Regulation (EC) No 976/2009 as regards download services and transformation services
				ret = "http://eur-lex.europa.eu/LexUriServ/LexUriServ.do?uri=CELEX:32010R1088:EN:NOT";
		}		
		else if	(title.contains("2009-10-20"))
			//5. Commission Regulation (EC) No 976/2009 of 19 October 2009 implementing Directive 2007/2/EC of the European Parliament and of the Council as regards the Network Services
			ret = "http://eur-lex.europa.eu/LexUriServ/LexUriServ.do?uri=CELEX:32009R0976:EN:NOT";
		else if	(title.contains("2010-03-30"))
			//6. Commission Regulation (EU) No 268/2010 of 29 March 2010 implementing Directive 2007/2/EC of the European Parliament and of the Council as regards the access to spatial data sets and services of the Member States by Community institutions and bodies under harmonised conditions
			ret = "http://eur-lex.europa.eu/LexUriServ/LexUriServ.do?uri=CELEX:32010R0268:EN:NOT";
		else if	(title.contains("2009-06-11"))
			//7. Commission Decision of 5 June 2009 implementing Directive 2007/2/EC of the European Parliament and of the Council as regards monitoring and reporting (notified under document number C(2009) 4199) (2009/442/EC)
			ret = "http://eur-lex.europa.eu/LexUriServ/LexUriServ.do?uri=CELEX:32009D0442:EN:NOT";
		
		// point to localized version
		String countryCode = LocaleInfo.getCurrentLocale().getLocaleName().toUpperCase();
		ret = ret.replaceAll(":EN:NOT", ":" + countryCode + ":NOT");
		
		return ret;
	}
		
	/**
	 * Close all the branches (tree items) of the {@link Tree}
	 */
	public static void clearOpenState() {
		Iterator<TreeItem> it = MainPanel.myTree.treeItemIterator();
		while (it.hasNext()) {
	       TreeItem val = it.next();
	       val.setState(false);
		}
	}

	/**
	 * Parse the {@link Tree} and return the resulting XML
	 * 
	 * @param node	{@link TreeItem} = the tree to parse
	 * 
	 * @return {@link String} = the XML structure
	 */
	public static String parseTree(final TreeItem node) {
		String result = "";		
		for (int i = 0; i < node.getChildCount(); i++) {
		   TreeItem val = node.getChild(i);
		   String myName = val.getText();
		   int nr_attributes = 0;
		   boolean hasAttributes = false;
		   boolean hasValue = false;
		   String myAttributes = "";
		   String myValue = "";
		   if (myName.startsWith("@")) continue;
		   if (myName.startsWith(constants.XMLValue())) {
			   myValue = clearValue(myName);
			   if (myValue.isEmpty()) continue;
			   else {
				   result += myValue;
				   continue;
			   }
		   }
		   // find attributes and the text value		  
		   if (val.getChildCount()>0) {
			   for (int j=0; j < val.getChildCount(); j++) {
				   if (val.getChild(j).getText().startsWith("@")) {
					   // add attribute
					   String[] attrValues = val.getChild(j).getText().split("=");
					   if (attrValues.length == 1)  
						   myAttributes += " " + attrValues[0].substring(1) + "=\"\"";
					   else if(attrValues.length == 2)
						   myAttributes += " " + attrValues[0].substring(1) + "=\"" + attrValues[1] + "\"";
					   else if(attrValues.length > 2) {
						   myAttributes += " " + attrValues[0].substring(1) + "=\"" + attrValues[1];
						   for(int ja=2;ja<attrValues.length;ja++)
							   myAttributes += "=" + attrValues[ja];
						   myAttributes += "\"";
					   }
					   nr_attributes++;
					   hasAttributes = true;
				   }
				   else if (val.getChild(j).getText().startsWith(constants.XMLValue())) {
					   myValue = val.getChild(j).getText();
					   hasValue = true;
				   }
			   }			   
		   }
		   // has attributes and a value (no children)
		   if (hasAttributes && val.getChildCount()==(nr_attributes+1) && hasValue) {
			   result += "<" + myName + myAttributes;
			   myValue = clearValue(myValue);
			   if (myValue.isEmpty()) result += "/>\n";
			   else result += ">" + myValue + "</" + myName + ">\n";
		   }
		   // has attributes and a value (no children)
		   else if (hasAttributes && val.getChildCount()==(nr_attributes+1) && !hasValue) {
			   boolean hasUsefulChildren = checkChildren(val);
			   if (hasUsefulChildren) {
				   result += "<" + myName + myAttributes + ">\n";
				   result += parseTree(val);
				   result += "</" + myName + ">\n";
			   }	
			   else
				   result += "<" + myName + myAttributes + "/>\n";
		   }
		   // has attributes but no value and no children
		   else if (hasAttributes && val.getChildCount()==nr_attributes) {
			   result += "<" + myName + myAttributes + "/>\n";			   
		   }
		   else if (hasAttributes && val.getChildCount()>(nr_attributes+1)) {
			   boolean hasUsefulChildren = checkChildren(val);
			   if (hasUsefulChildren) {
				   result += "<" + myName + myAttributes + ">\n";
				   result += parseTree(val);
				   result += "</" + myName + ">\n";
			   }				  
			   else
				   result += "<" + myName + myAttributes + "/>\n";
		   }
		   // has not attributes and only one children (could be a value (text) or a node)
		   else if (!hasAttributes && val.getChildCount() == 1) {
			   if (val.getChild(0).getChildCount()==0) { // has not children
				   myValue = clearValue(myValue);
				   if (!myValue.isEmpty()) result += "<" + myName + ">" + myValue + "</" + myName + ">\n";
				   //else result += "<" + myName + "/>\n";
			   }
			   else { // has children
				   boolean hasUsefulChildren = checkChildren(val);
				   if (hasUsefulChildren) {
					   result += "<" + myName + ">\n";
					   result += parseTree(val);
					   result += "</" + myName + ">\n";
				   }
			   }	
		   }
		   // has not attributes and has children
		   else if (!hasAttributes && val.getChildCount() > 1) {
			   boolean hasUsefulChildren = checkChildren(val);
			   if (hasUsefulChildren) {
				   result += "<" + myName + myAttributes + ">\n";
				   result += parseTree(val);
				   result += "</" + myName + ">\n";
			   }
		   }	
		   else { // has not attributes and not children
			   //result += "<" + myName + "/>\n";
		   }
		   
		}
		return result;
	}

	/**
	 * Chech if there are meaningful children
	 * 
	 * @param val	{@link TreeItem} = the tree item to check
	 * 
	 * @return {@link Boolean} = true if there are meaningful children
	 */
	private static boolean checkChildren(TreeItem val) {
		for (int i=0; i < val.getChildCount(); i++) {
			if (val.getChild(i).getText().startsWith("@")) {
				if (val.getChild(i).getText().startsWith("@codeList")) {
					// if its a codeList check the value
					if (val.getChild(i).getText().equalsIgnoreCase("@codeListValue")) {
						if (val.getChild(i).getText().split("=").length==2) return true;												  	
					}
				} // it must have at least an attribute (empty attribute is accepted)
				else return true;
			}
			if (val.getChild(i).getText().startsWith(constants.XMLValue())) {
				// it must have at least a value (text node)
				String ret = clearValue(val.getChild(i).getText());
				if (!ret.isEmpty()) return true;
			}
			if (val.getChild(i).getChildCount() > 0) {
				// check children
				boolean cond = checkChildren(val.getChild(i));
				if (cond) return true;
			}
		}
		return false;
	}

	/** Clean up the value and give back a valid XML code
	 * 
	 * @param val	{@link String} = the value to clean up
	 * 
	 * @return {@link String} = the cleaned value
	 */
	private static String clearValue(String val){
		if (val.equalsIgnoreCase("#value = #EMPTY")) return "";
		String myVal = val.replace(constants.XMLValue(), "").trim();
		myVal = myVal.replace(constants.emptyXMLValue(),"").trim();		
		Document document= XMLParser.createDocument();
		Text textNode= document.createTextNode(myVal);
		myVal=textNode.toString();
		// Fixes to known bug (issue 1011: XMLParser.parse() StackTraces with "Reference to undefined entity 'semi'...".  http://code.google.com/p/google-web-toolkit/issues/detail?id=1011)
		myVal = myVal.replaceAll("&semi;", ";");
		return myVal;
	}
	
	/**
	 * OnBlur event of a form field, set text if it is changed
	 * 
	 * @param myTreeItem	{@link TreeItem} = the tree item to set
	 * @param text 			{@link String} = the value to put
	 */
	public static void setTextTreeItem(TreeItem myTreeItem, String text) {
		if (myTreeItem!=null) {
			if (myTreeItem.getTitle().contains("@")) {
				// it's an attribute
				String[] attrValues = myTreeItem.getText().split("=");
				myTreeItem.setText(attrValues[0] + "=" + text); 		
			}
			else {
				if (text.isEmpty()) myTreeItem.setText(getEmptyTreeItemValue());
				else myTreeItem.setText(constants.XMLValue() + text);
			}			
		}
	}	
	
	/**
	 * @return {@link String} = composed by the constants the identify an empty tree item
	 */
	public static String getEmptyTreeItemValue() {
		return constants.XMLValue() + constants.emptyXMLValue();
	}
	
	/**
	 * Sort a {@link Map} array
	 * 
	 * @param aItems 	{@link Map} = the array to sort
	 * 
	 * @return {@link Map} = the sorted array
	 */
	public static Map<String, String> sortMapByKey(Map<String, String> aItems){
        TreeMap<String, String> result = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
        result.putAll(aItems);
        return result;
    }
	
	/**
	 * Show the correct error message
	 * 
	 * @param nodename	{@link String} = the path of the wrong node
	 * @param ret 		{@link String} = the type of error
	 * 
	 * @return {@link String} = the error message
	 */
	private static String showError(String nodename, String ret) {
		String errNr = ret.split("=")[1];
		String errMessage = "";
		if (errNr.equalsIgnoreCase("1") || errNr.equalsIgnoreCase("3")) {
			errMessage=constants.error() + nodename + " " + constants.errorDataType() + "\n";
		}
		if (errNr.equalsIgnoreCase("2")) {
			errMessage=constants.warning() + nodename + " " + constants.errorCodeListValue() + "\n";
		}		
		return errMessage;		
	}
	
	/**
	 * Clone a tree item (used for multiple fields, caused by the click on add button)
	 * 
	 * @param myTreeItem	{@link TreeItem} = it is the tree item to be copied (md_metadata[1].contact[1].ci_responsibleparty[1])
	 * @param topTreeItem  	{@link TreeItem} = it is the new tree item (md_metadata[1].contact[2].ci_responsibleparty[1])
	 * @param oldreplace 	{@link String} = the old id (md_metadata[1].contact[1].ci_responsibleparty[1])
	 * @param newreplace 	{@link String} = the new id (md_metadata[1].contact[2].ci_responsibleparty[1])
	 */	
	public static void cloneTreeItems(final TreeItem myTreeItem, final TreeItem topTreeItem, String oldreplace, String newreplace) {
		iso19115Constants constants = GWT.create(iso19115Constants.class);
		TreeItem currentNode = null;
        TreeItem subTreeItem = null;		
        for (int i = 0; i < myTreeItem.getChildCount(); i++) {
        	currentNode = myTreeItem.getChild(i);
        	TreeItem previousNode = null;
        	boolean add = false;
        	if (i!=0) previousNode = myTreeItem.getChild(i-1);
        	else add = true;
        	if (currentNode.getText().startsWith(constants.XMLValue())) add = true;
        	else if (currentNode.getText().startsWith("@")) add = true;
        	else
        		if (previousNode!=null)
        			if (!previousNode.getTitle().substring(0,previousNode.getTitle().lastIndexOf("[")).equalsIgnoreCase(currentNode.getTitle().substring(0,currentNode.getTitle().lastIndexOf("[")))) add=true;
        	if (add) {
        		subTreeItem = new TreeItem(currentNode.getHTML());
            	subTreeItem.setTitle(currentNode.getTitle().toLowerCase().replace(oldreplace, newreplace));
            	if (currentNode.getText().startsWith(constants.XMLValue())) 
            		subTreeItem.setText(Utilities.getEmptyTreeItemValue());
            	else if (currentNode.getText().startsWith("@gml:id")) {
            		Integer myNum = Random.nextInt();
            		if (Integer.signum(myNum)==-1) myNum=-(myNum);
            		subTreeItem.setText("@gml:id=ID_" + Integer.toHexString(myNum));
            	}
            	else if (currentNode.getText().startsWith("@codeListValue=")) 
            			subTreeItem.setText("@codeListValue=");
            	else 
            		subTreeItem.setText(currentNode.getText());            	
            	subTreeItem.setState(true);        	
            	if (currentNode.getChildCount()!=0) {
            		cloneTreeItems(currentNode, subTreeItem, oldreplace, newreplace);    				
            	}        	        	
            	topTreeItem.addItem(subTreeItem);
            	topTreeItem.setVisible(true);
            	topTreeItem.setState(true);
        	}        	
        }   		
	}

	/**
	 * This function is used to open an information popup. The information are extracted from the help using an anchor
	 * 
	 * @param helpAnchor	{@link String} the anchor in the help
	 * @param infoButton	{@link Button} the related button (the popup is opened near this button)
	 */
	public static void openInfo(String helpAnchor, Button infoButton) {
		// Get contextual help
		String contextualHelp = "";
		if ((EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.RDSI.toString())))
			contextualHelp = getContextualHelpRDSI(helpAnchor);
		else{
			contextualHelp = getContextualHelp(helpAnchor);
			if (helpAnchor.equalsIgnoreCase("topicCategory")) {
				contextualHelp += "<br/>" + getContextualHelp(helpAnchor + "_annex");
			}
		}
		String helpFile = "userguide/eurlex_" + LocaleInfo.getCurrentLocale().getLocaleName() + ".htm";
		if ((EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.RDSI.toString()))) {
			helpFile = "userguide/rdsi_guidelines_dataset.htm";
			if (EUOSMEGWT.metadataType.equalsIgnoreCase(DataTypes.DATA_SERVICE.toString())) 
				helpFile = "userguide/rdsi_guidelines_service.htm";
		}
		
		// Get URL to the entire userguide
		String helpURL = GWT.getHostPageBaseURL() + helpFile;
		
		// Show the popup
		PopupPanel pp = new PopupPanel(true,false);
		pp.setTitle(constants.info());
		VerticalPanel vp = new VerticalPanel();
		vp.add(new HTML(contextualHelp));
		Anchor link = null; 
		if ((EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.RDSI.toString()))) {
			link = new Anchor(constants.rdsiHelpTitle(), helpURL,"blank");
		}
		else 
			link = new Anchor(constants.regulationTitle(), helpURL,"blank");		
		vp.add(link);		
		
		if ((EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.RDSI.toString()))) {
			pp.setPixelSize(400,200);
			String linkGuidelines = "<a href=\"" + helpURL + "\" target=\"_blank\">" + constants.rdsiHelpTitle()+"</a><p></p>";
			HTMLPanel hp = new HTMLPanel(linkGuidelines + contextualHelp);			
			pp.setWidget(hp);			
			pp.showRelativeTo(infoButton);
			pp.getElement().getStyle().setOverflow(Overflow.SCROLL);
		}
		else {
			pp.setWidget(vp);
			pp.setPixelSize(400,200);
			pp.showRelativeTo(infoButton);
			pp.getElement().getStyle().setOverflow(Overflow.AUTO);
		}
	}
	
	/**
	 * This function is used to open an information popup. 
	 * 
	 * @param help	{@link String} the help string
	 *
	 */
	public static void openISOInfo(String help, Button infoButton) {	
		
		// Show the popup
		PopupPanel pp = new PopupPanel(true,false);
		pp.setTitle(constants.info());
		VerticalPanel vp = new VerticalPanel();
		vp.add(new HTML(help));
//		Anchor link = new Anchor(constants.regulationTitle(), helpURL,"blank");
//		vp.add(link);
		pp.setWidget(vp);
		pp.setPixelSize(400,200);
		pp.showRelativeTo(infoButton);
		pp.getElement().getStyle().setOverflow(Overflow.AUTO);
	}	
	
	/**
	 * This function finds the substring that represents the contextual help of a metadata element
	 * in the html guide
	 * 
	 * @param helpAnchor	{@link String} = the anchor in the userguide
	 * 
	 * @return {@link String} = contextual help string related to the anchor
	 */
	public static String getContextualHelp(String helpAnchor) {
		HTML infoHTM;
		String mainHTM = MainPanel.userGuideHTM.getHTML().toLowerCase();
		int beginIndex = mainHTM.indexOf("id='" + helpAnchor + "'");
		if (beginIndex==-1) beginIndex = mainHTM.indexOf("id=\"" + helpAnchor + "\"");
		if (beginIndex==-1) beginIndex = mainHTM.indexOf("id=" + helpAnchor);
		if (beginIndex==-1) infoHTM = new HTML("");
		else {
			int beginIndex2 = mainHTM.indexOf("</a>",beginIndex);
			int endIndex = mainHTM.indexOf("<h",beginIndex2);
			if (endIndex==0) endIndex = mainHTM.length();
			infoHTM = new HTML("<h4>" + MainPanel.userGuideHTM.getHTML().substring(beginIndex2, endIndex));
		}
		return infoHTM.getHTML().replace("<h4></h4>","");
	}
	
	/**
	 * This function finds the substring that represents the contextual help of a metadata element
	 * in the RDSI html guide
	 * 
	 * @param helpAnchor	{@link String} = the anchor in the userguide
	 * 
	 * @return {@link String} = contextual help string related to the anchor
	 */
	public static String getContextualHelpRDSI(String helpAnchor) {
		HTML infoHTM;
		String mainHTM = MainPanel.userGuideHTM.getHTML().toLowerCase();
		int beginIndex = mainHTM.indexOf("id='" + helpAnchor + "'");
		if (beginIndex==-1) beginIndex = mainHTM.indexOf("id=\"" + helpAnchor + "\"");
		if (beginIndex==-1) beginIndex = mainHTM.indexOf("id=" + helpAnchor);
		if (beginIndex==-1) infoHTM = new HTML("");
		else {
			int beginIndex2 = mainHTM.indexOf("</a>",beginIndex);
			int endIndex = mainHTM.indexOf("<h3",beginIndex2);
			if (endIndex==0) endIndex = mainHTM.length();
			infoHTM = new HTML("<h3>" + MainPanel.userGuideHTM.getHTML().substring(beginIndex2, endIndex));
		}
		return infoHTM.getHTML().replace("<h3></h3>","");
	}	
	
	
	/**
	 * Return the textual resource of a code list 
	 * 
	 * @param myCodeListName	{@link String} = the name of the code list
	 * 
	 * @return {@link String} = the text corresponding to the requested textual resource
	 */
	public static String getResourceCodeList(String myCodeListName) {
		String response = "";
		if (myCodeListName.equalsIgnoreCase("2")) 
			response=MyResources.INSTANCE.codeList2().getText();
    	if (myCodeListName.equalsIgnoreCase("3")) {
    		if (EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.RDSI.toString()))
    			response=MyResources.INSTANCE.codeList3RDSI().getText();
    		else 
    			response=MyResources.INSTANCE.codeList3().getText();    		
    		
    	}
    	if (myCodeListName.equalsIgnoreCase("4")) 
    		response=MyResources.INSTANCE.codeList4().getText();
    	if (myCodeListName.equalsIgnoreCase("5")) 
    		response=MyResources.INSTANCE.codeList5().getText();    		
    	if (myCodeListName.equalsIgnoreCase("6")) 
    		response=MyResources.INSTANCE.codeList6().getText();
    	if (myCodeListName.equalsIgnoreCase("7")) 
    		response=MyResources.INSTANCE.codeList7().getText();
    	if (myCodeListName.equalsIgnoreCase("8")) 
    		response=MyResources.INSTANCE.codeList8().getText();	
    	if (myCodeListName.equalsIgnoreCase("9")) 
    		response=MyResources.INSTANCE.codeList9().getText();
    	if (myCodeListName.equalsIgnoreCase("10")) 
    		response=MyResources.INSTANCE.codeList10().getText();
    	if (myCodeListName.equalsIgnoreCase("11")) 
    		response=MyResources.INSTANCE.codeList11().getText();
    	if (myCodeListName.equalsIgnoreCase("12")) {
    		if (EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.RDSI.toString()))
    			response=MyResources.INSTANCE.codeList12RDSI().getText();
    		else 
    			response=MyResources.INSTANCE.codeList12().getText();
    	}
    	if (myCodeListName.equalsIgnoreCase("13")) 
    		response=MyResources.INSTANCE.codeList13().getText();  
    	if (myCodeListName.equalsIgnoreCase("14")) 
    		response=MyResources.INSTANCE.codeList14().getText();
    	if (myCodeListName.equalsIgnoreCase("15")) {    	
    		if (EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.RDSI.toString()))
    			response=MyResources.INSTANCE.codeList15RDSI().getText();
    		else 
    			response=MyResources.INSTANCE.codeList15().getText();    		
    	}
    	return response;
	}
	
	/**
	 * Populate the {@link ListBox} with the values returned back by a RPC
	 * 
	 * @param response			{@link String} = the response of the RPC
	 * @param myList			{@link ListBox} = the list box to populate
	 * @param myDefaultValue	{@link String} = the default value to select
	 * @param order			{@link Boolean} = indicate if the list must be ordered alphabetically
	 */
	public static void setCodeList(String response, ListBox myList, String myDefaultValue, boolean order) {
		JSONValue jsonValueResponse = JSONParser.parseLenient(response);
		JSONObject jobjResponse = jsonValueResponse.isObject();	    
	    try {
	    	Map<String,String> definitions = new LinkedHashMap<String, String>();
	    	if (jobjResponse.get("value").isArray()!=null) {
		    	JSONArray jobjvalueCL = (JSONArray) jobjResponse.get("value");		    	
		    	for (int i = 0; i < jobjvalueCL.size(); i++) {
		    		JSONObject jobjCLitem = jobjvalueCL.get(i).isObject();
		    		String definition = jobjCLitem.get("name").isString().stringValue();
		    		String code = jobjCLitem.get("code").isString().stringValue();
		    		definitions.put(definition,code);
		    	}
	    	}
	    	else if (jobjResponse.get("value").isObject()!=null) {
	    		JSONObject jobjCLitem = (JSONObject) jobjResponse.get("value");	
	    		String definition = jobjCLitem.get("name").isString().stringValue();
	    		String code = jobjCLitem.get("code").isString().stringValue();
	    		definitions.put(definition,code);
	    	}	    	
		    putList(definitions, myList, myDefaultValue, order);
	    }
	    catch (Exception e) {
	    	GWT.log(e.getMessage());
	    }	    
	}
	
	/**
	 * Put a list of strings into a {@link ListBox}
	 * 
	 * @param definitions		{@link Map} = collection of strings
	 * @param myList			{@link ListBox} = the list box to populate
	 * @param myDefaultValue	{@link String} = the default value to select
	 * @param order			{@link Boolean} = indicate if the list must be ordered alphabetically
	 */
	private static void putList(Map<String, String> definitions, ListBox myList, String myDefaultValue, boolean order) {
  		iso19115Constants constants = GWT.create(iso19115Constants.class);
  		myList.addItem(constants.selectValue(), "");
  		if (definitions.size() >=0) {
    		if (order) definitions = Utilities.sortMapByKey(definitions);
    		Iterator<String> i = definitions.keySet().iterator();
    		while (i.hasNext()) {
    			String definition = i.next();
    			String code = definitions.get(definition);
	    		myList.addItem(definition, code);
	    		if (code.equalsIgnoreCase(myDefaultValue)) myList.setSelectedIndex(myList.getItemCount()-1);
		    }	    		
    	}
  		myList.removeItem(0);		
	}
	
	/**
	 * Clone the tree from a parentId
	 * 
	 * @param parentId	{@link String} = the path of the parent
	 * @param newId		{@link String} = the new path
	 * 
	 * @return {@link String} = the full path or node name of the node
	 */
	public static String cloneTree(String parentId, String newId) {	
		TreeItem myTreeItem;
		String myFormName = "";
		myTreeItem = Utilities.getSelectTreeItem(parentId);
		if (myTreeItem!=null &&  Utilities.getSelectTreeItem(newId) == null) {
			TreeItem topTreeItem = new TreeItem(myTreeItem.getHTML());
			topTreeItem.setText(myTreeItem.getText());
			topTreeItem.setTitle(newId);
			Utilities.cloneTreeItems(myTreeItem,topTreeItem, parentId.toLowerCase(),newId.toLowerCase());  	    
			// clone parent of characterString element
			TreeItem pcloneMyTreeItem = new TreeItem();
			int j = newId.lastIndexOf("[1]");
			myFormName = newId.substring(0,j);
			j = myFormName.lastIndexOf("]");
			pcloneMyTreeItem.setTitle(myFormName.substring(0,j+1));
			pcloneMyTreeItem.setText(myTreeItem.getParentItem().getText());
			pcloneMyTreeItem.addItem(topTreeItem);
			int beforeIndex = myTreeItem.getParentItem().getParentItem().getChildIndex(myTreeItem.getParentItem());
			myTreeItem.getParentItem().getParentItem().insertItem(beforeIndex+1, pcloneMyTreeItem);
	  	    topTreeItem.setVisible(true);
	  	    topTreeItem.setState(true);
	  	    myTreeItem.setSelected(false);	  	    
		}	
		return myFormName;
	}

	/**
	 * Populate a suggestion box with values of an RPC
	 * 
	 * @param response		{@link String} = a response from RPC
	 * @param myListOracle	{@link MultiWordSuggestOracle} = the suggestion box to populate
	 * @param order			{@link Boolean} = indicate if the list must be ordered alphabetically
	 */
	public static void setCodeList(String response, MultiWordSuggestOracle myListOracle, boolean order) {
		JSONValue jsonValueResponse = JSONParser.parseLenient(response);
		JSONObject jobjResponse = jsonValueResponse.isObject();	    
	    try {
	    	Map<String,String> definitions = new LinkedHashMap<String, String>();
	    	if (jobjResponse.get("value").isArray()!=null) {
		    	JSONArray jobjvalueCL = (JSONArray) jobjResponse.get("value");		    	
		    	for (int i = 0; i < jobjvalueCL.size(); i++) {
		    		JSONObject jobjCLitem = jobjvalueCL.get(i).isObject();
		    		String definition = jobjCLitem.get("name").isString().stringValue();
		    		String code = jobjCLitem.get("code").isString().stringValue();
		    		definitions.put(definition,code);
		    	}
	    	}
	    	else if (jobjResponse.get("value").isObject()!=null) {
	    		JSONObject jobjCLitem = (JSONObject) jobjResponse.get("value");	
	    		String definition = jobjCLitem.get("name").isString().stringValue();
	    		String code = jobjCLitem.get("code").isString().stringValue();
	    		definitions.put(definition,code);
	    	}	    	
		    putList(definitions, myListOracle, order);
	    }
	    catch (Exception e) {
	    	GWT.log(e.getMessage());
	    }	    
	}

	/**
	 * Put a list of strings into a {@link MultiWordSuggestOracle}
	 * 
	 * @param definitions	{@link Map} = collection of strings
	 * @param myListOracle	{@link MultiWordSuggestOracle} = the suggestion box to populate
	 * @param order			{@link Boolean} = indicate if the list must be ordered alphabetically
	 */
	private static void putList(Map<String, String> definitions, MultiWordSuggestOracle myListOracle, boolean order) {
		List<String> suggestions = new ArrayList<String>(); 
		if (definitions.size() >=0) {
    		if (order) definitions = Utilities.sortMapByKey(definitions);
    		Iterator<String> i = definitions.keySet().iterator();
    		while (i.hasNext()) {
    			String definition = i.next();
    			suggestions.add(definition);    				    		
		    }
    		myListOracle.addAll(suggestions);
    		// supports the show of all the terms if requested
    		myListOracle.setDefaultSuggestionsFromText(suggestions); 
    	}
	}

	/**
	 * Ensure the visibility of the requested tree item
	 * 
	 * @param selectedTreeItem	{@link TreeItem} = the tree item to select
	 */
	public static void ensureItemVisible(TreeItem selectedTreeItem) {
		MainPanel.myTree.setSelectedItem(null);
		Utilities.clearOpenState();
	 	MainPanel.myTree.setSelectedItem(selectedTreeItem, false);
	 	MainPanel.myTree.ensureSelectedItemVisible();		
	}

	/**
	 * Change the title of a tree item, this after the removing operation of an item before it
	 * 
	 * @param parentItem		{@link TreeItem} = the parent tree item
	 * @param searchFormName	{@link String} = the part of string to search
	 * @param newFormName		{@link String} = the new node name
	 */
	public static void changeTitle(TreeItem parentItem, String searchFormName, String newFormName) {
		if (parentItem!=null) {
			for (int i=0;i<parentItem.getChildCount();i++) {
				parentItem.getChild(i).setTitle(parentItem.getChild(i).getTitle().replace(searchFormName, newFormName));
				if (parentItem.getChild(i).getChildCount()!=0) changeTitle(parentItem.getChild(i),searchFormName, newFormName);
			}
			parentItem.setTitle(parentItem.getTitle().replace(searchFormName, newFormName));
		} else return;		
	}
		
	/**
	 * Populate the {@link ListBox} of repositories with the values returned back by a RPC
	 * 
	 * @param response			{@link String} = the response of the RPC
	 * @param myList			{@link ListBox} = the list box to populate
	 */
	public static void setSuggestList(String response, ListBox myList) {
		myList.clear();
    	myList.addItem(constants.selectValue(), "");
    	
    	Document messageDom = XMLParser.parse(response);

        NodeList nodes = messageDom.getElementsByTagName("result");
        Map<String,String> definitions = new LinkedHashMap<String, String>();
        //EUOSMEGWT.gemetPublicationDate = new LinkedHashMap<String, String>();
        for (int i = 0; i < nodes.getLength(); i++) {
			Node currentNode = nodes.item(i);
			String definition_en = "";
			String definition_lang = "";
			String definition_uri = "";
			String definition_date = "";
			for (int j = 0; j < currentNode.getChildNodes().getLength(); j++) {
				Node currentItem = currentNode.getChildNodes().item(j);
				// uri concept
				if (currentItem.getNodeName().equalsIgnoreCase("binding") && currentItem.getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase("v")) {
					if (currentItem.getChildNodes().getLength() >= 0) {
						for (int x= 0; x < currentItem.getChildNodes().getLength(); x++) {
							Node currentSubItem = currentItem.getChildNodes().item(x);
							if (currentSubItem.getNodeName().equalsIgnoreCase("uri"))
								definition_uri = currentSubItem.getFirstChild().getNodeValue();	  
						}					
					}
				}
				// literal in @en
				if (currentItem.getNodeName().equalsIgnoreCase("binding") && currentItem.getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase("l")) {
					if (currentItem.getChildNodes().getLength() >= 0) {
						for (int x= 0; x < currentItem.getChildNodes().getLength(); x++) {
							Node currentSubItem = currentItem.getChildNodes().item(x);
							if (currentSubItem.getNodeName().equalsIgnoreCase("literal"))
								definition_en = currentSubItem.getFirstChild().getNodeValue();	  
						}						
					}
				}
				// literal in client language
				if (currentItem.getNodeName().equalsIgnoreCase("binding") && currentItem.getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase("a")) {
					if (currentItem.getChildNodes().getLength() >= 0) {
						for (int x= 0; x < currentItem.getChildNodes().getLength(); x++) {
							Node currentSubItem = currentItem.getChildNodes().item(x);
							if (currentSubItem.getNodeName().equalsIgnoreCase("literal"))
								definition_lang = currentSubItem.getFirstChild().getNodeValue();	  
						}
					}
				}  
				
				// date (publication by default) 
				if (currentItem.getNodeName().equalsIgnoreCase("binding") && currentItem.getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase("d")) {
					if (currentItem.getChildNodes().getLength() >= 0) {
						for (int x= 0; x < currentItem.getChildNodes().getLength(); x++) {
							Node currentSubItem = currentItem.getChildNodes().item(x);
							if (currentSubItem.getNodeName().equalsIgnoreCase("literal"))
								definition_date = currentSubItem.getFirstChild().getNodeValue();	  
						}
					}
				} 				
			}
			if (!definition_en.isEmpty()) {
				String definition;
				definition = definition_en; 
				if (!definition_lang.isEmpty() && definition_lang != definition) definition = definition_lang;
				EUOSMEGWT.gemetPublicationDate.put(definition, definition_date);
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
	 * Populate the {@link TreeItem} of suggested keywords (of a particular repository) with the values returned back by a RPC
	 * 
	 * @param response			{@link String} = the response of the RPC
	 * @param myList			{@link ListBox} = the list box to populate
	 */
	public static void setSuggests(String response, TreeItem myList) {
		GWT.log(" RPC response " + response, null);
		GWT.log(" tree item " + myList.getText(), null);
		
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
						for (int x= 0; x < currentItem.getChildNodes().getLength(); x++) {
							Node currentSubItem = currentItem.getChildNodes().item(x);
							if (currentSubItem.getNodeName().equalsIgnoreCase("uri"))
								definition_uri = currentSubItem.getFirstChild().getNodeValue();	  
						}
					}
				}
				// literal in @en
				if (currentItem.getNodeName().equalsIgnoreCase("binding") && currentItem.getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase("l")) {
					if (currentItem.getChildNodes().getLength() >= 0) {
						for (int x= 0; x < currentItem.getChildNodes().getLength(); x++) {
							Node currentSubItem = currentItem.getChildNodes().item(x);
							if (currentSubItem.getNodeName().equalsIgnoreCase("literal"))
								definition_en = currentSubItem.getFirstChild().getNodeValue();	  
						}						
					}
				}
				// literal in language
				if (currentItem.getNodeName().equalsIgnoreCase("binding") && currentItem.getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase("a")) {
					if (currentItem.getChildNodes().getLength() >= 0) {
						for (int x= 0; x < currentItem.getChildNodes().getLength(); x++) {
							Node currentSubItem = currentItem.getChildNodes().item(x);
							if (currentSubItem.getNodeName().equalsIgnoreCase("literal"))
								definition_lang = currentSubItem.getFirstChild().getNodeValue();	  
						}						
					}
				}  
				// does it have narrower?
				if (currentItem.getNodeName().equalsIgnoreCase("binding") && currentItem.getAttributes().getNamedItem("name").getNodeValue().equalsIgnoreCase("d")) {
					if (currentItem.getChildNodes().getLength() >= 0) {
						for (int x= 0; x < currentItem.getChildNodes().getLength(); x++) {
							Node currentSubItem = currentItem.getChildNodes().item(x);
							if (currentSubItem.getNodeName().equalsIgnoreCase("uri"))
								definition_avuri = currentSubItem.getFirstChild().getNodeValue();	  
						}						
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
				//TODO Remove EUOSMEGWT.rpcRepository clause when the local resources about narrowers are available
				if (definition_uri.substring(0,1).equalsIgnoreCase("1") && EUOSMEGWT.rpcRepository) s.addItem(constants.loading());
				myList.addItem(s);	    				
		    }
			if (myList.getChild(0) != null)
				if (myList.getChild(0).getTitle().equalsIgnoreCase(constants.loading())) myList.getChild(0).remove();
			myList.getTree().ensureSelectedItemVisible();
			DOM.scrollIntoView(myList.getChild(myList.getChildCount()-1).getElement());
		}
	}

	/**
	 * Return the textual resource of a repository with the topmost concepts
	 * 
	 * @param myRepositoryName	{@link String} = the name of the repository
	 * 
	 * @return {@link String} = the text corresponding to the requested textual resource
	 */
	public static String getResourceRepository(String myRepositoryName) {
		String response = "";
		if (myRepositoryName.equalsIgnoreCase("GEMET Concepts")) 
			response=MyResources.INSTANCE.repositoryGEMET_Concepts().getText();
    	if (myRepositoryName.equalsIgnoreCase("GEMET Groups")) 
    		response=MyResources.INSTANCE.repositoryGEMET_Groups().getText();   		
    	if (myRepositoryName.equalsIgnoreCase("GEMET Themes")) 
    		response=MyResources.INSTANCE.repositoryGEMET_Themes().getText();
    	if (myRepositoryName.equalsIgnoreCase("GEOSS Societal Benefit Areas"))
    		response=MyResources.INSTANCE.repositoryGEOSS_Societal_Benefit_Areas().getText();    		
    	if (myRepositoryName.equalsIgnoreCase("INSPIRE Feature Concept Dictionary")) 
    		response=MyResources.INSTANCE.repositoryINSPIRE_Feature_Concept_Dictionary().getText();
    	if (myRepositoryName.equalsIgnoreCase("INSPIRE Glossary")) 
    		response=MyResources.INSTANCE.repositoryINSPIRE_Glossary().getText();
    	if (myRepositoryName.equalsIgnoreCase("ISO 19119 geographic services taxonomy")) 
    		response=MyResources.INSTANCE.repositoryISO_19119_geographic_services_taxonomy().getText();
    	return response;
	}

	public static void setDefaultValues() {
		// set metadata date to today
	    String myTodayDate = DateTimeFormat.getFormat("yyyy-MM-dd").format(new Date());
		Utilities.valueField("md_metadata[1].datestamp[1].date[1]", myTodayDate);
		TreeItem myTreeItem = Utilities.getSelectTreeItem("md_metadata[1].datestamp[1].date[1]");
		Utilities.setTextTreeItem(myTreeItem,myTodayDate);	
		// set identificationInfo sub type
		String identificationInfoSubType = "md_dataidentification";
		if (EUOSMEGWT.metadataType.equalsIgnoreCase(DataTypes.DATA_SERVICE.toString())) identificationInfoSubType = "sv_serviceidentification";
		// set creation date to today
		Utilities.valueField("md_metadata[1].identificationinfo[1]" + identificationInfoSubType + "[1].citation[1].ci_citation[1].date[3].ci_date[1].date[1].date[1]",myTodayDate);
	    myTreeItem = Utilities.getSelectTreeItem("md_metadata[1].identificationinfo[1]." + identificationInfoSubType + "[1].citation[1].ci_citation[1].date[3].ci_date[1].date[1].date[1]");
		Utilities.setTextTreeItem(myTreeItem,myTodayDate);
		// set default values
		if ((EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.RDSI.toString()))) {			
			
			myTreeItem = Utilities.getSelectTreeItem("md_metadata[1].identificationinfo[1]." + identificationInfoSubType + "[1].pointofcontact[1].ci_responsibleparty[1].organisationname[1].characterstring[1]");
			if (myTreeItem.getText().equals(getEmptyTreeItemValue())) {
				Utilities.valueField("md_metadata[1].identificationinfo[1]." + identificationInfoSubType + "[1].pointofcontact[1].ci_responsibleparty[1].organisationname[1].characterstring[1]",constants.RDSIorganisationName());
				Utilities.setTextTreeItem(myTreeItem,constants.RDSIorganisationName());
			}
			
			myTreeItem = Utilities.getSelectTreeItem("md_metadata[1].contact[1].ci_responsibleparty[1].organisationname[1].characterstring[1]");
			if (myTreeItem.getText().equals(getEmptyTreeItemValue())) {
				Utilities.valueField("md_metadata[1].contact[1].ci_responsibleparty[1].organisationname[1].characterstring[1]",constants.RDSIorganisationName());
				Utilities.setTextTreeItem(myTreeItem,constants.RDSIorganisationName());
			}
			
//			myTreeItem = Utilities.getSelectTreeItem("md_metadata[1].fileidentifier[1].characterstring[1]");
//			if (myTreeItem.getText().contains("#FILENAME#")) {
//				String tmpFileName = "";
//				Integer myNum = Random.nextInt();
//				if (Integer.signum(myNum)==-1) myNum=-(myNum);
//				tmpFileName = Integer.toHexString(myNum);
//				tmpFileName += ".xml";
//				Utilities.valueField("md_metadata[1].fileidentifier[1].characterstring[1]",tmpFileName);
//				Utilities.setTextTreeItem(myTreeItem,tmpFileName);
//			}		
		}		
	}
	
	/**
	 * remove all RDSI keywords from the tree
	 * @param keywords
	 */
	public static void removeKeywordRDSI() {
			int nrKeywords = 0;			
			int desIndex = 0;			
			TreeItem dataIdentificationItem = Utilities.getSelectTreeItem("md_metadata[1].identificationinfo[1].md_dataidentification[1]");	
			//md_metadata[1].identificationinfo[1].md_dataidentification[1].descriptivekeywords[6].md_keywords[1].keyword[1].characterstring[1]
			// current keyword list
			for (int k=0;k <dataIdentificationItem.getChildCount();k++) {
				if (dataIdentificationItem.getChild(k).getText().equalsIgnoreCase("gmd:descriptiveKeywords")) {
					desIndex = k;
					nrKeywords +=1;
				}
			}
			TreeItem lastDescriptiveKeywords = dataIdentificationItem.getChild(desIndex);
			lastDescriptiveKeywords.remove();
			//MainPanel.myTree.removeItem(lastDescriptiveKeywords);
	}
	
	/**
	 * This function is used to add a keyword of RDSI
	 * 
	 * 
	 * @param keyword		{@link String} = the keyword
	 */
	public static void addNewKeywordRDSI(ArrayList<String> keywords) {
		  // add a gmd:descriptiveKeywords/gmd:MD_Keywords/gmd:keyword/gco:CharacterString element plus thesaurus
		// formname: md_metadata[1].identificationinfo[1].md_dataidentification[1].descriptivekeywords[1].md_keywords[1]
		// parent item: md_metadata[1].identificationinfo[1].md_dataidentification[1]
				TreeItem dataIdentificationItem = Utilities.getSelectTreeItem("md_metadata[1].identificationinfo[1].md_dataidentification[1]");				
				int nrKeywords = 0;
				int lastKeywordIndex = 1;
				// current keyword list
				for (int k=0;k <dataIdentificationItem.getChildCount();k++) {
					if (dataIdentificationItem.getChild(k).getText().equalsIgnoreCase("gmd:descriptiveKeywords")) {
						nrKeywords +=1;
						lastKeywordIndex=dataIdentificationItem.getChildIndex(dataIdentificationItem.getChild(k));
					}
				}
				nrKeywords +=1;
				String parentTitle = dataIdentificationItem.getTitle() + ".descriptivekeywords[" + nrKeywords + "]";
				//gmd:descriptiveKeywords
				TreeItem descriptiveKeywordsTreeItem = new TreeItem();
				descriptiveKeywordsTreeItem.setTitle(parentTitle);
				descriptiveKeywordsTreeItem.setText("gmd:descriptiveKeywords");
				//gmd:MD_Keywords gmd:descriptiveKeywords/gmd:MD_Keywords
				TreeItem MD_KeywordsTreeItem = new TreeItem();
				MD_KeywordsTreeItem.setTitle(parentTitle + ".md_keywords[1]");
				MD_KeywordsTreeItem.setText("gmd:MD_Keywords");
				descriptiveKeywordsTreeItem.addItem(MD_KeywordsTreeItem);
				
				int index = 0;
				for (String keyword:keywords){
					index+=1;
					//gmd:keyword gmd:descriptiveKeywords/gmd:MD_Keywords/gmd:keyword
					TreeItem keywordTreeItem = new TreeItem();
					keywordTreeItem.setTitle(parentTitle + ".md_keywords[1].keyword["+index+"]");
					keywordTreeItem.setText("gmd:keyword");
					MD_KeywordsTreeItem.addItem(keywordTreeItem);
					//gco:CharacterString gmd:descriptiveKeywords/gmd:MD_Keywords/gmd:keyword/gco:CharacterString
					TreeItem characterstringTreeItem = new TreeItem();
					characterstringTreeItem.setTitle(parentTitle + ".md_keywords[1].keyword["+index+"].characterstring[1]");
					characterstringTreeItem.setText("gco:CharacterString");
					keywordTreeItem.addItem(characterstringTreeItem);
					//value of gco:CharacterString gmd:descriptiveKeywords/gmd:MD_Keywords/gmd:keyword/gco:CharacterString
					TreeItem valueTreeItem = new TreeItem(constants.XMLValue() + keyword); 				
					characterstringTreeItem.addItem(valueTreeItem);
				}
				
				// set source
				
				//insert created keyword element in the right place
				dataIdentificationItem.insertItem(lastKeywordIndex+1,descriptiveKeywordsTreeItem);
					
	}	
}