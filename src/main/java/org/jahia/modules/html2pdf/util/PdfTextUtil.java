package org.jahia.modules.html2pdf.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class PdfTextUtil.
 */
public abstract class PdfTextUtil {

    /* the logger */
    private static Logger logger = LoggerFactory.getLogger(PdfTextUtil.class);


	/**
	 * Gets the words by text.
	 *
	 * @param text the text
	 * @return the words by text
	 */
	public static List<String> getWordsByText(String text){
		List<String> words = new ArrayList<>();
		for (String word : text.split(" ")) {
			words.add(word);
		}
		return words;
	}

    /**
     * Get the advanced trim
     *
     * @param value the text value
     * @return the advanced trim value
     */
    public static String trimAdvanced(String value) {
        Objects.requireNonNull(value);

        int strLength = value.length();
        int len = value.length();
        int st = 0;
        char[] val = value.toCharArray();

        if (strLength == 0)
            return "";

        while ((st < len) && (val[st] <= ' ') || (val[st] == '\u00A0')) {
            st++;
            if (st == strLength)
                break;
        }

        while ((st < len) && (val[len - 1] <= ' ') || (val[len - 1] == '\u00A0')) {
            len--;
            if (len == 0)
                break;
        }

        return (st > len) ? "" : ((st > 0) || (len < strLength)) ? value.substring(st, len) : value;
    }

    /**
     * Gets the lines by text.
     *
     * @param fontSize the font size
     * @param pdfFont the pdf font
     * @param text the text
     * @return the lines by text
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static List<String> getLinesByTextLenght(Float fontSize, PDFont pdfFont , String text){
        return getLinesByTextLenght(fontSize, pdfFont , text, null, false);
    }

    /**
	 * Gets the lines by text.
	 *
	 * @param fontSize the font size
	 * @param pdfFont the pdf font
	 * @param text the text
     * @param sizeX the size x
     * @param onlyFirstLine only first line
	 * @return the lines by text
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static List<String> getLinesByTextLenght(Float fontSize, PDFont pdfFont , String text, Float sizeX, boolean onlyFirstLine){
		List<String> lines = new ArrayList<>();
		Float sizeComparator = sizeX != null ? PdfPageUtil.getLineWidthX() - sizeX : PdfPageUtil.getLineWidthX();
		
		try{
			int lastSpace = -1;
			while (text.length() > 0){
		        int spaceIndex = text.indexOf(' ', lastSpace + 1);
		        if (spaceIndex < 0)
		            spaceIndex = text.length();
		        
		        String subString = text.substring(0, spaceIndex);
		        float size = fontSize * pdfFont.getStringWidth(subString) / 1000;

		        if (size > sizeComparator){
		            if (lastSpace < 0)
		                lastSpace = spaceIndex;
		            subString = text.substring(0, lastSpace);
		            lines.add(subString);
		            text = text.substring(lastSpace).trim();
		            lastSpace = -1;
		            if(onlyFirstLine)
		            	sizeComparator = PdfPageUtil.getLineWidthX();
		        }
		        else if (spaceIndex == text.length()){
		        	lines.add(text);
		            text = "";
		        }
		        else{
		            lastSpace = spaceIndex;
		        }
			}
		}catch(Exception e){
			logger.error("getLinesByTextLenght: Error getting the text lines, {}", e.getLocalizedMessage());
		}
		return lines;
	}

    /**
     * Gets the lines by text.
     *
     * @param fontSize the font size
     * @param pdfFont the pdf font
     * @param text the text
     * @param sizeX the size x
     * @param xWidth thew x width line
     * @param onlyFirstLine only first line
     * @return the lines by text
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static List<String> getLinesByTextLenght(Float fontSize, PDFont pdfFont , String text, Float sizeX, Float xWidth, boolean onlyFirstLine){
        List<String> lines = new ArrayList<>();
        Float sizeComparator = sizeX != null ? xWidth - sizeX : xWidth;

        try{
            int lastSpace = -1;
            while (text.length() > 0){
                int spaceIndex = text.indexOf(' ', lastSpace + 1);
                if (spaceIndex < 0)
                    spaceIndex = text.length();

                String subString = text.substring(0, spaceIndex);
                float size = fontSize * pdfFont.getStringWidth(subString) / 1000;

                if (size > sizeComparator){
                    if (lastSpace < 0)
                        lastSpace = spaceIndex;
                    subString = text.substring(0, lastSpace);
                    lines.add(subString);
                    text = text.substring(lastSpace).trim();
                    lastSpace = -1;
                    if(onlyFirstLine)
                        sizeComparator = xWidth;
                }
                else if (spaceIndex == text.length()){
                    lines.add(text);
                    text = "";
                }
                else{
                    lastSpace = spaceIndex;
                }
            }
        }catch(Exception e){
            logger.error("getLinesByTextLenght: Error getting the text lines, {}", e.getLocalizedMessage());
        }

        return lines;
    }

    /**
     * <pre>getTextWidthX</pre>
     * Gets the text width
     *
     * @param text @String
     * @param font @PDFont
     * @param fontSize @Float
     * @return  @Float
     */
    public static Float getTextWidthX(String text, PDFont font, Float fontSize){
		try{
			return fontSize *  font.getStringWidth(text)/ 1000;
		}catch(Exception e){
			e.printStackTrace();
			return 0F;
		}
	}

