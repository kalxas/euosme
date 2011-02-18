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

package eu.europa.ec.jrc.euosme.gwt.client.iso19115.ui;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;

import eu.europa.ec.jrc.euosme.gwt.client.AppModes;
import eu.europa.ec.jrc.euosme.gwt.client.DataTypes;
import eu.europa.ec.jrc.euosme.gwt.client.EUOSMEGWT;
import eu.europa.ec.jrc.euosme.gwt.client.RESTfulWebServiceProxy;
import eu.europa.ec.jrc.euosme.gwt.client.RESTfulWebServiceProxyAsync;
import eu.europa.ec.jrc.euosme.gwt.client.MyResources;
import eu.europa.ec.jrc.euosme.gwt.client.Utilities;
import eu.europa.ec.jrc.euosme.gwt.client.callback.InspireServiceRpcCallback;
import eu.europa.ec.jrc.euosme.gwt.client.callback.ValidationRpcCallback;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;

/**
 * Create the main panel
 * 
 * @version 5.0 - February 2011
 * @author 	Marzia Grasso
 */
public class MainPanel extends Composite {
	
	private static MainPanelUiBinder uiBinder = GWT.create(MainPanelUiBinder.class);
	interface MainPanelUiBinder extends UiBinder<Widget, MainPanel> {}
	
	/** Constants declaration */
	private iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Global variable used to define IdentificationInfo type: md_dataidentification for dataset and dataset series and SV_ServiceIdentification for service */
	public static String identificationInfoSubType = "md_dataidentification";
		
	/** user guide declaration (used to get contextual help tips) */
	public static HTML userGuideHTM;
	
	/** tree declaration */
	@UiField(provided = true)
	public static Tree myTree = new Tree();
	
	/** HTML version of the metadata */
	@UiField
	public static Frame myHTML;
		
	/** menu declaration */
	@UiField(provided = true)
	MenuBar menuBar = new MenuBar();
	
	/** main form declaration */
	@UiField(provided = true)
	FormPanel myForm = new FormPanel();
	
	/** tabs */
	@UiField
	Tabs tabs;
	
	/** header */
	@UiField
	HTML myHeader;
	
	/** DockLayoutPanel which contains all */
	@UiField
	DockLayoutPanel lp;
	
	/** SplitLayoutPanel which contains form and tree */
	@UiField
	SplitLayoutPanel slp;
		
	@UiField
	Button refreshHTML = new Button();
	
