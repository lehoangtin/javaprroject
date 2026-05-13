package com.parking.gui;

import com.parking.bll.PriceConfigBLL;
import com.parking.entity.PriceConfig;
import com.parking.enums.VehicleType;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class PriceConfigPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<VehicleType> cbVehicleType;
    private JTextField txtBaseFee;
    private JTextField txtExtraFee;
    private JTextField txtMonthlyPrice;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private PriceConfigBLL bll;
    private Long selectedConfigId = null;

    public PriceConfigPanel() {
        bll = new PriceConfigBLL();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setBackground(Theme.BG_SECONDARY);
        setBorder(Theme.sectionPadding());
        setLayout(new BorderLayout(15, 15));

        JLabel lblTitle = new JLabel("Cấu Hình Biểu Giá (Price Config)");
        lblTitle.setFont(Theme.FONT_HEADER);
        lblTitle.setForeground(Theme.TEXT_PRIMARY);
        add(lblTitle, BorderLayout.NORTH);

        String[] columns = {"ID", "Loại Xe", "Giá Khởi Điểm", "Phí/Giờ Thêm", "Giá Vé Tháng"};
        tableModel = new DefaultTableModel(columns, 0) {
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
        
        formPanel.add(createLabel("Loại xe:"));
        cbVehicleType = new JComboBox<>(VehicleType.values());
        cbVehicleType.setFont(Theme.FONT_BODY);
        formPanel.add(cbVehicleType);

        formPanel.add(createLabel("Giá khởi điểm:"));
        txtBaseFee = createTextField();
        formPanel.add(txtBaseFee);

        formPanel.add(createLabel("Phí/giờ tiếp theo:"));
        txtExtraFee = createTextField();
        formPanel.add(txtExtraFee);

        formPanel.add(createLabel("Giá vé tháng:"));
        txtMonthlyPrice = createTextField();
        formPanel.add(txtMonthlyPrice);

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
                selectedConfigId = (Long) tableModel.getValueAt(selectedRow, 0);
                cbVehicleType.setSelectedItem(VehicleType.valueOf(tableModel.getValueAt(selectedRow, 1).toString()));
                txtBaseFee.setText(tableModel.getValueAt(selectedRow, 2).toString());
                txtExtraFee.setText(tableModel.getValueAt(selectedRow, 3).toString());
                txtMonthlyPrice.setText(tableModel.getValueAt(selectedRow, 4).toString());
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

    private void loadData() {
        tableModel.setRowCount(0);
        List<PriceConfig> list = bll.getAllConfigs();
        for (PriceConfig config : list) {
            Object[] row = {
                config.getId(),
                config.getVehicleType().name(),
                config.getBaseFee(),
                config.getExtraFeePerHour(),
                config.getMonthlyPrice()
            };
            tableModel.addRow(row);
        }
    }

    private void clearForm() {
        selectedConfigId = null;
        table.clearSelection();
        cbVehicleType.setSelectedIndex(0);
        txtBaseFee.setText("");
        txtExtraFee.setText("");
        txtMonthlyPrice.setText("");
    }

    private void addAction() {
        try {
            VehicleType type = (VehicleType) cbVehicleType.getSelectedItem();
            BigDecimal baseFee = new BigDecimal(txtBaseFee.getText().trim());
            BigDecimal extraFee = new BigDecimal(txtExtraFee.getText().trim());
            BigDecimal monthlyPrice = new BigDecimal(txtMonthlyPrice.getText().trim());

            if (bll.addConfig(type, baseFee, extraFee, monthlyPrice)) {
                JOptionPane.showMessageDialog(this, "Thêm cấu hình giá thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại. Giá tiền không được âm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập định dạng số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateAction() {
        if (selectedConfigId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một mục trên bảng để sửa!");
            return;
        }

        try {
            VehicleType type = (VehicleType) cbVehicleType.getSelectedItem();
            BigDecimal baseFee = new BigDecimal(txtBaseFee.getText().trim());
            BigDecimal extraFee = new BigDecimal(txtExtraFee.getText().trim());
            BigDecimal monthlyPrice = new BigDecimal(txtMonthlyPrice.getText().trim());

            if (bll.updateConfig(selectedConfigId, type, baseFee, extraFee, monthlyPrice)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại. Kiểm tra lại dữ liệu nhập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập định dạng số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAction() {
        if (selectedConfigId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một mục trên bảng để xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa cấu hình này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (bll.deleteConfig(selectedConfigId)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}