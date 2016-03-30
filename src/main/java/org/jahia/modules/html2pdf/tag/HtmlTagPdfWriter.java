package org.jahia.modules.html2pdf.tag;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDBorderStyleDictionary;
import org.jahia.modules.html2pdf.tag.table.BaseTable;
import org.jahia.modules.html2pdf.tag.table.Cell;
import org.jahia.modules.html2pdf.tag.table.Row;
import org.jahia.modules.html2pdf.to.HtmlOlUlManagerTO;
import org.jahia.modules.html2pdf.to.HtmlTagBackgroundTO;
import org.jahia.modules.html2pdf.to.PdfDocumentSecuenceTO;
import org.jahia.modules.html2pdf.to.PdfPictureSizeTO;
import org.jahia.modules.html2pdf.util.*;
import org.jahia.services.content.JCRSessionWrapper;
import org.jsoup.nodes.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HtmlTagPdfWriter Class.
 */
public class HtmlTagPdfWriter {

	/* the logger */
	private static Logger logger = LoggerFactory.getLogger(HtmlTagPdfWriter.class);

	/* the pd document */
	private PDDocument document;

	/* the pd page */
	private PDPage page;

	/* the pd page content stream */
	private PDPageContentStream stream;

	/* the pd document secuence to */
	private PdfDocumentSecuenceTO pdfDocumentSecuence;

	/* the html ol ul manager */
	private HtmlOlUlManagerTO liManager;

	/* the x position */
	private Float xPosition;

	/* the x width word */
	private Float xWidthWord;

	/* the y position */
	private Float yPosition;

	/* write same line */
	private Boolean writeSameLine;

	/* the pd border style dictionary */
	private PDBorderStyleDictionary borderULine;

	/* the pdf image util */
    private PdfImageUtil imageUtil;

	/* the jcr session wrapper */
    private JCRSessionWrapper session;


	/**
	 * <pre>HtmlTagPdfWriter Constructor</pre>
	 *
	 * @param iDocument @PDDocument
	 * @param iPage @PDPage
	 * @param iStream @ PDPageContentStream
	 * @param isession @JCRSessionWrapper
     */
	public HtmlTagPdfWriter(PDDocument iDocument, PDPage iPage, PDPageContentStream iStream, JCRSessionWrapper isession) {
		this.session = isession;
        this.document = iDocument;
		this.page = iPage;
		this.stream = iStream;
		if (yPosition == null) yPosition = PdfPageUtil.getPageStartY();
		if (xPosition == null) xPosition = PdfPageUtil.getPageStartX();
		if (writeSameLine == null) writeSameLine = Boolean.FALSE;
		if (liManager == null) liManager = new HtmlOlUlManagerTO();
		if (borderULine == null) borderULine = new PDBorderStyleDictionary();
        if (imageUtil == null) imageUtil = new PdfImageUtil(document);
		this.borderULine.setStyle(PDBorderStyleDictionary.STYLE_UNDERLINE);
	}

    /**
     * <pre>writeSimpleWord</pre>
     * Write a simple word into the pdf
     *
     * @param contentLine @String
     * @param color @Color
     * @param font @PDFont
     * @param fontSize @Float
     */
	private void writeSimpleWord(String contentLine, Color color, PDFont font, Float fontSize) {
		try {
            /* if need write into the same line, verify if is not end of the line
            *  otherwise change the line*/
			if (writeSameLine) {
				Float xWidthWordLocal = PdfTextUtil.getTextWidthX(contentLine, font, fontSize);
				Float xCalc = xWidthWordLocal + xPosition;

                if (PdfPageUtil.isEndOfPageX(xCalc)) {
					yPosition -= (fontSize * PdfFontUtil.getFontSpaceFactor());
					setPointToStartX();
					eopVerification();
				}
			}else{
				setPointToStartX();
			}

			stream.beginText();
			stream.setFont(font, fontSize);
			stream.setNonStrokingColor(color);
			stream.newLineAtOffset(xPosition, yPosition);
			stream.showText(contentLine);
			stream.endText();

            /* change line if writeSameLine is false */
			if (writeSameLine) {
				xWidthWord = PdfTextUtil.getTextWidthX(contentLine + " ", font, fontSize) + xPosition - PdfPageUtil.getPageMarginX();
				xPosition = PdfTextUtil.getTextWidthX(contentLine + " ", font, fontSize) + xPosition;
			} else {
				yPosition -= (fontSize * PdfFontUtil.getFontSpaceFactor());
				setPointToStartX();
				//eopVerification();
			}

		} catch (IOException e) {
			logger.error("writeSimpleWord: Error trying to add content line to pdf, " + e.getLocalizedMessage());
		}
	}

