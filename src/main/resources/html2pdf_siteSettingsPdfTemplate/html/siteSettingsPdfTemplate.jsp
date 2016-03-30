<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="s" uri="http://www.jahia.org/tags/search" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>

<fmt:message key="label.deleted" var="i18nDelete"/>
<c:set var="i18nDeleted" value="${functions:escapeJavaScript(i18nDelete)}"/>

<fmt:message key="label.edit" var="i18nEdit"/>
<c:set var="i18nEdit" value="${functions:escapeJavaScript(i18nEdit)}"/>


<fmt:message key="label.saved" var="i18nSaved"/>
<c:set var="i18nSaved" value="${functions:escapeJavaScript(i18nSaved)}"/>

<fmt:message key="label.pageProperties" var="i18nPageProperties"/>
<c:set var="i18nPageProperties" value="${functions:escapeJavaScript(i18nPageProperties)}"/>

<fmt:message key="label.pageProperties" var="i18nPageProperties"/>
<c:set var="i18nPageProperties" value="${functions:escapeJavaScript(i18nPageProperties)}"/>

<fmt:message key="label.classDefinition" var="i18nClassDefinition"/>
<c:set var="i18nClassDefinition" value="${functions:escapeJavaScript(i18nClassDefinition)}"/>

<fmt:message key="label.styleDefinition" var="i18nStyleDefinition"/>
<c:set var="i18nStyleDefinition" value="${functions:escapeJavaScript(i18nStyleDefinition)}"/>


<template:addResources type="javascript" resources="jquery.min.js,jquery.form.min.js,bootstrap-select.min.js,jquery.minicolors.min.js,pdfSettingsTemplate.js"/>
<template:addResources type="css" resources="bootstrap-select.min.css,jquery.minicolors.css"/>

<c:set var="site" value="${renderContext.mainResource.node.resolveSite}"/>
<c:set var="nodeTitle" value="${currentNode.properties['jcr:title'].string}"/>
<c:set var="nodeName" value="pdfTemplateSettings"/>
<c:set var="propFilteredStyleClass" value=""/>
<c:set var="propFilteredTagClass"   value=""/>
<c:set var="propPdfPage"   value=""/>

<jcr:node var="pdfTemplateSettings" path="${renderContext.site.path}/${nodeName}"/>
<c:set var="nodeTemplateSettingsIdentifier" value=""/>
<c:set var="create" value="true"/>
<c:set var="option" value="${renderContext.request.parameterMap['option']}"/>
<c:choose>
    <c:when test="${not empty pdfTemplateSettings}">
        <c:set var="create" value="false"/>
        <c:set var="nodeTemplateSettingsIdentifier" value="${pdfTemplateSettings.identifier}"/>
        <c:set var="propFilteredStyleClass" value="${pdfTemplateSettings.properties['pdfClassDefinition']}"/>
        <c:set var="propFilteredTagClass"   value="${pdfTemplateSettings.properties['pdfStyleDefinition']}"/>
        <c:set var="propPdfPage"            value="${pdfTemplateSettings.properties['pdfCommonProperties']}"/>
    </c:when>
</c:choose>
<c:set var="filteredStyleClass" value="${not empty propFilteredStyleClass ? propFilteredStyleClass.string : ''}"/>
<c:set var="filteredTagClass"   value="${not empty propFilteredTagClass ? propFilteredTagClass.string : ''}"/>
<c:set var="filteredPdfProps"   value="${not empty propPdfPage ? propPdfPage.string : ''}"/>


<c:set var="styleTab1" value=""/>
<c:set var="styleTab2" value=""/>
<c:set var="styleTab3" value=""/>

<c:choose>
    <c:when test="${not empty cookie.optionCK.value}">
        <c:choose>
            <c:when test="${cookie.optionCK.value == 1}">
                <c:set var="styleTab1" value="active"/>
            </c:when>
            <c:when test="${cookie.optionCK.value == 2}">
                <c:set var="styleTab2" value="active"/>
            </c:when>
            <c:when test="${cookie.optionCK.value == 3}">
                <c:set var="styleTab3" value="active"/>
            </c:when>
        </c:choose>
    </c:when>
    <c:otherwise>
        <c:set var="styleTab1" value="active"/>
    </c:otherwise>