	/** 
    * constructor main panel
    * 
    * @return	the widget composed by the main panel (tabs and toolbar)
    */
	public MainPanel() {
		initWidget(uiBinder.createAndBindUi(this));
		
		tabs.getElement().getParentElement().setAttribute("class","prettyHeight");
		
		// Set cursor to hourglass
		Document.get().getBody().getStyle().setCursor(Style.Cursor.WAIT);
		
		// Set user guide
		String helpHTM = MyResources.INSTANCE.help().getText();
		helpHTM = helpHTM.substring(helpHTM.indexOf("<body>")+"<body>".length(),helpHTM.indexOf("</body>"));
		userGuideHTM = new HTML(helpHTM);													
				    
		/** FORM ----------------------------------------------------------------*/
		// Set the main form: this is used to group all the html fields
		myForm.setAction("loadService");
		myForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		myForm.setMethod(FormPanel.METHOD_POST);
		
		/** MENU ----------------------------------------------------------------*/
		// Menu NEW
		MenuBar newMenu = new MenuBar(true);        
        Command cmd2 = new Command() {
            public void execute() {
            	newForm(DataTypes.DATASET.toString(),"");  
            }
        };
        newMenu.addItem(constants.newSpatialDataset(),cmd2);
        Command cmd2a = new Command() {
            public void execute() {
            	newForm(DataTypes.DATASET_SERIES.toString(),"");  
            }
        };
        newMenu.addItem(constants.newSpatialDatasetSeries(),cmd2a);
        Command cmd2b = new Command() {
            public void execute() {
            	newForm(DataTypes.DATA_SERVICE.toString(),"");  
            }
        };
        newMenu.addItem(constants.newSpatialDataService(),cmd2b);
        menuBar.addItem(constants.newFile(), newMenu);
        
        // Menu LOAD
        Command cmdLoadFile = new Command() {
            public void execute() {
            	loadFile();  
            }
        };
        menuBar.addItem(constants.load(), cmdLoadFile);
        
        // Menu VALIDATE
        Command cmdValidateFile = new Command() {
            public void execute() {
            	validateForm();				
            }
        };
        menuBar.addItem(constants.validate(), cmdValidateFile);
        
        // Menu SAVE
        Command cmdSaveFile = new Command() {
            public void execute() {
            	saveFile(".xml");            	
            }			
        };
        menuBar.addItem(constants.save(),cmdSaveFile);
        
        // Menu SAVE AS TEMPLATE
        Command cmdSaveTemplate = new Command() {
            public void execute() {
            	saveFile(".xmlt");            	
            }			
        };
        menuBar.addItem(constants.saveAsTemplate(),cmdSaveTemplate);
        
        // Menu Help
        MenuBar helpMenu = new MenuBar(true);
        // EUR-LEX guidelines
        Command cmdHelpRegulation = new Command() {
            public void execute() {     
            	String helpURL = GWT.getHostPageBaseURL() + "userguide/eurlex_" + LocaleInfo.getCurrentLocale().getLocaleName() + ".htm";
            	Window.open(helpURL, constants.inspireGuidelines(),"scrollbars=yes,resizable=yes,location=no,toolbar=no,menubar=no,height=300,width=550");
            }			
        };
        helpMenu.addItem(constants.regulationTitle(), cmdHelpRegulation);
        // User Guide
        Command cmdHelpUserGuide = new Command() {
            public void execute() {     
            	Window.open("http://www.eurogeoss.eu/Documents/EuroGEOSS_D_2_2_3.pdf", constants.helpUserGuide(),"scrollbars=yes,resizable=yes,location=yes,toolbar=no,menubar=no,height=300,width=550");
            }			
        };
        helpMenu.addItem(constants.helpUserGuide(), cmdHelpUserGuide);
        // Developer Guide
        Command cmdHelpDeveloperGuide = new Command() {
            public void execute() {     
            	Window.open("http://www.eurogeoss.eu/Documents/EuroGEOSS_D_2_2_3A.pdf ", constants.helpDeveloperGuide(),"scrollbars=yes,resizable=yes,location=yes,toolbar=no,menubar=no,height=300,width=550");
            }			
        };
        helpMenu.addItem(constants.helpDeveloperGuide(), cmdHelpDeveloperGuide);
        menuBar.addItem(constants.help(),helpMenu);
                
        /*Command cmdUpdate = new Command() {
            public void execute() {     
            	CodeListRpcCallback callback = new CodeListRpcCallback();
        		RESTfulWebServiceProxyAsync ls = RESTfulWebServiceProxy.Util.getInstance();
        		ls.invokeCacheRepositoryRESTfulWebService("GEMET_Concepts","http://www.eionet.europa.eu/gemet/concept/", callback);
        		ls.invokeCacheRepositoryRESTfulWebService("GEMET_Groups","http://www.eionet.europa.eu/gemet/group/", callback);
        		ls.invokeCacheRepositoryRESTfulWebService("GEMET_Themes","http://www.eionet.europa.eu/gemet/theme/", callback);
        		ls.invokeCacheRepositoryRESTfulWebService("GEOSS_Societal_Benefit_Areas","http://iaaa.unizar.es/thesaurus/SBA_EuroGEOSS", callback);
        		ls.invokeCacheRepositoryRESTfulWebService("INSPIRE_Feature_Concept_Dictionary","http://inspire-registry.jrc.ec.europa.eu/registers/FCD/items", callback);
        		ls.invokeCacheRepositoryRESTfulWebService("INSPIRE_Glossary","http://inspire-registry.jrc.ec.europa.eu/registers/GLOSSARY/items", callback);
        		ls.invokeCacheRepositoryRESTfulWebService("ISO_19119_geographic_services_taxonomy","http://inspire-registry.jrc.ec.europa.eu/registers/EN_ISO_19119/items", callback);
            }			
        };
        menuBar.addItem("cache", cmdUpdate);*/
        
        /** EVENTS management ------------------------------------------------------------*/
        // event.ONCLICK is used to add elements during load file action
        sinkEvents(Event.ONCLICK);
        
        /** LANGUAGE management ------------------------------------------------------------*/
        // Set the correct URL to change language and set the right class for the selected language
		setHeader();
		
        /** TREE initialization ----------------------------------------------------------*/
		initTree("");
					
		// add scroll bars
		slp.getWidgetContainerElement(slp.getWidget(0)).addClassName("auto");
		slp.getWidgetContainerElement(slp.getWidget(0)).getStyle().clearOverflow();
		
		// HTML preview
		refreshHTML.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//get XML tree structure
				String myXMLTree = getXMLTree();
				// call service via RPC
				InspireServiceRpcCallback callback = new InspireServiceRpcCallback();
				InspireServiceRpcCallback.setType("HTML");
				RESTfulWebServiceProxyAsync ls = RESTfulWebServiceProxy.Util.getInstance();
				// get File Name
				String myFileName = getFileName();
				if (myFileName.toLowerCase().endsWith(".xml")) myFileName = myFileName.replace(".xml",".htm");
				else if (myFileName.toLowerCase().endsWith(".xmlt")) myFileName = myFileName.replace(".xmlt",".htm");
				else myFileName += ".htm";
				// invoke the service 
				ls.invokeInspireMetadataConverterService(myXMLTree,LocaleInfo.getCurrentLocale().getLocaleName(),myFileName,callback);			
			}			
		});
		refreshHTML.setHTML(constants.refresh());
		refreshHTML.click();
    }
	
	private void setHeader() {
		String myURL = Utilities.getURL();
		String myHeaderHTML = myHeader.getHTML();
		myHeaderHTML = myHeaderHTML.replace("#template_page#", myURL);
		if (EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.RDSI.toString()))
			myHeaderHTML = myHeaderHTML.replace("#root_page#", "<a id=\"firstTab\" href=\"http://rdsi-portal.jrc.it/rdsi/\">IES Reference Data &amp; Services Initiative</a>"); 		
		else
			myHeaderHTML = myHeaderHTML.replace("#root_page#", "");
		if (EUOSMEGWT.metadataType.equalsIgnoreCase(DataTypes.DATASET.toString())) 
			myHeaderHTML = myHeaderHTML.replace("#datatype#", constants.newSpatialDataset());
		if (EUOSMEGWT.metadataType.equalsIgnoreCase(DataTypes.DATASET_SERIES.toString()))
			myHeaderHTML = myHeaderHTML.replace("#datatype#", constants.newSpatialDatasetSeries());
		if (EUOSMEGWT.metadataType.equalsIgnoreCase(DataTypes.DATA_SERVICE.toString()))
			myHeaderHTML = myHeaderHTML.replace("#datatype#", constants.newSpatialDataService());
        myHeader.setHTML(myHeaderHTML);
		NodeList<Element> myList = myHeader.getElement().getElementsByTagName("a");
		for (int i=0; i<myList.getLength(); i++) {
			if (myList.getItem(i).getClassName().equalsIgnoreCase("curlang") && !myList.getItem(i).getAttribute("lang").equalsIgnoreCase(LocaleInfo.getCurrentLocale().getLocaleName()))
				myList.getItem(i).setClassName("lang");
			if (myList.getItem(i).getClassName().equalsIgnoreCase("lang") && myList.getItem(i).getAttribute("lang").equalsIgnoreCase(LocaleInfo.getCurrentLocale().getLocaleName()))
				myList.getItem(i).setClassName("curlang");			
		}		
	}

	/**
	 * MenuBar - open an item in IE causes an error, this function is a workaround 
	 * 
	 * @param event	{@link Event}
	 */
	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		// WORKAROUND for issue 4532: Whole UI disappear in IE 7 when we Hover over the menubar menu item.
		// http://code.google.com/p/google-web-toolkit/issues/detail?id=4532
		if(Utilities.getUserAgent().contains("msie"))	{
			String rootWidth = RootLayoutPanel.get().getElement().getStyle().getWidth();
			RootLayoutPanel.get().setWidth(rootWidth.equals("100%") ? "" : "100%");
		}
	}
	
	/**
	 * Initialize the {@link Tree} passing an XML file
	 * 
	 * @param loadFileXML	{@link String} = the XML file to parse
	 */
	private void initTree(final String loadFileXML) {
		// Turn hourglass off
		Document.get().getBody().getStyle().setCursor(Style.Cursor.WAIT);
		MainPanel.myTree.removeItems();
		
		// Get XML
		if (EUOSMEGWT.metadataType.equalsIgnoreCase(DataTypes.DATASET_SERIES.toString())) Utilities.parseMessage(MyResources.INSTANCE.seriesXML().getText(),true);
		else if (EUOSMEGWT.metadataType.equalsIgnoreCase(DataTypes.DATASET.toString())) Utilities.parseMessage(MyResources.INSTANCE.datasetXML().getText(),true);					
		else Utilities.parseMessage(MyResources.INSTANCE.serviceXML().getText(),true);
		if (!loadFileXML.isEmpty()) Utilities.parseMessage(loadFileXML,false);
		
		// Open default tab and load user guide
		tabs.selectTab(0);
		// Turn hourglass off		
		Document.get().getBody().getStyle().setCursor(Style.Cursor.DEFAULT);
	}
	
	/**
    * This is called whenever the user clicks on validate button
    */
	private void validateForm() {
		tabs.myCheck();
		//get XML tree structure
		String myXMLTree = getXMLTree();
		// call validation service via RPC
		AsyncCallback<String> callback = new ValidationRpcCallback();
		RESTfulWebServiceProxyAsync ls = RESTfulWebServiceProxy.Util.getInstance();
		ls.invokeValidationService(myXMLTree, callback);
	}
	
	/**
    * This is called to create a new file
	* 
	* @param myNewInterface	{@link String} = the metadata type to load 
	* @param loadFileXML 	{@link String} = the XML file to parse 
    */
	private void newForm(String myNewInterface, String loadFileXML) {
		Element mapDiv = DOM.getElementById("mapstraction");
		Document.get().getBody().appendChild(mapDiv);
		Document.get().getBody().getStyle().setCursor(Style.Cursor.WAIT);
		EUOSMEGWT.metadataType = myNewInterface;
		identificationInfoSubType = "md_dataidentification";
		if (EUOSMEGWT.metadataType.equalsIgnoreCase(DataTypes.DATA_SERVICE.toString())) identificationInfoSubType = "sv_serviceidentification";
		tabs.removeFromParent();
		Tabs t = new Tabs();		
		tabs = t;
		this.myForm.add(tabs);
		setHeader();
		initTree(loadFileXML);
		Utilities.setDefaultValues();
	}
	
	/**
    * RPC save file
    */
	private void saveFile(String myExtension) {
		// set filename
		String tmpFileName = getFileName();
		if (tmpFileName.toLowerCase().endsWith(".xml") && myExtension.equalsIgnoreCase(".xmlt")) 
			tmpFileName = tmpFileName.replace(".xml",".xmlt");
		else if (tmpFileName.toLowerCase().endsWith(".xmlt") && myExtension.equalsIgnoreCase(".xml"))
			tmpFileName = tmpFileName.replace(".xmlt",".xml");
		else if (!(tmpFileName.toLowerCase().endsWith(".xml") || tmpFileName.toLowerCase().endsWith(".xmlt")))
			tmpFileName += myExtension;
		final String myFileName = tmpFileName;
		
		//get XML tree structure
		String myXMLTree = getXMLTree();
		
		// Show a dialog box when saving
		final DialogBox myUploadDialog = new DialogBox();
		myUploadDialog.setGlassEnabled(true);
		final VerticalPanel panel = new VerticalPanel();	
		final VerticalPanel vpanel = new VerticalPanel();
		final TextArea xmlTree = new TextArea();
		xmlTree.setText(myXMLTree);
		xmlTree.setName("xmltree");
		panel.add(xmlTree);
		final TextBox fileName = new TextBox();
		fileName.setText(myFileName);
		fileName.setName("filename");
		panel.add(fileName);
		final FormPanel myForm = new FormPanel();
		myForm.setAction("downloadService");
		myForm.setMethod(FormPanel.METHOD_POST);
		final Label loadingLabel = new Label(constants.savingFile());
		final Button openFileButton = new Button(constants.openFileButton(), new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				Window.open("temp/" + myFileName, "new file","");
				myUploadDialog.hide();	
			}
		});
		openFileButton.setVisible(false);
		final Label infoLabel= new Label(constants.infoOnDownload());
		infoLabel.setVisible(false);
		myForm.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				loadingLabel.removeFromParent();
				openFileButton.setVisible(true);
				infoLabel.setVisible(true);
		    }			
		});
		myForm.add(panel);
		myForm.setVisible(false);
		HorizontalPanel myHPanel = new HorizontalPanel();
		myHPanel.add(openFileButton);	
		// Add a 'cancel' button.
		myHPanel.add(new Button(constants.cancelButton(), new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				myUploadDialog.hide();			
			}
		}));
		vpanel.add(myForm);
		vpanel.add(infoLabel);		
		vpanel.add(loadingLabel);
		vpanel.add(myHPanel);
		myUploadDialog.add(vpanel);	
		myUploadDialog.setHTML("<b>" + constants.saveFileButton() + "</b><br/>");
		myUploadDialog.setModal(false);
		myUploadDialog.center();	
		myForm.submit();
	}
	
	/**
    * This is called whenever the user clicks on load file button
    */
	private void loadFile() {
		// Create a dialog box
		final DialogBox myUploadDialog = new DialogBox();
		myUploadDialog.setGlassEnabled(true);
		// Create a panel to hold all of the form widgets.
		final VerticalPanel panel = new VerticalPanel();		
		// Create a FileUpload widget.
		final FileUpload myFileUpload = new FileUpload();
		myFileUpload.setName("uploadFormElement");
		myFileUpload.getElement().setId("uploadFormElement");
		//myFileUpload.ensureDebugId("myFile");
		myFileUpload.getElement().setId("myFile");
		panel.add(myFileUpload);
		// Create a FormPanel and point it at a service.
		final FormPanel myUploadForm = new FormPanel();
		myUploadForm.setAction("loadService");
		// Because we're going to add a FileUpload widget, we'll need to set the
		// form to use the POST method, and multipart MIME encoding.
		myUploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		myUploadForm.setMethod(FormPanel.METHOD_POST);
		// Add SubmitHandler to the upload form
		myUploadForm.addSubmitHandler(new FormPanel.SubmitHandler() {
			@Override
		    public void onSubmit(SubmitEvent event) {
				// This event is fired just before the form is submitted. We can take
		        // this opportunity to perform validation.
		        if (myFileUpload.getFilename().length() == 0) {
		        	Window.alert(constants.selectFile());
		            event.cancel();
		        }		  
		        //messagesLabel.setText(constants.loadingFile());
				//messagesLabel.setStyleName("infoLabel");
		       //Turn hourglass off
				Document.get().getBody().getStyle().setCursor(Style.Cursor.WAIT);		        
		    }
		});
		final Button submitButton = new Button(constants.submitButton(), new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				myUploadForm.submit();
			}
		});
		final Button cancelButton = new Button(constants.cancelButton(), new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				myUploadDialog.hide();			
			}
		});
		// Add SubmitCompleteHandler to the upload form
		myUploadForm.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
		        // When the form submission is successfully completed, this event is
		        // fired. Assuming the service returned a response of type text/html,
		        // we can get the result text here (see the FormPanel documentation for
		        // further explanation).
				// hide loading message
				myUploadDialog.hide();
				// get results and process them
				String ret = event.getResults();
		        DivElement div = Document.get().createDivElement();
		        div.setInnerHTML(ret);
		        ret = div.getInnerText(); 
		        //Turn hourglass off
				Document.get().getBody().getStyle().setCursor(Style.Cursor.DEFAULT);
		        if (ret.startsWith("ERROR")) {
		        	Window.alert(constants.unableToLoadFile());
		    	}
		        else {
		        	String myFileInterface = Utilities.foundInterface(ret);
		        	if (myFileInterface.isEmpty()) 
		        		Window.alert(constants.unableToLoadFilemd_scopecode());
		        	else {
		        		if (!ret.contains("xmlns:gmd=\"http://www.isotc211.org/2005/gmd\"")) 
		        			Window.alert(constants.errorSchema());
			        	else 
			        		newForm(myFileInterface,ret);		        			
		        	}
		        }
		    }			
		});		
		// Add the panel to the form
		myUploadForm.add(panel);		
		HorizontalPanel myHPanel = new HorizontalPanel();		
		// Add a 'submit' button.
		myHPanel.add(submitButton);		
		// Add a 'cancel' button.
		myHPanel.add(cancelButton);		
		panel.add(myHPanel);		
		// Add the form to the dialog box 	
		myUploadDialog.add(myUploadForm);
		myUploadDialog.setHTML("<b>" + constants.openFileButton() + "</b><br/>");
		myUploadDialog.setModal(true);
		myUploadDialog.center();		
	} 	
	
	/**
    * Function to get the filename
    * If metadata element md_metadata[1].fileidentifier[1].characterstring[1] is valued, the function returns this value, 
    * otherwise a random combination of numbers and letters is returned
    * 
    * @return {@link String} = the name of the file  
    */
	private String getFileName () {
		// set or get (if there is one) the fileName
		String tmpFileName = "";
		Element fileIdentifier = Document.get().getElementById("md_metadata[1].fileidentifier[1].characterstring[1]");
		if (fileIdentifier != null)
			if (!fileIdentifier.getAttribute("value").isEmpty())
				tmpFileName = fileIdentifier.getAttribute("value");
		if (tmpFileName.isEmpty() || tmpFileName.equalsIgnoreCase("#FILENAME#"))  {
			Integer myNum = Random.nextInt();
			if (Integer.signum(myNum)==-1) myNum=-(myNum);
			tmpFileName = Integer.toHexString(myNum);			
			fileIdentifier.setAttribute("value", tmpFileName);
		}
		return tmpFileName;		
	}
	
	/**
	 * Get the XML structure starting from the {@link Tree}
	 * 
	 * @return {@link String} = the XML
	 */
	private String getXMLTree () {
		String myFileName = getFileName();
		// get XML tree structure
		String myXMLTree = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + Utilities.parseTree(myTree.getItem(0));
		myXMLTree = myXMLTree.replace("#FILENAME#",myFileName);
		myXMLTree = myXMLTree.replace(constants.emptyXMLValue(),"");
		// replace multiple blank lines with once
		myXMLTree = myXMLTree.replace("\n\n","\n");
		// Remove empty dates
		myXMLTree = myXMLTree.replace("<gmd:date>\n<gmd:CI_Date>\n<gmd:dateType>\n<gmd:CI_DateTypeCode codeList=\"http://standards.iso.org/ittf/PubliclyAvailableStandards/ISO_19139_Schemas/resources/Codelist/ML_gmxCodelists.xml#CI_DateTypeCode\" codeListValue=\"publication\">publication</gmd:CI_DateTypeCode>\n</gmd:dateType>\n</gmd:CI_Date>\n</gmd:date>\n","");
		myXMLTree = myXMLTree.replace("\n\n","\n");
		myXMLTree = myXMLTree.replace("<gmd:date>\n<gmd:CI_Date>\n<gmd:dateType>\n<gmd:CI_DateTypeCode codeList=\"http://standards.iso.org/ittf/PubliclyAvailableStandards/ISO_19139_Schemas/resources/Codelist/ML_gmxCodelists.xml#CI_DateTypeCode\" codeListValue=\"revision\">revision</gmd:CI_DateTypeCode>\n</gmd:dateType>\n</gmd:CI_Date>\n</gmd:date>\n","");
		myXMLTree = myXMLTree.replace("\n\n","\n");
		myXMLTree = myXMLTree.replace("<gmd:date>\n<gmd:CI_Date>\n<gmd:dateType>\n<gmd:CI_DateTypeCode codeList=\"http://standards.iso.org/ittf/PubliclyAvailableStandards/ISO_19139_Schemas/resources/Codelist/ML_gmxCodelists.xml#CI_DateTypeCode\" codeListValue=\"creation\">creation</gmd:CI_DateTypeCode>\n</gmd:dateType>\n</gmd:CI_Date>\n</gmd:date>\n","");
		myXMLTree = myXMLTree.replace("\n\n","\n");
		// Remove empty temporal element
		myXMLTree = myXMLTree.replace("<gmd:extent>\n<gmd:EX_Extent>\n<gmd:temporalElement>\n<gmd:EX_TemporalExtent>\n<gmd:extent>\n<gml:TimePeriod gml:id=\"IDcd3b1c4f-b5f7-439a-afc4-3317a4cd89be\" xsi:type=\"gml:TimePeriodType\">\n</gml:TimePeriod>\n</gmd:extent>\n</gmd:EX_TemporalExtent>\n</gmd:temporalElement>\n</gmd:EX_Extent>\n</gmd:extent>\n","");
		myXMLTree = myXMLTree.replace("\n\n","\n");
		myXMLTree = myXMLTree.replace("<gmd:temporalElement>\n<gmd:EX_TemporalExtent>\n<gmd:extent>\n<gml:TimePeriod gml:id=\"IDcd3b1c4f-b5f7-439a-afc4-3317a4cd89be\" xsi:type=\"gml:TimePeriodType\">\n</gml:TimePeriod>\n</gmd:extent>\n</gmd:EX_TemporalExtent>\n</gmd:temporalElement>","");
		myXMLTree = myXMLTree.replace("\n\n","\n");
		// Remove empty resource information
		String myTodayDate = DateTimeFormat.getFormat("yyyy-MM-dd").format(new Date());
		myXMLTree = myXMLTree.replace("<gmd:citation>\n<gmd:CI_Citation>\n<gmd:date>\n<gmd:CI_Date>\n<gmd:date>\n<gco:Date>"+ myTodayDate + "</gco:Date>\n</gmd:date>\n<gmd:dateType>\n<gmd:CI_DateTypeCode codeList=\"http://standards.iso.org/ittf/PubliclyAvailableStandards/ISO_19139_Schemas/resources/Codelist/ML_gmxCodelists.xml#CI_DateTypeCode\" codeListValue=\"creation\">creation</gmd:CI_DateTypeCode>\n</gmd:dateType>\n</gmd:CI_Date>\n</gmd:date>\n</gmd:CI_Citation>\n</gmd:citation>","");
		myXMLTree = myXMLTree.replace("\n\n","\n");
		// Degree not evaluated
		myXMLTree = myXMLTree.replace("</gmd:explanation>\n</gmd:DQ_ConformanceResult>","</gmd:explanation>\n<gmd:pass></gmd:pass>\n</gmd:DQ_ConformanceResult>");
		myXMLTree = myXMLTree.replace("\n\n","\n");
		// Remove empty distance
		myXMLTree = myXMLTree.replace("<gmd:distance>\n<gco:Distance uom=\"\"/>\n</gmd:distance>","");
		myXMLTree = myXMLTree.replace("\n\n","\n");
		// Remove empty spatial resolution
		myXMLTree = myXMLTree.replace("<gmd:spatialResolution>\n<gmd:MD_Resolution>\n</gmd:MD_Resolution>\n</gmd:spatialResolution>","");
		myXMLTree = myXMLTree.replace("\n\n","\n");
		// Remove empty identification
		myXMLTree = myXMLTree.replace("<gmd:identificationInfo>\n<gmd:MD_DataIdentification>\n</gmd:MD_DataIdentification>\n</gmd:identificationInfo>","");
		myXMLTree = myXMLTree.replace("\n\n","\n");
		// Remove empty distributionInfo
		myXMLTree = myXMLTree.replace("<gmd:distributionInfo>\n<gmd:MD_Distribution>\n<gmd:distributionFormat>\n<gmd:MD_Format>\n<gmd:name gco:nilReason=\"inapplicable\"/>\n<gmd:version gco:nilReason=\"inapplicable\"/>\n</gmd:MD_Format>\n</gmd:distributionFormat>\n</gmd:MD_Distribution>\n</gmd:distributionInfo>","");
		myXMLTree = myXMLTree.replace("\n\n","\n");
		// Remove empty report
		myXMLTree = myXMLTree.replace("<gmd:report>\n<gmd:DQ_DomainConsistency xsi:type=\"gmd:DQ_DomainConsistency_Type\">\n<gmd:measureIdentification>\n<gmd:RS_Identifier>\n<gmd:code>\n<gco:CharacterString>Conformity_001</gco:CharacterString>\n</gmd:code>\n<gmd:codeSpace>\n<gco:CharacterString>INSPIRE</gco:CharacterString>\n</gmd:codeSpace>\n</gmd:RS_Identifier>\n</gmd:measureIdentification>\n<gmd:result>\n<gmd:DQ_ConformanceResult xsi:type=\"gmd:DQ_ConformanceResult_Type\">\n<gmd:explanation>\n<gco:CharacterString>See the referenced specification</gco:CharacterString>\n</gmd:explanation>\n<gmd:pass></gmd:pass>\n</gmd:DQ_ConformanceResult>\n</gmd:result>\n</gmd:DQ_DomainConsistency>\n</gmd:report>","");
		myXMLTree = myXMLTree.replace("\n\n","\n");
		// Remove DQ_DomainConsistency
		myXMLTree = myXMLTree.replace("<gmd:DQ_DomainConsistency xsi:type=\"gmd:DQ_DomainConsistency_Type\">\n<gmd:measureIdentification>\n<gmd:RS_Identifier>\n<gmd:code>\n<gco:CharacterString>Conformity_001</gco:CharacterString>\n</gmd:code>\n<gmd:codeSpace>\n<gco:CharacterString>INSPIRE</gco:CharacterString>\n</gmd:codeSpace>\n</gmd:RS_Identifier>\n</gmd:measureIdentification>\n<gmd:result>\n<gmd:DQ_ConformanceResult xsi:type=\"gmd:DQ_ConformanceResult_Type\">\n<gmd:explanation>\n<gco:CharacterString>See the referenced specification</gco:CharacterString>\n</gmd:explanation>\n<gmd:pass></gmd:pass>\n</gmd:DQ_ConformanceResult>\n</gmd:result>\n</gmd:DQ_DomainConsistency>","");
		myXMLTree = myXMLTree.replace("\n\n","\n");
		// set explanation in conformity (multiple)
		myXMLTree = myXMLTree.replace("</gmd:specification>\n<gmd:pass>","</gmd:specification>\n<gmd:explanation>\n<gco:CharacterString>" + constants.explanationValue() +"</gco:CharacterString>\n</gmd:explanation>\n<gmd:pass>");
		myXMLTree = myXMLTree.replace("\n\n","\n");
		// remove otherRestrictions
		myXMLTree = myXMLTree.replace("<gmd:resourceConstraints>\n<gmd:MD_LegalConstraints>\n<gmd:accessConstraints>\n<gmd:MD_RestrictionCode codeList=\"http://standards.iso.org/ittf/PubliclyAvailableStandards/ISO_19139_Schemas/resources/Codelist/ML_gmxCodelists.xml#MD_RestrictionCode\" codeListValue=\"otherRestrictions\">otherRestrictions</gmd:MD_RestrictionCode>\n</gmd:accessConstraints>\n</gmd:MD_LegalConstraints>\n</gmd:resourceConstraints>","");
		myXMLTree = myXMLTree.replace("\n\n","\n");		
		//remove empty languagecode
		myXMLTree = myXMLTree.replace("<gmd:LanguageCode codeList=\"http://standards.iso.org/ittf/PubliclyAvailableStandards/ISO_19139_Schemas/resources/Codelist/ML_gmxCodelists.xml#LanguageCode\" codeListValue=\"\"/>","");
		myXMLTree = myXMLTree.replace("\n\n","\n");
		return myXMLTree;
	}
}