package org.vectomatic.svg.samples.client.smil;

import org.vectomatic.dom.svg.OMSVGAnimateElement;
import org.vectomatic.dom.svg.OMSVGCircleElement;
import org.vectomatic.dom.svg.OMSVGSVGElement;
import org.vectomatic.dom.svg.events.RepeatEvent;
import org.vectomatic.dom.svg.events.RepeatHandler;
import org.vectomatic.svg.samples.client.Main;
import org.vectomatic.svg.samples.client.Main.MainBundle;
import org.vectomatic.svg.samples.client.SampleBase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SmilSample extends SampleBase implements RepeatHandler {
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
	Label loopCount;

	@Override
	public TabLayoutPanel getPanel() {
		if (tabPanel == null) {
			tabPanel = binder.createAndBindUi(this);
			tabPanel.setTabText(0, "Animation");
			createCodeTabs("SmilSample");
			
			// Add the repeat handler manually instead of using the @UiHandler annotation
			// Indeed, since many browsers do not yet support the SVG anim tag, the
			// anim variable may well be null
			OMSVGAnimateElement anim = (OMSVGAnimateElement)circle.getFirstChild();
			if (anim != null) {
				anim.addRepeatHandler(this);
			}
		}
		return tabPanel;
	}
	
	public void onRepeat(RepeatEvent e) {
		int count = Integer.parseInt(loopCount.getText());
		loopCount.setText(Integer.toString(count + 1));
	}

	@Override
	protected void resize(int width, int height) {
		GWT.log(width + " " + height);
		if (svg != null) {
			svg.getStyle().setWidth(width, Unit.PX);
			svg.getStyle().setHeight(height, Unit.PX);
		}
	}

}
