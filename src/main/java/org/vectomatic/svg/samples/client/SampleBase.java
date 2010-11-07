/**********************************************
 * Copyright (C) 2009 Lukas Laag
 * This file is part of lib-gwt-svg-samples.
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
package org.vectomatic.svg.samples.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;

/**
 * Base class for lib-gwt-svg samples. All samples are
 * stored in a tab panel with three tabs.
 * <ol>
 * <li>The first tab contains the result of the sample code execution</li>
 * <li>The second tab contains the source code for the sample</li>
 * <li>The third tab contains the UiBinder code for the sample</li>
 * <ol>
 * @author laaglu
 */
public abstract class SampleBase {
	/**
	 * Directory where HTML-ified source code is generated
	 */
	public static final String HTML_SRC_DIR = "src/";
	/**
	 * Directory where HTML-ified UiBinder code is generated
	 */
	public static final String UIBINDER_SRC_DIR = "binder/";
	/**
	 * The HTML-ified source code
	 */
	public HTML sourceHtml;
	/**
	 * The HTML-ified UiBinder code
	 */
	public HTML uiBinderHtml;
	/**
	 * The tab panel containing this sample
	 */
	@UiField
	public TabLayoutPanel tabPanel;

	/**
	 * Instantiates the tab panel which contains the sample
	 * @return
	 */
	public abstract TabLayoutPanel getPanel();
	
	/**
	 * Dynamically create the source code tab and UiBinder code tab
	 * @param sampleName The name of the sample
	 */
	protected void createCodeTabs(String sampleName) {
		sourceHtml = createCodeTab("HTML");
		requestCodeContents(HTML_SRC_DIR + sampleName + ".html", sourceHtml);

		uiBinderHtml = createCodeTab("UIBinder");
		requestCodeContents(UIBINDER_SRC_DIR + sampleName + ".html", uiBinderHtml);

		tabPanel.selectTab(0);
	}
	
	private HTML createCodeTab(String tabName) {
		// Create the tab container structure
		FlowPanel tabContainer = new FlowPanel();
		SimplePanel simplePanel = new SimplePanel();
		FlowPanel container = new FlowPanel();
		container.setStyleName(Main.mainBundle.css().sample());
		HTML html = new HTML();
		tabContainer.add(simplePanel);
		simplePanel.setWidget(container);
		container.add(html);
		
		// Create the tab item
		Label label = new Label(tabName);
		label.setStyleName(Main.mainBundle.css().tab());
		
		// Add the tab
		tabPanel.add(tabContainer, label);
		return html;
	}

	/**
	 * Load the sample HTML source code
	 */
	protected void requestCodeContents(String partialPath, final HTML html) {

		// Request the contents of the file
		// Add a bogus query to bypass the browser cache as advised by:
		// https://developer.mozilla.org/En/Using_XMLHttpRequest#Bypassing_the_cache
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, GWT.getModuleBaseURL() + partialPath + "?ts=" + System.currentTimeMillis());
		builder.setCallback(new RequestCallback() {
			public void onError(Request request, Throwable exception) {
				html.setHTML("Cannot find resource");
			}

			public void onResponseReceived(Request request, Response response) {
				html.setHTML(response.getText());
			}
		});

		// Send the request
		try {
			builder.send();
		} catch (RequestException e) {
			GWT.log("Cannot fetch HTML source for " + partialPath, e);
		}
	}
	/**
	 * Resizes the sample
	 */
	protected void resize(int width, int height) {
		GWT.log("resize: " + width + " " + height);
	}
}
