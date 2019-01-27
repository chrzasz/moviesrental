import java.sql.*;
import java.util.TimeZone;

public class Main {
    public static void main(String[] args) {

        String url = args[0] + "?serverTimezone=" +
                TimeZone.getDefault().getID();

        String query = "SELECT title, releaseDate FROM moviesinfo";

        try(Connection connection = DriverManager.getConnection(url, args[1], args[2]);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query))
        {
            System.out.println("DB connection OK.");
            System.out.println("This is a list of movies in our rental offer:");
            System.out.println("Title\t|\tReleased");
            while(resultSet.next()) {
                System.out.println(
                        resultSet.getString("title") + "\t" + "|"
                        + "\t" + resultSet.getDate("releaseDate")
                );
            }
        }
        catch(SQLException ex) {
            System.out.println(ex);
        }


    }

}
