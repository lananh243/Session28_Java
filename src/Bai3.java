import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class Bai3 {
    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);

            deposit(conn, 1, 1000);

            withdraw(conn, 1, 400);

            conn.commit();
            System.out.println("Giao dịch thành công");
        } catch (SQLException e){
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Đã rollback thay đổi do lỗi.");
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void deposit(Connection conn, int accountId, double amount) {
        try (CallableStatement callSt = conn.prepareCall("{call deposit(?, ?)}")) {
            callSt.setInt(1, accountId);
            callSt.setDouble(2, amount);
            callSt.execute();
            System.out.println("Nạp tiền thành công vào tài khoản ID " + accountId);
        } catch (SQLException e) {
            System.err.println("Lỗi khi nạp tiền: " + e.getMessage());
        }
    }


    private static void withdraw(Connection conn, int accountId, double amount) {
        try (CallableStatement callSt = conn.prepareCall("{call withdraw(?, ?)}")) {
            callSt.setInt(1, accountId);
            callSt.setDouble(2, amount);
            callSt.execute();
            System.out.println("Rút tiền thành công từ tài khoản ID " + accountId);
        } catch (SQLException e) {
            System.err.println("Lỗi khi rút tiền: " + e.getMessage());
        }
    }
}
