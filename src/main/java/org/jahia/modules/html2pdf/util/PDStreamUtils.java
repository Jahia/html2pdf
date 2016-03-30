package org.jahia.modules.html2pdf.util;

import java.awt.Color;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class PDStreamUtils.
 */
public final class PDStreamUtils {

	/* the logger */
	private static Logger logger = LoggerFactory.getLogger(PDStreamUtils.class);

	/**
	 * Instantiates a new PD stream utils.
	 */
	private PDStreamUtils() {}

	/**
	 * <p>
	 * Provides ability to write on a {@link PDPageContentStream}. The text will be written above Y coordinate.
	 * </p>
	 * 
	 * @param stream
	 *            The {@link PDPageContentStream} where writing will be applied.
	 * @param text
	 *            The text which will be displayed.
	 * @param font The font of the text
	 * @param fontSize The font size of the text
	 * @param x
	 *            Start X coordinate for text.
	 * @param y
	 *            Start Y coordinate for text.
	 * @param color
	 *            Color of the text
	 */
	public static void write(final PDPageContentStream stream, final String text, final PDFont font,
			final float fontSize, final float x, final float y, final Color color) {
		try {
			stream.beginText();
			stream.setFont(font, fontSize);
			// we want to position our text on his baseline
			stream.newLineAtOffset(x, y - PdfFontUtil.getDescent(font, fontSize) - PdfFontUtil.getHeight(font, fontSize));
			stream.setNonStrokingColor(color);
			stream.showText(text);
			stream.endText();
		} catch (final IOException e) {
			logger.error("write: Unable to write text, {}", e.getLocalizedMessage());
		}
	}

	/**
	 * <p>
	 * Provides ability to draw rectangle for debugging purposes.
	 * </p>
	 * 
	 * @param stream
	 *            The {@link PDPageContentStream} where drawing will be applied.
	 * @param x
	 *            Start X coordinate for rectangle.
	 * @param y
	 *            Start Y coordinate for rectangle.
	 * @param width
	 *            Width of rectangle
	 * @param height
	 *            Height of rectangle
	 * @param color
	 *            Color of the text
	 */
	public static void rect(final PDPageContentStream stream, final float x, final float y, final float width,
			final float height, final Color color) {
		try {
			stream.setNonStrokingColor(color);
			// negative height because we want to draw down (not up!)
			stream.addRect(x, y, width, -height);
			stream.fill();
			stream.closePath();

			// Reset NonStroking Color to default value
			stream.setNonStrokingColor(Color.BLACK);
		} catch (final IOException e) {
			logger.error("rect: Unable to draw rectangle, {}", e.getLocalizedMessage());
		}
	}
	
	/**
	 * Rect font metrics.
	 *
	 * @param stream the stream
	 * @param x the x
	 * @param y the y
	 * @param font the font
	 * @param fontSize the font size
	 */
	public static void rectFontMetrics(final PDPageContentStream stream, final float x, final float y, final PDFont font, final float fontSize) {
		// height
		PDStreamUtils.rect(stream, x, y, 3, PdfFontUtil.getHeight(font, fontSize), Color.BLUE);
		// ascent
		PDStreamUtils.rect(stream, x+3, y, 3, PdfFontUtil.getAscent(font, fontSize), Color.CYAN);
		// descent
		PDStreamUtils.rect(stream, x+3, y - PdfFontUtil.getHeight(font, fontSize), 3, PdfFontUtil.getDescent(font, 14), Color.GREEN);
	}

	/**
	 * Add text to pdf
	 *
	 * @param document the pdf document
	 * @param contentStream the pd page content stream
	 * @param xInitialPosition the x initial position
	 * @param currentLine the y current line
	 * @param text the text
	 * @param font the font
	 * @param fontSize the font size
	 * @param lineTextWidth the line text width
	 * @param colorR color r
	 * @param colorG color g
     * @param colorB color b
     * @return the next line
     * @throws IOException
     */
	public static Float addTextToPdf(PDDocument document, PDPageContentStream contentStream, Float xInitialPosition, Float currentLine, String text, PDFont font, Float fontSize, Float lineTextWidth, int colorR, int colorG, int colorB) throws IOException {
		if (StringUtils.isNotEmpty(text)) {
            for(String lineContent: PdfTextUtil.getLinesByTextLenght(fontSize, font , text, lineTextWidth, false)){
				contentStream.beginText();
				contentStream.setFont(font, fontSize);
				contentStream.setNonStrokingColor(colorR, colorG, colorB);
				contentStream.newLineAtOffset(xInitialPosition, currentLine);
				contentStream.showText(lineContent);
				contentStream.newLine();
				contentStream.endText();
                currentLine -= (fontSize * PdfFontUtil.getFontSpaceFactor());
			}
		}
		return currentLine;
	}

	/**
	 * Add text to pdf
	 *
	 * @param document the pd document
	 * @param contentStream the pd page content stream
	 * @param currentLine the current y line
	 * @param text the text
	 * @param font the font
	 * @param fontSize the font size
	 * @param lineTextWidth the line text width
	 * @param colorR the color r
	 * @param colorG the color g
     * @param colorB the color b
     * @return the current y line
     * @throws IOException
     */
	public static Float addTextToPdf(PDDocument document, PDPageContentStream contentStream, Float currentLine, String text, PDFont font, Float fontSize, Float lineTextWidth, int colorR, int colorG, int colorB) throws IOException {
		return addTextToPdf(document, contentStream,  PdfPageUtil.getPageStartX(), currentLine, text, font, fontSize, lineTextWidth, colorR, colorG, colorB);
	}

}
