package com.parking.gui;

import com.parking.bll.ParkingInfoBLL;
import com.parking.entity.ParkingInfo;

import javax.swing.*;
import java.awt.*;

public class ParkingInfoPanel extends JPanel {
    private JTextField txtName;
    private JTextField txtAddress;
    private JTextField txtHotline;
    private JButton btnEdit;
    private JButton btnSave;
    
    private ParkingInfoBLL bll;

    public ParkingInfoPanel() {
        bll = new ParkingInfoBLL();
        initComponents();
        loadData();
        setFieldsEnabled(false);
    }

    private void initComponents() {
        setBackground(Theme.BG_SECONDARY);
        setLayout(new GridBagLayout()); 
        
        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(Theme.BG_PRIMARY);
        cardPanel.setBorder(Theme.cardBorder());
        cardPanel.setLayout(new BorderLayout(20, 20));
        cardPanel.setPreferredSize(new Dimension(500, 400));
        JLabel lblTitle = new JLabel("Thông Tin Bãi Xe", SwingConstants.CENTER);
        lblTitle.setFont(Theme.FONT_HEADER);
        lblTitle.setForeground(Theme.TEXT_PRIMARY);
        cardPanel.add(lblTitle, BorderLayout.NORTH);

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
       
        cardPanel.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setBackground(Theme.BG_PRIMARY);
        
        btnEdit = new JButton("Chỉnh Sửa");
        btnEdit.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnEdit.setFont(Theme.FONT_TITLE);
        btnEdit.setBackground(Color.DARK_GRAY);
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFocusPainted(false);
        btnEdit.setBorderPainted(false);
        btnEdit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEdit.setPreferredSize(new Dimension(150, 40));
        btnEdit.addActionListener(e -> setFieldsEnabled(true)); 
        btnEdit.setBorderPainted(false);
        btnEdit.setOpaque(true);
        btnSave = new JButton("Lưu Thay Đổi");
        btnSave.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnSave.setFont(Theme.FONT_TITLE);
        btnSave.setBackground(Theme.ACCENT_BLUE);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setBorderPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.setPreferredSize(new Dimension(150, 40));
        btnSave.addActionListener(e -> saveAction());
        btnSave.setBorderPainted(false);
        btnSave.setOpaque(true); 
        btnPanel.add(btnEdit);
        btnPanel.add(btnSave);
        
        cardPanel.add(btnPanel, BorderLayout.SOUTH);
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
        textField.setDisabledTextColor(Color.GRAY);
        return textField;
    }

    private void setFieldsEnabled(boolean isEnabled) {
        txtName.setEnabled(isEnabled);
        txtAddress.setEnabled(isEnabled);
        txtHotline.setEnabled(isEnabled);
        
        btnSave.setEnabled(isEnabled);     
        btnEdit.setEnabled(!isEnabled);   
    }

    private void loadData() {
        ParkingInfo info = bll.getInfo();
        if (info != null) {
            txtName.setText(info.getName() != null ? info.getName() : "");
            txtAddress.setText(info.getAddress() != null ? info.getAddress() : "");
            txtHotline.setText(info.getHotline() != null ? info.getHotline() : "");
        }
    }

    private void saveAction() {
        try {
            String name = txtName.getText().trim();
            String address = txtAddress.getText().trim();
            String hotline = txtHotline.getText().trim();

            if (bll.saveInfo(name, address, hotline)) {
                JOptionPane.showMessageDialog(this, "Đã lưu thông tin bãi xe thành công!");
                setFieldsEnabled(false); 
            } else {
                JOptionPane.showMessageDialog(this, "Lưu thất bại. Vui lòng kiểm tra lại thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Sức chứa phải là một số nguyên hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}