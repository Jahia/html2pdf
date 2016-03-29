package org.jahia.modules.html2pdf.util;

import org.jahia.modules.html2pdf.config.ConfigurationMap;
import org.jahia.services.content.JCRNodeWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/* CommonUtil Class */
public class CommonUtil {

	/* the logger */
	private static Logger logger = LoggerFactory.getLogger(CommonUtil.class);

	/* constructor */
	private CommonUtil(){}
	
	/**
	 * Gets the resource.
	 *
	 * @param resource the resource
	 * @return the resource
	 * @throws Exception the exception
	 */
	public static InputStream getFontResource(String resource) throws Exception {
		   ClassLoader cl = Thread.currentThread().getContextClassLoader();
		   return getInputStreamFromUrl(ConfigurationMap.getModulePath() + "css/fonts/" + resource + ".ttf");
	}

	/**
	 * <code>getInputStreamFromUrl</code>
	 * Get the InputStream from url File.
	 *
	 * @param urlPath @String
	 * @return @InputStream
	 * @throws java.io.IOException
	 */
	public static InputStream getInputStreamFromUrl(String urlPath) throws IOException {
		URL url = new URL(urlPath);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		return conn.getInputStream();
	}

	/**
	 * <pre>readSourceContent</pre>
	 * Get the ByteArrayOutputStream from InputStream
	 * @param inputStream @InputStream
	 * @return @ByteArrayOutputStream
	 * @throws @IOException
     */
	public static ByteArrayOutputStream readSourceContent(InputStream inputStream){
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int nextChar;
		try {
			while ((nextChar = inputStream.read()) != -1) {
				outputStream.write(nextChar);
			}
			outputStream.flush();
		} catch (IOException e) {
			logger.error("Exception occurred while reading content, {}", e.getLocalizedMessage());
		}
		return outputStream;
	}

	/**
	 * <pre>getStringValueFromPropertyNode</pre>
	 * get the requested property of the node
	 * if exists, otherwise return a empty string.
	 *
	 * @param node
	 * @param property
	 * @return
	 * @throws RepositoryException
	 */
	public static String getStringValueFromPropertyNode(JCRNodeWrapper node, String property) throws RepositoryException{
		return node.hasProperty(property) ? node.getPropertyAsString(property) : "";
	}


}
