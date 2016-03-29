package org.jahia.modules.html2pdf.page;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

/**
 * The Class DefaultPageProvider.
 */
public class DefaultPageProvider implements PageProvider<PDPage> {
	
	/** The document. */
	private final PDDocument document;
	
	/** The size. */
	private final PDRectangle size;

	/**
	 * Instantiates a new default page provider.
	 *
	 * @param document the document
	 * @param size the size
	 */
	public DefaultPageProvider(final PDDocument document, final PDRectangle size) {
		this.document = document;
		this.size = size;
	}

	/* (non-Javadoc)
	 * @see org.jahia.modules.html2pdf.page.PageProvider#createPage()
	 */
	@Override
	public PDPage createPage() {
		final PDPage newPage = new PDPage(size);
        document.addPage(newPage);
        return newPage;
	}

	/* (non-Javadoc)
	 * @see org.jahia.modules.html2pdf.page.PageProvider#getDocument()
	 */
	@Override
	public PDDocument getDocument() {
		return document;
	}

}