</c:choose>


<c:forTokens var="pdfPropItem" items="${filteredPdfProps}" delims=",">
    <c:set var="itmP" value="${fn:split(pdfPropItem, '=')[1]}"/>

    <c:choose>
        <c:when test="${fn:contains(pdfPropItem, 'page.bg.color')}">
            <c:set var="propPageBgColor" value="${itmP}"/>
        </c:when>
        <c:when test="${fn:contains(pdfPropItem, 'page.margin.x')}">
            <c:set var="propPageMarginX" value="${itmP}"/>
        </c:when>
        <c:when test="${fn:contains(pdfPropItem, 'page.margin.y')}">
            <c:set var="propPageMarginY" value="${itmP}"/>
        </c:when>
        <c:when test="${fn:contains(pdfPropItem, 'header.line.color')}">
            <c:set var="propPageHeaderLineColor" value="${itmP}"/>
        </c:when>
        <c:when test="${fn:contains(pdfPropItem, 'header.line.height')}">
            <c:set var="propPageHeaderLineHeight" value="${itmP}"/>
        </c:when>
        <c:when test="${fn:contains(pdfPropItem, 'footer.line.color')}">
            <c:set var="propPageFooterLineColor" value="${itmP}"/>
        </c:when>
        <c:when test="${fn:contains(pdfPropItem, 'footer.line.height')}">
            <c:set var="propPageFooterLineHeight" value="${itmP}"/>
        </c:when>
    </c:choose>
</c:forTokens>


