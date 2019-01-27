import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.TimeZone;

public class Main {
    public static void main(String[] args) {

        String url = args[0] + "?serverTimezone=" +
                TimeZone.getDefault().getID();

        try(Connection connection = DriverManager.getConnection(url, args[1], args[2])) {
            System.out.println("DB connection OK.");
        }
        catch(SQLException ex) {
            System.out.println(ex);
        }


    }

}
