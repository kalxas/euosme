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
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import eu.europa.ec.jrc.euosme.gwt.client.AppModes;
import eu.europa.ec.jrc.euosme.gwt.client.CIOrientations;
import eu.europa.ec.jrc.euosme.gwt.client.InfoButton;
import eu.europa.ec.jrc.euosme.gwt.client.Utilities;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Messages;

/**
 * Create CI model
 * 
 * @version 4.0 - February 2011
 * @author 	Marzia Grasso
 */

public class CI extends Composite {
	@UiTemplate("CI.ui.xml")
 	interface CIUiBinder extends UiBinder<Widget, CI> {}
	private static CIUiBinder CIUiBinder = GWT.create(CIUiBinder.class);
	
	/** Global variable used to check if the field is empty or not */ 
	public boolean isRequired=false;
	
	/** Global variable used to check if the field could be multiple */ 
	public boolean isMultiple=false;
	
	/** grouping fields declaration */
	@UiField(provided = true)
	VerticalPanel fieldsGroupVertical = new VerticalPanel();
	
	@UiField(provided = true)
	HorizontalPanel fieldsGroupHorizontal = new HorizontalPanel();
	
	public
	CellPanel fieldsGroup;
	
	/** Button for information */
	@UiField
	InfoButton infoButton = new InfoButton();
	
	/** vertical panel */
	@UiField(provided = true)
	protected
	VerticalPanel myPanel = new VerticalPanel();
	
	/** grouping fields declaration */
	@UiField(provided = true)
	protected
	DisclosurePanel componentPanel = new DisclosurePanel();	
	
	/** title declaration */
	@UiField(provided = true)
	protected
	Label myLabel = new Label();
	
	/** delete button */
	@UiField(provided = true)
	protected
	Button removeGroupButton = new Button();
	
	/** Error Label declaration */
	@UiField
	protected
	Label myError = new Label();
	
	/** Messages declaration */
	protected iso19115Messages messages = GWT.create(iso19115Messages.class);
		
	/** Name of the element in the form */
	private String myFormName;
	
	/** Global variable used to move into the user guide as an anchor */
	private String helpAnchor="";
	
