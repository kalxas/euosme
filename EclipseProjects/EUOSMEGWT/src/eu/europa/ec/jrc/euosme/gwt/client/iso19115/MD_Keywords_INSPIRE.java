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

import java.util.Date;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TreeItem;

import eu.europa.ec.jrc.euosme.gwt.client.CIOrientations;
import eu.europa.ec.jrc.euosme.gwt.client.CheckFunctions;
import eu.europa.ec.jrc.euosme.gwt.client.DataTypes;
import eu.europa.ec.jrc.euosme.gwt.client.EUOSMEGWT;
import eu.europa.ec.jrc.euosme.gwt.client.Utilities;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Messages;
import eu.europa.ec.jrc.euosme.gwt.client.iso19115.ui.MainPanel;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CI;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CharacterStringMultiple;

/**
 * Create MD_Keywords_INSPIRE model
 * This class includes other classes so the user could choose the keyword from INSPIRE data theme, GEMET or 
 * alternatively, type a free keyword
 * 
 * @version 2.0 - January 2011
 * @author 	Marzia Grasso
 */
public class MD_Keywords_INSPIRE extends CI {
	
	/** Constants declaration */
 	protected iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Messages declaration */
 	protected iso19115Messages messages = GWT.create(iso19115Messages.class);
	
 	/** list of added keywords */
 	CharacterStringMultiple keywordsObj = new CharacterStringMultiple(constants.keywordValue(),"keywordvalue", true, CheckFunctions.normal);
 	
	/** INSPIRE Data Themes */
	MD_Keywords_DataThemes keywordDataThemeObj = new MD_Keywords_DataThemes(constants.inspireDataThemes(), true, false,"");
	
	Button addDataThemesButton = new Button(constants.add() + " " + constants.inspireDataThemes());
	
	/** GEMET keywords */
	MD_Keywords_Gemet keywordGemetObj = new MD_Keywords_Gemet(constants.repository(), false, false,"");
	
	Button addGEMETButton = new Button(constants.add() + " " + constants.repository());
	
	/** free keyword control declaration */
	MD_Keywords keywordFreeObj = new MD_Keywords(constants.freeKeyword(), false, false,"");
	
	Button addFreeButton = new Button(constants.apply());
	
	/** Categories of data services */
	MD_Keywords_DataService keywordDataServiceObj = new MD_Keywords_DataService(constants.dataService(), true, false,"");
	
	Button addDataServiceButton = new Button(constants.add() + " " + constants.dataService());
	
