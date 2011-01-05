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

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Remote service interface (asyncronous) for {@link eu.europa.ec.jrc.euosme.gwt.client.InitServiceProxy}
 * 
 * @version 3.0 - December 2010
 * @author 	Marzia Grasso
 * 
 */
public interface InitServiceProxyAsync {
	void getInitService(AsyncCallback<Map<String, String>> callback);
}