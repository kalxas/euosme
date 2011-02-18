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
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

import eu.europa.ec.jrc.euosme.gwt.client.EUOSMEGWT;
import eu.europa.ec.jrc.euosme.gwt.client.InfoButton;
import eu.europa.ec.jrc.euosme.gwt.client.Utilities;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;

/**
 * Create an horizontal panel with a label and a text area
 * 
 * @version 3.0 - February 2011
 * @author 	Marzia Grasso
 */

public class CharacterStringLong extends Composite {
	
	private static CharacterStringUiBinder uiBinder = GWT.create(CharacterStringUiBinder.class);
	interface CharacterStringUiBinder extends UiBinder<Widget, CharacterStringLong> {	}
	
	/** TextBox declaration */
	@UiField
	public
	TextArea myTextArea = new TextArea();
	
	/** Global variable used to check if the field is empty or not */ 
	public boolean isRequired=false;
	
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
		
	/** Global variable used to move into the user guide as an anchor (OPTIONAL, if it is empty, it is used the tab's related anchor) */
	private String helpAnchor="";
	
	/** Relation between the form element and the tree item */
	private TreeItem myTreeItem;
				
	/** 
     * constructor CharacterString: 0..1 (String)
     * 
     * @param label		{@link String} = the label of the field
     * @param help		{@link String} = the anchor
     * @param required	{@link Boolean} = if it is true, the field is mandatory     
     * 
     * @return	the widget composed by an horizontal panel made up a label and a textarea
     */
	public CharacterStringLong(String label, String help, boolean required) {
		// Set global variables
		isRequired = required;
		helpAnchor = help;
		
		// Initialize widget
		initWidget(uiBinder.createAndBindUi(this));
		
		// Set Label widget
		setLabel(label);
				
		if (EUOSMEGWT.showAll) setDisplay(true);
		else setDisplay(required);
		
		//Set TextArea widget
		myTextArea.setCharacterWidth(80);
		myTextArea.setVisibleLines(5);
		
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
		if (isRequired==true && newItem.isEmpty()) {
       		// if the input string is empty return a warning message
	    	myError.setText(constants.mandatoryField());
			myError.setVisible(true);	    		    	  
	    }		
	}	
	
	/**
	 * @param myValue	{@link String} = set the value of myTextArea text box
	 */
	public void setMyValue(String myValue) {
		this.myTextArea.setValue(myValue);
	}

	/**
	 * @return {@link String} = the value of myTextArea text box
	 */
	public String getMyValue() {
		return this.myTextArea.getValue().trim();
	}	
	
	/**
	 * @param name {@link String} = the name of the field in the form
	 */
	public void setFormName(String name) {
		myTextArea.setName(name);
		myTextArea.getElement().setId(name);
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
     * This is called whenever a user press a key (tab, for instance) and force the field check
     * 
     * @see onKeyPress
     */
	@UiHandler("myTextArea")
	void onKeyPress(KeyPressEvent event) {
		if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_TAB) {
	    	this.myCheck();
	    	if (myError.isVisible()) {
	    		myTextArea.cancelKey();		    		
	    		myTextArea.setFocus(true);
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
	@UiHandler("myTextArea")
	void onFocus(FocusEvent event) {
		// select the related tree item
		if (myTreeItem==null) {
			TreeItem val = null;
			val = Utilities.getSelectTreeItem(myTextArea.getName());
			myTreeItem = val;						
		}
		// set selected item
		if (myTreeItem!=null) {
			Utilities.ensureItemVisible(myTreeItem);
			// get focus: it is lost on tree item selection 
		 	myTextArea.setFocus(true);
		}
	}
	
	/**
	 * OnBlur set the value to the corresponding {@link TreeItem}
	 * 
	 * @param event	{@link BlurEvent}
	 */
	@UiHandler("myTextArea")
	void onBlur(BlurEvent event) {
		Utilities.setTextTreeItem(myTreeItem,myTextArea.getText());		
	}
}