    /**
     * <pre>writeSimpleWord</pre>
     * Write a simple word into the pdf with
     * specific position into the x and y plan.
     *
     * @param contentStream @PDPageContentStream
     * @param xInitialPosition @Float
     * @param currentLine @Float
     * @param text @String
     * @param font @PDFont
     * @param fontSize @Float
     * @param lineTextWidth @Float
     * @param color @Color
     * @return @Float - current y line
     * @throws @IOException
     */
	public Float writeSimpleWord(PDPageContentStream contentStream, Float xInitialPosition, Float currentLine, String text, PDFont font, Float fontSize, Float lineTextWidth, Color color) throws IOException {
		if (StringUtils.isNotEmpty(text)) {
			for (String lineContent : PdfTextUtil.splitStringByLength(text, lineTextWidth)) {
				contentStream.beginText();
				contentStream.setFont(font, fontSize);
				contentStream.setNonStrokingColor(color);
				contentStream.newLineAtOffset(xInitialPosition, currentLine);
				contentStream.showText(lineContent);
				contentStream.newLine();
				contentStream.endText();
				currentLine -= (fontSize * PdfFontUtil.getFontSpaceFactor());
			}
			return currentLine;
		}
		return (fontSize * PdfFontUtil.getFontSpaceFactor());
	}

    /**
     * <pre>writeSimpleWord</pre>
     * Write a simple word into the pdf with
     * specific position into the x and y plan.
     *
     *
     * @param contentLine @String
     * @param color @Color
     * @param font @PDFont
     * @param fontSize @Float
     * @param xStartPosition @Float
     * @param xPositionLocal @Float
     * @param changeLine @Boolean
     * @return @Float - yPosition
     */
	private Float writeSimpleWord(String contentLine, Color color, PDFont font, Float fontSize, Float xStartPosition, Float xPositionLocal, Boolean changeLine) throws IOException {

        Float xWidthWordLocal = PdfTextUtil.getTextWidthX(contentLine + " " , font, fontSize);
        Float xCalc = xPositionLocal + xWidthWordLocal;

       /* if need write into the same line, verify if is not end of the line
        * otherwise change the line*/
        if (!changeLine){
            if(PdfPageUtil.isEndOfPageX(xCalc)){
                yPosition -= (fontSize * PdfFontUtil.getFontSpaceFactor());
                xPositionLocal = xStartPosition;
                xWidthWord = 0F;
            }
        }

        stream.beginText();
        stream.setFont(font, fontSize);
        stream.setNonStrokingColor(color);
        stream.newLineAtOffset(xPositionLocal, yPosition);
        stream.showText(contentLine);
        stream.endText();

        xPositionLocal = xCalc;

        /* change line if changeLine is true */
        if (changeLine) {
            yPosition -= (fontSize * PdfFontUtil.getFontSpaceFactor());
            xPositionLocal = xStartPosition;
            xWidthWord = 0F;
        }

        return xPositionLocal;
	}

    /**
     * <pre>writeWords</pre>
     * Write a list of word into the pdf with
     * specific position into the x and y plan.
     *
     * @param textLines @List
     * @param font @PDFont
     * @param fontColor @Color
     * @param fontSize @Float
     * @param xStartPosition @Float
     * @param changeLine @Boolean
     */
	private void writeWords(List<String> textLines, PDFont font, Color fontColor, Float fontSize, Float xStartPosition, Boolean changeLine) {
		for (String line : textLines) {
			try {
				eopVerification();
				writeSimpleWord(line, fontColor, font, fontSize, xStartPosition, xStartPosition, changeLine);
			} catch (IOException e) {
                logger.error("writeWords: Error while try to add words to pdf, {}", e.getMessage());
			}
		}
	}

    /**
     * <pre>writeWords</pre>
     * Write a list of word into the pdf with
     * specific position into the x and y plan
     * and diferent position in first line.
     *
     * @param textLines @List
     * @param font @PDFont
     * @param fontColor @Color
     * @param fontSize @Float
     * @param xStartPosition @Float
     * @param xFirstLetter @Float
     * @param changeLine @Boolean
     */
    private void writeWords(List<String> textLines, PDFont font, Color fontColor, Float fontSize, Float xStartPosition, Float xFirstLetter, Boolean changeLine) {
        boolean first = true;
        Float position = null;
        for (String line : textLines) {
            try {
                eopVerification();
                if(first){
                    position = writeSimpleWord(line, fontColor, font, fontSize, xFirstLetter,xFirstLetter, changeLine);
                    first = false;
                }else{
                    position = writeSimpleWord(line, fontColor, font, fontSize, xStartPosition, position, changeLine);
                }
            } catch (IOException e) {
                logger.error("writeWords: Error while try to add words to pdf, {}", e.getLocalizedMessage());
            }
        }
    }

    /**
     * <pre>writeWords</pre>
     * Write a text line into the pdf with
     * specific position into the x.
     *
     * @param textLine @String
     * @param font @PDFont
     * @param fontColor @Color
     * @param fontSize @Float
     * @param xStartPosition @Float
     * @param changeLine @Boolean
     */
    private void writeWords(String textLine, PDFont font, Color fontColor, Float fontSize, Float xStartPosition, Boolean changeLine) {
		try {
			eopVerification();
			writeSimpleWord(textLine, fontColor, font, fontSize, xStartPosition, xStartPosition, changeLine);
		} catch (IOException e) {
            logger.error("writeWords: Error while try to add words to pdf, {}", e.getLocalizedMessage());
		}
	}

