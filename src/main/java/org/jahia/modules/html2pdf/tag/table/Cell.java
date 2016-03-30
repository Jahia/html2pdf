
package org.jahia.modules.html2pdf.tag.table;

import java.awt.Color;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.jahia.modules.html2pdf.text.WrappingFunction;
import org.jahia.modules.html2pdf.util.HorizontalAlignment;
import org.jahia.modules.html2pdf.util.PdfTextUtil;
import org.jahia.modules.html2pdf.util.VerticalAlignment;

/**
 * The Class Cell.
 *
 * @param <T> the generic type
 */
public class Cell<T extends PDPage> {

	/** The width. */
	private float width;
	
	/** The text. */
	private String text;

	/** The font. */
	private PDFont font = PDType1Font.HELVETICA;
	
	/** The font bold. */
	private PDFont fontBold = PDType1Font.HELVETICA_BOLD;

	/** The font size. */
	private float fontSize = 8;
	
	/** The fill color. */
	private Color fillColor;
	
	/** The text color. */
	private Color textColor = Color.BLACK;
	
	/** The row. */
	private final Row<T> row;
	
	/** The wrapping function. */
	private WrappingFunction wrappingFunction;
	
	/** The is header cell. */
	private boolean isHeaderCell = false;

	/** The left padding. */
	// default padding
	private float leftPadding = 5f;
	
	/** The right padding. */
	private float rightPadding = 5f;
	
	/** The top padding. */
	private float topPadding = 5f;
	
	/** The bottom padding. */
	private float bottomPadding = 5f;

	/** The paragraph. */
	private Paragraph paragraph = null;

	/** The align. */
	private final HorizontalAlignment align;
	
	/** The valign. */
	private final VerticalAlignment valign;

	/** The horizontal free space. */
	float horizontalFreeSpace = 0;
	
	/** The vertical free space. */
	float verticalFreeSpace = 0;

	/**
	 * <p>
	 * Constructs a cell with the default alignment
	 * {@link VerticalAlignment#TOP} {@link HorizontalAlignment#LEFT}.
	 * </p>
	 *
	 * @param row the row
	 * @param width the width
	 * @param text the text
	 * @param isCalculated the is calculated
	 * @see Cell#Cell(Row, float, String, boolean, HorizontalAlignment,
	 *      VerticalAlignment)
	 */
	Cell(Row<T> row, float width, String text, boolean isCalculated) {
		this(row, width, text, isCalculated, HorizontalAlignment.LEFT, VerticalAlignment.TOP);
	}

	/**
	 * <p>
	 * Constructs a cell.
	 * </p>
	 * 
	 * @param row
	 *            The parent row
	 * @param width
	 *            absolute width in points or in % of table width (depending on
	 *            the parameter {@code isCalculated})
	 * @param text
	 *            The text content of the cell
	 * @param isCalculated
	 *            If {@code true}, the width is interpreted in % to the table
	 *            width
	 * @param align
	 *            The {@link HorizontalAlignment} of the cell content
	 * @param valign
	 *            The {@link VerticalAlignment} of the cell content
	 * @see Cell#Cell(Row, float, String, boolean)
	 */
	Cell(Row<T> row, float width, String text, boolean isCalculated, HorizontalAlignment align,
			VerticalAlignment valign) {
		this.row = row;
		if (isCalculated) {
			double calclulatedWidth = ((row.getWidth() * width) / 100);
			this.width = (float) calclulatedWidth;
		} else {
			this.width = width;
		}

		if (getWidth() > row.getWidth()) {
			throw new IllegalArgumentException(
					"Cell Width=" + getWidth() + " can't be bigger than row width=" + row.getWidth());
		}
		this.text = text == null ? "" : PdfTextUtil.html2text(text);
		this.align = align;
		this.valign = valign;
		this.wrappingFunction = null;
	}

	/**
	 * Gets the text color.
	 *
	 * @return the text color
	 */
	public Color getTextColor() {
		return textColor;
	}

	/**
	 * Sets the text color.
	 *
	 * @param textColor the new text color
	 */
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	/**
	 * Gets the fill color.
	 *
	 * @return the fill color
	 */
	public Color getFillColor() {
		return fillColor;
	}

	/**
	 * Sets the fill color.
	 *
	 * @param fillColor the new fill color
	 */
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * Gets the inner width.
	 *
	 * @return the inner width
	 */
	public float getInnerWidth() {
		return getWidth() - getLeftPadding() - getRightPadding();
	}

	/**
	 * Gets the inner height.
	 *
	 * @return the inner height
	 */
	public float getInnerHeight() {
		return getHeight() - getBottomPadding() - getTopPadding();
	}

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 *
	 * @param text the new text
	 */
	public void setText(String text) {
		this.text = text;

		// paragraph invalidated
		paragraph = null;
	}

	/**
	 * Gets the font.
	 *
	 * @return the font
	 */
	public PDFont getFont() {
		if (font == null) {
			throw new IllegalArgumentException("Font not set.");
		}
		return font;
	}

