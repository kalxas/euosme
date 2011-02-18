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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import eu.europa.ec.jrc.euosme.gwt.client.EUOSMEGWT;
import eu.europa.ec.jrc.euosme.gwt.client.InfoButton;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Messages;
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.CI_Date;
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.CI_OnlineResource;
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.CI_ResponsibleParty;
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.DQ_Element;
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.Distance;
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.EX_GeographicBoundingBox;
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.MD_Identifier;
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.MD_Keywords;
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.MD_Resolution;
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.TM_Primitive;

/**
 * Create CIMultiple model
 * 
 * @version 4.0 - February 2011
 * @author 	Marzia Grasso
 */
public class CIMultiple extends Composite {
	@UiTemplate("CImultiple.ui.xml")
 	interface CIUiBinder extends UiBinder<Widget, CIMultiple> {}
	private static CIUiBinder CIUiBinder = GWT.create(CIUiBinder.class);
	
	/** Global variable used to check if the field is empty or not */ 
	public boolean isRequired=false;
	
	/** add button declaration */
	@UiField(provided = true)
	public
	Button addButton = new Button();
	
	/** Constants declaration */
	protected iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Messages declaration */
	protected iso19115Messages messages = GWT.create(iso19115Messages.class);
	
	/** grouping fields declaration */
	@UiField(provided = true)
	DisclosurePanel componentPanel = new DisclosurePanel();	
	
	/** title declaration */
	@UiField(provided = true)
	Label myLabel = new Label();
	
	/** grouping fields declaration */
	@UiField(provided = true)
	VerticalPanel fieldsGroup = new VerticalPanel();
	
	/** Button for information */
	@UiField
	InfoButton infoButton = new InfoButton();
	
	/** vertical panel */
	@UiField(provided = true)
	protected
	VerticalPanel myPanel = new VerticalPanel();
	
	/** the widget */
	Composite myWidget=new Composite() {};
	
	/** The label used for the group */ 
	private String groupLabel="";
	
	/** Global variable used to move into the user guide as an anchor */
	private String helpAnchor="";
	
