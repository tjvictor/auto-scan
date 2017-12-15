package autoscan.model;

public class Invoice {

    private String Id;

    //发票代码
    private String invoiceCode;

    //发票号码
    private String invoiceNumber;

    //开票日期
    private String invoiceDate;

    //销方名称
    private String salesName;

    //销方税号
    private String salesTaxNumber;

    //金额
    private String amount;

    //税额
    private String tax;

    private String status;

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

    public String getSalesTaxNumber() {
        return salesTaxNumber;
    }

    public void setSalesTaxNumber(String salesTaxNumber) {
        this.salesTaxNumber = salesTaxNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
