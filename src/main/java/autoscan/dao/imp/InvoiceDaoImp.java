package autoscan.dao.imp;

import autoscan.Biz.EmailSender;
import autoscan.dao.InvoiceDao;
import autoscan.model.Email;
import autoscan.model.Invoice;
import autoscan.model.InvoicePackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

        String selectSql = String.format("SELECT Id, InvoiceCode, InvoiceNumber, InvoiceDate, SalesName, SalesTaxNumber, Amount, Tax, Status FROM Invoice where InvoiceCode = '%s' and InvoiceNumber = '%s'", code, number);
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    if(rs.next()) {
                        int i = 1;
                        item.setId(rs.getString(i++));
                        item.setInvoiceCode(rs.getString(i++));
                        item.setInvoiceNumber(rs.getString(i++));
                        item.setInvoiceDate(rs.getString(i++));
                        item.setSalesName(rs.getString(i++));
                        item.setSalesTaxNumber(rs.getString(i++));
                        item.setAmount(rs.getString(i++));
                        item.setTax(rs.getString(i++));
                        item.setStatus(rs.getString(i++));
                    }
                }
            }
        }

        return item;
    }

    @Override
    public List<Invoice> getInvoiceListByStatus(String status) throws SQLException {
        List<Invoice> items = new ArrayList<Invoice>();

        String selectSql = String.format("SELECT Id, InvoiceCode, InvoiceNumber, InvoiceDate, SalesName, SalesTaxNumber, Amount, Tax, Status FROM Invoice where Status = '%s'", status);
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    while(rs.next()) {
                        int i = 1;
                        Invoice item = new Invoice();
                        item.setId(rs.getString(i++));
                        item.setInvoiceCode(rs.getString(i++));
                        item.setInvoiceNumber(rs.getString(i++));
                        item.setInvoiceDate(rs.getString(i++));
                        item.setSalesName(rs.getString(i++));
                        item.setSalesTaxNumber(rs.getString(i++));
                        item.setAmount(rs.getString(i++));
                        item.setTax(rs.getString(i++));
                        item.setStatus(rs.getString(i++));

                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    @Override
    public List<Invoice> getInvoiceByPackageId(String packageId) throws SQLException {
        List<Invoice> items = new ArrayList<Invoice>();

        String selectSql = String.format("SELECT a.Id, a.InvoiceCode, a.InvoiceNumber, a.InvoiceDate, a.SalesName, a.SalesTaxNumber, a.Amount, a.Tax, a.Status FROM Invoice a join InvoicePackageRelation b on a.Id = b.InvoiceId  where b.PackageId = '%s'", packageId);
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    while(rs.next()) {
                        int i = 1;
                        Invoice item = new Invoice();
                        item.setId(rs.getString(i++));
                        item.setInvoiceCode(rs.getString(i++));
                        item.setInvoiceNumber(rs.getString(i++));
                        item.setInvoiceDate(rs.getString(i++));
                        item.setSalesName(rs.getString(i++));
                        item.setSalesTaxNumber(rs.getString(i++));
                        item.setAmount(rs.getString(i++));
                        item.setTax(rs.getString(i++));
                        item.setStatus(rs.getString(i++));

                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    @Override
    public List<InvoicePackage> getInvoicePackage() throws SQLException {
        List<InvoicePackage> items = new ArrayList<InvoicePackage>();

        String selectSql = String.format("SELECT Id, Name, Date from InvoicePackage order by Date desc");
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    while(rs.next()) {
                        int i = 1;
                        InvoicePackage item = new InvoicePackage();
                        item.setId(rs.getString(i++));
                        item.setName(rs.getString(i++));
                        item.setDate(rs.getString(i++));

                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    @Override
    public void verifyInvoices(String ids) throws SQLException {
        String[] idList = ids.split(",");
        try (Connection connection = DriverManager.getConnection(dbConnectString)){
            String insertSql = "update Invoice set Status=1 where Id=?";
            for(String item : idList)
                try(PreparedStatement ps = connection.prepareStatement(insertSql)) {
                    ps.setString(1, item);
                    ps.executeUpdate();
                }
        }
    }

    @Override
    public void packageVerifiedInvoice(String ids) throws SQLException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd_hhmmss");
        String packageId = UUID.randomUUID().toString();
        String packageName = sdf1.format(new Date());
        String packageDate = sdf.format(new Date());
        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            String insertSql = "insert into InvoicePackage values (?,?,?)";
            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                ps.setString(1, packageId);
                ps.setString(2, packageName);
                ps.setString(3, packageDate);
                ps.executeUpdate();
            }
        }

        String[] idList = ids.split(",");
        try (Connection connection = DriverManager.getConnection(dbConnectString)){
            String insertSql = "update Invoice set Status=2 where Id=?";
            for(String item : idList)
                try(PreparedStatement ps = connection.prepareStatement(insertSql)) {
                    ps.setString(1, item);
                    ps.executeUpdate();
                }
        }

        try (Connection connection = DriverManager.getConnection(dbConnectString)){
            String insertSql = "insert into InvoicePackageRelation values (?,?)";
            for(String item : idList)
                try(PreparedStatement ps = connection.prepareStatement(insertSql)) {
                    ps.setString(1, packageId);
                    ps.setString(2, item);
                    ps.executeUpdate();
                }
        }
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
