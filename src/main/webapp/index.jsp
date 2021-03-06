<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>渣打科营中心全国增值税发票查验平台</title>
    <link rel="stylesheet" type="text/css" href="res/css/common.css">
    <style>

    </style>
    <script type="text/javascript" src="res/js/jquery-1.11.2.js"></script>
    <script type="text/javascript" src="res/js/common.js"></script>
    <script type="text/javascript" src="res/js/js.cookie.js"></script>
    <script>

    </script>
</head>
<body>
<div id="top">
    <div id="headerbox">
        <div id="header">
            <div class="logo">渣打科营中心全国增值税发票查验平台</div>
        </div>
    </div>
</div>

<div class="main">
    <div class="leftPanel">
        <table>
            <tr style="display:table-row">
                <td style="padding:10px;font-size:16px;display:table-cell"><span style="color:red">*发票代码:</span></td>
                <td style="padding:10px;display:table-cell">
                    <input id="invoiceCode" type="text" style="-webkit-appearance: textfield;-webkit-rtl-ordering: logical;;width:200px;border: 1px solid #ccc; border-radius:5px;height:25px;line-height:25px;font-size:14px;color:#555" oninput="textOnchange()"></td>
            </tr>
            <tr style="display:table-row">
                <td style="padding:10px;font-size:16px;display:table-cell"><span style="color:red">*发票号码:</span></td>
                <td style="padding:10px;display:table-cell">
                    <input id="invoiceNumber" type="text" style="width:200px;border: 1px solid #ccc; border-radius:5px;height:25px;line-height:25px;font-size:14px;color:#555">
                </td>
            </tr>
            <tr style="display:table-row">
                <td style="padding:10px;font-size:16px;display:table-cell"><span style="color:red">*开票日期:</span></td>
                <td style="padding:10px;display:table-cell">
                    <input id="invoiceDate" type="text" style="width:200px;border: 1px solid #ccc; border-radius:5px;height:25px;line-height:25px;font-size:14px;color:#555">
                </td>
            </tr>
            <tr style="display:table-row">
                <td style="padding:10px;font-size:16px;display:table-cell"><span style="color:red">*开具金额:</span></td>
                <td style="padding:10px;display:table-cell">
                    <input id="invoiceAmount" type="text" style="width:200px;border: 1px solid #ccc; border-radius:5px;height:25px;line-height:25px;font-size:14px;color:#555">
                </td>
            </tr>
            <tr style="display:table-row">
                <td colspan="2" style="padding:10px;font-size:16px;display:table-cell">
                    <div style="height:20px;border-radius:10px;background-color:#ebebeb;border-left:1px solid transparent;border-right:1px solid transparent">
                        <div id="progress"
                             style="width:0px;display:none;height:20px;border-radius:10px;background-color:green;border:1px solid;background-image:url('res/img/progress.png')"></div>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="2" style="padding:10px;font-size:12px;display:table-cell;float:right;">
                    <div id="verifyDiv" style="position:relative; color:green;display:none;">
                        <img src="res/img/ok.png">Verified
                    </div>
                    <div id="warnDiv" style="position:relative; color:green;display:none;">
                        <img src="res/img/warn.png">Failed
                    </div>
                </td>
            </tr>
        </table>
    </div>
    <div class="rightPanel">
        <div style="width:100%;height:350px;">
            <div class="title" style="width:100%;height:20px;background-color:blue;color:white">已审核票据</div>
            <div class="content" style="width:100%;overflow:auto;height:300px;background-color:white;">
                <div id="verifiedInvoiceTitle" style="width:100%;background-color: #f9f9f9;color:black;font-size:12px;border-bottom:2px solid white;">
                    <div style="float:left;width:15%;">发票代码</div>
                    <div style="float:left;width:12%;">发票号码</div>
                    <div style="float:left;width:13%;">开票日期</div>
                    <div style="float:left;width:10%;">金额</div>
                    <div style="float:left;width:10%;">税额</div>
                    <div style="float:left;width:20%;">销方名称</div>
                    <div style="float:left;width:20%;">销方税号</div>
                    <div style="clear:both;"></div>
                    <input id="verifiedInvoiceList" type="hidden">
                </div>
                <div id="verifiedInvoiceContent">
                </div>
            </div>
            <div onclick="sendEmail()" style="float:right;width:150px;background-color:orange;color:white;border-radius:10px;text-align:center;margin-top:5px;box-shadow: 5px 5px 5px #888888;cursor:pointer;">Package & Send</div>
        </div>
        <div style="clear:both;"></div>
        <div style="width:100%;height:220px;margin-top:10px;">
            <div class="title" style="width:100%;height:20px;background-color:blue;color:white">未审核票据</div>
            <div class="content" style="width:100%;overflow:auto;height:200px;background-color:white;">
                <div id="warnedInvoiceTitle" style="width:100%;background-color: #f9f9f9;color:black;font-size:12px;border-bottom:2px solid white;">
                    <div style="float:left;width:15%;">发票代码</div>
                    <div style="float:left;width:12%;">发票号码</div>
                    <div style="float:left;width:13%;">开票日期</div>
                    <div style="float:left;width:10%;">金额</div>
                    <div style="float:left;width:10%;">税额</div>
                    <div style="float:left;width:20%;">销方名称</div>
                    <div style="float:left;width:20%;">销方税号</div>
                    <div style="clear:both;"></div>
                </div>
                <div id="warnedInvoiceContent">
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>