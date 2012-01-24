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
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

import eu.europa.ec.jrc.euosme.gwt.client.EUOSMEGWT;
import eu.europa.ec.jrc.euosme.gwt.client.InfoButton;
import eu.europa.ec.jrc.euosme.gwt.client.MySuggestBox;
import eu.europa.ec.jrc.euosme.gwt.client.RESTfulWebServiceProxy;
import eu.europa.ec.jrc.euosme.gwt.client.RESTfulWebServiceProxyAsync;
import eu.europa.ec.jrc.euosme.gwt.client.CheckFunctions;
import eu.europa.ec.jrc.euosme.gwt.client.Utilities;
import eu.europa.ec.jrc.euosme.gwt.client.callback.CodeListRpcCallback;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;

/**
 * Create an horizontal panel with a label and a list box 
 * The list items come from an external code list service
 * 
 * @version 3.0 - February 2011
 * @author 	Marzia Grasso
 */
public class CodeListFree extends Composite  {
	
	private static CodeListFreeUiBinder uiBinder = GWT.create(CodeListFreeUiBinder.class);
	interface CodeListFreeUiBinder extends UiBinder<Widget, CodeListFree> {	}
	
	/** ListBox declaration */
	public
	ListBox myListBox = new ListBox();
	
	/** ListBox declaration */
	@UiField
	HorizontalPanel myHP = new HorizontalPanel();
	
	/** Suggest box declaration */
	public
	MySuggestBox myTextBox;
	
	/** grouping fields declaration */
	@UiField(provided = true)
	DisclosurePanel componentPanel = new DisclosurePanel();	
	
	/** Label declaration */
	@UiField
	Label myLabel = new Label();
	
	/** Error Label declaration */
	@UiField
	Label myError = new Label();
	
	/** Button for information */
	@UiField
	InfoButton infoButton = new InfoButton();
	
	/** Label to help user */
	@UiField
	Label helpLabel = new Label();
	
	
	/** Global variable used to check if the field is empty or not */ 
	private boolean isRequired=false;
	
	/** Global variable used in the URL to get the code list */
	private String codeListName="";
    
	/** Global variable used to move into the user guide as an anchor */
	private String helpAnchor="";
	
	/** Relation between the form element and the tree item */
	private TreeItem myTreeItem;
		
	/** Global variable used to check the format of the input string */
	private CheckFunctions checkFunction;
	
	/** true to show the suggest box */
	private boolean showList = true;
	
	/** true to order the suggestions alphabetically */
	private boolean orderList = true;
	
