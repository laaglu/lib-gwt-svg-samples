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

import org.vectomatic.dom.svg.OMSVGCircleElement;
import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.utils.OMSVGParser;
import org.vectomatic.dom.svg.utils.SVGConstants;
import org.vectomatic.svg.samples.client.SampleBase;

import com.google.gwt.core.client.GWT;
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

	private static EventSampleBinder binder = GWT.create(EventSampleBinder.class);

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
			Element div = svgContainer.getElement();
			OMSVGDocument doc = OMSVGParser.currentDocument();

			// Create the root svg element
			OMSVGSVGElement svg = doc.createSVGSVGElement();
			svg.getViewBox().getBaseVal().setX(0);
			svg.getViewBox().getBaseVal().setY(0);
			svg.getViewBox().getBaseVal().setWidth(100);
			svg.getViewBox().getBaseVal().setHeight(200);
			svg.getWidth().getBaseVal().setValueAsString("250px");
			svg.getHeight().getBaseVal().setValueAsString("250px");

			// Create a circle
			final OMSVGCircleElement circle = doc.createSVGCircleElement();
			circle.getCx().getBaseVal().setValue(80);
			circle.getCy().getBaseVal().setValue(80);
			circle.getR().getBaseVal().setValue(40);
			circle.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, SVGConstants.CSS_BLACK_VALUE);
			setCircleColor(circle, SVGConstants.CSS_RED_VALUE);
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
					String color = getCircleColor(circle);
					while (color.equals(getCircleColor(circle))) {
						setCircleColor(circle, colors[Random.nextInt(colors.length)]);
					}
				}
			});
			
			// Insert the SVG root element into the HTML UI
			div.appendChild(svg.getElement());
		}
		return panel;
	}
	private static final String getCircleColor(OMSVGCircleElement circle) {
		return circle.getStyle().getSVGProperty(SVGConstants.CSS_FILL_PROPERTY);
	}
	private static final void setCircleColor(OMSVGCircleElement circle, String color) {
		circle.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, color);
	}

}
