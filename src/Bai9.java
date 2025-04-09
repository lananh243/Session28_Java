import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class Bai9 {
    public static void main(String[] args) {
        placeBid(1, 1, 150);
        placeBid(1,1, 50);
        placeBid(1,1, 200);
    }

    public static void placeBid(int auction_id, int user_id, double bid_amount) {
        Connection conn = null;
        CallableStatement callStmt = null;
        try {
            conn = ConnectionDB.openConnection();
            String sql = ("{call placebid(?, ?, ?)}");
            callStmt = conn.prepareCall(sql);
            callStmt.setInt(1, auction_id);
            callStmt.setInt(2, user_id);
            callStmt.setDouble(3, bid_amount);
            callStmt.executeUpdate();
            System.out.println("Bid placed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
