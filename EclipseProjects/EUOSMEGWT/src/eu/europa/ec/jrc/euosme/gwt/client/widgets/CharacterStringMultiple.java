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

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

import eu.europa.ec.jrc.euosme.gwt.client.EUOSMEGWT;
import eu.europa.ec.jrc.euosme.gwt.client.CheckFunctions;
import eu.europa.ec.jrc.euosme.gwt.client.Utilities;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;

/**
 * Create an horizontal panel with a  {@link Label} and a {@link FlexTable}
 * At the bottom, there is a text box with the button used to add an item to the list box 
 * 
 * @version 4.0 - December 2010
 * @author 	Marzia Grasso
 */
public class CharacterStringMultiple extends Composite {

	private static CharacterStringMultipleUiBinder uiBinder = GWT.create(CharacterStringMultipleUiBinder.class);
	interface CharacterStringMultipleUiBinder extends UiBinder<Widget, CharacterStringMultiple> {	}
	
	/** Constants declaration */
	private iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Error Label declaration */
	@UiField
	public
	Label myError = new Label();
	
	/** FlexTable declaration */
	@UiField
	public
	FlexTable myFlexTable = new FlexTable();
	
	/** ListBox declaration */
	@UiField
	public
	ListBox myListBox = new ListBox(true);
		
	/** New TextBox declaration */
	@UiField
	public
	TextBox newTextBox = new TextBox();
	
	/** Add Button declaration */
	@UiField
	public
	Button newButton = new Button();
	
	/** Array of values in the list/FlexTable */
	public ArrayList<String> myList = new ArrayList<String>();
	
	/** Button for information */
	@UiField
	public
	Button infoButton = new Button();
	
	/** grouping fields declaration */
	@UiField(provided = true)
	DisclosurePanel componentPanel = new DisclosurePanel();
	
	/** Label declaration */
	@UiField
	Label myLabel = new Label();
	
	/** Global variable used to check if the field is empty or not */ 
	private boolean isRequired=false;
	
	/** Global variable used to check the format of the input string */
	private CheckFunctions checkFunction;
	
	/** Global variable used to move into the user guide as an anchor */
	private String helpAnchor="";
	
	/** Relation between the form element and the tree item */
	private TreeItem myTreeItem;	
	
