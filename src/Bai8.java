import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class Bai8 {
    public static void main(String[] args) {
        bookRoom(1, 3, "2025-04-08");
    }

    public static void bookRoom(int customerId, int roomId, String bookingDate) {
        Connection conn = null;
        CallableStatement callSt = null;

        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);

            String sql = "{call book_room(?, ?, ?)}";
            callSt = conn.prepareCall(sql);
            callSt.setInt(1, customerId);
            callSt.setInt(2, roomId);
            callSt.setDate(3, java.sql.Date.valueOf(bookingDate));

            callSt.executeUpdate();
            conn.commit();
            System.out.println("Đặt phòng thành công.");
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Đã rollback giao dịch do lỗi");
            e.printStackTrace();
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }
}
