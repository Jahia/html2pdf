package org.jahia.modules.html2pdf.to;

import org.apache.commons.lang.StringUtils;
import org.jahia.modules.html2pdf.config.ConfigurationMap;
import org.jahia.modules.html2pdf.util.LineStyle;
import org.jahia.modules.html2pdf.util.PdfColorUtil;
import org.jsoup.nodes.Node;

import java.awt.*;

/**
 * Created by Juan Carlos Rodas on 21/03/2016.
 */
public class HtmlTagBackgroundTO {

    private Color backgroundColor;

    private LineStyle lineTopStyle;

    private Float lineTopPoint;

    private Color lineTopColor;

    private LineStyle lineBottomStyle;

    private Float lineBottomPoint;

    private Color lineBottomColor;

    private HtmlTagBackgroundTO(){}

    public HtmlTagBackgroundTO(String tag, String styleClass, String styleAttributes){
        setBackgroundColor(getBackgroundTagColor(tag, styleClass, styleAttributes));
        setLineTopStyle(getTagLineStyle(tag, styleClass, styleAttributes, Boolean.TRUE));
        setLineTopPoint(getTagLinePoint(tag, styleClass, styleAttributes, Boolean.TRUE));
        setLineTopColor(getTagLineColor(tag, styleClass, styleAttributes, Boolean.TRUE));
        setLineBottomStyle(getTagLineStyle(tag, styleClass, styleAttributes, Boolean.FALSE));
        setLineBottomPoint(getTagLinePoint(tag, styleClass, styleAttributes, Boolean.FALSE));
        setLineBottomColor(getTagLineColor(tag, styleClass, styleAttributes, Boolean.FALSE));
    }

    /**
     * <pre>getBackgroundTagColor</pre>
     *
     * @param tag
     * @param styleClass
     * @param styleAttribute
     * @return
     */
    public static Color getBackgroundTagColor(String tag, String styleClass, String styleAttribute){
        styleClass     = styleClass     != null ? (tag + "." + styleClass.trim().replaceAll(" ", "_"))     : "";
        styleAttribute = styleAttribute != null ? (tag + "." + styleAttribute.trim().replaceAll(" ", "_")) : "";

        String colorStr = ConfigurationMap.getProperty(styleClass + ".bg.color") != null ?
                ConfigurationMap.getProperty(styleClass + ".bg.color") :
                (      ConfigurationMap.getProperty(styleAttribute + ".bg.color") != null ?
                        ConfigurationMap.getProperty(styleAttribute + ".bg.color") :
                        (   ConfigurationMap.getProperty(tag + ".bg.color") != null ?
                                ConfigurationMap.getProperty(tag + ".bg.color") :
                                null
                        )
                );

        return (colorStr != null ? PdfColorUtil.getColorByHex(colorStr) : null);
    }

    /**
     * <pre>getTagLineColor</pre>
     *
     * @param tag
     * @param styleClass
     * @param styleAttribute
     * @param isTop
     * @return
     */
    public static Color getTagLineColor(String tag, String styleClass, String styleAttribute, Boolean isTop){

        styleClass     = styleClass     != null ? (tag + "." + styleClass.trim().replaceAll(" ", "_"))     : "";
        styleAttribute = styleAttribute != null ? (tag + "." + styleAttribute.trim().replaceAll(" ", "_")) : "";
        String searchKey = "line." + (isTop ? "top" : "bottom") + ".color";


        String colorStr = ConfigurationMap.getProperty(styleClass + "." + searchKey) != null ?
                ConfigurationMap.getProperty(styleClass + "." + searchKey) :
                (      ConfigurationMap.getProperty(styleAttribute + "." + searchKey) != null ?
                        ConfigurationMap.getProperty(styleAttribute + "." + searchKey) :
                        (   ConfigurationMap.getProperty(tag + "." + searchKey) != null ?
                                ConfigurationMap.getProperty(tag + "." + searchKey) :
                                null
                        )
                );

        return (colorStr != null ? PdfColorUtil.getColorByHex(colorStr) : null);
    }

