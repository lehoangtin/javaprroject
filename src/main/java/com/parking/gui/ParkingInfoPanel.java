package com.parking.gui;

import com.parking.bll.ParkingInfoBLL;
import com.parking.entity.ParkingInfo;

import javax.swing.*;
import java.awt.*;

public class ParkingInfoPanel extends JPanel {
    private JTextField txtName;
    private JTextField txtAddress;
    private JTextField txtHotline;
    private JTextField txtCapacity;
    
    // Khai báo 2 nút
    private JButton btnEdit;
    private JButton btnSave;
    
    private ParkingInfoBLL bll;

    public ParkingInfoPanel() {
        bll = new ParkingInfoBLL();
        initComponents();
        loadData();
        // Mặc định khóa form khi vừa load lên
        setFieldsEnabled(false);
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

        // --- Khu vực chứa nút bấm ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setBackground(Theme.BG_PRIMARY);
        
        // Nút Chỉnh Sửa
        btnEdit = new JButton("Chỉnh Sửa");
        btnEdit.setFont(Theme.FONT_TITLE);
        btnEdit.setBackground(Color.DARK_GRAY);
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFocusPainted(false);
        btnEdit.setBorderPainted(false);
        btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEdit.setPreferredSize(new Dimension(150, 40));
        btnEdit.addActionListener(e -> setFieldsEnabled(true)); // Bấm Edit thì mở khóa form
        btnEdit.setBorderPainted(false);
        btnEdit.setOpaque(true); // Thêm dòng này
        // Nút Lưu
        btnSave = new JButton("Lưu Thay Đổi");
        btnSave.setFont(Theme.FONT_TITLE);
        btnSave.setBackground(Theme.ACCENT_BLUE);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setBorderPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.setPreferredSize(new Dimension(150, 40));
        btnSave.addActionListener(e -> saveAction()); // Bấm Lưu thì gọi hàm Save
        btnSave.setBorderPainted(false);
        btnSave.setOpaque(true); // Thêm dòng này
        btnPanel.add(btnEdit);
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
        // Đổi màu nền một chút khi textfield bị khóa (tùy chọn)
        textField.setDisabledTextColor(Color.GRAY);
        return textField;
    }

    // --- Hàm điều khiển trạng thái Form ---
    private void setFieldsEnabled(boolean isEnabled) {
        txtName.setEnabled(isEnabled);
        txtAddress.setEnabled(isEnabled);
        txtHotline.setEnabled(isEnabled);
        txtCapacity.setEnabled(isEnabled);
        
        btnSave.setEnabled(isEnabled);     // Đang mở form thì nút Lưu bật
        btnEdit.setEnabled(!isEnabled);    // Đang mở form thì nút Edit tắt
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
                setFieldsEnabled(false); // Lưu thành công thì khóa form lại
            } else {
                JOptionPane.showMessageDialog(this, "Lưu thất bại. Vui lòng kiểm tra lại thông tin (Tên và Sức chứa không được trống)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Sức chứa phải là một số nguyên hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}