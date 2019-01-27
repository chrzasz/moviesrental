import java.sql.*;
import java.util.TimeZone;

public class Main {
    public static void main(String[] args) {

/*Korzystając z PreparedStatement, napisz funkcję obsługującą wypożyczenie filmu
przez klienta. */

        String url = args[0] + "?serverTimezone=" + TimeZone.getDefault().getID();

        String sqlUpdate = "UPDATE moviesinfo "
                + "SET description = ? "
                + "WHERE movieInfoId = ?";

        try (Connection connection = DriverManager.getConnection(url, args[1], args[2]);
             PreparedStatement pstmt = connection.prepareStatement(sqlUpdate)) {

            System.out.println(String.format("Connected to database %s " + "successfully.", connection.getCatalog()));

            pstmt.setString(1, "pusty opis");
            pstmt.setInt(2, 4);
            int rowAffected = pstmt.executeUpdate();
            System.out.println(String.format("Row affected %d", rowAffected));

        } catch (SQLException ex) {
            System.out.println(ex);
        }


    }

}

