package com.parking.gui;

import com.parking.bll.StaffBLL;
import com.parking.entity.Staff;
//import com.parking.ui.Theme.;
import com.parking.gui.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StaffPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private StaffBLL bll = new StaffBLL();

    public StaffPanel() {
        setLayout(new BorderLayout(0, 16));
        setBackground(Theme.BG_SECONDARY);
        setBorder(Theme.sectionPadding());

        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(Theme.BG_PRIMARY);
        card.setBorder(Theme.cardBorder());

        // Header
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        JLabel lblTitle = new JLabel("Danh sách Nhân sự");
        lblTitle.setFont(Theme.FONT_HEADER);
        lblTitle.setForeground(Theme.TEXT_PRIMARY);
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        card.add(pnlHeader, BorderLayout.NORTH);

        // Bảng dữ liệu
        model = new DefaultTableModel(new String[]{"ID", "Họ Tên", "Tên Đăng Nhập", "Chức Vụ", "Trạng Thái"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        styleTable(table);
        card.add(new JScrollPane(table), BorderLayout.CENTER);

        // Nút bấm Footer
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlFooter.setOpaque(false);
        
        JButton btnAdd = createFlatButton("Thêm mới", Theme.ACCENT_BLUE);
        JButton btnEdit = createFlatButton("Sửa thông tin", Theme.ACCENT_AMBER);
        JButton btnToggle = createFlatButton("Khóa / Mở tài khoản", Theme.ACCENT_TEAL);

        btnAdd.addActionListener(e -> showStaffDialog(null));
        btnEdit.addActionListener(e -> handleEdit());
        btnToggle.addActionListener(e -> handleToggle());

        pnlFooter.add(btnAdd);
        pnlFooter.add(btnEdit);
        pnlFooter.add(btnToggle);

        card.add(pnlFooter, BorderLayout.SOUTH);
        add(card, BorderLayout.CENTER);

        loadData();
    }

    // ================= XỬ LÝ SỰ KIỆN =================

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row != -1) {
            Staff s = new Staff();
            s.setId((Long) model.getValueAt(row, 0));
            s.setFullName((String) model.getValueAt(row, 1));
            s.setUsername((String) model.getValueAt(row, 2));
            s.setRole((String) model.getValueAt(row, 3));
            showStaffDialog(s);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleToggle() {
        int row = table.getSelectedRow();
        if (row != -1) {
            Long id = (Long) model.getValueAt(row, 0);
            boolean isActive = model.getValueAt(row, 4).equals("Đang hoạt động");
            if (bll.toggleStatus(id, isActive)) {
                loadData();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Hiển thị Dialog Thêm/Sửa
    private void showStaffDialog(Staff staff) {
        boolean isAdd = (staff == null);
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), isAdd ? "Thêm Nhân Viên Mới" : "Cập Nhật Thông Tin", true);
        dialog.setSize(400, isAdd ? 320 : 250);
        dialog.setLocationRelativeTo(this);
        
        JPanel pnlForm = new JPanel(new GridLayout(isAdd ? 4 : 3, 2, 10, 15));
        pnlForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlForm.setBackground(Color.WHITE);

        JTextField txtName = new JTextField(isAdd ? "" : staff.getFullName());
        JTextField txtUsername = new JTextField(isAdd ? "" : staff.getUsername());
        if (!isAdd) txtUsername.setEnabled(false); // Không cho đổi username khi sửa
        
        JPasswordField txtPassword = new JPasswordField();
        JComboBox<String> cbRole = new JComboBox<>(new String[]{"Quản lý", "Nhân viên"});
        if (!isAdd && staff.getRole().equals("Nhân viên")) cbRole.setSelectedIndex(1);

        pnlForm.add(new JLabel("Họ và Tên:")); pnlForm.add(txtName);
        pnlForm.add(new JLabel("Username:")); pnlForm.add(txtUsername);
        if (isAdd) {
            pnlForm.add(new JLabel("Mật khẩu:")); pnlForm.add(txtPassword);
        }
        pnlForm.add(new JLabel("Chức vụ:")); pnlForm.add(cbRole);

        JButton btnSave = createFlatButton("Lưu thông tin", Theme.ACCENT_GREEN);
        btnSave.addActionListener(e -> {
            try {
                Staff s = isAdd ? new Staff() : staff;
                s.setFullName(txtName.getText());
                s.setRole((String) cbRole.getSelectedItem());
                
                if (isAdd) {
                    s.setUsername(txtUsername.getText());
                    s.setPassword(new String(txtPassword.getPassword()));
                    if (bll.addStaff(s)) dialog.dispose();
                } else {
                    if (bll.updateStaff(s)) dialog.dispose();
                }
                loadData();
                JOptionPane.showMessageDialog(this, "Lưu thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel pnlBot = new JPanel();
        pnlBot.setBackground(Color.WHITE);
        pnlBot.add(btnSave);

        dialog.add(pnlForm, BorderLayout.CENTER);
        dialog.add(pnlBot, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // ================= TIỆN ÍCH UI =================

    private void loadData() {
        model.setRowCount(0);
        List<Staff> list = bll.getAllStaffs();
        for (Staff s : list) {
            model.addRow(new Object[]{ s.getId(), s.getFullName(), s.getUsername(), s.getRole(), s.getIsActive() ? "Đang hoạt động" : "Đã khóa" });
        }
    }

    private void styleTable(JTable t) {
        t.setFont(Theme.FONT_BODY);
        t.setRowHeight(32);
        t.getTableHeader().setFont(Theme.FONT_SMALL);
        t.getTableHeader().setBackground(Theme.BG_SECONDARY);
        t.setGridColor(Theme.BORDER);
        t.setSelectionBackground(new Color(230, 241, 251));
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < t.getColumnCount(); i++) t.getColumnModel().getColumn(i).setCellRenderer(center);
    }

    private JButton createFlatButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.FONT_BODY);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(150, 36));
        return btn;
    }
}