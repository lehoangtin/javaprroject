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
        setBorder(Theme.sectionPadding()); 

        // --- Tiêu đề trang ---
        JLabel lblTitle = new JLabel("Quản Lý Nhân Sự (Staff)");
        lblTitle.setFont(Theme.FONT_HEADER);
        lblTitle.setForeground(Theme.TEXT_PRIMARY);
        add(lblTitle, BorderLayout.NORTH);

        // --- Panel Khung chính ---
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
        // THÊM CỘT TRẠNG THÁI VÀO BẢNG
        String[] columns = {"ID", "Tên đăng nhập", "Họ tên", "Vai trò", "Trạng thái"};
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

        // --- 3. KHU VỰC NÚT BẤM ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Theme.BG_SECONDARY);

        btnAdd = new JButton("Thêm Mới");
        btnAdd.setFont(Theme.FONT_TITLE);
        btnAdd.setBackground(Theme.ACCENT_TEAL);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setOpaque(true);
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

        // ĐỔI TÊN VÀ MÀU SẮC CHO NÚT "KHÓA TÀI KHOẢN"
        btnDelete = new JButton("Khóa tài khoản");
        btnDelete.setFont(Theme.FONT_TITLE);
        btnDelete.setBackground(new Color(255, 153, 0)); // Màu Cam Cảnh báo
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.setBorderPainted(false);
        btnDelete.setOpaque(true);
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.setPreferredSize(new Dimension(160, 35));

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
            // Hiển thị text trực quan dựa trên is_active
        	String status = (s.getIsActive() != null && s.getIsActive()) ? "Đang làm việc" : "Đã nghỉ việc";            
            tableModel.addRow(new Object[]{
                s.getId(), s.getUsername(), s.getFullName(), s.getRole(), status
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
            txtPassword.setText(""); 
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
            JOptionPane.showMessageDialog(this, "Thêm thất bại. Vui lòng nhập đủ thông tin hoặc Username đã bị trùng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần thao tác từ bảng!");
            return;
        }
        
        Long id = Long.parseLong(txtId.getText());
        String username = txtUsername.getText(); // Lấy username để BLL kiểm tra
        
        // SỬA CÂU HỎI XÁC NHẬN CHO CHUẨN UX
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn khóa tài khoản / cho nhân viên này nghỉ việc không?", 
                "Xác nhận khóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
        if (confirm == JOptionPane.YES_OPTION) {
            String result = bll.deleteStaff(id, username); // Gọi hàm mới
            
            if ("SUCCESS".equals(result)) {
                JOptionPane.showMessageDialog(this, "Đã khóa tài khoản nhân viên thành công!");
                loadDataToTable();
                clearForm();
            } else {
                // In ra chi tiết lý do từ chối (Vd: Là Admin gốc)
                JOptionPane.showMessageDialog(this, result, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}