    /**
     * <pre>writeWords</pre>
     * Write a text line into the pdf with
     * specific position into the x and add
     * background color to the lines with
     * style zebra.
     *
     * @param textLines @String
     * @param font @PDFont
     * @param fontColor @Color
     * @param fontSize @Float
     * @param tagBackgroundTO @HtmlTagBackgroundTO
     */
	private void writeWords(List<String> textLines, PDFont font, Color fontColor, Float fontSize, HtmlTagBackgroundTO tagBackgroundTO) {
		try {
			/* has background */
			if (tagBackgroundTO != null) {
				Float iCurrentLine = yPosition;
				Float sizeZebraLines = Float.valueOf(textLines.size());
				Float sizeWordLines = sizeZebraLines - 1;
				int counterZebraLines = 0;
				int textCurrentCounter = 0;
				boolean isFirstLine = true;

				do {
                    /*verification of is end of page*/
					if(eopVerification()){
						iCurrentLine = yPosition;
					}

	                /* Adding the first line */
					if (isFirstLine) {
                        if(tagBackgroundTO.getLineTopColor() != null &&
                           tagBackgroundTO.getLineTopPoint() > 0     &&
                           !tagBackgroundTO.getLineTopStyle().equals(LineStyle.NONE)) {
                            drawLine(stream,
                                    PdfPageUtil.getPageStartX(),
                                    yPosition,
                                    PdfPageUtil.getPageEndX() - PdfPageUtil.getPageMarginX(),
                                    -tagBackgroundTO.getLineTopPoint(),
                                    tagBackgroundTO.getLineTopColor(),
                                    tagBackgroundTO.getLineBottomStyle().equals(LineStyle.DASHED));

                            yPosition  -= ((fontSize * PdfFontUtil.getFontSpaceFactor()) + tagBackgroundTO.getLineTopPoint());
                            iCurrentLine = yPosition;
                        }
						isFirstLine = false;
					}

                    /*drawing the background*/
					while ((counterZebraLines <= sizeZebraLines) && tagBackgroundTO.getBackgroundColor() != null) {
						if (PdfPageUtil.isEndOfPageY(iCurrentLine)){
							break;
						}else{
							drawBackground((fontSize * PdfFontUtil.getFontSpaceFactor()), iCurrentLine, tagBackgroundTO.getBackgroundColor());
							iCurrentLine -= (fontSize * PdfFontUtil.getFontSpaceFactor());
							counterZebraLines++;
						}
					}

                    /*writing the text lines*/
					while (sizeWordLines >= textCurrentCounter) {

						if (PdfPageUtil.isEndOfPageY(yPosition)) {
							break;
						}else{
							//setPointToStartX();
							writeSimpleWord(textLines.get(textCurrentCounter), fontColor, font, fontSize);
							textCurrentCounter++;
						}
					}

				} while (counterZebraLines <= sizeZebraLines);

                if(tagBackgroundTO.getLineBottomColor() != null &&
                        tagBackgroundTO.getLineBottomPoint() > 0     &&
                        !tagBackgroundTO.getLineBottomStyle().equals(LineStyle.NONE)) {
                    drawLine(stream,
                            PdfPageUtil.getPageStartX(),
                            (iCurrentLine + (fontSize * PdfFontUtil.getFontSpaceFactor())),
                            PdfPageUtil.getPageEndX() - PdfPageUtil.getPageMarginX(),
                            -tagBackgroundTO.getLineBottomPoint(),
                            tagBackgroundTO.getLineBottomColor(),
                            tagBackgroundTO.getLineBottomStyle().equals(LineStyle.DASHED));
                    yPosition  =  (iCurrentLine + (fontSize * PdfFontUtil.getFontSpaceFactor())) - ((fontSize * PdfFontUtil.getFontSpaceFactor()) + tagBackgroundTO.getLineBottomPoint());
                }

			}/* without background */
			else {
				for (String line : textLines) {
                    xWidthWord = 0F;
                    eopVerification();
					writeSimpleWord(line, fontColor, font, fontSize);
				}
			}
		} catch (Exception e) {
            logger.error("writeWords: Error while try to add words to pdf, {}", e.getLocalizedMessage());
		}
	}

    /**
     * <pre>eopVerification</pre>
     * End of page verification, if is
     * end of page, then add a new page.
     *
     * @return @Boolean
     * @throws @IOException
     */
	private Boolean eopVerification() throws IOException {
		Boolean result = Boolean.FALSE;
		pdfDocumentSecuence = PdfPageUtil.documentEopVerification(document, page, stream, yPosition);
		if (result = pdfDocumentSecuence.isNewPage()) {
			yPosition = pdfDocumentSecuence.getLineNumber();
			stream = pdfDocumentSecuence.getContentStream();
			page = pdfDocumentSecuence.getPage();
		}
		pdfDocumentSecuence = null;
		return result;
	}

