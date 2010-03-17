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
package org.vectomatic.svg.samples.client.features;

import org.vectomatic.dom.svg.utils.DOMHelper;
import org.vectomatic.dom.svg.utils.SVGConstants;
import org.vectomatic.svg.samples.client.SampleBase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;

public class FeaturesSample extends SampleBase {
	interface FeaturesSampleBinder extends UiBinder<SimplePanel, FeaturesSample> {
	}
	private static FeaturesSampleBinder binder = GWT.create(FeaturesSampleBinder.class);
	private static final String[] features = {
		SVGConstants.SVG_FEATURE_ANIMATION,                                           
		SVGConstants.SVG_FEATURE_ANIMATION_EVENTS_ATTRIBUTE,                          
		SVGConstants.SVG_FEATURE_BASIC_CLIP,                                          
		SVGConstants.SVG_FEATURE_BASIC_FILTER,                                        
		SVGConstants.SVG_FEATURE_BASIC_FONT,                                          
		SVGConstants.SVG_FEATURE_BASIC_GRAPHICS_ATTRIBUTE,                            
		SVGConstants.SVG_FEATURE_BASIC_PAINT_ATTRIBUTE,                               
		SVGConstants.SVG_FEATURE_BASIC_STRUCTURE,                                     
		SVGConstants.SVG_FEATURE_BASIC_TEXT,                                          
		SVGConstants.SVG_FEATURE_CLIP,                                                
		SVGConstants.SVG_FEATURE_COLOR_PROFILE,
		SVGConstants.SVG_FEATURE_CONDITIONAL_PROCESSING,
		SVGConstants.SVG_FEATURE_CONTAINER_ATTRIBUTE,
		SVGConstants.SVG_FEATURE_CORE_ATTRIBUTE,
		SVGConstants.SVG_FEATURE_CURSOR,
		SVGConstants.SVG_FEATURE_DOCUMENT_EVENTS_ATTRIBUTE,
		SVGConstants.SVG_FEATURE_EXTENSIBILITY,
		SVGConstants.SVG_FEATURE_EXTERNAL_RESOURCES_REQUIRED,
		SVGConstants.SVG_FEATURE_FILTER,
		SVGConstants.SVG_FEATURE_FONT,
		SVGConstants.SVG_FEATURE_GRADIENT,
		SVGConstants.SVG_FEATURE_GRAPHICAL_EVENTS_ATTRIBUTE,
		SVGConstants.SVG_FEATURE_GRAPHICS_ATTRIBUTE,
		SVGConstants.SVG_FEATURE_HYPERLINKING,
		SVGConstants.SVG_FEATURE_IMAGE,
		SVGConstants.SVG_FEATURE_MARKER,
		SVGConstants.SVG_FEATURE_MASK,
		SVGConstants.SVG_FEATURE_OPACITY_ATTRIBUTE,
		SVGConstants.SVG_FEATURE_PAINT_ATTRIBUTE,
		SVGConstants.SVG_FEATURE_PATTERN,
		SVGConstants.SVG_FEATURE_SCRIPT,
		SVGConstants.SVG_FEATURE_SCRIPTING,
		SVGConstants.SVG_FEATURE_SHAPE,
		SVGConstants.SVG_FEATURE_STRUCTURE,
		SVGConstants.SVG_FEATURE_STYLE,
		SVGConstants.SVG_FEATURE_SVG,
		SVGConstants.SVG_FEATURE_SVG_ANIMATION,
		SVGConstants.SVG_FEATURE_SVGDOM,
		SVGConstants.SVG_FEATURE_SVGDOM_ANIMATION,
		SVGConstants.SVG_FEATURE_SVGDOM_DYNAMIC,
		SVGConstants.SVG_FEATURE_SVGDOM_STATIC,
		SVGConstants.SVG_FEATURE_SVG_DYNAMIC,
		SVGConstants.SVG_FEATURE_SVG_STATIC,
		SVGConstants.SVG_FEATURE_TEXT,
		SVGConstants.SVG_FEATURE_VIEW,
		SVGConstants.SVG_FEATURE_VIEWPORT_ATTRIBUTE,
		SVGConstants.SVG_FEATURE_XLINK_ATTRIBUTE
	};

	@UiField
	FlexTable table;

	@Override
	public Panel getPanel() {
		FeaturesCss css = FeaturesBundle.INSTANCE.getCss();
		
		// Inject CSS in the document headers
		StyleInjector.inject(css.getText());
		
		// Initialize the UI with UiBinder
		panel = binder.createAndBindUi(this);
		requestSourceContents(HTML_SRC_DIR + "FeaturesSample" + ".html");
		tabPanel.getTabBar().setTabText(0, "Features");
		tabPanel.getTabBar().setTabText(1, "HTML");
		tabPanel.selectTab(0);
		
		// Test all the feature names
		table.setText(0, 0, "Feature name");
		table.getCellFormatter().addStyleName(0, 0, css.header());
		table.setText(0, 1, "Supported");
		table.getCellFormatter().addStyleName(0, 1, css.header());
		for (int i = 0; i < features.length; i++) {
			table.setText(i + 1, 0, features[i]);
			boolean hasFeature = DOMHelper.hasFeature(features[i]);
			table.setText(i + 1, 1, hasFeature ? "yes" : "no");
			table.getCellFormatter().addStyleName(i + 1, 1, hasFeature ? css.supported() : css.unsupported());
		}
		
		return panel;
	}

}
