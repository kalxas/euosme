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

package eu.europa.ec.jrc.euosme.gwt.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

import eu.europa.ec.jrc.euosme.gwt.client.EUOSMEGWT;
import eu.europa.ec.jrc.euosme.gwt.client.RESTfulWebServiceProxy;
import eu.europa.ec.jrc.euosme.gwt.client.RESTfulWebServiceProxyAsync;
import eu.europa.ec.jrc.euosme.gwt.client.Utilities;
import eu.europa.ec.jrc.euosme.gwt.client.callback.CodeListRpcCallback;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;

/**
 * Create an horizontal panel with a label and a list box 
 * The list items come from an external code list service
 * 
 * @version 6.0 - January 2011
 * @author 	Marzia Grasso
 */
public class CodeList extends Composite  {
	
	private static CodeListUiBinder uiBinder = GWT.create(CodeListUiBinder.class);
	interface CodeListUiBinder extends UiBinder<Widget, CodeList> {	}
	
	/** grouping fields declaration */
	@UiField(provided = true)
	DisclosurePanel componentPanel = new DisclosurePanel();	
	
	/** Label declaration */
	@UiField
	Label myLabel = new Label();
	
	/** Error Label declaration */
	@UiField
	Label myError = new Label();
	
	/** ListBox declaration */
	@UiField
	public
	ListBox myListBox = new ListBox();
	
	/** Button for information */
	@UiField
	Button infoButton = new Button();
	
	/** Declare constants */
	private iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Global variable used to check if the field is empty or not */ 
	private boolean isRequired=false;
	
	/** Global variable used in the URL to get the code list */
	private String codeListName="";
    
	/** Global variable used to move into the user guide as an anchor */
	private String helpAnchor="";
	
	/** Relation between the form element and the tree item */
	private TreeItem myTreeItem;
	
	/** true to order the suggestions alphabetically */
	private boolean orderList = true;
	
