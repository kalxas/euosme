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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;

import eu.europa.ec.jrc.euosme.gwt.client.CIOrientations;
import eu.europa.ec.jrc.euosme.gwt.client.RESTfulWebServiceProxy;
import eu.europa.ec.jrc.euosme.gwt.client.RESTfulWebServiceProxyAsync;
import eu.europa.ec.jrc.euosme.gwt.client.callback.DataThemesCallback;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Messages;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CI;

/**
 * Create MD_Keywords_DataThemes model
 * This class proposes a list with the INSPIRE data themes. 
 * To obtain this list the application calls the RESTful service that gets the topmost concepts using the GemetClient library.
 * 
 * @version 1.0 - November 2010
 * @author 	Marzia Grasso
 */

public class MD_Keywords_DataThemes extends CI {
	
	/** Constants declaration */
 	protected iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Messages declaration */
 	protected iso19115Messages messages = GWT.create(iso19115Messages.class);
	
 	/** List of data themes */
	ListBox listDataThemes = new ListBox();	
	
	/** Button for information about INSPIRE Data Themes */
	Button infoDataThemesButton = new Button();
	PopupPanel dataThemesPopupPanel = new PopupPanel(true,false);
	
	/** 
     * constructor MD_Keywords_DataThemes model
     * 
     * @param label		{@link String} = the header
     * @param required	{@link Boolean} = if true, it is required
     * @param multiple	{@link Boolean} = if true, it could be added more than ones
     * @param help		{@link String} = the anchor in the help 
     * 
     * @return	the widget composed by MD_Keywords_DataThemes fields
     */
	public MD_Keywords_DataThemes(String label, boolean required, boolean multiple, String help) {
		super(label, required, multiple, help, CIOrientations.VERTICAL);
		HorizontalPanel hpDataThemes = new HorizontalPanel();
		hpDataThemes.add(listDataThemes);
		infoDataThemesButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dataThemesPopupPanel.setTitle(constants.info());
				dataThemesPopupPanel.setPixelSize(400,200);
				dataThemesPopupPanel.showRelativeTo(infoDataThemesButton);
				dataThemesPopupPanel.getElement().getStyle().setOverflow(Overflow.AUTO);
			}		
		});
		infoDataThemesButton.addStyleName("infoButton");		
		hpDataThemes.add(infoDataThemesButton);
		fieldsGroup.add(hpDataThemes);
		requestListOfDataThemes(listDataThemes);		
	}
	
	/**
	 * Contact the RPC service to get the list of Data Themes
	 * 
	 * @param myListRPC	{@link ListBox} = list box to populate
	 */
	private void requestListOfDataThemes(ListBox myListRPC) {
		DataThemesCallback callback = new DataThemesCallback();
		DataThemesCallback.setList(myListRPC,dataThemesPopupPanel);
		RESTfulWebServiceProxyAsync ls = RESTfulWebServiceProxy.Util.getInstance();
		ls.invokeGemetService("dataThemes",LocaleInfo.getCurrentLocale().getLocaleName(),callback);
	}
}