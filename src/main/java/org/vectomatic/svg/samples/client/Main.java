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
import org.vectomatic.svg.samples.client.xpath.XPathSample;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

/**
 * Application main class. Creates the base UI structure and sets up
 * individual samples
 * @author laaglu
 */
public class Main implements EntryPoint {
	/**
	 * UiBinder template interface
	 */
	interface MainBinder extends UiBinder<SplitLayoutPanel, Main> {
	}
	private static MainBinder binder = GWT.create(MainBinder.class);

	/**
	 * The type passed into the
	 * {@link org.vectomatic.svg.samples.generator.SourceGenerator}.
	 */
	private static final class GeneratorInfo {
	}
	
	/**
	 * Client bundle interface
	 */
	public interface MainBundle extends ClientBundle {
		@Source("orgball.gif")
		public ImageResource treeItem();
		@Source("main.css")
		public MainCss css();
	}
	
	/**
	 * CSS interface
	 */
	public interface MainCss extends CssResource {
		/**
		 * style of tab items
		 */
		public String tab();
		/**
		 * style of sample container
		 */
		public String sample();
	}
	
	/**
	 * The mai resource bundle
	 */
	public static MainBundle mainBundle = GWT.create(MainBundle.class);
	
	/**
	 * The sample navigation tree
	 */
	@UiField
	Tree tree;
	/**
	 * The split panel
	 */
	@UiField
	SplitLayoutPanel splitPanel;
	/**
	 * A LayoutPanel which acts as a card layout (it has several
	 * child widgets, but only one is visible at a given time).
	 */
	@UiField
	LayoutPanel sampleContainer;


	@Override
	public void onModuleLoad() {
		mainBundle.css().ensureInjected();
		
	    // Generate the source code for the examples
	    GWT.create(GeneratorInfo.class);
	    SplitLayoutPanel panel = binder.createAndBindUi(this);
	    
		// Populate the sample tree
	    TreeItem shapesSample = tree.addItem(AbstractImagePrototype.create(mainBundle.treeItem()).getHTML() +  " shapes");
	    shapesSample.setUserObject(new ShapesSample());
	    TreeItem eventSample = tree.addItem(AbstractImagePrototype.create(mainBundle.treeItem()).getHTML() +  " events");
	    eventSample.setUserObject(new EventSample());
	    TreeItem parserSample = tree.addItem(AbstractImagePrototype.create(mainBundle.treeItem()).getHTML() +  " parser");
	    parserSample.setUserObject(new ParserSample());
	    TreeItem featuresSample = tree.addItem(AbstractImagePrototype.create(mainBundle.treeItem()).getHTML() +  " features");
	    featuresSample.setUserObject(new FeaturesSample());
	    TreeItem widgetsSample = tree.addItem(AbstractImagePrototype.create(mainBundle.treeItem()).getHTML() +  " widgets");
	    widgetsSample.setUserObject(new WidgetsSample());
	    TreeItem smilSample = tree.addItem(AbstractImagePrototype.create(mainBundle.treeItem()).getHTML() +  " SMIL animation");
	    smilSample.setUserObject(new SmilSample());
	    TreeItem xpathSample = tree.addItem(AbstractImagePrototype.create(mainBundle.treeItem()).getHTML() +  " XPath");
	    xpathSample.setUserObject(new XPathSample());
	    TreeItem about = tree.addItem(AbstractImagePrototype.create(mainBundle.treeItem()).getHTML() +  " about");
	    about.setUserObject(new AboutSample());
	    tree.addSelectionHandler(new SelectionHandler<TreeItem>() {
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				SampleBase currentSample = (SampleBase) event.getSelectedItem().getUserObject();
				selectSample(currentSample.getPanel());
			}
	    	
	    });
	    tree.setSelectedItem(shapesSample);

	    RootLayoutPanel.get().add(panel);
	}
	
	/**
	 * If it is not already a child of the layoutPanel,
	 * adds this sample panel to children of the layoutPanel. Make all other children
	 * hidden except this this sample panel.
	 */
	private void selectSample(Widget samplePanel) {
		int count = sampleContainer.getWidgetCount();
		int index = -1;
		for (int i = 0; i < count; i++) {
			Widget w = sampleContainer.getWidget(i);
			if (w != samplePanel) {
				sampleContainer.setWidgetVisible(w, false);
			} else {
				sampleContainer.setWidgetVisible(w, true);
				index = i;
			}
		}
		if (index == -1) {
			sampleContainer.add(samplePanel);
			sampleContainer.setWidgetVisible(samplePanel, true);
		}
	}
}
