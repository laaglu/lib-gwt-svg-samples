package org.vectomatic.svg.samples.client.smil;

import org.vectomatic.dom.svg.OMSVGAnimateElement;
import org.vectomatic.dom.svg.OMSVGCircleElement;
import org.vectomatic.dom.svg.events.RepeatEvent;
import org.vectomatic.dom.svg.events.RepeatHandler;
import org.vectomatic.svg.samples.client.SampleBase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SmilSample extends SampleBase implements RepeatHandler {
	interface SmilSampleBinder extends UiBinder<SimplePanel, SmilSample> {
	}
	private static SmilSampleBinder binder = GWT.create(SmilSampleBinder.class);

	
	@UiField
	VerticalPanel vpanel;
	@UiField
	OMSVGCircleElement circle;
	@UiField
	Label loopCount;

	@Override
	public Panel getPanel() {
		if (panel == null) {
			panel = binder.createAndBindUi(this);
			tabPanel.getTabBar().setTabText(0, "Animation");
			loadSampleCode("SmilSample");
			
			// Add the repeat handler manually instead of using the @UiHandler annotation
			// Indeed, since many browsers do not yet support the SVG anim tag, the
			// anim variable may well be null
			OMSVGAnimateElement anim = (OMSVGAnimateElement)circle.getFirstChild();
			if (anim != null) {
				anim.addRepeatHandler(this);
			}
		}
		return panel;
	}
	
	public void onRepeat(RepeatEvent e) {
		int count = Integer.parseInt(loopCount.getText());
		loopCount.setText(Integer.toString(count + 1));
	}

}