	/** 
     * constructor MD_Keywords_INSPIRE model
     * 
     * @param label		{@link String} = the header
     * @param required	{@link Boolean} = if true, it is required
     * @param multiple	{@link Boolean} = if true, it could be added more than ones
     * @param help		{@link String} = the anchor in the help 
     *  
     * @return	the widget composed by MD_Keywords_INSPIRE fields
     */
	public MD_Keywords_INSPIRE(String label, boolean required, boolean multiple, String help) {
		super(label, required, multiple, help, CIOrientations.VERTICAL);
		// list of keywords
		fieldsGroup.add(keywordsObj);
		keywordsObj.newButton.setVisible(false);
		keywordsObj.newTextBox.setVisible(false);
		if (!EUOSMEGWT.metadataType.equalsIgnoreCase(DataTypes.DATA_SERVICE.toString())) {
			// data themes
			fieldsGroup.add(keywordDataThemeObj);
			addDataThemesButton.setVisible(false);
			addDataThemesButton.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					if (keywordDataThemeObj.listDataThemes.getSelectedIndex()!=0) {
						@SuppressWarnings("unused")
						String selectedValue = keywordDataThemeObj.listDataThemes.getValue(keywordDataThemeObj.listDataThemes.getSelectedIndex()).trim();
						String myKeyword = keywordDataThemeObj.listDataThemes.getItemText(keywordDataThemeObj.listDataThemes.getSelectedIndex()).trim();
						//TODO Get the version of the web service
						addNew(myKeyword,"GEMET - INSPIRE themes, version 1.0","2008-06-01","", false);						
					}				
				}
			});
			keywordDataThemeObj.fieldsGroup.add(addDataThemesButton);
			keywordDataThemeObj.listDataThemes.addChangeHandler(new ChangeHandler() {			
				@Override
				public void onChange(ChangeEvent event) {
					addDataThemesButton.click();					
				}
			});
		}
		else {
			// data service
			fieldsGroup.add(keywordDataServiceObj);
			addDataServiceButton.setVisible(false);
			addDataServiceButton.addClickHandler(new ClickHandler(){
				@Override
				public void onClick(ClickEvent event) {
					if (keywordDataServiceObj.listDataServices.getSelectedIndex()!=0) {
						@SuppressWarnings("unused")
						String selectedValue = keywordDataServiceObj.listDataServices.getValue(keywordDataServiceObj.listDataServices.getSelectedIndex()).trim();
						String myKeyword = keywordDataServiceObj.listDataServices.getItemText(keywordDataServiceObj.listDataServices.getSelectedIndex()).trim();
						String myKeywordService = toDefinedKeywordService(myKeyword);
						addNew(myKeywordService,"","","", false);						
					}				
				}
			});
			keywordDataServiceObj.fieldsGroup.add(addDataServiceButton);
			keywordDataServiceObj.listDataServices.addChangeHandler(new ChangeHandler() {			
				@Override
				public void onChange(ChangeEvent event) {
					addDataServiceButton.click();					
				}
			});
		}
		// GEMET keyword
		fieldsGroup.add(keywordGemetObj);
		addGEMETButton.setVisible(false);
		addGEMETButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//TODO get the version of the repository
				String myKeyword = keywordGemetObj.keywordGEMETObj.getText().replace(constants.selectedValue(), "").trim();
				String mySource = keywordGemetObj.listScheme.getItemText(keywordGemetObj.listScheme.getSelectedIndex()).trim();
				if (keywordGemetObj.listScheme.getSelectedIndex()!=0) 
					if (mySource.equalsIgnoreCase("ISO 19119 geographic services taxonomy")) {
						myKeyword = toDefinedKeywordService(myKeyword);
						addNew(myKeyword,mySource + ", version 2.3","","", false);					
					}
					else {
						if (EUOSMEGWT.gemetPublicationDate.isEmpty())
							addNew(myKeyword,mySource ,"","", false);
						else
							addNew(myKeyword,mySource ,EUOSMEGWT.gemetPublicationDate.get(mySource),"", false);
					}
			}				
		});
		keywordGemetObj.fieldsGroup.add(addGEMETButton);
		keywordGemetObj.suggestObj.addSelectionHandler(new SelectionHandler<TreeItem>()  {
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				if(event.getSelectedItem().getParentItem()!=null) {
					keywordGemetObj.GEMETPanel.setVisible(true);
					keywordGemetObj.keywordGEMETObj.setText(constants.selectedValue() + event.getSelectedItem().getText());
					addGEMETButton.click();
				}
			}			
		});
		// free keyword
		fieldsGroup.add(keywordFreeObj);
		addFreeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String myKeyword = keywordFreeObj.keywordObj.myTextBox.getText();
				String mySource = keywordFreeObj.thesaurusObj.titleObj.myTextBox.getText();
				String myDate = keywordFreeObj.thesaurusObj.dateObj.dateObj.myDateBox.getTextBox().getText();
				String myDateType = keywordFreeObj.thesaurusObj.dateObj.dateTypeObj.getMyValue();
				if (!myKeyword.isEmpty()) 
					addNew(myKeyword,mySource,myDate,myDateType, true);					
			}				
		});
		keywordFreeObj.fieldsGroup.add(addFreeButton);
		// set interface
		setInterface(-1);
	}

	@Override
	public void myCheck() {
		if (this.getParent().isVisible()) {
			keywordFreeObj.myCheck();			
		}
	}

	@Override
	public void setFormName(String name) {
		super.setFormName(name);
		keywordFreeObj.setFormName(name);
		addFreeButton.getElement().setId("newButton-" + name);
	}
	
	/**
	 * This function is used to add a composed string including keyword, source, date and date type
	 * to the list of keywords
	 * 
	 * @param myString		{@link String} = the keyword
	 * @param mySource		{@link String} = the source as the originating controlled vocabulary
	 * @param myDate		{@link String} = the date related to the originating controlled vocabulary
	 * @param myDateType	{@link String} = the date type
	 */
	private void addNew(final String myString, final String mySource, String myDate, String myDateType, boolean free) {
		myError.setVisible(false);
		//check if keyword is empty
		if (myString.isEmpty()) {
			myError.setText(constants.emptyString());
	  	    myError.setVisible(true);
			return;
		}
		// construct item value
		String newItemDescr = "";
		if (!mySource.isEmpty()) {
			if (myDate.isEmpty()) myDate = DateTimeFormat.getFormat("yyyy-MM-dd").format(new Date());
			if (myDateType.isEmpty()) myDateType = "publication";
			newItemDescr = "(" + mySource +", " + myDateType + ", " + myDate + ") " + myString;
		}
		else newItemDescr = myString;
		final String newItem = newItemDescr;
		
		// check if there is a duplicate
		if (keywordsObj.myList.contains(newItem.toUpperCase()))
			return;
		// Add the String to the table.
		Integer row = keywordsObj.myFlexTable.getRowCount();
		keywordsObj.myList.add(newItem.toUpperCase());
		
		keywordsObj.myListBox.addItem(newItem);
		keywordsObj.myListBox.setItemSelected(keywordsObj.myListBox.getItemCount()-1,true);
		// if it is empty, set the value of the current tree item
		Integer i = keywordsObj.myListBox.getItemCount();
		String parentTitle = "";
		if (i==1) { // set current value
			parentTitle = this.getMyFormName() + ".keyword[1]";
			TreeItem val = Utilities.getSelectTreeItem(this.getMyFormName() + ".keyword[1].characterstring[1]");
			if (val!=null) {
				Utilities.setTextTreeItem(val,myString);
				Utilities.ensureItemVisible(val);
				if (!mySource.isEmpty()) {
					val = Utilities.getSelectTreeItem(this.getMyFormName() + ".thesaurusname[1].ci_citation[1].title[1].characterstring[1]");
					if (val!=null) Utilities.setTextTreeItem(val,mySource);
					val = Utilities.getSelectTreeItem(this.getMyFormName() + ".thesaurusname[1].ci_citation[1].date[1].ci_date[1].date[1].date[1]");
					if (val!=null) Utilities.setTextTreeItem(val,myDate);
					val = Utilities.getSelectTreeItem(this.getMyFormName() + ".thesaurusname[1].ci_citation[1].date[1].ci_date[1].datetype[1].ci_datetypecode[1]");
					if (val!=null) Utilities.setTextTreeItem(val,myDateType);
					val = Utilities.getSelectTreeItem(this.getMyFormName() + ".thesaurusname[1].ci_citation[1].date[1].ci_date[1].datetype[1].ci_datetypecode[1].@codeListValue");
					if (val!=null) Utilities.setTextTreeItem(val,myDateType);
				}
			}
		}
		else { // else add a new node
			TreeItem myParentItem = null;
			if (!mySource.isEmpty()) myParentItem = getThesaurusParentTreeItem(mySource);			
			if (myParentItem!=null && !free) { //add a gmd:keyword/gco:CharacterString element
				int nrKeywords = 0;
				int lastKeywordIndex = 1;
				for (int k=0;k <myParentItem.getChildCount();k++) {
					if (myParentItem.getChild(k).getText().equalsIgnoreCase("gmd:keyword")) {
						nrKeywords +=1;
						lastKeywordIndex=myParentItem.getChildIndex(myParentItem.getChild(k));
					}
				}
				nrKeywords +=1;
				parentTitle = myParentItem.getTitle() + ".keyword[" + nrKeywords + "]";
				//gmd:keyword
				TreeItem keywordTreeItem = new TreeItem();
				keywordTreeItem.setTitle(parentTitle);
				keywordTreeItem.setText("gmd:keyword");
				//gco:CharacterString
				TreeItem characterstringTreeItem = new TreeItem();
				characterstringTreeItem.setTitle(parentTitle + ".characterstring[1]");
				characterstringTreeItem.setText("gco:CharacterString");
				keywordTreeItem.addItem(characterstringTreeItem);
				//value
				TreeItem valueTreeItem = new TreeItem(constants.XMLValue() + myString);				
				characterstringTreeItem.addItem(valueTreeItem);
				//insert created keyword element in the right place
				myParentItem.insertItem(lastKeywordIndex+1,keywordTreeItem);
				// set selected item
				Utilities.ensureItemVisible(valueTreeItem);				
			}
			else { // add a gmd:descriptiveKeywords/gmd:MD_Keywords/gmd:keyword/gco:CharacterString element plus thesaurus
				myParentItem = Utilities.getSelectTreeItem(this.getMyFormName());
				myParentItem = myParentItem.getParentItem().getParentItem();
				int nrKeywords = 0;
				int lastKeywordIndex = 1;
				for (int k=0;k <myParentItem.getChildCount();k++) {
					if (myParentItem.getChild(k).getText().equalsIgnoreCase("gmd:descriptiveKeywords")) {
						nrKeywords +=1;
						lastKeywordIndex=myParentItem.getChildIndex(myParentItem.getChild(k));
					}
				}
				nrKeywords +=1;
				parentTitle = myParentItem.getTitle() + ".descriptivekeywords[" + nrKeywords + "]";
				//gmd:descriptiveKeywords
				TreeItem descriptiveKeywordsTreeItem = new TreeItem();
				descriptiveKeywordsTreeItem.setTitle(parentTitle);
				descriptiveKeywordsTreeItem.setText("gmd:descriptiveKeywords");
				//gmd:MD_Keywords gmd:descriptiveKeywords/gmd:MD_Keywords
				TreeItem MD_KeywordsTreeItem = new TreeItem();
				MD_KeywordsTreeItem.setTitle(parentTitle + ".md_keywords[1]");
				MD_KeywordsTreeItem.setText("gmd:MD_Keywords");
				descriptiveKeywordsTreeItem.addItem(MD_KeywordsTreeItem);
				//gmd:keyword gmd:descriptiveKeywords/gmd:MD_Keywords/gmd:keyword
				TreeItem keywordTreeItem = new TreeItem();
				keywordTreeItem.setTitle(parentTitle + ".md_keywords[1].keyword[1]");
				keywordTreeItem.setText("gmd:keyword");
				MD_KeywordsTreeItem.addItem(keywordTreeItem);
				//gco:CharacterString gmd:descriptiveKeywords/gmd:MD_Keywords/gmd:keyword/gco:CharacterString
				TreeItem characterstringTreeItem = new TreeItem();
				characterstringTreeItem.setTitle(parentTitle + ".md_keywords[1].keyword[1].characterstring[1]");
				characterstringTreeItem.setText("gco:CharacterString");
				keywordTreeItem.addItem(characterstringTreeItem);
				//value of gco:CharacterString gmd:descriptiveKeywords/gmd:MD_Keywords/gmd:keyword/gco:CharacterString
				TreeItem valueTreeItem = new TreeItem(constants.XMLValue() + myString);				
				characterstringTreeItem.addItem(valueTreeItem);
				if (!mySource.isEmpty()) {
					//<gmd:thesaurusName> gmd:descriptiveKeywords/gmd:MD_Keywords/gmd:thesaurusName
					TreeItem thesaurusNameTreeItem = new TreeItem();
					thesaurusNameTreeItem.setTitle(parentTitle + ".md_keywords[1].thesaurusname[1]");
					thesaurusNameTreeItem.setText("gmd:thesaurusName");
					MD_KeywordsTreeItem.addItem(thesaurusNameTreeItem);
					//<gmd:CI_Citation> gmd:thesaurusName/gmd:CI_Citation
					TreeItem CI_CitationTreeItem = new TreeItem();
					CI_CitationTreeItem.setTitle(parentTitle + ".md_keywords[1].thesaurusname[1].ci_citation[1]");
					CI_CitationTreeItem.setText("gmd:CI_Citation");
					thesaurusNameTreeItem.addItem(CI_CitationTreeItem);
					//<gmd:title> gmd:thesaurusName/gmd:CI_Citation/gmd:title
					TreeItem titleTreeItem = new TreeItem();
					titleTreeItem.setTitle(parentTitle + ".md_keywords[1].thesaurusname[1].ci_citation[1].title[1]");
					titleTreeItem.setText("gmd:title");
					CI_CitationTreeItem.addItem(titleTreeItem);
					//<gco:CharacterString> gmd:thesaurusName/gmd:CI_Citation/gmd:title/gco:CharacterString
					TreeItem characterstringThesaurusTreeItem = new TreeItem();
					characterstringThesaurusTreeItem.setTitle(parentTitle + ".md_keywords[1].thesaurusname[1].ci_citation[1].title[1].characterstring[1]");
					characterstringThesaurusTreeItem.setText("gco:CharacterString");
					titleTreeItem.addItem(characterstringThesaurusTreeItem);
					//value of gco:CharacterString gmd:thesaurusName/gmd:CI_Citation/gmd:title/gco:CharacterString
					TreeItem valueThesaurusTreeItem = new TreeItem(constants.XMLValue() + mySource);				
					characterstringThesaurusTreeItem.addItem(valueThesaurusTreeItem);
					//<gmd:date> gmd:thesaurusName/gmd:CI_Citation/gmd:date
					TreeItem dateTreeItem = new TreeItem();
					dateTreeItem.setTitle(parentTitle + ".md_keywords[1].thesaurusname[1].ci_citation[1].date[1]");
					dateTreeItem.setText("gmd:date");
					CI_CitationTreeItem.addItem(dateTreeItem);
					//<gmd:CI_Date> gmd:thesaurusName/gmd:CI_Citation/gmd:date/gmd:CI_Date
					TreeItem CI_DateTreeItem = new TreeItem();
					CI_DateTreeItem.setTitle(parentTitle + ".md_keywords[1].thesaurusname[1].ci_citation[1].date[1].ci_date[1]");
					CI_DateTreeItem.setText("gmd:CI_Date");
					dateTreeItem.addItem(CI_DateTreeItem);
					//<gmd:date> gmd:thesaurusName/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date
					TreeItem dateSubTreeItem = new TreeItem();
					dateSubTreeItem.setTitle(parentTitle + ".md_keywords[1].thesaurusname[1].ci_citation[1].date[1].ci_date[1].date[1]");
					dateSubTreeItem.setText("gmd:date");
					CI_DateTreeItem.addItem(dateSubTreeItem);				
					//<gco:Date>2010-12-02</gco:Date> gmd:thesaurusName/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date
					TreeItem dateGcoTreeItem = new TreeItem();
					dateGcoTreeItem.setTitle(parentTitle + ".md_keywords[1].thesaurusname[1].ci_citation[1].date[1].ci_date[1].date[1].date[1]");
					dateGcoTreeItem.setText("gco:Date");
					dateSubTreeItem.addItem(dateGcoTreeItem);		
					//value of <gco:Date> gmd:thesaurusName/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date
					TreeItem valueDateTreeItem = new TreeItem(constants.XMLValue() + myDate);				
					dateGcoTreeItem.addItem(valueDateTreeItem);
					//<gmd:dateType> gmd:thesaurusName/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:dateType
					TreeItem dateTypeTreeItem = new TreeItem();
					dateTypeTreeItem.setTitle(parentTitle + ".md_keywords[1].thesaurusname[1].ci_citation[1].date[1].ci_date[1].datetype[1]");
					dateTypeTreeItem.setText("gmd:dateType");
					CI_DateTreeItem.addItem(dateTypeTreeItem);	
					//<gmd:CI_DateTypeCode codeList="http://standards.iso.org/ittf/PubliclyAvailableStandards/ISO_19139_Schemas/resources/Codelist/ML_gmxCodelists.xml#CI_DateTypeCode" codeListValue="publication">publication</gmd:CI_DateTypeCode> gmd:thesaurusName/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:dateType/gmd:CI_DateTypeCode
					TreeItem dateTypeCodeTreeItem = new TreeItem();
					dateTypeCodeTreeItem.setTitle(parentTitle + ".md_keywords[1].thesaurusname[1].ci_citation[1].date[1].ci_date[1].datetype[1].ci_datetypecode[1]");
					dateTypeCodeTreeItem.setText("gmd:CI_DateTypeCode");
					dateTypeTreeItem.addItem(dateTypeCodeTreeItem);	
					//attribute codelist
					TreeItem codelistTreeItem = new TreeItem();		
					codelistTreeItem.setTitle(parentTitle + ".md_keywords[1].thesaurusname[1].ci_citation[1].date[1].ci_date[1].datetype[1].ci_datetypecode[1].@codelist[1]");
					codelistTreeItem.setText("@codeList=http://standards.iso.org/ittf/PubliclyAvailableStandards/ISO_19139_Schemas/resources/Codelist/ML_gmxCodelists.xml#CI_DateTypeCode");
					dateTypeCodeTreeItem.addItem(codelistTreeItem);
					//attribute codelistValue
					TreeItem codelistvalueTreeItem = new TreeItem();		
					codelistvalueTreeItem.setTitle(parentTitle + ".md_keywords[1].thesaurusname[1].ci_citation[1].date[1].ci_date[1].datetype[1].ci_datetypecode[1].@codelistvalue[1]");
					codelistvalueTreeItem.setText("@codeListValue=" + myDateType);
					dateTypeCodeTreeItem.addItem(codelistvalueTreeItem);
					//value of gmd:CI_DateTypeCode
					TreeItem valuedatetypeTreeItem = new TreeItem(constants.XMLValue() + myDateType);				
					dateTypeCodeTreeItem.addItem(valuedatetypeTreeItem);
				}
				//insert created keyword element in the right place
				myParentItem.insertItem(lastKeywordIndex+1,descriptiveKeywordsTreeItem);
				// set selected item
				Utilities.ensureItemVisible(valueTreeItem);				
			}
		}
		keywordsObj.myFlexTable.setText(row, 0, newItem);
		final String myTitle = parentTitle;
		// Add a button to remove this deliveryPoint from the table.
		Button removeButton = new Button();
		removeButton.addStyleName("minusButton");
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//remove from the list and the flextable
				Integer removedIndex = keywordsObj.myList.indexOf(newItem.toUpperCase()) + 1;
				keywordsObj.myList.remove(removedIndex-1);
				keywordsObj.myListBox.removeItem(removedIndex-1);
				keywordsObj.myFlexTable.removeRow(removedIndex-1);
				//remove from the tree
				if (keywordsObj.myListBox.getItemCount()!=0) {
					//search the item in the tree (use thesaurus name)
					//if there are more than once keywords, remove only the keyword and reassign the id to other keyword elements
					//if there are only one keyword, remove all the tree related to gmd:descriptiveKeywords and reassign the id to other descriptiveKeyword elements 
					String find = "";
					TreeItem myParentItem = getThesaurusParentTreeItem(mySource);
					int myNrElements = 0;
					for (int k=0;k <myParentItem.getChildCount();k++) {
						if (myParentItem.getChild(k).getText().equalsIgnoreCase("gmd:keyword")) {
							myNrElements +=1;
							TreeItem cc = myParentItem.getChild(k).getChild(0);
							if (cc.getChild(0).getText().endsWith(myString)) find = myParentItem.getChild(k).getTitle();
						}
					}
					if (find.isEmpty()) find = myTitle;
					TreeItem val = null;
					if (myNrElements==1 && find.lastIndexOf(".keyword")>0) find=find.substring(0,find.lastIndexOf(".md_keywords"));
					if (myNrElements > 1 && find.lastIndexOf(".keyword")<0) find=find + ".md_keywords[1].keyword[1]";
					val = Utilities.getSelectTreeItem(find);
					if (val!=null) val.remove();					
					String sremovedIndex = find.substring(find.lastIndexOf("[")+1).replace("]","").trim();
					removedIndex = Integer.parseInt( sremovedIndex );					
					if (find.lastIndexOf(".keyword")<0) {
						// count descriptivekeywords
						myParentItem = Utilities.getSelectTreeItem(find.substring(0,find.lastIndexOf(".descriptivekeywords")));
						if (myParentItem!=null) {
							myNrElements = 1;
							for (int k=0;k <myParentItem.getChildCount();k++) {
								if (myParentItem.getChild(k).getText().equalsIgnoreCase("gmd:descriptiveKeywords")) myNrElements +=1;
							}
						}
					}
					for (Integer x = removedIndex + 1; x <= myNrElements;x++) {
						// Reset count of tree items
						String searchFormName = find.substring(0,find.lastIndexOf("[")) + "[" + (x).toString() + "]";
						Integer z = x-1;
						String newFormName = find.substring(0,find.lastIndexOf("[")) + "[" + (z).toString() + "]";
						TreeItem val1 = Utilities.getSelectTreeItem(searchFormName);
						Utilities.changeTitle(val1,searchFormName,newFormName);
					}					
		        }
		        else {
		        	TreeItem val = Utilities.getSelectTreeItem(getMyFormName() + ".keyword[1].characterstring[1]");
					if (val!=null) Utilities.setTextTreeItem(val,constants.emptyXMLValue());
					val = Utilities.getSelectTreeItem(getMyFormName() + ".thesaurusname[1].ci_citation[1].title[1].characterstring[1]");
					if (val!=null) Utilities.setTextTreeItem(val,constants.emptyXMLValue());
					val = Utilities.getSelectTreeItem(getMyFormName() + ".thesaurusname[1].ci_citation[1].date[1].ci_date[1].date[1].date[1]");
					if (val!=null) Utilities.setTextTreeItem(val,constants.emptyXMLValue());
					val = Utilities.getSelectTreeItem(getMyFormName() + ".thesaurusname[1].ci_citation[1].date[1].ci_date[1].datetype[1].ci_datetypecode[1]");
					if (val!=null) Utilities.setTextTreeItem(val,constants.emptyXMLValue());
					val = Utilities.getSelectTreeItem(getMyFormName() + ".thesaurusname[1].ci_citation[1].date[1].ci_date[1].datetype[1].ci_datetypecode[1].@codeListValue");
					if (val!=null) Utilities.setTextTreeItem(val,constants.emptyXMLValue());
		        }
		    }
		});
		keywordsObj.myFlexTable.setWidget(row, 1, removeButton);		
	}
	
	/**
	 * Get the {@link TreeItem} corresponding to the defined thesaurus. Each theasurus has its own list of keywords
	 * 
	 * @param thesaurus	{@link String} = the name of thesaurus to check
	 * 
	 * @return {@link TreeItem} = the parent item
	 */
	private TreeItem getThesaurusParentTreeItem(String thesaurus) {
		Iterator<TreeItem> it = MainPanel.myTree.treeItemIterator();
		while (it.hasNext()) {
	       TreeItem val = it.next();
	       if (val.getTitle().endsWith(".thesaurusname[1].ci_citation[1].title[1].characterstring[1]")) {
	    	   if (val.getChildCount()!=0) {
	    		   for (int i=0;i<val.getChildCount();i++){
	    			   if (val.getChild(i).getText().endsWith(thesaurus)) return(val.getParentItem().getParentItem().getParentItem().getParentItem());   
	    		   }	    		   
	    	   }
	       }
		}
		return null;
	}	
	
	/**
	 * 
	 * @param myKeyword = the non-camel case of keyword
	 * @return  the camel case of keyword (as defined in Metadata Implement Guidelines, e.g humanGeographicViewer) 
	 */
	public String toDefinedKeywordService(String myKeyword) {
		String result = "";
		if (myKeyword.indexOf("(") == -1)
			result = myKeyword;
		else {
			int start = myKeyword.indexOf("(")+1;
			int end = myKeyword.indexOf(")");
			if (end>start) result = myKeyword.substring(start, end);
			else result = myKeyword;
		}		
		return result;
	}
}