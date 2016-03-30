package org.jahia.modules.html2pdf;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.jahia.modules.html2pdf.config.ConfigurationMap;
import org.jahia.modules.html2pdf.tag.HtmlTagPdfWriter;
import org.jahia.modules.html2pdf.to.HtmlTagBackgroundTO;
import org.jahia.modules.html2pdf.to.PdfDocumentSecuenceTO;
import org.jahia.modules.html2pdf.util.*;
import org.jahia.services.content.JCRSessionWrapper;
import org.jsoup.examples.HtmlToPlainText;
import org.jsoup.nodes.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class HtmlPdfWriter.
 */
@SuppressWarnings("unused")
public class HtmlPdfWriter {
	
    /** The logger. */
    private static Logger logger = LoggerFactory.getLogger(HtmlPdfWriter.class);
	
	/** The document. */
	private PDDocument document;
	
	/** The page. */
	private PDPage page;
	
	/** The stream. */
	private PDPageContentStream stream;
	
	/** The pdf document secuence. */
	private PdfDocumentSecuenceTO pdfDocumentSecuence;

	/** The tag writer. */
	private HtmlTagPdfWriter tagWriter;

	/** The jcr session wrapper */
    private JCRSessionWrapper session;

	/** The pdf image util */
	private PdfImageUtil imageUtil;

	/** the principal page title map */
    Map<Integer, String> principalPageTitleMap;

    private HtmlToPlainText htmlToPlainText;

	
	/**
	 * Instantiates a new html pdf writer.
	 *
	 * @param iDocument the i document
	 */
	public HtmlPdfWriter(PDDocument iDocument, JCRSessionWrapper isession){
		try {
			this.document = iDocument;
            this.session = isession;
			this.page = new PDPage();
			this.document.addPage(page);
			this.stream = new PDPageContentStream(this.document, page, true, true);
            PdfPageUtil.fillBackgroundColorPage(this.stream);
            this.tagWriter = new HtmlTagPdfWriter(this.document, this.page, this.stream, session);
			this.imageUtil = new PdfImageUtil(this.document);
            this.principalPageTitleMap = new HashMap<Integer, String>();
            this.htmlToPlainText = new HtmlToPlainText();
		}catch(IOException ioe){
            logger.error("HtmlPdfWriter: Error trying to instantiate the HtmlPdfWriter.class, {}", ioe.getMessage());
        }
	}

    /**
     * <pre>openStream</pre>
     * open stream if is closed.
     *
     * @throws IOException
     */
    public void openStream() throws IOException {
        if (this.stream == null || this.tagWriter.getStream() == null) {
            try {
                this.page = new PDPage();
                this.document.addPage(page);
                this.stream = new PDPageContentStream(document, page, true, true);
                PdfPageUtil.fillBackgroundColorPage(this.stream);
                this.tagWriter.setyPosition(PdfPageUtil.getPageStartY());
                this.tagWriter.setxPosition(PdfPageUtil.getPageStartX());
                this.tagWriter.setStream(stream);
            } catch (IOException e) {
                logger.error("openStream: Error trying to open the PdPageContentStream, {}", e.getMessage());
            }
        }
    }

    /**
     * Y space10.
     */
    public void ySpace5(){
        tagWriter.ySpace(5F);
    }

    /**
	 * Y space10.
	 */
	public void ySpace10(){
		tagWriter.ySpace(10F);
	}
	
	/**
	 * Y space20.
	 */
	public void ySpace20(){
		tagWriter.ySpace(20F);
	}
	
	/**
	 * Y space30.
	 */
	public void ySpace30(){
		tagWriter.ySpace(30F);
	}

    /**
     * Y space int.
     */
    public void ySpace(Float space){
        tagWriter.ySpace(space);
    }

    /**
     * <pre>startNewLine</pre>
     *
     * @param tag
     * @return true if tag need a new line
     *         otherwise false
     */
    private Boolean startNewLine(String tag) {
        return  tag.equalsIgnoreCase("p")  ||
                tag.equalsIgnoreCase("h1") ||
                tag.equalsIgnoreCase("h2") ||
                tag.equalsIgnoreCase("h3") ||
                tag.equalsIgnoreCase("h4") ||
                tag.equalsIgnoreCase("h5") ;
    }


