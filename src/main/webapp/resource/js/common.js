function clone(obj) {
    if (null == obj || "object" != typeof obj)
        return obj;
    var copy = obj.constructor();
    for ( var attr in obj) {
        if (obj.hasOwnProperty(attr))
            copy[attr] = obj[attr];
    }
    return copy;
}
function getPostJson(obj) {
    var postJson = {
        type : "POST",
        contentType : "application/json",
        dataType : 'json',
        error : function(xhr, textStatus, errorThrown) {
            if (xhr.responseText.indexOf('{49cdd9d3-a473-4aef-8190-5dc5bf7b3984}') != -1) {
                top.location.href = "/login";
                return;
            }
            var dialog = new MessageDialog("error", xhr.responseJSON.message);
            dialog.show();
            ShowDataTable(null);
        }
    };
    var copy = clone(postJson);

    for ( var k in obj)
        copy[k] = obj[k];
    return copy;
}

function multiSelectOption(obj) {
    var option = {
        maxHeight : 200,
        includeSelectAllOption : true,
        enableFiltering : true,
        enableClickableOptGroups : true,
        numberDisplayed : 3,
        selectAllText : '全选',
        buttonWidth : '100%',
        disableIfEmpty : true,
        enableCaseInsensitiveFiltering : true
    };
    var copy = clone(option);

    for ( var k in obj)
        copy[k] = obj[k];
    return copy;
}
function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)), sURLVariables = sPageURL.split('&'), sParameterName, i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
};