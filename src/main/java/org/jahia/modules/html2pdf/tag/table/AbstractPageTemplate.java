package org.jahia.modules.html2pdf.tag.table;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 * The Class AbstractPageTemplate.
 */
public abstract class AbstractPageTemplate extends PDPage {

    /**
     * Gets the document.
     *
     * @return the document
     */
    protected abstract PDDocument getDocument();

    /**
     * Y start.
     *
     * @return the float
     */
    protected abstract float yStart();

    /**
     * Adds the picture.
     *
     * @param ximage the ximage
     * @param cursorX the cursor x
     * @param cursorY the cursor y
     * @param width the width
     * @param height the height
     * @throws IOException Signals that an I/O exception has occurred.
     */
    protected void addPicture(PDImageXObject ximage, float cursorX, float cursorY, int width, int height) throws IOException {

        PDPageContentStream contentStream = new PDPageContentStream(getDocument(), this, true, false);
        contentStream.drawImage(ximage, cursorX, cursorY, width, height);
        contentStream.close();
    }

    /**
     * Load picture.
     *
     * @param nameJPGFile the name jpg file
     * @return the PD image
     * @throws IOException Signals that an I/O exception has occurred.
     */
    protected PDImage loadPicture(String nameJPGFile) throws IOException {
        return PDImageXObject.createFromFile(nameJPGFile, getDocument());
    }

}
