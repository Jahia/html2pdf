package org.jahia.modules.html2pdf.util;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.jahia.modules.html2pdf.config.ConfigurationMap;
import org.jahia.modules.html2pdf.to.PdfDocumentSecuenceTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class PdfPageUtil. 
 * Created by Juan Carlos Rodas
 */
public class PdfPageUtil {

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(PdfPageUtil.class);

	/**
	 * Gets the page width x.
	 *
	 * @return the page width x
	 */
	public static Float getPageWidthX() {
		return new Float(ConfigurationMap.getProperty("page.width"));
	}

	/**
	 * Gets the page height y.
	 *
	 * @return the page height y
	 */
	public static Float getPageHeightY() {
		return new Float(ConfigurationMap.getProperty("page.height"));
	}

	/**
	 * Gets the page margin x.
	 *
	 * @return the page margin x
	 */
	public static Float getPageMarginX() {
		return new Float(ConfigurationMap.getProperty("page.margin.x"));
	}

	/**
	 * Gets the page margin y.
	 *
	 * @return the page margin y
	 */
	public static Float getPageMarginY() {
		return new Float(ConfigurationMap.getProperty("page.margin.y"));
	}

	/**
	 * Gets the line width x.
	 *
	 * @return the line width x
	 */
	public static Float getLineWidthX() {
		return (getPageWidthX() - (getPageMarginX() * 2));
	}

	/**
	 * Gets the line height y.
	 *
	 * @return the line height y
	 */
	public static Float getLineHeightY() {
		return (getPageHeightY() - (getPageMarginY() * 2));
	}

	/**
	 * Gets the page start x.
	 *
	 * @return the page start x
	 */
	public static Float getPageStartX() {
		return getPageMarginX();
	}

	/**
	 * Gets the page end x.
	 *
	 * @return the page end x
	 */
	public static Float getPageEndX() {
		return (getPageWidthX() - getPageMarginX());
	}

	/**
	 * Gets the page start y.
	 *
	 * @return the page start y
	 */
	public static Float getPageStartY() {
		return (getPageHeightY() - getPageMarginY());
	}

	/**
	 * Gets the page end y.
	 *
	 * @return the page end y
	 */
	public static Float getPageEndY() {
		return getPageMarginY();
	}

	/**
	 * Checks if is end of page x.
	 *
	 * @param xPosition the x position
	 * @return the boolean
	 */
	public static Boolean isEndOfPageX(Float xPosition) {
		return xPosition > getPageEndX();
	}

	/**
	 * Checks if is end of page y.
	 *
	 * @param yPosition the y position
	 * @return the boolean
	 */
	public static Boolean isEndOfPageY(Float yPosition) {
		return yPosition < getPageEndY();
	}

    /**
     * <code>documentSecuenceVerification</code>
     * Get the current line and the contentStream
     * of the current page if not is the end of page,
     * otherwise get the new line and new contentSrream
     * from a new page.
     *
     * @param document @PDDocument
     * @param contentStream @PDPageContentStream
     * @param currentLine Float
     * @return @PdfDocumentSecuenceTO
     * @throws IOException
     */
    public static PdfDocumentSecuenceTO documentEopVerification(PDDocument document, PDPage page, PDPageContentStream contentStream, Float currentLine) throws IOException {
        if (isEndOfPageY(Float.valueOf(currentLine))) {
            contentStream.close();
            page = new PDPage();
            document.addPage(page);
            contentStream = new PDPageContentStream(document, page, true, true);
			PdfPageUtil.fillBackgroundColorPage(contentStream);
            currentLine =  getPageStartY();
            return new PdfDocumentSecuenceTO(currentLine, contentStream, page, true);
        }
        return new PdfDocumentSecuenceTO(currentLine, contentStream, page, false);
    }



	public static void fillBackgroundColorPage(PDPageContentStream contentStream){
		logger.debug("fillBackgroundColorPage: Setting the page background color.");
		try{
			String pageBgColor =  ConfigurationMap.getProperty("page.bg.color");
			if(pageBgColor != null) {

				contentStream.setNonStrokingColor(PdfColorUtil.getColorByHex(pageBgColor));
				contentStream.addRect(0, 0, PdfPageUtil.getPageWidthX(), PdfPageUtil.getPageHeightY());
				contentStream.fill();
			}else{
				logger.debug("fillBackgroundColorPage: There is not property for page background color.");
			}
		}catch(Exception e){
			logger.error("fillBackgroundColorPage: Can not set the background color to the page, {}.", e.getLocalizedMessage());
		}
	}


}
