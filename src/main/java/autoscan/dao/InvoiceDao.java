package autoscan.dao;

import autoscan.model.Invoice;

import java.sql.SQLException;

public interface InvoiceDao {
    Invoice getInvoiceByCode(String code, String number) throws SQLException;

    void sendEmail(String codeList);
}
