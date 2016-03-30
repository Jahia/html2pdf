<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="ui" uri="http://www.jahia.org/tags/uiComponentsLib" %>
<%@ taglib prefix="functions" uri="http://www.jahia.org/tags/functions" %>
<%@ taglib prefix="query" uri="http://www.jahia.org/tags/queryLib" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="s" uri="http://www.jahia.org/tags/search" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>

<template:addResources>
    <script type="text/javascript">
        $(document).ready(function() {

            $('#downloadPDFLocal').submit(function( event ) {
                var message = "<fmt:message key="html2pdf.downloading"/>";
                var newValue = "<fmt:message key="html2pdf.downloading"/>";
                var defaultValue ="<fmt:message key='html2pdf.text'/>";

                $("#downloadPDFButtonLocal").attr('data-original-title',newValue)
                        .tooltip('fixTitle')
                        .tooltip('show');
                $("#downloadPDFButtonLocal").popover('show');
                $("#downloadPDFButtonLocal").tooltip('destroy');
                    setTimeout(function(){
                        $("#downloadPDFButtonLocal").popover('hide');
                        $("#downloadPDFButtonLocal").tooltip('hide')
                            .attr('data-original-title', defaultValue)
                            .tooltip('fixTitle').tooltip();
                }, 10000);
            });
        });
    </script>
</template:addResources>

<c:url var="urlCurrentModule" value="${url.server}${url.currentModule}/"/>
<c:set var="nodeDocumentTypeSearch" value="jant:document"/>
<c:set var="currentPageNode" value="${renderContext.mainResource.node}"/>
<c:set var="currentContentListNode" value="${jcr:getNodes(currentPageNode,'jnt:contentList')}"/>
<c:set var="nodeParagraphTypeSearch" value="document-area"/>
<c:set var="documentName" value="${currentPageNode.propertiesAsString['j:nodename']}"/>
<c:forEach items="${currentContentListNode}" var="nodeItem">
    <c:if test="${nodeItem.name eq nodeParagraphTypeSearch}">
        <c:set var="documentNode" value="${nodeItem}" />
    </c:if>
</c:forEach>

<c:if test="${not empty documentNode}">
    <c:set var="nodeTypeDocument" value="${jcr:getNodes(documentNode, nodeDocumentTypeSearch)}"/>
    <c:if test="${not empty nodeTypeDocument and fn:length(nodeTypeDocument) > 0}" >
        <%--<h3>Printing org.jahia.modules.academyTestComponents.pdf component....</h3>--%>
        <form id="downloadPDFLocal" action="<c:url value="${url.base}${currentPageNode.path}.html2Pdf.do"/>" method="post">
            <input type="hidden" name="documentParagraphNodePath" value="${nodeTypeDocument[0].path}">
            <input type="hidden" name="documentName" value="${documentName}" >
            <input type="hidden" name="currentPageNodePath" value="${currentPageNode.path}" />
            <input type="hidden" name="currentModulePath" value="${urlCurrentModule}" />
            <input type="hidden" name="currentNodePath" value="${currentNode.path}" />

            <button id="downloadPDFButtonLocal" type="submit" data-toggle="tooltip" data-placement="top"  title="<fmt:message key="html2pdf.text"/>"
                    class="bounce-button" >
                <img alt="<fmt:message key='html2pdf.text'/>"
                     class="bounce-img"
                     src="<c:url value='${url.currentModule}/img/action_download.png'/>">
                <fmt:message key="html2pdf.export.pdf"/>
            </button>
        </form>

    </c:if>
</c:if>