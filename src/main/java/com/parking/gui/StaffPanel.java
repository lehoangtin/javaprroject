package com.parking.gui;

import com.parking.bll.StaffBLL;
import com.parking.entity.Staff;
import com.parking.enums.UserRole;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StaffPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtUsername, txtFullName;
    private JPasswordField txtPassword;
    private JComboBox<UserRole> cbRole;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private StaffBLL bll = new StaffBLL();
    private Long selectedId = null;

    public StaffPanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setBackground(Theme.BG_SECONDARY);
        setBorder(Theme.sectionPadding());
        setLayout(new BorderLayout(15, 15));

        JLabel lblTitle = new JLabel("Quản Lý Nhân Viên");
        lblTitle.setFont(Theme.FONT_HEADER);
        add(lblTitle, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Tên đăng nhập", "Họ tên", "Vai trò"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(Theme.FONT_BODY);
        table.setRowHeight(30);
        table.getTableHeader().setFont(Theme.FONT_TITLE);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        
        formPanel.add(new JLabel("Tên đăng nhập:"));
        txtUsername = new JTextField();
        formPanel.add(txtUsername);

        formPanel.add(new JLabel("Mật khẩu:"));
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);

        formPanel.add(new JLabel("Họ và tên:"));
        txtFullName = new JTextField();
        formPanel.add(txtFullName);

        formPanel.add(new JLabel("Vai trò:"));
        cbRole = new JComboBox<>(UserRole.values());
        formPanel.add(cbRole);
        bottomPanel.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAdd = new JButton("Thêm"); btnUpdate = new JButton("Sửa"); 
        btnDelete = new JButton("Xóa"); btnClear = new JButton("Làm mới");
        btnPanel.add(btnAdd); btnPanel.add(btnUpdate); btnPanel.add(btnDelete); btnPanel.add(btnClear);
        bottomPanel.add(btnPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        // Events
        table.getSelectionModel().addListSelectionListener(e -> {
            if (table.getSelectedRow() >= 0) {
                int r = table.getSelectedRow();
                selectedId = (Long) tableModel.getValueAt(r, 0);
                txtUsername.setText(tableModel.getValueAt(r, 1).toString());
                txtUsername.setEnabled(false); // Không cho sửa username
                txtFullName.setText(tableModel.getValueAt(r, 2).toString());
                cbRole.setSelectedItem(UserRole.valueOf(tableModel.getValueAt(r, 3).toString()));
                txtPassword.setText(""); // Reset password field để trống
            }
        });

        btnAdd.addActionListener(e -> {
            if (bll.addStaff(txtUsername.getText(), new String(txtPassword.getPassword()), txtFullName.getText(), (UserRole)cbRole.getSelectedItem())) {
                loadData(); clearForm();
            } else JOptionPane.showMessageDialog(this, "Thêm lỗi!");
        });

        btnUpdate.addActionListener(e -> {
            if (selectedId != null && bll.updateStaff(selectedId, txtFullName.getText(), (UserRole)cbRole.getSelectedItem(), new String(txtPassword.getPassword()))) {
                loadData(); clearForm();
            }
        });
        
        btnDelete.addActionListener(e -> {
            if (selectedId != null && bll.deleteStaff(selectedId)) { loadData(); clearForm(); }
        });
        
        btnClear.addActionListener(e -> clearForm());
    }

    private void loadData() {
        tableModel.setRowCount(0);
        for (Staff s : bll.getAll()) tableModel.addRow(new Object[]{s.getId(), s.getUsername(), s.getFullName(), s.getRole()});
    }

    private void clearForm() {
        selectedId = null; table.clearSelection();
        txtUsername.setText(""); txtUsername.setEnabled(true);
        txtPassword.setText(""); txtFullName.setText(""); cbRole.setSelectedIndex(0);
    }
}