<template:addResources>
    <script type="text/javascript">
        $(document).ready(function() {

            $('#newAttFontColor').minicolors({
                control: $(this).attr('data-control') || 'hue',
                defaultValue: $(this).attr('data-defaultValue') || '',
                format: $(this).attr('data-format') || 'hex',
                keywords: $(this).attr('data-keywords') || '',
                inline: $(this).attr('data-inline') === 'true',
                letterCase: $(this).attr('data-letterCase') || 'lowercase',
                opacity: $(this).attr('data-opacity'),
                position: $(this).attr('data-position') || 'bottom left',
                swatches: $(this).attr('data-swatches') ? $(this).attr('data-swatches').split('|') : [],
                change: function(hex, opacity) {
                    var log;
                    try {
                        log = hex ? hex : 'transparent';
                        if( opacity ) log += ', ' + opacity;
                        console.log(log);
                    } catch(e) {}
                },
                theme: 'default'
            });

            $('#newPageBgColor').minicolors({
                control: $(this).attr('data-control') || 'hue',
                defaultValue: $(this).attr('data-defaultValue') || '',
                format: $(this).attr('data-format') || 'hex',
                keywords: $(this).attr('data-keywords') || '',
                inline: $(this).attr('data-inline') === 'true',
                letterCase: $(this).attr('data-letterCase') || 'lowercase',
                opacity: $(this).attr('data-opacity'),
                position: $(this).attr('data-position') || 'bottom left',
                swatches: $(this).attr('data-swatches') ? $(this).attr('data-swatches').split('|') : [],
                change: function(hex, opacity) {
                    var log;
                    try {
                        log = hex ? hex : 'transparent';
                        if( opacity ) log += ', ' + opacity;
                        console.log(log);
                    } catch(e) {}
                },
                theme: 'default'
            });


            $('#newPageHeaderLineColor').minicolors({
                control: $(this).attr('data-control') || 'hue',
                defaultValue: $(this).attr('data-defaultValue') || '',
                format: $(this).attr('data-format') || 'hex',
                keywords: $(this).attr('data-keywords') || '',
                inline: $(this).attr('data-inline') === 'true',
                letterCase: $(this).attr('data-letterCase') || 'lowercase',
                opacity: $(this).attr('data-opacity'),
                position: $(this).attr('data-position') || 'bottom left',
                swatches: $(this).attr('data-swatches') ? $(this).attr('data-swatches').split('|') : [],
                change: function(hex, opacity) {
                    var log;
                    try {
                        log = hex ? hex : 'transparent';
                        if( opacity ) log += ', ' + opacity;
                        console.log(log);
                    } catch(e) {}
                },
                theme: 'default'
            });


            $('#newPageFooterLineColor').minicolors({
                control: $(this).attr('data-control') || 'hue',
                defaultValue: $(this).attr('data-defaultValue') || '',
                format: $(this).attr('data-format') || 'hex',
                keywords: $(this).attr('data-keywords') || '',
                inline: $(this).attr('data-inline') === 'true',
                letterCase: $(this).attr('data-letterCase') || 'lowercase',
                opacity: $(this).attr('data-opacity'),
                position: $(this).attr('data-position') || 'bottom left',
                swatches: $(this).attr('data-swatches') ? $(this).attr('data-swatches').split('|') : [],
                change: function(hex, opacity) {
                    var log;
                    try {
                        log = hex ? hex : 'transparent';
                        if( opacity ) log += ', ' + opacity;
                        console.log(log);
                    } catch(e) {}
                },
                theme: 'default'
            });

            $('#newBgColor').minicolors({
                control: $(this).attr('data-control') || 'hue',
                defaultValue: $(this).attr('data-defaultValue') || '',
                format: $(this).attr('data-format') || 'hex',
                keywords: $(this).attr('data-keywords') || '',
                inline: $(this).attr('data-inline') === 'true',
                letterCase: $(this).attr('data-letterCase') || 'lowercase',
                opacity: $(this).attr('data-opacity'),
                position: $(this).attr('data-position') || 'bottom left',
                swatches: $(this).attr('data-swatches') ? $(this).attr('data-swatches').split('|') : [],
                change: function(hex, opacity) {
                    var log;
                    try {
                        log = hex ? hex : 'transparent';
                        if( opacity ) log += ', ' + opacity;
                        console.log(log);
                    } catch(e) {}
                },
                theme: 'default'
            });

            $('#newLineTopColor').minicolors({
                control: $(this).attr('data-control') || 'hue',
                defaultValue: $(this).attr('data-defaultValue') || '',
                format: $(this).attr('data-format') || 'hex',
                keywords: $(this).attr('data-keywords') || '',
                inline: $(this).attr('data-inline') === 'true',
                letterCase: $(this).attr('data-letterCase') || 'lowercase',
                opacity: $(this).attr('data-opacity'),
                position: $(this).attr('data-position') || 'bottom left',
                swatches: $(this).attr('data-swatches') ? $(this).attr('data-swatches').split('|') : [],
                change: function(hex, opacity) {
                    var log;
                    try {
                        log = hex ? hex : 'transparent';
                        if( opacity ) log += ', ' + opacity;
                        console.log(log);
                    } catch(e) {}
                },
                theme: 'default'
            });

            $('#newLineBottomColor').minicolors({
                control: $(this).attr('data-control') || 'hue',
                defaultValue: $(this).attr('data-defaultValue') || '',
                format: $(this).attr('data-format') || 'hex',
                keywords: $(this).attr('data-keywords') || '',
                inline: $(this).attr('data-inline') === 'true',
                letterCase: $(this).attr('data-letterCase') || 'lowercase',
                opacity: $(this).attr('data-opacity'),
                position: $(this).attr('data-position') || 'bottom left',
                swatches: $(this).attr('data-swatches') ? $(this).attr('data-swatches').split('|') : [],
                change: function(hex, opacity) {
                    var log;
                    try {
                        log = hex ? hex : 'transparent';
                        if( opacity ) log += ', ' + opacity;
                        console.log(log);
                    } catch(e) {}
                },
                theme: 'default'
            });


        });
    </script>
</template:addResources>

<%--
${site.propertiesAsString}
--%>

<div class="clearfix">
    <h1 class="pull-left">
        <fmt:message key="html2pdf.pdfTemplateSettings.label.header"/> - ${fn:escapeXml(site.displayableName)}
    </h1>
    <div class="pull-right">
        <img src="<c:url value="${url.currentModule}/img/pdf.png"/>">
    </div>
</div>

<ul class="nav nav-tabs" id="pdfSettingsTab">
    <li class="${styleTab1}">
        <a data-target="#tab1" data-toggle="tab">
            <h1 style="text-decoration: underline"><fmt:message key="html2pdf.pdfTemplateSettings.label.class"/></h1>
        </a>
    </li>
    <li class="${styleTab2}">
        <a data-target="#tab2" data-toggle="tab">
            <h1 style="text-decoration: underline"><fmt:message key="html2pdf.pdfTemplateSettings.label.style"/></h1>
        </a>
    </li>
    <li class="${styleTab3}">
        <a data-target="#tab3" data-toggle="tab">
            <h1 style="text-decoration: underline"><fmt:message key="html2pdf.pdfTemplateSettings.label.page.properties"/></h1>
        </a>
    </li>
