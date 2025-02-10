package com.inventory.dao;

import com.inventory.model.InventoryItem;
import com.inventory.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    public void addItem(InventoryItem item) throws SQLException {
        String sql = "INSERT INTO Inv_Control (ItemID, ItemName, Category, Quantity, Price, StockDate, Department) VALUES (?, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, item.getItemId());
            pstmt.setString(2, item.getItemName());
            pstmt.setString(3, item.getCategory());
            pstmt.setInt(4, item.getQuantity());
            pstmt.setDouble(5, item.getPrice());
            pstmt.setString(6, new java.sql.Date(item.getStockDate().getTime()).toString());
            pstmt.setString(7, item.getDepartment());
            pstmt.executeUpdate();
        }
    }

    public void updateItem(InventoryItem item) throws SQLException {
        String sql = "UPDATE Inv_Control SET ItemName=?, Category=?, Quantity=?, Price=?, StockDate=TO_DATE(?, 'YYYY-MM-DD'), Department=? WHERE ItemID=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getItemName());
            pstmt.setString(2, item.getCategory());
            pstmt.setInt(3, item.getQuantity());
            pstmt.setDouble(4, item.getPrice());
            pstmt.setString(5, new java.sql.Date(item.getStockDate().getTime()).toString());
            pstmt.setString(6, item.getDepartment());
            pstmt.setInt(7, item.getItemId());
            pstmt.executeUpdate();
        }
    }

    public void deleteItem(int itemId) throws SQLException {
        String sql = "DELETE FROM Inv_Control WHERE ItemID=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            pstmt.executeUpdate();
        }
    }

    public List<InventoryItem> getAllItems() throws SQLException {
        List<InventoryItem> items = new ArrayList<>();
        String sql = "SELECT ItemID, ItemName, Category, Quantity, Price, StockDate, Department FROM Inv_Control";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                InventoryItem item = new InventoryItem();
                item.setItemId(rs.getInt("ItemID"));
                item.setItemName(rs.getString("ItemName"));
                item.setCategory(rs.getString("Category"));
                item.setQuantity(rs.getInt("Quantity"));
                item.setPrice(rs.getDouble("Price"));
                item.setStockDate(rs.getDate("StockDate"));
                item.setDepartment(rs.getString("Department"));
                items.add(item);
            }
        }
        return items;
    }
}