    /**
     * <pre>drawBackground</pre>
     * Draw Background in to the pdf.
     *
     * @param size @Float
     * @param iCurrentLine @Float
     * @param background  @Color
     */
	private void drawBackground(Float size, Float iCurrentLine, Color background) {
		try {
			stream.setNonStrokingColor(background);
			stream.addRect(PdfPageUtil.getPageStartX(), iCurrentLine, PdfPageUtil.getPageEndX() - PdfPageUtil.getPageMarginX(), size);
			stream.fill();
		} catch (IOException e) {
			logger.error("drawBackground: Error trying to draw the background, {}", e.getLocalizedMessage());
		}
	}

    /**
     * <pre>drawBackground</pre>
     * Draw Background in to the pdf.
     *
     * @param size @Float
     * @param iCurrentLine @Float
     * @param textBgOne @Color
     * @param textBgTwo @Color
     * @param dark @Boolean
     */
    private void drawZebraBackground(Float size, Float iCurrentLine, Color textBgOne, Color textBgTwo, Boolean dark) {
        try {
            if (dark)
                stream.setNonStrokingColor(textBgOne);
            else
                stream.setNonStrokingColor(textBgTwo);
            stream.addRect(PdfPageUtil.getPageStartX(), iCurrentLine, PdfPageUtil.getPageEndX() - PdfPageUtil.getPageMarginX(), size);
            stream.fill();
        } catch (IOException e) {
            logger.error("drawBackground: Error trying to draw the background, {}", e.getLocalizedMessage());
        }
    }

    /**
     * <pre>drawLine</pre>
     * Draw line into the pdf.
     *
     * @param iContentStream @PDPageContentStream
     * @param ixPosition @Float
     * @param iyPosition @Float
     * @param width  @Float
     * @param height @Float
     * @param color  @Color
     */
    public void drawLine(PDPageContentStream iContentStream, Float ixPosition, Float iyPosition, Float width, Float height, Color color) {
        drawLine(iContentStream, ixPosition, iyPosition, width, height, color, false);
    }

    /**
     * <pre>drawLine</pre>
     * Draw line into the pdf.
     *
     * @param iContentStream @PDPageContentStream
     * @param ixPosition @Float
     * @param iyPosition @Float
     * @param width  @Float
     * @param height @Float
     * @param color  @Color
     */
	public void drawLine(PDPageContentStream iContentStream, Float ixPosition, Float iyPosition, Float width, Float height, Color color, boolean isDashed) {
		try {
             if(!isDashed){
                iContentStream.setNonStrokingColor(color);
                iContentStream.addRect(ixPosition, iyPosition, width, height);
                iContentStream.fill();
              }
             else {
                 Float endPosition = ixPosition + width;
                 for (int counter = 0; ixPosition <= endPosition; counter++) {
                     if (counter % 2 == 0) {
                         iContentStream.setNonStrokingColor(color);
                         iContentStream.addRect(ixPosition, iyPosition, (Math.abs(height) * 4), height);
                         iContentStream.fill();
                         ixPosition += Math.abs(height) * 4;
                     } else {
                         ixPosition += Math.abs(height);
                     }
                 }
             }
        } catch (IOException e) {
			logger.error("drawLine: Error trying to draw line, " + e.getLocalizedMessage());
		}
	}

    /**
     * <pre>isTagPartOfSameLine</pre>
     * return true if the tag must be part
     * of the same line into the pdf.
     *
     * @param tag @String
     * @return @Boolean
     */
	private Boolean isTagPartOfSameLine(String tag) {
		return  tag.equalsIgnoreCase("em") ||
				tag.equalsIgnoreCase("p")  ||
				tag.equalsIgnoreCase("a")  ||
				tag.equalsIgnoreCase("li") ||
				tag.equalsIgnoreCase("ol") ||
				tag.equalsIgnoreCase("ul") ||
                tag.equalsIgnoreCase("div")  ||
                tag.equalsIgnoreCase("code") ||
                tag.equalsIgnoreCase("span") ||
				tag.equalsIgnoreCase("strong");
	}

    /**
     * <pre>getNodeName</pre>
     * Return the node name.
     *
     * @param node @Node
     * @return @String
     */
	private String getNodeName(Node node) {
		return node.nodeName();
	}

    /**
     * <pre>processBeforeNode</pre>
     * Defined task before the node process.
     *
     * @param tag @String
     * @param font @PDFont
     * @param fontColor @Color
     * @param fontSize  @Float
     * @param content   @String
     */
	private void processBeforeNode(String tag, PDFont font, Color fontColor, Float fontSize, String content) {
		writeSameLine = isTagPartOfSameLine(tag);
	}

    /**
     * <pre>processAfterNode</pre>
     * Defined task after the node process.
     *
     * @param node @Node
     */
	private void processAfterNode(Node node) {
		writeSameLine = Boolean.FALSE;
	}

    /**
     * <pre>ySpace</pre>
     * Add space between text lines into the pdf.
     *
     * @param y @Float
     */
	public void ySpace(Float y) {
		yPosition -= y;
	}

