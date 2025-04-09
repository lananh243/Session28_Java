import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Bai5 {
    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement orderStmt = null;
        PreparedStatement detailStmt = null;

        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);


            String insertOrderSql = "INSERT INTO Orders (customer_name, order_date) VALUES (?, ?)";
            orderStmt = conn.prepareStatement(insertOrderSql, PreparedStatement.RETURN_GENERATED_KEYS);
            orderStmt.setString(1, "Nguyen Van A");
            orderStmt.setDate(2, java.sql.Date.valueOf("2025-04-08"));
            orderStmt.executeUpdate();

            int orderId = -1;
            var generatedKeys = orderStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            }

            String insertDetailSql = "INSERT INTO OrderDetails (order_id, product_name, quantity) VALUES (?, ?, ?)";
            detailStmt = conn.prepareStatement(insertDetailSql);

            String[][] orderDetails = {
                    {"Sản phẩm A", "2"},
                    {"Sản phẩm B", "3"},
                    {"Sản phẩm C", "-1"}
            };

            for (String[] detail : orderDetails) {
                String productName = detail[0];
                int quantity = Integer.parseInt(detail[1]);

                if (quantity < 0) {
                    throw new IllegalArgumentException("Số lượng không thể âm.");
                }

                detailStmt.setInt(1, orderId);
                detailStmt.setString(2, productName);
                detailStmt.setInt(3, quantity);
                detailStmt.executeUpdate();
            }

            conn.commit();
            System.out.println("Thêm đơn hàng và chi tiết thành công.");
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Đã rollback giao dịch do lỗi");
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (detailStmt != null) {
                try {
                    detailStmt.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if (orderStmt != null) {
                try {
                    orderStmt.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
