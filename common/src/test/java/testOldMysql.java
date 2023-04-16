import java.sql.*;

/**
 * @Author Zemise_
 * @Date 2023/4/8
 * @Description 测试传统mysql数据库连接
 */

public class testOldMysql {
    public static void main(String[] args) throws SQLException {
        // 数据库连接信息
        String url = "jdbc:mysql://hostIP:3306/database";
        String username = "username";
        String password = "password";

        // 加载MySQL JDBC驱动程序
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 获取数据库连接对象
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 创建Statement对象，用于向数据库发送SQL语句
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 执行SQL语句，得到ResultSet对象
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery("SELECT * FROM authme");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 对ResultSet对象进行处理，提取数据
        try {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("username");
                System.out.println("id: " + id + ", username: " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 关闭连接和Statement对象
        try {
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
