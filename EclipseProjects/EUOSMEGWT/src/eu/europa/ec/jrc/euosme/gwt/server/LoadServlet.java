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

package eu.europa.ec.jrc.euosme.gwt.server;

import java.io.IOException;
import java.util.List;
import java.util.Iterator;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@SuppressWarnings("hiding")
public class LoadServlet<FileItem> extends HttpServlet implements Servlet {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		
		//verify that the request is multipart
		@SuppressWarnings("unused")
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		  
		FileItemFactory factory = new DiskFileItemFactory();
		  
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		try {
			//get the items from the request object
			List items = upload.parseRequest(request);
		  
			Iterator iter = items.iterator();
			while(iter.hasNext()){
				Object obj = iter.next();
				
				//make sure the object is not null
				if(obj == null) continue;
		            
		        org.apache.commons.fileupload.FileItem item = (org.apache.commons.fileupload.FileItem)obj;
		  
		        //if item is a form field then get the value from the item
		        if(item.isFormField()){
		        	String name = item.getName();
		        	String value = "";
		            if(name.compareTo("textBoxFormElement")==0){
		                value = item.getString();		                
		            } 
		            else {
		                value = item.getString();		                
		            }
		            response.getWriter().write(name + "=" + value + "\n");
		         //if the item is not a form field, you know you have
		         //an uploaded file, and therefore you need to process
		         //it as such.
		         } 
		         else {
	                //Here we can do whatever we want with the uploaded file.
	                byte[] fileContents = item.get();
	                String message = new String(fileContents);
	                response.setCharacterEncoding("UTF-8");
	                response.setContentType("text/plain");
	        		response.getWriter().write(message);	        		
		         }
			}		   
		} catch (Exception ex) {			
			response.getWriter().write("ERROR:" + ex.getMessage());
		}
	}	
}