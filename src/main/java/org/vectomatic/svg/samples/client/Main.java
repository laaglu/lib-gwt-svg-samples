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

import org.vectomatic.svg.samples.client.events.EventSample;
import org.vectomatic.svg.samples.client.features.FeaturesSample;
import org.vectomatic.svg.samples.client.parser.ParserSample;
import org.vectomatic.svg.samples.client.shapes.ShapesSample;
import org.vectomatic.svg.samples.client.smil.SmilSample;
import org.vectomatic.svg.samples.client.widgets.WidgetsSample;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitPanelHelper;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class Main implements EntryPoint {
	interface MainBinder extends UiBinder<DecoratorPanel, Main> {
	}
	private static MainBinder binder = GWT.create(MainBinder.class);

	/**
	 * The type passed into the
	 * {@link org.vectomatic.svg.samples.generator.SourceGenerator}.
	 */
	private static final class GeneratorInfo {
	}
	
	public interface MainBundle extends ClientBundle {
		public static MainBundle INSTANCE = GWT.create(MainBundle.class);
		@Source("orgball.gif")
		public ImageResource treeItem();
	}
	
	@UiField
	static HorizontalSplitPanel splitPanel;
	@UiField
	Tree tree;
	@UiField
	SimplePanel sampleContainer;
	static SampleBase currentSample;

	public static ResizeHandler resizeHandler = new ResizeHandler() {
		@Override
		public void onResize(ResizeEvent event) {
			int w = Window.getClientWidth();
			int h = Window.getClientHeight() - 120;
			splitPanel.setSize(w + Unit.PX.toString(), h + Unit.PX.toString());
			
			Style style = SplitPanelHelper.getStyle(splitPanel);
			String leftPaneWidth = style.getWidth();
			if (leftPaneWidth != null && leftPaneWidth.length() > 0) {
				// Process events with size in pixels only
				int index = leftPaneWidth.indexOf(Style.Unit.PX.name().toLowerCase());
				if (index != -1) {
					try {
						currentSample.resize(w - Integer.valueOf(leftPaneWidth.substring(0, index)), h);
					} catch(NumberFormatException e) {
						GWT.log("Incorrect width: " + leftPaneWidth, e);
					}
				}
			}
		}
	};

	@Override
	public void onModuleLoad() {
	    // Generate the source code for the examples
	    GWT.create(GeneratorInfo.class);
	    DecoratorPanel panel = binder.createAndBindUi(this);
	    
		// Populate the sample tree
	    TreeItem shapesSample = tree.addItem(AbstractImagePrototype.create(MainBundle.INSTANCE.treeItem()).getHTML() +  " shapes");
	    shapesSample.setUserObject(new ShapesSample());
	    TreeItem eventSample = tree.addItem(AbstractImagePrototype.create(MainBundle.INSTANCE.treeItem()).getHTML() +  " events");
	    eventSample.setUserObject(new EventSample());
	    TreeItem parserSample = tree.addItem(AbstractImagePrototype.create(MainBundle.INSTANCE.treeItem()).getHTML() +  " parser");
	    parserSample.setUserObject(new ParserSample());
	    TreeItem featuresSample = tree.addItem(AbstractImagePrototype.create(MainBundle.INSTANCE.treeItem()).getHTML() +  " features");
	    featuresSample.setUserObject(new FeaturesSample());
	    TreeItem widgetsSample = tree.addItem(AbstractImagePrototype.create(MainBundle.INSTANCE.treeItem()).getHTML() +  " widgets");
	    widgetsSample.setUserObject(new WidgetsSample());
	    TreeItem smilSample = tree.addItem(AbstractImagePrototype.create(MainBundle.INSTANCE.treeItem()).getHTML() +  " SMIL animation");
	    smilSample.setUserObject(new SmilSample());
	    tree.addSelectionHandler(new SelectionHandler<TreeItem>() {
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				currentSample = (SampleBase) event.getSelectedItem().getUserObject();
				Panel samplePanel = currentSample.getPanel();
				sampleContainer.setWidget(samplePanel);
				resizeHandler.onResize(null);
			}
	    	
	    });
	    tree.setSelectedItem(shapesSample);

		// Hack the HorizontalSplitPanel to generate an event when
		// the splitter element is moved
		splitPanel.setSplitPosition("20%");
		SplitPanelHelper.addHandler(splitPanel, new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if (splitPanel.isResizing()) {
					resizeHandler.onResize(null);
				}
			}
		}, MouseMoveEvent.getType());
	    Window.addResizeHandler(resizeHandler);
	    resizeHandler.onResize(null);
	    RootPanel.get("uiRoot").add(panel);
	}
}
