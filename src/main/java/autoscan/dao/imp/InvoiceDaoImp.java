package autoscan.dao.imp;

import autoscan.Biz.EmailSender;
import autoscan.dao.InvoiceDao;
import autoscan.model.Email;
import autoscan.model.Invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class InvoiceDaoImp implements InvoiceDao{

    public static final String DEFAULT_MLS_FROM_ADDRESS = "victor.guo@sc.com";
    public static final String DEFAULT_MLS_FROM_DISPLAY = "Victor Test";
    public static final String DEFAULT_MLS_REPLY_TO_ADDRESS = "victor.guo@sc.com";
    public static final String DEFAULT_MLS_REPLY_TO_DISPLAY = "Victor Test";

    @Value("${db.connectString}")
    protected String dbConnectString;

    @Override
    public Invoice getInvoiceByCode(String code, String number) throws SQLException {
        Invoice item = new Invoice();

        String selectSql = String.format("SELECT InvoiceCode, InvoiceNumber, InvoiceDate, SalesName, SalesTaxNumber, Amount, Tax FROM Invoice where InvoiceCode = '%s' and InvoiceNumber = '%s'", code, number);
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    if(rs.next()) {
                        int i = 1;
                        item.setInvoiceCode(rs.getString(i++));
                        item.setInvoiceNumber(rs.getString(i++));
                        item.setInvoiceDate(rs.getString(i++));
                        item.setSalesName(rs.getString(i++));
                        item.setSalesTaxNumber(rs.getString(i++));
                        item.setAmount(rs.getString(i++));
                        item.setTax(rs.getString(i++));
                    }
                }
            }
        }

        return item;
    }

    @Override
    public void sendEmail(String codeList) {

        String[] list = codeList.trim().split(",");

        sendEmail(new String[]{"victor.guo@sc.com"},"test","list");
    }

    @Autowired
    private EmailSender emailSender;

    private boolean sendEmail(String[] toAddress, String subject, String content) {
        Email email = new Email(toAddress, subject, content);
        email.setFrom(DEFAULT_MLS_FROM_ADDRESS);
        email.setFromDisplay(DEFAULT_MLS_FROM_DISPLAY);
        email.setReplyTo(DEFAULT_MLS_REPLY_TO_ADDRESS);
        email.setReplyToDisplay(DEFAULT_MLS_REPLY_TO_DISPLAY);
        return emailSender.sendEmail(email);
    }
}
