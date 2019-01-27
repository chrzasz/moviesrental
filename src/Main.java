import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.TimeZone;

public class Main {
    public static void main(String[] args) {

/*Korzystając z PreparedStatement, napisz funkcję obsługującą wypożyczenie filmu
przez klienta. */
        String url = args[0] + "?serverTimezone=" + TimeZone.getDefault().getID();

        final int customerId = 1;
        final int copyId = 1;

        try (Connection connection = DriverManager.getConnection(url, args[1], args[2])) {

            rentCopyToCustomer(customerId,copyId,connection);
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
                insertIntoRents(copyId, customerId, RentStatus.IN_RENT,
                        getRentPricePerDay(getReleaseDate(copyId)), java.sql.Date.valueOf(LocalDate.now()));
            }
        }
    }

    private static boolean insertIntoRents(final int copyId, final int customer, final RentStatus status,
                                           final double rentPricePerDay, final java.sql.Date rentedDate, final Connection connection) {

        String sql = "INSERT INTO rents(" +
                "rentedMovieId,customer,status,rentPricePerDay,rentedDate,returnedDate)\n" +
                "VALUES (?,?,?,?,?,?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, copyId);
        } catch (SQLException e) {
            e.printStackTrace();
        }



    return true;
    }

    //java.sql.Date -> LocalDate
    private static LocalDate getReleaseDate(final int copyId, final Connection connection) {

        LocalDate releaseDate = LocalDate.now();

        String parametrizedQuery = "SELECT releaseDate FROM moviesinfo WHERE movieInfoId = (SELECT movieInfoId FROM " +
                "moviescopies WHERE copyId = ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(parametrizedQuery)) {
            preparedStatement.setInt(1, copyId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    releaseDate = resultSet.getDate("releasedDate").toLocalDate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static double getRentPricePerDay(final LocalDate releaseDate) {

        int releasedDaysAgo = (int) Duration.between(LocalDate.now(), releaseDate).toDays();
        if (releasedDaysAgo < 14) {
            return 10.0;
        } else if (releasedDaysAgo >= 14 && releasedDaysAgo < 180) {
            return 5.0;
        } else {
            return 2.5;
        }
    }

    private static boolean updateMoviesCopies(final int copyId, final boolean isRented, final int rentedTimes,
                                              final int rentedTo, final Connection connection) {
        String parametrizedQuery = "UPDATE moviescopies SET isRented = ?, rentedTimes = ?, rentedTo = ? WHERE copyId = ?";
        boolean wasExecuted = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement(parametrizedQuery)) {
            preparedStatement.setBoolean(1, isRented);
            preparedStatement.setInt(2, rentedTimes);
            preparedStatement.setInt(3, rentedTo);
            preparedStatement.setInt(4, copyId);
            wasExecuted = (preparedStatement.executeUpdate() == 1);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return wasExecuted;
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