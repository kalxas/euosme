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

import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Messages;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CI;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CodeList;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.DateImpl;

/**
 * Create CI_Date model
 * It is a reference date for the cited resource
 * 
 * @version 2.1.1 - January 2011
 * @author 	Marzia Grasso
 */
public class CI_Date extends CI {
	
	/** Constants declaration */
 	protected iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Messages declaration */
	protected iso19115Messages messages = GWT.create(iso19115Messages.class);
	
	/** Date control declaration */
	public DateImpl dateObj =	new DateImpl(constants.date(), "", true);
	
	/** dateType control declaration */
	CodeList dateTypeObj =	new CodeList(constants.dateType(),"",true,"5","",true);
	
	/** 
     * constructor CI_Date model
     * 
     * @param label		{@link String} = the header
     * @param required	{@link Boolean} = if true, it is required
     * @param multiple	{@link Boolean} = if true, it could be added more than ones
     * 
     * @return	the widget composed by CI_Date fields
     */
	public CI_Date(String label, boolean required, boolean multiple) {
		super(label, required, multiple);		
		fieldsGroup.add(dateObj);
		fieldsGroup.add(dateTypeObj);		
	}
	
	@Override
	public void myCheck() {
		if (this.getParent().isVisible()) {
			dateObj.myCheck();
			dateTypeObj.myCheck();				
		}		
	}
	
	@Override
	public void setFormName(String name) {
		super.setFormName(name);
		dateObj.setFormName(name + ".date[1].date[1]");
		dateTypeObj.setFormName(name + ".datetype[1].ci_datetypecode[1]");				
	}
	
	@Override
	public void setInterface(int i) {
		if (i==0) {				
			dateTypeObj.setMyValue("publication");
			dateObj.setHelpAnchor("publicationdate");			
			dateTypeObj.setVisible(false);			
		}
		if (i==1) {				
			dateTypeObj.setMyValue("revision");
			dateObj.setHelpAnchor("revisiondate");
			dateTypeObj.setVisible(false);
		}
		if (i==2) {				
			dateTypeObj.setMyValue("creation");
			dateObj.setHelpAnchor("creationdate");
			dateTypeObj.setVisible(false);
		}
	}
}