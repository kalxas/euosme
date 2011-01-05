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
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
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
import eu.europa.ec.jrc.euosme.gwt.client.Utilities;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.ui.TabGeographic;

/**
 * @version 5.0 - December 2010
 * @author 	Marzia Grasso
 */
public class GeoBoundsMultiple extends Composite {

	private static CharacterStringMultipleUiBinder uiBinder = GWT.create(CharacterStringMultipleUiBinder.class);
	interface CharacterStringMultipleUiBinder extends UiBinder<Widget, GeoBoundsMultiple> {	}
	
	/** Error Label declaration */
	@UiField
	public
	Label myError = new Label();
	
	/** New TextBox declaration */
	@UiField
	public
	TextBox newTextBoxNorth = new TextBox();
	@UiField
	public
	TextBox newTextBoxSouth = new TextBox();
	@UiField
	public
	TextBox newTextBoxWest = new TextBox();
	@UiField
	public
	TextBox newTextBoxEast = new TextBox();
		
	/** grouping fields declaration */
	@UiField(provided = true)
	DisclosurePanel componentPanel = new DisclosurePanel();
	
	/** Label declaration */
	@UiField
	Label myLabel = new Label();	
	
	/** FlexTable declaration */
	@UiField
	FlexTable myFlexTable = new FlexTable();
	
	/** ListBox declaration */
	@UiField
	ListBox myListBox = new ListBox(true);
	
	/** Add Button declaration */
	@UiField
	Button newButton = new Button();
	
	/** Button for information */
	@UiField
	Button infoButton = new Button();
		
	/** Constants declaration */
	private iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Array of values in the list/FlexTable */
	private ArrayList<String> myList = new ArrayList<String>();
	
	/** Global variable used to check if the field is empty or not */ 
	private boolean isRequired=false;
	
