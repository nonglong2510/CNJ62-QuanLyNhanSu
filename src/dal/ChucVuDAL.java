package dal;

import model.ChucVu;
import utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChucVuDAL {
    
    public List<ChucVu> getAll() {
        List<ChucVu> list = new ArrayList<>();
        String sql = "SELECT * FROM ChucVu";
        
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                ChucVu cv = new ChucVu();
                cv.setMaCV(rs.getString("MaCV"));
                cv.setTenCV(rs.getString("TenCV"));
                cv.setPhuCapChucVu(rs.getDouble("PhuCapChucVu"));
                list.add(cv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
