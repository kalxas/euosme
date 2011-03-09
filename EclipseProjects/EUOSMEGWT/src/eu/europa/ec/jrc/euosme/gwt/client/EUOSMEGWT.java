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

package eu.europa.ec.jrc.euosme.gwt.client;

import com.google.gwt.ajaxloader.client.AjaxLoader;
import com.google.gwt.ajaxloader.client.AjaxLoader.AjaxLoaderOptions;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocaleInfo;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;

import eu.europa.ec.jrc.euosme.gwt.client.callback.InitRpcCallback;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.ui.MainPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * 
 * @version 5.1 - February 2011
 * @author 	Marzia Grasso
 */
public class EUOSMEGWT implements EntryPoint {
	// User Settings
	/** Default metadata type loaded */ 
	public static String metadataType = DataTypes.DATASET.toString();
	/** Set the profile used, it enable or disable elements to be shown */
	public static String appMode = AppModes.DEFAULT.toString();
	/** true, the application uses the online code lists; false, the application uses the local resources */
	public static boolean rpcCodeList = false;
	/** the type of maps to show: google or openlayers */
	public static String apiMapstraction = "openlayers";
	/** true, all the disclosure panels are opened; false, the disclosure panels are opened only if the content is required */
	public static boolean showAll = false;
	/** true, the application uses the online repositories; false, the application uses the local resources */
	public static boolean rpcRepository = false;	
	/** true, to show the header with the banner; false, to hide it */
	public static boolean showHeader = false;
	
	// The following variables are changed during loading by the application	
	/** Indicates the availability of online service that is connected to the respositories */
	public static boolean repositoryAvailable = true;
	/** Indicates the availability of INSPIRE Data Themes service */
	public static boolean datathemesAvailable = true;
	/** Indicates the availability of codelist service that is connected to the respositories */
	public static boolean codelistAvailable = true;
	/** Indicates the availability of INSPIRE services */
	public static boolean inspireserviceAvailable = true;
	
	/**
	 * This is the entry point method
	 *  
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	public void onModuleLoad() {
		iso19115Constants constants = GWT.create(iso19115Constants.class);
		
		// set loading panel
		HorizontalPanel loadingPanel = new HorizontalPanel();		
		Label messagesLabel = new Label(constants.loading());
		messagesLabel.setStyleName("infoLabel");
		loadingPanel.add(messagesLabel);
		loadingPanel.add(new Image(MyResources.INSTANCE.loadingImg()));
		RootLayoutPanel.get().add(loadingPanel);
		
		// get settings and then load the application, calling the function startup
		InitRpcCallback callback = new InitRpcCallback();
		InitServiceProxyAsync ls = InitServiceProxy.Util.getInstance();
		ls.getInitService(callback);
	}
	
	
	/**
	 * If the configuration settings are properly read, startup the application
	 * by loading the google maps, if requested, and attach MainPanel to the DOM.
	 */
	public static void startup() {
		// Initialize map widget		
		if (apiMapstraction.equalsIgnoreCase("google")) {
			if (GWT.getHostPageBaseURL().toLowerCase().startsWith("http://www.inspire-geoportal.eu")) AjaxLoader.init("ABQIAAAARjXAippIeSRWK4tBInUxmhSD7MB-jf9S6Yyk_R7IzrhbNTTkdBSwBiQI7R1--A7WQNWYfUyJBvrqsg");
			if (GWT.getHostPageBaseURL().toLowerCase().startsWith("http://geoportal.jrc.it")) AjaxLoader.init("ABQIAAAARjXAippIeSRWK4tBInUxmhT_vLas5zTmqrPjIhUgjuA8wUkjCxRgGjptngyINrfoHo-67aphR9weVQ");
			if (GWT.getHostPageBaseURL().toLowerCase().startsWith("http://geoportal.h07.jrc.it")) AjaxLoader.init("ABQIAAAARjXAippIeSRWK4tBInUxmhTQ1X74t6kGZs--HzvSPyHWO7_xzxQc1yGwiKilz6jwyWZG0j-S4lSayQ");
			
			AjaxLoaderOptions currLanguage = AjaxLoader.AjaxLoaderOptions.newInstance();
			currLanguage.setLanguage(LocaleInfo.getCurrentLocale().getLocaleName());
			//TODO in order to use the Maps API, you need to apply for a Google Maps API key. 
			//Running with no key specified will work with localhost for development purposes, 
			//but you will need to apply for your own key to deploy to a website. 
			//AjaxLoader.init("Type your API key here");
			AjaxLoader.loadApi("maps", "2", new Runnable() {
				@Override
				public void run() {
					initPanels();
			    }
			}, currLanguage);	
		}
		else initPanels();		
	}
	
	
	/**
	 * Attach the MainPanel to the default root panel. This panel wraps the body of the browser's document.<p>
	 * Set the default dates to today. 
	 */
	private static void initPanels() {
		MainPanel p = new MainPanel();
  		RootLayoutPanel.get().add(p);
  		Utilities.setDefaultValues();
	}
}