</ul>

<div class="tab-content">

    <%--  Tab Style Class --%>
    <div class="tab-pane ${styleTab1}" id="tab1">

        <p><fmt:message key="html2pdf.pdfTemplateSettings.label.class.description"/>:</p>
        <form id="updateStyleClassSiteForm" action="<c:url value='${url.base}${renderContext.mainResource.node.resolveSite.path}'/>" method="post">
        <table width="100%" >
            <tr>
                <!-- first column -->
                <td width="50%">

                    <table id="tblClassDef" width="100%"  class="table-hover" >

                        <tr>
                            <td colspan="2" width="60%">
                                &nbsp;
                            </td>
                        </tr>
                        <tr>
                            <td rowspan="1" >
                                <h3 style="margin-left: 5px; margin-top: 5px"><fmt:message key="html2pdf.pdfTemplateSettings.label.class.styleName"/>:</h3>
                            </td>
                            <td>
                                <input type="text" name="newStyleClass" id="newStyleClass" value="" size="10"/>
                            </td>
                        </tr>
                        <tr>
                            <td >
                                <h4  style="margin-left: 5px"><fmt:message key="html2pdf.pdfTemplateSettings.label.class.fontFamily"/>:</h4>
                            </td>
                            <td>
                                <div class="col-lg-10">
                                    <select id="newAttFontFamily"  class="selectpicker" data-live-search="true">
                                        <option value="">&nbsp;</option>
                                        <option value="lato">LATO</option>
                                        <option value="times_roman">TIMES ROMAN</option>
                                        <option value="helvetica">HELVETICA</option>
                                        <option value="courier">COURIER</option>
                                    </select>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <h4 style="margin-left: 5px"><fmt:message key="html2pdf.pdfTemplateSettings.label.class.fontSize"/>:</h4>
                            </td>
                            <td>
                                <input type="number" name="newAttFontSize" id="newAttFontSize" value="" size="10"/>
                            </td>
                        </tr>
                        <tr>
                            <td >
                                <h4 style="margin-left: 5px"><fmt:message key="html2pdf.pdfTemplateSettings.label.class.fontStyle"/>:</h4>
                            </td>
                            <td>
                                <div class="col-lg-10">
                                    <select id="newAttFontStyle"  class="selectpicker" data-live-search="true">
                                        <option value="">&nbsp;</option>
                                        <option value="normal">NORMAL</option>
                                        <option value="italic">ITALIC</option>
                                    </select>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td >
                                <h4 style="margin-left: 5px"><fmt:message key="html2pdf.pdfTemplateSettings.label.class.fontWeight"/>:</h4>
                            </td>
                            <td>
                                <div class="col-lg-10">
                                    <select id="newAttFontWeight"  class="selectpicker" data-live-search="true">
                                        <option value="">&nbsp;</option>
                                        <option value="normal">NORMAL</option>
                                        <option value="bold">BOLD</option>
                                    </select>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td >
                                <h4 style="margin-left: 5px"><fmt:message key="html2pdf.pdfTemplateSettings.label.class.fontColor"/>:</h4>
                            </td>
                            <td>
                                <input type="text" name="newAttFontColor" id="newAttFontColor" value="" size="10"/>
                            </td>
                        </tr>

                        <tr>
                            <td colspan="2" >
                                <button class="btn btn-primary"
                                        type="button"
                                        name="save"
                                        id="addStyleClass"
                                        onclick="setCookie('optionCK', '1', 1); updateSitePdfStyleClass('${nodeName}',  '${url.context}', '${renderContext.UILocale}', '${renderContext.site.identifier}', '${nodeTemplateSettingsIdentifier}', '${create}', '${i18nClassDefinition}&nbsp;:val&nbsp;${i18nSaved}', 1); return false;"
                                >
                                    <i class="icon-circle-arrow-up icon-white"></i> <fmt:message key='label.save.style.class'/>
                                </button>
                            </td>
                        </tr>
                    </table>

                </td>

                <!-- second column -->
                <td width="50%">

                    <table id="tblClassDef2" width="100%"  class="table-hover" >

                        <tr>
                            <td colspan="2" width="60%">
                                &nbsp;
                            </td>
                        </tr>

                        <tr>
                            <td >
                                <h4 style="margin-left: 5px"><fmt:message key="html2pdf.pdfTemplateSettings.label.class.bgColor"/>:</h4>
                            </td>
                            <td>
                                <input type="text" name="newBgColor" id="newBgColor" value="" size="10"/>
                            </td>
                        </tr>

                        <tr>
                            <td >
                                <h4  style="margin-left: 5px"><fmt:message key="html2pdf.pdfTemplateSettings.label.class.lineTop"/>:</h4>
                            </td>
                            <td>
                                <div class="col-lg-10">
                                    <select id="newLineTopStyle"  class="selectpicker" data-live-search="true">
                                        <option value="none">NONE</option>
                                        <option value="solid">SOLID</option>
                                        <option value="dashed">DASHED</option>
                                    </select>
                                </div>
                            </td>
                        </tr>

                        <tr>
                            <td >
                                <h4  style="margin-left: 5px">&nbsp;</h4>
                            </td>
                            <td>
                                <div class="col-lg-10">
                                    <input type="number" name="newLineTopPoint" id="newLineTopPoint" value="${newLineTopPoint}" size="10"/>
                                </div>
                            </td>
                        </tr>

                        <tr>
                            <td >
                                <h4 style="margin-left: 5px">&nbsp;</h4>
                            </td>
                            <td>
                                <input type="text" name="newLineTopColor" id="newLineTopColor" value="" size="10"/>
                            </td>
                        </tr>

                        <!-- second line bottom -->
                        <tr>
                            <td >
                                <h4  style="margin-left: 5px"><fmt:message key="html2pdf.pdfTemplateSettings.label.class.lineBottom"/>:</h4>
                            </td>
                            <td>
                                <div class="col-lg-10">
                                    <select id="newLineBottomStyle"  class="selectpicker" data-live-search="true">
                                        <option value="none">NONE</option>
                                        <option value="solid">SOLID</option>
                                        <option value="dashed">DASHED</option>
                                    </select>
                                </div>
                            </td>
                        </tr>

                        <tr>
                            <td >
                                <h4  style="margin-left: 5px">&nbsp;</h4>
                            </td>
                            <td>
                                <div class="col-lg-10">
                                    <input type="number" name="newLineBottomPoint" id="newLineBottomPoint" value="${newLineBottomPoint}" size="10"/>
                                </div>
                            </td>
                        </tr>

                        <tr>
                            <td >
                                <h4 style="margin-left: 5px">&nbsp;</h4>
                            </td>
                            <td>
                                <input type="text" name="newLineBottomColor" id="newLineBottomColor" value="" size="10"/>
                            </td>
                        </tr>

                    </table>

                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <table id="tblStyleClass" class="table table-bordered table-striped table-hover" >
                        <tbody>
                        <c:forTokens var="styleClass" items="${filteredStyleClass}" delims=", ">
                            <tr id="rowStyleClass${styleClass}">
                                <fmt:message key="label.delete.style.class" var="i18nDelete"/>
                                <c:set var="i18nDelete" value="${fn:escapeXml(i18nDelete)}"/>
                                <td>
                                    <button style="margin-bottom:0px;"
                                            title="${i18nDelete}"
                                            class="btn btn-small btn-danger"
                                            type="button"
                                            onclick="setCookie('optionCK', '1', 1); $(this).parent().parent().remove(); updateSitePdfStyleClass('${nodeName}',  '${url.context}', '${renderContext.UILocale}', '${renderContext.site.identifier}', '${nodeTemplateSettingsIdentifier}', '${create}', '${i18nClassDefinition}&nbsp;' + $(this).parent().parent().text().trim().split('=')[0] + '&nbsp;${i18nDeleted}', 3); return false;"
                                    >
                                        <i class="icon-remove icon-white"></i>
                                    </button>
                                </td>
                                <td>
                                    <button style="margin-bottom:0px;"
                                            title="${i18nEdit}"
                                            class="btn btn-small btn-info"
                                            type="button"
                                            onclick="editClassDefinition(this); return false;"
                                    >
                                        <i class="icon-edit icon-white"></i>
                                    </button>
                                </td>
                                <td width="100%"><strong class="styleClassToFilter">${styleClass}</strong></td>
                            </tr>
                        </c:forTokens>
                        </tbody>
                    </table>
                </td>
            </tr>

        </table>
        </form>
    </div>

    <%-- Tab Tab Style --%>
    <div class="tab-pane ${styleTab2}" id="tab2">
        <p><fmt:message key="html2pdf.pdfTemplateSettings.label.style.description"/>:</p>

        <form id="updateStyleTagSiteForm" action="<c:url value='${url.base}${renderContext.mainResource.node.resolveSite.path}'/>" method="post">
            <table id="tblStyleTagDef" width="50%"  class="table-hover" >

                <tr>
                    <td colspan="2" width="30%">
                        &nbsp;
                    </td>
                </tr>

                <tr>
                    <td>
                        <h3 style="margin-left: 5px; margin-top: 5px"><fmt:message key="html2pdf.pdfTemplateSettings.label.style.tagName"/>:</h3>
                    </td>
                    <td>
                        <input type="text" name="newTagName" id="newTagName" value="" size="10"/>
                    </td>
                </tr>

                <tr>
                    <td >
                        <h4  style="margin-left: 5px"><fmt:message key="html2pdf.pdfTemplateSettings.label.style.cssClassName"/>:</h4>
                    </td>
                    <td>
                        <input type="text" name="newTagCssClassName" id="newTagCssClassName" value="" size="10"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <h4 style="margin-left: 5px"><fmt:message key="html2pdf.pdfTemplateSettings.label.style.namePdfClassDefinition"/>:</h4>
                    </td>
                    <td>
                        <div class="col-lg-10">
                            <select id="newTagPdfClassDef"  class="selectpicker" data-live-search="true">
                                <option value="">&nbsp;</option>
                                <c:forTokens var="sClass" items="${filteredStyleClass}" delims=", ">
                                    <c:set var="itm" value="${fn:split(sClass, '=')[0]}"/>
                                    <option value="${fn:toLowerCase(itm)}">${fn:toUpperCase(itm)}</option>
                                </c:forTokens>
                            </select>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" >
                        <button class="btn btn-primary"
                                type="button"
                                name="save"
                                id="addTagDefinition"
                                onclick="setCookie('optionCK', '2', 1); updateSitePdfStyleClass('${nodeName}',  '${url.context}', '${renderContext.UILocale}', '${renderContext.site.identifier}', '${nodeTemplateSettingsIdentifier}', '${create}', '${i18nStyleDefinition}&nbsp;:val&nbsp;${i18nSaved}', 2); return false;"
                        >
                            <i class="icon-circle-arrow-up icon-white"></i> <fmt:message key='label.save.tag.style'/>
                        </button>
                    </td>
                </tr>
            </table>

            <br/>

            <table id="tblTagStyle" class="table table-bordered table-striped table-hover" >
                <tbody>
                <c:forTokens var="tagClass" items="${filteredTagClass}" delims=", ">
                    <tr id="rowStyleClass${tagClass}">
                        <fmt:message key="label.delete.tag.style" var="i18nDelete"/>
                        <c:set var="i18nDelete" value="${fn:escapeXml(i18nDelete)}"/>
                        <td>
                            <button style="margin-bottom:0px;"
                                    title="${i18nDelete}"
                                    class="btn btn-small btn-danger"
                                    type="button"
                                    onclick="setCookie('optionCK', '2', 1); $(this).parent().parent().remove(); updateSitePdfStyleClass('${nodeName}',  '${url.context}', '${renderContext.UILocale}', '${renderContext.site.identifier}', '${nodeTemplateSettingsIdentifier}', '${create}', '${i18nStyleDefinition}&nbsp;' + $(this).parent().parent().text().trim().split('=')[0] + '&nbsp;${i18nDeleted}', 3); return false;"
                                    >
                                <i class="icon-remove icon-white"></i>
                            </button>
                        </td>
                        <td>
                            <button style="margin-bottom:0px;"
                                    title="${i18nEdit}"
                                    class="btn btn-small btn-info"
                                    type="button"
                                    onclick="editTagStyleDefinition(this); return false;"
                            >
                                <i class="icon-edit icon-white"></i>
                            </button>
                        </td>
                        <td width="100%"><strong class="tagDefToFilter">${tagClass}</strong></td>
                    </tr>
                </c:forTokens>
                </tbody>
            </table>
        </form>

    </div>

    <%--  Tab Page Properties --%>
    <div class="tab-pane ${styleTab3}" id="tab3">

        <form id="updatePropertiesSiteForm" action="<c:url value='${url.base}${renderContext.mainResource.node.resolveSite.path}'/>" method="post">
            <table id="tblPropertiesDef" width="50%"  class="table-hover" >

                <tr>
                    <td colspan="2" width="30%">
                        &nbsp;
                    </td>
                </tr>

                <tr>
                    <td>
                        <h4 style="margin-left: 5px; margin-top: 5px"><fmt:message key="html2pdf.pdfTemplateSettings.label.page.properties.bg.color"/>:</h4>
                    </td>
                    <td>
                        <input type="text" name="newPageBgColor" id="newPageBgColor" value="${propPageBgColor}" size="10"/>
                    </td>
                </tr>

                <tr>
                    <td>
                        <h4 style="margin-left: 5px"><fmt:message key="html2pdf.pdfTemplateSettings.label.page.properties.margin.x"/>:</h4>
                    </td>
                    <td>
                        <input type="number" name="newPageMarginX" id="newPageMarginX" value="${propPageMarginX}" size="10"/>
                    </td>
                </tr>

                <tr>
                    <td>
                        <h4 style="margin-left: 5px"><fmt:message key="html2pdf.pdfTemplateSettings.label.page.properties.margin.y"/>:</h4>
                    </td>
                    <td>
                        <input type="number" name="newPageMarginY" id="newPageMarginY" value="${propPageMarginY}" size="10"/>
                    </td>
                </tr>

                <tr>
                    <td>
                        <h4 style="margin-left: 5px; margin-top: 5px"><fmt:message key="html2pdf.pdfTemplateSettings.label.page.properties.header.line.color"/>:</h4>
                    </td>
                    <td>
                        <input type="text" name="newPageHeaderLineColor" id="newPageHeaderLineColor" value="${propPageHeaderLineColor}" size="10"/>
                    </td>
                </tr>

                <tr>
                    <td>
                        <h4 style="margin-left: 5px"><fmt:message key="html2pdf.pdfTemplateSettings.label.page.properties.header.line.height"/>:</h4>
                    </td>
                    <td>
                        <input type="number" name="newPageHeaderLineHeight" id="newPageHeaderLineHeight" value="${propPageHeaderLineHeight}" size="10"/>
                    </td>
                </tr>

                <tr>
                    <td>
                        <h3 style="margin-left: 5px; margin-top: 5px"><fmt:message key="html2pdf.pdfTemplateSettings.label.page.properties.footer.line.color"/>:</h3>
                    </td>
                    <td>
                        <input type="text" name="newPageFooterLineColor" id="newPageFooterLineColor" value="${propPageFooterLineColor}" size="10"/>
                    </td>
                </tr>

                <tr>
                    <td>
                        <h4 style="margin-left: 5px"><fmt:message key="html2pdf.pdfTemplateSettings.label.page.properties.footer.line.height"/>:</h4>
                    </td>
                    <td>
                        <input type="number" name="newPageFooterLineHeight" id="newPageFooterLineHeight" value="${propPageFooterLineHeight}" size="10"/>
                    </td>
                </tr>

                <tr>
                    <td colspan="2" >
                        <button class="btn btn-primary"
                                type="button"
                                name="save"
                                id="addPageProperties"
                                onclick="setCookie('optionCK', '3', 1); updateSitePdfProperties('${nodeName}',  '${url.context}', '${renderContext.UILocale}', '${renderContext.site.identifier}', '${nodeTemplateSettingsIdentifier}', '${create}', '${i18nPageProperties}&nbsp;${i18nSaved}'); return false;"
                        >
                            <i class="icon-circle-arrow-up icon-white"></i> <fmt:message key='label.save'/>&nbsp;<fmt:message key='label.pageProperties'/>
                        </button>
                    </td>
                </tr>

            </table>
        </form>

    </div>

</div>