    /**
     * <pre>writeSubTitle</pre>
     * Write subtitle into the pdf
     *
     * @param text @String
     * @return @PDPageContentStream
     */
	public PDPageContentStream writeSubTitle(String text){
		String tag = "subtitle";
		String styleClass = null;
        String styleAttribute = null;
		PDFont  font      = PdfFontUtil.getFont(document, tag, styleClass, styleAttribute);
		Color   fontColor = PdfFontUtil.getFontColor(tag, styleClass, styleAttribute);
		Float   fontSize  = PdfFontUtil.getFontSize(tag, styleClass, styleAttribute);

		writeWords(
				PdfTextUtil.splitStringByLineAndLength(text, font, fontSize, null , false),
				font,
				fontColor,
				fontSize,
				null);

		return stream;
	}

    /**
     * <pre>writeTag</pre>
     * Write tag into the pdf.
     *
     * @param node @Node
     * @param text @String
     * @return @PDPageContentStream
     */
	public PDPageContentStream writeTag(Node node, String text){
		return writeTag(node, text, null);
	}

    /**
     * <pre>writeTag</pre>
     * Write tag into the pdf.
     *
     * @param node @Node
     * @param text @String
     * @param ixPosition @Float
     * @return @PDPageContentStream
     */
	public PDPageContentStream writeTag(Node node, String text, Float ixPosition) {
		String tag            = getNodeName(node);
		String styleClass     = (StringUtils.isNotEmpty(node.attr("class")) ? node.attr("class") : null );
        String styleAttribute = (StringUtils.isNotEmpty(node.attr("style")) ? node.attr("style") : null );

		PDFont font = PdfFontUtil.getFont(document, tag, styleClass, styleAttribute);
		Color fontColor = PdfFontUtil.getFontColor(tag, styleClass, styleAttribute);
		Float fontSize = PdfFontUtil.getFontSize(tag, styleClass, styleAttribute);

        HtmlTagBackgroundTO tagBackgroundTO = new HtmlTagBackgroundTO(tag, styleClass, styleAttribute);
        processBeforeNode(tag, font, fontColor, fontSize, text);

        /* if is defined ixPosition*/
		if (ixPosition != null) {
			writeWords(
					PdfTextUtil.splitStringByLineAndLength(text, font, fontSize, ixPosition, false),
					font,
					fontColor,
					fontSize,
					ixPosition,
					!writeSameLine);
		}
        /* otherwise */
        else {
            Boolean hasBackground = HtmlTagBackgroundTO.hasBackground(node);


            if(hasBackground){
                writeSameLine = Boolean.FALSE;
                setPointToStartX();
            }else{
                tagBackgroundTO = null;
            }

            writeWords(
                    PdfTextUtil.splitStringByLineAndLength(text, font, fontSize, writeSameLine ? xWidthWord : null, true),
                    font,
                    fontColor,
                    fontSize,
                    tagBackgroundTO);

		}
		processAfterNode(node);
		tagBackgroundTO = null;
		return stream;
	}

    /**
     * <pre>setPointToStartX</pre>
     * return the x position to start position
     * and set the xWidthWord to 0.
     */
    public void setPointToStartX(){
        xPosition = PdfPageUtil.getPageStartX();
        xWidthWord = 0F;
    }

    /**
     * <pre>changeLineY</pre>
     * change y line by node font size.
     *
     * @param node
     */
	public void changeLineY(Node node)  {
        try {
            String tag = getNodeName(node);
            String styleClass = (StringUtils.isNotEmpty(node.attr("class")) ? node.attr("class") : null);
            String styleAttribute = (StringUtils.isNotEmpty(node.attr("style")) ? node.attr("style") : null);
            Float fontSize = PdfFontUtil.getFontSize(tag, styleClass, styleAttribute);
            Float spaceFactor = PdfFontUtil.getFontSpaceFactor();

            if (tag.equalsIgnoreCase("h1") ||
                    tag.equalsIgnoreCase("h2") ||
                    tag.equalsIgnoreCase("h3") ||
                    tag.equalsIgnoreCase("h4") ||
                    tag.equalsIgnoreCase("h5")) {
                spaceFactor = spaceFactor * 2;
            }

            yPosition -= (fontSize * spaceFactor);
            eopVerification();
            setPointToStartX();
        }catch(IOException ioe){
            logger.error("changeLineY: Error trying to change line, {} ", ioe.getMessage());
        }
	}

    /**
     * <pre>writeTag</pre>
     * Write tag into the pdf with initial x
     * position for first line.
     *
     * @param node @Node
     * @param text @String
     * @param ixPosition @Float
     * @param xWidth @Float
     * @param xFirstLetter @Float
     * @return @PDPageContentStream
     */
    public PDPageContentStream writeTag(Node node, String text, Float ixPosition, Float xWidth, Float xFirstLetter) {
        String tag = getNodeName(node);
        String styleClass     = (StringUtils.isNotEmpty(node.attr("class")) ? node.attr("class") : null );
        String styleAttribute = (StringUtils.isNotEmpty(node.attr("style")) ? node.attr("style") : null );

        PDFont font = PdfFontUtil.getFont(document, tag, styleClass, styleAttribute);
        Color fontColor = PdfFontUtil.getFontColor(tag, styleClass, styleAttribute);
        Float fontSize = PdfFontUtil.getFontSize(tag, styleClass, styleAttribute);

        processBeforeNode(tag, font, fontColor, fontSize, text);

        writeWords(
                PdfTextUtil.splitStringByLineAndLength(text, font, fontSize, xFirstLetter, xWidth, true),
                font,
                fontColor,
                fontSize,
                ixPosition,
                xFirstLetter,
                !writeSameLine);

        processAfterNode(node);
        return stream;
    }

