package org.jahia.modules.html2pdf.util;

import org.jahia.modules.html2pdf.config.ConfigurationMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.Color;

/**
 * The Class PdfColorUtil.
 */
public abstract class PdfColorUtil {

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(PdfColorUtil.class);

	
	/**
	 * <pre>getColorByHex</pre>
	 * Gets the rgb.
	 *
	 * @param color the color @String
	 * @return the rgb @Color
	 */
	public static Color getColorByHex(String color){
		try{
			color =  (color.charAt(0)== '#') ? color.substring(1,7):color;
        	int r = Integer.parseInt(color.substring(0,2),16);
        	int g = Integer.parseInt(color.substring(2,4),16);
        	int b = Integer.parseInt(color.substring(4,6),16);        	
	        return new Color(r, g, b);
		}catch(Exception e){
			return new Color(0, 0, 0);
		}	
	}

	/**
	 * <pre>getBackgroundTextColor</pre>
	 * Gets the background text color.
	 *
	 * @param tag @String
	 * @return @Color
     */
	public static Color getBackgroundTextColor(String tag){
		return ConfigurationMap.getProperty(tag + ".font.bg.color") != null ?
			   PdfColorUtil.getColorByHex(ConfigurationMap.getProperty(tag + ".font.bg.color")) :
			   null;
	}

	/**
	 * <pre>getBackgroundTextColorTwo</pre>
	 * Gets the second background text color for tag.
	 *
	 * @param tag @String
	 * @param styleClass @String
	 * @return @Color
	 */
	public static Color getBackgroundTextColorOne(String tag, String styleClass, String styleAttribute){
		styleClass = styleClass != null ? (tag + "." + styleClass.trim().replaceAll(" ", "_")) : "";

		String colorStr =  ConfigurationMap.getProperty(styleClass + ".font.bg.color.one") != null ?
   			    ConfigurationMap.getProperty(styleClass + ".font.bg.color.one") :
				(      ConfigurationMap.getProperty(tag + ".font.bg.color.one") != null ?
				       ConfigurationMap.getProperty(tag + ".font.bg.color.one") :
					   null
				);

		return (colorStr != null ? PdfColorUtil.getColorByHex(colorStr) : null);
	}

	/**
	 * <pre>getBackgroundTextColorTwo</pre>
	 * Gets the second background text color for tag.
	 *
	 * @param tag @String
	 * @param styleClass @String
     * @return @Color
     */
	public static Color getBackgroundTextColorTwo(String tag, String styleClass, String styleAttribute){
		styleClass = styleClass != null ? (tag + "." + styleClass.trim().replaceAll(" ", "_")) : "";

		String colorStr =  ConfigurationMap.getProperty(styleClass + ".font.bg.color.two") != null ?
				ConfigurationMap.getProperty(styleClass + ".font.bg.color.two") :
				(      ConfigurationMap.getProperty(tag + ".font.bg.color.two") != null ?
					   ConfigurationMap.getProperty(tag + ".font.bg.color.two") :
						null
				);

		return (colorStr != null ? PdfColorUtil.getColorByHex(colorStr) : null);
	}

	
}
