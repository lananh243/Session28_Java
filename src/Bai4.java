import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class Bai4 {
    public static void main(String[] args) {
        Connection conn = null;
        CallableStatement callSt = null;

        try {
            conn = ConnectionDB.openConnection();
            if (conn != null) {
                conn.setAutoCommit(false);

                String sql = "{call transfer(?, ?, ?)}";
                callSt = conn.prepareCall(sql);
                callSt.setInt(1, 1);
                callSt.setInt(2, 2);
                callSt.setDouble(3, 1000);

                callSt.executeUpdate();
                conn.commit();
                System.out.println("Chuyển khoản thành công.");
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Đã rollback thay đổi do lỗi.");
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
            ConnectionDB.closeConnection(conn, callSt);
        }
    }
}
