package org.jahia.modules.html2pdf.util;

import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.jahia.api.Constants;
import org.jahia.modules.html2pdf.config.ConfigurationMap;
import org.jahia.modules.html2pdf.to.PdfPictureSizeTO;
import org.jahia.services.content.JCRNodeWrapper;
import org.jahia.services.content.JCRSessionWrapper;
import org.jahia.services.content.decorator.JCRFileContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.imageio.ImageIO;
import javax.jcr.Node;
import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * PdfImagesUtil Class
 * Created by Juan Carlos Rodas.
 */
public class PdfImageUtil {

    /** The logger. */
    private static Logger logger = LoggerFactory.getLogger(PdfImageUtil.class);
    
    /** The document. */
    private PDDocument document;

    /**
     * <code>PdfImagesUtil</code>
     * PdfImagesUtil Constructor.
     *
     * @param iDocument the i document
     */
    public PdfImageUtil(PDDocument iDocument) {
        this.document = iDocument;
    }

	/**
	 * Gets the image max size x.
	 *
	 * @return the image max size x
	 */
	public static Float getImageMaxSizeX(){
	    String size = ConfigurationMap.getProperty("picture.max.x") != null ?
                ConfigurationMap.getProperty("picture.max.x") :
			          "400";
        return new Float(size);
	} 
	
	/**
	 * Gets the image max size y.
	 *
	 * @return the image max size y
	 */
	public static Float getImageMaxSizeY(){
	    String size = ConfigurationMap.getProperty("picture.max.y") != null ?
                ConfigurationMap.getProperty("picture.max.y") :
			          "500";
        return new Float(size);
	} 

    /**
     * <code>pictureResizeValue</code>
     * resize the picture dimensions
     * if it's necessary, (coeficient).
     *
     * @param size @int
     * @param max @int
     * @return @float
     */
    public float pictureResizeValue(Float size, Float max) {
        float skale = 1f;
        while ((size * skale) >= max)
            skale = skale - 0.1f;
        return skale;
    }

    /**
     * <code>resizePicture</code>
     * resize the picture dimensions
     * if it's necessary.
     *
     * @param pdImage @PDImageXObject
     * @return @PdfPictureSizeTO
     */
    public PdfPictureSizeTO resizePicture(PDImageXObject pdImage) {
        float xSize = pdImage.getWidth();
        float ySize = pdImage.getHeight();
        float skale = 1;

        /* resize for skale x */
        skale = pictureResizeValue(xSize, getImageMaxSizeX());
        xSize = (float) (xSize * skale);
        ySize = (float) (ySize * skale);

        /* resize for skale y */
        skale = pictureResizeValue(ySize, getImageMaxSizeY());
        xSize = (int) (xSize * skale);
        ySize = (int) (ySize * skale);

        return new PdfPictureSizeTO(xSize, ySize);
    }

    /**
     * <code>getImageInputStreamFromSrc</code>
     * return the image from node src path.
     *
     * @param imageSrc the image src
     * @return @InputStream
     */
    public PDImageXObject getPDImageFromSrc(String imageSrc) {
    	 if (StringUtils.isNotEmpty(imageSrc)) {
             logger.debug("getPDImageFromSrc: getting the image [{}]", imageSrc);
             try {
                 URL url = new URL(imageSrc.trim());
                 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                 BufferedImage bimg = ImageIO.read(conn.getInputStream());
                 return LosslessFactory.createFromImage(document, bimg);
             } catch (Exception e) {
                 logger.error("getPDImageFromSrc: Error getting the image {}", imageSrc, e.getLocalizedMessage());
             }
         } else {
             logger.error("getPDImageFromSrc: Error, modulePath null, can't get the image {}", imageSrc);
         }
         return null;
    }

    /**
     * <code>getParagraphImageFromSrc</code>
     * return the image from node src path.
     *
     * @param path @String
     * @param session @JCRSessionWrapper
     * @return @PDImageXObject
     */
    public PDImageXObject getParagraphImageFromSrc(String path, JCRSessionWrapper session) {
        try {
            logger.debug("getParagraphImageFromSrc: Getting the image {}", path);
            JCRNodeWrapper imageNode = session.getNode(path);
            JCRFileContent fileContent = imageNode.getFileContent();
            BufferedImage bi = ImageIO.read(fileContent.downloadFile());
            return LosslessFactory.createFromImage(document, bi);
        } catch (Exception e) {
            logger.error("getImageInputStream: Error getting the image {}, ", path, e);
            return null;
        }
    }

    /**
     * <code>getPDImageFromModule</code>
     * return the image from module path.
     *
     * @param document @PDDocument
     * @param imageName @String
     * @return @PDImageXObject
     */
    public PDImageXObject getPDImageFromModule(PDDocument document,String imageName) {
        String imagesModulePath = "";
        String modulePath = ConfigurationMap.getModulePath();
        if (StringUtils.isNotEmpty(modulePath)) {
            imagesModulePath = modulePath + "img/";

            logger.debug("getPDImageFromModule: getting the image [{}]", imagesModulePath + imageName);
            try {
                URL url = new URL((imagesModulePath + imageName).trim());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedImage bimg = ImageIO.read(conn.getInputStream());
                return LosslessFactory.createFromImage(document, bimg);
            } catch (Exception e) {
                logger.error("getPDImageFromModule: Error getting the image {}", imagesModulePath + imageName, e.getMessage());
            }
        } else {
            logger.error("getPDImageFromModule: Error, modulePath null, can't get the image {}", imagesModulePath + imageName);
        }
        return null;
    }

}
