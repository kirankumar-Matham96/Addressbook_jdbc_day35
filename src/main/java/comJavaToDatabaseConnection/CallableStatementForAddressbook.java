package comJavaToDatabaseConnection;

import java.sql.*;

public class CallableStatementForAddressbook {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/address_book_db";//127.0.0.1:3306//localhost:3306
        String userName = "root";
        String password = "Energy123@*/+";
        Connection connection = DriverManager.getConnection(url, userName, password);
        ResultSet resultSet = null;

        /**
         * using callable statement to call a stored function from the database
         */
        CallableStatement callableStatement = connection.prepareCall("{call select_all()}");
        boolean hasResult = callableStatement.execute();

        /**
         * getting results if callable statement executed
         */
        if(hasResult){
            resultSet = callableStatement.getResultSet();
        }

        /**
         * printing results
         */
        while (resultSet.next()) {
            //to get all the data from the table
            System.out.print(resultSet.getInt(1) + " ");
            System.out.println(
                resultSet.getString("first_name")+" "+
                resultSet.getString("last_name")+" "+
                resultSet.getString("address")+" "+
                resultSet.getString("city")+" "+
                resultSet.getString("state")+" "+
                resultSet.getString("zip")+" "+
                resultSet.getString("phonenumber")+" "+
                resultSet.getString("email")
            );
        }

        /**
         * closing resources
         */
        CloseResources.closeCallableStatement(callableStatement);
        CloseResources.closeConnection(connection);
    }
}
