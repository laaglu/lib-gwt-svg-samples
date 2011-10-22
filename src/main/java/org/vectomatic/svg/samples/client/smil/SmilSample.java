/**********************************************
 * Copyright (C) 2010 Lukas Laag
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
package org.vectomatic.svg.samples.client.smil;

import java.util.Iterator;

import org.vectomatic.dom.svg.OMSVGAnimateElement;
import org.vectomatic.dom.svg.OMSVGCircleElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.OMSVGTSpanElement;
import org.vectomatic.dom.svg.OMSVGTextElement;
import org.vectomatic.dom.svg.OMText;
import org.vectomatic.dom.svg.events.BeginEvent;
import org.vectomatic.dom.svg.events.BeginHandler;
import org.vectomatic.dom.svg.events.EndEvent;
import org.vectomatic.dom.svg.events.EndHandler;
import org.vectomatic.dom.svg.events.RepeatEvent;
import org.vectomatic.dom.svg.events.RepeatHandler;
import org.vectomatic.dom.svg.utils.DOMHelper;
import org.vectomatic.dom.svg.utils.SVGConstants;
import org.vectomatic.dom.svg.utils.SVGPrefixResolver;
import org.vectomatic.svg.samples.client.Main;
import org.vectomatic.svg.samples.client.Main.MainBundle;
import org.vectomatic.svg.samples.client.SampleBase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TabLayoutPanel;

/**
 * Class to demonstrate the use of SVG/SMIL animations
 * @author laaglu
 */
public class SmilSample extends SampleBase implements RepeatHandler, BeginHandler, EndHandler {
	interface SmilSampleBinder extends UiBinder<TabLayoutPanel, SmilSample> {
	}
	private static SmilSampleBinder binder = GWT.create(SmilSampleBinder.class);

	
	@UiField(provided=true)
	public static MainBundle mainBundle = Main.mainBundle;
	@UiField
	OMSVGSVGElement svg;
	@UiField
	OMSVGCircleElement circle;
	@UiField
	OMSVGTSpanElement countSpan;
	@UiField
	OMSVGTextElement beginText;
	@UiField
	OMSVGTextElement endText;
	@UiField
	Button endButton;
	@UiField
	Button beginButton;
	
	Timer beginTimer = new Timer() {
		@Override
		public void run() {
			setTextColor(beginText, SVGConstants.CSS_WHITE_VALUE);
		}
	};
	Timer endTimer = new Timer() {
		@Override
		public void run() {
			setTextColor(endText, SVGConstants.CSS_WHITE_VALUE);
		}
	};

	@Override
	public TabLayoutPanel getPanel() {
		if (tabPanel == null) {
			tabPanel = binder.createAndBindUi(this);
			tabPanel.setTabText(0, "Animation");
			createCodeTabs("SmilSample");
			
			// Add the repeat handler manually instead of using the @UiHandler annotation
			// Indeed, since many browsers do not yet support the SVG anim tag, the
			// anim variable may well be null
			OMSVGAnimateElement anim = getAnimation(); 
			if (anim != null) {
				anim.addRepeatHandler(this);
				anim.addBeginHandler(this);
				anim.addEndHandler(this);
			}
		}
		return tabPanel;
	}
	
	private OMSVGAnimateElement getAnimation() {
		Iterator<OMSVGAnimateElement> iterator = DOMHelper.evaluateXPath(circle, "svg:animate", SVGPrefixResolver.INSTANCE);
		if (iterator.hasNext()) {
			return iterator.next(); 
		}
		return null;
	}
	
	@Override
	public void onRepeat(RepeatEvent e) {
		OMText loopCount = (OMText) countSpan.getFirstChild();
		int count = Integer.parseInt(loopCount.getData());
		loopCount.setData(Integer.toString(count + 1));
	}

	@Override
	public void onEnd(EndEvent event) {
		setTextColor(endText, SVGConstants.CSS_YELLOW_VALUE);
		endTimer.schedule(200);
	}

	@Override
	public void onBegin(BeginEvent event) {
		setTextColor(beginText, SVGConstants.CSS_YELLOW_VALUE);
		beginTimer.schedule(200);
	}
	
	private void setTextColor(OMSVGTextElement text, String color) {
		text.getStyle().setSVGProperty(SVGConstants.CSS_FILL_PROPERTY, color);
		text.getStyle().setSVGProperty(SVGConstants.CSS_STROKE_PROPERTY, color);
	}
	
	@UiHandler("endButton")
	public void end(ClickEvent event) {
		OMSVGAnimateElement anim = getAnimation(); 
		if (anim != null) {
			anim.endElement();
		}
	}
	@UiHandler("beginButton")
	public void begin(ClickEvent event) {
		OMSVGAnimateElement anim = getAnimation(); 
		if (anim != null) {
			anim.beginElement();
		}
	}

}
