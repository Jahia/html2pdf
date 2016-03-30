package org.jahia.modules.html2pdf.tag.table;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.jahia.modules.html2pdf.page.PageProvider;
import org.jahia.modules.html2pdf.text.WrappingFunction;
import org.jahia.modules.html2pdf.util.PdfFontUtil;
import org.jahia.modules.html2pdf.util.HorizontalAlignment;
import org.jahia.modules.html2pdf.util.PDStreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class Table.
 *
 * @param <T> the generic type
 */
public abstract class Table<T extends PDPage> {

	/** The Constant LOGGER. */
	private final static Logger LOGGER = LoggerFactory.getLogger(Table.class);

	/** The document. */
	public final PDDocument document;
	
	/** The margin. */
	private float margin;

	/** The current page. */
	private T currentPage;
	
	/** The table content stream. */
	private PDPageContentStream tableContentStream;
	
	/** The bookmarks. */
	private List<PDOutlineItem> bookmarks;
	
	/** The header. */
	private Row<T> header;
	
	/** The rows. */
	private List<Row<T>> rows = new ArrayList<>();

	/** The y start new page. */
	private final float yStartNewPage;
	
	/** The y start. */
	private float yStart;
	
	/** The width. */
	private final float width;
	
	/** The draw lines. */
	private final boolean drawLines;
	
	/** The draw content. */
	private final boolean drawContent;
	
	/** The header bottom margin. */
	private float headerBottomMargin = 4f;

	/** The table is broken. */
	private boolean tableIsBroken = false;

	/** The page provider. */
	private PageProvider<T> pageProvider;

	/** The page top margin. */
	// page margins
	private final float pageTopMargin;
	
	/** The page bottom margin. */
	private final float pageBottomMargin;

	/** The draw debug. */
	private boolean drawDebug;

	/**
	 * Instantiates a new table.
	 *
	 * @param yStart the y start
	 * @param yStartNewPage the y start new page
	 * @param pageTopMargin the page top margin
	 * @param pageBottomMargin the page bottom margin
	 * @param width the width
	 * @param margin the margin
	 * @param document the document
	 * @param currentPage the current page
	 * @param drawLines the draw lines
	 * @param drawContent the draw content
	 * @param pageProvider the page provider
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Table(float yStart, float yStartNewPage, float pageTopMargin, float pageBottomMargin, float width,
			float margin, PDDocument document, T currentPage, boolean drawLines, boolean drawContent,
			PageProvider<T> pageProvider) throws IOException {
		this.pageTopMargin = pageTopMargin;
		this.document = document;
		this.drawLines = drawLines;
		this.drawContent = drawContent;
		// Initialize table
		this.yStartNewPage = yStartNewPage;
		this.margin = margin;
		this.width = width;
		this.yStart = yStart;
		this.pageBottomMargin = pageBottomMargin;
		this.currentPage = currentPage;
		this.pageProvider = pageProvider;
		loadFonts();
	}

	/**
	 * Instantiates a new table.
	 *
	 * @param yStartNewPage the y start new page
	 * @param pageTopMargin the page top margin
	 * @param pageBottomMargin the page bottom margin
	 * @param width the width
	 * @param margin the margin
	 * @param document the document
	 * @param drawLines the draw lines
	 * @param drawContent the draw content
	 * @param pageProvider the page provider
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Table(float yStartNewPage, float pageTopMargin, float pageBottomMargin, float width, float margin,
			PDDocument document, boolean drawLines, boolean drawContent, PageProvider<T> pageProvider)
					throws IOException {
		this.pageTopMargin = pageTopMargin;
		this.document = document;
		this.drawLines = drawLines;
		this.drawContent = drawContent;
		// Initialize table
		this.yStartNewPage = yStartNewPage;
		this.margin = margin;
		this.width = width;
		this.pageProvider = pageProvider;
		this.pageBottomMargin = pageBottomMargin;

		// Fonts needs to be loaded before page creation
		loadFonts();
		this.currentPage = pageProvider.createPage();
	}

	/**
	 * Load fonts.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected abstract void loadFonts() throws IOException;

	/**
	 * Load font.
	 *
	 * @param fontName the font path
	 * @return the PD type0 font
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected PDType0Font loadFont(String fontName) throws Exception {
		return PdfFontUtil.loadFont(getDocument(), fontName);
	}

	/**
	 * Gets the document.
	 *
	 * @return the document
	 */
	protected PDDocument getDocument() {
		return document;
	}