	/** 
     * constructor CharacterStringMultiple: 0..* (String)
     * 
     * @param label		{@link String} = the label of the field
     * @param help		{@link String} = the anchor in the help
     * @param required	{@link Boolean} = it is true, the field is mandatory
     * @param check		{@link CheckFunctions} = the format of the string
     * 
     * @return	the widget composed by an horizontal panel made up a label and a single-line text box and a {@link FlexTable} 
     */
	public CharacterStringMultiple(String label, String help, boolean required, CheckFunctions check) {

		// Set global variables
		isRequired = required;
		checkFunction = check;
		helpAnchor = help;
		
		// Initialize widget
		initWidget(uiBinder.createAndBindUi(this));
	    
	    // Set Label widget
		setLabel(label);
				
	    // Set the name of the form element
	    myListBox.setVisible(false);
	    
	    // Set the label of the button
	    newButton.setText(constants.add());
	    //newButton.ensureDebugId("newButton");
	    
	    myListBox.addFocusHandler(new FocusHandler(){
			@Override
			public void onFocus(FocusEvent event) {
				addNew("loadFile",myListBox.getItemText(myListBox.getItemCount()-1).trim());
			}	    	
	    });
	    
	    infoButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Utilities.openInfo(helpAnchor,infoButton);				
			}	
		});
	   
	    if (EUOSMEGWT.showAll) setDisplay(true);
		else setDisplay(required);
	    
	    //Set Error Label widget		
		myError.setVisible(false);
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
     * This is called whenever a user click the add button
     * 
     * @see onClick
     */
	@UiHandler("newButton")
	public void handleonClick(ClickEvent event) {
		addNew("newButton",newTextBox.getText().trim());	    	
	}
	
	/**
     * This is called whenever a user press a key (tab, for instance) and force the field check
     * 
     * @see onKeyPress event
     */
	@UiHandler("newTextBox")
	public void handleonKeyPressNew(KeyUpEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {	
		   	addNew("newButton",newTextBox.getText().trim());
		}		
	}
	
	/**
	 * This is called to make field's validation and show an error if it fails
	 */
	public void myCheck() {
		// hide error label
		myError.setVisible(false);
		if (isRequired==true && myList.size() == 0) {
	  	    myError.setText(constants.mandatoryField());
	  	    myError.setVisible(true);			
  	  	}
	}

	/**
	 * Add an item to FlexTable. Executed when the user clicks the newButton 
	 */
	public void addNew(String fromWhere, String myString) {
		final String newItem = myString;
		myError.setVisible(false);
		if (fromWhere=="newButton") {
			newTextBox.setFocus(true);
						 
			// Check if is empty
			if (newItem.isEmpty()){
				myError.setText(constants.emptyString());
		  	    myError.setVisible(true);
				return;
			}
			  
			// Don't add the deliveryPoint if it's already in the table.
			if (myList.contains(newItem.toUpperCase()))
			      return;
		
			//String checkret = Utilities.checkString(myFlexTable.getText(0,0),newItem,checkFunction);
			String checkret = Utilities.checkString(newItem,checkFunction);
			if (checkret != "") {
				myError.setText(checkret);
		  	    myError.setVisible(true);	
	    		return;
	    	}
		}
		
		// Add the String to the table.
		Integer row = myFlexTable.getRowCount();
		myList.add(newItem.toUpperCase());
		if (fromWhere=="newButton") {
			myListBox.addItem(newItem);
			myListBox.setItemSelected(myListBox.getItemCount()-1,true);
			// if it is empty, set the value of the current tree item
			Integer i = myListBox.getItemCount();
			if (i==1) { // set current value
				TreeItem val = Utilities.getSelectTreeItem(myListBox.getName());
				Utilities.setTextTreeItem(val,newItem);	
			}
			else { // else add a new node
				if (myTreeItem==null) myTreeItem = Utilities.getSelectTreeItem(myListBox.getName());
				if (myTreeItem!=null) {
					int j = myListBox.getName().lastIndexOf("[1]");
					String myFormName = myListBox.getName().substring(0,j);
					j = myFormName.lastIndexOf("[1]");
					myFormName = myFormName.substring(0,j) + "[" + i.toString() + "]";
					if (Utilities.getSelectTreeItem(myFormName)==null) {
						// clone parent of characterString element
						TreeItem pcloneMyTreeItem = new TreeItem();
						pcloneMyTreeItem.setTitle(myFormName);
						pcloneMyTreeItem.setText(myTreeItem.getParentItem().getParentItem().getText());
						// clone characterString element
						TreeItem cloneMyTreeItem = new TreeItem();
						myFormName+=myListBox.getName().substring(j+"[1]".length(), myListBox.getName().length());
						cloneMyTreeItem.setTitle(myFormName);
						cloneMyTreeItem.setText(myTreeItem.getParentItem().getText());				
						pcloneMyTreeItem.addItem(cloneMyTreeItem);
						// clone value
						TreeItem cloneMyTreeItemValue = new TreeItem(constants.XMLValue() + newItem);
						cloneMyTreeItem.addItem(cloneMyTreeItemValue);
						// insert new item
						int beforeIndex = myTreeItem.getParentItem().getParentItem().getParentItem().getChildIndex(myTreeItem.getParentItem().getParentItem());
						myTreeItem.getParentItem().getParentItem().getParentItem().insertItem(beforeIndex+1, pcloneMyTreeItem);
						// set selected item
						Utilities.ensureItemVisible(cloneMyTreeItemValue);
					}
					else {
						TreeItem val = Utilities.getSelectTreeItem(myFormName);
						if (val.getChild(0)!=null)
							if (val.getChild(0).getChild(0)!=null) val.getChild(0).getChild(0).setText(constants.XMLValue() + newItem);
					}
				}
			}
			
		}		
		myFlexTable.setText(row, 0, newItem);
		  
		// Add a button to remove this deliveryPoint from the table.
		Button removeDeliveryPointButton = new Button("x");
		removeDeliveryPointButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Integer removedIndex = myList.indexOf(newItem.toUpperCase()) + 1;
				myList.remove(removedIndex-1);
		        myListBox.removeItem(removedIndex-1);
		        myFlexTable.removeRow(removedIndex-1);
		        int j = myListBox.getName().lastIndexOf("[1]");
				String myFormName = myListBox.getName().substring(0,j);
				j = myFormName.lastIndexOf("[1]");
				myFormName = myFormName.substring(0,j) + "[" + (removedIndex).toString() + "]";// + myListBox.getName().substring(j+"[1]".length(), myListBox.getName().length());
				if (myListBox.getItemCount()!=0) {
			        TreeItem val = Utilities.getSelectTreeItem(myFormName);
					if (val!=null) {
						GWT.log("TREE: remove " + myFormName, null);
						val.remove();
					}
					for (Integer x = removedIndex + 1; x <= (myListBox.getItemCount()+1);x++) {
						// Reset count of tree items
						j = myListBox.getName().lastIndexOf("[1]");
						myFormName = myListBox.getName().substring(0,j);
						j = myFormName.lastIndexOf("[1]");
						String searchFormName = myFormName.substring(0,j) + "[" + (x).toString() + "]";// + myListBox.getName().substring(j+"[1]".length(), myListBox.getName().length());
						Integer z = x-1;
						String newFormName = myFormName.substring(0,j) + "[" + (z).toString() + "]";
						GWT.log("TREE: search " + searchFormName + " and change to " + newFormName,null);								
						TreeItem val1 = Utilities.getSelectTreeItem(searchFormName);
						if (val1!=null) {
							val1.getChild(0).setTitle(val1.getChild(0).getTitle().replace(searchFormName, newFormName));
							val1.setTitle(newFormName);
						}
					}
		        }
		        else {
		        	TreeItem val = Utilities.getSelectTreeItem(myFormName+myListBox.getName().substring(j+"[1]".length(), myListBox.getName().length()));
					Utilities.setTextTreeItem(val,constants.emptyXMLValue());					
		        }
		    }
		});
		myFlexTable.setWidget(row, 1, removeDeliveryPointButton);
		
		newTextBox.setFocus(true);
	}

	/**
	 * @param myValues {@link ArrayList} of {@link String} = the new values to insert
	 */
	public void setMyValues(ArrayList<String> myValues) {
		myList.clear();
		myListBox.clear();
		myFlexTable.clear();
		for (int i=0;i<myValues.size();i++) {
			myList.add(myValues.get(i).toUpperCase());
			myListBox.addItem(myValues.get(i));
			myListBox.setItemSelected(i,true);			
			myFlexTable.setText(i, 0, myValues.get(i));
		}		
	}

	/**
	 * @return {@link ArrayList} of {@link String} = the values of the {@link FlexTable}
	 */
	public ArrayList<String> getMyValues() {
		return myList;
	}

	/**
	 * @param name {@link String} = the name of the field in the form
	 */
	public void setFormName(String name) {
		myListBox.setName(name);
		myListBox.getElement().setId(name);
		newButton.getElement().setId("newButton-" + name);
		newTextBox.getElement().setId("new-" + name);
		myError.getElement().setId("error-" + name);
	}
	
	/**
	 * @param required {@link Boolean = true if the field is required
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
	@UiHandler("newButton")
	void handleonKeyPressNewButton(KeyPressEvent event) {
		if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_TAB) {	
			myCheck();
		   	if (myError.isVisible()) newTextBox.setFocus(true);		    	 
		}	
	}
		
	/**
     * This is called whenever a user focus on the new text box
     * 1. goto related anchor in the user guide
     * 2. select related tree item
     * 
     * @see onFocus event
     */
	@UiHandler("newTextBox")
	void onFocus(FocusEvent event) {
		// select the related tree item
		TreeItem val = null;
		val = Utilities.getSelectTreeItem(myListBox.getName());
		myTreeItem = val;						
		// set selected item
		Utilities.ensureItemVisible(myTreeItem);
		// get focus: it is lost on tree item selection 
	 	newTextBox.setFocus(true);
	}
}