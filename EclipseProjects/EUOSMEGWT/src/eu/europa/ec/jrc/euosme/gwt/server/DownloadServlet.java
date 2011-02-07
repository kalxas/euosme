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

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DownloadServlet extends HttpServlet {

	private static final long serialVersionUID = -4356636877078339046L;

	byte[] bbuf = new byte[1024];

	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		String filename = "";
		ServletContext context = getServletConfig().getServletContext();
		
		String dir = "";
		if (context.getRealPath("temp")==null) dir = context.getRealPath("/temp");
		else dir = context.getRealPath("temp");
		
		try {
			String[] attrArray = request.getParameterValues("filename");			
			if(attrArray != null) filename = attrArray[0];
			String xmltree = "";
			attrArray = request.getParameterValues("xmltree");
			if(attrArray != null) xmltree = attrArray[0];
			
	    	Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + "/" + filename), "UTF-8"));
	    	//xmltree = xmltree.replace("&amp;","&");
	    	//xmltree = xmltree.replace("&","&amp;");
	    	out.append(xmltree);
        	out.flush();
       		out.close();
	    }
	    catch (IOException e)    {   
	    	e.printStackTrace();
	    }
	    try {
			ServletOutputStream out = response.getOutputStream();
			
			File file = new File(dir + "/" + filename);
			String mimetype = context.getMimeType(filename);

			response.setContentType((mimetype != null) ? mimetype : "application/octet-stream");
			response.setContentLength((int) file.length());
			response.setHeader("Content-Disposition", "attachement; filename=\"" + filename + "\"");

			DataInputStream in = new DataInputStream(new FileInputStream(file));

			int length;
			while ((in != null) && ((length = in.read(bbuf)) != -1)) {
				out.write(bbuf, 0, length);
			}

			in.close();
			out.flush();
			out.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	}	

	public void doGet(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException {
		doPost(request, response);
	}
}