    /**
     * <pre>writeTable</pre>
     * Write table into the pdf.
     *
     * @param node @Node
     * @return @PDPageContentStream
     */
    public PDPageContentStream writeTable(Node node){
    	try {
            PdfTableUtil tableUtil = new PdfTableUtil(node);
			String styleClass      = (StringUtils.isNotEmpty(node.attr("class")) ?node.attr("class") : null );
            String styleAttribute  = (StringUtils.isNotEmpty(node.attr("style")) ? node.attr("style") : null );
    		Integer widthSize      = (100/tableUtil.getColumnSize());
			Boolean first          = Boolean.TRUE;
			String tag             = node.nodeName();
			String tagHeader       = tag + ".header";
			String tagCell         = tag + ".cell";
			Row<PDPage> row;
			Cell<PDPage> cell;
			
			/* Header table configuration */
			PDFont fontHeader      = PdfFontUtil.getFont(document, tagHeader, styleClass, styleAttribute);
			Color  fontHeaderColor = PdfFontUtil.getFontColor(tagHeader, styleClass, styleAttribute);
			Float  fontHeaderSize  = PdfFontUtil.getFontSize(tagHeader, styleClass, styleAttribute);
			Color  textBgHeader    = PdfColorUtil.getBackgroundTextColor(tagHeader);
			
			/* Header cell configuration */
			PDFont fontCell        = PdfFontUtil.getFont(document, tagCell, styleClass, styleAttribute);
			Color  fontCellColor   = PdfFontUtil.getFontColor(tagCell, styleClass, styleAttribute);
			Float  fontCellSize    = PdfFontUtil.getFontSize(tagCell, styleClass, styleAttribute);
			Color  textBgCell      = PdfColorUtil.getBackgroundTextColor(tagCell);
		
			BaseTable table        = new BaseTable(yPosition,
                                     PdfPageUtil.getPageStartY(),
                                     PdfPageUtil.getPageEndY(),
                                     PdfPageUtil.getLineWidthX(),
                                     PdfPageUtil.getPageMarginX(),
                                     this.document, this.page,
                                     true, true);
			
			
			for (String[] fact : tableUtil.getRows()) {
				row = table.createRow((first ? fontHeaderSize : fontCellSize));
				
	            for (int i = 0; i < fact.length; i++) {
	                cell = row.createCell(widthSize, fact[i]);
	                cell.setFont(first ? fontHeader : fontCell);
	                cell.setFontSize(first ? fontHeaderSize : fontCellSize);
	                cell.setTextColor(first ? fontHeaderColor : fontCellColor);
	                
	                if(first && textBgHeader != null)
	                	cell.setFillColor(textBgHeader);
	                else if (!first && textBgCell != null)
	                	cell.setFillColor(textBgCell);
	            }
	            first = Boolean.FALSE;
	        }
	        yPosition = table.draw();
		} catch (IOException e) {
            logger.error("writeTable: Error trying to write table, {} ", e.getMessage());
		}
	
		return stream;
	}

    /**
     * <pre>writeLI</pre>
     * Write li into the pdf.
     *
     * @param isOl @Boolean
     * @param liNode @Node
     */
	private void writeLI(Boolean isOl, Node liNode){
		String styleClass     = (StringUtils.isNotEmpty(liNode.attr("class")) ? liNode.attr("class") : null );
        String styleAttribute = (StringUtils.isNotEmpty(liNode.attr("style")) ? liNode.attr("style") : null );
		Float  fontSize       = PdfFontUtil.getFontSize(liNode.nodeName(), styleClass, styleAttribute);
		PDFont font           = PdfFontUtil.getFont(document, liNode.nodeName(), styleClass, styleAttribute);
		Color  fontColor      = PdfFontUtil.getFontColor(liNode.nodeName(), styleClass, styleAttribute);
		Float position        = 0F;
		writeSameLine         = true;

        /* if is part of ol tag */
		if(isOl){
			liManager.registerOlLi();
			position =  liManager.getOlIndex() <= 1 ? PdfPageUtil.getPageStartX() + 15 : PdfPageUtil.getPageStartX() + 15 + (5 * liManager.getOlIndex());
			drawOrdererBullet(liManager.getOlIndex(),  font,  fontSize, fontColor);
		}
        /* if is part of ul tag */
        else{
			liManager.registerUlLi();
			position =  liManager.getUlIndex() <= 1 ? PdfPageUtil.getPageStartX() + 15 : PdfPageUtil.getPageStartX() + 15 + (5 * liManager.getUlIndex());
			drawLineBullet(liManager.getUlIndex(), 6F, fontSize);
		}

		xPosition = position;
        subProcessLiNode(liNode, position, position);
        xPosition = PdfPageUtil.getPageStartX();
		writeSameLine = false;
	}

