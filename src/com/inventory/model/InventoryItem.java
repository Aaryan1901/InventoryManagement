package com.inventory.model;

import java.util.Date;

public class InventoryItem {
    private int itemId;
    private String itemName;
    private String category;
    private int quantity;
    private double price;
    private Date stockDate;
    private String department;

    // Getters and Setters
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Date getStockDate() { return stockDate; }
    public void setStockDate(Date stockDate) { this.stockDate = stockDate; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}