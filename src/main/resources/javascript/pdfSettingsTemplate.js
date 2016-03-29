function regularize(val){
    return val
        .replace(/[\"]/g, '\\"')
        .replace(/[\\]/g, '\\\\')
        .replace(/[\/]/g, '\\/')
        .replace(/[\b]/g, '\\b')
        .replace(/[\f]/g, '\\f')
        .replace(/[\n]/g, '\\n')
        .replace(/[\r]/g, '\\r')
        .replace(/[\t]/g, '\\t')
        ;
}

function replaceAll(str, find, replace) {
    return str.replace(new RegExp(find, 'g'), replace);
}

function setCookie(cname, cvalue, exmins) {
    var d = new Date();
    d.setTime(d.getTime() + (exmins*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
}

function pdfSettingsAddStyleClass() {
    var newStyleClass      = $('#newStyleClass');
    var newAttFontFamily   = $('#newAttFontFamily');
    var newAttFontSize     = $('#newAttFontSize');
    var newAttFontStyle    = $('#newAttFontStyle');
    var newAttFontWeight   = $('#newAttFontWeight');
    var newAttFontColor    = $('#newAttFontColor');

    var newBgColor         = $('#newBgColor');
    var newLineTopStyle    = $('#newLineTopStyle');
    var newLineTopPoint    = $('#newLineTopPoint');
    var newLineTopColor    = $('#newLineTopColor');
    var newLineBottomStyle = $('#newLineBottomStyle');
    var newLineBottomPoint = $('#newLineBottomPoint');
    var newLineBottomColor = $('#newLineBottomColor');


    /* validating the name class */
    var newStyleClassVal = newStyleClass.val();
    if (newStyleClassVal.length == 0) {
        return {
            attributeName: '',
            response: false
        };
    }

    if ($('#btnDeleteStyleClass' + newStyleClassVal).length == 0) {
        var valueStr = replaceAll(newStyleClassVal,' ', '_') + '=';
        if(newAttFontFamily != null && !(newAttFontFamily.val() === "") ) {valueStr += 'font-family:' + newAttFontFamily.val() + ';'; }
        if(newAttFontSize   != null && !(newAttFontSize.val()   === "") ) {valueStr += 'font-size:'   + newAttFontSize.val()   + ';'; }
        if(newAttFontStyle  != null && !(newAttFontStyle.val()  === "") ) {valueStr += 'font-style:'  + newAttFontStyle.val()  + ';'; }
        if(newAttFontWeight != null && !(newAttFontWeight.val() === "") ) {valueStr += 'font-weight:' + newAttFontWeight.val() + ';'; }
        if(newAttFontColor  != null && !(newAttFontColor.val()  === "") ) {valueStr += 'font-color:'  + newAttFontColor.val()  + ';'; }

        if(newBgColor != null && !(newBgColor.val() === "") ) {valueStr += 'bg-color:' + newBgColor.val() + ';'; }
        if(newLineTopStyle != null && !(newLineTopStyle.val() === "") ) {valueStr += 'line-top-style:' + newLineTopStyle.val() + ';'; }
        if(newLineTopPoint != null && !(newLineTopPoint.val() === "") ) {valueStr += 'line-top-point:' + newLineTopPoint.val() + ';'; }
        if(newLineTopColor != null && !(newLineTopColor.val() === "") ) {valueStr += 'line-top-color:' + newLineTopColor.val() + ';'; }
        if(newLineBottomStyle != null && !(newLineBottomStyle.val() === "") ) {valueStr += 'line-bottom-style:' + newLineBottomStyle.val() + ';'; }
        if(newLineBottomPoint != null && !(newLineBottomPoint.val() === "") ) {valueStr += 'line-bottom-point:' + newLineBottomPoint.val() + ';'; }
        if(newLineBottomColor != null && !(newLineBottomColor.val() === "") ) {valueStr += 'line-bottom-color:' + newLineBottomColor.val() + ';'; }


        $('#tblStyleClass').find('tbody:last').append('<tr>'
            + '<td><button style="margin-bottom:0px;" title="${i18nDelete}" onclick="$(this).parent().parent().remove(); return false;" class="btn btn-small btn-danger" type="button" id="remStyleClass"><i class="icon-remove icon-white"></i></button></td>'
            + '<td><button style="margin-bottom:0px;" title="${i18nEdit}"   onclick="" class="btn btn-small btn-danger" type="button" id="editStyleClass"><i class="icon-edit icon-white"></i></button></td>'
            + '</td><td width="100%"><strong class="styleClassToFilter">' + valueStr + '</strong></td>'
            + '</tr>');
    }

    newStyleClass.val('');
    newAttFontFamily.val('').change();
    newAttFontSize.val('');
    newAttFontStyle.val('').change();
    newAttFontWeight.val('').change();
    newAttFontColor.val('');

    newBgColor.val('');
    newLineTopStyle.val('').change();
    newLineTopPoint.val('');
    newLineTopColor.val('');
    newLineBottomStyle.val('').change();
    newLineBottomPoint.val('');
    newLineBottomColor.val('');

    return {
        attributeName: newStyleClassVal,
        response: true
    };
}

function pdfSettingsAddTagDefinition() {
    var newTagName          = $('#newTagName');
    var newTagCssClassName  = $('#newTagCssClassName');
    var newTagPdfClassDef   = $('#newTagPdfClassDef');

    /* validating the tag name  */
    var newTagNameVal = newTagName.val();
    var newTagPdfClassDefVal = newTagPdfClassDef.val();
    if (newTagNameVal.length == 0 || newTagPdfClassDefVal.length == 0) {
        return {
            attributeName: '',
            response: false
        };
    }

    if ($('#btnDeleteTagDef' + newTagNameVal).length == 0) {
        var valueStr = newTagNameVal;
        if(newTagCssClassName != null && !(newTagCssClassName.val() === "") ) {valueStr += '.' + replaceAll(newTagCssClassName.val(),' ', '_'); }
        valueStr += '=';
        if(newTagPdfClassDef   != null && !(newTagPdfClassDef.val()   === "") ) {valueStr += newTagPdfClassDef.val(); }

        $('#tblTagStyle').find('tbody:last').append('<tr>'
            + '<td><button style="margin-bottom:0px;" title="${i18nDelete}" onclick="" class="btn btn-small btn-danger" type="button" id="remTagDefinition"><i class="icon-remove icon-white"></i></button></td>'
            + '<td><button style="margin-bottom:0px;" title="${i18nEdit}"   onclick="" class="btn btn-small btn-danger" type="button" id="editTagDefinition"><i class="icon-edit icon-white"></i></button></td>'
            + '<td width="100%"><strong class="tagDefToFilter">' +  valueStr + '</strong></td>'
            + '</tr>');
    }

    newTagName.val('');
    newTagCssClassName.val('');
    newTagPdfClassDef.val('').change();
    return {
        attributeName: newTagNameVal,
        response: true
    };
}

function updateSitePdfStyleClass(nodeName, context, locale, identifier, tsIdentifier, create, strSaved, option) {

    if(option == '1'){
        var result = pdfSettingsAddStyleClass();
        strSaved = replaceAll(strSaved,':val',result.attributeName);
        if(!result.response)
            return false;
    }else if(option == '2'){
        var result = pdfSettingsAddTagDefinition();
        strSaved = replaceAll(strSaved,':val',result.attributeName);
        if(!result.response)
            return false;
    }

    var API_URL = context + '/modules/api/jcr/v1';
    var writeUrl = '';

    /* table styleClass */
    var styleClasses='';
    $('strong.styleClassToFilter').each(function() {
        if (styleClasses.length > 0) {
            styleClasses+=',';
        }
        styleClasses+=$(this).text();
    });

    /* table tagDefinition */
    var tagDefinition='';
    $('strong.tagDefToFilter').each(function() {
        if (tagDefinition.length > 0) {
            tagDefinition+=',';
        }
        tagDefinition+=$(this).text();
    });

    if (create == 'true') {
        jsonData = "{\"children\":{\"" + nodeName + "\":{\"name\":\"" + nodeName + "\",\"type\":\"html2pdf:siteSettingsPdfTemplate\",\"properties\":{\"pdfClassDefinition\":{\"value\":\"" + regularize(styleClasses) + "\"}, \"pdfStyleDefinition\":{\"value\":\"" + regularize(tagDefinition) + "\"}}}}}";
        writeUrl = API_URL + '/default/' + locale + '/nodes/' + identifier;
    }else {
        jsonData = "{\"properties\":{\"pdfClassDefinition\":{\"value\":\"" + regularize(styleClasses) + "\"},\"pdfStyleDefinition\":{\"value\":\"" + regularize(tagDefinition) + "\"}}}";
        writeUrl = API_URL + '/default/' + locale + '/nodes/' + tsIdentifier;
    }

    $.ajax({
        contentType: 'application/json',
        data: jsonData,
        dataType: 'json',
        processData: false,
        async: false,
        type: 'PUT',
        url: writeUrl,
        success: function (result) {
            if (result.warn != undefined) {
                alert(result.warn);
            } else {
                alert(strSaved);
                window.location.reload();
            }
        }
    });
}

function updateSitePdfProperties(nodeName, context, locale, identifier, tsIdentifier, create, strSaved) {

    var newPageBgColor  = $('#newPageBgColor');
    var newPageMarginX  = $('#newPageMarginX');
    var newPageMarginY  = $('#newPageMarginY');
    var newPageHeaderLineColor   = $('#newPageHeaderLineColor');
    var newPageHeaderLineHeight  = $('#newPageHeaderLineHeight');
    var newPageFooterLineColor   = $('#newPageFooterLineColor');
    var newPageFooterLineHeight  = $('#newPageFooterLineHeight');


    var propertyValues = '';


    if(newPageBgColor != null && !(newPageBgColor.val() === "") ) {
        propertyValues += 'page.bg.color=' + newPageBgColor.val();
    }
    if(newPageMarginX != null && !(newPageMarginX.val() === "") ) {
        if (propertyValues.length > 0) {  propertyValues+=',';  }
        propertyValues += 'page.margin.x=' + newPageMarginX.val();
    }
    if(newPageMarginY != null && !(newPageMarginY.val() === "") ) {
        if (propertyValues.length > 0) {  propertyValues+=',';  }
        propertyValues += 'page.margin.y=' + newPageMarginY.val();
    }
    if(newPageHeaderLineColor != null && !(newPageHeaderLineColor.val() === "") ) {
        if (propertyValues.length > 0) {  propertyValues+=',';  }
        propertyValues += 'header.line.color=' + newPageHeaderLineColor.val();
    }
    if(newPageHeaderLineHeight != null && !(newPageHeaderLineHeight.val() === "") ) {
        if (propertyValues.length > 0) {  propertyValues+=',';  }
        propertyValues += 'header.line.height=' + newPageHeaderLineHeight.val();
    }
    if(newPageFooterLineColor != null && !(newPageFooterLineColor.val() === "") ) {
        if (propertyValues.length > 0) {  propertyValues+=',';  }
        propertyValues += 'footer.line.color=' + newPageFooterLineColor.val();
    }
    if(newPageFooterLineHeight != null && !(newPageFooterLineHeight.val() === "") ) {
        if (propertyValues.length > 0) {  propertyValues+=',';  }
        propertyValues += 'footer.line.height=' + newPageFooterLineHeight.val();
    }

    /* saving or updatinf the values */

    var API_URL = context + '/modules/api/jcr/v1';
    var writeUrl = '';


    if (create == 'true') {
        jsonData = "{\"children\":{\"" + nodeName + "\":{\"name\":\"" + nodeName + "\",\"type\":\"html2pdf:siteSettingsPdfTemplate\",\"properties\":{\"pdfCommonProperties\":{\"value\":\"" + regularize(propertyValues) + "\"}}}}}";
        writeUrl = API_URL + '/default/' + locale + '/nodes/' + identifier;
    }else {
        jsonData = "{\"properties\":{\"pdfCommonProperties\":{\"value\":\"" + regularize(propertyValues) + "\"}}}";
        writeUrl = API_URL + '/default/' + locale + '/nodes/' + tsIdentifier;
    }

    $.ajax({
        contentType: 'application/json',
        data: jsonData,
        dataType: 'json',
        processData: false,
        async: false,
        type: 'PUT',
        url: writeUrl,
        success: function (result) {
            if (result.warn != undefined) {
                alert(result.warn);
            } else {
                alert(strSaved);
                window.location.reload();
            }
        }
    });
}

function editTagStyleDefinition(row){
    var data = $(row).parent().parent().text().trim();
    var elements = data.split('=');
    var namePart = elements[0].split('.');
    $('#newTagName').val(namePart[0].trim());
    if(namePart.length == 2){
        $('#newTagCssClassName').val(namePart[1].trim());
    }else{
        $('#newTagCssClassName').val('');
    }
    $('#newTagPdfClassDef').val(elements[1].trim()).change();

    $(row).parent().parent().remove();
}

function editClassDefinition(row){
    var data = $(row).parent().parent().text().trim();
    var nameValuePart = data.split('=');
    var valuePart = nameValuePart[1].split(';');

    $('#newStyleClass').val(nameValuePart[0].trim());
    $('#newAttFontFamily').val('').change();
    $('#newAttFontSize').val('');
    $('#newAttFontStyle').val('').change();
    $('#newAttFontWeight').val('').change();
    $('#newAttFontColor').val('');
    $('#newAttFontColor').minicolors('settings', {value: ''});

    $('#newBgColor').val('');
    $('#newBgColor').minicolors('settings', {value: ''});
    $('#newLineTopStyle').val('none').change();
    $('#newLineTopPoint').val('');
    $('#newLineTopColor').val('');
    $('#newLineTopColor').minicolors('settings', {value: ''});
    $('#newLineBottomStyle').val('none').change();
    $('#newLineBottomPoint').val('');
    $('#newLineBottomColor').val('');
    $('#newLineBottomColor').minicolors('settings', {value: ''});


    for(i = 0; i < valuePart.length; i++){
        var attNameValue=valuePart[i].split(':');

        if(attNameValue[0].indexOf('font-family') >= 0){
            $('#newAttFontFamily').val(attNameValue[1].trim()).change();
        }
        if(attNameValue[0].indexOf('font-size') >= 0){
            $('#newAttFontSize').val(attNameValue[1].trim());
        }
        if(attNameValue[0].indexOf('font-style') >= 0){
            $('#newAttFontStyle').val(attNameValue[1].trim()).change();
        }
        if(attNameValue[0].indexOf('font-weight') >= 0){
            $('#newAttFontWeight').val(attNameValue[1].trim()).change();
        }
        if(attNameValue[0].indexOf('font-color') >= 0){
            $('#newAttFontColor').val(attNameValue[1].trim());
            $('#newAttFontColor').minicolors('settings', {
                value: attNameValue[1].trim()
            });
        }

        /* begins the top and bottom lines configuration */
        if(attNameValue[0].indexOf('bg-color') >= 0){
            $('#newBgColor').val(attNameValue[1].trim());
            $('#newBgColor').minicolors('settings', {
                value: attNameValue[1].trim()
            });
        }
        if(attNameValue[0].indexOf('line-top-style') >= 0){
            $('#newLineTopStyle').val(attNameValue[1].trim()).change();
        }
        if(attNameValue[0].indexOf('line-top-point') >= 0){
            $('#newLineTopPoint').val(attNameValue[1].trim());
        }
        if(attNameValue[0].indexOf('line-top-color') >= 0){
            $('#newLineTopColor').val(attNameValue[1].trim());
            $('#newLineTopColor').minicolors('settings', {
                value: attNameValue[1].trim()
            });
        }

        if(attNameValue[0].indexOf('line-bottom-style') >= 0){
            $('#newLineBottomStyle').val(attNameValue[1].trim()).change();
        }
        if(attNameValue[0].indexOf('line-bottom-point') >= 0){
            $('#newLineBottomPoint').val(attNameValue[1].trim());
        }
        if(attNameValue[0].indexOf('line-bottom-color') >= 0){
            $('#newLineBottomColor').val(attNameValue[1].trim());
            $('#newLineBottomColor').minicolors('settings', {
                value: attNameValue[1].trim()
            });
        }
    }

    $(row).parent().parent().remove();
}