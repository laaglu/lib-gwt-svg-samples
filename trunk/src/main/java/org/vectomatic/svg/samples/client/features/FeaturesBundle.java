package org.vectomatic.svg.samples.client.features;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface FeaturesBundle extends ClientBundle {
	public static final FeaturesBundle INSTANCE =  GWT.create(FeaturesBundle.class);
	@Source("features.css")
	public FeaturesCss getCss();

}
