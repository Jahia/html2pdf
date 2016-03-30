package org.jahia.modules.html2pdf.tag.table;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.jahia.modules.html2pdf.page.DefaultPageProvider;
import org.jahia.modules.html2pdf.page.PageProvider;

/**
 * The Class BaseTable.
 */
public class BaseTable extends Table<PDPage> {

    /**
     * Instantiates a new base table.
     *
     * @param yStart the y start
     * @param yStartNewPage the y start new page
     * @param bottomMargin the bottom margin
     * @param width the width
     * @param margin the margin
     * @param document the document
     * @param currentPage the current page
     * @param drawLines the draw lines
     * @param drawContent the draw content
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public BaseTable(float yStart, float yStartNewPage, float bottomMargin, float width, float margin, PDDocument document, PDPage currentPage, boolean drawLines, boolean drawContent) throws IOException {
        super(yStart, yStartNewPage, 0, bottomMargin, width, margin, document, currentPage, drawLines, drawContent, new DefaultPageProvider(document, currentPage.getMediaBox()));
    }
    
    /**
     * Instantiates a new base table.
     *
     * @param yStart the y start
     * @param yStartNewPage the y start new page
     * @param pageTopMargin the page top margin
     * @param bottomMargin the bottom margin
     * @param width the width
     * @param margin the margin
     * @param document the document
     * @param currentPage the current page
     * @param drawLines the draw lines
     * @param drawContent the draw content
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public BaseTable(float yStart, float yStartNewPage, float pageTopMargin, float bottomMargin, float width, float margin, PDDocument document, PDPage currentPage, boolean drawLines, boolean drawContent) throws IOException {
        super(yStart, yStartNewPage, pageTopMargin, bottomMargin, width, margin, document, currentPage, drawLines, drawContent, new DefaultPageProvider(document, currentPage.getMediaBox()));
    }
    
    /**
     * Instantiates a new base table.
     *
     * @param yStart the y start
     * @param yStartNewPage the y start new page
     * @param pageTopMargin the page top margin
     * @param bottomMargin the bottom margin
     * @param width the width
     * @param margin the margin
     * @param document the document
     * @param currentPage the current page
     * @param drawLines the draw lines
     * @param drawContent the draw content
     * @param pageProvider the page provider
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public BaseTable(float yStart, float yStartNewPage, float pageTopMargin, float bottomMargin, float width, float margin, PDDocument document, PDPage currentPage, boolean drawLines, boolean drawContent, final PageProvider<PDPage> pageProvider) throws IOException {
        super(yStart, yStartNewPage, pageTopMargin, bottomMargin, width, margin, document, currentPage, drawLines, drawContent, pageProvider);
    }

    /* (non-Javadoc)
     * @see org.jahia.modules.html2pdf.tag.table.Table#loadFonts()
     */
    @Override
    protected void loadFonts() {
        // Do nothing as we don't have any fonts to load
    }

}
