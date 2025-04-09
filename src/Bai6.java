import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Bai6 {
    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement departmentStmt = null;
        PreparedStatement employeeStmt = null;

        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);

            String insertDepartmentSql = "INSERT INTO departments (name) VALUES (?)";
            departmentStmt = conn.prepareStatement(insertDepartmentSql, PreparedStatement.RETURN_GENERATED_KEYS);
            departmentStmt.setString(1, "Phòng Kinh Doanh");
            departmentStmt.executeUpdate();

            int departmentId = -1;
            var generatedKeys = departmentStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                departmentId = generatedKeys.getInt(1);
            }

            String insertEmployeeSql = "INSERT INTO employees (name, department_id) VALUES (?, ?)";
            employeeStmt = conn.prepareStatement(insertEmployeeSql);

            String[] employeeNames = {"Nguyen Van A", "Tran Thi B", "Le Van C"};
            for (String name : employeeNames) {
                employeeStmt.setString(1, name);
                employeeStmt.setInt(2, departmentId);
                employeeStmt.executeUpdate();
            }

            conn.commit();
            System.out.println("Thêm phòng ban và nhân viên thành công.");
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Đã rollback giao dịch do lỗi.");
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (employeeStmt != null) {
                try {
                    employeeStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (departmentStmt != null) {
                try {
                    departmentStmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}