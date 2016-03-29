package org.jahia.modules.html2pdf.action;

import org.apache.commons.lang.StringUtils;
import org.jahia.bin.Action;
import org.jahia.bin.ActionResult;
import org.jahia.modules.html2pdf.config.ConfigurationMap;
import org.jahia.modules.html2pdf.exception.Html2PdfException;
import org.jahia.modules.html2pdf.impl.PdfDocumentation;
import org.jahia.modules.html2pdf.util.CommonUtil;
import org.jahia.modules.html2pdf.util.PdfTextUtil;
import org.jahia.modules.html2pdf.util.StyleTag2MapUtil;
import org.jahia.services.content.JCRNodeIteratorWrapper;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.content.JCRTemplate;
import org.jahia.services.content.decorator.JCRFileContent;
import org.jahia.services.render.RenderContext;
import org.jahia.services.render.Resource;
import org.jahia.services.render.URLResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.RepositoryException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Created by Juan Carlos Rodas.
 */
public class Html2PdfAction extends Action {

    /* the logger */
    private static Logger logger = LoggerFactory.getLogger(Html2PdfAction.class);

    /* the documentation */
    private PdfDocumentation documentation;

    /* the pdf class definition */
    private String pdfClassDefinition;

    /* the pdf style definition */
    private String pdfStyleDefinition;

    /* the pdf props definition */
    private String pdfPropsDefinition;

    /* the document paragraph node path */
    private String documentParagraphNodePath;


