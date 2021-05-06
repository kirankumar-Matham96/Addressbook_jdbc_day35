package comJavaToDatabaseConnection;

import java.sql.*;

public class BatchProcessingForAddressbook {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/address_book_db";//127.0.0.1:3306//localhost:3306
        String userName = "root";
        String password = "Energy123@*/+";
        String query1 = "select * from id_name";
        String query2 = "insert into id_name (first_name,last_name,address,city,state,zip,phonenumber,email)" +
                        " values(?,?,?,?,?,?,?,?)";
        String query3 = "DELETE FROM id_name WHERE n_id = ?";

        /**
         * establishing a connection with database
         */
        Connection connection = DriverManager.getConnection(url, userName, password);

        /**
         * using Prepared statement
         */
        PreparedStatement preparedStatement = connection.prepareStatement(query2);

        /**
         * using Prepared statement to execute same query multiple times
         */
        for (int i = 6; i <= 10; i++) {
            preparedStatement.setString(1, "new " + i);
            preparedStatement.setString(2, "Person " + i);
            preparedStatement.setString(3, "Person " + i+"'s address");
            preparedStatement.setString(4, "Person " + i+"; city");
            preparedStatement.setString(5, "Person " + i+"'s state");
            preparedStatement.setInt(6, i);
            preparedStatement.setString(7, "+91 00000000" + i);
            preparedStatement.setString(8, "new" + i+"@gmail.com");
            /**
             * adding as a batch
             */
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();

        /**
         * using statement to get the data from database
         */
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query1);

        /**
         * printing the data
         */
        while (resultSet.next()) {
            //to get all the data from the table
            System.out.print(resultSet.getInt(1) + " ");
            System.out.println(resultSet.getString("name"));
        }

        /**
         * using another Prepared statement for deleting data
         */
        PreparedStatement preparedStatement1 = connection.prepareStatement(query3);
        for (int i = 6; i <= 10; i++) {
            preparedStatement1.setInt(1, i);
            preparedStatement1.addBatch();
        }
        preparedStatement1.executeBatch();

        /**
         * closing resources
         */
        CloseResources.closePreparedStatement(preparedStatement);
        CloseResources.closePreparedStatement(preparedStatement1);
        CloseResources.closeConnection(connection);
    }
}
