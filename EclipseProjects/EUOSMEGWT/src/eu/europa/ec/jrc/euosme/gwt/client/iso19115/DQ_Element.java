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
import com.google.gwt.user.client.ui.TreeItem;

import eu.europa.ec.jrc.euosme.gwt.client.EUOSMEGWT;
import eu.europa.ec.jrc.euosme.gwt.client.AppModes;
import eu.europa.ec.jrc.euosme.gwt.client.Utilities;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Constants;
import eu.europa.ec.jrc.euosme.gwt.client.i18n.iso19115Messages;
import eu.europa.ec.jrc.euosme.gwt.client.widgets.CI;

/**
 * Create DQ_Element model
 * Aspect of quantitative quality information
 * 
 * @version 2.0 - October 2010
 * @author 	Marzia Grasso
 */
public class DQ_Element extends CI {
	
	/** Constants declaration */
 	protected iso19115Constants constants = GWT.create(iso19115Constants.class);
	
	/** Messages declaration */
 	protected iso19115Messages messages = GWT.create(iso19115Messages.class);
	
 	/** {@link MD_Identifier} element declaration */
 	MD_Identifier mdIdentifierObj =	new MD_Identifier(constants.measureIdentification(), false, false);
	
 	/** {@link DQ_ConformanceResult} element declaration */
	DQ_ConformanceResult resultObj = new DQ_ConformanceResult(constants.result(),true,false);
			
	/** 
     * constructor DQ_Element model
     * 
     * @param label		{@link String} = the header
     * @param required	{@link Boolean} = if true, it is required
     * @param multiple	{@link Boolean} = if true, it could be added more than ones
     * 
     * @return	the widget composed by DQ_Element fields
     */
	public DQ_Element(String label, boolean required, boolean multiple) {
		super(label, required, multiple);		
		fieldsGroup.add(mdIdentifierObj);
		fieldsGroup.add(resultObj);
		setInterface(-1);
	}
	
	@Override
	public void myCheck() {
		if (this.getParent().isVisible()) {
			mdIdentifierObj.myCheck();
			resultObj.myCheck();
		}
	}

	@Override
	public void setFormName(String name) {
		super.setFormName(name);
		mdIdentifierObj.setFormName(name + ".measureidentification[1].rs_identifier[1]");
		resultObj.setFormName(name + ".result[1].dq_conformanceresult[1]");
	}
	
	@Override
	public void setInterface(int i) {
		if ((EUOSMEGWT.appMode.equalsIgnoreCase(AppModes.GEOPORTAL.toString()))) {
			mdIdentifierObj.setInterface(0);
			mdIdentifierObj.setVisible(false);
			resultObj.removeDisclosure();
		}
	}
	
	@Override
	public String cloneTree(String myId) {
		String ret = super.cloneTree(myId);
		Utilities.valueField(ret + ".result[1].dq_conformanceresult[1].explanation[1].characterstring[1]",constants.explanationValue());
		TreeItem myTreeItem = Utilities.getSelectTreeItem(ret + ".result[1].dq_conformanceresult[1].explanation[1].characterstring[1]");
		Utilities.setTextTreeItem(myTreeItem,constants.explanationValue());	
		return ret;
	}
}