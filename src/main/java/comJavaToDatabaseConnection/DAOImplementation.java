package comJavaToDatabaseConnection;

import java.sql.*;

public class DAOImplementation implements IDataAccessObject {
    private static final String URL = "jdbc:mysql://localhost:3306/address_book_service_db";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "Energy123@*/+";

    //queries
    public static final String CREATE_TABLE =
            "CREATE TABLE + sample_table (" +
                    " ID int not null primary key auto_increment," +//<== getting error
                    " name varchar(50)," +
                    " phone_number varchar(20)" +
                    ")";
    public static final String INSERT_INTO =
            "INSERT INTO + sample_table + (first_name,last_name,address,city,state,zip,phonenumber,email)" +
                    "values" +
                    "(" +
                    "   ?,?" +//name and phone_number
                    ")";
    public static final String UPDATE_TABLE = "UPDATE sample_table SET ? = ? WHERE ? = ?";
    public static final String DELETE_TABLE = "DROP TABLE + sample_table";
    public static String selectFromTable = "SELECT * FROM sample_table";

    Connection connection = null;
    Statement statement = null;
    PreparedStatement temp = null;

    /**
     * gets a connection with database and returns it
     * using singleton design pattern
     * made it synchronized to prevent thread interference and consistency problem
     *
     * @return
     * @throws SQLException
     */
    synchronized private Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        }
        return connection;
    }

    /**
     * creates a statement and returns it
     * using singleton design pattern
     * made it synchronized to prevent thread interference and consistency problem
     *
     * @return
     * @throws SQLException
     */
    synchronized private Statement getStatement() throws SQLException {
        DAOImplementation daoImplementation = new DAOImplementation();
        if (statement == null) {
            statement = daoImplementation.getConnection().createStatement();
        }
        return statement;
    }

    /**
     * creates PreparedStatement
     * NOTE --> when need a new PrepareStatement, put temp = null before calling new instance
     *
     * @throws SQLException
     */
    private PreparedStatement getPreparedStatement(String operation) throws SQLException {
        if (temp == null) {
            PreparedStatement preparedStatement = getConnection().prepareStatement(operation);
            temp = preparedStatement;
        }
        return temp;
    }

    //METHODS
    @Override
    public void createTable() throws SQLException {
        getStatement().executeUpdate(CREATE_TABLE);
    }

    @Override
    public void insertIntoTable(int numberOfContacts) throws SQLException {
        PreparedStatement preparedStatement = getPreparedStatement(INSERT_INTO);
        for (int i = 1; i <= numberOfContacts; i++) {
            preparedStatement.setString(2, "Person " + i);
            preparedStatement.setString(3, "+91 000000000" + i);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void updateTable(String name, String phone_number) throws SQLException {
        closeResources(temp);
        temp = null;//to make new PreparedStatement
        PreparedStatement preparedStatement = getPreparedStatement(UPDATE_TABLE);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, phone_number);
        preparedStatement.executeUpdate();
    }

    @Override
    public void deleteTable() throws SQLException {
        Statement statement = getStatement();
        statement.executeUpdate(DELETE_TABLE);
    }

    @Override
    public ResultSet getResults() throws SQLException {
        Statement statement = getStatement();
        ResultSet resultSet = statement.executeQuery(selectFromTable);
        return resultSet;
    }

    /**
     * prints the data
     * @param resultSet
     * @throws SQLException
     */
    public void printResultSet(ResultSet resultSet) throws SQLException {
        System.out.println("ID   name   phone_number");
        while (resultSet.next()) {
            System.out.println(resultSet.getInt(1) + "    " + resultSet.getString(2) + "  " + resultSet.getString(3));
        }
    }

    public void closeResources(PreparedStatement preparedStatement) throws SQLException {
        CloseResources closeResources = new CloseResources();
        closeResources.closePreparedStatement(preparedStatement);
    }

    //closing resources
    public void closeResources(Statement statement,PreparedStatement preparedStatement,Connection connection) throws SQLException {
        CloseResources closeResources = new CloseResources();
        closeResources.closeStatement(statement);
        closeResources.closePreparedStatement(preparedStatement);
        closeResources.closeConnection(connection);
    }

    public static void main(String[] args) throws SQLException {
        DAOImplementation daoImplementation = new DAOImplementation();
        daoImplementation.createTable();
        daoImplementation.insertIntoTable(5);
        daoImplementation.printResultSet(daoImplementation.getResults());
        daoImplementation.updateTable("Kirankumar", "+91 596237410");
        daoImplementation.printResultSet(daoImplementation.getResults());
        daoImplementation.deleteTable();
        daoImplementation.closeResources(daoImplementation.getStatement(), daoImplementation.temp, daoImplementation.getConnection());
    }
}
