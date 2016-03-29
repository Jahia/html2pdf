package org.jahia.modules.html2pdf.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.jahia.modules.html2pdf.HtmlPdfWriter;
import org.jahia.modules.html2pdf.util.CommonUtil;
import org.jahia.services.content.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PdfDocumentation Class
 * Created by Juan Carlos Rodas
 */
public class PdfDocumentation {

    /* the logger */
    private static Logger logger = LoggerFactory.getLogger(PdfDocumentation.class);

    /* the http servlet response*/
    private HttpServletResponse response;

    /* the jcr session wrapper*/
    private JCRSessionWrapper session;

    /* the document title */
    private String documentTitle;

    /*  the document sub title */
    private String documentSubTitle;

    /* the principal content */
    private String principalContent;

    /* the document node */
    private JCRNodeWrapper documentNode;

    /* the html pdf writer */
    private HtmlPdfWriter pdfWriter;

    /* the jsoup document */
    private org.jsoup.nodes.Document jsoupDocument;

    /* the pdf document */
    private PDDocument document;

    /* the page node list */
    private List<JCRNodeWrapper> listPageNodes = new ArrayList<JCRNodeWrapper>();


    /**
     * <code>PdfDocumentation</code>
     * PdfDocumentation Constructor
     *
     * @param iSession  @JCRSessionWrapper
     * @param request   @HttpServletRequest
     * @param iResponse @HttpServletResponse
     */
    public PdfDocumentation(JCRSessionWrapper iSession, HttpServletRequest request, HttpServletResponse iResponse) {
        this.response = iResponse;
        this.session = iSession;
        String pageNodePath = null;
        JCRNodeWrapper pageNode = null;
        JCRPropertyWrapper propertyNodeList = null;
        String currentNodePath;

        try {
            logger.debug("PdfDocumentation Constructor: begins the initialization.");

            /*register the first current page*/
            pageNodePath = request.getParameter("currentPageNodePath");
            if (StringUtils.isNotEmpty(pageNodePath)) {
                try {
                    pageNode = session.getNode(pageNodePath.trim());
                    listPageNodes.add(pageNode);
                } catch (RepositoryException re) {
                    logger.error("PdfDocumentation Constructor: Error, can't get the current page node[{}], {}", pageNodePath, re.getCause().getMessage());
                }
            }

            /*getting the page node list from module*/
            currentNodePath = request.getParameter("currentNodePath");
            if (StringUtils.isNotEmpty(currentNodePath)) {
                try {
                    pageNode = session.getNode(currentNodePath.trim());
                    if (pageNode.hasProperty("pageList")) {
                        propertyNodeList = pageNode.getProperty("pageList");
                        for (JCRValueWrapper valueWrapper : propertyNodeList.getValues()) {
                            try {
                                pageNode = valueWrapper.getNode();
                                listPageNodes.add(pageNode);
                            } catch (RepositoryException re) {
                                logger.error("PdfDocumentation Constructor: Error, can't get the current page node[{}], {}", pageNodePath, re.getCause().getMessage());
                            }
                        }
                    } else {
                        logger.error("PdfDocumentation Constructor: Error, there is not property wit name 'pageList' the current page module node[{}], {}", currentNodePath);
                    }
                } catch (RepositoryException re) {
                    logger.error("PdfDocumentation Constructor: Error, can't get the current page module node[{}], {}", currentNodePath, re.getCause().getMessage());
                }
            }
        } catch (Exception ex) {
            logger.error("PdfDocumentation Constructor: Error, {}", ex);
        }
    }

    /**
     * Create document pdf byte array output stream.
     *
     * @return the byte array output stream
     * @throws Exception the exception
     */
    public ByteArrayOutputStream createDocumentPdf() throws Exception {
        document = new PDDocument();
        this.pdfWriter = new HtmlPdfWriter(document, session);
        boolean isFirstPage = true;

        for (JCRNodeWrapper pageNode : listPageNodes) {
            this.pdfWriter.openStream();
            processPage(pageNode, isFirstPage);
            isFirstPage = false;
            this.pdfWriter.closeStream();
        }

        this.pdfWriter.closeStream();
        this.pdfWriter.addHeaderToPages(this.documentTitle);
        this.pdfWriter.addFooteToPages();
        this.pdfWriter.closeStream();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        document.save(baos);
        document.close();
        return baos;
    }