	/**
	 * constructor CodeListFree   
	 * 
	 * @param label				{@link String} = the label of the field
	 * @param help				{@link String} = the anchor
	 * @param required			{@link Boolean} = if it is true, the field is mandatory
	 * @param myListName		{@link String} = name of the code list to get
	 * @param myDefaultValue	{@link String} = the default value
	 * @param check				{@link CheckFunctions} = the type of string
	 * @param showOracleList	{@link Boolean} = true to show the suggest box
	 * @param order				{@link Boolean} = indicate if the list must be ordered alphabetically
	 */
	public CodeListFree(String label, String help, boolean required, String myListName, String myDefaultValue, CheckFunctions check, boolean showOracleList, boolean order) {
		// Set global variables
		isRequired = required;
		codeListName = myListName;
		checkFunction = check;
		helpAnchor = help;
		showList = showOracleList;
		orderList = order;
		
		// Initialize widget
		initWidget(uiBinder.createAndBindUi(this));
		
		MultiWordSuggestOracle myListOracle = new MultiWordSuggestOracle();  
		  
		myTextBox = new MySuggestBox(myListOracle);
		myTextBox.addStyleName("svP");
		myHP.add(myTextBox);
		
		
		
		// Set Label widget
		setLabel(label);
		
		setDisplay(true);
		
		// Invoke RESTful service or get the list from available local resource
		if (EUOSMEGWT.rpcCodeList)
			invokeRESTfulWebService(codeListName, myListOracle);
		else
			Utilities.setCodeList(Utilities.getResourceCodeList(codeListName), myListOracle, orderList);
			
		//Set Error Label widget	
		myError.setVisible(false);	
		
		// Set info button
		infoButton.setHelpAnchor(helpAnchor);
		componentPanel.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {				
				event.getTarget().setOpen(true);
			}	
		});
		
		myTextBox.getTextBox().addFocusHandler( new FocusHandler(){
			@Override
			public void onFocus(FocusEvent event) {
				// select the related tree item
				if (myTreeItem==null) {
					TreeItem val = null;
					val = Utilities.getSelectTreeItem(myTextBox.getTextBox().getName());
					myTreeItem = val;						
				}
				// set selected item
				if (myTreeItem!=null) {
					Utilities.ensureItemVisible(myTreeItem);
					// get focus: it is lost on tree item selection 
				 	myTextBox.setFocus(true);
				}
			}
		});
		
		myTextBox.getTextBox().addKeyPressHandler(new KeyPressHandler() {			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_TAB) {
			    	myCheck();
			    	if (myError.isVisible()) {
			    		myTextBox.getTextBox().cancelKey();
			    		myTextBox.setFocus(true);
			    	}
			    }
			}
		});	
				
		myTextBox.getTextBox().addKeyUpHandler(new KeyUpHandler() {
			@SuppressWarnings("deprecation")
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (myTextBox.isSuggestionListShowing() && !showList)
		    		myTextBox.hideSuggestionList();
			}
			
		});
			
		myTextBox.getTextBox().addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				Utilities.setTextTreeItem(myTreeItem,myTextBox.getText());
			}			
		});	
		
		myTextBox.addSelectionHandler(new SelectionHandler<Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				Utilities.setTextTreeItem(myTreeItem,myTextBox.getText());
			}			
		});	
		
		// Set label for the help
		iso19115Constants constants = GWT.create(iso19115Constants.class);
		helpLabel.setText(constants.helpCodeList());
		helpLabel.addStyleName("suggestion");
	}	

	public void setBoxWidth(String s){
		myTextBox.setWidth(s);
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
		// declare constants
		iso19115Constants constants = GWT.create(iso19115Constants.class);
		// get input string
		String newItem = this.getMyValue();
		// hide error label
		myError.setVisible(false);
		// check if it is required or not and perform the right actions
       	if (isRequired==true) {
       		// check if the string is empty or if it is of the specified format
	    	if (newItem.isEmpty()) {
       			// if the input string is empty return a warning message
	    		myError.setText(constants.mandatoryField());
				myError.setVisible(true);
	    	}	
	  	  	else if (checkFunction!=CheckFunctions.normal) {
	  	  		// if the input string is of a particular format, check it
	  	  		String ret = Utilities.checkString(newItem,checkFunction);
	  	  		if (ret!="") {
	  	  			myError.setText(ret);
	  	  			myError.setVisible(true);
	  	  		}
	  	  	}
	    } else if (newItem.isEmpty()==false && checkFunction!=CheckFunctions.normal) {
	    	// if it is not required but it is not empty and of a particular format, check it
	    	String ret = Utilities.checkString(newItem,checkFunction);
	    	if (ret!="") {
  	  			myError.setText(ret);
  	  			myError.setVisible(true);
  	  		} 	    	  
	    }				
	}	
	
	/**
	 * @param myValue {@link String} = set the value of myTextBox text box
	 */
	public void setMyValue(String myValue) {
		this.myTextBox.setText(myValue);		
	}

	/**
	 * @return {@link String} = the value of myTextBox text box
	 */
	public String getMyValue() {
		return this.myTextBox.getText().trim();
	}	
	
	/**
	 * @param name {@link String} = the name of the field in the form
	 */
	public void setFormName(String name) {
		myTextBox.getTextBox().setName(name);
		myTextBox.getElement().setId(name);
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
		infoButton.setHelpAnchor(helpAnchor);
	}

	/**
	 * @param b	{@link Boolean} = true to show the suggest box
	 */
	public void setShowList(boolean b) {
		showList = b;		
		helpLabel.setVisible(b);
	}	
	
	/**
	 * @param b	{@link Boolean} = true to order suggestions alphabetically
	 */
	public void setOrderList(boolean b) {
		orderList = b;		
	}	
	
	/**
	 * This is called to invoke the RESTful service
	 * 
	 * @param	codeListname	{@link String} = the name of the code list
	 * @param	myListRPC		{@link MultiWordSuggestOracle} = the list to put values on callback 
	 * 
	 */
	private void invokeRESTfulWebService(String codeListName, MultiWordSuggestOracle myListRPC) {
		CodeListRpcCallback callback = new CodeListRpcCallback();
		callback.setList(myListRPC);
		callback.setCodeListName(codeListName);
		callback.setOrderList(orderList);
		RESTfulWebServiceProxyAsync ls = RESTfulWebServiceProxy.Util.getInstance();
		ls.invokeGetRESTfulWebService("codelists", codeListName, LocaleInfo.getCurrentLocale().getLocaleName(), "", callback);
	}
}