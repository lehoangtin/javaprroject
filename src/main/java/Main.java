//package com.parking; // QUAN TRỌNG: Tín kiểm tra nếu file nằm trong folder com/parking thì phải có dòng này

import javax.swing.UIManager; // Fix lỗi UIManager cannot be resolved
import javax.swing.SwingUtilities; // Fix lỗi SwingUtilities cannot be resolved
import com.parking.gui.LoginDialog;
import com.parking.gui.MainDashboard;
import javax.swing.JOptionPane; // Dòng này sẽ xóa lỗi đỏ ở JOptionPane
/**
 * Lớp khởi chạy chính của hệ thống.
 * Đã bọc trong 'public class Main' để tránh lỗi Java 25 'Compact Source Files'[cite: 23].
 */
public class Main {
    
    public static void main(String[] args) {
        // 1. Thiết lập giao diện theo hệ điều hành (Windows/macOS)[cite: 23]
        try { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
        } catch (Exception e) {
            System.err.println("Không thể thiết lập Look and Feel: " + e.getMessage());
        }

        // 2. Khởi chạy luồng giao diện Swing[cite: 23]
        SwingUtilities.invokeLater(() -> {
            try {
                LoginDialog login = new LoginDialog();
                login.setVisible(true);

                // Kiểm tra kết quả đăng nhập từ LoginDialog[cite: 23]
                if (login.isSucceeded()) {
                    MainDashboard dashboard = new MainDashboard();
                    dashboard.setVisible(true);
                } else {
                    System.out.println("Ứng dụng kết thúc do không đăng nhập.");
                    System.exit(0); 
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khởi động: " + e.getMessage());
            }
        });
    }
}