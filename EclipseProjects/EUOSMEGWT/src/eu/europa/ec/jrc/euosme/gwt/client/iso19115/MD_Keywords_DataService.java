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
//import com.google.gwt.dom.client.Style.Overflow;
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocaleInfo;
//import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
//import com.google.gwt.user.client.ui.PopupPanel;

import eu.europa.ec.jrc.euosme.gwt.client.CIOrientations;
import eu.europa.ec.jrc.euosme.gwt.client.EUOSMEGWT;
import eu.europa.ec.jrc.euosme.gwt.client.RESTfulWebServiceProxy;
import eu.europa.ec.jrc.euosme.gwt.client.RESTfulWebServiceProxyAsync;
import eu.europa.ec.jrc.euosme.gwt.client.Utilities;
import eu.europa.ec.jrc.euosme.gwt.client.callback.SuggestListCallback;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Messages;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CI;

/**
 * Create MD_Keywords_DataService model
 * This class proposes a list of categories derived from the geographic services taxonomy of EN ISO 19119. 
 * To obtain this list the application calls the RESTful service that gets the topmost concepts using the GemetClient library.
 * 
 * @version 1.0 - February 2011
 * @author 	Marzia Grasso
 */

public class MD_Keywords_DataService extends CI {
	
	/** Constants declaration */
 	protected iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Messages declaration */
 	protected iso19115Messages messages = GWT.create(iso19115Messages.class);
	
 	/** List of data themes */
	ListBox listDataServices = new ListBox();	
	
	/** Button for information about INSPIRE Data Themes */
	//Button infoDataServicesButton = new Button();
	//PopupPanel DataServicesPopupPanel = new PopupPanel(true,false);
	
	/** 
     * constructor MD_Keywords_DataService model
     * 
     * @param label		{@link String} = the header
     * @param required	{@link Boolean} = if true, it is required
     * @param multiple	{@link Boolean} = if true, it could be added more than ones
     * @param help		{@link String} = the anchor in the help 
     * 
     * @return	the widget composed by MD_Keywords_DataServices fields
     */
	public MD_Keywords_DataService(String label, boolean required, boolean multiple, String help) {
		super(label, required, multiple, help, CIOrientations.VERTICAL);
		HorizontalPanel hpDataServices = new HorizontalPanel();
		hpDataServices.add(listDataServices);
		/*infoDataServicesButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				DataServicesPopupPanel.setTitle(constants.info());
				DataServicesPopupPanel.setPixelSize(400,200);
				DataServicesPopupPanel.showRelativeTo(infoDataServicesButton);
				DataServicesPopupPanel.getElement().getStyle().setOverflow(Overflow.AUTO);
			}		
		});
		infoDataServicesButton.addStyleName("infoButton");
		hpDataServices.add(infoDataServicesButton);*/
		fieldsGroup.add(hpDataServices);
		requestListOfDataServices(listDataServices);		
	}
	
	/**
	 * Contact the RPC service to get the list of Data Services
	 * 
	 * @param myListRPC	{@link ListBox} = list box to populate
	 */
	private void requestListOfDataServices(ListBox myListRPC) {
		/*DataThemesCallback callback = new DataThemesCallback();
		DataThemesCallback.setList(myListRPC,DataServicesPopupPanel);
		RESTfulWebServiceProxyAsync ls = RESTfulWebServiceProxy.Util.getInstance();
		ls.invokeGemetService("dataServices",LocaleInfo.getCurrentLocale().getLocaleName(),callback);*/
		if (EUOSMEGWT.rpcRepository) {
			SuggestListCallback callback = new SuggestListCallback();
			callback.setList(myListRPC);
			RESTfulWebServiceProxyAsync ls = RESTfulWebServiceProxy.Util.getInstance();
			ls.invokeGetRESTfulWebService("repository", "http://inspire-registry.jrc.ec.europa.eu/registers/EN_ISO_19119/items", LocaleInfo.getCurrentLocale().getLocaleName(), "", callback);
		}
		else {
			Utilities.setSuggestList(Utilities.getResourceRepository("ISO 19119 geographic services taxonomy"), myListRPC);	
		}
	}
}