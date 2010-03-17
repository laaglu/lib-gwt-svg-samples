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
package org.vectomatic.svg.samples.generator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;

import org.vectomatic.svg.samples.client.SampleBase;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;

import de.java2html.converter.IJavaSourceConverter;
import de.java2html.converter.JavaSource2HTMLConverter;
import de.java2html.javasource.JavaSourceParser;
import de.java2html.options.JavaSourceConversionOptions;

public class SourceGenerator extends Generator {
	/**
	 * The class loader used to get resources.
	 */
	private ClassLoader classLoader = null;

	/**
	 * The generator context.
	 */
	private GeneratorContext context = null;

	/**
	 * The {@link TreeLogger} used to log messages.
	 */
	private TreeLogger logger = null;
	
	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
	    this.logger = logger;
	    this.context = context;
	    this.classLoader = Thread.currentThread().getContextClassLoader();

	    // Only generate files on the first permutation
	    if (!isFirstPass()) {
	      return null;
	    }

		JClassType sampleClass = null;
		try {
			sampleClass = context.getTypeOracle().getType(
					SampleBase.class.getName());
		} catch (NotFoundException e) {
			logger.log(TreeLogger.ERROR, "Cannot find SampleBase class", e);
			throw new UnableToCompleteException();
		}
		JClassType[] types = sampleClass.getSubtypes();

		// Generate the source and raw source files
		for (JClassType type : types) {
			generateSourceFile(type);
		}
		return null;
	}

	private void generateSourceFile(JClassType type) throws UnableToCompleteException {
		JavaSourceConversionOptions options = JavaSourceConversionOptions.getDefault();
		IJavaSourceConverter converter = new JavaSource2HTMLConverter() {
			public java.lang.String getDocumentHeader(JavaSourceConversionOptions options, java.lang.String title) {
				return "";
			};

			public java.lang.String getDocumentFooter(JavaSourceConversionOptions options) {
				return "";
			}
		};
		StringWriter stringWriter = new StringWriter();
		JavaSourceParser parser = new JavaSourceParser(options);
	    String filename = type.getQualifiedSourceName().replace('.', '/') + ".java";
	    String fileContents = getResourceContents(filename);
		de.java2html.javasource.JavaSource source = parser.parse(fileContents);
		try {
			converter.convert(source, options, stringWriter);
		    // Save the source code to a file
		    String dstPath = SampleBase.HTML_SRC_DIR + type.getSimpleSourceName() + ".html";
		    createPublicResource(dstPath, stringWriter.toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Set the full contents of a resource in the public directory.
	 * @param partialPath
	 * the path to the file relative to the public directory
	 * @param contents
	 * the file contents
	 */
	private void createPublicResource(String partialPath, String contents) {
		try {
			logger.log(TreeLogger.INFO, "Generating " + partialPath);
			OutputStream outStream = context.tryCreateResource(logger, partialPath);
			outStream.write(contents.getBytes());
			context.commitResource(logger, outStream);
		} catch (UnableToCompleteException e) {
			logger.log(TreeLogger.ERROR, "Failed while writing", e);
		} catch (IOException e) {
			logger.log(TreeLogger.ERROR, "Failed while writing", e);
		}
	}

	  /**
	   * Get the full contents of a resource.
	   * @param path the path to the resource
	   * @return the contents of the resource
	   */
	private String getResourceContents(String path) throws UnableToCompleteException {
		InputStream in = classLoader.getResourceAsStream(path);
		if (in == null) {
			logger.log(TreeLogger.ERROR, "Resource not found: " + path);
			throw new UnableToCompleteException();
		}

		StringBuffer fileContentsBuf = new StringBuffer();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(in));
			String temp;
			while ((temp = br.readLine()) != null) {
				fileContentsBuf.append(temp).append('\n');
			}
		} catch (IOException e) {
			logger.log(TreeLogger.ERROR, "Cannot read resource", e);
			throw new UnableToCompleteException();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}

		// Return the file contents as a string
		return fileContentsBuf.toString();
	}
	

	  /**
	   * Ensure that we only generate files once by creating a placeholder file,
	   * then looking for it on subsequent generates.
	   * 
	   * @return true if this is the first pass, false if not
	   */
	  private boolean isFirstPass() {
	    String placeholder = SampleBase.HTML_SRC_DIR + "generated";
	    try {
	      OutputStream outStream = context.tryCreateResource(logger, placeholder);
	      if (outStream == null) {
	        return false;
	      } else {
	        context.commitResource(logger, outStream);
	      }
	    } catch (UnableToCompleteException e) {
	      logger.log(TreeLogger.ERROR, "Unable to generate", e);
	      return false;
	    }
	    return true;
	  }

}
