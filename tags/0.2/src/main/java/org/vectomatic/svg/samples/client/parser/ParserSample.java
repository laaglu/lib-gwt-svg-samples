/**********************************************
 * Copyright (C) 2009 Lukas Laag
 * This file is part of libgwtsvg-samples.
 * 
 * libgwtsvg-samples is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * libgwtsvg-samples is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with libgwtsvg-samples.  If not, see http://www.gnu.org/licenses/
 **********************************************/
package org.vectomatic.svg.samples.client.parser;

import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.utils.OMSVGParser;
import org.vectomatic.svg.samples.client.SampleBase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Class to demonstrate the parsing of SVG files
 * @author laaglu
 */
public class ParserSample extends SampleBase {
	interface ParserSampleBinder extends UiBinder<SimplePanel, ParserSample> {
	}
	private static ParserSampleBinder binder = GWT.create(ParserSampleBinder.class);

	@UiField
	HTML svgContainer;
	@UiField
	ListBox documentListBox;
	
	@Override
	public Panel getPanel() {
		if (panel == null) {
			// Initialize the UI with UiBinder
			panel = binder.createAndBindUi(this);
			requestSourceContents(HTML_SRC_DIR + "ParserSample" + ".html");
			tabPanel.getTabBar().setTabText(0, "Parser");
			tabPanel.getTabBar().setTabText(1, "HTML");
			tabPanel.selectTab(0);

			// Fill the list box with svg file names
			documentListBox.addItem("tiger", "tiger.svg");
			documentListBox.addItem("lion", "lion.svg");
			documentListBox.addItem("butterfly", "butterfly.svg");
			documentListBox.setSelectedIndex(0);
			documentListBoxChange(null);
		}
		return panel;
	}
	
	@UiHandler("documentListBox")
	public void documentListBoxChange(ChangeEvent event) {
		// Request the contents of the file
		String svgDocument = documentListBox.getValue(documentListBox.getSelectedIndex());
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, GWT.getModuleBaseURL() + svgDocument);
		builder.setCallback(new RequestCallback() {
			public void onError(Request request, Throwable exception) {
				source.setHTML("Cannot find resource");
			}

			public void onResponseReceived(Request request, Response response) {
				// Parse the document
				OMSVGSVGElement svg = OMSVGParser.parse(response.getText());
				
				// Insert the SVG root element into the HTML UI
				// Note that the elements must be imported in the UI since they come from another XML document
				Element div = svgContainer.getElement();
				div.getStyle().setHeight(600, Style.Unit.PX);
				if (div.hasChildNodes()) {
					div.replaceChild(svg.getElement(), div.getFirstChild());
				} else {
					div.appendChild(svg.getElement());					
				}
			}
		});

		// Send the request
		try {
			builder.send();
		} catch (RequestException e) {
			GWT.log("Cannot fetch HTML source for " + svgDocument, e);
		}
	}
}
