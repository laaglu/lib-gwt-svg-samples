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
package org.vectomatic.svg.samples.client.xpath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.vectomatic.dom.svg.impl.Attr;
import org.vectomatic.dom.svg.impl.NamedNodeMap;
import org.vectomatic.dom.svg.ui.SVGImage;
import org.vectomatic.dom.svg.ui.SVGResource;
import org.vectomatic.dom.svg.ui.SVGResource.Validated;
import org.vectomatic.dom.svg.utils.DOMHelper;
import org.vectomatic.dom.svg.utils.SVGConstants;
import org.vectomatic.dom.svg.utils.SVGPrefixResolver;
import org.vectomatic.svg.samples.client.Main;
import org.vectomatic.svg.samples.client.Main.MainBundle;
import org.vectomatic.svg.samples.client.SampleBase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.PreElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Text;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Class to demonstrate the use of XPaths to navigate in SVG documents
 * @author laaglu
 */
public class XPathSample extends SampleBase {
	interface XmlCssResource extends CssResource {
		public String element();
		public String attribute();
		public String text();
		public String comment();
		public String markup();
		public String selected();
		public String root();
	}
	interface XPathBundle extends ClientBundle {
		public static XPathBundle INSTANCE = GWT.create(XPathBundle.class);
		@Source("xml.css")
		public XmlCssResource css();
		@Validated(validated=false)
		@Source("pizza_pepperoni_bw.svg")
		public SVGResource pizza();
	}
	
	interface XPathSampleBinder extends UiBinder<TabLayoutPanel, XPathSample> {
	}
	private static XPathSampleBinder binder = GWT.create(XPathSampleBinder.class);

	@UiField(provided=true)
	public static XPathBundle xpathBundle = XPathBundle.INSTANCE;	
	@UiField(provided=true)
	public static MainBundle mainBundle = Main.mainBundle;
	/**
	 * A Text box to enter XPath expressions
	 */
	@UiField
	TextBox xpathBox;
	/**
	 * A button to request evaluation of the xpath expression
	 */
	@UiField
	Button evaluateButton;
	/**
	 * Checkbox to request continuous evaluation of xpath expressions
	 * as they are typed
	 */
	@UiField
	CheckBox evaluateCheckBox;
	/**
	 * A label to display XPath parsing error message
	 */
	@UiField
	Label errorLabel;
	/**
	 * A sample SVG image
	 */
	@UiField
	SVGImage svgImage;
	/**
	 * A colorized version of the SVG source XML text
	 */
	@UiField
	HTML xmlContainer;
	
	/**
	 * The root document
	 */
	private Document doc = Document.get();
	/**
	 * The CSS used to colorize text
	 */
	private XmlCssResource css = xpathBundle.css();
	/**
	 * Maps SVG nodes to bits of XML colorized text
	 */
	private Map<Node, SpanElement> nodeToSpan = new HashMap<Node, SpanElement>();
	/**
	 * The bits of XML colorized text corresponding to the SVG
	 * nodes selected by the XPath expression
	 */
	private List<Element> xpathSpans;
	/**
	 * The SVG elements selected by the XPath expression
	 */
	private List<Element> svgElements;
	/**
	 * To resolve namespace prefixes
	 */
	private SVGPrefixResolver resolver;

	@Override
	public TabLayoutPanel getPanel() {
		if (tabPanel == null) {
			tabPanel = binder.createAndBindUi(this);
			tabPanel.setTabText(0, "XPath");
			createCodeTabs("XPathSample");
			
			css.ensureInjected();
			xpathSpans = new ArrayList<Element>();
			svgElements = new ArrayList<Element>();
			PreElement pre = doc.createPreElement();
			visit(svgImage.getElement(), pre);
			pre.addClassName(css.root());
			xmlContainer.getElement().appendChild(pre);
			resolver = new SVGPrefixResolver() {
				{
					prefixToUri.put("i", "http://ns.adobe.com/AdobeIllustrator/10.0/");
					prefixToUri.put("cc", "http://web.resource.org/cc/");
					prefixToUri.put("dc", "http://purl.org/dc/elements/1.1/");
					prefixToUri.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
				}
			};
			evaluate();
		}
		return tabPanel;
	}
	
	@UiHandler("evaluateButton")
	public void evaluate(ClickEvent event) {
		evaluate();
	}

	@UiHandler("evaluateCheckBox") 
	public void checkbox(ClickEvent event) {
		evaluateButton.setEnabled(!evaluateCheckBox.getValue());
	}
	
