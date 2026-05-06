package com.parking.gui;

import com.parking.bll.StaffBLL;
import com.parking.entity.Staff;
import com.parking.enums.UserRole;
import com.parking.utils.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StaffPanel extends JPanel {
    private JTextField txtId, txtUsername, txtFullName;
    private JPasswordField txtPassword;
    private JComboBox<UserRole> cbRole;
    private JTable tableStaff;
    private DefaultTableModel tableModel;
    
    private JButton btnAdd, btnEdit, btnDelete, btnClear;
    private StaffBLL bll;

    public StaffPanel() {
        bll = new StaffBLL();
        initComponents();
        loadDataToTable();
        applyPermissions();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.BG_SECONDARY);
        setBorder(Theme.sectionPadding()); // Đồng bộ padding

        // --- Tiêu đề trang ---
        JLabel lblTitle = new JLabel("Quản Lý Nhân Sự (Staff)");
        lblTitle.setFont(Theme.FONT_HEADER);
        lblTitle.setForeground(Theme.TEXT_PRIMARY);
        add(lblTitle, BorderLayout.NORTH);

        // --- Panel Khung chính (Chứa Form và Bảng) ---
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);

        // --- 1. FORM NHẬP LIỆU ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Theme.BG_PRIMARY);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                Theme.cardBorder(), 
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Dòng 1
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2; 
        formPanel.add(createLabel("Mã NV:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8; 
        txtId = createTextField(); txtId.setEnabled(false); 
        formPanel.add(txtId, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.2; 
        formPanel.add(createLabel("Họ và tên:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.8; 
        txtFullName = createTextField(); 
        formPanel.add(txtFullName, gbc);

        // Dòng 2
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.2; 
        formPanel.add(createLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8; 
        txtUsername = createTextField(); 
        formPanel.add(txtUsername, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.2; 
        formPanel.add(createLabel("Vai trò:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.8; 
        cbRole = new JComboBox<>(UserRole.values()); 
        cbRole.setFont(Theme.FONT_BODY);
        formPanel.add(cbRole, gbc);

        // Dòng 3
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.2; 
        formPanel.add(createLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 0.8; 
        txtPassword = new JPasswordField(); 
        txtPassword.setFont(Theme.FONT_BODY);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        txtPassword.setToolTipText("Bỏ trống nếu không muốn đổi mật khẩu khi Cập nhật");
        formPanel.add(txtPassword, gbc);

        centerPanel.add(formPanel, BorderLayout.NORTH);

        // --- 2. BẢNG DỮ LIỆU ---
        String[] columns = {"ID", "Tên đăng nhập", "Họ tên", "Vai trò"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableStaff = new JTable(tableModel);
        tableStaff.setFont(Theme.FONT_BODY);
        tableStaff.setRowHeight(30);
        tableStaff.setSelectionBackground(Theme.SLOT_EMPTY);
        tableStaff.setSelectionForeground(Theme.TEXT_PRIMARY);
        tableStaff.getTableHeader().setFont(Theme.FONT_TITLE);
        tableStaff.getTableHeader().setBackground(Theme.BG_PRIMARY);
        tableStaff.getTableHeader().setForeground(Theme.TEXT_PRIMARY);

        tableStaff.getSelectionModel().addListSelectionListener(e -> fillFormFromTable());
        
        JScrollPane scrollPane = new JScrollPane(tableStaff);
        scrollPane.setBorder(Theme.cardBorder());
        scrollPane.getViewport().setBackground(Theme.BG_PRIMARY);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // --- 3. KHU VỰC NÚT BẤM (Đồng bộ kiểu dáng và căn phải) ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Theme.BG_SECONDARY);

        // Khởi tạo và thiết lập trực tiếp cho từng nút
        btnAdd = new JButton("Thêm Mới");
        btnAdd.setFont(Theme.FONT_TITLE);
        btnAdd.setBackground(Theme.ACCENT_TEAL);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false); // Bắt buộc cho macOS
        btnAdd.setOpaque(true);         // Bắt buộc cho macOS
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setPreferredSize(new Dimension(130, 35));

        btnEdit = new JButton("Cập Nhật");
        btnEdit.setFont(Theme.FONT_TITLE);
        btnEdit.setBackground(Theme.ACCENT_BLUE);
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFocusPainted(false);
        btnEdit.setBorderPainted(false);
        btnEdit.setOpaque(true);
        btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEdit.setPreferredSize(new Dimension(130, 35));

        btnDelete = new JButton("Xóa");
        btnDelete.setFont(Theme.FONT_TITLE);
        btnDelete.setBackground(Theme.ACCENT_RED);
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.setBorderPainted(false);
        btnDelete.setOpaque(true);
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.setPreferredSize(new Dimension(130, 35));

        btnClear = new JButton("Làm Mới");
        btnClear.setFont(Theme.FONT_TITLE);
        btnClear.setBackground(Theme.TEXT_MUTED);
        btnClear.setForeground(Color.WHITE);
        btnClear.setFocusPainted(false);
        btnClear.setBorderPainted(false);
        btnClear.setOpaque(true);
        btnClear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClear.setPreferredSize(new Dimension(135, 35));

        btnPanel.add(btnAdd); 
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete); 
        btnPanel.add(btnClear);

        bottomPanel.add(btnPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- 4. SỰ KIỆN NÚT BẤM ---
        btnAdd.addActionListener(e -> addAction());
        btnEdit.addActionListener(e -> editAction());
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

    private void applyPermissions() {
        if (!Session.isAdmin()) {
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
        }
    }

    private void loadDataToTable() {
        tableModel.setRowCount(0); 
        List<Staff> list = bll.getAllStaff();
        for (Staff s : list) {
            tableModel.addRow(new Object[]{
                s.getId(), s.getUsername(), s.getFullName(), s.getRole()
            });
        }
    }

    private void fillFormFromTable() {
        int row = tableStaff.getSelectedRow();
        if (row >= 0) {
            txtId.setText(tableModel.getValueAt(row, 0).toString());
            txtUsername.setText(tableModel.getValueAt(row, 1).toString());
            txtFullName.setText(tableModel.getValueAt(row, 2).toString());
            cbRole.setSelectedItem(tableModel.getValueAt(row, 3));
            txtPassword.setText(""); // Luôn để trống mật khẩu vì lý do bảo mật
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtUsername.setText("");
        txtFullName.setText("");
        txtPassword.setText("");
        cbRole.setSelectedIndex(0);
        tableStaff.clearSelection();
    }

    private void addAction() {
        Staff staff = new Staff();
        staff.setUsername(txtUsername.getText().trim());
        staff.setFullName(txtFullName.getText().trim());
        staff.setRole((UserRole) cbRole.getSelectedItem());
        
        String rawPassword = new String(txtPassword.getPassword());

        if (bll.addStaff(staff, rawPassword)) {
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
            loadDataToTable();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại. Vui lòng nhập đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editAction() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa từ bảng!");
            return;
        }
        
        Staff staff = new Staff();
        staff.setId(Long.parseLong(txtId.getText()));
        staff.setUsername(txtUsername.getText().trim());
        staff.setFullName(txtFullName.getText().trim());
        staff.setRole((UserRole) cbRole.getSelectedItem());
        
        String rawPassword = new String(txtPassword.getPassword());
        
        if (bll.updateStaff(staff, rawPassword)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadDataToTable();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAction() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa từ bảng!");
            return;
        }
        
        Long id = Long.parseLong(txtId.getText());
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (bll.deleteStaff(id)) {
                JOptionPane.showMessageDialog(this, "Đã xóa nhân viên!");
                loadDataToTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}