    /** {@inheritDoc} */
    @Override
    public ActionResult doExecute(HttpServletRequest req, RenderContext renderContext, Resource resource, JCRSessionWrapper session, Map<String, List<String>> parameters, URLResolver urlResolver) throws Exception {
        logger.info("doExecute: begins the Html2PdfAction action.");

        try {
            /*checking the necessary parameters*/
            if (StringUtils.isEmpty(req.getParameter("documentName"))) {
                throw new Html2PdfException("The document name is missing.");
            }
            if (StringUtils.isEmpty(req.getParameter("currentPageNodePath"))) {
                throw new Html2PdfException("The page node path is missing.");
            }
            if (StringUtils.isEmpty(req.getParameter("documentParagraphNodePath"))) {
                throw new Html2PdfException("The document paragraph node path is missing.");
            }
            if (StringUtils.isEmpty(req.getParameter("currentModulePath"))) {
                throw new Html2PdfException("The current module node path is missing.");
            }

            Date settingsModDate = fillPdfSettings(renderContext);

            String currentPageNodePath = req.getParameter("currentPageNodePath").trim();
            String documentName = req.getParameter("documentName").trim();
            String currentModulePath = req.getParameter("currentModulePath").trim();
            documentParagraphNodePath = req.getParameter("documentParagraphNodePath").trim();
            HttpServletResponse response = renderContext.getResponse();
            ByteArrayOutputStream baos = null;

            logger.info("doExecute, Starting to download {}.pdf....", documentName);

            /*if the pdf file exists then get the file, otherwise create and store the pdf file*/
            InputStream pdfInputStream = getPdfInputStream(currentPageNodePath, session, documentName, settingsModDate);

            if (pdfInputStream != null) {
                baos = CommonUtil.readSourceContent(pdfInputStream);
            } else {
                ConfigurationMap.setModulePath(currentModulePath);
                this.documentation = new PdfDocumentation(session, req, renderContext.getResponse());
                baos = this.documentation.createDocumentPdf();
                setPdfInputStream(currentPageNodePath, session, new ByteArrayInputStream(baos.toByteArray()), new Date(), documentName);
            }

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + documentName + ".pdf\"");

            OutputStream os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
            os.close();

            return new ActionResult(HttpServletResponse.SC_OK);
        } catch (Exception ex) {
            logger.error("getGeneratePDF(), Error,", ex);
            return new ActionResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * <pre>setPdfInputStream</pre>
     * Store the pdf file into jcr node.
     *
     * @param pagePath @String
     * @param session @JCRSessionWrapper
     * @param is @InputStream
     * @param pdfDate @Date
     * @param fileName @String
     * @return @boolean
     */
    public boolean setPdfInputStream(String pagePath, JCRSessionWrapper session, InputStream is, Date pdfDate, String fileName) {
        try {
            logger.debug("setPdfInputStream: setting the PDF File {}", pagePath);
            JCRNodeWrapper pageNode = session.getNode(pagePath.trim());
            JCRNodeWrapper documentFolder = null;

            if (pageNode.hasNode("document")) {
                documentFolder = pageNode.getNode("document");
            } else {
                documentFolder = pageNode.addNode("document", "jnt:folder");
            }

            Calendar lastModified = Calendar.getInstance();
            lastModified.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
            documentFolder.uploadFile(fileName + ".pdf", is, "application/pdf").setProperty("jcr:lastModified", lastModified);

            session.save();
            return true;
        } catch (Exception e) {
            logger.error("setPdfInputStream: Error setting the PDF File {}, ", pagePath, e);
            return false;
        }
    }

    /**
     * <pre>getPdfInputStream</pre>
     * if pdf file exists, return the file
     * input stream, otherwise return null.
     *
     * @param pagePath @String
     * @param session @JCRSessionWrapper
     * @param filename @String
     * @param settingsModDate @Date
     * @return @InputStream
     */
    public InputStream getPdfInputStream(String pagePath, JCRSessionWrapper session, String filename, Date settingsModDate) {
        boolean newPdf = false;
        try {
            logger.debug("getPdfInputStream: Getting the PDF File {}", pagePath);
            JCRNodeWrapper pageNode = session.getNode(pagePath.trim());
            if (pageNode.hasNode("document")) {
                JCRNodeWrapper pdfFolder = pageNode.getNode("document");

                //Did we created the pdf already?
                if (pdfFolder.hasNode(filename + ".pdf")) {
                    JCRNodeWrapper pdfNode = pdfFolder.getNode(filename + ".pdf");
                    JCRNodeWrapper documentNode = session.getNode(documentParagraphNodePath.trim());

                    logger.debug("getPdfInputStream: PDF found =>[" + pdfNode.getPath() + "]");
                    //Check if this is the newest file
                    Date pdfDate = pdfNode.getContentLastModifiedAsDate();
                    //Get document last publish date
                    Date docDate = documentNode.getLastPublishedAsDate();

                    logger.debug("getPdfInputStream: PDF modification date: " + pdfDate);
                    logger.debug("getPdfInputStream: Document: " + docDate + pageNode.getDisplayableName());
                    //Is the Document (title, subtitle)  newer than stored pdf?
                    newPdf = docDate.after(pdfDate);
                    //There is a change in the document body (paragraphs)?
                    if (!newPdf) {
                        JCRNodeIteratorWrapper iteratorWrapper = documentNode.getNodes();
                        for (JCRNodeWrapper currentNode : iteratorWrapper) {
                            docDate = currentNode.getLastPublishedAsDate();
                            logger.debug("getPdfInputStream: Paragraph: '" + currentNode.getDisplayableName() + "'. Published: " + currentNode.getLastPublishedAsDate());
                            //If true, we can stop
                            if (newPdf = ((docDate.after(pdfDate)) || (settingsModDate != null && settingsModDate.after(pdfDate))))
                                break;
                        }
                    }
                    //After all, do we need to create again the PDF, or we can use the stored version?
                    if (newPdf) {
                        logger.info("getPdfInputStream: There is a new publised version of the document. The PDF will be created again.");
                        return null;
                    } else {
                        logger.info("getPdfInputStream:  There is no new publised version of the document. Using the PDF stored version .");
                        JCRFileContent fileContent = pdfNode.getFileContent();
                        return fileContent.downloadFile();
                    }
                }
            }
            throw new Exception("The pdfFile does't exists. ");
        } catch (Exception e) {
            logger.error("getPdfInputStream: Error getting the PDF File {}, ", pagePath, e.getMessage());
            return null;
        }
    }

    /**
     * <pre>fillPdfSettings</pre>
     * Fill the property map with the configuration page
     * and return the last modified date of the configuration
     * page.
     *
     * @param renderContext @RenderContext
     * @return @Date
     * @throws @RepositoryException
     */
    private Date fillPdfSettings(RenderContext renderContext) throws RepositoryException {
        try {
            logger.debug("fillPdfSettings:  starting to fill the configuration map.");
            JCRNodeWrapper pdfConfigNode = JCRTemplate.getInstance().getSessionFactory().getCurrentUserSession().getNode(renderContext.getSite().getPath() + "/pdfTemplateSettings");
            this.pdfClassDefinition = pdfConfigNode.hasProperty("pdfClassDefinition")  ? pdfConfigNode.getProperty("pdfClassDefinition").getString() : "";
            this.pdfStyleDefinition = pdfConfigNode.hasProperty("pdfStyleDefinition")  ? pdfConfigNode.getProperty("pdfStyleDefinition").getString() : "";
            this.pdfPropsDefinition = pdfConfigNode.hasProperty("pdfCommonProperties") ? pdfConfigNode.getProperty("pdfCommonProperties").getString() : "";

            Map<String, Map<String, String>> classMap = new HashMap<String, Map<String, String>>();
            Map<String, String> styleTagMap = new HashMap<String, String>();

            for (String lineClass : PdfTextUtil.splitStringByChar(this.pdfClassDefinition, ",")) {
                classMap.putAll(StyleTag2MapUtil.getClassMap(lineClass));
                logger.debug("fillPdfSettings: line class ==> " + lineClass);
            }

            for (String lineStyle : PdfTextUtil.splitStringByChar(this.pdfStyleDefinition, ",")) {
                styleTagMap.putAll(StyleTag2MapUtil.getStyleTagMap(lineStyle, classMap));
                logger.debug("fillPdfSettings: line style ==> " + lineStyle);
            }

            styleTagMap.putAll(StyleTag2MapUtil.getPdfPropsMap(this.pdfPropsDefinition));

            ConfigurationMap.setCustomPropertyMap(styleTagMap);
            logger.debug("fillPdfSettings: custom configuration map map <=> {}", ConfigurationMap.getCustomPropertyMap().toString());

            return pdfConfigNode.getLastModifiedAsDate();
        } catch (Exception e) {
            logger.error("fillPdfSettings: Could no get the configuration template node[" + renderContext.getSite().getPath() + "/pdfTemplateSettings" + "]");
        }

        return null;
    }

}