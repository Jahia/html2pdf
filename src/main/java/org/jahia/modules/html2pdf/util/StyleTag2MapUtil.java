package org.jahia.modules.html2pdf.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Juan Carlos Rodas.
 */
public class StyleTag2MapUtil {


    /** The logger. */
    private static Logger logger = LoggerFactory.getLogger(StyleTag2MapUtil.class);

    /* StyleTag2MapUtil constructor */
    private StyleTag2MapUtil(){}

    /**
     * <pre>getClassMap</pre>
     * Convert the configuration string
     * in configuration map.
     *
     * @param strClass @String
     * @return @Map
     */
    public static Map<String,Map<String, String>> getClassMap(String strClass){
        Map<String, Map<String, String>> classMap = new HashMap<String, Map<String, String>>();
        try{
            String[] strClassPart = strClass.split("=");
            String className = strClassPart[0].trim();
            classMap.put(className, new HashMap<String, String>());

            for (String definition : strClassPart[1].split(";")) {
                String[] keyValue = definition.trim().split(":");
                classMap.get(className).put(keyValue[0].replaceAll("-", "."), keyValue[1].trim());
            }
        }catch(Exception e){
            logger.error("getClassMap: String for class is not valid.");
        }

        return classMap;
    }

    /**
     * <pre>getStyleTagMap</pre>
     * Convert the configuration string
     * by tag in configuration map.
     *
     * @param strStyleTag @String
     * @param classMap @String
     * @return @Map
     */
    public static Map<String,String> getStyleTagMap(String strStyleTag, Map<String, Map<String, String>> classMap){
        Map<String, String> styleMap = new HashMap<String, String>();
        try{

            String[] strStyleTagPart = strStyleTag.split("=");
            String styleTagName = strStyleTagPart[0].trim().replaceAll(" ", "_");
            String strStyleTagClass = strStyleTagPart[1].trim();

            if(classMap.containsKey(strStyleTagClass)){
                for (String key : classMap.get(strStyleTagClass).keySet()) {
                    styleMap.put(styleTagName + "." + key, classMap.get(strStyleTagClass).get(key));
                }
            }
        }catch(Exception e){
            logger.error("getStyleTagMap: String for style tag is not valid.");
        }

        return styleMap;
    }

    /**
     * <pre>getPdfPropsMap</pre>
     * Convert the configuration string
     * in configuration map.
     *
     * @param strProps @String
     * @return @Map
     */
    public static Map<String,String> getPdfPropsMap(String strProps){
        Map<String, String> pdfPropsMap = new HashMap<String, String>();
        try{
            for (String lineProp : PdfTextUtil.splitStringByChar(strProps, ",")) {
                String[] strPropPart = lineProp.split("=");
                String propName = strPropPart[0].trim();
                String propValue = strPropPart[1].trim();
                pdfPropsMap.put(propName, propValue);
                logger.debug("getPdfPropsMap: line prop ==> " + lineProp);
            }
        }catch(Exception e){
            logger.error("getClassMap: String for class is not valid.");
        }

        return pdfPropsMap;
    }

}
