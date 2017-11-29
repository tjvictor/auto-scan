package autoscan.dao;

import java.sql.SQLException;

public interface SequenceDao {
    void insertSequence(String uuid) throws SQLException;

    int getSequenceRowIdByUUID(String uuid) throws SQLException;
}
