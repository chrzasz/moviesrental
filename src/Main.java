import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class Main {
    public static void main(String[] args) {

/*Korzystając z PreparedStatement, napisz funkcję obsługującą wypożyczenie filmu
przez klienta. */

        String url = args[0] + "?serverTimezone=" + TimeZone.getDefault().getID();

        String sql = "INSERT INTO rents(" +
                "rentedMovieId,customer,status,rentPricePerDay,rentedDate,returnedDate)\n" +
                "VALUES (?,?,?,?,?,?)";

        ResultSet resultSet = null;

        try (Connection connection = DriverManager.getConnection(url, args[1], args[2]);
             PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {

            System.out.println(String.format("Connected to database %s " + "successfully.", connection.getCatalog()));

            // set parameters for statement
            pstmt.setInt(1, 1);
            pstmt.setInt(2, 1);
            pstmt.setString(3, String.valueOf("In rent"));
            pstmt.setDouble(4,Double.valueOf("9.99"));
            pstmt.setDate(5, Date.valueOf("1980-01-04"));
            pstmt.setString(6, null);

            int myId = 0;

            int rowAffected = pstmt.executeUpdate();
            if (rowAffected == 1) {
                resultSet = pstmt.getGeneratedKeys();
                if (resultSet.next())
                    myId = resultSet.getInt(1);

            }

        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            try {
                if(resultSet != null)  resultSet.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }


    }

}