	/**
	 * Process node.
	 *
	 * @param node the node
	 */
	public void processNode(Node node){

        Boolean hasBackground = HtmlTagBackgroundTO.hasBackground(node);
        String tag = node.nodeName();
        if(startNewLine(tag)){ tagWriter.changeLineY(node); }

        if(node.nodeName().equalsIgnoreCase("#text")){
            stream = tagWriter.writeTag(node.parent(), node.toString());
        }
        else if(node.nodeName().equalsIgnoreCase("img")){
            ySpace5();
            stream = tagWriter.writeImage(node);
            ySpace5();
        }
        else if(node.nodeName().equalsIgnoreCase("table")){
            stream = tagWriter.writeTable(node);
        }
        else if(node.nodeName().equalsIgnoreCase("ul")){
            stream = tagWriter.writeUL(node);
        }
        else if(node.nodeName().equalsIgnoreCase("ol")){
            stream = tagWriter.writeOL(node);
        }
        else{
            if(hasBackground){
                ySpace5();
                stream = tagWriter.writeTag(node, node.toString());
                ySpace5();
            }else {
                for (Node chNode : node.childNodes()) {
                    processNode(chNode);
                }
            }
        }
	}

    /**
     * <pre>processSubTitle</pre>
     * write subtitle into the pdf.
     *
     * @param text
     */
	public void processSubTitle(String text){
		stream = tagWriter.writeSubTitle(text);
	}

    /**
     * <pre>addHeaderToPages</pre>
     * add the header to all pdf pages.
     *
     * @throws IOException
     */
	public void addHeaderToPages() throws IOException {
		addHeaderToPages(null);
	}

    /**
     * <pre>addHeaderToPages</pre>
     * add the header to all pdf pages.
     *
     * @param  title @String
     * @throws IOException
     */
	public void addHeaderToPages(String title) throws IOException {
        String styleClass     = null;
        String styleAttribute = null;

		 /* Line configuration */
		 String lineColor     = ConfigurationMap.getProperty("header.line.color");
		 String lineHeight    = ConfigurationMap.getProperty("header.line.height");
		 String linePositionY = ConfigurationMap.getProperty("header.line.position.y");
		 
		 /* Text configuration */
		 String tag            = "header.title";
	     String titleText      = StringUtils.isNotEmpty(title) ? title : ConfigurationMap.getProperty("header.title.text");
	     String titlePositionX = ConfigurationMap.getProperty("header.title.position.x");
	     String titlePositionY = ConfigurationMap.getProperty("header.title.position.y");
	    	
	     /* Font configuration */
    	 Color  fontColor      = PdfFontUtil.getFontColor(tag, styleClass, styleAttribute);
    	 PDFont font           = PdfFontUtil.getFont(document, tag, styleClass, styleAttribute);
    	 Float  fontSize       = PdfFontUtil.getFontSize(tag, styleClass, styleAttribute);
    	 
		 /* Image configuration */
    	 String imageSrc       = ConfigurationMap.getProperty("header.image.src");
	     String imagePositionX = ConfigurationMap.getProperty("header.image.position.x");
	     String imagePositionY = ConfigurationMap.getProperty("header.image.position.y");
	     String imageWidth     = ConfigurationMap.getProperty("header.image.width");
	     String imageHeight    = ConfigurationMap.getProperty("header.image.height");

         PDPageTree pages = this.document.getPages();

	     for (int i = 0; i < pages.getCount(); i++) {
             if(principalPageTitleMap.containsKey((i+1))){
                 titleText = principalPageTitleMap.get(i+1);
                 continue;
             }

             PDPage page = pages.get(i);
             PDPageContentStream iContentStream = new PDPageContentStream(document, page, true, true);

             if(StringUtils.isNotEmpty(lineColor) &&
                     StringUtils.isNotEmpty(lineHeight)&&
                     StringUtils.isNotEmpty(linePositionY)){
                      tagWriter.drawLine(iContentStream,
                           PdfPageUtil.getPageStartX(),
                           Float.valueOf(linePositionY),
                           PdfPageUtil.getLineWidthX(),
                           Float.valueOf(lineHeight),
                           PdfColorUtil.getColorByHex(lineColor));

                      }

             if(StringUtils.isNotEmpty(titleText) &&
                StringUtils.isNotEmpty(titlePositionX) &&
                StringUtils.isNotEmpty(titlePositionY) &&
                StringUtils.isNotEmpty(titleText)){
                 tagWriter.writeSimpleWord(iContentStream,
                           PdfPageUtil.getPageStartX(),
                           Float.valueOf(titlePositionY),
                           titleText,
                           font,
                           fontSize,
                           PdfPageUtil.getLineWidthX(),
                           fontColor);
                 }

             if(StringUtils.isNotEmpty(imageSrc) &&
                StringUtils.isNotEmpty(imagePositionX) &&
                StringUtils.isNotEmpty(imagePositionY) &&
                StringUtils.isNotEmpty(imageWidth) &&
                StringUtils.isNotEmpty(imageHeight)){

                    addImageToPdf(iContentStream,
                           imageSrc,
                           Float.valueOf(imagePositionX),
                           Float.valueOf(imagePositionY),
                           Float.valueOf(imageWidth),
                           Float.valueOf(imageHeight));
             }else{
                addModuleImageToPdfHeaderRight(iContentStream, Constants.IMG_JAHIA_LOGO_WM_NAME);
             }
             iContentStream.close();
         }
    }