    /**
     * <pre>writeUL</pre>
     * Write Ul into the pdf.
     *
     * @param node @Node
     */
	public PDPageContentStream writeUL(Node node){
		return writeUL(node, false);
	}

    /**
     * <pre>writeUL</pre>
     * Write Ul into the pdf with change line.
     *
     * @param node @Node
     * @param changeLine @Boolean
     */
	public PDPageContentStream writeUL(Node node, Boolean changeLine){
		liManager.registerUl();
		String styleClass     = (StringUtils.isNotEmpty(node.attr("class")) ?node.attr("class") : null );
        String styleAttribute = (StringUtils.isNotEmpty(node.attr("style")) ? node.attr("style") : null );
		Float  fontSize       = PdfFontUtil.getFontSize(node.nodeName(), styleClass, styleAttribute);

		if(changeLine)
			yPosition -= (fontSize * PdfFontUtil.getFontSpaceFactor());

		for(Node childNode: node.childNodes()){
            if(!childNode.childNodes().isEmpty() && StringUtils.isNotEmpty(childNode.toString())){
                writeLI(false, childNode);
            }else{
                yPosition -= (fontSize * PdfFontUtil.getFontSpaceFactor());
            }
		}
		liManager.unregisterUl();
        setPointToStartX();
		return stream;
	}

    /**
     * <pre>writeOL</pre>
     * Write Ol into the pdf.
     *
     * @param node @Node
     */
	public PDPageContentStream writeOL(Node node){
		return writeOL(node, false);
	}

    /**
     * <pre>writeOL</pre>
     * Write Ol into the pdf with change line.
     *
     * @param node @Node
     * @param changeLine @Boolean
     */
	public PDPageContentStream writeOL(Node node, Boolean changeLine){
		liManager.registerOl();
		String styleClass     = (StringUtils.isNotEmpty(node.attr("class")) ?node.attr("class") : null );
        String styleAttribute = (StringUtils.isNotEmpty(node.attr("style")) ? node.attr("style") : null );
		Float   fontSize  = PdfFontUtil.getFontSize(node.nodeName(), styleClass, styleAttribute);
		if(changeLine)
			yPosition -= (fontSize * PdfFontUtil.getFontSpaceFactor());

		for(Node childNode: node.childNodes()){
			if(!childNode.childNodes().isEmpty() && StringUtils.isNotEmpty(childNode.toString())){
                writeLI(true, childNode);
			}else{
                yPosition -= (fontSize * PdfFontUtil.getFontSpaceFactor());
            }
		}
		liManager.unregisterOl();
        setPointToStartX();
		return stream;
	}

    /**
     * <pre>writeImage</pre>
     * Draw image into the pdf file.
     *
     * @param imgNode @Node
     * @return @PDPageContentStream
     */
    public PDPageContentStream writeImage(Node imgNode){
        String imageSrc = "";

        if (imgNode.hasAttr("src")) {
            try {
                String[] imageSrcPart = imgNode.attr("src").split("\\{workspace}");
                imageSrc = imageSrcPart[1].trim();
                PDImageXObject pdImage = imageUtil.getParagraphImageFromSrc(imageSrc, session);
                if (pdImage != null) {
                    yPosition -= 3;
                    PdfPictureSizeTO sizeXY = this.imageUtil.resizePicture(pdImage);
                    if (PdfPageUtil.isEndOfPageY(yPosition - sizeXY.getHeight())) {
                        pdfDocumentSecuence = PdfPageUtil.documentEopVerification(document, page, stream, (yPosition - sizeXY.getHeight()));
                        yPosition = pdfDocumentSecuence.getLineNumber();
                        stream = pdfDocumentSecuence.getContentStream();
                    }
                    stream.drawImage(pdImage, PdfPageUtil.getPageStartX(), yPosition - (sizeXY.getHeight()), sizeXY.getWidth(), sizeXY.getHeight());
                    yPosition = (yPosition - sizeXY.getHeight() - 10);
                }
            }catch (Exception e) {
                logger.error("writeImage: Error trying to add the image [{}] to the pdf, ", imageSrc, e.getLocalizedMessage());
            }
        }
        setPointToStartX();
        return stream;
    }

    /**
     * <pre>drawLineBullet</pre>
     * Draw Line Bullet into the pdf.
     *
     * @param xIndex @Float
     * @param size @Float
     * @param fontSize @Float
     */
    private void drawLineBullet(Float xIndex, Float size, Float fontSize) {
    	try {
			eopVerification();
			Float position = (xIndex <= 1 ? PdfPageUtil.getPageStartX() : PdfPageUtil.getPageStartX() + (5 * xIndex) );
	        drawLine(stream,
	    		     position,
	    		     yPosition + (fontSize / 3),
	    		     size,
	    		     1F,
	                 new Color(88, 88, 91));
		} catch (IOException e) {
            logger.error("drawLineBullet: Error trying to draw the bullet into the pdf, ", e.getMessage());
		}
    }

