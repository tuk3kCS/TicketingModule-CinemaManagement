package dao;

import model.FoodItem;
import java.sql.*;
import java.util.*;

public class FoodItemDAO extends DAO {
    public FoodItemDAO() { super(); }

    public List<FoodItem> searchFoodItemByKeyword(String name) {
        List<FoodItem> list = new ArrayList<>();
        String sql = "SELECT * FROM tblFoodItem WHERE name LIKE ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new FoodItem(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("size"),
                    rs.getInt("unitPrice")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public FoodItem getFoodItemById(FoodItem foodItem) {
        String sql = "SELECT * FROM tblFoodItem WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, foodItem.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new FoodItem(
                    rs.getInt("id"), 
                    rs.getString("name"), 
                    rs.getString("size"), 
                    rs.getInt("unitPrice")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
} 