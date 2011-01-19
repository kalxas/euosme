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

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;

/**
 * {@link SuggestBox} that if the user click on ENTER (with an empty string), the application proposes the full list of
 * available suggestions
 * 
 * @version 1.0 - January 2011
 * @author 	Marzia Grasso
 */
public class MySuggestBox extends SuggestBox implements KeyPressHandler {
	
	public MySuggestBox(MultiWordSuggestOracle oracle) { 
        super(oracle); 
        this.addKeyPressHandler(this); 
	} 

	@Override
	public void onKeyPress(KeyPressEvent event) {
		int c = event.getNativeEvent().getKeyCode(); 
        int i = this.getText().length(); 
        if (c == KeyCodes.KEY_ENTER && i == 0) { 
        	/* Since the query string is null, the default suggestions will get listed */ 
        	this.showSuggestionList(); 
        }		
	} 
} 