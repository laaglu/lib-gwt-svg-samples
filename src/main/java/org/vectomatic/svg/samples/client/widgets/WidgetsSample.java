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

/**
 * This sample contains artwork from project Open Clip Art
 * See the project website at http://www.openclipart.org/ for license details
 */

package org.vectomatic.svg.samples.client.widgets;

import org.vectomatic.dom.svg.ui.SVGImage;
import org.vectomatic.dom.svg.ui.SVGResource;
import org.vectomatic.svg.samples.client.SampleBase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class WidgetsSample extends SampleBase {
	private static class Tooltip extends PopupPanel {
		private HTML contents;
			  
		public Tooltip() {
			super(true);
			contents = new HTML();
			add(contents);
			setStyleName(WidgetsSampleBundle.INSTANCE.getCss().tooltip());
		}

		public void show(Widget sender, int offsetX, int offsetY,
				final String text, final int delay) {
			contents.setHTML(text);
			int left = sender.getAbsoluteLeft() + offsetX;
			int top = sender.getAbsoluteTop() + offsetY;
			setPopupPosition(left, top);
			super.show();
			Timer t = new Timer() {
				public void run() {
					Tooltip.this.hide();
				}
			};
			t.schedule(delay);
		}
	}

	public interface WidgetsSampleBundle extends ClientBundle {
		public static WidgetsSampleBundle INSTANCE = GWT.create(WidgetsSampleBundle.class);
		
		@Source("as_coeur_jean_victor_bal_.svg")
		SVGResource hearts();
		@Source("as_trefle_jean_victor_ba_.svg")
		SVGResource clubs();
		@Source("as_carreau_jean_victor_b_.svg")
		SVGResource diamonds();
		@Source("as_pique_jean_victor_bal_.svg")
		SVGResource spades();
		@Source("tooltip.css")
		public TooltipCss getCss();
	}
	
	interface TooltipCss extends CssResource {
		public String tooltip();
	}
	
	interface WidgetsSampleBinder extends UiBinder<SimplePanel, WidgetsSample> {
	}
	private static WidgetsSampleBinder binder = GWT.create(WidgetsSampleBinder.class);
	
	@UiField
	SVGImage hearts;
	@UiField
	SVGImage clubs;
	@UiField
	SVGImage diamonds;
	@UiField
	SVGImage spades;
	private Tooltip tooltip;

	@Override
	public Panel getPanel() {
		if (panel == null) {
			TooltipCss css = WidgetsSampleBundle.INSTANCE.getCss();
			
			// Inject CSS in the document headers
			StyleInjector.inject(css.getText());

			tooltip = new Tooltip();
			panel = binder.createAndBindUi(this);
			requestSourceContents(HTML_SRC_DIR + "WidgetsSample" + ".html");
			tabPanel.getTabBar().setTabText(0, "Widgets");
			tabPanel.getTabBar().setTabText(1, "HTML");
			tabPanel.selectTab(0);
		}
		return panel;
	}
	
	private void showTooltip(SVGImage sender, String text) {
		tooltip.show(sender, 10, 35, text, 3000);
	}

	@UiHandler("hearts")
	public void onMouseOutHearts(MouseOutEvent event) {
		tooltip.hide();
	}

	@UiHandler("hearts")
	public void onMouseOverHearts(MouseOverEvent event) {
		showTooltip(hearts, "hearts");
	}

	@UiHandler("clubs")
	public void onMouseOutClubs(MouseOutEvent event) {
		tooltip.hide();
	}

	@UiHandler("clubs")
	public void onMouseOverClubs(MouseOverEvent event) {
		showTooltip(clubs, "clubs");
	}

	@UiHandler("diamonds")
	public void onMouseOutDiamonds(MouseOutEvent event) {
		tooltip.hide();
	}

	@UiHandler("diamonds")
	public void onMouseOverDiamonds(MouseOverEvent event) {
		showTooltip(diamonds, "diamonds");
	}

	@UiHandler("spades")
	public void onMouseOutSpades(MouseOutEvent event) {
		tooltip.hide();
	}

	@UiHandler("spades")
	public void onMouseOverSpades(MouseOverEvent event) {
		showTooltip(spades, "spades");
	}
}
