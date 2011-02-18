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
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

import eu.europa.ec.jrc.euosme.gwt.client.EUOSMEGWT;
import eu.europa.ec.jrc.euosme.gwt.client.CheckFunctions;
import eu.europa.ec.jrc.euosme.gwt.client.InfoButton;
import eu.europa.ec.jrc.euosme.gwt.client.Utilities;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;

/**
 * Create an horizontal panel with a label and a text box
 * 
 * @version 7.0 - February 2011
 * @author 	Marzia Grasso
 */
public class CharacterString extends Composite {
	
	private static CharacterStringUiBinder uiBinder = GWT.create(CharacterStringUiBinder.class);
	interface CharacterStringUiBinder extends UiBinder<Widget, CharacterString> {	}
	
	/** TextBox declaration */
	@UiField
	public
	TextBox myTextBox = new TextBox();
	
	/** Label declaration */
	@UiField
	public
	Label myLabel = new Label();
	
	/** Error Label declaration */
	@UiField
	public
	Label myError = new Label();
	
	/** Global variable used to check if the field is empty or not */ 
	public boolean isRequired=false;
	
	/** Button for information */
	@UiField
	InfoButton infoButton = new InfoButton();
	
	/** grouping fields declaration */
	@UiField(provided = true)
	DisclosurePanel componentPanel = new DisclosurePanel();
	
	/** Global variable used to check the format of the input string */
	private CheckFunctions checkFunction;
	
	/** Global variable used to move into the user guide as an anchor (OPTIONAL, if it is empty, it is used the tab's related anchor) */
	private String helpAnchor="";
	
	/** Relation between the form element and the tree item */
	private TreeItem myTreeItem;
	
	/** True indicates that OnBlur event updates automatically the value in the tree */
	private boolean autoupdate=true;
	
	/** 
     * constructor CharacterString: 0..1 (String)
     * 
     * @param label				{@link String} = the label of the field
     * @param help				{@link String} = the anchor in the help
     * @param required			{@link Boolean} = if it is true, the field is mandatory
     * @param check				{@link CheckFunctions} = the format of the string
     * @param autoupdatetree	{@link Boolean} = if it is true, automatically update correspondent tree item's value
     * 
     * @return	the widget composed by an horizontal panel made up a label and a single-line text box
     */
	public CharacterString(String label, String help, boolean required, CheckFunctions check, boolean autoupdatetree) {
		// Set global variables
		isRequired = required;
		checkFunction = check;
		helpAnchor = help;
		autoupdate = autoupdatetree;
		
		// Initialize widget
		initWidget(uiBinder.createAndBindUi(this));
		
		// Set Label widget
		setLabel(label);
		
		if (EUOSMEGWT.showAll) setDisplay(true);
		else setDisplay(required);
		
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
	 * @param myValue	{@link String} = set the value of myTextBox text box
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
		myTextBox.setName(name);
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
	 * @param newAnchor	{@link String} = the new anchor in the help
	 */
	public void setHelpAnchor(String newAnchor) {
		helpAnchor=newAnchor;
		infoButton.setHelpAnchor(helpAnchor);
	}	
	
	/**
     * This is called whenever a user press a key (tab, for instance) and force the field check
     * 
     * @see onKeyPress
     */
	@UiHandler("myTextBox")
	void onKeyPress(KeyPressEvent event) {
		if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_TAB) {
	    	this.myCheck();
	    	if (myError.isVisible()) {
	    		myTextBox.cancelKey();
	    		myTextBox.setFocus(true);
	    	}
	    }
	}
	
	/**
	 * 
  	 * This is called whenever a user focus on the text box
     * 1. goto related anchor in the user guide
     * 2. select related tree item
     * 
     * @see onFocus event
     */
	@UiHandler("myTextBox")
	void onFocus(FocusEvent event) {
		// select the related tree item
		if (myTreeItem==null) {
			TreeItem val = null;
			val = Utilities.getSelectTreeItem(myTextBox.getName());
			myTreeItem = val;						
		}
		// set selected item
		if (myTreeItem!=null) {
			Utilities.ensureItemVisible(myTreeItem);
			// get focus: it is lost on tree item selection 
		 	myTextBox.setFocus(true);
		}
	}
	
	/**
	 * OnBlur set the value to the corresponding {@link TreeItem}
	 * 
	 * @param event	{@link BlurEvent}
	 */
	@UiHandler("myTextBox")
	void onBlur(BlurEvent event) {
		if (autoupdate) Utilities.setTextTreeItem(myTreeItem,myTextBox.getText());		
		myCheck();
	}
}