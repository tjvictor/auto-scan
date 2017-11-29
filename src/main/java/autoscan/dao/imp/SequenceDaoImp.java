package autoscan.dao.imp;

import autoscan.dao.SequenceDao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class SequenceDaoImp implements SequenceDao {

    @Value("${db.connectString}")
    protected String dbConnectString;


    @Override
    public void insertSequence(String uuid) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbConnectString)){
            String insertSql = "insert into Sequence values(?);";
            try(PreparedStatement ps = connection.prepareStatement(insertSql)) {
                ps.setString(1, uuid);
                ps.executeUpdate();
            }
        }
    }

    @Override
    public int getSequenceRowIdByUUID(String uuid) throws SQLException {

        String selectSql = String.format("SELECT rowid  FROM Sequence where id = '%s'", uuid);

        try (Connection connection = DriverManager.getConnection(dbConnectString)) {
            try (Statement stmt = connection.createStatement()) {
                try(ResultSet rs = stmt.executeQuery(selectSql)) {
                    if(rs.next()){
                        return rs.getInt(1);
                    }
                }
            }
        }

        return -1;
    }
}
