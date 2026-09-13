package dal;

import model.PhongBan;
import utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhongBanDAL {
    
    public List<PhongBan> getAll() {
        List<PhongBan> list = new ArrayList<>();
        String sql = "SELECT * FROM PhongBan";
        
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                PhongBan pb = new PhongBan();
                pb.setMaPB(rs.getString("MaPB"));
                pb.setTenPB(rs.getString("TenPB"));
                pb.setSoDienThoai(rs.getString("SoDienThoai"));
                list.add(pb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
