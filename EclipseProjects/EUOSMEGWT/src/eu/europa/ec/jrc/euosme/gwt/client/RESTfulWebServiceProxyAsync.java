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

import eu.europa.ec.jrc.euosme.gwt.client.callback.InspireServiceRpcCallback;

/**
 * Remote service asynchronous interface for {@link RESTfulWebServiceProxy}
 * 
 * @version 5.0 - February 2011
 * @author 	Marzia Grasso
 */
public interface RESTfulWebServiceProxyAsync {
	
	public void invokeGetRESTfulWebService(String uri, String contentType, String clientLanguage, String filter, AsyncCallback<String> callback);
    
    public void invokeGemetService(String serviceName, String clientLanguage, AsyncCallback<Map<String,String>> callback);
    
	public void invokeUpdateRESTfulWebService(AsyncCallback<InspireServiceRpcCallback.returnType> callback);
	
	void invokeValidationService(String XMLTree, String clientLanguage,
			AsyncCallback<InspireServiceRpcCallback.returnType> callback);

	//public void invokeInspireMetadataConverterService(String acceptType, String XMLTree, String clientLanguage, String filename, AsyncCallback<String> callback);

	public void invokeInspireUUIDService(AsyncCallback<InspireServiceRpcCallback.returnType> callback);

	public void invokeCacheRepositoryRESTfulWebService(String resource, String repository, AsyncCallback<String> callback);
/*
	public void invokeValidationService(String myXMLTree, String localeName,
			InspireServiceRpcCallback callback);*/
	
}