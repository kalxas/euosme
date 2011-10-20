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
 * Authors: Marzia Grasso, Angelo Quaglia, Massimo Craglia, Minh-Tri Duong
LICENSE END***/

package eu.europa.ec.jrc.euosme.gwt.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import eu.europa.ec.jrc.euosme.gwt.client.CIOrientations;
import eu.europa.ec.jrc.euosme.gwt.client.RESTfulWebServiceProxy;
import eu.europa.ec.jrc.euosme.gwt.client.RESTfulWebServiceProxyAsync;
import eu.europa.ec.jrc.euosme.gwt.client.callback.DataThemesCallback;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Messages;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.DateImpl.DateImplUiBinder;

/**
 * Create Metadata of metadata - publish information (where do this metadata publish to?) 
 * 
 * @version 1.0 - October 2011
 * @author 	Minh-Tri Duong
 */

public class MD_MD_PublishInfo extends Composite {
	private static MD_MD_PublishInfoUiBinder uiBinder = GWT.create(MD_MD_PublishInfoUiBinder.class);
	interface MD_MD_PublishInfoUiBinder extends UiBinder<Widget, MD_MD_PublishInfo> {	}	
	
	/** Constants declaration */
 	protected iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Messages declaration */
 	protected iso19115Messages messages = GWT.create(iso19115Messages.class);
 	
	/** grouping fields declaration */
	@UiField(provided = true)
	DisclosurePanel componentPanel = new DisclosurePanel();
 	
 	/** Label of the panel */
	@UiField
 	Label myLabel = new Label(""); 	
	

 	
 	/** Publish organisations */
	@UiField
	public CheckBox ck_rdsi = new CheckBox(constants.rdsi_rdsi());
	@UiField
	public CheckBox ck_inspire = new CheckBox(constants.rdsi_inspire());
	@UiField
	public CheckBox ck_cch = new CheckBox(constants.rdsi_cch());

	
	
	/** Button for information about publish */
	@UiField
	Button infoPublishButton = new Button();
	
	PopupPanel publishHelpPopupPanel = new PopupPanel(true,false);
	
	/** 
     * constructor MD_MD_PublishInfo
     *
     */
	public MD_MD_PublishInfo(String label, String help, boolean required) {
		// Initialize widget
		initWidget(uiBinder.createAndBindUi(this));
		myLabel.setText(label);
		if (required) myLabel.setText(myLabel.getText() + " (*)");	
		
		componentPanel.setOpen(true);

		infoPublishButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				publishHelpPopupPanel.setTitle(constants.info());
				publishHelpPopupPanel.setPixelSize(400,200);
				publishHelpPopupPanel.showRelativeTo(infoPublishButton);
				//publishHelpPopupPanel.getElement().getStyle().setOverflow(Overflow.AUTO);
				publishHelpPopupPanel.setWidget(new HTML(constants.publish_info_help())); 
			}		
		});
		
		componentPanel.addCloseHandler(new CloseHandler<DisclosurePanel>() {
			@Override
			public void onClose(CloseEvent<DisclosurePanel> event) {				
				event.getTarget().setOpen(true);
			}
		});		

	}
	/**
	 * This is called to make field's validation and show an error if it fails
	 */
	public void myCheck() {
		// declare constants
		iso19115Constants constants = GWT.create(iso19115Constants.class);	
	}	
	
	
	/** 
	 * reset all keywords
	 */
	public void reset() {
		ck_rdsi.setValue(false);
		ck_inspire.setValue(false);
		ck_cch.setValue(false);
	}


}