	/**
	 * Sets the font.
	 *
	 * @param font the new font
	 */
	public void setFont(PDFont font) {
		this.font = font;

		// paragraph invalidated
		paragraph = null;
	}

	/**
	 * Gets the font size.
	 *
	 * @return the font size
	 */
	public float getFontSize() {
		return fontSize;
	}

	/**
	 * Sets the font size.
	 *
	 * @param fontSize the new font size
	 */
	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;

		// paragraph invalidated
		paragraph = null;
	}

	/**
	 * Gets the paragraph.
	 *
	 * @return the paragraph
	 */
	public Paragraph getParagraph() {
		if (paragraph == null) {
			if (isHeaderCell) {
				paragraph = new Paragraph(text, fontBold, fontSize, getInnerWidth(), align, textColor, null, wrappingFunction);
			} else {
				paragraph = new Paragraph(text, font, fontSize, getInnerWidth(), align, textColor, null, wrappingFunction);
			}
		}
		return paragraph;
	}

	/**
	 * Gets the extra width.
	 *
	 * @return the extra width
	 */
	public float getExtraWidth() {
		return this.row.getLastCellExtraWidth() + getWidth();
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public float getHeight() {
		return row.getHeight();
	}

	/**
	 * Gets the text height.
	 *
	 * @return the text height
	 */
	public float getTextHeight() {
		return getParagraph().getHeight();
	}

	/**
	 * Gets the left padding.
	 *
	 * @return the left padding
	 */
	public float getLeftPadding() {
		return leftPadding;
	}

	/**
	 * Sets the left padding.
	 *
	 * @param cellLeftPadding the new left padding
	 */
	public void setLeftPadding(float cellLeftPadding) {
		this.leftPadding = cellLeftPadding;

		// paragraph invalidated
		paragraph = null;
	}

	/**
	 * Gets the right padding.
	 *
	 * @return the right padding
	 */
	public float getRightPadding() {
		return rightPadding;
	}

	/**
	 * Sets the right padding.
	 *
	 * @param cellRightPadding the new right padding
	 */
	public void setRightPadding(float cellRightPadding) {
		this.rightPadding = cellRightPadding;

		// paragraph invalidated
		paragraph = null;
	}

	/**
	 * Gets the top padding.
	 *
	 * @return the top padding
	 */
	public float getTopPadding() {
		return topPadding;
	}

	/**
	 * Sets the top padding.
	 *
	 * @param cellTopPadding the new top padding
	 */
	public void setTopPadding(float cellTopPadding) {
		this.topPadding = cellTopPadding;
	}

	/**
	 * Gets the bottom padding.
	 *
	 * @return the bottom padding
	 */
	public float getBottomPadding() {
		return bottomPadding;
	}

	/**
	 * Sets the bottom padding.
	 *
	 * @param cellBottomPadding the new bottom padding
	 */
	public void setBottomPadding(float cellBottomPadding) {
		this.bottomPadding = cellBottomPadding;
	}

	/**
	 * Gets the vertical free space.
	 *
	 * @return the vertical free space
	 */
	public float getVerticalFreeSpace() {
		return getInnerHeight() - getTextHeight();
	}

	/**
	 * Gets the horizontal free space.
	 *
	 * @return the horizontal free space
	 */
	public float getHorizontalFreeSpace() {
		float tw = 0.0f;
		try {
			for (final String line : getParagraph().getLines()) {
				tw = Math.max(tw, getFont().getStringWidth(line.trim()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		tw = tw / 1000 * getFontSize();
		return getInnerWidth() - tw;
	}

	/**
	 * Gets the align.
	 *
	 * @return the align
	 */
	public HorizontalAlignment getAlign() {
		return align;
	}

	/**
	 * Gets the valign.
	 *
	 * @return the valign
	 */
	public VerticalAlignment getValign() {
		return valign;
	}

	/**
	 * Checks if is header cell.
	 *
	 * @return true, if is header cell
	 */
	public boolean isHeaderCell() {
		return isHeaderCell;
	}

	/**
	 * Sets the header cell.
	 *
	 * @param isHeaderCell the new header cell
	 */
	public void setHeaderCell(boolean isHeaderCell) {
		this.isHeaderCell = isHeaderCell;
	}

	/**
	 * Gets the wrapping function.
	 *
	 * @return the wrapping function
	 */
	public WrappingFunction getWrappingFunction() {
		return getParagraph().getWrappingFunction();
	}

	/**
	 * Sets the wrapping function.
	 *
	 * @param wrappingFunction the new wrapping function
	 */
	public void setWrappingFunction(WrappingFunction wrappingFunction) {
		this.wrappingFunction = wrappingFunction;

		// paragraph invalidated
		paragraph = null;
	}

	/**
	 * Gets the font bold.
	 *
	 * @return the font bold
	 */
	public PDFont getFontBold() {
		return fontBold;
	}

}
