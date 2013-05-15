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

import java.io.Serializable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

import eu.europa.ec.jrc.euosme.gwt.client.EUOSMEGWT;
import eu.europa.ec.jrc.euosme.gwt.client.Utilities;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.ui.MainPanel;

/**
 * Contains the implementation of the callback class from INSPIRE Web service
 * 
 * @version 1.0 - December 2010
 * @author 	Marzia Grasso 
 */
public class InspireServiceRpcCallback implements AsyncCallback <InspireServiceRpcCallback.returnType>, RequestCallback {

	public static class returnType implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String url;
		private String text;
		public void setUrl(String url) {
			this.url = url;
		}
		public String getUrl() {
			return url;
		}
		public void setText(String text) {
			this.text = text;
		}
		public String getText() {
			return text;
		}
	}
	
	/** the text box corresponding to the Code object to value */
	TextBox codeObj;
	
	/** file identifier **/ 
	String fileIdentifier = "";
	
	/** resource identifier **/ 
	String resourceIdentifier = "";
	
	/** this service could return an XML or an HTML*/
	private static String typeOfResult = "XML";
		
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
	 */
	public void onSuccess(returnType result) {
    	if (result == null)
    		return;
    	String url = result.getUrl();
    	if (url != null){
    	    MainPanel.myHTML.setUrl(url);
    	} else if (typeOfResult.equalsIgnoreCase("XML")) {
	    	try {
	    		Document uuidDocument = XMLParser.parse(result.getText());
	    		//<uuid><canonicalForm>d5025221-f93b-11df-b599-0017085a97ab</canonicalForm>
	    		String uuid = uuidDocument.getElementsByTagName("canonicalForm").item(0).getFirstChild().getNodeValue();    
	    		codeObj.setText(uuid);
	    		
	    		// set file identifier		
	    		if (fileIdentifier.length() > 0) {
					TreeItem myTreeItem = Utilities.getSelectTreeItem(fileIdentifier);
					Utilities.valueField(fileIdentifier, uuid);
					Utilities.setTextTreeItem(myTreeItem, uuid);
	    		}
	    		
	    		// set resource identifier		
	    		if (resourceIdentifier.length() > 0) {
					TreeItem myTreeItem = Utilities.getSelectTreeItem(resourceIdentifier);					
					Utilities.setTextTreeItem(myTreeItem, uuid);
	    		}	    		
	    	}
	    	catch (Exception ex) {
	    		return;
	    	}
    	}

    }

    /* (non-Javadoc)
     * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
     */
    public void onFailure(Throwable caught) {
    	if (EUOSMEGWT.inspireserviceAvailable) {
    		EUOSMEGWT.inspireserviceAvailable=false;
    		iso19115Constants constants = GWT.create(iso19115Constants.class);
    		String msg=caught.getMessage();
    		if (msg != null)
    			Window.alert(constants.inspireServiceFailed() + " " + msg);
    	}	
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

	/**
	 * @param myType	{@link String} = the type of result	
	 */
	public static void setType(String myType) {
		typeOfResult  = myType;		
	}

	/**
	 * @param myTextBox	= {@link TextBox} the code text box to value
	 */
	public void setCodeObj(TextBox myTextBox) {
		codeObj = myTextBox;		
	}
	
	/**
	 * @param myFileId = 
	 */
	public void setFileIdentifier(String myFileId) {
		fileIdentifier = myFileId;		
	}	
	
	/**
	 * @param myFileId = 
	 */
	public void setResourceIdentifier(String myResourceId) {
		resourceIdentifier = myResourceId;		
	}	
}