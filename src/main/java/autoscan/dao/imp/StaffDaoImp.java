package autoscan.dao.imp;

import autoscan.dao.StaffDao;
import autoscan.model.Staff;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class StaffDaoImp implements StaffDao{

    @Value("${db.connectString}")
    protected String dbConnectString;

    @Override
    public Staff login(String bankId, String password, String role) throws SQLException {
        Staff item = new Staff();

        String selectSql = String.format("SELECT a.Id, a.BankId, a.Password, a.Role, a.TelNumber FROM Staff a where a.BankId = '%s' and a.Password = '%s' and a.Role = '%s'", bankId, password, role);

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(selectSql)) {
                    if (rs.next()) {
                        int i = 1;
                        item.setId(rs.getString(i++));
                        item.setBankId(rs.getString(i++));
                        item.setPassword(rs.getString(i++));
                        item.setRole(rs.getString(i++));
                        item.setTelNumber(rs.getString(i++));
                    }
                }
            }
        }

        return item;
    }
}
