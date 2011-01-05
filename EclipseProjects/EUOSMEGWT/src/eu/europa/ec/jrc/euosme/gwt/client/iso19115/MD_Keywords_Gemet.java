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

package eu.europa.ec.jrc.euosme.gwt.client.iso19115;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

import eu.europa.ec.jrc.euosme.gwt.client.RESTfulWebServiceProxy;
import eu.europa.ec.jrc.euosme.gwt.client.RESTfulWebServiceProxyAsync;
import eu.europa.ec.jrc.euosme.gwt.client.MyResources;
import eu.europa.ec.jrc.euosme.gwt.client.callback.SuggestCallback;
import eu.europa.ec.jrc.euosme.gwt.client.callback.SuggestListCallback;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Messages;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CI;

/**
 * Create MD_Keywords_Gemet model
 * This class lets the user to choose a keyword from all the available repository of GEMET
 * 
 * @version 1.0 - November 2010
 * @author 	Marzia Grasso
 */
public class MD_Keywords_Gemet extends CI {
	
	/** Constants declaration */
 	protected iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Messages declaration */
 	protected iso19115Messages messages = GWT.create(iso19115Messages.class);
	
 	/** List of available repositories */
	ListBox listRepository = new ListBox();
	
	/** suggest control declaration */
	Tree suggestObj = new Tree(); 	
	Tree suggestSubObj = new Tree();
	
	/** panel used to add a keyword */
	HorizontalPanel GEMETPanel = new HorizontalPanel();
	public Label keywordGEMETObj = new Label();
	
	/** Filter */
	HorizontalPanel filterPanel = new HorizontalPanel();
	
