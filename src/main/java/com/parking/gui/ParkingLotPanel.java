package com.parking.gui;

import com.parking.bll.ParkingInfoBLL;
import com.parking.entity.ParkingInfo;

import javax.swing.*;
import java.awt.*;

public class ParkingLotPanel extends JPanel {
    private JTextField txtName;
    private JTextField txtAddress;
    private JTextField txtHotline;
    private JTextField txtCapacity;
    private JButton btnSave;
    
    private ParkingInfoBLL bll;

    public ParkingLotPanel() {
        bll = new ParkingInfoBLL();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setBackground(Theme.BG_SECONDARY);
        setLayout(new GridBagLayout()); // Căn giữa nội dung
        
        // --- Card chứa form ---
        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(Theme.BG_PRIMARY);
        cardPanel.setBorder(Theme.cardBorder());
        cardPanel.setLayout(new BorderLayout(20, 20));
        cardPanel.setPreferredSize(new Dimension(500, 400));

        // --- Tiêu đề ---
        JLabel lblTitle = new JLabel("Thông Tin Bãi Xe", SwingConstants.CENTER);
        lblTitle.setFont(Theme.FONT_HEADER);
        lblTitle.setForeground(Theme.TEXT_PRIMARY);
        cardPanel.add(lblTitle, BorderLayout.NORTH);

        // --- Form nhập liệu ---
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 20));
        formPanel.setBackground(Theme.BG_PRIMARY);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        formPanel.add(createLabel("Tên hệ thống/bãi xe:"));
        txtName = createTextField();
        formPanel.add(txtName);

        formPanel.add(createLabel("Địa chỉ:"));
        txtAddress = createTextField();
        formPanel.add(txtAddress);

        formPanel.add(createLabel("Số Hotline:"));
        txtHotline = createTextField();
        formPanel.add(txtHotline);

        formPanel.add(createLabel("Sức chứa tối đa (xe):"));
        txtCapacity = createTextField();
        formPanel.add(txtCapacity);

        cardPanel.add(formPanel, BorderLayout.CENTER);

        // --- Nút lưu ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(Theme.BG_PRIMARY);
        
        btnSave = new JButton("Lưu Thay Đổi");
        btnSave.setFont(Theme.FONT_TITLE);
        btnSave.setBackground(Theme.ACCENT_BLUE);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setBorderPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.setPreferredSize(new Dimension(200, 40));
        
        btnSave.addActionListener(e -> saveAction());
        btnPanel.add(btnSave);
        
        cardPanel.add(btnPanel, BorderLayout.SOUTH);

        // Thêm card vào Panel chính
        add(cardPanel);
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
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        return textField;
    }

    // --- Logic xử lý ---
    private void loadData() {
        ParkingInfo info = bll.getInfo();
        if (info != null) {
            txtName.setText(info.getName() != null ? info.getName() : "");
            txtAddress.setText(info.getAddress() != null ? info.getAddress() : "");
            txtHotline.setText(info.getHotline() != null ? info.getHotline() : "");
            txtCapacity.setText(info.getCapacity() != null ? String.valueOf(info.getCapacity()) : "");
        }
    }

    private void saveAction() {
        try {
            String name = txtName.getText().trim();
            String address = txtAddress.getText().trim();
            String hotline = txtHotline.getText().trim();
            Integer capacity = Integer.parseInt(txtCapacity.getText().trim());

            if (bll.saveInfo(name, address, hotline, capacity)) {
                JOptionPane.showMessageDialog(this, "Đã lưu thông tin bãi xe thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Lưu thất bại. Vui lòng kiểm tra lại thông tin (Tên và Sức chứa không được trống)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Sức chứa phải là một số nguyên hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}