    /**
     * <pre>drawCubeBullet</pre>
     * Draw Cube Bullet into the pdf.
     *
     * @param xIndex @Float
     * @param size @Float
     */
	private void drawCubeBullet(Float xIndex, Float size) {
    	try {
			eopVerification();
			Float position = (xIndex <= 1 ? PdfPageUtil.getPageStartX() : PdfPageUtil.getPageStartX() + (5 * xIndex) );
	        drawLine(stream,
	    		    position,
	    		    yPosition,
	    		    size,
	                size,
	                new Color(127, 127, 127));
		} catch (IOException e) {
            logger.error("drawCubeBullet: Error trying to draw the cube into the pdf, ", e.getMessage());
		}
    }

    /**
     * <pre>drawOrdererBullet</pre>
     * Draw Orderer Bullet into the pdf.
     *
     * @param xIndex @Float
     * @param font @PDFont
     * @param fontSize @Float
     * @param fontColor @Color
     */
    private void drawOrdererBullet(Float xIndex, PDFont font, Float fontSize, Color fontColor) {
    	try {
			eopVerification();
	        Float position = (xIndex <= 1 ? PdfPageUtil.getPageStartX() : PdfPageUtil.getPageStartX() + (5 * xIndex) );       
	        writeWords(
					liManager.getOlLiIndex().intValue() + ".",
					font, 
					fontColor, 
					fontSize,
					position, 
					false);
		} catch (IOException e) {
            logger.error("drawOrdererBullet: Error trying to draw the number into the pdf, ", e.getMessage());
		}
    }

    /**
     * <pre>subProcessLiNode</pre>
     * process child tag into the li node.
     *
     * @param node @Node
     * @param startPosition @Float
     * @param position @Float
     * @return xPosition @Float
     */
    public Float subProcessLiNode(Node node, Float startPosition, Float position){
        boolean wsl = isTagPartOfSameLine(node.parent().nodeName());
        Boolean hasBackground = HtmlTagBackgroundTO.hasBackground(node);

        if(node.nodeName().equalsIgnoreCase("#text")){
            if(wsl){
                Float widhTxt = PdfPageUtil.getLineWidthX() - (startPosition - PdfPageUtil.getPageMarginX());
                this.writeTag(node.parent(), node.toString(),startPosition, widhTxt, position);

                String tag            = getNodeName(node);
                String styleClass     = (StringUtils.isNotEmpty(node.attr("class")) ?node.attr("class") : null );
                String styleAttribute = (StringUtils.isNotEmpty(node.attr("style")) ? node.attr("style") : null );

                PDFont font    = PdfFontUtil.getFont(document, tag, styleClass, styleAttribute);
                Float fontSize = PdfFontUtil.getFontSize(tag, styleClass, styleAttribute);
                position      += PdfTextUtil.getTextWidthX(node.toString(), font, fontSize);
            }else{
                this.writeTag(node.parent(), node.toString());
            }
        }else if(node.nodeName().equalsIgnoreCase("table")){
            stream = writeTable(node);
        }else if(node.nodeName().equalsIgnoreCase("ul")){
            stream = writeUL(node, true);
        }else if(node.nodeName().equalsIgnoreCase("ol")){
            stream = writeOL(node, true);
        }
        else{
            if(hasBackground){
                ySpace(5F);
                this.writeTag(node, node.toString());
                setPointToStartX();
                ySpace(5F);
            }else {
                for (Node chNode : node.childNodes()) {
                    //processNode(chNode);
                    position = subProcessLiNode(chNode, startPosition, position);
                }
            }
        }
        return position;
    }

    /**
     * <pre>getStream</pre>
     * Gets the PDPageContentStream
     *
     * @return @PDPageContentStream
     */
    public PDPageContentStream getStream() {
		return stream;
	}

    /**
     * <pre>setStream</pre>
     * Sets the PDPageContentStream
     *
     * @param stream @PDPageContentStream
     */
	public void setStream(PDPageContentStream stream) {
		this.stream = stream;
	}

    /**
     * <pre>getxPosition</pre>
     * Gets the x positon
     *
     * @return @Float
     */
	public Float getxPosition() {
		return xPosition;
	}

    /**
     * <pre>setxPosition</pre>
     * Sets the x position.
     *
     * @param xPosition @Float
     */
	public void setxPosition(Float xPosition) {
		this.xPosition = xPosition;
	}

    /**
     * <pre>getyPosition</pre>
     * Gets the y position
     *
     * @return @Float
     */
	public Float getyPosition() {
		return yPosition;
	}

    /**
     * <pre>setyPosition</pre>
     * Sets the y position.
     *
     * @param yPosition
     */
	public void setyPosition(Float yPosition) {
		this.yPosition = yPosition;
	}

    /**
     * <pre>setPage</pre>
     * Sets the PDPage
     *
     * @param ipage @PDPage
     */
    public void setPage(PDPage ipage){
        this.page = ipage;
    }

    /**
     * <pre>getPage</pre>
     * Gets the pdPage
     *
     * @return @PDPage
     */
    public PDPage getPage(){
        return this.page;
    }

}