package com.parking.gui;

import com.parking.bll.MonthlySubscriptionBLL;
import com.parking.dao.VehicleDAO;
import com.parking.entity.MonthlySubscription;
import com.parking.entity.Vehicle;
import com.parking.enums.SubscriptionStatus;
import com.parking.enums.VehicleType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MonthlySubscriptionPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    
    // Các ô nhập liệu thiết kế mới
    private JTextField txtLicensePlate, txtOwnerName, txtOwnerPhone;
    private JTextField txtStartDate, txtEndDate, txtAmountPaid;
    private JComboBox<VehicleType> cbVehicleType;
    private JComboBox<SubscriptionStatus> cbStatus;    
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    
    private MonthlySubscriptionBLL bll;
    private VehicleDAO vehicleDAO; // Để hiển thị thông tin ra bảng
    private Long selectedSubId = null;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public MonthlySubscriptionPanel() {
        bll = new MonthlySubscriptionBLL();
        vehicleDAO = new VehicleDAO();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setBackground(Theme.BG_SECONDARY);
        setBorder(Theme.sectionPadding());
        setLayout(new BorderLayout(15, 15));

        // --- TITLE ---
        JLabel lblTitle = new JLabel("Quản Lý Khách Hàng & Vé Tháng");
        lblTitle.setFont(Theme.FONT_HEADER);
        lblTitle.setForeground(Theme.TEXT_PRIMARY);
        add(lblTitle, BorderLayout.NORTH);

        // --- TABLE ---
        String[] columns = {"ID Vé", "Biển Số", "Loại Xe", "Tên Chủ Xe", "SĐT", "Ngày Đăng Ký", "Ngày Hết Hạn", "Số Tiền", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(Theme.FONT_BODY);
        table.setRowHeight(30);
        table.getTableHeader().setFont(Theme.FONT_TITLE);
        table.getTableHeader().setBackground(Theme.BG_PRIMARY);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(Theme.cardBorder());
        add(scrollPane, BorderLayout.CENTER);

        // --- FORM PANEL (Layout siêu đẹp) ---
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(Theme.BG_PRIMARY);
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
                Theme.cardBorder(), 
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Dùng GridBagLayout để căn chỉnh form xịn xò
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Theme.BG_PRIMARY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 15, 8, 15);
        gbc.weightx = 0.5;

        // CỘT 1 (Bên Trái)
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("Biển số xe *"), gbc);
        gbc.gridy = 1;
        txtLicensePlate = createTextField();
        formPanel.add(txtLicensePlate, gbc);

        gbc.gridy = 2;
        formPanel.add(createLabel("Tên chủ xe"), gbc);
        gbc.gridy = 3;
        txtOwnerName = createTextField();
        formPanel.add(txtOwnerName, gbc);

        gbc.gridy = 4;
        formPanel.add(createLabel("Ngày đăng ký (yyyy-MM-dd) *"), gbc);
        gbc.gridy = 5;
        txtStartDate = createTextField();
        formPanel.add(txtStartDate, gbc);

        gbc.gridy = 6;
        formPanel.add(createLabel("Số tiền thu (VNĐ) *"), gbc);
        gbc.gridy = 7;
        txtAmountPaid = createTextField();
        formPanel.add(txtAmountPaid, gbc);

        // CỘT 2 (Bên Phải)
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(createLabel("Loại xe"), gbc);
        gbc.gridy = 1;
        cbVehicleType = new JComboBox<>(VehicleType.values());
        cbVehicleType.setFont(Theme.FONT_BODY);
        formPanel.add(cbVehicleType, gbc);

        gbc.gridy = 2;
        formPanel.add(createLabel("Số điện thoại"), gbc);
        gbc.gridy = 3;
        txtOwnerPhone = createTextField();
        formPanel.add(txtOwnerPhone, gbc);

        gbc.gridy = 4;
        formPanel.add(createLabel("Ngày hết hạn (yyyy-MM-dd) *"), gbc);
        gbc.gridy = 5;
        txtEndDate = createTextField();
        formPanel.add(txtEndDate, gbc);

        gbc.gridy = 6;
        formPanel.add(createLabel("Trạng thái vé"), gbc);
        gbc.gridy = 7;
        cbStatus = new JComboBox<>(SubscriptionStatus.values());
        cbStatus.setFont(Theme.FONT_BODY);
        cbStatus.setRenderer(new DefaultListCellRenderer() {
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof com.parking.enums.SubscriptionStatus) {
                    setText(((com.parking.enums.SubscriptionStatus) value).getDisplayName());
                }
                return this;
            }
        });
        formPanel.add(cbStatus, gbc);

        bottomPanel.add(formPanel, BorderLayout.CENTER);

        // --- BUTTONS ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Theme.BG_PRIMARY);

        btnAdd = createButton("Ghi Nhận", Theme.ACCENT_TEAL);
        btnUpdate = createButton("Cập Nhật", Theme.ACCENT_BLUE);
        btnDelete = createButton("Xóa Vé", Theme.ACCENT_RED);
        btnClear = createButton("Làm Mới", Theme.TEXT_MUTED);

        btnPanel.add(btnAdd); btnPanel.add(btnUpdate); 
        btnPanel.add(btnDelete); btnPanel.add(btnClear);
        
        bottomPanel.add(btnPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- EVENTS ---
        table.getSelectionModel().addListSelectionListener(e -> fillFormFromTable());
        btnAdd.addActionListener(e -> saveAction(true));
        btnUpdate.addActionListener(e -> saveAction(false));
        btnDelete.addActionListener(e -> deleteAction());
        btnClear.addActionListener(e -> clearForm());
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(Theme.TEXT_PRIMARY);
        return label;
    }

    private JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setFont(Theme.FONT_BODY);
        tf.setPreferredSize(new Dimension(200, 32));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return tf;
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.FONT_TITLE);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 36));
        return btn;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<MonthlySubscription> list = bll.getAllSubscriptions();
        
        for (MonthlySubscription sub : list) {
            // Lấy thông tin Phương Tiện từ bảng Vehicle để map ra Table
            Vehicle v = vehicleDAO.findById(sub.getVehicleId());
            String plate = (v != null) ? v.getLicensePlate() : "N/A";
            String vType = (v != null) ? v.getVehicleType().name() : "N/A";
            String name = (v != null && v.getOwnerName() != null) ? v.getOwnerName() : "";
            String phone = (v != null && v.getOwnerPhone() != null) ? v.getOwnerPhone() : "";

            tableModel.addRow(new Object[]{
                sub.getId(), plate, vType, name, phone,
                sub.getStartDate(), sub.getEndDate(), 
                sub.getAmountPaid(), sub.getStatus().getDisplayName()
            });
        }
    }

    private void fillFormFromTable() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            selectedSubId = (Long) tableModel.getValueAt(row, 0);
            txtLicensePlate.setText(tableModel.getValueAt(row, 1).toString());
            cbVehicleType.setSelectedItem(VehicleType.valueOf(tableModel.getValueAt(row, 2).toString()));
            txtOwnerName.setText(tableModel.getValueAt(row, 3).toString());
            txtOwnerPhone.setText(tableModel.getValueAt(row, 4).toString());
            txtStartDate.setText(tableModel.getValueAt(row, 5).toString());
            txtEndDate.setText(tableModel.getValueAt(row, 6).toString());
            txtAmountPaid.setText(tableModel.getValueAt(row, 7).toString());
            cbStatus.setSelectedItem(tableModel.getValueAt(row, 8));
        }
    }

    private void clearForm() {
        selectedSubId = null;
        table.clearSelection();
        txtLicensePlate.setText("");
        txtOwnerName.setText("");
        txtOwnerPhone.setText("");
        txtStartDate.setText(LocalDate.now().format(dateFormatter));
        txtEndDate.setText(LocalDate.now().plusMonths(1).format(dateFormatter));
        txtAmountPaid.setText("0");
        cbVehicleType.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
    }

    private void saveAction(boolean isAdd) {
        if (!isAdd && selectedSubId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một vé tháng từ bảng để cập nhật!");
            return;
        }

        try {
            String plate = txtLicensePlate.getText().trim();
            VehicleType type = (VehicleType) cbVehicleType.getSelectedItem();
            String ownerName = txtOwnerName.getText().trim();
            String ownerPhone = txtOwnerPhone.getText().trim();

            MonthlySubscription sub = new MonthlySubscription();
            sub.setStartDate(LocalDate.parse(txtStartDate.getText().trim(), dateFormatter));
            sub.setEndDate(LocalDate.parse(txtEndDate.getText().trim(), dateFormatter));
            sub.setAmountPaid(new BigDecimal(txtAmountPaid.getText().trim()));
            sub.setStatus((com.parking.enums.SubscriptionStatus) cbStatus.getSelectedItem());

            // Gọi hàm xử lý thông minh từ BLL
            String result = bll.saveSubscriptionFull(isAdd ? null : selectedSubId, plate, type, ownerName, ownerPhone, sub);

            if ("SUCCESS".equals(result)) {
                JOptionPane.showMessageDialog(this, isAdd ? "Đăng ký vé tháng thành công!" : "Cập nhật vé tháng thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, result, "Lỗi Nghiệp Vụ", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Ngày tháng phải đúng định dạng yyyy-MM-dd (VD: 2026-05-01)", "Lỗi Nhập Liệu", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số tiền thu phải là số hợp lệ!", "Lỗi Nhập Liệu", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAction() {
        if (selectedSubId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn vé tháng cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa vé tháng này? (Thông tin xe vẫn sẽ được giữ lại)", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (bll.deleteSubscription(selectedSubId)) {
                JOptionPane.showMessageDialog(this, "Đã xóa vé tháng!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi hệ thống khi xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}