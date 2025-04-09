import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/TransactionJDBC";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "12345678";

    public static Connection openConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            System.out.println("Kết nối CSDL thành công");

            boolean autoCommit = conn.getAutoCommit();
            System.out.println("Trạng thái auto-commit ban đầu: " + autoCommit);
        } catch (SQLException e) {
            System.err.println("Có lỗi trong quá trình kết nối với CSDL: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Có lỗi không xác định trong quá trình kết nối: " + e.getMessage());
        }
        return conn;
    }

    public static void closeConnection(Connection conn, CallableStatement callSt) {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            callSt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
