package eu.europa.ec.jrc.euosme.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;


public class ISOInfoButton extends Button {
	
	private String helpAnchor="";
	
	public ISOInfoButton() {
		super();
		this.addStyleName("infoButton");
		this.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Utilities.openISOInfo(helpAnchor,(Button)event.getSource());			
			}			
		});
	}
	
	public void setHelpAnchor(String help) {
		helpAnchor = help;
		if (help.isEmpty()) this.setVisible(false);
		else this.setVisible(true);
		this.getElement().getParentElement().setAttribute("width","100%");
		this.getElement().getParentElement().setAttribute("align", "right");
	}
}