	@UiHandler("xpathBox") 
	public void keyUp(KeyUpEvent event) {
		if (evaluateCheckBox.getValue()) {
			evaluate();
		}
	}
	
	public void evaluate() {
		String error = "";
		try {
			for (Element span : xpathSpans) {
				span.removeClassName(css.selected());
			}
			xpathSpans.clear();
			for (Element svgElement : svgElements) {
				svgElement.setAttribute(SVGConstants.SVG_FILL_ATTRIBUTE, "#FFFFFF");
			}
			svgElements.clear();
			
			String expr = xpathBox.getText();
			GWT.log(expr);
			Iterator<Node> iterator = DOMHelper.evaluateNodeListXPath(svgImage.getElement(), expr, resolver);
			while (iterator.hasNext()) {
				Node node = iterator.next();
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					svgElements.add(node.<Element>cast());
				}
				Element span = nodeToSpan.get(node);
				xpathSpans.add(span);
			}
			for (Element span : xpathSpans) {
				span.addClassName(css.selected());
			}
			for (Element svgElement : svgElements) {
				svgElement.setAttribute(SVGConstants.SVG_FILL_ATTRIBUTE, "green");
			}
		} catch(Throwable t) {
			error = t.getMessage();
		}	
		errorLabel.setText(error);
	}

	private SpanElement createMarkup(String markup) {
		SpanElement markupSpan = doc.createSpanElement();
		markupSpan.addClassName(css.markup());
		markupSpan.appendChild(doc.createTextNode(markup));
		return markupSpan;
	}
	
	private void visit(Node node, Element parentSpan) {
		SpanElement span = doc.createSpanElement();
		nodeToSpan.put(node, span);
		
		parentSpan.appendChild(span);
		NodeList<Node> childNodes = node.getChildNodes();
		switch(node.getNodeType()) {
			case Node.ELEMENT_NODE:
				{
					Element element = node.<Element>cast();
					span.addClassName(css.element());
					
					// Populate the span with a start tag
					span.appendChild(createMarkup("<"));
					String tagName = element.getTagName();
					int index = tagName.indexOf(":");
					if (index != -1) {
						span.appendChild(doc.createTextNode(tagName.substring(0, index)));
						span.appendChild(createMarkup(":"));
						span.appendChild(doc.createTextNode(tagName.substring(index + 1)));
					} else {
						span.appendChild(doc.createTextNode(tagName));
					}

					// Create the attribute nodes spans
					NamedNodeMap<Attr> attributes = DOMHelper.getAttributes(element);
					for (int i = 0, length = attributes.getLength(); i < length; i++) {
						span.appendChild(doc.createTextNode(" "));

						SpanElement attrSpan = doc.createSpanElement();
						attrSpan.addClassName(css.attribute());
						span.appendChild(attrSpan);
						Attr attr = attributes.item(i);
						nodeToSpan.put(attr, attrSpan);
						
						String attrName = attr.getName();
						index = attrName.indexOf(":");
						if (index != -1) {
							attrSpan.appendChild(doc.createTextNode(attrName.substring(0, index)));
							attrSpan.appendChild(createMarkup(":"));
							attrSpan.appendChild(doc.createTextNode(attrName.substring(index + 1)));
						} else {
							attrSpan.appendChild(doc.createTextNode(attrName));
						}
						attrSpan.appendChild(createMarkup("=\""));
						attrSpan.appendChild(doc.createTextNode(attr.getValue()));
						attrSpan.appendChild(createMarkup("\""));
					}
					span.appendChild(createMarkup(childNodes.getLength() > 0 ? ">" : "/>"));
				}
				break;
			case Node.TEXT_NODE:
				{
					// Populate span with text
					Text text = node.<Text>cast();
					span.addClassName(css.text());
					span.appendChild(doc.createTextNode(text.getData()));
				}
				break;
		}

		for (int i = 0, count = node.getChildCount(); i < count; i++) {
			visit(childNodes.getItem(i), span);
		}
		
		if (childNodes.getLength() > 0 && node.getNodeType() == Node.ELEMENT_NODE) {
			Element element = node.<Element>cast();
			span.addClassName(css.element());
			
			// Populate the span with a close tag
			span.appendChild(createMarkup("</"));
			String tagName = element.getTagName();
			int index = tagName.indexOf(":");
			if (index != -1) {
				span.appendChild(doc.createTextNode(tagName.substring(0, index)));
				span.appendChild(createMarkup(":"));
				span.appendChild(doc.createTextNode(tagName.substring(index + 1)));
			} else {
				span.appendChild(doc.createTextNode(tagName));
			}
			span.appendChild(createMarkup(">"));
		}
	}

}
