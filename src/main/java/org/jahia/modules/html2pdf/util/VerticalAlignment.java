package org.jahia.modules.html2pdf.util;

/**
 * The Enum VerticalAlignment.
 */
public enum VerticalAlignment {
	
	 /** The top. */
	 TOP, 
	 /** The middle. */
	 MIDDLE, 
	 /** The bottom. */
	 BOTTOM;
	
	/**
	 * Gets the.
	 *
	 * @param key the key
	 * @return the vertical alignment
	 */
	public static VerticalAlignment get(final String key) {
		switch (key == null ? "top" : key.toLowerCase().trim()) {
		case "top":
			return TOP;
		case "middle":
			return MIDDLE;
		case "bottom":
			return BOTTOM;
		default:
			return TOP;
		}
	}
}
