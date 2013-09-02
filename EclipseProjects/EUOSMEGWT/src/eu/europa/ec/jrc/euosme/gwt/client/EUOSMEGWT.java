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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.ajaxloader.client.AjaxLoader;
import com.google.gwt.ajaxloader.client.AjaxLoader.AjaxLoaderOptions;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.i18n.client.LocaleInfo;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;

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
	/** true, the application uses the online repositories; false, the application uses the local resources */
	public static String wmsEndpoint = "";	
	
	// The following variables are changed during loading by the application
	/** Indicate the app is ready to load */
	public static boolean readyToLoad = false;
	/** Indicates the availability of online service that is connected to the respositories */
	public static boolean repositoryAvailable = true;
	/** Indicates the availability of INSPIRE Data Themes service */
	public static boolean datathemesAvailable = true;
	/** Indicates the availability of codelist service that is connected to the respositories */
	public static boolean codelistAvailable = true;
	/** Indicates the availability of INSPIRE services */
	public static boolean inspireserviceAvailable = true;
	
	/** The filename of metadata file */
	public static String fileName = "#FILENAME#";
	
	/** The  RDSI keywords */	
	public static ArrayList<String> rdsi_keyword = new ArrayList<String>();
	
	/** The publication date of GEMET schemes */
	public static Map<String,String> gemetPublicationDate = new LinkedHashMap<String, String>();
	
	
	// The Main Panel 
	public static MainPanel mainPanel;
	//public static HorizontalPanel loadingPanel;
	
	/**
	 * This is the entry point method
	 *  
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	public void onModuleLoad() {
		iso19115Constants constants = GWT.create(iso19115Constants.class);
		
		//chooseResourceType();
		
		// set loading panel
		//if (readyToLoad) {
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
		//}
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
				//but you will need to apply for your own key to deploy to a website. -
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
		mainPanel = new MainPanel();
  		RootLayoutPanel.get().add(mainPanel);
  		Utilities.setDefaultValues(true);
  		
	}
	
	/** 
	 *  Choose resource type
	 */
	public static void chooseResourceType() {
		final iso19115Constants constants = GWT.create(iso19115Constants.class);
		// Create a dialog box
		final DialogBox resourceDialog = new DialogBox();
		resourceDialog.setGlassEnabled(true);
		
		final FormPanel chooseTypeForm = new FormPanel();			
		chooseTypeForm.setMethod(FormPanel.METHOD_POST);
		
		// Create a panel to hold all of the form widgets.
		final VerticalPanel panel = new VerticalPanel();
	
		final RadioButton rb0 = new RadioButton("resourceType", constants.newSpatialDataset());
		final RadioButton rb1 = new RadioButton("resourceType", constants.newSpatialDatasetSeries());		
		final RadioButton rb2 = new RadioButton("resourceType", constants.newSpatialDataService());
	    rb0.setValue(true);	    
	    panel.add(rb0);
	    panel.add(rb1);
	    //panel.add(rb2);	
	    
	    final HorizontalPanel myHPanel = new HorizontalPanel();		    
		
		Button okButton = new Button(constants.okButton(), new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {	
				//Document.get().getBody().getStyle().setCursor(Style.Cursor.WAIT);
				resourceDialog.hide();	
				if(rb1.getValue()) {
					metadataType = DataTypes.DATASET_SERIES.toString();
					if (mainPanel != null) {
						mainPanel.newForm();
					}
				}
				else if(rb2.getValue()) {
					metadataType = DataTypes.DATA_SERVICE.toString();
					if (mainPanel != null) {			
						mainPanel.newForm();
					}
				}
				readyToLoad = true;
							
			}
		});		    
		
		
		myHPanel.add(okButton);
		panel.add(myHPanel);			

		// Add the panel to the form
		chooseTypeForm.add(panel);		
				
		// Add the form to the dialog box 	
		resourceDialog.add(chooseTypeForm);		
		resourceDialog.setHTML("<b>" + constants.resourceType() + "</b><br/>");
		resourceDialog.setModal(true);
		resourceDialog.center();		
	} 		

}