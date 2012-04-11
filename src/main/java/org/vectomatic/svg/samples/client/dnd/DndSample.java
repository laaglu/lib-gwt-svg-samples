package org.vectomatic.svg.samples.client.dnd;

import org.vectomatic.dom.svg.ui.SVGImage;
import org.vectomatic.dom.svg.ui.SVGResource;
import org.vectomatic.svg.samples.client.Main;
import org.vectomatic.svg.samples.client.Main.MainBundle;
import org.vectomatic.svg.samples.client.SampleBase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TabLayoutPanel;

public class DndSample extends SampleBase {
	public interface DndSampleBundle extends ClientBundle {
		
		@Source("bee_forestgreen.svg")
		SVGResource bee();
		@Source("flower.svg")
		SVGResource flower1();
		@Source("HakanL_Simple_Flower.svg")
		SVGResource flower2();
		@Source("PeterM_Flower.svg")
		SVGResource flower3();
		@Source("Gerald_G_Simple_Fruit_15.svg")
		SVGResource fruit1();
		@Source("Gerald_G_Simple_Fruit_2.svg")
		SVGResource fruit2();
		@Source("Gerald_G_Simple_Fruit_5.svg")
		SVGResource fruit3();
	}
	@UiField(provided=true)
	public static DndSampleBundle resources = GWT.create(DndSampleBundle.class);

	
	interface DndSampleBinder extends UiBinder<TabLayoutPanel, DndSample> {
	}
	private static DndSampleBinder binder = GWT.create(DndSampleBinder.class);

	@UiField(provided=true)
	public static MainBundle mainBundle = Main.mainBundle;
	@UiField
	SVGImage flower1;
	@UiField
	SVGImage flower2;
	@UiField
	SVGImage flower3;
	@UiField
	SVGImage fruit1;
	@UiField
	SVGImage fruit2;
	@UiField
	SVGImage fruit3;
	@UiField
	Image bee;
	@UiField
	FlowPanel container;
	
	@Override
	public TabLayoutPanel getPanel() {
		if (tabPanel == null) {
			tabPanel = binder.createAndBindUi(this);
			tabPanel.setTabText(0, "Drag-And-Drop");
			createCodeTabs("DndSample");
			bee.getElement().setAttribute("draggable", "true");
		}
		return tabPanel;
	}
	
	@UiHandler("flower1")
	public void flower1Enter(DragEnterEvent event) {
		flower1.getStyle().setOpacity(0.5f);
		event.preventDefault();
	}
	@UiHandler("flower1")
	public void flower1Leave(DragLeaveEvent event) {
		flower1.getStyle().setOpacity(1f);
	}
	@UiHandler("flower1")
	public void flower1Over(DragOverEvent event) {
		flower1.getStyle().setOpacity(0.5f);
		event.preventDefault();
	}
	@UiHandler("flower1")
	public void flower1Drop(DropEvent event) {
		flower1.getStyle().setVisibility(Visibility.HIDDEN);
		flower1.getStyle().setOpacity(1f);
		fruit1.getStyle().setVisibility(Visibility.VISIBLE);
		Timer t = new Timer() {
			@Override
			public void run() {
				flower1.getStyle().setVisibility(Visibility.VISIBLE);
				fruit1.getStyle().setVisibility(Visibility.HIDDEN);
			}
		};
		t.schedule(3000);
	}

	@UiHandler("flower2")
	public void flower2Enter(DragEnterEvent event) {
		flower2.getStyle().setOpacity(0.5f);
		event.preventDefault();
	}
	@UiHandler("flower2")
	public void flower2Leave(DragLeaveEvent event) {
		flower2.getStyle().setOpacity(1f);
	}
	@UiHandler("flower2")
	public void flower2Over(DragOverEvent event) {
		event.preventDefault();
	}
	@UiHandler("flower2")
	public void flower2Drop(DropEvent event) {
		flower2.getStyle().setVisibility(Visibility.HIDDEN);
		flower2.getStyle().setOpacity(1f);
		fruit2.getStyle().setVisibility(Visibility.VISIBLE);
		Timer t = new Timer() {
			@Override
			public void run() {
				flower2.getStyle().setVisibility(Visibility.VISIBLE);
				fruit2.getStyle().setVisibility(Visibility.HIDDEN);
			}
		};
		t.schedule(3000);
	}

	@UiHandler("flower3")
	public void flower3Enter(DragEnterEvent event) {
		flower3.getStyle().setOpacity(0.5f);
		event.preventDefault();
	}
	@UiHandler("flower3")
	public void flower3Leave(DragLeaveEvent event) {
		flower3.getStyle().setOpacity(1f);
	}
	@UiHandler("flower3")
	public void flower3Over(DragOverEvent event) {
		event.preventDefault();
	}
	@UiHandler("flower3")
	public void flower3Drop(DropEvent event) {
		flower3.getStyle().setVisibility(Visibility.HIDDEN);
		flower3.getStyle().setOpacity(1f);
		fruit3.getStyle().setVisibility(Visibility.VISIBLE);
		Timer t = new Timer() {
			@Override
			public void run() {
				flower3.getStyle().setVisibility(Visibility.VISIBLE);
				fruit3.getStyle().setVisibility(Visibility.HIDDEN);
			}
		};
		t.schedule(3000);
	}

	@UiHandler("bee")
	public void beeDragStart(DragStartEvent event) {
		event.getDataTransfer().setData("text/plain", "bzzz");
	}
}
