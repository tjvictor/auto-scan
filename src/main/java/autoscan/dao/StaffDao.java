package autoscan.dao;

import autoscan.model.Staff;

import java.sql.SQLException;

public interface StaffDao {

    Staff login(String bankId, String password, String role) throws SQLException;
}