    /**
     * <pre>convertStringToUTF8</pre>
     * converts the string to utf-8 encoding.
     *
     * @param content @String
     * @return @String
     */
	public static String convertStringToUTF8(String content){
		try {
			return new String(content.getBytes("UTF-8"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("convertStringToUTF8: Can not convert the string to encoding utf-8, {}", e.getLocalizedMessage() );
		}
		 return "";
	}

    /**
     * <pre>stripNonValidXMLCharacters</pre>
     * strip the invalid xml characters.
     *
     * @param in @String
     * @return @String
     */
    public static String stripNonValidXMLCharacters(String in) {
        StringBuffer out = new StringBuffer(); // Used to hold the output.
        char current; // Used to reference the current character.

        if (in == null || ("".equals(in))) return ""; //
        for (int i = 0; i < in.length(); i++) {
            current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught here; it should not happen.
            if ((current == 0x9) ||
                    (current == 0xA) ||
                    (current == 0xD) ||
                    ((current >= 0x20) && (current <= 0xD7FF)) ||
                    ((current >= 0xE000) && (current <= 0xFFFD)) ||
                    ((current >= 0x10000) && (current <= 0x10FFFF)))
                out.append(current);
        }
        return out.toString();
    }

    /**
     * <code>cleanInvalidCharacters</code>
     * return string without invalid characters.
     *
     * @param in @String
     * @return @String
     */
    public static String cleanInvalidCharacters(String in) {
        in = Normalizer.normalize(in, Normalizer.Form.NFD).replaceAll("[^\\x00-\\x7F]", "");
        StringBuilder out = new StringBuilder();
        char current;
        if (in == null || ("".equals(in))) {
            return "";
        }
        for (int i = 0; i < in.length(); i++) {
            current = in.charAt(i);
            if ((current == 0x9) || (current == 0xA) || (current == 0xD) || ((current >= 0x20) && (current <= 0xD7FF)) || ((current >= 0xE000) && (current <= 0xFFFD)) || ((current >= 0x10000))) {
                out.append(current);
            }
        }
        return out.toString().replaceAll("\\s", " ");
    }

    /**
     * <code>regularizeString</code>
     * Gets the regularized string.
     *
     * @param content string content
     * @return regularized string.
     */
    public static String regularizeString(String content){
        return cleanInvalidCharacters(convertStringToUTF8(content.replaceAll("&nbsp;", " ")));
    }

    /**
     *<code>splitStringByLine</code>
     * return a list of line text splited
     * by change line.
     *
     * @param text @String
     * @return @List
     */
    public static List<String> splitStringByLine(String text) {
        if(text.toLowerCase().contains("<strong>") || text.toLowerCase().contains("<code>")) {
            text = cleanPreserveLineBreaks(text);
        }
        List<String> textLines = new ArrayList<>();
        String[] splitText = text.split("\n");
        Collections.addAll(textLines, splitText);
        return textLines;
    }

    /**
     *<code>splitStringByChar</code>
     * return a list of line text splited
     * by change line.
     *
     * @param text @String
     * @return @List
     */
    public static List<String> splitStringByChar(String text, String character) {
        List<String> textLines = new ArrayList<>();
        String[] splitText = text.split(character);
        Collections.addAll(textLines, splitText);
        return textLines;
    }

    /**
     * <code>splitStringByLineAndLength</code>
     * divides the string by changing of line
     * and line width and return the list of
     * text lines.
     *
     * @param text @String
     * @param font @PDFont
     * @param fontSize @Float
     * @param sizeX @Float
     * @param onlyFirstLine @boolean
     * @return @List
     */
    public static List<String> splitStringByLineAndLength(String text, PDFont font, Float fontSize,  Float sizeX, boolean onlyFirstLine) {
        List<String> textLines = new ArrayList<>();
        for (String part : splitStringByLine(text)) {
        	 for (String textPart : getLinesByTextLenght(fontSize, font, html2text(part), sizeX, onlyFirstLine)) {
                 textLines.add(textPart);
        	 }
		}

        return textLines;
    }

    /**
     * <code>splitStringByLineAndLength</code>
     * divides the string by changing of line
     * and line width and return the list of
     * text lines.
     *
     * @param text @String
     * @param font @PDFont
     * @param fontSize @Float
     * @param sizeX @Float
     * @param xWidth @Float
     * @param onlyFirstLine @boolean
     * @return @List
     */
    public static List<String> splitStringByLineAndLength(String text, PDFont font, Float fontSize,  Float sizeX, Float xWidth, boolean onlyFirstLine) {
        List<String> textLines = new ArrayList<>();
        for (String part : splitStringByLine(text)) {
            for (String textPart : getLinesByTextLenght(fontSize, font, html2text(part), sizeX, xWidth, onlyFirstLine)) {
                textLines.add(textPart);
            }
        }

        return textLines;
    }
    
    /**
     * <code>countBeginsCharacter</code>
     * returns the count of matching characters
     * at the beginning of a string.
     *
     * @param content @String
     * @param character @String
     * @return @int
     */
    private static int countBeginsCharacter(String content, String character) {
        int counter = 0;
        for (int i = 0; i < content.length(); i++) {
            Character item = content.charAt(i);
            if (item.toString().equals(character)) {
                counter++;
            } else {
                break;
            }
        }
        return counter;
    }
    
    /**
     * <code>repeatString</code>
     * repeated a number of times defined a
     * character and returns it as string
     *
     * @param character @String
     * @param counter @int
     * @return @String
     */
    private static String repeatString(String character, int counter) {
        String text = "";
        for (int i = 0; i < counter; i++) {
            text += character;
        }
        return text;
    }
    
    /**
     * <code>regularizeHtmlText</code>
     * remove html tags from a string
     * and return the string.
     *
     * @param htmlText @String
     * @return @String
     */
    public static String regularizeHtmlText(String htmlText) {
        htmlText = htmlText.replaceAll("<img", "");
        htmlText = htmlText.replaceAll("(alt|title|style|class)=(\"[^\"]*\")", "");
        htmlText = htmlText.replaceAll("(alt)=(\"[^\"]*\")", "");
        htmlText = htmlText.replaceAll("(style)=(\"[^\"]*\")", "");
        htmlText = htmlText.replaceAll("(class)=(\"[^\"]*\")", "");
        return htmlText;
    }

    /**
     * <code>html2text</code>
     * return the html text like a
     * simple text.
     *
     * @param html @String
     * @return @String
     */
    public static String html2text(String html) {
        int blankCounter = countBeginsCharacter(html, " ");
        html = regularizeString(html);
        EditorKit kit = new HTMLEditorKit();
        Document doc = kit.createDefaultDocument();
        doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
        try {
            Reader reader = new StringReader(html);
            kit.read(reader, doc, 0);
            return repeatString(" ", blankCounter) +  doc.getText(0, doc.getLength()).replace("\n", "");
        } catch (Exception e) {

            return "";
        }
    }

    public static String cleanPreserveLineBreaks(String bodyHtml) {
        return Jsoup.clean(bodyHtml, Whitelist.none().addTags("br")).replaceAll("(?i)<br[^>]*>", "\n");
    }

    /**
     * <code>splitStringByLength</code>
     * return a array of string splited
     * by width line.
     *
     * @param text @text
     * @param lineTextSize @int
     * @return @String[]
     */
    public static String[]  splitStringByLength(String text, Float lineTextSize) {

        //how many linebreaks do I need?
        Integer linebreaks = (int)(text.length() / lineTextSize);

        String[] newText = new String[linebreaks + 1];

        //save each word into an array-element
        String[] parts = text.split(" ");

        //split each word in String into a an array of String text.
        //StringBuffer is necessary because of manipulating text
        StringBuffer[] stringBuffer = new StringBuffer[linebreaks + 1];
        int i = 0; //initialize counter
        int totalTextLength = 0;
        for (int k = 0; k < linebreaks + 1; k++) {
            stringBuffer[k] = new StringBuffer();
            while (true) {

                //avoid NullPointerException
                if (i >= parts.length) break;

                //count each word in String
                totalTextLength = totalTextLength + parts[i].length();

                //put each word in a stringbuffer until string length is > LINE_TEXT_SIZE
                if (totalTextLength > lineTextSize) break;
                stringBuffer[k].append(parts[i]);
                stringBuffer[k].append(" ");
                i++;
            }

            //reset counter, save linebreaked text into the array, finally convert it to a string
            totalTextLength = 0;
            newText[k] = stringBuffer[k].toString();
        }
        return newText;
    }
	
}
