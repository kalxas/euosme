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
import eu.europa.ec.jrc.euosme.gwt.client.AppModes;
import eu.europa.ec.jrc.euosme.gwt.client.Utilities;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;

/**
 * Create an horizontal panel with a label and a list box 
 * The list items come from an external code list service
 * 
 * @version 2.0 - October 2010
 * @author 	Marzia Grasso
 */
public class Boolean extends Composite  {
	
	private static BooleanUiBinder uiBinder = GWT.create(BooleanUiBinder.class);

	interface BooleanUiBinder extends UiBinder<Widget, Boolean> {}
	
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
	ListBox myListBox = new ListBox();
	
	/** Button for information */
	@UiField
	Button infoButton = new Button();
	
	// Declare constants
	private iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Global variable used to check if the field is empty or not */ 
	private boolean isRequired=false;
	
	/** Global variable used to move into the user guide as an anchor */
	private String helpAnchor="";
	
	/** Relation between the form element and the tree item */
	private TreeItem myTreeItem;	
		
	/** 
     * constructor Boolean
     * 
     * @param label				{@link String} = the label of the field
     * @param help				{@link String} = the anchor in the help 
     * @param required			{@link Boolean} = if it is true, the field is mandatory
     * @param myDefaultValue	{@link String} = the default value
     * 
     * @return	the widget composed by an horizontal panel made up a label and a list box
     */
	public Boolean(String label, String help, boolean required, String myDefaultValue) {
		// Set global variables
		isRequired = required;
		helpAnchor = help;
		
		// Initialize widget
		initWidget(uiBinder.createAndBindUi(this));
		
		// Set Label widget
		setLabel(label);
				
		// Set Label and name of myList widget
		setInterface();
		
		setDisplay(true);
		
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
	 * Checking the current {@link AppModes}, this is used to remove elements, show and hide and change labels...
	 */
	private void setInterface() {
		if ((EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.GEOPORTAL.toString()))) {
			myListBox.clear();
			myListBox.addItem(constants.notEvaluated(),"");		
			myListBox.addItem(constants.notConformant(),"false");
			myListBox.addItem(constants.conformant(),"true");						
		}
		else {
			myListBox.clear();
			myListBox.addItem(constants.notEvaluated(),"");		
			myListBox.addItem(constants.isfalse(),"false");
			myListBox.addItem(constants.istrue(),"true");
		}		
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
		myCheck();
	}
		
	/**
	 * OnChange event set the value in the {@link TreeItem}
	 */
	@UiHandler("myListBox")
	void onChange(ChangeEvent event) {
		Utilities.setTextTreeItem(myTreeItem,getMyValue());	
		myCheck();
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
			if (myListBox.getItemText(i)==myValue) sel_i = i;
		}
		myListBox.setItemSelected(sel_i, true);
	}

	/**
	 * @return {@link String} = the value of selected item of myListBox
	 */
	public String getMyValue() {
		//String ret = myListBox.getItemText(myListBox.getSelectedIndex());
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
		if (required==true) myLabel.setText(myLabel.getText() + " (*)");
		if (EUOSMEGWT.showAll) setDisplay(true);
		else setDisplay(required);
	}
	
	/**
	 * @param visible {@link Boolean} true to open disclosure panel and show fields group
	 */
	public void setDisplay(boolean visible) {
		componentPanel.setOpen(visible);
	}	
}