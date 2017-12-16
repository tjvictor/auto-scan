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

function loadPage(url){
    $('#mainDiv').load(url);
}

function textOnchange() {

    //01,04,1200164320,02388138,224.27,20170430,72421672501935626208,3C4C,
    //01,04,3100162130,44557616,132.67,20170525,72421672501935626208,3C4C,
    var dataLength = $('#invoiceCode').val().split(',').length;
    if(dataLength == 9){
        var data = $('#invoiceCode').val().split(',');

        $('#invoiceCode').val(data[2]);
        $('#invoiceNumber').val(data[3]);
        $('#invoiceDate').val(data[5]);
        $('#invoiceAmount').val(data[4]);

        callAjax('/invoiceService/getInvoiceByCode', '', 'getInvoiceByCodeCallback', '', '', 'code=' + $('#invoiceCode').val()+'&number='+$('#invoiceNumber').val(), '');
    }
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
                $('#verifiedInvoiceList').val($('#verifiedInvoiceList').val()+','+item.id)
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
    item += '<input class="verifiedInvoice" type="text" style="display:none;" value="'+data.id+'">';
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

function login() {
    var sid = $('#bankIdTxt').val();
    var password = $('#passwordTxt').val();
    var role = $('#roleSelect').val();
    if (sid == '' || password == '')
        $('#errorShow').text('Bank Id or Password can\'t be empty!');
    else{
        var parameter = 'bankId='+sid+'&password='+password+'&role='+role;
        callAjax('/invoiceService/login', '', 'loginCallback', '', '', parameter, '');
    }
}

function loginCallback(data){
    if (data.status == "ok") {
        var user = {
            "id" : data.callBackData.id,
            "bankId" : data.callBackData.bankId,
            "name" : data.callBackData.name,
            "roleId" : data.callBackData.role,
            "tel" : data.callBackData.telNumber,
        }
        Cookies.set("user", user, { expires: 1 });
        window.location = '/index.html';
    }else{
        $('#errorShow').text(data.prompt);
    }
}

function logout(){
    Cookies.remove("user");
    window.location = '/login.html';
}

function initialPo(){
    var user;
    if(Cookies.get("user"))
        user = jQuery.parseJSON(Cookies.get("user"));
    $('#staffIdTxt').val(user.bankId);
    $('#telNumberTxt').val(user.tel);
    callAjax('/invoiceService/getBarCodeString', '', 'getBarCodeStringCallback', '', '', '', '');
}

function getBarCodeStringCallback(data){
    if(data.status == "ok") {
        $('#codeBarImg').barcode(data.callBackData, "code39",{barWidth:2, barHeight:30});
        $('#barCodeValueTxt').val(data.callBackData);
    }
}

function insertPoTicket(){
    var vendorId = $('#vendorIdTxt').val();
    var claimCurrency = $('#claimCurrencyTxt').val();
    var claimAmount = $('#claimAmountTxt').val();
    var bu = $('#buTxt').val();
    var poNumber = $('#poNumberTxt').val();
    var poReceivedAmount = $('#poReceivedAmountTxt').val();
    var comments = $('#commentsTxt').val();
    var barCode = $('#barCodeValueTxt').val();
    var staffId = $('#staffIdTxt').val();
    var telNumber = $('#telNumberTxt').val();
    if(vendorId == '' || claimCurrency == '' || claimAmount == '' ||
        bu == '' || poNumber == '' || poReceivedAmount == '' ||
        comments == '' || barCode == ''){
            showPop('PLEASE FILL IN ALL THE INFORMATION.');
            return;
        }

    var postValue = {
        "barCode": barCode,
        "vendorId": vendorId,
        "claimCurrency": claimCurrency,
        "claimAmount": claimAmount,
        "bu": bu,
        "poNumber": poNumber,
        "poReceivedAmount": poReceivedAmount,
        "comment": comments,
        "staffId": staffId,
        "telNumber": telNumber,
    };

    callAjax('/invoiceService/insertPoTicket', '', 'insertPoTicketCallback', '', 'POST', postValue, '');
}

function insertPoTicketCallback(data){
    if(data.status == "ok") {
        showPop(data.prompt);
        $('#submitBtn').css('background-color','buttonface');
        $('#submitBtn').css('color','graytext');
        $('#submitBtn').attr('disabled','disabled');
    }
}

function searchPOTicket(){
    var dataLength = $('#barCodeTxt').val().length;
    if(dataLength == 10){
        var data = $('#barCodeTxt').val();
        callAjax('/invoiceService/getPOTicketByBarCode', '', 'getPOTicketByBarCodeCallback', '', '', 'barCode=' + data, '');
    }
}

function getPOTicketByBarCodeCallback(data){
    $('#reminderSpan').text(data.prompt);
    if (data.status == "ok") {
        $('#poTicketDetailDiv').css('display','block');
        $('#poTicketProgressDiv').css('display','block');

        var item = data.callBackData;
        $('#ticketIdTxt').val(item.id);
        $('#vendorIdTxt').val(item.vendorId);
        $('#claimCurrencyTxt').val(item.claimCurrency);
        $('#claimAmountTxt').val(item.claimAmount);
        $('#buTxt').val(item.bu);
        $('#poNumberTxt').val(item.poNumber);
        $('#poReceivedAmountTxt').val(item.poReceivedAmount);
        $('#commentsTxt').val(item.comment);
        $('#staffIdTxt').val(item.staffId);
        $('#telNumberTxt').val(item.telNumber);
        $('#codeBarImg').barcode(item.barCode, "code39",{barWidth:2, barHeight:30});

        if($("#invoiceCode")[0])
            $("#invoiceCode")[0].focus()

        $('#submitDate').text('');
        $('#verifiedDate').text('');
        $('#completedDate').text('');
        $('#submitDate').css('display','none');
        $('#verifiedDate').css('display','none');
        $('#completedDate').css('display','none');
        $('#submitDate').next().css('display','none');
        $('#verifiedDate').next().css('display','none');
        $('#completedDate').next().css('display','none');

        $('#draftImg').css('display','none');
        $('#inProgressImg').css('display','none');
        $('#completedImg').css('display','none');

        if(item.status == 0){
            $('#submitDate').next().css('display','inline');
            $('#draftImg').css('display','block');
        }else if(item.status == 1){
            $('#submitDate').text(item.submitDate);
            $('#submitDate').css('display','block');
            $('#verifiedDate').next().css('display','inline');
            $('#inProgressImg').css('display','block');
        }else if(item.status == 2){
            $('#submitDate').text(item.submitDate);
            $('#verifiedDate').text(item.verifiedDate);
            $('#submitDate').css('display','block');
            $('#verifiedDate').css('display','block');
            $('#completedDate').next().css('display','inline');
            $('#inProgressImg').css('display','block');
        }else if(item.status == 3){
            $('#submitDate').text(item.submitDate);
            $('#verifiedDate').text(item.verifiedDate);
            $('#completedDate').text(item.completedDate);
            $('#submitDate').css('display','block');
            $('#verifiedDate').css('display','block');
            $('#completedDate').css('display','block');
            $('#completedImg').css('display','block');
        }
    }else{
        $('#poTicketDetailDiv').css('display','none');
        $('#poTicketProgressDiv').css('display','none');
    }
}

function verifyTicket(){
    var ticketId = $('#ticketIdTxt').val();

    if(ticketId==''){
        showPop('PLEASE SEARCH TICKET FIRSTLY.');
        return;
    }

    var invoiceList = $('#verifiedInvoiceList').val();

    var postValue = {
        "ticketId": ticketId,
        "invoiceList": invoiceList,
    };

    callAjax('/invoiceService/verifyPOTicket', '', 'verifyPOTicketCallback', '', 'POST', postValue, '');
}

function verifyPOTicketCallback(data){
    showPop(data.prompt);
    if (data.status == "ok") {
        $('#reminderSpan').text('');
        $('#barCodeTxt').val('');
        $('#ticketIdTxt').val('');
        $('#vendorIdTxt').val('');
        $('#claimCurrencyTxt').val('');
        $('#claimAmountTxt').val('');
        $('#buTxt').val('');
        $('#poNumberTxt').val('');
        $('#poReceivedAmountTxt').val('');
        $('#commentsTxt').val('');
        $('#staffIdTxt').val('');
        $('#telNumberTxt').val('');
        $('#invoiceCode').val('');
        $('#invoiceNumber').val('');
        $('#invoiceDate').val('');
        $('#invoiceAmount').val('');
        $('#verifiedInvoiceContent').html('');
        $('#warnedInvoiceContent').html('');
        $('#verifiedInvoiceList').val('');
    }
}

function rejectTicket(){
    var rejectReason = $('#rejectTxt').val();
    if(rejectReason == ''){
        showPop('PLEASE INPUT REJECTED REASON.');
    }else{
        showPop('SORRY, REJECTED FUNCTION IS NOT SUPPORTED IN DEMO.');
    }
}

function showPop(reminder){
    $('#popReminder').text(reminder);
    $('#maskDiv').css('display','block');
}

function hidePop(){
    $('#maskDiv').css('display','none');
}

function packageTabChange(item){
    $(item).css('background-color','#FF5300');
    $(item).css('color','#F7DED2');
    $(item).siblings().css('background-color','');
    $(item).siblings().css('color','');
}

function verifiedInvoiceTab(item){
    packageTabChange(item);

    $('#verifiedInvoiceDiv').css('display','block');
    $('#nonVerifiedInvoiceDiv').css('display','none');
    $('#historyPackageDiv').css('display','none');

    callAjax('/invoiceService/getInvoiceListByStatus', '', 'getVerifiedInvoiceListCallback', '', '', 'status=1', '');
}
function getVerifiedInvoiceListCallback(data){
    $('#verifiedInvoiceContent').html('');
    $('#verifiedInvoiceList').val('');
    if(data.status == "ok"){
        for(var i = 0 ; i < data.callBackData.length ; i++){
            var obj = data.callBackData[i];
            var item = '<div style="width:100%;background-color: #f9f9f9;color:black;font-size:12px;border-bottom:2px solid white;">';
            item += '<input class="verifiedInvoice" type="text" style="display:none;" value="'+obj.id+'">';
            item += '<div style="float:left;width:10%;">' + obj.invoiceCode + '</div>';
            item += '<div style="float:left;width:10%;">' + obj.invoiceNumber + '</div>';
            item += '<div style="float:left;width:10%;">' + obj.invoiceDate + '</div>';
            item += '<div style="float:left;width:8%;">' + obj.amount + '</div>';
            item += '<div style="float:left;width:8%;">' + obj.tax + '</div>';
            item += '<div style="float:left;width:27%;"><p style="text-overflow:ellipsis;width:100%;overflow:hidden;white-space:nowrap;">' + obj.salesName + '</p></div>';
            item += '<div style="float:left;width:27%;"><p style="text-overflow:ellipsis;width:100%;overflow:hidden;white-space:nowrap;">' + obj.salesTaxNumber + '</p></div>';
            item += '<div style="clear:both;"></div>';
            item += '</div>';

            $('#verifiedInvoiceContent').html($('#verifiedInvoiceContent').html() + item);
            $('#verifiedInvoiceList').val($('#verifiedInvoiceList').val() + obj.id + ',');
        }
    }
}


function nonVerifiedInvoiceTab(item){
    packageTabChange(item);

    $('#verifiedInvoiceDiv').css('display','none');
    $('#nonVerifiedInvoiceDiv').css('display','block');
    $('#historyPackageDiv').css('display','none');

    callAjax('/invoiceService/getInvoiceListByStatus', '', 'getNonVerifiedInvoiceListCallback', '', '', 'status=0', '');
}
function getNonVerifiedInvoiceListCallback(data){
    $('#warnedInvoiceContent').html('');
    $('#nonVerifiedInvoiceList').val('');
    if(data.status == "ok"){
        for(var i = 0 ; i < data.callBackData.length ; i++){
            var obj = data.callBackData[i];
            var item = '<div style="width:100%;background-color: #f9f9f9;color:black;font-size:12px;border-bottom:2px solid white;">';
            item += '<input class="verifiedInvoice" type="text" style="display:none;" value="'+obj.id+'">';
            item += '<div style="float:left;width:10%;">' + obj.invoiceCode + '</div>';
            item += '<div style="float:left;width:10%;">' + obj.invoiceNumber + '</div>';
            item += '<div style="float:left;width:10%;">' + obj.invoiceDate + '</div>';
            item += '<div style="float:left;width:8%;">' + obj.amount + '</div>';
            item += '<div style="float:left;width:8%;">' + obj.tax + '</div>';
            item += '<div style="float:left;width:27%;"><p style="text-overflow:ellipsis;width:100%;overflow:hidden;white-space:nowrap;">' + obj.salesName + '</p></div>';
            item += '<div style="float:left;width:27%;"><p style="text-overflow:ellipsis;width:100%;overflow:hidden;white-space:nowrap;">' + obj.salesTaxNumber + '</p></div>';
            item += '<div style="clear:both;"></div>';
            item += '</div>';

            $('#warnedInvoiceContent').html($('#warnedInvoiceContent').html() + item);
            $('#nonVerifiedInvoiceList').val($('#nonVerifiedInvoiceList').val() + obj.id + ',');
        }
    }
}


function historyPackageTab(item){
    packageTabChange(item);

    $('#verifiedInvoiceDiv').css('display','none');
    $('#nonVerifiedInvoiceDiv').css('display','none');
    $('#historyPackageDiv').css('display','block');

    callAjax('/invoiceService/getInvoicePackage', '', 'getInvoicePackageCallback', '', '', '', '');
}
function getInvoicePackageCallback(data){
    $('#packageContent').html('');
    $('#packageDetailContent').html('');
    if(data.status == "ok"){
        for(var i = 0 ; i < data.callBackData.length ; i++){
            var obj = data.callBackData[i];
            var id = "'"+obj.id+"'";
            var item = '<div style="width:100%;background-color: #f9f9f9;color:black;font-size:12px;border-bottom:2px solid white;cursor:pointer;" onclick="getInvoiceByPackageId('+id+')">';
            item += '<div style="float:left;width:50%;">' + obj.name + '</div>';
            item += '<div style="float:left;width:50%;">' + obj.date + '</div>';
            item += '<div style="clear:both;"></div>';
            item += '</div>';

            $('#packageContent').html($('#packageContent').html() + item);
        }
    }
}

function getInvoiceByPackageId(packageId){
    callAjax('/invoiceService/getInvoiceByPackageId', '', 'getInvoiceByPackageIdCallback', '', '', 'packageId='+packageId, '');
}

function getInvoiceByPackageIdCallback(data){
    $('#packageDetailContent').html('');
    if(data.status == "ok"){
        for(var i = 0 ; i < data.callBackData.length ; i++){
            var obj = data.callBackData[i];
            var item = '<div style="width:100%;background-color: #f9f9f9;color:black;font-size:12px;border-bottom:2px solid white;">';
            item += '<input class="verifiedInvoice" type="text" style="display:none;" value="'+obj.id+'">';
            item += '<div style="float:left;width:10%;">' + obj.invoiceCode + '</div>';
            item += '<div style="float:left;width:10%;">' + obj.invoiceNumber + '</div>';
            item += '<div style="float:left;width:10%;">' + obj.invoiceDate + '</div>';
            item += '<div style="float:left;width:8%;">' + obj.amount + '</div>';
            item += '<div style="float:left;width:8%;">' + obj.tax + '</div>';
            item += '<div style="float:left;width:27%;"><p style="text-overflow:ellipsis;width:100%;overflow:hidden;white-space:nowrap;">' + obj.salesName + '</p></div>';
            item += '<div style="float:left;width:27%;"><p style="text-overflow:ellipsis;width:100%;overflow:hidden;white-space:nowrap;">' + obj.salesTaxNumber + '</p></div>';
            item += '<div style="clear:both;"></div>';
            item += '</div>';

            $('#packageDetailContent').html($('#packageDetailContent').html() + item);
        }
    }
}

function packageVerifiedInvoice(data){

    var postValue = {
       "invoiceList" : $('#verifiedInvoiceList').val(),
    };
    callAjax('/invoiceService/packageVerifiedInvoice', '', 'packageVerifiedInvoiceCallback', '', 'POST', postValue, '');
}

function packageVerifiedInvoiceCallback(data){
    showPop(data.prompt);
    if(data.status == "ok"){
        $('#verifiedInvoiceList').val('');
        $('#verifiedInvoiceContent').html('');
    }
}

function nonVerifiedInvoiceEmail(){
    showPop('SORRY, THIS FUNCTION IS NOT SUPPORTED IN DEMO.');
}