	/**
	 * Draw title.
	 *
	 * @param title the title
	 * @param font the font
	 * @param fontSize the font size
	 * @param tableWidth the table width
	 * @param height the height
	 * @param alignment the alignment
	 * @param freeSpaceForPageBreak the free space for page break
	 * @param drawHeaderMargin the draw header margin
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void drawTitle(String title, PDFont font, int fontSize, float tableWidth, float height, String alignment, float freeSpaceForPageBreak, boolean drawHeaderMargin)
			throws IOException {
		drawTitle(title, font, fontSize, tableWidth, height, alignment, freeSpaceForPageBreak, null, drawHeaderMargin);
	}

	/**
	 * Draw title.
	 *
	 * @param title the title
	 * @param font the font
	 * @param fontSize the font size
	 * @param tableWidth the table width
	 * @param height the height
	 * @param alignment the alignment
	 * @param freeSpaceForPageBreak the free space for page break
	 * @param wrappingFunction the wrapping function
	 * @param drawHeaderMargin the draw header margin
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void drawTitle(String title, PDFont font, int fontSize, float tableWidth, float height, String alignment, float freeSpaceForPageBreak,
			WrappingFunction wrappingFunction, boolean drawHeaderMargin) throws IOException {

		ensureStreamIsOpen();
		
		if (isEndOfPage(freeSpaceForPageBreak)) {
			this.tableContentStream.close();
			pageBreak();
		}

		if (title == null) {
			// if you don't have title just use the height of maxTextBox in your
			// "row"
			yStart -= height;
		} else {
			PDPageContentStream articleTitle = createPdPageContentStream();
			Paragraph paragraph = new Paragraph(title, font, fontSize, tableWidth, HorizontalAlignment.get(alignment),
					wrappingFunction);
			paragraph.setDrawDebug(drawDebug);
			yStart = paragraph.write(articleTitle, margin, yStart);
			if (paragraph.getHeight() < height) {
				yStart -= (height - paragraph.getHeight());
			}

			articleTitle.close();
			
			if (drawDebug) {
				// margin
				PDStreamUtils.rect(tableContentStream, margin, yStart, width, headerBottomMargin, Color.CYAN);
			}
		}

		if (drawHeaderMargin) {
			yStart -= headerBottomMargin;
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
	 * Creates the row.
	 *
	 * @param height the height
	 * @return the row
	 */
	public Row<T> createRow(float height) {
		Row<T> row = new Row<T>(this, height);
		this.rows.add(row);
		return row;
	}

	/**
	 * Creates the row.
	 *
	 * @param cells the cells
	 * @param height the height
	 * @return the row
	 */
	public Row<T> createRow(List<Cell<T>> cells, float height) {
		Row<T> row = new Row<T>(this, cells, height);
		this.rows.add(row);
		return row;
	}