    /**
     * <pre>addFooterToPages</pre>
     * add the header to all pdf pages.
     *
     * @throws IOException
     */
    public void addFooteToPages() throws IOException {
    	String tag = "footer.legend";
        String styleClass     = null;
        String styleAttribute = null;

        /* line configuration */
    	String lineColor     = ConfigurationMap.getProperty("footer.line.color");
    	String lineHeight    = ConfigurationMap.getProperty("footer.line.height");
    	String linePositionY = ConfigurationMap.getProperty("footer.line.position.y");

        /* text configuration */
        String pageText      = ConfigurationMap.getProperty("footer.page.text");
    	String legendText    = ConfigurationMap.getProperty("footer.legend.text");

        /* font configuration */
        Color  fontColor     = PdfFontUtil.getFontColor(tag, styleClass, styleAttribute);
    	PDFont font          = PdfFontUtil.getFont(document, tag, styleClass, styleAttribute);
    	Float  fontSize      = PdfFontUtil.getFontSize(tag, styleClass, styleAttribute);
    	Float spaceFactor    = Float.valueOf(linePositionY) - Float.valueOf(lineHeight) - (fontSize * PdfFontUtil.getFontSpaceFactor());
  	
    	/* Print the footer if position exists */
    	if(StringUtils.isNotEmpty(linePositionY)){
    		PDPageTree pages = this.document.getPages();
            int numberPages = pages.getCount();
            
            for (int i = 0; i < pages.getCount(); i++) {
                if(principalPageTitleMap.containsKey((i+1))){
                    continue;
                }

                PDPage page = pages.get(i);
                PDPageContentStream iContentStream = new PDPageContentStream(document, page, true, true);

                /* setting the principal line */
                if(StringUtils.isNotEmpty(lineHeight) && StringUtils.isNotEmpty(lineColor)){
	                tagWriter.drawLine(iContentStream,
	                				   PdfPageUtil.getPageStartX(),
	                				   Float.valueOf(linePositionY), 
	                				   PdfPageUtil.getLineWidthX(), 
	                				   Float.valueOf(lineHeight), 
	                				   PdfColorUtil.getColorByHex(lineColor));
                }

                if(StringUtils.isNotEmpty(legendText)){
	                tagWriter.writeSimpleWord(iContentStream,
	                			 PdfPageUtil.getPageStartX(),
	                			 spaceFactor,
	                			 legendText,
	                			 font,
	                             fontSize,
	                             PdfPageUtil.getLineWidthX(),
	                             fontColor);
                }

                String textRightPosition = (StringUtils.isNotEmpty(pageText) ? pageText + " " : "") + (i + 1) + " / " + numberPages;
                Float positionRightX = PdfTextUtil.getTextWidthX(textRightPosition, font, fontSize);  
                
                tagWriter.writeSimpleWord(iContentStream,
           			 PdfPageUtil.getPageEndX() - positionRightX,
           			 spaceFactor,
           			 textRightPosition,
           			 font,
                     fontSize,
                     PdfPageUtil.getLineWidthX(),
                     fontColor);

                iContentStream.close();
            }
    	}
    }

    /**
     * <pre>addImageToPdf</pre>
     * draw image into the pdf.
     *
     * @param iContentStream @PDPageContentStream
     * @param imageSrc @String
     * @param xPosition @Float
     * @param yPosition @Float
     * @param width @Float
     * @param height @Float
     */
    private void addImageToPdf(PDPageContentStream iContentStream, String imageSrc, Float xPosition, Float yPosition, Float width, Float height) {
        try {
            PDImageXObject pdImage = this.imageUtil.getPDImageFromSrc(imageSrc);
            if (pdImage != null)
                iContentStream.drawImage(pdImage, xPosition, yPosition, width, height);
        } catch (Exception e) {
            logger.error("addImageToPdf: Error getting the image [{}]", imageSrc, e);
        }

    }

