package org.jahia.modules.html2pdf.util;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.jahia.modules.html2pdf.config.ConfigurationMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class PdfFontUtil.
 */
@SuppressWarnings("deprecation")
public class PdfFontUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(PdfFontUtil.class);

	/**
	 * <p>
	 * {@link HashMap} for caching {@link FontMetrics} for designated
	 * {@link PDFont} because {@link PdfFontUtil#getHeight(PDFont, float)} is
	 * expensive to calculate and the results are only approximate.
	 * </p>
	 */
	private static final Map<String, FontMetrics> fontMetrics = new HashMap<>();

	/**
	 * the PdfFontUtil constructor
	 */
	private PdfFontUtil() {}
		
	
	/**
	 * Gets the font color.
	 *
	 * @param tag the tag
	 * @return the font color
	 */
	public static Color getFontColor(String tag, String styleClass, String styleAttribute){

		styleClass     = styleClass != null ? (tag + "." + styleClass.trim().replaceAll(" ", "_")) : "";
        styleAttribute = styleAttribute != null ? (tag + "." + styleAttribute.trim().replaceAll(" ", "_")) : "";

		String color = ConfigurationMap.getProperty(styleClass + ".font.color") != null ?
				ConfigurationMap.getProperty(styleClass + ".font.color") :
				(      ConfigurationMap.getProperty(styleAttribute + ".font.color") != null ?
					   ConfigurationMap.getProperty(styleAttribute + ".font.color") :
                        (  ConfigurationMap.getProperty(tag + ".font.color") != null ?
                           ConfigurationMap.getProperty(tag + ".font.color") :
                           ConfigurationMap.getProperty("font.color")
                        )
				);

		return PdfColorUtil.getColorByHex(color);
	} 
	
	/**
	 * Gets the font size.
	 *
	 * @param tag the tag
	 * @return the font size
	 */
	public static Float getFontSize(String tag, String styleClass, String styleAttribute){

		styleClass     = styleClass != null ? (tag + "." + styleClass.trim().replaceAll(" ", "_")) : "";
        styleAttribute = styleAttribute != null ? (tag + "." + styleAttribute.trim().replaceAll(" ", "_")) : "";

		String size = ConfigurationMap.getProperty(styleClass + ".font.size") != null ?
				 ConfigurationMap.getProperty(styleClass + ".font.size") :
				 (      ConfigurationMap.getProperty(styleAttribute + ".font.size") != null ?
						ConfigurationMap.getProperty(styleAttribute + ".font.size") :
                         (  ConfigurationMap.getProperty(tag + ".font.size") != null ?
                            ConfigurationMap.getProperty(tag + ".font.size") :
                            ConfigurationMap.getProperty("font.size")
                         )
				 );

        return StringUtils.isNotEmpty(size) ? new Float(size) : 0;
	} 

	/**
	 * Gets the font.
	 *
	 * @param document the document
	 * @param tag the tag
	 * @return the font
	 */
	public static PDFont getFont(PDDocument document, String tag, String styleClass, String styleAttribute){
		PDFont font    = null;
		styleClass     = styleClass     != null ? (tag + "." + styleClass.trim().replaceAll(" ", "_"))     : "";
        styleAttribute = styleAttribute != null ? (tag + "." + styleAttribute.trim().replaceAll(" ", "_")) : "";

		String fontFamily = ConfigurationMap.getProperty(styleClass + ".font.family") != null ?
				 ConfigurationMap.getProperty(styleClass + ".font.family") :
				 (      ConfigurationMap.getProperty(styleAttribute + ".font.family") != null ?
						ConfigurationMap.getProperty(styleAttribute + ".font.family") :
                         (  ConfigurationMap.getProperty(tag + ".font.family") != null ?
                            ConfigurationMap.getProperty(tag + ".font.family") :
                            ConfigurationMap.getProperty("font.family")
                         )
				 );

		String fontStyle  =  ConfigurationMap.getProperty(styleClass + ".font.style") != null ?
				 ConfigurationMap.getProperty(styleClass + ".font.style") :
				 (      ConfigurationMap.getProperty(styleAttribute + ".font.style") != null ?
						ConfigurationMap.getProperty(styleAttribute + ".font.style") :
                         (  ConfigurationMap.getProperty(tag + ".font.style") != null ?
                            ConfigurationMap.getProperty(tag + ".font.style") :
                            ConfigurationMap.getProperty("font.style")
                         )
				 );

		String fontWeight  =  ConfigurationMap.getProperty(styleClass + ".font.weight") != null ?
				 ConfigurationMap.getProperty(styleClass + ".font.weight") :
				 (      ConfigurationMap.getProperty(styleAttribute + ".font.weight") != null ?
						ConfigurationMap.getProperty(styleAttribute + ".font.weight") :
                         (  ConfigurationMap.getProperty(tag + ".font.weight") != null ?
                            ConfigurationMap.getProperty(tag + ".font.weight") :
                            ConfigurationMap.getProperty("font.weight")
                         )
				 );

        try{
	  		switch (fontFamily.replace(" ","_").toLowerCase()) {
		       case "times_roman":  
			   		if(fontWeight.equalsIgnoreCase("normal")){
			   			if(fontStyle.equalsIgnoreCase("normal"))
			   				font = PDType1Font.TIMES_ROMAN;
			   			else if(fontStyle.equalsIgnoreCase("italic") || fontStyle.equalsIgnoreCase("oblique"))
			   				font = PDType1Font.TIMES_ITALIC;
			   		}else if(fontWeight.equalsIgnoreCase("bold")){
			   			if(fontStyle.equalsIgnoreCase("normal"))
			   				font = PDType1Font.TIMES_BOLD;
			   			else if(fontStyle.equalsIgnoreCase("italic") || fontStyle.equalsIgnoreCase("oblique"))
			   				font = PDType1Font.TIMES_BOLD_ITALIC;
			   		}
		            break;
		       case "helvetica":  
		    	   if(fontWeight.equalsIgnoreCase("normal")){
			   			if(fontStyle.equalsIgnoreCase("normal"))
			   				font = PDType1Font.HELVETICA;
			   			else if(fontStyle.equalsIgnoreCase("italic") || fontStyle.equalsIgnoreCase("oblique"))
			   				font = PDType1Font.HELVETICA_OBLIQUE;
			   		}else if(fontWeight.equalsIgnoreCase("bold")){
			   			if(fontStyle.equalsIgnoreCase("normal"))
			   				font = PDType1Font.HELVETICA_BOLD;
			   			else if(fontStyle.equalsIgnoreCase("italic") || fontStyle.equalsIgnoreCase("oblique"))
			   				font = PDType1Font.HELVETICA_BOLD_OBLIQUE;
			   		}
		            break;
		       case "courier": 
		    	   if(fontWeight.equalsIgnoreCase("normal")){
		   				if(fontStyle.equalsIgnoreCase("normal"))
		   					font = PDType1Font.COURIER;
		   				else if(fontStyle.equalsIgnoreCase("italic") || fontStyle.equalsIgnoreCase("oblique"))
		   					font = PDType1Font.COURIER_OBLIQUE;
		   		   }else if(fontWeight.equalsIgnoreCase("bold")){
		   			    if(fontStyle.equalsIgnoreCase("normal"))
		   			    	font = PDType1Font.COURIER_BOLD;
		   			    else if(fontStyle.equalsIgnoreCase("italic") || fontStyle.equalsIgnoreCase("oblique"))
		   			    	font = PDType1Font.COURIER_BOLD_OBLIQUE;
		   		   }
		    	   break;
	
		       default:
		    	   if(fontWeight.equalsIgnoreCase("normal")){
		   				if(fontStyle.equalsIgnoreCase("normal")){
		   					font = PDTrueTypeFont.loadTTF(document, CommonUtil.getFontResource(fontFamily.replace(" ","_").toLowerCase()));
		   				}else if(fontStyle.equalsIgnoreCase("italic") || fontStyle.equalsIgnoreCase("oblique"))
		   					font = PDTrueTypeFont.loadTTF(document, CommonUtil.getFontResource(fontFamily.replace(" ","_").toLowerCase() + "_italic"));
		   		   }else if(fontWeight.equalsIgnoreCase("bold")){
		   			    if(fontStyle.equalsIgnoreCase("normal"))
		   			    	font = PDTrueTypeFont.loadTTF(document, CommonUtil.getFontResource(fontFamily.replace(" ","_").toLowerCase() + "_bold"));
		   			    else if(fontStyle.equalsIgnoreCase("italic") || fontStyle.equalsIgnoreCase("oblique"))
		   			    	font = PDTrueTypeFont.loadTTF(document, CommonUtil.getFontResource(fontFamily.replace(" ","_").toLowerCase() + "_bold_italic"));
		   		   }
		    	   break;
	  		}
		 	          		
        }catch(Exception e){
        	logger.error("getFont: Error getting the font for tag[{}], {}", tag, e.getLocalizedMessage());
        }
  				
  		return (font != null ? font : PDType1Font.COURIER);
	}

	/**
	 * <pre>getFontSpaceFactor</pre>
	 * Gets the font space factor
	 *
 	 * @return @Float
     */
	public static Float getFontSpaceFactor(){
		return ConfigurationMap.getProperty("font.space.factor") != null ?
				       new Float(ConfigurationMap.getProperty("font.space.factor")) :
				       1.5F;
	}
	

	/**
	 * <p>
	 * Loads the {@link PDType0Font} to be embedded in the specified
	 * {@link PDDocument}.
	 * </p>
	 * 
	 * @param document
	 * @param fontName
	 * @return The read {@link PDType0Font}
	 * @throws IOException
	 *             If reading the font file fails
	 */
	public static final PDType0Font loadFont(PDDocument document, String fontName) throws Exception {
		return PDType0Font.load(document,CommonUtil.getFontResource(fontName));
	}

	/**
	 * <p>
	 * Retrieving {@link String} width depending on current font size. The width
	 * of the string in 1/1000 units of text space.
	 * </p>
	 * 
	 * @param font
	 *            The font of text whose width will be retrieved
	 * @param text
	 *            The text whose width will be retrieved
	 * @param fontSize
	 *            The font size of text whose width will be retrieved
	 * @return
	 */
	public static float getStringWidth(final PDFont font, final String text, final float fontSize) {
		try {
			return font.getStringWidth(text) / 1000 * fontSize;
		} catch (final IOException e) {
			logger.error("getStringWidth: Unable to determine text width, {}", e.getLocalizedMessage());
		}
		return text.length() * fontSize;
	}

	/**
	 * <p>
	 * Calculate the font ascent distance.
	 * </p>
	 * 
	 * @param font
	 *            The font from which calculation will be applied
	 * @param fontSize
	 *            The font size from which calculation will be applied
	 * @return Positive font ascent distance
	 */
	public static float getAscent(final PDFont font, final float fontSize) {
		final String fontName = font.getName();
		if (!fontMetrics.containsKey(fontName)) {
			try {
				createFontMetrics(font);
			} catch (final IOException e) {
				logger.info("getAscent: Getting font ascent distance ...");
				return font.getFontDescriptor().getAscent() / 1000 * fontSize;
			}
		}

		return fontMetrics.get(fontName).ascent * fontSize;
	}

	/**
	 * <p>
	 * Calculate the font descent distance.
	 * </p>
	 * 
	 * @param font
	 *            The font from which calculation will be applied
	 * @param fontSize
	 *            The font size from which calculation will be applied
	 * @return Negative font descent distance
	 */
	public static float getDescent(final PDFont font, final float fontSize) {
		final String fontName = font.getName();
		if (!fontMetrics.containsKey(fontName)) {
			try {
				createFontMetrics(font);
			} catch (final IOException e) {
				logger.info("getDescent: Getting font descent distance ...");
				return font.getFontDescriptor().getDescent() / 1000 * fontSize;
			}
		}

		return fontMetrics.get(fontName).descent * fontSize;
	}

	/**
	 * <p>
	 * Calculate the font height.
	 * </p>
	 * 
	 * @param font
	 * @param fontSize
	 * @return
	 */
	public static float getHeight(final PDFont font, final float fontSize) {
		final String fontName = font.getName();
		if (!fontMetrics.containsKey(fontName)) {
			try {
				createFontMetrics(font);
			} catch (final IOException e) {
				logger.info("getHeight, Getting font height ...");
				return font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize * 0.865f;
			}
		}

		return fontMetrics.get(fontName).height * fontSize;
	}

	/**
	 * <p>
	 * Create basic {@link FontMetrics} for current font.
	 * <p>
	 * 
	 * @param font
	 *            The font from which calculation will be applied
	 * @throws IOException
	 */
	private static void createFontMetrics(final PDFont font) throws IOException {
		final float base = font.getHeight("a".codePointAt(0)) / 1000;
		final float ascent = font.getHeight("d".codePointAt(0)) / 1000 - base;
		final float descent = font.getHeight("g".codePointAt(0)) / 1000 - base;
		fontMetrics.put(font.getName(), new FontMetrics(base + ascent + descent, ascent, -descent));
	}
	
	
	/* the FontMetrics class. */
	private static final class FontMetrics {

		/* the ascendent */
		private final float ascent;

		/* the descent */
		private final float descent;

		/* the height */
		private final float height;

		/* the FontMetrics constructor */
		public FontMetrics(final float height, final float ascent, final float descent) {
			this.height = height;
			this.ascent = ascent;
			this.descent = descent;
		}
	}

}
