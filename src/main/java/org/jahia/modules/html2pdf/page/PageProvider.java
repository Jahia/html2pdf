package org.jahia.modules.html2pdf.page;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

/**
 * The Interface PageProvider.
 *
 * @param <T> the generic type
 */
public interface PageProvider<T extends PDPage> {

	/**
	 * Creates the page.
	 *
	 * @return the t
	 */
	T createPage();
	
	/**
	 * Gets the document.
	 *
	 * @return the document
	 */
	PDDocument getDocument();
}