    /**
     * <pre>addPrincipalTitlePage</pre>
     * write the principal title for
     * the title page.
     *
     * @param title @String
     * @param isFirstPage @Boolean
     * @throws @IOException
     */
    public void addPrincipalTitlePage(String title, boolean isFirstPage) throws IOException {

        principalPageTitleMap.put(this.document.getNumberOfPages(), title);
        Float yTitlePosition = ((( PdfPageUtil.getPageHeightY()) / 2) + 50);
        addModuleImageToPdfHeaderRight(stream, Constants.IMG_JAHIA_LOGO_NAME);
        addModuleImageBGToPdf(stream, Constants.IMG_JAHIA_BG_NAME, 0, 0, 0, 150, 1);

        String tag            = "principal.page.title";
        String styleClass     = null;
        String styleAttribute = null;
        PDFont font           = PdfFontUtil.getFont(document, tag, styleClass, styleAttribute);
        Float fontTitleSize   = PdfFontUtil.getFontSize(tag, styleClass, styleAttribute);

        PDStreamUtils.addTextToPdf(document,
                stream,
                (PdfPageUtil.getPageMarginX() * 2),
                yTitlePosition,
                title.toUpperCase(),
                font,
                fontTitleSize,
                (PdfPageUtil.getPageMarginX() * 2) ,
                255, 255, 255);
        closeStream();
        addNewPage();
    }

    /**
     * <pre>addModuleImageToPdfHeaderRight</pre>
     * Draw image from module into the right of
     * the header page.
     *
     * @param iContentStream
     * @param imageName
     */
    private void addModuleImageToPdfHeaderRight(PDPageContentStream iContentStream, String imageName) {
        try {
            PDImageXObject pdImage = this.imageUtil.getPDImageFromModule(document, imageName);
            if (pdImage != null)
                iContentStream.drawImage(pdImage, PdfPageUtil.getPageEndX() - (pdImage.getWidth() * Constants.IMG_JAHIA_SCALE),  PdfPageUtil.getPageHeightY() - (10) - (pdImage.getHeight() * Constants.IMG_JAHIA_SCALE), pdImage.getWidth() * Constants.IMG_JAHIA_SCALE, pdImage.getHeight() * Constants.IMG_JAHIA_SCALE);
        } catch (Exception e) {
            logger.error("addModuleImageToPdfHeaderRight: Error getting the image {}",imageName, e.getLocalizedMessage());
        }

    }

    /**
     * <pre>addModuleImageBGToPdf</pre>
     * Draw image background for the
     * title page.
     *
     * @param iContentStream @PDPageContentStream
     * @param imageName @Sting
     * @param xStartPosition @int
     * @param yStartPosition @int
     * @param xRelativeMinus @int
     * @param yRelativeMinus @int
     * @param imgScale @int
     */
    private void addModuleImageBGToPdf(PDPageContentStream iContentStream, String imageName, int xStartPosition, int yStartPosition, int xRelativeMinus, int yRelativeMinus, int imgScale) {
        try {
            PDImageXObject pdImage = this.imageUtil.getPDImageFromModule(this.document, imageName);
            if (pdImage != null)
                iContentStream.drawImage(pdImage, xStartPosition, yStartPosition, PdfPageUtil.getPageWidthX() - xRelativeMinus, PdfPageUtil.getPageHeightY() - yRelativeMinus);
        } catch (Exception e) {
            logger.error("addModuleImageBGToPdf: Error getting the image {}", imageName);
        }
    }

    /**
     * <pre>addNewPage</pre>
     * Add a new pdf page.
     *
     * @throws IOException
     */
    public void addNewPage() throws IOException {
        closeStream();
        this.page = new PDPage();
        this.document.addPage(page);
        this.tagWriter.setPage(page);
        this.stream = new PDPageContentStream(document, page, true, true);
        PdfPageUtil.fillBackgroundColorPage(this.stream);
        this.tagWriter.setyPosition(PdfPageUtil.getPageStartY());
        this.tagWriter.setxPosition(PdfPageUtil.getPageStartX());
        this.tagWriter.setStream(stream);
    }

    /**
     * <pre>closeStream</pre>
  	 * Close the pd page content stream.
  	 */
  	public void closeStream() {
        if (this.stream != null) {
            try {
                this.stream.close();
                this.stream = null;
                if(tagWriter.getStream() != null){
                    tagWriter.getStream().close();
                    tagWriter.setStream(null);
                }
            } catch (IOException e) {
                logger.error("closeStream: Error trying to close the PdPageContentStream, " + e.getLocalizedMessage());
            }
        }
    }

}
