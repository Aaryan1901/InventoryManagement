package com.inventory.ui;

import com.inventory.dao.InventoryDAO;
import com.inventory.model.InventoryItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryManagementUI extends JFrame {
    private JTextField itemIdField, itemNameField, categoryField, quantityField, priceField, stockDateField, departmentField;
    private JTable table;
    private DefaultTableModel tableModel;
    private InventoryDAO inventoryDAO;
    private List<InventoryItem> buffer = new ArrayList<>();
    private int currentIndex = -1;

    public InventoryManagementUI() {
        inventoryDAO = new InventoryDAO();
        initializeUI();
        loadTableData();
    }

    private void initializeUI() {
        setTitle("Inventory Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(7, 2));
        inputPanel.add(new JLabel("Item ID:"));
        itemIdField = new JTextField();
        inputPanel.add(itemIdField);

        inputPanel.add(new JLabel("Item Name:"));
        itemNameField = new JTextField();
        inputPanel.add(itemNameField);

        inputPanel.add(new JLabel("Category:"));
        categoryField = new JTextField();
        inputPanel.add(categoryField);

        inputPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        inputPanel.add(quantityField);

        inputPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        inputPanel.add(priceField);

        inputPanel.add(new JLabel("Stock Date (YYYY-MM-DD):"));
        stockDateField = new JTextField();
        inputPanel.add(stockDateField);

        inputPanel.add(new JLabel("Department:"));
        departmentField = new JTextField();
        inputPanel.add(departmentField);

        add(inputPanel, BorderLayout.NORTH);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add to Buffer");
        addButton.addActionListener(e -> insertToBuffer());
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Update in Buffer");
        updateButton.addActionListener(e -> updateInBuffer());
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete from Buffer");
        deleteButton.addActionListener(e -> deleteFromBuffer());
        buttonPanel.add(deleteButton);

        JButton commitButton = new JButton("Commit to Database");
        commitButton.addActionListener(e -> commitToDatabase());
        buttonPanel.add(commitButton);

        JButton previousButton = new JButton("Previous");
        previousButton.addActionListener(e -> showPrevious());
        buttonPanel.add(previousButton);

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> showNext());
        buttonPanel.add(nextButton);

        JButton clearButton = new JButton("Clear Form");
        clearButton.addActionListener(e -> clearForm());
        buttonPanel.add(clearButton);

        add(buttonPanel, BorderLayout.CENTER);

        // Table
        tableModel = new DefaultTableModel(new String[]{"Item ID", "Item Name", "Category", "Quantity", "Price", "Stock Date", "Department"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.SOUTH);

        // Load initial data into the buffer
        loadDataIntoBuffer();
    }

    private void loadDataIntoBuffer() {
        try {
            buffer = inventoryDAO.getAllItems();
            if (!buffer.isEmpty()) {
                currentIndex = 0;
                loadFormWithData(buffer.get(currentIndex));
            }
            displayBufferData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void insertToBuffer() {
        try {
            InventoryItem item = new InventoryItem();
            item.setItemId(Integer.parseInt(itemIdField.getText()));
            item.setItemName(itemNameField.getText());
            item.setCategory(categoryField.getText());
            item.setQuantity(Integer.parseInt(quantityField.getText()));
            item.setPrice(Double.parseDouble(priceField.getText()));
            item.setStockDate(java.sql.Date.valueOf(stockDateField.getText()));
            item.setDepartment(departmentField.getText());

            buffer.add(item);
            currentIndex = buffer.size() - 1;
            displayBufferData();
            JOptionPane.showMessageDialog(this, "Item added to buffer!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateInBuffer() {
        if (currentIndex >= 0 && currentIndex < buffer.size()) {
            InventoryItem item = buffer.get(currentIndex);
            item.setItemName(itemNameField.getText());
            item.setCategory(categoryField.getText());
            item.setQuantity(Integer.parseInt(quantityField.getText()));
            item.setPrice(Double.parseDouble(priceField.getText()));
            item.setStockDate(java.sql.Date.valueOf(stockDateField.getText()));
            item.setDepartment(departmentField.getText());

            displayBufferData();
            JOptionPane.showMessageDialog(this, "Item updated in buffer!");
        } else {
            JOptionPane.showMessageDialog(this, "No item selected in buffer!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteFromBuffer() {
        if (currentIndex >= 0 && currentIndex < buffer.size()) {
            buffer.remove(currentIndex);
            if (buffer.size() > 0) {
                currentIndex = Math.min(currentIndex, buffer.size() - 1);
            } else {
                currentIndex = -1;
            }
            displayBufferData();
            JOptionPane.showMessageDialog(this, "Item deleted from buffer!");
        } else {
            JOptionPane.showMessageDialog(this, "No item selected in buffer!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void commitToDatabase() {
        if (currentIndex >= 0 && currentIndex < buffer.size()) {
            try {
                inventoryDAO.addItem(buffer.get(currentIndex));
                JOptionPane.showMessageDialog(this, "Item committed to database!");
                loadTableData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No item selected in buffer!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showPrevious() {
        if (currentIndex > 0) {
            currentIndex--;
            loadFormWithData(buffer.get(currentIndex));
        } else {
            JOptionPane.showMessageDialog(this, "No previous item!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showNext() {
        if (currentIndex < buffer.size() - 1) {
            currentIndex++;
            loadFormWithData(buffer.get(currentIndex));
        } else {
            JOptionPane.showMessageDialog(this, "No next item!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        itemIdField.setText("");
        itemNameField.setText("");
        categoryField.setText("");
        quantityField.setText("");
        priceField.setText("");
        stockDateField.setText("");
        departmentField.setText("");
    }

    private void loadFormWithData(InventoryItem item) {
        itemIdField.setText(String.valueOf(item.getItemId()));
        itemNameField.setText(item.getItemName());
        categoryField.setText(item.getCategory());
        quantityField.setText(String.valueOf(item.getQuantity()));
        priceField.setText(String.valueOf(item.getPrice()));
        stockDateField.setText(new java.sql.Date(item.getStockDate().getTime()).toString());
        departmentField.setText(item.getDepartment());
    }

    private void displayBufferData() {
        tableModel.setRowCount(0); // Clear existing data
        for (InventoryItem item : buffer) {
            tableModel.addRow(new Object[]{
                    item.getItemId(),
                    item.getItemName(),
                    item.getCategory(),
                    item.getQuantity(),
                    item.getPrice(),
                    item.getStockDate(),
                    item.getDepartment()
            });
        }
    }

    private void loadTableData() {
        try {
            List<InventoryItem> items = inventoryDAO.getAllItems();
            tableModel.setRowCount(0); // Clear existing data
            for (InventoryItem item : items) {
                tableModel.addRow(new Object[]{
                        item.getItemId(),
                        item.getItemName(),
                        item.getCategory(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getStockDate(),
                        item.getDepartment()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InventoryManagementUI().setVisible(true);
        });
    }
}