	/**
	 * Draw.
	 *
	 * @return the float
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public float draw() throws IOException {
		ensureStreamIsOpen();
		
		for (Row<T> row : rows) {
			if (row == header) {
				// check if header row height and first data row height can fit the page
				// if not draw them on another side
				float headerHeightIncludingFirstDataRow = header.getHeight() + rows.get(1).getHeight();
				if (isEndOfPage(headerHeightIncludingFirstDataRow)) {
					pageBreak();
				}
			}
			drawRow(row);
		}

		endTable();
		return yStart;
	}

	/**
	 * Draw row.
	 *
	 * @param row the row
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void drawRow(Row<T> row) throws IOException {
		// draw the bookmark
		if (row.getBookmark() != null) {
			PDPageXYZDestination bookmarkDestination = new PDPageXYZDestination();
			bookmarkDestination.setPage(currentPage);
			bookmarkDestination.setTop((int) yStart);
			row.getBookmark().setDestination(bookmarkDestination);
			this.addBookmark(row.getBookmark());
		}

		if (isEndOfPage(row)) {

			// Draw line at bottom of table
			endTable();

			// insert page break
			pageBreak();

			// redraw all headers on each currentPage
			if (header != null) {
				drawRow(header);
			} else {
				LOGGER.warn("No Header Row Defined.");
			}
		}

		if (drawLines) {
			drawVerticalLines(row);
		}

		if (drawContent) {
			drawCellContent(row);
		}
	}

	/**
	 * <p>
	 * Method to switch between the {@link PageProvider} and the abstract method
	 * {@link Table#createPage()}, preferring the {@link PageProvider}.
	 * </p>
	 * <p>
	 * Will be removed once {@link #createPage()} is removed.
	 * </p>
	 *
	 * @return the t
	 */
	private T createNewPage() {
		if (pageProvider != null) {
			return pageProvider.createPage();
		}
		return createPage();
	}

	/**
	 * Creates the page.
	 *
	 * @return the t
	 */
	protected T createPage() {
		throw new IllegalStateException(
				"You either have to provide a " + PageProvider.class.getCanonicalName() + " or override this method");
	}

