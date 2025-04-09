import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class Bai2 {
    public static void main(String[] args) {
        Connection conn = null;
        CallableStatement callSt = null;

        try {
            conn = ConnectionDB.openConnection();
            if (conn != null) {
                conn.setAutoCommit(false);

                String sql = "{call crease_user(?, ?)}";
                callSt = conn.prepareCall(sql);
                callSt.setString(1, "Nguyen Van A");
                callSt.setString(2, "nva@gmail.com");

                callSt.executeUpdate();
                System.out.println("Người dùng đầu tiên đã được thêm thành công.");

                String sqlInsert2 = "{call crease_duplicate_user(?, ?, ?)}";
                callSt = conn.prepareCall(sqlInsert2);
                callSt.setInt(1, 1);
                callSt.setString(2, "Tran Thi B");
                callSt.setString(3, "ttb@gmail.com");

                callSt.executeUpdate();
                System.out.println("Người dùng thứ hai đã được thêm thành công.");

                conn.commit();
                System.out.println("Thay đổi đã được lưu.");
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Đã rollback thay đổi do lỗi");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }
}