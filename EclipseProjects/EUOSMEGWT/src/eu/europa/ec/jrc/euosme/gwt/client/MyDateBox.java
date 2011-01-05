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

import java.util.Date;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;


/**
 * This class extends {@link DateBox} solving the known issue 4532 on IE.{@link http://code.google.com/p/google-web-toolkit/issues/detail?id=4532}
 * 
 * @version 3.0 - October 2010
 * @author 	Marzia Grasso
 * 
 */
public class MyDateBox extends DateBox {

	public MyDateBox() {
		super();
	}

	/**
	 * Instantiate the widget
	 * 
	 * @param picker	DatePicker, standars date picker
	 * @param date		Date, represents a specific instant in time
	 * @param format	Format, is an abstract base class for formatting locale-sensitive information such as dates
	 */
	public MyDateBox(DatePicker picker, Date date, Format format) {
		super(picker, date, format);
		this.sinkEvents(Event.ONCLICK);
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.Composite#onBrowserEvent(com.google.gwt.user.client.Event)
	 */
	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		// WORKAROUND for issue 4532: Whole UI disappear in IE 7 when we Hover over the menubar menu item.
		// http://code.google.com/p/google-web-toolkit/issues/detail?id=4532
		if(Utilities.getUserAgent().contains("msie"))	{
			String rootWidth = RootLayoutPanel.get().getElement().getStyle().getWidth();
			RootLayoutPanel.get().setWidth(rootWidth.equals("100%") ? "" : "100%");
		}
	}
}