    /**
     * <pre>processPage</pre>
     * process each page
     *
     * @param pageNode @JCRNodeWrapper
     * @param isFirstPage @boolean
     */
    private void processPage(JCRNodeWrapper pageNode, boolean isFirstPage) {
        try {
            logger.info("processPage: processing page nodePath[{}]", pageNode);
            JCRNodeWrapper documentNode = geDocumentNode(pageNode);
            if (documentNode != null) {
                String subtitle    = "";
                String content     = "";
                String coloredCode = "";
                String documentTitle    = CommonUtil.getStringValueFromPropertyNode(documentNode, "jcr:title");
                String documentSubTitle = CommonUtil.getStringValueFromPropertyNode(documentNode, "subtitle");
                String principalContent = CommonUtil.getStringValueFromPropertyNode(documentNode, "description");

                /* printing the page title only for the first page. */
                if (isFirstPage)
                    this.pdfWriter.addPrincipalTitlePage(documentTitle, isFirstPage);

                /*adding the document subtitle of the paragraph*/
                if (StringUtils.isNotEmpty(documentSubTitle)) {
                    this.pdfWriter.processSubTitle(documentSubTitle);
                }

                /* adding the principal content of the paragraph */
                if (StringUtils.isNotEmpty(principalContent)) {
                    jsoupDocument = Jsoup.parse(principalContent);
                    //jsoupDocument.outputSettings(new PDDocument().OutputSettings().prettyPrint(false));//makes html() preserve linebreaks and spacing
                    //jsoupDocument.select("br").append("\\n");
                    //jsoupDocument.select("p").prepend("\\n\\n");
                    for (Node node : jsoupDocument.body().childNodes())
                        this.pdfWriter.processNode(node);
                }

                if(StringUtils.isNotEmpty(documentSubTitle) || StringUtils.isNotEmpty(principalContent)){
                    this.pdfWriter.ySpace30();
                }

                /* process paragraph child nodes */
                for (JCRNodeWrapper paragraphNode : documentNode.getNodes()) {
                    subtitle    = CommonUtil.getStringValueFromPropertyNode(paragraphNode, "jcr:title");
                    content     = CommonUtil.getStringValueFromPropertyNode(paragraphNode, "text");
                    coloredCode = CommonUtil.getStringValueFromPropertyNode(paragraphNode, "coloredCode");

                    /* processing subtitle */
                    if (StringUtils.isNotEmpty(subtitle))
                        this.pdfWriter.processSubTitle(subtitle);

                    /* processing html content */
                    if (StringUtils.isNotEmpty(content)) {
                        jsoupDocument = Jsoup.parse(content);
                        //jsoupDocument.select("br").append("\\n");
                        //jsoupDocument.select("p").prepend("\\n\\n");
                        for (Node node : jsoupDocument.body().childNodes())
                            this.pdfWriter.processNode(node);
                    }

                    /* processing colored html code*/
                    if (StringUtils.isNotEmpty(coloredCode)) {
                        coloredCode = "<pre class=\"prettyprint\">" + coloredCode + "</pre>";
                        jsoupDocument = Jsoup.parse(coloredCode);
                        //jsoupDocument.select("br").append("\\n");
                        //jsoupDocument.select("p").prepend("\\n\\n");
                        for (Node node : jsoupDocument.body().childNodes())
                            this.pdfWriter.processNode(node);
                        this.pdfWriter.ySpace30();
                    }

                    /* adding space at the end of the paragraph */
                    this.pdfWriter.ySpace(15F);
                }
            }
        } catch (Exception e) {
            logger.error("processPage: Error processing the page[{}], {}", pageNode.getPath(), e);
        }
    }

    /**
     * <pre>getParagraphNode</pre>
     * Gets the child paragraph node
     * from page node.
     *
     * @param pageNode
     * @return
     */
    private JCRNodeWrapper getParagraphNode(JCRNodeWrapper pageNode) {
        JCRNodeWrapper paragraphNode = null;
        try {
            for (JCRNodeWrapper child : JCRContentUtils.getChildrenOfType(pageNode, "jnt:contentList")) {
                if (child.getName().equals("document-area")) {
                    return child;
                }
            }
        } catch (Exception re) {
            logger.error("getParagraphNode: can't get the document-area node, " + re);
        }
        return paragraphNode;
    }

    /**
     * <pre>geDocumentNode</pre>
     * Gets the child document node
     * from page node.
     *
     * @param pageNode
     * @return
     */
    private JCRNodeWrapper geDocumentNode(JCRNodeWrapper pageNode) {
        JCRNodeWrapper paragraphNode = getParagraphNode(pageNode);
        if (paragraphNode != null) {
            try {
                for (JCRNodeWrapper child : JCRContentUtils.getChildrenOfType(paragraphNode, "jant:document")) {
                    return child;
                }
            } catch (Exception re) {
                logger.error("geDocumentNode: can't get the jant:document node, " + re);
            }
        }
        return null;
    }

}
