function callAjax(url, iTarget, iCallBack, iCallBackParam, iPost, iParams, iLoading) {
    var aPost = iPost ? 'POST': 'GET';
    var aParams = iParams ? iParams: '';
    var aTarget = iTarget ? '#' + iTarget: iTarget;
    $(iLoading).css('display', 'block');
    $.ajax({
        type: aPost,
        url: url,
        data: aParams,
        contentType: "application/x-www-form-urlencoded; charset=utf-8",
        success: function(data, textStatus, jqXHR) {
            if (aTarget) {
                $(aTarget).html(data);
            }
            if (iCallBack) {
                if (iCallBackParam) {
                    eval(iCallBack)(iCallBackParam, data);
                } else {
                    eval(iCallBack)(data);
                }
            }
        },
        error: function(xhr, textStatus) {
            alert(xhr.responseText);
        },
        complete: function(data) {
            $(iLoading).css('display', 'none');
        }
    });
}

function textOnchange() {

    //01,04,1200164320,02388138,224.27,20170430,72421672501935626208,3C4C,
    //01,04,3100162130,44557616,132.67,20170525,72421672501935626208,3C4C,
    var data = $('#invoiceCode').val().split(',');

    $('#invoiceCode').val(data[2]);
    $('#invoiceNumber').val(data[3]);
    $('#invoiceDate').val(data[5]);
    $('#invoiceAmount').val(data[4]);

    callAjax('/invoiceService/getInvoiceByCode', '', 'getInvoiceByCodeCallback', '', '', 'code=' + $('#invoiceCode').val(), '');

}

function getInvoiceByCodeCallback(data) {
    if (data.status == 'ok') {
        var item = data.callBackData;
        $('#progress').css('display', 'block');
        $('#progress').addClass('progress').one('webkitAnimationEnd',
        function() {
            $(this).removeClass('progress');
            $('#progress').css('display', 'none');
            $('#verifyDiv').css('display', 'block');
            $('#verifyDiv').addClass('verifyInvoiceMove').one('webkitAnimationEnd',
            function() {
                $(this).removeClass('verifyInvoiceMove');
                $('#verifyDiv').css('display', 'none');
                $('#verifiedInvoiceList').val($('#verifiedInvoiceList').val()+','+item.invoiceCode)
                addVerifiedInvoiceItem(item);
            });
        });

    } else {
        $('#progress').css('display', 'block');
        $('#progress').addClass('progress').one('webkitAnimationEnd',
        function() {
            $(this).removeClass('progress');
            $('#progress').css('display', 'none');
            $('#warnDiv').css('display', 'block');
            $('#warnDiv').addClass('warnInvoiceMove').one('webkitAnimationEnd',
            function() {
                $(this).removeClass('warnInvoiceMove');
                $('#warnDiv').css('display', 'none');
                addWarnedInvoiceItem()
            });
        });

    }
}

function addVerifiedInvoiceItem(data) {
    var item = '<div style="width:100%;background-color: #f9f9f9;color:black;font-size:12px;border-bottom:2px solid white;">';
    item += '<div style="float:left;width:15%;">' + data.invoiceCode + '</div>';
    item += '<div style="float:left;width:12%;">' + data.invoiceNumber + '</div>';
    item += '<div style="float:left;width:13%;">' + data.invoiceDate + '</div>';
    item += '<div style="float:left;width:10%;">' + data.amount + '</div>';
    item += '<div style="float:left;width:10%;">' + data.tax + '</div>';
    item += '<div style="float:left;width:20%;"><p style="text-overflow:ellipsis;width:100%;overflow:hidden;white-space:nowrap;">' + data.salesName + '</p></div>';
    item += '<div style="float:left;width:20%;"><p style="text-overflow:ellipsis;width:100%;overflow:hidden;white-space:nowrap;">' + data.salesTaxNumber + '</p></div>';
    item += '<div style="clear:both;"></div>';
    item += '</div>';

    $('#verifiedInvoiceContent').html($('#verifiedInvoiceContent').html() + item)
}

function addWarnedInvoiceItem() {
    var item = '<div style="width:100%;background-color: #f9f9f9;color:black;font-size:12px;border-bottom:2px solid white;">';
    item += '<div style="float:left;width:15%;">' + $('#invoiceCode').val() + '</div>';
    item += '<div style="float:left;width:12%;">' + $('#invoiceNumber').val() + '</div>';
    item += '<div style="float:left;width:13%;">' + $('#invoiceDate').val() + '</div>';
    item += '<div style="float:left;width:10%;">' + $('#invoiceAmount').val() + '</div>';
    item += '<div style="float:left;width:10%;"></div>';
    item += '<div style="float:left;width:20%;"></div>';
    item += '<div style="float:left;width:20%;"></div>';
    item += '<div style="clear:both;"></div>';
    item += '</div>';

    $('#warnedInvoiceContent').html($('#warnedInvoiceContent').html() + item)
}

function sendEmail(){

    var postValue = {
        "codeList": $('#verifiedInvoiceList').val(),
    };

    callAjax('/invoiceService/sendEmail', '', 'sendEmailCallback', '', 'POST', postValue, '');
}

function sendEmailCallBack(data){
    if (data.status == 'ok') {
        $('#verifiedInvoiceList').val('');
        $('#verifiedInvoiceContent').html('');
    }
}