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
package org.vectomatic.svg.samples.client.events;

import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGEllipseElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.gwt.SVGConstants;
import org.vectomatic.dom.svg.gwt.SVGParser;
import org.vectomatic.svg.samples.client.SampleBase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Class to demonstrate the SVG event handling
 * @author laaglu
 */
public class EventSample extends SampleBase {
	interface EventSampleBinder extends UiBinder<SimplePanel, EventSample> {
	}

	private static EventSampleBinder binder = GWT
			.create(EventSampleBinder.class);

	@UiField
	HTML svgContainer;

	@Override
	public Panel getPanel() {
		if (panel == null) {
			panel = binder.createAndBindUi(this);
			requestSourceContents(HTML_SRC_DIR + "EventSample" + ".html");
			tabPanel.getTabBar().setTabText(0, "Events");
			tabPanel.getTabBar().setTabText(1, "HTML");
			tabPanel.selectTab(0);

			// Cast the document into a SVG document
			DivElement div = svgContainer.getElement().cast();
			OMSVGDocument doc = div.getOwnerDocument().cast();

			// Create the root svg element
			OMSVGSVGElement svg = doc.createElementNS(SVGConstants.SVG_NAMESPACE_URI, SVGConstants.SVG_SVG_TAG).cast();
			svg.setAttribute(SVGConstants.SVG_VIEW_BOX_ATTRIBUTE, "0 0 100 200");
			svg.setAttribute(SVGConstants.SVG_WIDTH_ATTRIBUTE, "250x");
			svg.setAttribute(SVGConstants.SVG_HEIGHT_ATTRIBUTE, "250px");

			// Create a circle
			final OMSVGEllipseElement circle = doc.createElementNS(
					SVGConstants.SVG_NAMESPACE_URI,	
					SVGConstants.SVG_ELLIPSE_TAG).cast();
			circle.setAttribute(SVGConstants.SVG_CX_ATTRIBUTE, "80");
			circle.setAttribute(SVGConstants.SVG_CY_ATTRIBUTE, "80");
			circle.setAttribute(SVGConstants.SVG_RX_ATTRIBUTE, "40");
			circle.setAttribute(SVGConstants.SVG_RY_ATTRIBUTE, "40");
			circle.setAttribute(SVGConstants.CSS_FILL_PROPERTY,	SVGConstants.CSS_RED_VALUE);
			circle.setAttribute(SVGConstants.CSS_STROKE_PROPERTY, SVGConstants.CSS_BLACK_VALUE);
			svg.appendChild(circle);
	
			// Set a mousedown event handler
			circle.addMouseDownHandler(new MouseDownHandler() {
				final String[] colors = new String[] {
						SVGConstants.CSS_RED_VALUE,
						SVGConstants.CSS_BLUE_VALUE,
						SVGConstants.CSS_GREEN_VALUE,
						SVGConstants.CSS_YELLOW_VALUE,
						SVGConstants.CSS_PINK_VALUE };

				@Override
				public void onMouseDown(MouseDownEvent event) {
					circle.setAttribute(SVGConstants.CSS_FILL_PROPERTY,
							colors[Random.nextInt(colors.length)]);
				}
			});
			
			// Insert the SVG root element into the HTML UI
			div.appendChild((Element) svg.cast());
		}
		return panel;
	}

}