	/**
	 * Creates the pd page content stream.
	 *
	 * @return the PD page content stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private PDPageContentStream createPdPageContentStream() throws IOException {
		return new PDPageContentStream(getDocument(), getCurrentPage(), true, true);
	}

	/**
	 * Draw cell content.
	 *
	 * @param row the row
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void drawCellContent(Row<T> row) throws IOException {

		// position into first cell (horizontal)
		float cursorX = margin;

		for (Cell<T> cell : row.getCells()) {
			// no text without font
			if (cell.getFont() == null) {
				throw new IllegalArgumentException("Font is null on Cell=" + cell.getText());
			}

			// position at top of current cell
			// descending by font height - font descent, because we are
			// positioning the base line here
			float cursorY = yStart - cell.getTopPadding() - PdfFontUtil.getHeight(cell.getFont(), cell.getFontSize())
					- PdfFontUtil.getDescent(cell.getFont(), cell.getFontSize());

			switch (cell.getValign()) {
			case TOP:
				break;
			case MIDDLE:
				cursorY -= cell.getVerticalFreeSpace() / 2;
				break;
			case BOTTOM:
				cursorY -= cell.getVerticalFreeSpace();
				break;
			}

			// remember horizontal cursor position, so we can advance to the
			// next cell easily later
			float cellStartX = cursorX;

			// respect left padding
			cursorX += cell.getLeftPadding();

			// remember this horizontal position, as it is the anchor for each
			// new line
			float lineStartX = cursorX;

			// font settings
			this.tableContentStream.setFont(cell.getFont(), cell.getFontSize());

			// if it is head row or if it is header cell then please use bold
			// font
			if (row.equals(header) || cell.isHeaderCell()) {
				this.tableContentStream.setFont(cell.getFontBold(), cell.getFontSize());
			}
			this.tableContentStream.setNonStrokingColor(cell.getTextColor());

			// print all lines of the cell
			float tw = 0.0f;
			for (String line : cell.getParagraph().getLines()) {
				// screw you, whitespace!
				line = line.trim();

				// we start at the line start ... seems legit
				cursorX = lineStartX;

				// the widest text does not fill the inner width of the cell? no
				// problem, just add it ;)
				switch (cell.getAlign()) {
				case CENTER:
					cursorX += cell.getHorizontalFreeSpace() / 2;
					break;
				case LEFT:
					break;
				case RIGHT:
					cursorX += cell.getHorizontalFreeSpace();
					break;
				}

				// calculate the width of this line
				tw = Math.max(tw, cell.getFont().getStringWidth(line));
				tw = tw / 1000 * cell.getFontSize();
				float freeSpaceWithinLine = cell.getInnerWidth() - cell.getHorizontalFreeSpace() - tw;
				switch (cell.getAlign()) {
				case CENTER:
					cursorX += freeSpaceWithinLine / 2;
					break;
				case LEFT:
					// it doesn't matter because X position is always the same
					// as row above
					break;
				case RIGHT:
					cursorX += freeSpaceWithinLine;
					break;
				}

				// finally draw the line
				this.tableContentStream.beginText();
				this.tableContentStream.newLineAtOffset(cursorX, cursorY);

				this.tableContentStream.showText(line);
				this.tableContentStream.endText();
				this.tableContentStream.closePath();

				// advance a line vertically
				cursorY = cursorY - cell.getParagraph().getFontHeight();
			}

			// set cursor to the start of this cell plus its width to advance to
			// the next cell
			cursorX = cellStartX + cell.getWidth();

		}
		// Set Y position for next row
		yStart = yStart - row.getHeight();
	}

	/**
	 * Draw vertical lines.
	 *
	 * @param row the row
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void drawVerticalLines(Row<T> row) throws IOException {
		float xStart = margin;

		// give an extra margin to the latest cell
		float xEnd = row.xEnd();

		// Draw Row upper border
		drawLine("Row Upper Border ", xStart, yStart, xEnd, yStart);

		Iterator<Cell<T>> cellIterator = row.getCells().iterator();
		while (cellIterator.hasNext()) {
			Cell<T> cell = cellIterator.next();

			fillCellColor(cell, yStart, xStart, cellIterator);

			float yEnd = yStart - row.getHeight();

			// draw the vertical line to separate cells
			drawLine("Cell Separator ", xStart, yStart, xStart, yEnd);

			xStart += getWidth(cell, cellIterator);
		}

		// draw the last vertical line at the right of the table
		float yEnd = yStart - row.getHeight();

		drawLine("Last Cell ", xEnd, yStart, xEnd, yEnd);
	}

	/**
	 * Draw line.
	 *
	 * @param type the type
	 * @param xStart the x start
	 * @param yStart the y start
	 * @param xEnd the x end
	 * @param yEnd the y end
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void drawLine(String type, float xStart, float yStart, float xEnd, float yEnd) throws IOException {

		this.tableContentStream.setNonStrokingColor(Color.BLACK);
		this.tableContentStream.setStrokingColor(Color.BLACK);

		this.tableContentStream.moveTo(xStart, yStart);
		this.tableContentStream.lineTo(xEnd, yEnd);
		this.tableContentStream.stroke();
		this.tableContentStream.closePath();
	}

	/**
	 * Fill cell color.
	 *
	 * @param cell the cell
	 * @param yStart the y start
	 * @param xStart the x start
	 * @param cellIterator the cell iterator
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void fillCellColor(Cell<T> cell, float yStart, float xStart, Iterator<Cell<T>> cellIterator)
			throws IOException {
		// Fill Cell Color
		if (cell.getFillColor() != null) {
			this.tableContentStream.setNonStrokingColor(cell.getFillColor());

			// y start is bottom pos
			yStart = yStart - cell.getHeight();
			float height = cell.getHeight() - 1f;

			float cellWidth = getWidth(cell, cellIterator);
			this.tableContentStream.addRect(xStart, yStart, cellWidth, height);
			this.tableContentStream.fill();
			this.tableContentStream.closePath();

			// Reset NonStroking Color to default value
			this.tableContentStream.setNonStrokingColor(Color.BLACK);
		}
	}

	/**
	 * Gets the width.
	 *
	 * @param cell the cell
	 * @param cellIterator the cell iterator
	 * @return the width
	 */
	private float getWidth(Cell<T> cell, Iterator<Cell<T>> cellIterator) {
		float width;
		if (cellIterator.hasNext()) {
			width = cell.getWidth();
		} else {
			width = cell.getExtraWidth();
		}
		return width;
	}
	