	/** 
     * constructor GeoBoundsMultiple
     * 
     * @param label		{@link String} = the label of the field
     * @param required	{@link Boolean} = it is true, the field is mandatory
     */
	public GeoBoundsMultiple(String label, boolean required) {
		// Set global variables
		isRequired = required;
		
		// Initialize widget
		initWidget(uiBinder.createAndBindUi(this));
		
		myFlexTable.setText(0, 0, constants.north());
		myFlexTable.setText(0, 1, constants.east());
		myFlexTable.setText(0, 2, constants.south());
		myFlexTable.setText(0, 3, constants.west());
		//myFlexTable.setText(0, 4, constants.address());

		// Add styles to elements in the stock list table.
		myFlexTable.getCellFormatter().addStyleName(0, 0, "GeoBoundsHeader");
		myFlexTable.getCellFormatter().addStyleName(0, 1, "GeoBoundsHeader");
		myFlexTable.getCellFormatter().addStyleName(0, 2, "GeoBoundsHeader");
		myFlexTable.getCellFormatter().addStyleName(0, 3, "GeoBoundsHeader");
		//myFlexTable.getCellFormatter().addStyleName(0, 4, "GeoBoundsHeader");
		
		newTextBoxSouth.setWidth("75px");
		newTextBoxNorth.setWidth("75px");
		newTextBoxEast.setWidth("75px");
		newTextBoxWest.setWidth("75px");
		
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
	    
	    if (EUOSMEGWT.showAll) setDisplay(true);
		else setDisplay(required);
	    
	    //Set Error Label widget		
		myError.setVisible(false);
		
		infoButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Utilities.openInfo("boundingBox",infoButton);				
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
     * This is called whenever a user press a key (tab, for instance) and force the field check
     * 
     * @see onKeyPress event
     */
	@UiHandler("newTextBoxWest")
	public void handleonKeyPressNew(KeyUpEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {	
			boolean n = true; boolean s = true; boolean e = true; boolean w = true;
			try {
				Double.parseDouble(newTextBoxNorth.getText().trim());			
			}
			catch(NumberFormatException nFE) {
				n = false;
			}
			try {
				Double.parseDouble(newTextBoxSouth.getText().trim());			
			}
			catch(NumberFormatException nFE) {
				s = false;
			}
			try {
				Double.parseDouble(newTextBoxEast.getText().trim());			
			}
			catch(NumberFormatException nFE) {
				e = false;
			}
			try {
				Double.parseDouble(newTextBoxWest.getText().trim());			
			}
			catch(NumberFormatException nFE) {
				w = false;
			}
			if (n & s & e & w) addNew("newButton",newTextBoxNorth.getText().trim()+";"+newTextBoxEast.getText().trim()+";"+newTextBoxSouth.getText().trim()+";"+newTextBoxWest.getText().trim());
			else {
				myError.setText(constants.invalidDouble());
				myError.setVisible(true);
			}	
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
			//newTextBoxNorth.setFocus(true);
			
			// Check if is empty
			if (newItem.isEmpty()){
				myError.setText(constants.emptyString());
		  	    myError.setVisible(true);
				return;
			}
			  
			// Don't add the string if it's already in the table.
			if (myList.contains(newItem.toUpperCase()))
			      return;			
		}
		
		// Add the String to the table.
		Integer row = myFlexTable.getRowCount();
		myList.add(newItem.toUpperCase());
		if (fromWhere=="newButton") {
			myListBox.addItem(newItem);
			myListBox.setItemSelected(myListBox.getItemCount()-1,true);
			// if it is empty, set the value of the current tree item
			Integer i = myListBox.getItemCount();
			if (i==1) {
				TreeItem val = Utilities.getSelectTreeItem(myListBox.getName()+ ".ex_geographicboundingbox[1].northboundlatitude[1].decimal[1]");
				Utilities.setTextTreeItem(val,newTextBoxNorth.getText());
				val = Utilities.getSelectTreeItem(myListBox.getName()+ ".ex_geographicboundingbox[1].eastboundlongitude[1].decimal[1]");
				Utilities.setTextTreeItem(val,newTextBoxEast.getText());	
				val = Utilities.getSelectTreeItem(myListBox.getName()+ ".ex_geographicboundingbox[1].southboundlatitude[1].decimal[1]");
				Utilities.setTextTreeItem(val,newTextBoxSouth.getText());	
				val = Utilities.getSelectTreeItem(myListBox.getName()+ ".ex_geographicboundingbox[1].westboundlongitude[1].decimal[1]");
				Utilities.setTextTreeItem(val,newTextBoxWest.getText());
				TreeItem geographicElement = Utilities.getSelectTreeItem(myListBox.getName());
			 	Utilities.ensureItemVisible(geographicElement);
			}
			else { // else add a new node
				//String myXml = "<gmd:geographicElement><gmd:EX_GeographicBoundingBox><gmd:westBoundLongitude><gco:Decimal>#WEST#</gco:Decimal></gmd:westBoundLongitude><gmd:eastBoundLongitude><gco:Decimal>#EAST#</gco:Decimal></gmd:eastBoundLongitude><gmd:southBoundLatitude><gco:Decimal>#SOUTH#</gco:Decimal></gmd:southBoundLatitude><gmd:northBoundLatitude><gco:Decimal>#NORTH#</gco:Decimal></gmd:northBoundLatitude></gmd:EX_GeographicBoundingBox></gmd:geographicElement></gmd:EX_Extent></gmd:extent>";
				int j = myListBox.getName().lastIndexOf("[1]");
				String myFormName = myListBox.getName().substring(0,j) + "[" + i.toString() + "]";
				if (Utilities.getSelectTreeItem(myFormName + ".ex_geographicboundingbox[1]")==null) {
					TreeItem myTreeItem = Utilities.getSelectTreeItem(myListBox.getName());
					TreeItem geographicElement = new TreeItem();
					geographicElement.setTitle(myFormName);
					geographicElement.setText(myTreeItem.getText());
					TreeItem EX_GeographicBoundingBox = new TreeItem();
					EX_GeographicBoundingBox.setTitle(myFormName + ".ex_geographicboundingbox[1]");
					EX_GeographicBoundingBox.setText("gmd:EX_GeographicBoundingBox");
					geographicElement.addItem(EX_GeographicBoundingBox);
					// West
					TreeItem westBoundLongitude = new TreeItem();
					westBoundLongitude.setTitle(myFormName + ".westboundlongitude[1]");
					westBoundLongitude.setText("gmd:westBoundLongitude");
					EX_GeographicBoundingBox.addItem(westBoundLongitude);
					TreeItem decimalW = new TreeItem();
					decimalW.setTitle(myFormName + ".decimal[1]");
					decimalW.setText("gco:Decimal");
					westBoundLongitude.addItem(decimalW);
					TreeItem myValueW = new TreeItem(constants.XMLValue() + newItem.split(";")[3]);
					decimalW.addItem(myValueW);
					// East
					TreeItem eastBoundLongitude = new TreeItem();
					eastBoundLongitude.setTitle(myFormName + ".eastboundlongitude[1]");
					eastBoundLongitude.setText("gmd:eastBoundLongitude");
					EX_GeographicBoundingBox.addItem(eastBoundLongitude);
					TreeItem decimalE = new TreeItem();
					decimalE.setTitle(myFormName + ".decimal[1]");
					decimalE.setText("gco:Decimal");
					eastBoundLongitude.addItem(decimalE);
					TreeItem myValueE = new TreeItem(constants.XMLValue() + newItem.split(";")[1]);
					decimalE.addItem(myValueE);
					// South
					TreeItem southBoundLatitude = new TreeItem();
					southBoundLatitude.setTitle(myFormName + ".southboundlatitude[1]");
					southBoundLatitude.setText("gmd:southBoundLatitude");
					EX_GeographicBoundingBox.addItem(southBoundLatitude);
					TreeItem decimalS = new TreeItem();
					decimalS.setTitle(myFormName + ".decimal[1]");
					decimalS.setText("gco:Decimal");
					southBoundLatitude.addItem(decimalS);
					TreeItem myValueS = new TreeItem(constants.XMLValue() + newItem.split(";")[2]);
					decimalS.addItem(myValueS);
					// North
					TreeItem northBoundLatitude = new TreeItem();
					northBoundLatitude.setTitle(myFormName + ".northboundlatitude[1]");
					northBoundLatitude.setText("gmd:northBoundLatitude");
					EX_GeographicBoundingBox.addItem(northBoundLatitude);
					TreeItem decimalN = new TreeItem();
					decimalN.setTitle(myFormName + ".decimal[1]");
					decimalN.setText("gco:Decimal");
					northBoundLatitude.addItem(decimalN);
					TreeItem myValueN = new TreeItem(constants.XMLValue() + newItem.split(";")[0]);
					decimalN.addItem(myValueN);
					int found = 0;
					for (int ii=0;ii<myTreeItem.getParentItem().getChildCount();ii++) {
						if (myTreeItem.getParentItem().getChild(ii).getTitle().toLowerCase().contains("geographicelement"))
							found = ii;					
					}
					myTreeItem.getParentItem().insertItem(found+1,geographicElement);				
					Utilities.ensureItemVisible(geographicElement);
				}				
			}			
		}		
		
		myFlexTable.setText(row, 0, newItem.split(";")[0]); //north
		myFlexTable.setText(row, 1, newItem.split(";")[1]); //east
		myFlexTable.setText(row, 2, newItem.split(";")[2]); //south
		myFlexTable.setText(row, 3, newItem.split(";")[3]); //west
		
		Button removeButton = new Button("x");
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Integer removedIndex = myList.indexOf(newItem.toUpperCase()) + 1;
				myList.remove(removedIndex-1);
		        myListBox.removeItem(removedIndex-1);
		        myFlexTable.removeRow(removedIndex);
		        int j = myListBox.getName().lastIndexOf("[1]");
		        String myFormName = myListBox.getName().substring(0,j) + "[" + (removedIndex).toString() + "]";
				if (myListBox.getItemCount()!=0) {
			        TreeItem val = Utilities.getSelectTreeItem(myFormName);
					if (val!=null) {
						GWT.log("TREE: remove " + myFormName, null);
						val.remove();
					}
					for (Integer x = removedIndex + 1; x <= (myListBox.getItemCount()+1);x++) {
						// Reset count of tree items
						myFormName = myListBox.getName();
						j = myFormName.lastIndexOf("[1]");
						String searchFormName = myFormName.substring(0,j) + "[" + (x).toString() + "]";
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
		        	TreeItem val = Utilities.getSelectTreeItem(myListBox.getName()+ ".ex_geographicboundingbox[1].northboundlatitude[1].decimal[1]");
		        	Utilities.setTextTreeItem(val,constants.emptyXMLValue());
					val = Utilities.getSelectTreeItem(myListBox.getName()+ ".ex_geographicboundingbox[1].eastboundlongitude[1].decimal[1]");
					Utilities.setTextTreeItem(val,constants.emptyXMLValue());	
					val = Utilities.getSelectTreeItem(myListBox.getName()+ ".ex_geographicboundingbox[1].southboundlatitude[1].decimal[1]");
					Utilities.setTextTreeItem(val,constants.emptyXMLValue());	
					val = Utilities.getSelectTreeItem(myListBox.getName()+ ".ex_geographicboundingbox[1].westboundlongitude[1].decimal[1]");
					Utilities.setTextTreeItem(val,constants.emptyXMLValue());		        	
		        }
		    }
		});
		myFlexTable.setWidget(row, 4, removeButton);
		Button selectButton = new Button(constants.runQuery());
		selectButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (EUOSMEGWT.apiMapstraction.equalsIgnoreCase("google")) {
					LatLng northEast = LatLng.newInstance(Double.parseDouble(newItem.split(";")[0]), Double.parseDouble(newItem.split(";")[1]));
					LatLng southWest = LatLng.newInstance(Double.parseDouble(newItem.split(";")[2]), Double.parseDouble(newItem.split(";")[3]));
					LatLngBounds latlngbounds = LatLngBounds.newInstance(southWest, northEast);
					TabGeographic.queryTextBox.setText(latlngbounds.getCenter().toUrlValue());
					TabGeographic.queryButton.click();
				}
				else {
					setBoundsMapstraction(Double.parseDouble(newItem.split(";")[2]),Double.parseDouble(newItem.split(";")[3]),Double.parseDouble(newItem.split(";")[0]), Double.parseDouble(newItem.split(";")[1]));					
				}
			}
		});	
		myFlexTable.setWidget(row, 5, selectButton);
	}

	
	/**
	 * @return {@link ArrayList} of {@link String} = list of values of the flex table
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
		newTextBoxNorth.getElement().setId("new-north-" + name);
		newTextBoxSouth.getElement().setId("new-south-" + name);
		newTextBoxEast.getElement().setId("new-east-" + name);
		newTextBoxWest.getElement().setId("new-west-" + name);
		
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
	@UiHandler("newButton")
	void handleonKeyPressNewButton(KeyPressEvent event) {
		if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_TAB) {	
			myCheck();
		   	if (myError.isVisible()) newTextBoxNorth.setFocus(true);		    	 
		}	
	}
	
	/**
     * This is called whenever a user click the add button
     * 
     * @see onClick
     */
	@UiHandler("newButton")
	void handleonClick(ClickEvent event) {
		boolean n = true; boolean s = true; boolean e = true; boolean w = true;
		try {
			Double.parseDouble(newTextBoxNorth.getText().trim());			
		}
		catch(NumberFormatException nFE) {
			n = false;
		}
		try {
			Double.parseDouble(newTextBoxSouth.getText().trim());			
		}
		catch(NumberFormatException nFE) {
			s = false;
		}
		try {
			Double.parseDouble(newTextBoxEast.getText().trim());			
		}
		catch(NumberFormatException nFE) {
			e = false;
		}
		try {
			Double.parseDouble(newTextBoxWest.getText().trim());			
		}
		catch(NumberFormatException nFE) {
			w = false;
		}
		if (n & s & e & w) addNew("newButton",newTextBoxNorth.getText().trim()+";"+newTextBoxEast.getText().trim()+";"+newTextBoxSouth.getText().trim()+";"+newTextBoxWest.getText().trim());
		else {
			myError.setText(constants.invalidDouble());
			myError.setVisible(true);
		}	
	}
	
	/**
	 * JSNI function to set Bounds on mapstraction map
	 * 
	 * @param swlat	{@link Double} = SW latitude
	 * @param swlon {@link Double} = SW longitude
	 * @param nelat {@link Double} = NE latitude
	 * @param nelon {@link Double} = SW longitude
	 */
	private native void setBoundsMapstraction(double swlat, double swlon, double nelat, double nelon) /*-{
		$wnd.mapstraction.setBounds(new $wnd.mxn.BoundingBox(swlat,swlon,nelat,nelon));		
	}-*/;
}