	/** 
     * constructor CIMultiple model
     *     
	 * @param label		{@link String} = the header
	 * @param myWidget	{@link Composite} = the widget to duplicate
	 * @param required	{@link Boolean} = if true at least one is mandatory
	 * @param help		{@link String} = the anchor in the help 
	 */
	public CIMultiple(String label, Composite myWidget, boolean required, String help) {
		isRequired = required;
		groupLabel = label;
		helpAnchor = help;
		
		// Add * for mandatory fields
		setLabel(label);
		
		// Show the fields if required		
		fieldsGroup.add(myWidget);
		this.myWidget = myWidget;
		
		// Initialize widget
		initWidget(CIUiBinder.createAndBindUi(this));
		
		// set add button
		addButton.setTitle(messages.add(label));		
		addButton.addStyleName("plusButton");
		
		if (EUOSMEGWT.showAll) setDisplay(true);
		else setDisplay(required);
		
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
	 * Set the ID used to identify the element or group of elements in the XML.
	 * It has a corresponding TreeItem with the same title.
	 * 
	 * @param name 	{@link String} = the name of the field in the form (ID or path)
	 */
	public void setFormName(String name) {
		this.addButton.getElement().setId("add-" + name);
	}
	
	/**
	 * Set the mandatory value
	 * 
	 * @param required 	{@link Boolean} = true if the field is required
	 */
	public void setRequired(boolean required) {
		this.isRequired = required;
		if (required==true) myLabel.setText(myLabel.getText().replace("(*)", "") + " (*)");
		if (EUOSMEGWT.showAll) setDisplay(true);
		else setDisplay(required);
	}
	
	/**
	 * Open the disclosure panel and show all the fields
	 * 
	 * @param visible 	{@link Boolean} = true to open disclosure panel and show fields group
	 */
	public void setDisplay(boolean visible) {
		componentPanel.setOpen(visible);
	}
	
	/**
     * This is called whenever a user click the add component or show/hide button.
     * It duplicates the widget in the DOM and clone the {@link TreeItem}
	 * 
     * @see onClick
     */
	@UiHandler("addButton")
	void handleonClick(ClickEvent event) {		
		if (fieldsGroup.isVisible()==false) { 
			fieldsGroup.setVisible(true);
		} else {
			// get formname of the object to "recalculate" the new id
			int i = myWidget.getClass().getName().lastIndexOf(".");
			String myClass = myWidget.getClass().getName().substring(i+1);	
			String myFormName = myWidget.getElement().getId();
			int j = myFormName.lastIndexOf("[1]");
			int x = myFormName.substring(0,j).lastIndexOf("[1]");
			Integer n = fieldsGroup.getWidgetCount()+1;
			myFormName = myFormName.substring(0,j).substring(0,x) + "[" + n.toString() + "]" + myFormName.substring(x+"[1]".length());			
			// create new widget
			Widget newObj = null;
			if (myClass.equalsIgnoreCase("DQ_Element")) {
		    	newObj = new DQ_Element(groupLabel,isRequired, true,"");
		    	((DQ_Element) newObj).setFormName(myFormName);
		    	((DQ_Element) newObj).setLabelCount(n);
		    	((DQ_Element) newObj).cloneTree(myWidget.getElement().getId());	    	
		    }
			if (myClass.equalsIgnoreCase("EX_GeographicBoundingBox")) {
		    	newObj = new EX_GeographicBoundingBox(groupLabel,isRequired, true,"");
		    	((EX_GeographicBoundingBox) newObj).setFormName(myFormName);
		    	((EX_GeographicBoundingBox) newObj).setLabelCount(n);
		    	((EX_GeographicBoundingBox) newObj).cloneTree(myWidget.getElement().getId());		    	
		    }
			if (myClass.equalsIgnoreCase("md_resolution")) {
		    	newObj = new MD_Resolution(groupLabel,isRequired, true,"");
		    	((MD_Resolution) newObj).setFormName(myFormName);
		    	((MD_Resolution) newObj).setLabelCount(n);
		    	((MD_Resolution) newObj).cloneTree(myWidget.getElement().getId());		    	
		    }
			if (myClass.equalsIgnoreCase("distance")) {
		    	newObj = new Distance(groupLabel,isRequired, true,"");
		    	((Distance) newObj).setFormName(myFormName);
		    	((Distance) newObj).setLabelCount(n);
		    	((Distance) newObj).cloneTree(myWidget.getElement().getId());		    	
		    }
			if (myClass.equalsIgnoreCase("ci_date")) {
		    	newObj = new CI_Date(groupLabel,isRequired, true,"");
		    	((CI_Date) newObj).setFormName(myFormName);
		    	((CI_Date) newObj).setLabelCount(n);
		    	((CI_Date) newObj).cloneTree(myWidget.getElement().getId());		    	
		    }
			if (myClass.equalsIgnoreCase("tm_primitive")) {
		    	newObj = new TM_Primitive(groupLabel,isRequired, true,"");
		    	((TM_Primitive) newObj).setFormName(myFormName);
		    	((TM_Primitive) newObj).setLabelCount(n);
		    	((TM_Primitive) newObj).cloneTree(myWidget.getElement().getId());		    	
		    }
			if (myClass.equalsIgnoreCase("md_keywords")) {
		    	newObj = new MD_Keywords(groupLabel,isRequired, true,"");
		    	((MD_Keywords) newObj).setFormName(myFormName);
		    	((MD_Keywords) newObj).setLabelCount(n);
		    	((MD_Keywords) newObj).cloneTree(myWidget.getElement().getId());		    	
		    }
		    if (myClass.equalsIgnoreCase("ci_onlineresource")) {
		    	newObj = new CI_OnlineResource(groupLabel,isRequired, true,"");
		    	((CI_OnlineResource) newObj).setFormName(myFormName);
		    	((CI_OnlineResource) newObj).setLabelCount(n);
		    	((CI_OnlineResource) newObj).cloneTree(myWidget.getElement().getId());		    	
		    }
		    if (myClass.equalsIgnoreCase("md_identifier")) {
		    	newObj = new MD_Identifier(groupLabel,isRequired, true,"");
		    	((MD_Identifier) newObj).setFormName(myFormName);
		    	((MD_Identifier) newObj).setLabelCount(n);
		    	((MD_Identifier) newObj).cloneTree(myWidget.getElement().getId());		    	
		    }
		    if (myClass.equalsIgnoreCase("ci_responsibleparty")) {
		    	newObj = new CI_ResponsibleParty(groupLabel,isRequired, true,"");	
		    	((CI_ResponsibleParty) newObj).setFormName(myFormName);
		    	((CI_ResponsibleParty) newObj).setLabelCount(n);
		    	((CI_ResponsibleParty) newObj).cloneTree(myWidget.getElement().getId());
		    	if (groupLabel.contains(constants.pointOfContact())) ((CI_ResponsibleParty) newObj).setInterface(0);	
		    	else ((CI_ResponsibleParty) newObj).setInterface(1);
		    }	
			fieldsGroup.add(newObj);
			setDisplay(true);
		}		
	}
	
	/**
	 * Hide the disclosure panel to make more simple the interface 
	 */
	public void removeDisclosure() {
		componentPanel.removeFromParent();
		myPanel.add(fieldsGroup);
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