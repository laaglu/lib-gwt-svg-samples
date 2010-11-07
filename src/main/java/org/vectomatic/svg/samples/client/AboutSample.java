package org.vectomatic.svg.samples.client;

import org.vectomatic.svg.samples.client.Main.MainBundle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TabLayoutPanel;

public class AboutSample extends SampleBase {
	interface AboutSampleSampleBinder extends UiBinder<TabLayoutPanel, AboutSample> {
	}
	private static AboutSampleSampleBinder binder = GWT.create(AboutSampleSampleBinder.class);
	@UiField(provided=true)
	public static MainBundle mainBundle = Main.mainBundle;

	@Override
	public TabLayoutPanel getPanel() {
		if (tabPanel == null) {
			tabPanel = binder.createAndBindUi(this);
		}
		return tabPanel;
	}

}
