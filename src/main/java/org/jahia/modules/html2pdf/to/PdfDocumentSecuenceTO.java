package org.jahia.modules.html2pdf.to;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

/**
 * PdfDocumentSecuenceTO Class
 * Created by Juan Carlos Rodas
 */
public class PdfDocumentSecuenceTO {

    private PDPageContentStream contentStream;
    private PDPage page;
	private Float lineNumber;
    private Boolean newPage;


    /**
     * <code>PdfDocumentSecuenceTO</code>
     * PdfDocumentSecuenceTO Constructor
     *
     * @param iLineNumber    @int
     * @param iContentStream @PdPageContentStream
     * @param iNewPage       @boolean
     */
    public PdfDocumentSecuenceTO(Float iLineNumber, PDPageContentStream iContentStream, PDPage iPage, Boolean iNewPage) {
        this.lineNumber = iLineNumber;
        this.contentStream = iContentStream;
        this.page = iPage;
        this.newPage = iNewPage;
    }

    /**
     * <code>getContentStream</code>
     *
     * @return @PdPageContentStream
     */
    public PDPageContentStream getContentStream() {
        return contentStream;
    }

    /**
     * <code>setContentStream</code>
     *
     * @param contentStream @PDPageContentStream
     */
    public void setContentStream(PDPageContentStream contentStream) {
        this.contentStream = contentStream;
    }

    /**
     * <content>getLineNumber</content>
     *
     * @return @Float
     */
    public Float getLineNumber() {
        return lineNumber;
    }

    /**
     * <code>setLineNumber</code>
     *
     * @param lineNumber @Float
     */
    public void setLineNumber(Float lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * <code>isNewPage</code>
     *
     * @return @boolean
     */
    public boolean isNewPage() {
        return newPage;
    }

    /**
     * <code>setNewPage</code>
     *
     * @param isNewPage
     */
    public void setNewPage(boolean isNewPage) {
        this.newPage = isNewPage;
    }

    
    public PDPage getPage() {
  		return page;
  	}

  	/**
	 * Sets the page.
	 *
	 * @param page the new page
	 */
	public void setPage(PDPage page) {
  		this.page = page;
  	}
    
}
