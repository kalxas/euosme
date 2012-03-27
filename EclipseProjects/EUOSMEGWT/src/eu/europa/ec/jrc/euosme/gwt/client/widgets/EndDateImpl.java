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

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import eu.europa.ec.jrc.euosme.gwt.client.EUOSMEGWT;
import eu.europa.ec.jrc.euosme.gwt.client.InfoButton;
import eu.europa.ec.jrc.euosme.gwt.client.MyDateBox;
import eu.europa.ec.jrc.euosme.gwt.client.Utilities;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;

/**
 * Create an horizontal panel with a label and a date box
 * 
 * @version 4.0 - February 2011
 * @author 	Marzia Grasso
 */
public class EndDateImpl extends Composite {
	
	private static EndDateImplUiBinder uiBinder = GWT.create(EndDateImplUiBinder.class);
	interface EndDateImplUiBinder extends UiBinder<Widget, EndDateImpl> {	}

	final iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** grouping fields declaration */
	@UiField(provided = true)
	DisclosurePanel componentPanel = new DisclosurePanel();
	
	/** DateBox declaration */
	@UiField
	public
	MyDateBox myDateBox = new MyDateBox();
	
	/** radio Date declaration */
	@UiField
	public
	RadioButton radioDate;
	
	/** radio Now declaration */
	@UiField
	public
	RadioButton radioNow;
	
	/** Global variable used to check if the field is empty or not */ 
	public boolean isRequired=false;
	
	/** Label declaration */
	@UiField
	Label myLabel = new Label();
	
	/** Error Label declaration */
	@UiField
	Label myError = new Label();
	
	/** Button for information */
	@UiField
	InfoButton infoButton = new InfoButton();
	
	/** now or a specific date*/
	public boolean isNow = true;
	
	
	/** Global variable used to move into the user guide as an anchor */
	private String helpAnchor="";
	
	/** Relation between the form element and the tree item */
	private TreeItem myTreeItem;
			
	/** 
     * constructor Date: 0..1 (Date)
     * 
     * @param label		{@link String} = the label of the field
     * @param help		{@link String} = the anchor in the user guide
     * @param required	{@link Boolean} = if it is true, the field is mandatory
     * 
     * @return	the widget composed by an horizontal panel made up a label and a single-line date box
     */
	public EndDateImpl(String label, String help, boolean required) {
		// Set global variables
		isRequired = required;
		helpAnchor = help;
		
		// Initialize widget
		initWidget(uiBinder.createAndBindUi(this));
		
		// Set Label widget
		setLabel(label);
				
		setDisplay(true);
		
		// get tree item 
		if (myTreeItem==null) {
			TreeItem val = null;
			val = Utilities.getSelectTreeItem(myDateBox.getTextBox().getName());
			myTreeItem = val;						
		}
		
		// default is Now
		radioNow.setValue(true);
		myDateBox.setEnabled(false);
		setNow(true);

		// set format
		myDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd")));
		
		// set focus handler
		myDateBox.getTextBox().addFocusHandler(new FocusHandler(){
			@Override
			public void onFocus(FocusEvent event) {
				// select the related tree item
				if (myTreeItem==null) {
					TreeItem val = null;
					val = Utilities.getSelectTreeItem(myDateBox.getTextBox().getName());
					myTreeItem = val;						
				}
				// set selected item
				if (myTreeItem!=null) {
					Utilities.ensureItemVisible(myTreeItem);
					// get focus: it is lost on tree item selection 
				 	myDateBox.getTextBox().setFocus(true);
				}
			}
			
		});
		
		// set blur handler
		myDateBox.getTextBox().addBlurHandler(new BlurHandler(){
			@Override
			public void onBlur(BlurEvent event) {
				myCheck();
				if (myError.isVisible()) {
					//myDateBox.setFocus(true);
				} else {
					Utilities.setTextTreeItem(myTreeItem,myDateBox.getTextBox().getText().split(" ")[0]);
				}									
			}
		});
		
		// radio handler 
		radioNow.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				myDateBox.setEnabled(false);	
				setNow(true);
				
			}			
		});
		radioDate.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				myDateBox.setEnabled(true);		
				setNow(false);
			}			
		});		
		
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
	
	/** Now or a date */
	public void loadType(){
		// default is Now
		TreeItem val = null;
		val = Utilities.getSelectTreeItem(myDateBox.getTextBox().getName());	
		if (val != null && !val.getText().equalsIgnoreCase(Utilities.getEmptyTreeItemValue()) ){
			radioDate.setValue(true);
			myDateBox.setEnabled(true);
			setNow(false);
		}
		else {
			radioNow.setValue(true);
			myDateBox.setEnabled(false);
			setNow(true);
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
	 * This is called to make field's validation and show an error if it fails
	 */
	public void myCheck() {
		// declare constants
		iso19115Constants constants = GWT.create(iso19115Constants.class);
		// get input string
		java.util.Date newItem = this.getMyValue();
		// hide error label
		myError.setVisible(false);
		// check if it is required or not and perform the right actions
		if (isRequired==true && newItem == null) {
       		// if the input string is empty return a warning message
			myError.setText(constants.mandatoryField());
			myError.setVisible(true);	    	
	    }	
	}	
	
	/** in case multiple, set the index of the date */
	public void setIndex(Integer n){
		radioDate.setName(radioDate.getName()+ n.toString());
		radioNow.setName(radioNow.getName()+ n.toString());
	}
	
	/** set End date to now */
	public void setNow(boolean now){
		isNow = now;
		// get tree item 
		if (myTreeItem==null) {
			TreeItem val = null;
			val = Utilities.getSelectTreeItem(myDateBox.getTextBox().getName());
			myTreeItem = val;						
		}
		
		if (isNow){
			Utilities.setTextTreeItem(myTreeItem, constants.now());
		}
	}
	
	/**
	 * @param myValue {@link Date} = set the value of myTextBox text box
	 */
	public void setMyValue(java.util.Date myValue) {
		this.myDateBox.setValue(myValue);		
	}

	/**
	 * @return {@link Date} = the value of myTextBox text box
	 */
	public java.util.Date getMyValue() {
		return this.myDateBox.getValue();
	}	
	
	/**
	 * @param name {@link String} = the name of the field in the form
	 */
	public void setFormName(String name) {
		myDateBox.getTextBox().setName(name);
		myDateBox.getTextBox().getElement().setAttribute("id", name);
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
	 * @param newAnchor {@link String} = the new anchor to use
	 */
	public void setHelpAnchor(String newAnchor) {
		helpAnchor=newAnchor;
		infoButton.setHelpAnchor(helpAnchor);		
	}

}