	/** 
     * constructor CodeList
     * 
     * @param label				{@link String} = the label of the field
     * @param required			{@link Boolean} = if it is true, the field is mandatory
     * @param myListName		{@link String} = name of the code list to get
     * @param myDefaultValue	{@link String} = the default value
     * @param order				{@link Boolean} = indicate if the list must be ordered alphabetically 
     * 
     * @return	the widget composed by an horizontal panel made up a label and a single-line text box
     */
	public CodeList(String label, String help, boolean required, String myListName, String myDefaultValue, boolean order) {
		// Set global variables
		isRequired = required;
		codeListName = myListName;
		helpAnchor = help;
		orderList = order;
		
		// Initialize widget
		initWidget(uiBinder.createAndBindUi(this));
		
		// Set Label widget
		setLabel(label);
				
		// Set Label and name of myList widget
		myListBox.addItem(constants.loadingData(),"");		
		
		setDisplay(true);
		
		// Invoke RESTful service or get the list from available local resource
		if (EUOSMEGWT.rpcCodeList)
			invokeRESTfulWebService(codeListName, myListBox, myDefaultValue);
		else
			Utilities.setCodeList(Utilities.getResourceCodeList(codeListName), myListBox, myDefaultValue, order);
		
		//Set Error Label widget	
		myError.setVisible(false);	
		
		infoButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Utilities.openInfo(helpAnchor,infoButton);				
			}
		});
	}	

	/**
	 * Set the header
	 * 
	 * @param label	{@link String} = the header string
	 */
	public void setLabel(String label) {
		myLabel.setText(label);
		if (isRequired==true) myLabel.setText(myLabel.getText() + " (*)");		
	}
	
	/**
	 * This is called to make field's validation and show an error if it fails
	 */
	public void myCheck() {       	
		// hide error label
		myError.setVisible(false);
		// check if it is required or not and perform the right actions
		if (isRequired==true && myListBox.getSelectedIndex()==0) {
			myError.setText(constants.mandatoryField());
			myError.setVisible(true);  	  		
		}
	}
	
	/**
	 * @param myValue {@link String} = set the selected item of myListBox
	 */
	public void setMyValue(String myValue) {		
		int sel_i = 0;
		for (int i = 0;i<myListBox.getItemCount();i++) {
			myListBox.setItemSelected(i,false);
			if (myListBox.getValue(i).equalsIgnoreCase(myValue)) sel_i = i;
		}
		myListBox.setItemSelected(sel_i, true);
		if (myTreeItem==null) {
			TreeItem val = null;
			val = Utilities.getSelectTreeItem(myListBox.getName());
			myTreeItem = val;
		}
		Utilities.setTextTreeItem(myTreeItem,myValue);
		TreeItem attributeCodeListValue = null;
		attributeCodeListValue = Utilities.getSelectTreeItem(myListBox.getName() + ".@codeListValue");
		Utilities.setTextTreeItem(attributeCodeListValue,myValue);
	}

	/**
	 * @return {@link String} = the value of selected item of myListBox
	 */
	public String getMyValue() {
		String ret = myListBox.getValue(myListBox.getSelectedIndex());
		if (ret.equalsIgnoreCase(constants.selectValue())) ret = "";
		return ret;
	}

	/**
	 * @param name {@link String} = the name of the field in the form
	 */
	public void setFormName(String name) {
		myListBox.setName(name);
		myListBox.getElement().setId(name);
		myError.getElement().setId("error-" + name);
	}
	
	/**
	 * @param required {@link Boolean} = true if the field is required
	 */
	public void setRequired(boolean required) {
		this.isRequired = required;	
		if (required==true) myLabel.setText(myLabel.getText().replace("(*)", "") + " (*)");
		if (EUOSMEGWT.showAll) setDisplay(true);
		else setDisplay(required);
	}
	
	/**
	 * @param visible {@link Boolean} = true to open disclosure panel and show fields group
	 */
	public void setDisplay(boolean visible) {
		componentPanel.setOpen(visible);
	}

	/**
	 * Set the new help anchor
	 * 
	 * @param newAnchor {@link String} = the new anchor
	 */
	public void setHelpAnchor(String newAnchor) {
		helpAnchor=newAnchor;		
	}
	
	/**
	 * @param b	{@link Boolean} = true to order suggestions alphabetically
	 */
	public void setOrderList(boolean b) {
		orderList = b;		
	}
	
	/**
     * This is called whenever a user press a key (tab, for instance) and force the field check
     * 
     * @see onKeyPress
     */
	@UiHandler("myListBox")
	void onKeyPress(KeyPressEvent event) {
		if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_TAB) {	
	    	myCheck();
	    	if (myError.isVisible()) myListBox.setFocus(true);	    	
		}		
	}
	
	/**
	 * 
  	 * This is called whenever a user focus on the list box
     * 1. goto related anchor in the user guide
     * 2. select related tree item
     * 
     * @see onFocus event
     */
	@UiHandler("myListBox")
	void onFocus(FocusEvent event) {
		// get the related tree item
		if (myTreeItem==null) {
			TreeItem val = null;
			val = Utilities.getSelectTreeItem(myListBox.getName());
			myTreeItem = val;
		}
		// set selected item
		if (myTreeItem!=null) {
			Utilities.ensureItemVisible(myTreeItem);
			// get focus: it is lost on tree item selection 
			myListBox.setFocus(true);
		}
	}
	
	/**
	 * OnChange set the value in the corresponding {@link TreeItem}
	 * 
	 * see {@link ChangeEvent}
	 */
	@UiHandler("myListBox")
	void onChange(ChangeEvent event) {
		Utilities.setTextTreeItem(myTreeItem,getMyValue());
		TreeItem attributeCodeListValue = null;
		attributeCodeListValue = Utilities.getSelectTreeItem(myListBox.getName() + ".@codeListValue");
		Utilities.setTextTreeItem(attributeCodeListValue,getMyValue());
		myCheck();
	}
			
	/**
	 * This is called to invoke the RESTful service
	 * 
	 * @param codeListname		{@link String} = the name of the code list
	 * @param myListRPC			{@link ListBox} = the list to put values on callback
	 * @param myDefaultValue 	{@link String} = the default value
	 */
	private void invokeRESTfulWebService(String codeListName, ListBox myListRPC, String myDefaultValue) {
		CodeListRpcCallback callback = new CodeListRpcCallback();
		callback.setList(myListRPC, myDefaultValue);
		callback.setCodeListName(codeListName);
		callback.setOrderList(orderList);
		RESTfulWebServiceProxyAsync ls = RESTfulWebServiceProxy.Util.getInstance();
		ls.invokeGetRESTfulWebService("codelists", codeListName, LocaleInfo.getCurrentLocale().getLocaleName(), "", callback);
	}
}