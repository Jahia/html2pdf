package org.jahia.modules.html2pdf.util;

/**
 * The Enum HorizontalAlignment.
 */
public enum HorizontalAlignment {
	
	 /** The left. */
  	 LEFT, 
	 /** The center. */
	 CENTER, 
	 /** The right. */
	 RIGHT;

	/**
	 * Gets the.
	 *
	 * @param key the key
	 * @return the horizontal alignment
	 */
	public static HorizontalAlignment get(final String key) {
		switch (key == null ? "left" : key.toLowerCase().trim()) {
		case "left":
			return LEFT;
		case "center":
			return CENTER;
		case "right":
			return RIGHT;
		default:
			return LEFT;
		}
	}
}
