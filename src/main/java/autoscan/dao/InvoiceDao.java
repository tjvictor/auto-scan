package autoscan.dao;

import autoscan.model.Invoice;
import autoscan.model.InvoicePackage;

import java.sql.SQLException;
import java.util.List;

public interface InvoiceDao {
    Invoice getInvoiceByCode(String code, String number) throws SQLException;

    List<Invoice> getInvoiceListByStatus(String status) throws SQLException;

    List<Invoice> getInvoiceByPackageId(String packageId) throws SQLException;

    List<InvoicePackage> getInvoicePackage() throws SQLException;

    void verifyInvoices(String ids) throws SQLException;

    void packageVerifiedInvoice(String ids) throws SQLException;

    void sendEmail(String codeList);
}
