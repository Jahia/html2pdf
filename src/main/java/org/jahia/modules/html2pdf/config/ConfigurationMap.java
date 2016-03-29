package org.jahia.modules.html2pdf.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Juan Carlos Rodas.
 */
public class ConfigurationMap {

    /* the logger */
    private static Logger logger = LoggerFactory.getLogger(ConfigurationMap.class);

    /* the default configuration map */
    private static Map<String,String> defaultConfiguration;

    /* the custom configuration map */
    private static Map<String,String> customConfiguration = new HashMap<String, String>();

    /* the module path*/
    private static String modulePath;

    /* constructor */
    private ConfigurationMap(){}

    /**
     * <pre>loadDefaultConfiguracion</pre>
     * fill the default configuration map when is emty.
     */
    private static void loadDefaultConfiguracion(){
        if(defaultConfiguration == null){
            logger.debug("loadDefaultConfiguracion: fill the default configuration map.");
            defaultConfiguration = new HashMap<String, String>();

            /*## FILE FOR DEFAULT CONDIFUGURATION PDF ##*/

             /*## Default Configuration ##*/
            defaultConfiguration.put("principal.page.title.font.color","#000000");
            defaultConfiguration.put("principal.page.title.font.size","30");
           // defaultConfiguration.put("principal.page.title.font.size","18");
            defaultConfiguration.put("principal.page.title.font.family","lato");
            defaultConfiguration.put("principal.page.title.font.style","normal");
            defaultConfiguration.put("principal.page.title.font.weight","normal");
            defaultConfiguration.put("principal.page.title.font.space.factor","1.5");

            /*## Page Configuration ##*/
            defaultConfiguration.put("page.margin.x","30");
            defaultConfiguration.put("page.margin.y","100");
            defaultConfiguration.put("page.width","612");
            defaultConfiguration.put("page.height","792");
            defaultConfiguration.put("page.leading.factor","1.5");
            //defaultConfiguration.put("page.bg.color", "#ffff00");

            /*## Default Configuration ##*/
            defaultConfiguration.put("font.color","#000000");
            defaultConfiguration.put("font.size","10");
            defaultConfiguration.put("font.family","lato");
            defaultConfiguration.put("font.style","normal");
            defaultConfiguration.put("font.weight","normal");
            defaultConfiguration.put("font.space.factor","1.5");

            defaultConfiguration.put("picture.max.x","400");
            defaultConfiguration.put("picture.max.y","500");

           /*## Page header configuration ##*/
            defaultConfiguration.put("header.line.color","#80d0f1");
            defaultConfiguration.put("header.line.height","3");
            defaultConfiguration.put("header.line.position.y","715");
            defaultConfiguration.put("header.title.position.x","30");
            defaultConfiguration.put("header.title.position.y","750");
            //defaultConfiguration.put("header.title.text","JAHIA DOCUMENT");
            defaultConfiguration.put("header.title.font.size","18");
            defaultConfiguration.put("header.title.font.family","lato");
            defaultConfiguration.put("header.title.font.weight","normal");
            defaultConfiguration.put("header.title.font.color","#000000");
            //defaultConfiguration.put("header.image.src","https://www.jahia.com/files/live/sites/jahiacom/files/resources/blog/jahia-logo.jpg");
            defaultConfiguration.put("header.image.position.x","500");
            defaultConfiguration.put("header.image.position.y","750");
            defaultConfiguration.put("header.image.width","80");
            defaultConfiguration.put("header.image.height","35");

            /*## Page footer configuration ##*/
            defaultConfiguration.put("footer.line.color","#80d0f1");
            defaultConfiguration.put("footer.line.height","3");
            defaultConfiguration.put("footer.line.position.y","50");
            defaultConfiguration.put("footer.page.text","Page ");
            defaultConfiguration.put("footer.legend.text","© 2002 – 2016 Jahia Solutions Group SA");
            defaultConfiguration.put("footer.legend.font.size","8");
            defaultConfiguration.put("footer.legend.font.family","lato");
            defaultConfiguration.put("footer.legend.font.weight","normal");
            defaultConfiguration.put("footer.legend.font.color","#000000");

            /*## Tag P ##*/
            defaultConfiguration.put("p.font.color","#58585b");
            defaultConfiguration.put("p.font.size","10");
            defaultConfiguration.put("p.font.family","lato");
            defaultConfiguration.put("p.font.style","normal");

            /*## Tag EM ##*/
            defaultConfiguration.put("em.font.color","#000000");
            defaultConfiguration.put("em.font.size","10");
            defaultConfiguration.put("em.font.family","lato");
            defaultConfiguration.put("em.font.style","italic");

            /*## Tag H1 ##*/
            defaultConfiguration.put("h1.font.color","#00a0e3");
            defaultConfiguration.put("h1.font.size","17");
            defaultConfiguration.put("h1.font.family","lato_bold");
            defaultConfiguration.put("h1.font.style","normal");

            /*## Tag H2 ##*/
            defaultConfiguration.put("h2.font.color","#00a0e3");
            defaultConfiguration.put("h2.font.size","15");
            defaultConfiguration.put("h2.font.family","lato");
            defaultConfiguration.put("h2.font.style","normal");

            /*## Tag H3 ##*/
            defaultConfiguration.put("h3.font.color","#00a0e3");
            defaultConfiguration.put("h3.font.size","13");
            defaultConfiguration.put("h3.font.family","lato");
            defaultConfiguration.put("h3.font.style","normal");

             /*## Tag H4 ##*/
            defaultConfiguration.put("h4.font.color","#58585b");
            defaultConfiguration.put("h4.font.size","11");
            defaultConfiguration.put("h4.font.family","lato");
            defaultConfiguration.put("h4.font.style","normal");

              /*## Tag H5 ##*/
            defaultConfiguration.put("h5.font.color","#58585b");
            defaultConfiguration.put("h5.font.size","11");
            defaultConfiguration.put("h5.font.family","lato");
            defaultConfiguration.put("h5.font.style","normal");

            /*## Tag Subtitle ##*/
            defaultConfiguration.put("subtitle.font.color","#58585b");
            defaultConfiguration.put("subtitle.font.size","16");
            defaultConfiguration.put("subtitle.font.family","lato");
            defaultConfiguration.put("subtitle.font.style","normal");

            /*## Tag PRE ##*/
            //defaultConfiguration.put("pre.font.bg.color.one","#f2f2f2");
            //defaultConfiguration.put("pre.font.bg.color.two","#f2f2f2");

            defaultConfiguration.put("pre.font.color","#919192");
            defaultConfiguration.put("pre.font.family","courier");
            defaultConfiguration.put("pre.font.size","8");

            defaultConfiguration.put("pre.bg.color","#f2f2f2");
            defaultConfiguration.put("pre.line.top.style","solid");
            defaultConfiguration.put("pre.line.top.point","3");
            defaultConfiguration.put("pre.line.top.color","#7f7f7f");
            defaultConfiguration.put("pre.line.bottom.style","solid");
            defaultConfiguration.put("pre.line.bottom.point","3");
            defaultConfiguration.put("pre.line.bottom.color","#7f7f7f");

            /*## Tag TABLE ##*/
            defaultConfiguration.put("table.header.font.family","lato");
            defaultConfiguration.put("table.header.font.color","#919192");
            defaultConfiguration.put("table.header.font.style","normal");
            defaultConfiguration.put("table.header.font.weight","bold");
            defaultConfiguration.put("table.header.font.bg.color","#f2f2f2");
            defaultConfiguration.put("table.header.font.size","8");

            defaultConfiguration.put("table.cell.font.family","lato");
            defaultConfiguration.put("table.cell.font.color","#000000");
            defaultConfiguration.put("table.cell.font.style","normal");
            defaultConfiguration.put("table.cell.font.weight","normal");
            defaultConfiguration.put("table.cell.font.size","8");

            /*## Tag Strong ##*/
            defaultConfiguration.put("strong.font.color","#000000");
            defaultConfiguration.put("strong.font.family","lato");
            defaultConfiguration.put("strong.font.style","normal");
            defaultConfiguration.put("strong.font.weight","bold");
        }
    }

