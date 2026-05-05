package com.parking.gui;

import com.parking.dao.StaffDAO;
import com.parking.entity.Staff;
import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {
    private JTextField txtUser = new JTextField(20);
    private JPasswordField txtPass = new JPasswordField(20);
    private boolean authenticated = false;
    private Staff loggedInStaff;

    public LoginDialog() {
        setTitle("ĐĂNG NHẬP HỆ THỐNG");
        setLayout(new GridLayout(3, 2, 10, 10));
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(new JLabel("Tên đăng nhập:")); add(txtUser);
        add(new JLabel("Mật khẩu:")); add(txtPass);

        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.addActionListener(e -> handleLogin());
        add(new JLabel("")); add(btnLogin);

        pack();
        setLocationRelativeTo(null);
        setModal(true);
    }

    private void handleLogin() {
        StaffDAO dao = new StaffDAO();
        loggedInStaff = dao.login(txtUser.getText(), new String(txtPass.getPassword()));
        if (loggedInStaff != null) {
            authenticated = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Sai tài khoản hoặc mật khẩu!");
        }
    }

    public boolean isSucceeded() { return authenticated; }
}