	/** 
     * constructor MD_Keywords_Gemet model
     * 
     * @param label		{@link String} = the header
     * @param required	{@link Boolean} = if true, it is required
     * @param multiple	{@link Boolean} = if true, it could be added more than ones
     * 
     * @return	the widget composed by MD_Keywords_Gemet fields
     */
	public MD_Keywords_Gemet(String label, boolean required, boolean multiple) {
		super(label, required, multiple);
		// Filter
		final TextBox filterTextBox = new TextBox();
		Button filterButton = new Button("Filter");
		final Button filterClearButton = new Button("Clear");
		fieldsGroup.add(listRepository);
		listRepository.addChangeHandler(new ChangeHandler(){
			@Override
			public void onChange(ChangeEvent event) {
				if (listRepository.getSelectedIndex()!=0) {
					String selectedValue = listRepository.getValue(listRepository.getSelectedIndex()).trim();
					if (selectedValue.isEmpty()) {
						suggestObj.setVisible(false);
						filterClearButton.setEnabled(false);
						filterPanel.setVisible(false);							
					}
					else requestSuggestions(selectedValue,"");						
				}				
			}
		});
		filterPanel.add(filterTextBox);
		filterButton.addClickHandler(new ClickHandler()  {
			@Override
			public void onClick(ClickEvent event) {
				String selectedValue = listRepository.getValue(listRepository.getSelectedIndex()).trim();
				if (selectedValue.isEmpty()) {
					suggestObj.setVisible(false);
					filterClearButton.setEnabled(false);
					filterPanel.setVisible(false);
				}
				else {
					requestSuggestions(selectedValue,filterTextBox.getText());
					filterClearButton.setEnabled(true);
				}
			}
		});		
		filterClearButton.addClickHandler(new ClickHandler()  {
			@Override
			public void onClick(ClickEvent event) {
				String selectedValue = listRepository.getValue(listRepository.getSelectedIndex()).trim();
				requestSuggestions(selectedValue,"");
				filterClearButton.setEnabled(false);	
				filterTextBox.setText("");
			}
		});		
		filterClearButton.setEnabled(false);
		filterPanel.add(filterButton);
		filterPanel.add(filterClearButton);
		fieldsGroup.add(filterPanel);
		filterPanel.setVisible(false);
		// Suggest box
		fieldsGroup.add(suggestObj);
		suggestObj.getElement().getStyle().setHeight(100,Unit.PX);
		suggestObj.getElement().getStyle().setOverflow(Overflow.AUTO);
		suggestObj.setVisible(false);
		suggestObj.addOpenHandler(new OpenHandler<TreeItem>() {
			@Override
			public void onOpen(OpenEvent<TreeItem> event) {
				TreeItem w = event.getTarget();
				if (w.getChildCount()==1 && w.getParentItem()!=null) {
					w.removeItems();
					w.addItem(new Image(MyResources.INSTANCE.loadingImg()));
					w.getChild(0).setTitle(constants.loading());
					suggestObj.ensureSelectedItemVisible();
					requestSubSuggestions(w);
				}								
			}		
		});
		suggestObj.addSelectionHandler(new SelectionHandler<TreeItem>()  {
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				if(event.getSelectedItem().getParentItem()!=null) {
					GEMETPanel.setVisible(true);
					keywordGEMETObj.setText(constants.selectedValue() + event.getSelectedItem().getText())	;					
				}
			}			
		});
		// GEMET Panel
		keywordGEMETObj.getElement().getStyle().clearMarginLeft();
		keywordGEMETObj.setText(constants.selectedValue());
		GEMETPanel.add(keywordGEMETObj);
		GEMETPanel.setVisible(false);			
		fieldsGroup.add(GEMETPanel);
		// RPC call
		requestListOfRepository(listRepository);
	}
	
	/**
	 * Get a list of definitions for the given repository
	 * 
	 * @param repository	{@link String} = the repository
	 * @param filter		{@link String} = the string to use as a filter
	 */
	private void requestSuggestions(String repository, String filter) {
		if(suggestObj.getItemCount()!=0)
			if(suggestObj.getItem(0).getChildCount()!=0)
				if (suggestObj.getItem(0).getChild(0).getTitle().equalsIgnoreCase(constants.loading())) return;
	
		suggestObj.clear(); 
		suggestObj.addItem(constants.keyword());
		suggestObj.getItem(0).addItem(new Image(MyResources.INSTANCE.loadingImg()));
		suggestObj.getItem(0).getChild(0).setTitle(constants.loading());
		suggestObj.getItem(0).setState(true);
		suggestObj.setVisible(true);		
		filterPanel.setVisible(true);	
		SuggestCallback callback = new SuggestCallback();
		callback.setList(suggestObj.getItem(0));
		RESTfulWebServiceProxyAsync ls = RESTfulWebServiceProxy.Util.getInstance();
		ls.invokeGetRESTfulWebService("repository", repository, LocaleInfo.getCurrentLocale().getLocaleName(), filter, callback);		
    }
	
	/**
	 * Request the list of narrower starting with the given keyword
	 * 
	 * @param myTreeItem	{@link TreeItem} = the starting keyword
	 */
	private void requestSubSuggestions(TreeItem myTreeItem) {		
		SuggestCallback callback = new SuggestCallback();
		callback.setList(myTreeItem);
		RESTfulWebServiceProxyAsync ls = RESTfulWebServiceProxy.Util.getInstance();
		ls.invokeGetRESTfulWebService("narrower", myTreeItem.getTitle(), LocaleInfo.getCurrentLocale().getLocaleName(), "", callback);         
    }
	
	/**
	 * Request the list of available repositories
	 * 
	 * @param myListRPC	{@link ListBox} = the list box to populate with the list
	 */
	private void requestListOfRepository(ListBox myListRPC) {
		SuggestListCallback callback = new SuggestListCallback();
		SuggestListCallback.setList(myListRPC);
		RESTfulWebServiceProxyAsync ls = RESTfulWebServiceProxy.Util.getInstance();
		ls.invokeGetRESTfulWebService("repositories", "", LocaleInfo.getCurrentLocale().getLocaleName(), "", callback);
	}
}