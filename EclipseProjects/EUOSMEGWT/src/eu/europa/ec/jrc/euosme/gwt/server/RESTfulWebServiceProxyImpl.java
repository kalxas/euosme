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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;

import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import eu.europa.ec.jrc.euosme.gwt.client.RESTfulWebServiceException;
import eu.europa.ec.jrc.euosme.gwt.client.RESTfulWebServiceProxy;
import eu.europa.ec.jrc.euosme.gwt.client.callback.InspireServiceRpcCallback;
import eu.europa.ec.jrc.euosme.gwt.client.callback.InspireServiceRpcCallback.returnType;
import eu.inspire.geoportal.viewclient.cache.gemet.Concept;
import eu.inspire.geoportal.viewclient.cache.gemet.GemetClient;

public class RESTfulWebServiceProxyImpl extends RemoteServiceServlet implements RESTfulWebServiceProxy {
	private static final long serialVersionUID = 1L;
	static String kuser = ""; // your account name
	static String kpass = ""; // your password for the account
	static String codelists = ""; 
	static String repositories = "";
	static String limit = "200";
	static String dataThemes = "";
	static String dataServices = "";
	static Boolean saveCodeList = false;
	static String inspireValidationService = "";
	static String inspireWebService = "";
	static String repoType = "";
	static String repoGraph="";
	static String repoGraph2="";
	
