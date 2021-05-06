package comJavaToDatabaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IDataAccessObject {
    void createTable() throws SQLException;
    void insertIntoTable(int numberOfContacts) throws SQLException;
    void updateTable(String name, String phone_number) throws SQLException;
    void deleteTable() throws SQLException;
    ResultSet getResults() throws SQLException;
}
