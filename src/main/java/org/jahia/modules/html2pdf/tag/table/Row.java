package org.jahia.modules.html2pdf.tag.table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.jahia.modules.html2pdf.util.HorizontalAlignment;
import org.jahia.modules.html2pdf.util.VerticalAlignment;

/**
 * The Class Row.
 *
 * @param <T> the generic type
 */
public class Row<T extends PDPage> {

	/** The table. */
	private final Table<T> table;
	
	/** The bookmark. */
	PDOutlineItem bookmark;
	
	/** The cells. */
	List<Cell<T>> cells;
	
	/** The height. */
	float height;

	/**
	 * Instantiates a new row.
	 *
	 * @param table the table
	 * @param cells the cells
	 * @param height the height
	 */
	Row(Table<T> table, List<Cell<T>> cells, float height) {
		this.table = table;
		this.cells = cells;
		this.height = height;
	}

	/**
	 * Instantiates a new row.
	 *
	 * @param table the table
	 * @param height the height
	 */
	Row(Table<T> table, float height) {
		this.table = table;
		this.cells = new ArrayList<>();
		this.height = height;
	}

	/**
	 * <p>
	 * Creates a cell with provided width, cell value and default left top
	 * alignment
	 * </p>.
	 *
	 * @param width the width
	 * @param value the value
	 * @return the cell
	 */
	public Cell<T> createCell(float width, String value) {
		Cell<T> cell = new Cell<T>(this, width, value, true);
		cells.add(cell);
		return cell;
	}

	/**
	 * <p>
	 * Creates a cell with provided width, cell value, horizontal and vertical
	 * alignment
	 * </p>.
	 *
	 * @param width the width
	 * @param value the value
	 * @param align the align
	 * @param valign the valign
	 * @return the cell
	 */
	public Cell<T> createCell(float width, String value, HorizontalAlignment align, VerticalAlignment valign) {
		Cell<T> cell = new Cell<T>(this, width, value, true, align, valign);
		cells.add(cell);
		return cell;
	}

	/**
	 * Creates a cell with the same width as the corresponding header cell.
	 *
	 * @param value the value
	 * @return the cell
	 */
	public Cell<T> createCell(String value) {

		float headerCellWidth = table.getHeader().getCells().get(cells.size()).getWidth();
		Cell<T> cell = new Cell<T>(this, headerCellWidth, value, false);
		cells.add(cell);
		return cell;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public float getHeight() {

		float maxheight = new Float(0);

		for (Cell<T> cell : this.cells) {
			float cellHeight = 0;
			cellHeight = cell.getTextHeight() + cell.getTopPadding() + cell.getBottomPadding();

			if (cellHeight > maxheight) {
				maxheight = cellHeight;
			}
		}
		return maxheight;
	}

	/**
	 * Gets the line height.
	 *
	 * @return the line height
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public float getLineHeight() throws IOException {
		return height;

	}

	/**
	 * Sets the height.
	 *
	 * @param height the new height
	 */
	public void setHeight(float height) {
		this.height = height;
	}

	/**
	 * Gets the cells.
	 *
	 * @return the cells
	 */
	public List<Cell<T>> getCells() {
		return cells;
	}

	/**
	 * Gets the col count.
	 *
	 * @return the col count
	 */
	public int getColCount() {
		return cells.size();
	}

	/**
	 * Sets the cells.
	 *
	 * @param cells the new cells
	 */
	public void setCells(List<Cell<T>> cells) {
		this.cells = cells;
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public float getWidth() {
		return table.getWidth();
	}

	/**
	 * Gets the bookmark.
	 *
	 * @return the bookmark
	 */
	public PDOutlineItem getBookmark() {
		return bookmark;
	}

	/**
	 * Sets the bookmark.
	 *
	 * @param bookmark the new bookmark
	 */
	public void setBookmark(PDOutlineItem bookmark) {
		this.bookmark = bookmark;
	}

	/**
	 * Gets the last cell extra width.
	 *
	 * @return the last cell extra width
	 */
	protected float getLastCellExtraWidth() {
		float cellWidth = 0;
		for (Cell<T> cell : cells) {
			cellWidth += cell.getWidth();
		}

		float lastCellExtraWidth = this.getWidth() - cellWidth;
		return lastCellExtraWidth;
	}

	/**
	 * X end.
	 *
	 * @return the float
	 */
	public float xEnd() {
		return table.getMargin() + getWidth();
	}
}
