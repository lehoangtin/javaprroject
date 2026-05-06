package com.parking.gui;

import com.parking.bll.MonthlySubscriptionBLL;
import com.parking.entity.MonthlySubscription;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MonthlySubscriptionPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtVehicleId;
    private JTextField txtStartDate; // Định dạng YYYY-MM-DD
    private JTextField txtEndDate;   // Định dạng YYYY-MM-DD
    private JTextField txtAmountPaid;
    private JComboBox<String> cbStatus;
    private JButton btnAdd, btnUpdate, btnClear;
    private MonthlySubscriptionBLL bll;
    private Long selectedId = null;

    public MonthlySubscriptionPanel() {
        bll = new MonthlySubscriptionBLL();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setBackground(Theme.BG_SECONDARY);
        setBorder(Theme.sectionPadding());
        setLayout(new BorderLayout(15, 15));

        // --- Tiêu đề ---
        JLabel lblTitle = new JLabel("Quản Lý Hợp Đồng Vé Tháng");
        lblTitle.setFont(Theme.FONT_HEADER);
        lblTitle.setForeground(Theme.TEXT_PRIMARY);
        add(lblTitle, BorderLayout.NORTH);

        // --- Bảng dữ liệu ---
        String[] columns = {"Mã HĐ", "Mã Xe", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Số Tiền Thu", "Trạng Thái"};
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

        // --- Form nhập liệu ---
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(Theme.BG_PRIMARY);
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
                Theme.cardBorder(), 
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JPanel formPanel = new JPanel(new GridLayout(3, 4, 15, 15));
        formPanel.setBackground(Theme.BG_PRIMARY);

        formPanel.add(createLabel("Mã Xe (Vehicle ID):"));
        txtVehicleId = createTextField();
        formPanel.add(txtVehicleId);

        formPanel.add(createLabel("Số Tiền (VNĐ):"));
        txtAmountPaid = createTextField();
        formPanel.add(txtAmountPaid);

        formPanel.add(createLabel("Ngày Bắt Đầu (YYYY-MM-DD):"));
        txtStartDate = createTextField();
        txtStartDate.setText(LocalDate.now().toString()); // Default hôm nay
        formPanel.add(txtStartDate);

        formPanel.add(createLabel("Ngày Kết Thúc (YYYY-MM-DD):"));
        txtEndDate = createTextField();
        txtEndDate.setText(LocalDate.now().plusMonths(1).toString()); // Default +1 tháng
        formPanel.add(txtEndDate);

        formPanel.add(createLabel("Trạng thái:"));
        cbStatus = new JComboBox<>(new String[]{"ACTIVE", "EXPIRED", "CANCELLED"});
        cbStatus.setFont(Theme.FONT_BODY);
        formPanel.add(cbStatus);

        bottomPanel.add(formPanel, BorderLayout.CENTER);

        // --- Nút chức năng ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Theme.BG_PRIMARY);

        btnAdd = createButton("Đăng ký mới", Theme.ACCENT_TEAL);
        btnUpdate = createButton("Cập nhật", Theme.ACCENT_BLUE);
        btnClear = createButton("Làm mới", Theme.TEXT_MUTED);

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnClear);
        bottomPanel.add(btnPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // --- Bắt sự kiện chọn dòng trên bảng ---
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int row = table.getSelectedRow();
                selectedId = (Long) tableModel.getValueAt(row, 0);
                txtVehicleId.setText(tableModel.getValueAt(row, 1).toString());
                txtStartDate.setText(tableModel.getValueAt(row, 2).toString());
                txtEndDate.setText(tableModel.getValueAt(row, 3).toString());
                txtAmountPaid.setText(tableModel.getValueAt(row, 4).toString());
                cbStatus.setSelectedItem(tableModel.getValueAt(row, 5).toString());
            }
        });

        // --- Gắn Action ---
        btnAdd.addActionListener(e -> addAction());
        btnUpdate.addActionListener(e -> updateAction());
        btnClear.addActionListener(e -> clearForm());
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Theme.FONT_TITLE);
        lbl.setForeground(Theme.TEXT_PRIMARY);
        return lbl;
    }

    private JTextField createTextField() {
        JTextField tf = new JTextField();
        tf.setFont(Theme.FONT_BODY);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return tf;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.FONT_TITLE);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 35));
        return btn;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<MonthlySubscription> list = bll.getAll();
        for (MonthlySubscription sub : list) {
            tableModel.addRow(new Object[]{
                sub.getId(),
                sub.getVehicleId(),
                sub.getStartDate(),
                sub.getEndDate(),
                sub.getAmountPaid(),
                sub.getStatus()
            });
        }
    }

    private void clearForm() {
        selectedId = null;
        table.clearSelection();
        txtVehicleId.setText("");
        txtAmountPaid.setText("");
        txtStartDate.setText(LocalDate.now().toString());
        txtEndDate.setText(LocalDate.now().plusMonths(1).toString());
        cbStatus.setSelectedIndex(0);
    }

    private void addAction() {
        try {
            Long vehicleId = Long.parseLong(txtVehicleId.getText().trim());
            BigDecimal amount = new BigDecimal(txtAmountPaid.getText().trim());
            LocalDate start = LocalDate.parse(txtStartDate.getText().trim());
            LocalDate end = LocalDate.parse(txtEndDate.getText().trim());
            String status = cbStatus.getSelectedItem().toString();

            if (bll.registerMonthly(vehicleId, start, end, amount, status)) {
                JOptionPane.showMessageDialog(this, "Đăng ký vé tháng thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi: Ngày kết thúc phải sau ngày bắt đầu và số tiền >= 0!", "Lỗi Logic", JOptionPane.ERROR_MESSAGE);
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ. Vui lòng dùng YYYY-MM-DD!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng kiểm tra lại thông tin nhập (Mã xe, Số tiền)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateAction() {
        if (selectedId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hợp đồng để cập nhật!");
            return;
        }
        try {
            Long vehicleId = Long.parseLong(txtVehicleId.getText().trim());
            BigDecimal amount = new BigDecimal(txtAmountPaid.getText().trim());
            LocalDate start = LocalDate.parse(txtStartDate.getText().trim());
            LocalDate end = LocalDate.parse(txtEndDate.getText().trim());
            String status = cbStatus.getSelectedItem().toString();

            if (bll.updateSubscription(selectedId, vehicleId, start, end, amount, status)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại. Kiểm tra lại dữ liệu!", "Lỗi Logic", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi dữ liệu nhập. Vui lòng kiểm tra lại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}