	/**
	 * Ensure stream is open.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void ensureStreamIsOpen() throws IOException {
		if (tableContentStream == null) {
			tableContentStream = createPdPageContentStream();
		}
	}

	/**
	 * End table.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void endTable() throws IOException {
		if (drawLines) {
			// Draw line at bottom
			drawLine("Row Bottom Border ", this.margin, this.yStart, this.margin + width, this.yStart);
		}
		this.tableContentStream.close();
	}

	/**
	 * Gets the current page.
	 *
	 * @return the current page
	 */
	public T getCurrentPage() {
		checkNotNull(this.currentPage, "No current page defined.");
		return this.currentPage;
	}

	/**
	 * Checks if is end of page.
	 *
	 * @param row the row
	 * @return true, if is end of page
	 */
	private boolean isEndOfPage(Row<T> row) {
		float currentY = yStart - row.getHeight();
		boolean isEndOfPage = currentY <= pageBottomMargin;
		if (isEndOfPage) {
			setTableIsBroken(true);
			System.out.println("Its end of page. Table row height caused the problem.");
		}

		// If we are closer than bottom margin, consider this as
		// the end of the currentPage
		// If you add rows that are higher then bottom margin, this needs to be
		// checked
		// manually using getNextYPos
		return isEndOfPage;
	}

	/**
	 * Checks if is end of page.
	 *
	 * @param freeSpaceForPageBreak the free space for page break
	 * @return true, if is end of page
	 */
	private boolean isEndOfPage(float freeSpaceForPageBreak) {
		float currentY = yStart - freeSpaceForPageBreak;
		boolean isEndOfPage = currentY <= pageBottomMargin;
		if (isEndOfPage) {
			setTableIsBroken(true);
			System.out.println("Its end of the page. Table title caused this problem.");
		}
		return isEndOfPage;
	}

	/**
	 * Page break.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void pageBreak() throws IOException {
		tableContentStream.close();
		this.yStart = yStartNewPage - pageTopMargin;
		this.currentPage = createNewPage();
		this.tableContentStream = createPdPageContentStream();
	}
	
	/**
	 * Adds the bookmark.
	 *
	 * @param bookmark the bookmark
	 */
	private void addBookmark(PDOutlineItem bookmark) {
		if (bookmarks == null)
			bookmarks = new ArrayList<>();
		bookmarks.add(bookmark);
	}

	/**
	 * Gets the bookmarks.
	 *
	 * @return the bookmarks
	 */
	public List<PDOutlineItem> getBookmarks() {
		return bookmarks;
	}

	/**
	 * Sets the header.
	 *
	 * @param header the new header
	 */
	public void setHeader(Row<T> header) {
		this.header = header;
	}

	/**
	 * Gets the header.
	 *
	 * @return the header
	 */
	public Row<T> getHeader() {
		if (header == null) {
			throw new IllegalArgumentException("Header Row not set on table");
		}
		return header;
	}

	/**
	 * Gets the margin.
	 *
	 * @return the margin
	 */
	public float getMargin() {
		return margin;
	}

	/**
	 * Sets the y start.
	 *
	 * @param yStart the new y start
	 */
	protected void setYStart(float yStart) {
		this.yStart = yStart;
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
	 * Table is broken.
	 *
	 * @return true, if successful
	 */
	public boolean tableIsBroken() {
		return tableIsBroken;
	}
	
	/**
	 * Gets the header and data height.
	 *
	 * @return the header and data height
	 */
	public float getHeaderAndDataHeight() {
		return header == null ? 0 : header.getHeight() + rows.get(header == null ? 0 : 1).getHeight();
	}

	/**
	 * Sets the table is broken.
	 *
	 * @param tableIsBroken the new table is broken
	 */
	public void setTableIsBroken(boolean tableIsBroken) {
		this.tableIsBroken = tableIsBroken;
	}

   /**
	* Check not null.
	*
	* @param <T> the generic type
	* @param reference the reference
	* @param errorMessage the error message
	* @return the t
	*/
	public static <T> T checkNotNull(T reference, Object errorMessage) {
	  if (reference == null) {
	    throw new NullPointerException(String.valueOf(errorMessage != null  ? errorMessage : ""));
	  }
	  return reference;
	}
	  
}
