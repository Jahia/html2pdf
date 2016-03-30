package org.jahia.modules.html2pdf.exception;

/**
 * Created by Juan Carlos Rodas.
 */
public class Html2PdfException extends Exception {

    /**
     * <pre>constructor</pre>
     */
    public Html2PdfException() {
        super();
    }

    /**
     * <pre>Html2PdfException</pre>
     *
     * @param message @String
     */
    public Html2PdfException(String message) {
        super(message);
    }

    /**
     * <pre>Html2PdfException</pre>
     *
     * @param message @String
     * @param cause @Throwable
     */
    public Html2PdfException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * <pre>Html2PdfException</pre>
     *
     * @param cause @Throwable
     */
    public Html2PdfException(Throwable cause) {
        super(cause);
    }

}