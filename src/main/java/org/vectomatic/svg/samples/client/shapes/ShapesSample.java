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
package org.vectomatic.svg.samples.client.shapes;

import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGEllipseElement;
import org.vectomatic.dom.svg.OMSVGLength;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGPathSegList;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.OMSVGTextElement;
import org.vectomatic.dom.svg.utils.OMSVGParser;
import org.vectomatic.dom.svg.utils.SVGConstants;
import org.vectomatic.svg.samples.client.Main;
import org.vectomatic.svg.samples.client.Main.MainBundle;
import org.vectomatic.svg.samples.client.SampleBase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TabLayoutPanel;

/**
 * Class to demonstrate the creation of miscellaneous SVG shapes
 * @author laaglu
 */
public class ShapesSample extends SampleBase {
	interface ShapesSampleBinder extends UiBinder<TabLayoutPanel, ShapesSample> {
	}
	private static ShapesSampleBinder binder = GWT.create(ShapesSampleBinder.class);

	@UiField(provided=true)
	public static MainBundle mainBundle = Main.mainBundle;
	@UiField
	HTML svgContainer;
	OMSVGSVGElement svg;

	@Override
	public TabLayoutPanel getPanel() {
		if (tabPanel == null) {
			tabPanel = binder.createAndBindUi(this);
			tabPanel.setTabText(0, "Shapes");
			createCodeTabs("ShapesSample");
			
			// Create a SVG document
			OMSVGDocument doc = OMSVGParser.currentDocument();
			
			// Create the root svg element
			svg =  doc.createSVGSVGElement();
			svg.setViewBox(0f, 0f, 300f, 200f);

			// Create a rect
			OMSVGRectElement rect = doc.createSVGRectElement(5f, 35f, 50f, 20f, 4f, 4f);
			rect.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_LIGHTGREEN_VALUE);
			rect.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, SVGConstants.CSS_BLACK_VALUE);
			
			// Create an ellipse
			OMSVGEllipseElement ellipse = doc.createSVGEllipseElement(60f, 80f, 30f, 15f);
			ellipse.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_YELLOW_VALUE);
			ellipse.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, SVGConstants.CSS_BLACK_VALUE);
			ellipse.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_DASHARRAY_PROPERTY, "5,2,2,2");
			
			// Create a heart-shaped path
			OMSVGPathElement path = doc.createSVGPathElement();
			OMSVGPathSegList segs = path.getPathSegList();
			segs.appendItem(path.createSVGPathSegMovetoAbs(14.86487f, 27.54341f));
			segs.appendItem(path.createSVGPathSegCurvetoCubicAbs(2.16163f, 16.27388f, 10.93614f, 23.44857f, 5.53039f, 20.93069f));
			segs.appendItem(path.createSVGPathSegCurvetoCubicAbs(0.40252f, 6.50142f, 0.19540f, 13.47135f, -0.27051f, 9.80295f));
			segs.appendItem(path.createSVGPathSegCurvetoCubicAbs(8.61522f, 0.38648f, 1.50231f, 3.03806f, 4.88027f, 0.20151f));
			segs.appendItem(path.createSVGPathSegCurvetoCubicAbs(15.54304f, 4.44105f, 11.49036f, 0.13255f, 14.15935f, 2.02987f));
			segs.appendItem(path.createSVGPathSegCurvetoCubicRel(0.71868f, 1.07327f, 0.37594f, 0.23175f, 0.35941f, 2.21266f));		
			segs.appendItem(path.createSVGPathSegCurvetoCubicAbs(23.25811f, 0.39544f, 17.31181f, 2.60470f, 20.15524f, 0.48976f));
			segs.appendItem(path.createSVGPathSegCurvetoCubicAbs(31.97687f, 7.41380f, 27.37183f, 0.07861f, 31.56253f, 3.22656f));
			segs.appendItem(path.createSVGPathSegCurvetoCubicRel(-2.04024f, 9.10060f, 0.46508f, 3.14678f, -0.04068f, 6.54082f));
			segs.appendItem(path.createSVGPathSegCurvetoCubicRel(-10.90462f, 9.53931f, -2.92245f, 3.93336f, -7.42165f, 6.18284f));
			segs.appendItem(path.createSVGPathSegCurvetoCubicRel(-3.12799f, 2.94243f, -1.08820f, 0.79363f, -2.28426f, 2.46524f));
			segs.appendItem(path.createSVGPathSegCurvetoCubicRel(-1.03915f, -1.45272f, -0.28088f, -0.52741f, -0.64130f, -1.00829f));
			segs.appendItem(path.createSVGPathSegClosePath());
			path.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, SVGConstants.CSS_RED_VALUE);
			
			// Create a text
			OMSVGTextElement text = doc.createSVGTextElement(10, 120, OMSVGLength.SVG_LENGTHTYPE_PX, "Hello, World");
			text.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, SVGConstants.CSS_BLUE_VALUE);

			svg.appendChild(rect);
			svg.appendChild(ellipse);
			svg.appendChild(path);
			svg.appendChild(text);
			
			// Insert the SVG root element into the HTML UI
			Element div = svgContainer.getElement();
			div.appendChild(svg.getElement());
		}
		return tabPanel;
	}
}
