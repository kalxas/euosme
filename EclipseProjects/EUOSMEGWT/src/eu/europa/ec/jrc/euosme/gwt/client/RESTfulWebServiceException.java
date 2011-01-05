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

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * RPC Exception class
 * 
 * A special custom exception is needed so that the server can pass exceptions to the client. 
 * This custom exception class extends the Exception class and implement the IsSerializable interface.
 * 
 * @version 1.0 - February 2010
 * @author 	Marzia Grasso
 * @see IsSerializable
 */
public class RESTfulWebServiceException extends Exception implements IsSerializable {
	private static final long serialVersionUID = 1L;
	private String message;
	  
	public RESTfulWebServiceException() {
	}
	  
	public RESTfulWebServiceException(String message) {
		super(message);
	    this.message = message;
	}
	 
	public RESTfulWebServiceException(Throwable cause) {
		super(cause);
	}
	 
	public RESTfulWebServiceException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}
	 
	public String getMessage() {
		return message;
	}
}