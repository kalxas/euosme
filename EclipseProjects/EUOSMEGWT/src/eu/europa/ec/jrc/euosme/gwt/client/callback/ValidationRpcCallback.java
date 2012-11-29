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

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
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
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;

/**
 * Contains the implementation of the callback class from Validation service
 * 
 * @version 1.1 - December 2010
 * @author Marzia Grasso
 */
public class ValidationRpcCallback implements AsyncCallback<String>,
		RequestCallback {

	/** Constants declaration */
	private iso19115Constants constants = GWT.create(iso19115Constants.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
	 */
	public void onSuccess(String result) {
		if (result == null)
			return;
		// getOldValidationMsg(result);
		getNewValidationMsg(result);
	}

	public void getNewValidationMsg(String result) {
		String warningDialog = "";

		// HTML resultHTM = new HTML(result);
		Document messageDom = XMLParser.parse(result);

		int nrSchema = 0;
		int nrInspire = 0;

		String smrSchema = "";
		String smrInspire = "";

		com.google.gwt.xml.client.NodeList errorElements = messageDom
				.getElementsByTagName("InspireValidationErrors");
		ArrayList<String> errorList = buildMessageElements(errorElements);
		com.google.gwt.xml.client.NodeList warningElements = messageDom
				.getElementsByTagName("InspireValidationWarnings");
		ArrayList<String> warningList = buildMessageElements(warningElements);
		warningDialog = "<p>INSPIRE validation errors: " + errorList.size()
				+ "</p><ul>" + listToString(errorList) + "</ul>";
		warningDialog += "<p>INSPIRE validation warnings: "
				+ warningList.size() + "</p><ul>" + listToString(warningList)
				+ "</ul>";

		final DialogBox resultValidation = new DialogBox(false, false);
		resultValidation.setHTML(warningDialog);
		resultValidation.add(new Button(constants.close(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				resultValidation.hide();
			}
		}));
		resultValidation.setSize("600px", "300px");
		resultValidation.center();
		try {
			resultValidation.getElement().getElementsByTagName("div")
					.getItem(0).getStyle().setOverflow(Overflow.AUTO);
		} catch (Exception ex) {
		}
	}

	public String listToString(ArrayList<String> list) {

		String retVal = "";
		for (String str : list) {
			retVal += str;
		}
		return retVal;
	}

	public ArrayList<String> buildMessageElements(
			com.google.gwt.xml.client.NodeList errorElements) {

		ArrayList<String> retVal = new ArrayList<String>();

		for (int j = 0; j < errorElements.getLength(); j++) {
			com.google.gwt.xml.client.Node curNode = errorElements.item(j);
			for (int i = 0; i < curNode.getChildNodes().getLength(); i++) {
				com.google.gwt.xml.client.Node curMsgNode = curNode
						.getChildNodes().item(i);
				if ((curMsgNode.getPrefix() + ":" + "ValidationError")
						.equals(curMsgNode.getNodeName())) {
					for (int k = 0; k < curMsgNode.getChildNodes().getLength(); k++) {
						com.google.gwt.xml.client.Node curExcNode = curMsgNode
								.getChildNodes().item(k);
						if ((curExcNode.getPrefix() + ":" + "GeoportalExceptionMessage")
								.equals(curExcNode.getNodeName())) {

							for (int l = 0; l < curExcNode.getChildNodes()
									.getLength(); l++) {
								com.google.gwt.xml.client.Node curSubNode = curExcNode
										.getChildNodes().item(l);
								if ((curSubNode.getPrefix() + ":" + "Message")
										.equals(curSubNode.getNodeName())) {
									String warning = "<li>"
											+ curSubNode.getFirstChild()
													.getNodeValue() + "</li>";
									retVal.add(warning);
									// nrInspire++;

								}

							}

						}
					}
				}
			}
		}
		return retVal;
	}

	public void getOldValidationMsg(String result) {
		String warningDialog = "";
		HTML resultHTM = new HTML(result);
		NodeList<Element> wrongElements = resultHTM.getElement()
				.getElementsByTagName("li");
		int nrWrongs = 0;
		for (int i = 0; i < wrongElements.getLength(); i++) {
			String wrongElement = wrongElements.getItem(i).getInnerHTML();
			if (wrongElement.contains("XPath:")) {
				nrWrongs += 1;
				String warning = "<li>" + wrongElement.split("XPath:")[0]
						+ "</li>";
				warningDialog += warning;
				String xpath = wrongElement.split("XPath:")[1].trim();
				xpath = xpath.replace("/*:", ".");
				xpath = xpath.replace(
						"[namespace-uri()='http://www.isotc211.org/2005/gmd']",
						"");
				xpath = xpath.replace("[]", "");
				xpath = "error-" + xpath.substring(1).toLowerCase();
				try {
					Element divError = DOM.getElementById(xpath);
					divError.setNodeValue(warning);
					divError.getStyle().clearDisplay();
				} catch (Exception ex) {
				}
			}
		}
		warningDialog = "<p>Incorrect elements: " + nrWrongs + "</p><ul>"
				+ warningDialog + "</ul>";
		final DialogBox resultValidation = new DialogBox(false, false);
		resultValidation.setHTML(warningDialog);
		resultValidation.add(new Button(constants.close(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				resultValidation.hide();
			}
		}));
		resultValidation.center();
		resultValidation.setSize("400", "300");
		try {
			resultValidation.getElement().getElementsByTagName("div")
					.getItem(0).getStyle().setOverflow(Overflow.AUTO);
		} catch (Exception ex) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable
	 * )
	 */
	@Override
	public void onFailure(Throwable caught) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.http.client.RequestCallback#onError(com.google.gwt.http
	 * .client.Request, java.lang.Throwable)
	 */
	@Override
	public void onError(Request request, Throwable exception) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.http.client.RequestCallback#onResponseReceived(com.google
	 * .gwt.http.client.Request, com.google.gwt.http.client.Response)
	 */
	@Override
	public void onResponseReceived(Request request, Response response) {
		String ret = response.getText();
		Window.alert(ret);
	}
}