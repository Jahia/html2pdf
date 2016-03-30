package org.jahia.modules.html2pdf.to;

/**
 * PdfPictureSizeTO Class
 * Created by Juan Carlos Rodas
 */
public class PdfPictureSizeTO {

    private float width;
    private float height;

    /**
     * <code>PdfPictureSizeTO</code>
     * PdfPictureSizeTO Constructor
     *
     * @param iWidth @float
     * @param iHeight @float
     */
    public PdfPictureSizeTO(float iWidth, float iHeight) {
        this.width = iWidth;
        this.height = iHeight;
    }

    /**
     * <code>getWidth</code>
     *
     * @return @float
     */
    public float getWidth() {
        return width;
    }

    /**
     * <code>setWidth</code>
     *
     * @param width @float
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * <code>getHeight</code>
     *
     * @return @float
     */
    public float getHeight() {
        return height;
    }

    /**
     * <code>getHeight</codeZ>
     *
     * @param height @float
     */
    public void setHeight(float height) {
        this.height = height;
    }


}