    /**
     * Gets the property.
     *
     * @param property the property
     * @return the property
     */
    public static String getProperty(String property){
        loadDefaultConfiguracion();

        if(customConfiguration.containsKey(property)) {
            logger.debug("getProperty: getting the property from settings configuration page.");
            return customConfiguration.get(property);
        }

        if(defaultConfiguration.containsKey(property)) {
            logger.debug("getProperty: getting the property from default configuration settings.");
            return defaultConfiguration.get(property);
        }

        logger.debug("getProperty: there is not property with name [{}].", property);
        return null;
    }

    /**
     * <pre>getModulePath</pre>
     * Gets the current module path
     *
     * @return @String
     */
    public static String getModulePath() {
        return ConfigurationMap.modulePath;
    }

    /**
     * <pre>setModulePath</pre>
     * Sets the current module path
     *
     * @param modulePath @String
     */
    public static void setModulePath(String modulePath) {
        ConfigurationMap.modulePath = modulePath;
    }

    /**
     * <pre>setFooterPageWord</pre>
     * Sets the footer legend page word.
     *
     * @param pageWord @String
     */
    public static void setFooterPageWord(String pageWord) {
        customConfiguration.put("footer.page.text", pageWord + " ");
    }

    /**
     * <pre>setFooterLegendWord</pre>
     * Sets the footer legend word
     *
     * @param legendWord @String
     */
    public static void setFooterLegendWord(String legendWord) {
        customConfiguration.put("footer.legend.text", legendWord);
    }

    /**
     * <pre>getCustomPropertyMap</pre>
     * Gets the custom property Map
     *
     * @return @Map
     */
    public static Map<String, String> getCustomPropertyMap() {
        return customConfiguration;
    }

    /**
     * <pre>setCustomProperty</pre>
     * Add custom property to map.
     *
     * @param customProperty @String
     * @param value @String
     */
    public static void setCustomProperty(String customProperty, String value) {
        customConfiguration.put(customProperty, value);
    }

    /**
     * <pre>setCustomPropertyMap</pre>
     * Sets a custom property map.
     *
     * @param propertyMap @Map
     */
    public static void setCustomPropertyMap(Map<String, String> propertyMap) {
        customConfiguration.clear();
        for (String key: propertyMap.keySet()) {
            setCustomProperty(key, propertyMap.get(key));
        }
    }

}