    static class MyAuthenticator extends Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            return (new PasswordAuthentication(kuser, kpass.toCharArray()));
        }
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        kuser = config.getInitParameter("username");
        kpass = config.getInitParameter("password");
        repoType = config.getInitParameter("repotype");
        repoGraph = config.getInitParameter("repograph");
        repoGraph2 = config.getInitParameter("repograph2");
        codelists = config.getInitParameter("codelists");
        repositories = config.getInitParameter("repositories");
        dataThemes = config.getInitParameter("dataThemes");
        dataServices = config.getInitParameter("dataServices");
        limit = config.getInitParameter("limit");
        inspireValidationService = config.getInitParameter("inspireValidationService");
        inspireWebService = config.getInitParameter("inspireWebService");
      }
    
    public RESTfulWebServiceProxyImpl() { // must have
    }

    public String invokeGetRESTfulWebService(String paramName, String scheme, String clientLanguage, String filter) 
    	throws RESTfulWebServiceException {
    	try {
    		//check user and password for semantic researches
    		if (!paramName.equalsIgnoreCase("codelists") &&    				
    				((repoType.equalsIgnoreCase("sesame")) && kuser.isEmpty())
    				)
    			return "AUTHENTICATIONFAILED";
    		
        	String urlParameters = "";
        	String uri = "";
        	String encoding = "UTF-8";
        	String query = "";
        	if (paramName.equalsIgnoreCase("codelists")) {
        		uri = codelists + scheme + "/values?max=" + limit;
        	}
        	else {
        		
        		// get the list of schemes: repositories
        		// get the top-level concepts, build the tree: repository
        		// expand one node, get its children: narrower
        		// search: search 
        		if (paramName.equalsIgnoreCase("repositories") ||
        				paramName.equalsIgnoreCase("repository") ||
        				paramName.equalsIgnoreCase("narrower") || 
        				paramName.equalsIgnoreCase("search")) {
        			
        			uri = repositories;
    				if (repoType.equalsIgnoreCase("virtuoso"))  {
    					if (repoGraph2 == null)
    						query = getContents("query_geoss_" + paramName + ".rq");
    					else {
    						query = getContents("query_" + paramName + ".rq");
    						query = query.replace("##graphURI2##",repoGraph2);
    					}
    					query = query.replace("##graphURI##",repoGraph);
    					
    					
    				}
    				else 
    					query = getContents("query_" + paramName + ".rq");
    				//uri = repositories + "?queryLn=SPARQL&query=PREFIX%20rdfs%3A%3Chttp%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23%3E%0APREFIX%20owl2xml%3A%3Chttp%3A%2F%2Fwww.w3.org%2F2006%2F12%2Fowl2-xml%23%3E%0APREFIX%20dct%3A%3Chttp%3A%2F%2Fpurl.org%2Fdc%2Fterms%2F%3E%0APREFIX%20xsd%3A%3Chttp%3A%2F%2Fwww.w3.org%2F2001%2FXMLSchema%23%3E%0APREFIX%20owl%3A%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23%3E%0APREFIX%20rdf%3A%3Chttp%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23%3E%0APREFIX%20inspire%3A%3Chttp%3A%2F%2Finspire-registry.jrc.ec.europa.eu%2Frdfschema%2Finspire-schema.rdf%23%3E%0APREFIX%20skos%3A%3Chttp%3A%2F%2Fwww.w3.org%2F2004%2F02%2Fskos%2Fcore%23%3E%0A%0ASELECT%20DISTINCT%20%3Fv%20%3Fl%0AWHERE%20{%0A%20%20{%0A%20%20%20%20%3Fv%20skos%3AprefLabel%20%3Fl.%0A%20%20%20%20%3Fv%20rdf%3Atype%20skos%3AConceptScheme.%0A%20%20}%0A}%0AORDER%20BY%20ASC%28%3Fl%29&limit=" + limit + "&infer=true";
                    // geoss: http://semanticlab.jrc.ec.europa.eu:4433/sparql?debug=off&format=text%2Fxml&query=[add query here]
    				if (paramName.equalsIgnoreCase("repositories")){
	    				if (repoType.equalsIgnoreCase("virtuoso"))
	        				uri = uri + "?debug=off&format=text%2Fxml" + "&query=" + URLEncoder.encode(query, "UTF-8");
	        			else
	        				uri = uri + "?queryLn=SPARQL" + "&query=" + URLEncoder.encode(query, "UTF-8")+"&limit=" + limit + "&infer=true";
	    			}
    				else {
	    				query = query.replace("##scheme##",scheme);
	    				query = query.replace("##clientLanguage##",clientLanguage);
	    				query = query.replace("##filter##",filter); 
	    				
	    				if (repoType.equalsIgnoreCase("virtuoso"))
	    					urlParameters="debug=off&format=text%2Fxml" + "&query=" + URLEncoder.encode(query, "UTF-8");
	    				else {    					
	        				urlParameters="queryLn=" + URLEncoder.encode("SPARQL", "UTF-8");
	    					urlParameters+="&query=" + URLEncoder.encode(query, "UTF-8");
	        				urlParameters+="&limit=" + limit + "&infer=true";        				
	    				}
    				}
        		}  
        		
				if (!repoType.equalsIgnoreCase("virtuoso"))
				  Authenticator.setDefault(new MyAuthenticator());
        	}        	
            URL u = new URL(uri);
            HttpURLConnection uc = (HttpURLConnection) u.openConnection();
            
            if (paramName.equalsIgnoreCase("repository") || paramName.equalsIgnoreCase("narrower") || paramName.equalsIgnoreCase("search")) {
            	uc.setRequestMethod("POST");
                uc.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
                uc.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
                uc.setRequestProperty("Content-Language", clientLanguage );
                uc.setUseCaches (false);
                uc.setDoInput(true);
                uc.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream (uc.getOutputStream ());
                wr.writeBytes (urlParameters);
                wr.flush ();
                wr.close ();
                if (uc.getContentEncoding()!=null) encoding = uc.getContentEncoding();
            }    
            else if (paramName.equalsIgnoreCase("repositories")) {
            	uc.setRequestProperty("Content-Type", "application/xml;charset=ISO-8859-1");
            	uc.setRequestMethod("GET");
            	uc.setUseCaches(true);
            	int status = uc.getResponseCode();
                if (status != 200)
                	if (status == 401) return "AUTHENTICATIONFAILED";
                	else throw (new RESTfulWebServiceException("Invalid HTTP response status code " + status + " from web service server."));
                if (uc.getContentEncoding()!=null) encoding = uc.getContentEncoding();         	
            }           	
            
            else if (paramName.equalsIgnoreCase("codelists")) {            	
            	uc.setDoOutput(false);
            	uc.setRequestProperty("accept", "application/json");
            	uc.setRequestProperty("accept-language", clientLanguage);
            	uc.setRequestMethod("GET");
            	uc.setUseCaches(true);
            	int status = uc.getResponseCode();
                if (status != 200)
                    throw (new RESTfulWebServiceException("Invalid HTTP response status code " + status + " from web service server."));
                if (uc.getContentEncoding()!=null) encoding = uc.getContentEncoding();              
            }                                  
            BufferedReader d = new BufferedReader(new InputStreamReader(uc.getInputStream(), encoding));
            StringBuilder buffer = new StringBuilder(16384);
            try {
            	String line;
                while ((line = d.readLine()) != null) {
                	buffer.append(line.trim());                	
                }
            } finally {
            	d.close();
	        }
            if (paramName.equalsIgnoreCase("codelists") && saveCodeList) {         
	            ServletContext context = getServletConfig().getServletContext();
	    		String dir = "";
				if (context.getRealPath("temp")==null) dir = context.getRealPath("/temp");
	    		else dir = context.getRealPath("temp");
	    		try {
	    			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + "/" + scheme + "_" + clientLanguage + ".txt" ), "UTF-8"));
	             	out.append(buffer.toString());
	            	out.flush();
	           		out.close();
	    		}
	           	catch (IOException e)    {   
	        	   	e.printStackTrace();
	        	}	
            }
            return buffer.toString();
            
        } 
        catch (MalformedURLException e) {
        	throw new RESTfulWebServiceException(e.getMessage(), e);
        }
        catch (IOException e) {
            throw new RESTfulWebServiceException(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
	public Map<String,String> invokeGemetService(String serviceName, String clientLanguage) 
	throws RESTfulWebServiceException {
    	try {
    		Map<String,String> definitions = new LinkedHashMap<String, String>();
    		String url = "";
    		if (serviceName.equalsIgnoreCase("dataThemes")) url = dataThemes;
    		else url = dataServices;
    		GemetClient instance = new GemetClient(url);    		
    		ArrayList c = instance.getTopmostConcepts(instance.getThesaurusInspireThemes(),new Locale(clientLanguage));
    		Iterator i = c.iterator();
            while (i.hasNext()) {
                Concept concept = (Concept) i.next();
                String preferredLabel = concept.getPreferredLabel().getPropertyValue().getString();
                String definition = concept.getDefinition().getPropertyValue().getString();
                //Quick fix while we update the our GEMET clone 
                if ("nl".equals(clientLanguage)){
                	if ("Gebieden met natuurrisico'es".equals(preferredLabel)){
                		preferredLabel = "Gebieden met natuurrisico's";
                	}
                }
                @SuppressWarnings("unused")
				String definition_uri = concept.getUri();
                definitions.put(preferredLabel,definition);
            }
        	return definitions;        
	    } 
	    catch (MalformedURLException e) {
	    	throw new RESTfulWebServiceException(e.getMessage(), e);
	    }	   
	 }
    
	public InspireServiceRpcCallback.returnType invokeUpdateRESTfulWebService() 
	throws RESTfulWebServiceException {
		try {
	    	String uri = "";
	    	String encoding = "UTF-8";
	    	ServletContext context = getServletConfig().getServletContext();
			String dir = "";
			if (context.getRealPath("temp")==null) dir = context.getRealPath("/temp");
			else dir = context.getRealPath("temp");
	    	for (int extraValue=2;extraValue<=2;extraValue++) {
	    		uri = codelists + extraValue + "/values?max=" + limit;
	    		URL u = new URL(uri);
	    		String[] languages={"bg","cs","da","de","el","en","es","et","fi","fr","hu","it","lt","lv","mt","nl","pl","pt","ro","sk","sl","sv"};
	    		for (int i = 0; i<languages.length; i++ ) {
	    			String clientLanguage = languages[i];
		            HttpURLConnection uc = (HttpURLConnection) u.openConnection();
		            uc.setDoOutput(false);
		        	uc.setRequestProperty("accept", "application/json");
		        	//uc.setRequestProperty("accept-language", "en");
		        	uc.setRequestProperty("accept-language", clientLanguage);
		        	uc.setRequestMethod("GET");
		        	uc.setUseCaches(true);
		        	int status = uc.getResponseCode();
		            if (status != 200)
		                throw (new RESTfulWebServiceException("Invalid HTTP response status code " + status + " from web service server."));
		            if (uc.getContentEncoding()!=null) encoding = uc.getContentEncoding();            
			        BufferedReader d = new BufferedReader(new InputStreamReader(uc.getInputStream(), encoding));
			        StringBuilder buffer = new StringBuilder(16384);
			        try {
			        	String line;
			            while ((line = d.readLine()) != null) {
			            	buffer.append(line.trim());                	
			            }
			        } finally {
			        	d.close();
			        }		        
		    		try {
		    			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + "/" + extraValue + "_" + clientLanguage + ".txt" ), "UTF-8"));
		             	out.append(buffer.toString());
		            	out.flush();
		           		out.close();
		           		if (clientLanguage.equalsIgnoreCase("en")) {
		           			Writer outOriginal = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + "/" + extraValue + ".txt" ), "UTF-8"));
		           			outOriginal.append(buffer.toString());
		           			outOriginal.flush();
		           			outOriginal.close();
		           		}
		    		}
		           	catch (IOException e)    {   
		        	   	e.printStackTrace();
		        	}
	    		}
	    	}
	    } 
	    catch (MalformedURLException e) {
	    	throw new RESTfulWebServiceException(e.getMessage(), e);
	    }
	    catch (IOException e) {
	        throw new RESTfulWebServiceException(e.getMessage(), e);
	    }
		return null;
	}
	/*
	public String invokeValidationService(String XMLTree) 
 	throws RESTfulWebServiceException {
		String dir = "";
		try {
	    	ServletContext context = getServletConfig().getServletContext();			
			if (context.getRealPath("temp")==null) dir = context.getRealPath("/temp");
			else dir = context.getRealPath("temp");
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + "/tmp.xml" ), "UTF-8"));
         	out.append(XMLTree);
        	out.flush();
       		out.close();
	    } 
	    catch (MalformedURLException e) {
	    	throw new RESTfulWebServiceException(e.getMessage(), e);
	    }
	    catch (IOException e) {
	        throw new RESTfulWebServiceException(e.getMessage(), e);
	    }
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String exsistingFileName = dir + "/tmp.xml";
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary =  "*****";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1*1024*1024;
		
		try {		   //------------------ CLIENT REQUEST
			FileInputStream fileInputStream = new FileInputStream( new File(exsistingFileName) );
			// open a URL connection to the Servlet 
			URL url = new URL(inspireValidationService);
			// Open a HTTP connection to the URL
			conn = (HttpURLConnection) url.openConnection();
			// Allow Inputs
			conn.setDoInput(true);
			// Allow Outputs
			conn.setDoOutput(true);
			// Don't use a cached copy.
			conn.setUseCaches(false);
			// Use a post method.
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
			dos = new DataOutputStream( conn.getOutputStream() );
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"dataFile\";"  + " filename=\"tmp.xml" + lineEnd);
			dos.writeBytes(lineEnd);
			// create a buffer of maximum size
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
			// read file and write it into form...
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}
			// send multipart form data necesssary after file data...
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			// close streams
			fileInputStream.close();
			dos.flush();
			dos.close();
			
			BufferedReader d = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		    StringBuilder bufferResponse = new StringBuilder(16384);
		    try {
		    	String lineResponse;
				while ((lineResponse = d.readLine()) != null) {
		    		bufferResponse.append(lineResponse.trim());                	
		        }
		    } finally {
		      	d.close();
			}
		    // Delete temporary file
		    File f = new File(exsistingFileName); // A File object to represent the filename
		    f.delete(); // Attempt to delete it
		    // Return message
		    return bufferResponse.toString();
		}
		catch (MalformedURLException ex) {
			System.out.println("From ServletCom CLIENT REQUEST:"+ex);
		}
		catch (IOException ioe) {
			System.out.println("From ServletCom CLIENT REQUEST:"+ioe);
		}
		return null;
	}
	*/
	
	public InspireServiceRpcCallback.returnType invokeValidationService(String XMLTree, String clientLanguage) 
 	throws RESTfulWebServiceException {
        HttpURLConnection urlConnection = null;
        
		try {
		
			URL u = new URL(inspireWebService + "resources/INSPIREResourceTester");

            urlConnection = (HttpURLConnection) u.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Accept","application/xml");
            urlConnection.setRequestProperty("Content-Type","text/plain;charset=UTF-8");
            urlConnection.setRequestProperty("Accept-language", clientLanguage );
            urlConnection.setUseCaches (false);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            
            byte[] data=XMLTree.getBytes("UTF-8");
            urlConnection.setRequestProperty("Content-Length", "" + Integer.toString(data.length));
            
            DataOutputStream wr = new DataOutputStream (urlConnection.getOutputStream ());
            //wr.writeBytes (XMLTree);
            
            //out.writeInt(data.length);
            
            wr.write(data);            
            
            wr.flush ();
            wr.close ();
            int status = urlConnection.getResponseCode();
            if (status != 201){
                throw (new RESTfulWebServiceException("Invalid HTTP response status code " + status + " from web service server."));
            }
            
            List<String> listLocation = urlConnection.getHeaderFields().get("Location");
            String url = listLocation.get(0); 
           
            BufferedReader d = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
	        StringBuilder buffer = new StringBuilder(16384);
	        try {
	        	String line;
	            while ((line = d.readLine()) != null) {
	            	buffer.append(line.trim());                	
	            }
	        } finally {
	        	d.close();
	        }
	        // Write file

	        ServletContext context = getServletConfig().getServletContext();
	    	String dir = "";
	    	InspireServiceRpcCallback.returnType retVal = new InspireServiceRpcCallback.returnType();
	    	retVal.setText(buffer.toString());

	    	retVal.setUrl(url);
	    	//Try to fetch translated page
	    	HttpClient httpClient = null;
	        try {
		    	httpClient = new DefaultHttpClient();
		        HttpParams httpParams = httpClient.getParams();
		        HttpRequestBase req = null;
		        
		        req = new HttpGet(url);
	        	req.setHeader("Accept-Language", clientLanguage);
	        	req.setHeader("Accept", "text/html");
	        	
		        HttpResponse response = httpClient.execute(req);
		        Header loc = response.getFirstHeader("Content-Location");
		        String fileName = loc.getValue();
		        String fullUrl = url + "/" + fileName;
		        retVal.setUrl(fullUrl);

	        	
	          
	        	  

	        } catch (Exception e) {
	          // Code omitted for clarity
	        	e.getMessage();
	        } finally{
	        	if (httpClient != null){
	            httpClient.getConnectionManager().shutdown();
	        	}
	        }
	        

       		return retVal;
 
       } 
       catch (MalformedURLException e) {
       	throw new RESTfulWebServiceException(e.getMessage(), e);
       }
       catch (IOException e) {
           throw new RESTfulWebServiceException(e.getMessage(), e);
       }		
       finally{
    	   if (urlConnection != null){
    		   urlConnection.disconnect();    	   
    	   }
       }
	}
	
	/*
	public String invokeInspireMetadataConverterService(String acceptType, String XMLTree, String clientLanguage, String filename) 
 	throws RESTfulWebServiceException {
		try {
            URL u = new URL(inspireWebService + "resources/INSPIREResource");
            HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Accept",acceptType);
            urlConnection.setRequestProperty("Content-Type","application/xml;charset=UTF-8");
            urlConnection.setRequestProperty("Content-Length", "" + Integer.toString(XMLTree.getBytes().length));
            urlConnection.setRequestProperty("Accept-language", clientLanguage );
            urlConnection.setUseCaches (false);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream (urlConnection.getOutputStream ());
            wr.writeBytes (XMLTree);
            wr.flush ();
            wr.close ();
            int status = urlConnection.getResponseCode();
            if (status != 200)
                throw (new RESTfulWebServiceException("Invalid HTTP response status code " + status + " from web service server."));
           
            BufferedReader d = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
	        StringBuilder buffer = new StringBuilder(16384);
	        try {
	        	String line;
	            while ((line = d.readLine()) != null) {
	            	buffer.append(line.trim());                	
	            }
	        } finally {
	        	d.close();
	        }
	        // Write file
	        ServletContext context = getServletConfig().getServletContext();
	    	String dir = "";
			if (context.getRealPath("temp")==null) dir = context.getRealPath("/temp");
			else dir = context.getRealPath("temp");
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + "/" + filename ), "UTF-8"));
         	out.append(
         			buffer.toString().replace(
       				"url(\"/schemas/altova/inspireResource.css\");", 
       				"url(\"http://inspire-geoportal.ec.europa.eu/schemas/altova/inspireResource.css\");")
         			);
       		

        	out.flush();
       		out.close();
       		return filename;
       } 
       catch (MalformedURLException e) {
       	throw new RESTfulWebServiceException(e.getMessage(), e);
       }
       catch (IOException e) {
           throw new RESTfulWebServiceException(e.getMessage(), e);
       }		
	}*/
	
	public returnType invokeInspireUUIDService() 
 	throws RESTfulWebServiceException {
		try {
            URL u = new URL(inspireWebService + "resources/UUID ");
            HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("accept","application/xml");
            urlConnection.setUseCaches (false);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            int status = urlConnection.getResponseCode();
            if (status != 200)
                throw (new RESTfulWebServiceException("Invalid HTTP response status code " + status + " from web service server."));
           
            BufferedReader d = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
	        StringBuilder buffer = new StringBuilder(16384);
	        try {
	        	String line;
	            while ((line = d.readLine()) != null) {
	            	buffer.append(line.trim());                	
	            }
	        } finally {
	        	d.close();
	        }		 
	        InspireServiceRpcCallback.returnType retVal = new InspireServiceRpcCallback.returnType();
            retVal.setText(buffer.toString());
            return retVal;
       } 
       catch (MalformedURLException e) {
       	throw new RESTfulWebServiceException(e.getMessage(), e);
       }
       catch (IOException e) {
           throw new RESTfulWebServiceException(e.getMessage(), e);
       }		
	}	
	
	public String invokeCacheRepositoryRESTfulWebService(String resource, String repository) 
	throws RESTfulWebServiceException {
		//String listResource = "@Source(\"repository/" + resource + ".cache.json\")\npublic TextResource " + resource + "();";
    	
		try {
	    	String uri = repositories;
			String urlParameters = "";
	    	String encoding = "UTF-8";
	    	
	    	// set temporary directory where create files
	    	ServletContext context = getServletConfig().getServletContext();
	    	String dir = "";
			if (context.getRealPath("temp")==null) dir = context.getRealPath("/temp");
			else dir = context.getRealPath("temp");
			
			// call the service for each language
			String[] languages={"bg","cs","da","de","el","en","es","et","fi","fr","hu","it","lt","lv","mt","nl","pl","pt","ro","sk","sl","sv"};
    		//for (int i = 0; i<languages.length; i++ ) {
    			String clientLanguage = "en";//languages[i];
    			
    			// query the service and, for each keyword found, create the cache file
				urlParameters="queryLn=" + URLEncoder.encode("SPARQL", "UTF-8");
				String query = getContents("query_cache.rq"); //getContents("query_repository.rq");
				if (resource.equalsIgnoreCase("Repositories"))
					query = getContents("query_repositories.rq");
				query = query.replace("##scheme##",repository);
				query = query.replace("##clientLanguage##",clientLanguage);
				query = query.replace("##filter##","");
				urlParameters+="&query=" + URLEncoder.encode(query, "UTF-8");
				urlParameters+="&limit=0&infer=true";    				
    			
				Authenticator.setDefault(new MyAuthenticator());
        	
    			URL u = new URL(uri);
    			HttpURLConnection uc = (HttpURLConnection) u.openConnection();
               	uc.setRequestMethod("POST");
                uc.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
                uc.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
                uc.setRequestProperty("Content-Language", clientLanguage );
                uc.setUseCaches (false);
                uc.setDoInput(true);
                uc.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream (uc.getOutputStream ());
                wr.writeBytes (urlParameters);
                wr.flush ();
                wr.close ();
                if (uc.getContentEncoding()!=null) encoding = uc.getContentEncoding();
                int status = uc.getResponseCode();
	            if (status != 200)
	                throw (new RESTfulWebServiceException("Invalid HTTP response status code " + status + " from web service server."));
	            if (uc.getContentEncoding()!=null) encoding = uc.getContentEncoding();            
		        BufferedReader d = new BufferedReader(new InputStreamReader(uc.getInputStream(), encoding));
		        StringBuilder buffer = new StringBuilder(16384);
		        try {
		        	String line;
		            while ((line = d.readLine()) != null) {
		            	buffer.append(line.trim());                	
		            }
		        } finally {
		        	d.close();
		        }		        
	    		try {
	    			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + "/" + resource + "_" + clientLanguage + ".cache.xml" ), "UTF-8"));
	             	out.append(buffer.toString());
	            	out.flush();
	           		out.close();
	           		if (clientLanguage.equalsIgnoreCase("en")) {
	           			Writer outOriginal = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + "/" + resource + ".cache.xml" ), "UTF-8"));
	           			outOriginal.append(buffer.toString());
	           			outOriginal.flush();
	           			outOriginal.close();
	           		}
	    		}
	           	catch (IOException e)    {   
	        	   	e.printStackTrace();
	        	}
            //}	    
    	} 
	    catch (MalformedURLException e) {
	    	throw new RESTfulWebServiceException(e.getMessage(), e);
	    }
	    catch (IOException e) {
	        throw new RESTfulWebServiceException(e.getMessage(), e);
	    }
		//return listResource;
	    return null;
	}

	private String getContents(String filename) {
		String dir = "";
		ServletContext context = getServletConfig().getServletContext();			
		if (context.getRealPath("scripts")==null) dir = context.getRealPath("/euosme/scripts");
		else dir = context.getRealPath("scripts");
		
		File aFile = new File(dir + "/" + filename);
		
	    StringBuilder contents = new StringBuilder();
	    try {
	    	BufferedReader input =  new BufferedReader(new FileReader(aFile));
	    	try {
	    		String line = null; //not declared within while loop
	    		while (( line = input.readLine()) != null){
	    			contents.append(line);
	    			contents.append(System.getProperty("line.separator"));
	    		}
	    	}
	    	finally {
	    		input.close();
	    	}
	    }
	    catch (IOException ex){
	    	ex.printStackTrace();
	    }	    
	    return contents.toString();
	  }

	
}        