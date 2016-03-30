package org.jahia.modules.html2pdf.tag.table;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.jahia.modules.html2pdf.text.WrappingFunction;
import org.jahia.modules.html2pdf.util.PdfFontUtil;
import org.jahia.modules.html2pdf.util.HorizontalAlignment;
import org.jahia.modules.html2pdf.util.PDStreamUtils;
import org.jahia.modules.html2pdf.util.TextType;

/**
 * The Class Paragraph.
 */
public class Paragraph {

	/** The width. */
	private float width = 500;
	
	/** The text. */
	private String text;
	
	/** The font size. */
	private float fontSize;
	
	/** The font. */
	private PDFont font;
	
	/** The wrapping function. */
	private final WrappingFunction wrappingFunction;
	
	/** The align. */
	private HorizontalAlignment align;
	
	/** The text type. */
	private TextType textType;

	/** The color. */
	private Color color;

	/** The draw debug. */
	private boolean drawDebug;
	
	/**
	 * Instantiates a new paragraph.
	 *
	 * @param text the text
	 * @param font the font
	 * @param fontSize the font size
	 * @param width the width
	 * @param align the align
	 */
	public Paragraph(String text, PDFont font, float fontSize, float width, final HorizontalAlignment align) {
		this(text, font, fontSize, width, align, null);
	}

	/** The Constant DEFAULT_WRAP_FUNC. */
	private static final WrappingFunction DEFAULT_WRAP_FUNC = new WrappingFunction() {
		@Override
		public String[] getLines(String t) {
			return t.split("(?<=\\s|-|@|,|\\.|:|;)");
		}
	};

	/**
	 * Instantiates a new paragraph.
	 *
	 * @param text the text
	 * @param font the font
	 * @param fontSize the font size
	 * @param width the width
	 */
	public Paragraph(String text, PDFont font, int fontSize, int width) {
		this(text, font, fontSize, width, HorizontalAlignment.LEFT, null);
	}

	/**
	 * Instantiates a new paragraph.
	 *
	 * @param text the text
	 * @param font the font
	 * @param fontSize the font size
	 * @param width the width
	 * @param align the align
	 * @param wrappingFunction the wrapping function
	 */
	public Paragraph(String text, PDFont font, float fontSize, float width, final HorizontalAlignment align,
			WrappingFunction wrappingFunction) {
		this(text, font, fontSize, width, align, Color.BLACK, (TextType) null, wrappingFunction);
	}

	/**
	 * Instantiates a new paragraph.
	 *
	 * @param text the text
	 * @param font the font
	 * @param fontSize the font size
	 * @param width the width
	 * @param align the align
	 * @param color the color
	 * @param textType the text type
	 * @param wrappingFunction the wrapping function
	 */
	public Paragraph(String text, PDFont font, float fontSize, float width, final HorizontalAlignment align,
			final Color color, final TextType textType, WrappingFunction wrappingFunction) {
		this.color = color;
		this.text = text;
		this.font = font;
		this.fontSize = fontSize;
		this.width = width;
		this.textType = textType;
		this.setAlign(align);
		this.wrappingFunction = wrappingFunction == null ? DEFAULT_WRAP_FUNC : wrappingFunction;
	}

	/**
	 * Gets the lines.
	 *
	 * @return the lines
	 */
	public List<String> getLines() {
		List<String> result = new ArrayList<>();

		String[] split = wrappingFunction.getLines(text);

		int[] possibleWrapPoints = new int[split.length];

		possibleWrapPoints[0] = split[0].length();

		for (int i = 1; i < split.length; i++) {
			possibleWrapPoints[i] = possibleWrapPoints[i - 1] + split[i].length();
		}

		int start = 0;
		int end = 0;
		for (int i : possibleWrapPoints) {
			float width = 0;
			try {
				width = font.getStringWidth(text.substring(start, i)) / 1000 * fontSize;
			} catch (IOException e) {
				throw new IllegalArgumentException(e.getMessage(), e);
			}
			if (start < end && width > this.width) {
				result.add(text.substring(start, end));
				start = end;
			}
			end = i;
		}
		// Last piece of text
		result.add(text.substring(start));
		return result;
	}

	/**
	 * Write.
	 *
	 * @param stream the stream
	 * @param cursorX the cursor x
	 * @param cursorY the cursor y
	 * @return the float
	 */
	public float write(final PDPageContentStream stream, float cursorX, float cursorY) {
		if (drawDebug) {
			PDStreamUtils.rectFontMetrics(stream, cursorX, cursorY, font, fontSize);

			// width
			PDStreamUtils.rect(stream, cursorX, cursorY, width, 1, Color.RED);
		}

		for (String line : getLines()) {
			line = line.trim();

			float textX = cursorX;
			switch (align) {
			case CENTER:
				textX += getHorizontalFreeSpace(line) / 2;
				break;
			case LEFT:
				break;
			case RIGHT:
				textX += getHorizontalFreeSpace(line);
				break;
			}

			PDStreamUtils.write(stream, line, font, fontSize, textX, cursorY, color);

			if (textType != null) {
				switch (textType) {
				case HIGHLIGHT:
				case SQUIGGLY:
				case STRIKEOUT:
					throw new UnsupportedOperationException("Not implemented.");
				case UNDERLINE:
					float y = (float) (cursorY - PdfFontUtil.getHeight(font, fontSize)
							- PdfFontUtil.getDescent(font, fontSize) - 1.5);
					try {
						float titleWidth = font.getStringWidth(line) / 1000 * fontSize;
						stream.moveTo(textX, y);
						stream.lineTo(textX + titleWidth, y);
						stream.stroke();
					} catch (final IOException e) {
						throw new IllegalStateException("Unable to underline text", e);
					}
					break;
				default:
					break;
				}
			}

			// move one "line" down
			cursorY -= getFontHeight();
		}

		return cursorY;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public float getHeight() {
		return getLines().size() * getFontHeight();
	}

	/**
	 * Gets the font height.
	 *
	 * @return the font height
	 */
	public float getFontHeight() {
		return PdfFontUtil.getHeight(font, fontSize);
	}

	/**
	 * Gets the horizontal free space.
	 *
	 * @param text the text
	 * @return the horizontal free space
	 */
	private float getHorizontalFreeSpace(final String text) {
		try {
			final float tw = font.getStringWidth(text.trim()) / 1000 * fontSize;
			return width - tw;
		} catch (IOException e) {
			throw new IllegalStateException("Unable to calculate text width", e);
		}
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
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {
		return text;
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
	 * Gets the font.
	 *
	 * @return the font
	 */
	public PDFont getFont() {
        return font;
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
	 * Sets the align.
	 *
	 * @param align the new align
	 */
	public void setAlign(HorizontalAlignment align) {
		this.align = align;
	}

	/**
	 * Checks if is draw debug.
	 *
	 * @return true, if is draw debug
	 */
	public boolean isDrawDebug() {
		return drawDebug;
	}

	/**
	 * Sets the draw debug.
	 *
	 * @param drawDebug the new draw debug
	 */
	public void setDrawDebug(boolean drawDebug) {
		this.drawDebug = drawDebug;
	}

	/**
	 * Gets the wrapping function.
	 *
	 * @return the wrapping function
	 */
	public WrappingFunction getWrappingFunction() {
		return wrappingFunction;
	}

}
