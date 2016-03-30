package org.jahia.modules.html2pdf.tag.table;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.jahia.modules.html2pdf.page.PageProvider;


/**
 * The Class AbstractTemplatedTable.
 *
 * @param <T> the generic type
 */
public abstract class AbstractTemplatedTable<T extends AbstractPageTemplate> extends Table<T> {

    /**
     * Instantiates a new abstract templated table.
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
     * @param pageProvider the page provider
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public AbstractTemplatedTable(float yStart, float yStartNewPage, float bottomMargin, float width, float margin, PDDocument document, T currentPage, boolean drawLines, boolean drawContent, PageProvider<T> pageProvider) throws IOException {
        super(yStart, yStartNewPage, 0, bottomMargin, width, margin, document, currentPage, drawLines, drawContent, pageProvider);
    }

    /**
     * Instantiates a new abstract templated table.
     *
     * @param yStartNewPage the y start new page
     * @param bottomMargin the bottom margin
     * @param width the width
     * @param margin the margin
     * @param document the document
     * @param drawLines the draw lines
     * @param drawContent the draw content
     * @param pageProvider the page provider
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public AbstractTemplatedTable(float yStartNewPage, float bottomMargin, float width, float margin, PDDocument document, boolean drawLines, boolean drawContent, PageProvider<T> pageProvider) throws IOException {
        super(yStartNewPage, 0, bottomMargin, width, margin, document, drawLines, drawContent, pageProvider);
        setYStart(getCurrentPage().yStart());
    }

}
