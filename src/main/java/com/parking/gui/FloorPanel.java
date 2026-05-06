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

        // --- Tiêu đề trang ---
        JLabel lblTitle = new JLabel("Quản Lý Tầng (Floors)");
        lblTitle.setFont(Theme.FONT_HEADER);
        lblTitle.setForeground(Theme.TEXT_PRIMARY);
        add(lblTitle, BorderLayout.NORTH);

        // --- Bảng hiển thị dữ liệu ---
        String[] columns = {"ID", "Số Tầng", "Mô Tả"};
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

        // --- Panel Form nhập liệu và Nút chức năng ---
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(Theme.BG_PRIMARY);
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
                Theme.cardBorder(), 
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Form nhập liệu
        JPanel formPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        formPanel.setBackground(Theme.BG_PRIMARY);
        
        formPanel.add(createLabel("Số tầng (VD: 1, -1):"));
        txtFloorNumber = createTextField();
        formPanel.add(txtFloorNumber);

        formPanel.add(createLabel("Mô tả (Tùy chọn):"));
        txtDescription = createTextField();
        formPanel.add(txtDescription);

        bottomPanel.add(formPanel, BorderLayout.CENTER);

        // Panel chứa các nút
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Theme.BG_PRIMARY);

        btnAdd = createButton("Thêm", Theme.ACCENT_TEAL);
        btnUpdate = createButton("Sửa", Theme.ACCENT_BLUE);
        btnDelete = createButton("Xóa", Theme.ACCENT_RED);
        btnClear = createButton("Làm mới", Theme.TEXT_MUTED);

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);
        
        bottomPanel.add(btnPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- Sự kiện chọn dòng trên bảng ---
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int selectedRow = table.getSelectedRow();
                selectedFloorId = (Long) tableModel.getValueAt(selectedRow, 0);
                txtFloorNumber.setText(tableModel.getValueAt(selectedRow, 1).toString());
                
                Object descObj = tableModel.getValueAt(selectedRow, 2);
                txtDescription.setText(descObj != null ? descObj.toString() : "");
            }
        });

        // --- Gắn sự kiện cho các nút ---
        btnAdd.addActionListener(e -> addAction());
        btnUpdate.addActionListener(e -> updateAction());
        btnDelete.addActionListener(e -> deleteAction());
        btnClear.addActionListener(e -> clearForm());
    }

    // Các hàm tạo UI đồng bộ Theme
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

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.FONT_TITLE);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 35));
        return btn;
    }

    // Logic xử lý
    private void loadData() {
        tableModel.setRowCount(0);
        List<Floor> list = bll.getAllFloors();
        for (Floor floor : list) {
            Object[] row = {
                floor.getId(),
                floor.getFloorNumber(),
                floor.getDescription()
            };
            tableModel.addRow(row);
        }
    }

    private void clearForm() {
        selectedFloorId = null;
        table.clearSelection();
        txtFloorNumber.setText("");
        txtDescription.setText("");
    }

    private void addAction() {
        try {
            Integer floorNumber = Integer.parseInt(txtFloorNumber.getText().trim());
            String desc = txtDescription.getText().trim();

            if (bll.addFloor(floorNumber, desc)) {
                JOptionPane.showMessageDialog(this, "Thêm tầng thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số tầng phải là số nguyên (VD: 1, 2, -1)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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

            if (bll.updateFloor(selectedFloorId, floorNumber, desc)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số tầng phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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