	/**
	 * constructor CI model
	 * 
	 * @param label		{@link String} = the label in the header
	 * @param required	{@link Boolean} = indicates if the element is required
	 * @param multiple	{@link Boolean} = indicates if the element could be duplicated
	 * @param help		{@link String} = the anchor in the help 
	 */
	public CI(String label, boolean required, boolean multiple, String help, CIOrientations orientation) {
		// Set global variables
		isRequired = required;
		isMultiple = multiple;
		helpAnchor = help;
		
		// Add * for mandatory fields
		if (multiple==true) {
			removeGroupButton.setTitle(messages.remove(myLabel.getText()));
			removeGroupButton.setEnabled(false);
			removeGroupButton.setVisible(true);
			removeGroupButton.addStyleName("minusButton");			
		}
		setLabel(label);			
		
		// Initialize widget
		initWidget(CIUiBinder.createAndBindUi(this));				
		
		//componentPanel.setOpen(required);
		componentPanel.setOpen(true); // always opened
		//componentPanel.getElement().getStyle().setBackgroundColor("red");
		
		//Set Error Label widget		
		myError.setVisible(false);
		
		sinkEvents(com.google.gwt.user.client.Event.ONKEYPRESS);
		
		// Set info button
		infoButton.setHelpAnchor(helpAnchor);
		componentPanel.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {				
				event.getTarget().setOpen(true);
			}	
		});
		
		// Set preferred layout
		if (orientation.equals(CIOrientations.VERTICAL)) fieldsGroup = fieldsGroupVertical;
		else fieldsGroup = fieldsGroupHorizontal;
	}
	
	/**
	 * Set the header
	 * 
	 * @param label	{@link String} = the header string
	 */
	public void setLabel(String label) {		
		if (isMultiple==true) myLabel.setText(label + " 1");
		else {
			if(isRequired==true) myLabel.setText(label + " (*)");
			else myLabel.setText(label);
		}			
	}
	
	/**
	 * Handle the click event on remove button: delete the widget from the DOM and delete the corresponding {@link TreeItem}
	 * 
	 * @param event	{@link ClickEvent}
	 */
	@UiHandler("removeGroupButton")
	void handleRemoveGroup(ClickEvent event) {
		this.removeFromParent();
		TreeItem myTreeItem = Utilities.getSelectTreeItem(this.getElement().getId());
		if (myTreeItem!=null) {
			if (myTreeItem.getParentItem()!=null) myTreeItem.getParentItem().remove();
			else myTreeItem.remove();
		}
	}
	
	/**
	 * This is called to make a client (first) check of the field
	 */
	public void myCheck() {}
	
	
	/**
	 * Set the ID used to identify the element or group of elements in the XML.
	 * It has a corresponding TreeItem with the same title.
	 * 
	 * @param name 	{@link String} = the name of the field in the form (ID or path)
	 */
	public void setFormName(String name) {
		setMyFormName(name);
	}
	
	/**
	 * Checking the current {@link AppModes}, this is used to remove elements, show and hide and change labels...
	 * 
	 * @param i	{@link Integer} = this number is used to differentiate the profiles
	 */
	public void setInterface(int i) {}
	
	/**
	 * Hide the disclosure panel to make more simple the interface 
	 */
	public void removeDisclosure() {
		componentPanel.removeFromParent();
		myPanel.add(fieldsGroup);
	}
		
	/** 
	 * If the user enter TAB key, start the check/validation of the element 
	 * 
	 * @see com.google.gwt.user.client.ui.Composite#onBrowserEvent(com.google.gwt.user.client.Event)
	 */
	@Override
	public void onBrowserEvent(com.google.gwt.user.client.Event event) {
		super.onBrowserEvent(event);
		if (event.getKeyCode() == KeyCodes.KEY_TAB) {
	    	this.myCheck();
	    }
	}
	
	/**
	 * Set the ID used to identify the element or group of elements in the XML.
	 * It has a corresponding TreeItem with the same title.
	 * 
	 * @param myFormName 	{@link String} = the ID
	 */
	protected void setMyFormName(String myFormName) {
		this.getElement().setId(myFormName);		
		this.myFormName = myFormName;
	}

	/**
	 * Set if the element is required
	 * 
	 * @param required {@link Boolean} = true if the field is required
	 */
	public void setRequired(boolean required) {
		this.isRequired = required;
		if (required==true) myLabel.setText(myLabel.getText().replace("(*)", "") + " (*)");
	}
	
	/**
	 * Set if the element could be duplicated
	 * 
	 * @param multiple {@link Boolean} = true if the field could be duplicated
	 */
	public void setMultiple(boolean multiple) {
		this.isMultiple = multiple;	
		if (isMultiple==true) {
			if (isRequired==true) myLabel.setText(myLabel.getText().replace("(*)", " 1"));
			else myLabel.setText(myLabel.getText() + " 1");
		} else {
			myLabel.setText(myLabel.getText().replace(" 1",""));
		}
	}
	
	/**
	 * Get the value of the ID
	 * 
	 * @return {@link String} = the ID of the element
	 */
	protected String getMyFormName() {
		return myFormName;
	}
	
	/**
	 * Set the number of the current widget. This value increases duplicating the widget.
	 * 
	 * @param n	{@link Integer} = the widget number
	 */
	protected void setLabelCount(Integer n) {
		String oldlabel = myLabel.getText();
		myLabel.setText(oldlabel.replace("1", n.toString()));
		removeGroupButton.setTitle(messages.remove(myLabel.getText()));	
		removeGroupButton.setEnabled(true);
		removeGroupButton.setVisible(true);
		removeGroupButton.getElement().getParentElement().getStyle().setVerticalAlign(VerticalAlign.MIDDLE);
	}

	/**
	 * Clone xml node in the tree
	 * 
	 * @param myId	{@link String} = the ID of the element to clone
	 * 
	 * @return {@link String} = the new name
	 */
	protected String cloneTree(String myId) {
		myFormName = Utilities.cloneTree(myId, this.getElement().getId());
		componentPanel.setOpen(true);
		return myFormName;
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
}