package comJavaToDatabaseConnection;

import java.sql.*;

public class PreparedStatementWithAddressbook {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/address_book_db";//127.0.0.1:3306//localhost:3306
        String userName = "root";
        String password = "Energy123@*/+";
        String query1 = "select * from address_book";
        String query2 = "insert into address_book " +
                        "(first_name,last_name,address,city,state,zip,phonenumber,email) " +
                        "values(?,?,?,?,?,?,?,?)";
        String query3 = "DELETE FROM address_book WHERE id = ?";

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
        for (int i = 10; i <= 15; i++) {
            preparedStatement.setString(1, "new " + i);
            preparedStatement.setString(2, "Person " + i);
            preparedStatement.setString(3, "Person " + i+"'s address");
            preparedStatement.setString(4, "Person " + i+"; city");
            preparedStatement.setString(5, "Person " + i+"'s state");
            preparedStatement.setInt(6, i);
            preparedStatement.setString(7, "+91 00000000" + i);
            preparedStatement.setString(8, "new" + i+"@gmail.com");
            preparedStatement.executeUpdate();
        }

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
         * using another Prepared statement for deleting data
         */
        PreparedStatement preparedStatement1 = connection.prepareStatement(query3);

        for (int i = 10; i <= 15; i++) {
            preparedStatement1.setInt(1, i);
            preparedStatement1.executeUpdate();
        }

        /**
         * closing resources
         */
        CloseResources.closeStatement(statement);
        CloseResources.closePreparedStatement(preparedStatement);
        CloseResources.closePreparedStatement(preparedStatement1);
        CloseResources.closeConnection(connection);
    }
}
