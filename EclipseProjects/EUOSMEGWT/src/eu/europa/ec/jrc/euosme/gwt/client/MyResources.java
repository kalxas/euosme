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

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

/**
 * {@link ClientBundle} resources
 * 
 * @version 2.0 - January 2011
 * @author 	Marzia Grasso
 */
public interface MyResources extends ClientBundle{
	public static final MyResources INSTANCE =  GWT.create(MyResources.class);

	@Source("images/loaderb16.gif")
    ImageResource loadingImg();
	
	@Source("XMLsources/dataset.xml")
	public TextResource datasetXML();
	
	@Source("XMLsources/series.xml")
	public TextResource seriesXML();
	
	@Source("XMLsources/service.xml")
	public TextResource serviceXML();
	
	@Source("userGuides/eurlex.htm")
	public TextResource help();
	
	@Source("codeLists/2.txt")
	public TextResource codeList2();
	
	@Source("codeLists/3.txt")
	public TextResource codeList3();
	
	@Source("codeLists/4.txt")
	public TextResource codeList4();
	
	@Source("codeLists/5.txt")
	public TextResource codeList5();
	
	@Source("codeLists/6.txt")
	public TextResource codeList6();
	
	@Source("codeLists/7.txt")
	public TextResource codeList7();
	
	@Source("codeLists/8.txt")
	public TextResource codeList8();
	
	@Source("codeLists/9.txt")
	public TextResource codeList9();
	
	@Source("codeLists/10.txt")
	public TextResource codeList10();
	
	@Source("codeLists/11.txt")
	public TextResource codeList11();

	@Source("codeLists/12.txt")
	public TextResource codeList12();
	
	@Source("repositories/Repositories.cache.xml")
	public TextResource repositoryList();
	
	@Source("repositories/GEMET_Concepts.cache.xml")
	public TextResource repositoryGEMET_Concepts();

	@Source("repositories/GEMET_Groups.cache.xml")
	public TextResource repositoryGEMET_Groups();

	@Source("repositories/GEMET_Themes.cache.xml")
	public TextResource repositoryGEMET_Themes();

	@Source("repositories/GEOSS_Societal_Benefit_Areas.cache.xml")
	public TextResource repositoryGEOSS_Societal_Benefit_Areas();

	@Source("repositories/INSPIRE_Feature_Concept_Dictionary.cache.xml")
	public TextResource repositoryINSPIRE_Feature_Concept_Dictionary();
	
	@Source("repositories/INSPIRE_Glossary.cache.xml")
	public TextResource repositoryINSPIRE_Glossary();
	
	@Source("repositories/ISO_19119_geographic_services_taxonomy.cache.xml")
	public TextResource repositoryISO_19119_geographic_services_taxonomy();
}