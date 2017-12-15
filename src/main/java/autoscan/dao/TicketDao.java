package autoscan.dao;

import autoscan.model.PoTicket;

import java.sql.SQLException;

public interface TicketDao {

    void insertPoTicket(PoTicket item) throws SQLException;

    PoTicket getPOTicketByBarCode(String barCode) throws SQLException;

    void verifyPOTicket(String id) throws SQLException;
}