    /**
     * <pre>getTagLineStyle</pre>
     *
     * @param tag
     * @param styleClass
     * @param styleAttribute
     * @param isTop
     * @return
     */
    public static LineStyle getTagLineStyle(String tag, String styleClass, String styleAttribute, Boolean isTop){

        styleClass     = styleClass     != null ? (tag + "." + styleClass.trim().replaceAll(" ", "_"))     : "";
        styleAttribute = styleAttribute != null ? (tag + "." + styleAttribute.trim().replaceAll(" ", "_")) : "";
        String searchKey = "line." + (isTop ? "top" : "bottom") + ".style";

        String styleStr = ConfigurationMap.getProperty(styleClass + "." + searchKey) != null ?
                ConfigurationMap.getProperty(styleClass + "." + searchKey) :
                (      ConfigurationMap.getProperty(styleAttribute + "." + searchKey) != null ?
                       ConfigurationMap.getProperty(styleAttribute + "." + searchKey) :
                        (   ConfigurationMap.getProperty(tag + "." + searchKey) != null ?
                            ConfigurationMap.getProperty(tag + "." + searchKey) :
                            ""
                        )
                );

        switch (styleStr){
            case "dashed":
                return LineStyle.DASHED;
            case "solid":
                return LineStyle.SOLID;
            default:
                return LineStyle.NONE;
        }
    }

    /**
     * <pre>getTagLinePoint</pre>
     *
     * @param tag
     * @param styleClass
     * @param styleAttribute
     * @param isTop
     * @return
     */
    public static Float getTagLinePoint(String tag, String styleClass, String styleAttribute, Boolean isTop){

        styleClass     = styleClass     != null ? (tag + "." + styleClass.trim().replaceAll(" ", "_"))     : "";
        styleAttribute = styleAttribute != null ? (tag + "." + styleAttribute.trim().replaceAll(" ", "_")) : "";
        String searchKey = "line." + (isTop ? "top" : "bottom") + ".point";

        String pointStr = ConfigurationMap.getProperty(styleClass + "." + searchKey) != null ?
                ConfigurationMap.getProperty(styleClass + "." + searchKey) :
                (      ConfigurationMap.getProperty(styleAttribute + "." + searchKey) != null ?
                       ConfigurationMap.getProperty(styleAttribute + "." + searchKey) :
                        (   ConfigurationMap.getProperty(tag + "." + searchKey) != null ?
                            ConfigurationMap.getProperty(tag + "." + searchKey) :
                            null
                        )
                );

        return pointStr != null ? Float.valueOf(pointStr) : 0;
    }

    /**
     * <pre>containsBackground</pre>
     *
     * @param node
     * @return
     */
    public static Boolean hasBackground(Node node){
        String tag = node.nodeName();
        String styleClass     = (StringUtils.isNotEmpty(node.attr("class")) ? node.attr("class") : null );
        String styleAttribute = (StringUtils.isNotEmpty(node.attr("style")) ? node.attr("style") : null );
        styleClass     = styleClass     != null ? (tag + "." + styleClass.trim().replaceAll(" ", "_"))     : "";
        styleAttribute = styleAttribute != null ? (tag + "." + styleAttribute.trim().replaceAll(" ", "_")) : "";

        String colorStr = ConfigurationMap.getProperty(styleClass + ".bg.color") != null ?
                ConfigurationMap.getProperty(styleClass + ".bg.color") :
                (      ConfigurationMap.getProperty(styleAttribute + ".bg.color") != null ?
                        ConfigurationMap.getProperty(styleAttribute + ".bg.color") :
                        (   ConfigurationMap.getProperty(tag + ".bg.color") != null ?
                                ConfigurationMap.getProperty(tag + ".bg.color") :
                                null
                        )
                );

        return (colorStr != null ? Boolean.TRUE : Boolean.FALSE);
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public LineStyle getLineTopStyle() {
        return lineTopStyle;
    }

    public void setLineTopStyle(LineStyle lineTopStyle) {
        this.lineTopStyle = lineTopStyle;
    }

    public Color getLineTopColor() {
        return lineTopColor;
    }

    public void setLineTopColor(Color lineTopColor) {
        this.lineTopColor = lineTopColor;
    }

    public Float getLineTopPoint() {
        return lineTopPoint;
    }

    public void setLineTopPoint(Float lineTopPoint) {
        this.lineTopPoint = lineTopPoint;
    }

    public LineStyle getLineBottomStyle() {
        return lineBottomStyle;
    }

    public void setLineBottomStyle(LineStyle lineBottomStyle) {
        this.lineBottomStyle = lineBottomStyle;
    }

    public Float getLineBottomPoint() {
        return lineBottomPoint;
    }

    public void setLineBottomPoint(Float lineBottomPoint) {
        this.lineBottomPoint = lineBottomPoint;
    }

    public Color getLineBottomColor() {
        return lineBottomColor;
    }

    public void setLineBottomColor(Color lineBottomColor) {
        this.lineBottomColor = lineBottomColor;
    }

}
