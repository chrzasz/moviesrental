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
        final int customerId = 1;
        final int copyId = 20;
        try (Connection connection = DriverManager.getConnection(url, args[1], args[2])) {
            //rentCopyToCustomer(copyId, customerId, connection);
            getRentedTimes(1, connection);
            System.out.println(String.format("Connected to database %s " + "successfully.", connection.getCatalog()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void rentCopyToCustomer(int copyId, int customerId, Connection connection) {
        int copyRentedTimes = getRentedTimes(copyId, connection);
        if (copyRentedTimes >= 0) {  //normalnie to robimy wyjatkami
            boolean isCopyUpdated = updateMoviesCopies(copyId, true, copyRentedTimes + 1, customerId, connection);
            if (isCopyUpdated) {
            }
        }
    }

    private static boolean updateMoviesCopies(final int copyId, final boolean isRented, final int rentedTimes,
                                              final int rentedTo, final Connection connection) {
        return true;
    }

    private static int getRentedTimes(final int copyId, final Connection connection) {
        String parametrizedQuery = "SELECT rentedTimes FROM moviescopies WHERE copyId=?";
        int rentedTimes = -1;
        try (PreparedStatement preparedStatement = connection.prepareStatement(parametrizedQuery)) {
            preparedStatement.setInt(1, copyId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("rentedTimes"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentedTimes;
    }
}