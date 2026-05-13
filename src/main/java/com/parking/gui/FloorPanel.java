package com.parking.gui;

import com.parking.bll.FloorBLL;
import com.parking.entity.Floor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class FloorPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtFloorNumber;
    private JTextField txtDescription;
    private JTextField txtCapacity;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private FloorBLL bll;
    private Long selectedFloorId = null;

    public FloorPanel() {
        bll = new FloorBLL();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setBackground(Theme.BG_SECONDARY);
        setBorder(Theme.sectionPadding());
        setLayout(new BorderLayout(15, 15));

        JLabel lblTitle = new JLabel("Quản Lý Tầng (Floors)");
        lblTitle.setFont(Theme.FONT_HEADER);
        lblTitle.setForeground(Theme.TEXT_PRIMARY);
        add(lblTitle, BorderLayout.NORTH);

        String[] columns = {"ID", "Số Tầng", "Mô Tả", "Sức chứa"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(Theme.FONT_BODY);
        table.setRowHeight(30);
        table.setSelectionBackground(Theme.SLOT_EMPTY);
        table.setSelectionForeground(Theme.TEXT_PRIMARY);

        JTableHeader header = table.getTableHeader();
        header.setFont(Theme.FONT_TITLE);
        header.setBackground(Theme.BG_PRIMARY);
        header.setForeground(Theme.TEXT_PRIMARY);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(Theme.cardBorder());
        scrollPane.getViewport().setBackground(Theme.BG_PRIMARY);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(Theme.BG_PRIMARY);
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
                Theme.cardBorder(), 
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JPanel formPanel = new JPanel(new GridLayout(2, 4, 15, 15));
        formPanel.setBackground(Theme.BG_PRIMARY);
        
        formPanel.add(createLabel("Số tầng (VD: 1, -1):"));
        txtFloorNumber = createTextField();
        formPanel.add(txtFloorNumber);

        formPanel.add(createLabel("Mô tả (Tùy chọn):"));
        txtDescription = createTextField();
        formPanel.add(txtDescription);

        formPanel.add(createLabel("Sức chứa (Capacity):"));
        txtCapacity = createTextField();
        formPanel.add(txtCapacity);

        formPanel.add(new JLabel(""));
        formPanel.add(new JLabel(""));

        bottomPanel.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Theme.BG_PRIMARY);

        btnAdd = new JButton("Thêm");
        btnAdd.setFont(Theme.FONT_TITLE);
        btnAdd.setBackground(Theme.ACCENT_TEAL);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false); 
        btnAdd.setOpaque(true);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setPreferredSize(new Dimension(100, 35));

        btnUpdate = new JButton("Sửa");
        btnUpdate.setFont(Theme.FONT_TITLE);
        btnUpdate.setBackground(Theme.ACCENT_BLUE);
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFocusPainted(false);
        btnUpdate.setBorderPainted(false);
        btnUpdate.setOpaque(true);
        btnUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUpdate.setPreferredSize(new Dimension(100, 35));

        btnDelete = new JButton("Xóa");
        btnDelete.setFont(Theme.FONT_TITLE);
        btnDelete.setBackground(Theme.ACCENT_RED);
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.setBorderPainted(false);
        btnDelete.setOpaque(true);
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.setPreferredSize(new Dimension(100, 35));

        btnClear = new JButton("Làm mới");
        btnClear.setFont(Theme.FONT_TITLE);
        btnClear.setBackground(Theme.TEXT_MUTED);
        btnClear.setForeground(Color.WHITE);
        btnClear.setFocusPainted(false);
        btnClear.setBorderPainted(false);
        btnClear.setOpaque(true);
        btnClear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClear.setPreferredSize(new Dimension(135, 35));

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);
        
        bottomPanel.add(btnPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int selectedRow = table.getSelectedRow();
                selectedFloorId = (Long) tableModel.getValueAt(selectedRow, 0);
                txtFloorNumber.setText(tableModel.getValueAt(selectedRow, 1).toString());
                
                Object descObj = tableModel.getValueAt(selectedRow, 2);
                txtDescription.setText(descObj != null ? descObj.toString() : "");

                Object capObj = tableModel.getValueAt(selectedRow, 3);
                txtCapacity.setText(capObj != null ? capObj.toString() : "");
            }
        });

        btnAdd.addActionListener(e -> addAction());
        btnUpdate.addActionListener(e -> updateAction());
        btnDelete.addActionListener(e -> deleteAction());
        btnClear.addActionListener(e -> clearForm());
    }
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.FONT_TITLE);
        label.setForeground(Theme.TEXT_PRIMARY);
        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(Theme.FONT_BODY);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return textField;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Floor> list = bll.getAllFloors();
        com.parking.bll.SlotBLL slotBLL = new com.parking.bll.SlotBLL();

        for (Floor floor : list) {
            int currentSlots = slotBLL.getSlotsByFloor(floor.getId()).size();
            Object[] row = {
                floor.getId(),
                floor.getFloorNumber(),
                floor.getDescription(),
                floor.getCapacity()
           };
            tableModel.addRow(row);
        }
    }

    private void clearForm() {
        selectedFloorId = null;
        table.clearSelection();
        txtFloorNumber.setText("");
        txtDescription.setText("");
        txtCapacity.setText("");
    }

    private void addAction() {
        try {
            Integer floorNumber = Integer.parseInt(txtFloorNumber.getText().trim());
            String desc = txtDescription.getText().trim();
            Integer capacity = txtCapacity.getText().trim().isEmpty() ? null : Integer.parseInt(txtCapacity.getText().trim());
            if (bll.addFloor(floorNumber, desc, capacity)) {
                JOptionPane.showMessageDialog(this, "Thêm tầng thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số tầng và Sức chứa phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateAction() {
        if (selectedFloorId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một tầng để sửa!");
            return;
        }
        try {
            Integer floorNumber = Integer.parseInt(txtFloorNumber.getText().trim());
            String desc = txtDescription.getText().trim();
            Integer capacity = txtCapacity.getText().trim().isEmpty() ? null : Integer.parseInt(txtCapacity.getText().trim());

            if (bll.updateFloor(selectedFloorId, floorNumber, desc, capacity)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số tầng và Sức chứa phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAction() {
        if (selectedFloorId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một tầng để xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa tầng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (bll.deleteFloor(selectedFloorId)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại (có thể tầng đang chứa bãi xe)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}