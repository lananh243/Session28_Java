import java.sql.*;

public class Bai7 {
    public static void main(String[] args) {
        String[] isolationLevels = {
                "READ_UNCOMMITTED",
                "READ_COMMITTED",
                "REPEATABLE_READ",
                "SERIALIZABLE"
        };

        for (String isolationLevel : isolationLevels) {
            System.out.println("Thử nghiệm với mức độ cô lập: " + isolationLevel);
            testIsolationLevel(isolationLevel);
        }
    }

    private static void testIsolationLevel(String isolationLevel) {
        Connection connection1 = null;
        Connection connection2 = null;

        try {
            connection1 = ConnectionDB.openConnection();
            connection2 = ConnectionDB.openConnection();

            connection1.setTransactionIsolation(getIsolationLevel(isolationLevel));
            connection2.setTransactionIsolation(getIsolationLevel(isolationLevel));

            connection2.setAutoCommit(false);
            String insertSql = "insert into OrdersB7 (customer_name, status) values (?, ?)";
            try (PreparedStatement insertStmt = connection2.prepareStatement(insertSql)) {
                insertStmt.setString(1, "Nguyễn Văn O");
                insertStmt.setString(2, "Đang chờ xử lý");
                insertStmt.executeUpdate();
            }

            String selectSql = "SELECT * FROM OrdersB7";
            try (PreparedStatement selectStmt = connection1.prepareStatement(selectSql);
                 ResultSet rs = selectStmt.executeQuery()) {

                System.out.println("Dữ liệu đọc từ connection1:");
                while (rs.next()) {
                    System.out.println("Mã đơn hàng: " + rs.getInt("order_id") + ", Tên khách hàng: " + rs.getString("customer_name") + ", Trạng thái: " + rs.getString("status"));
                }
            }

            connection2.rollback();
            System.out.println("Giao dịch trên connection2 đã rollback.\n");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection1 != null) connection1.close();
                if (connection2 != null) connection2.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static int getIsolationLevel(String level) {
        switch (level) {
            case "READ_UNCOMMITTED":
                return Connection.TRANSACTION_READ_UNCOMMITTED;
            case "READ_COMMITTED":
                return Connection.TRANSACTION_READ_COMMITTED;
            case "REPEATABLE_READ":
                return Connection.TRANSACTION_REPEATABLE_READ;
            case "SERIALIZABLE":
                return Connection.TRANSACTION_SERIALIZABLE;
            default:
                throw new IllegalArgumentException("Mức độ cô lập không hợp lệ: " + level);
        }
    }
}
