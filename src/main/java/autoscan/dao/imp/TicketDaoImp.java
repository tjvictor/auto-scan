package autoscan.dao.imp;

import autoscan.dao.TicketDao;
import autoscan.model.PoTicket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class TicketDaoImp implements TicketDao {

    @Value("${db.connectString}")
    protected String dbConnectString;

    @Override
    public void insertPoTicket(PoTicket item) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbConnectString)){
            String insertSql = "insert into POTicket values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
            int i = 1;
            try(PreparedStatement ps = connection.prepareStatement(insertSql)) {

                ps.setString(i++, item.getId());
                ps.setString(i++, item.getVendorId());
                ps.setString(i++, item.getClaimCurrency());
                ps.setString(i++, item.getClaimAmount());
                ps.setString(i++, item.getBu());
                ps.setString(i++, item.getPoNumber());
                ps.setString(i++, item.getPoReceivedAmount());
                ps.setString(i++, item.getComment());
                ps.setString(i++, item.getStaffId());
                ps.setString(i++, item.getTelNumber());
                ps.setString(i++, item.getSubmitDate());
                ps.setString(i++, "");
                ps.setString(i++, "");
                ps.setString(i++, item.getStatus());
                ps.setString(i++, item.getBarCode());
                ps.executeUpdate();
            }
        }
    }

    @Override
    public PoTicket getPOTicketByBarCode(String barCode) throws SQLException {
        PoTicket item = new PoTicket();

        String selectSql = String.format("SELECT Id, VendorId, ClaimCurrency, ClaimAmount, BU, PoNumber, PoReceivedAmount, Comment, StaffId, TelNumber, SubmitDate, VerifiedDate, CompletedDate, Status, BarCode FROM POTicket where BarCode = '%s';", barCode);

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    if (rs.next()) {
                        int i = 1;
                        item.setId(rs.getString(i++));
                        item.setVendorId(rs.getString(i++));
                        item.setClaimCurrency(rs.getString(i++));
                        item.setClaimAmount(rs.getString(i++));
                        item.setBu(rs.getString(i++));
                        item.setPoNumber(rs.getString(i++));
                        item.setPoReceivedAmount(rs.getString(i++));
                        item.setComment(rs.getString(i++));
                        item.setStaffId(rs.getString(i++));
                        item.setTelNumber(rs.getString(i++));
                        item.setSubmitDate(rs.getString(i++));
                        item.setVerifiedDate(rs.getString(i++));
                        item.setCompletedDate(rs.getString(i++));
                        item.setStatus(rs.getString(i++));
                        item.setBarCode(rs.getString(i++));
                    }
                }
            }
        }

        return item;
    }
}
