package com.parking.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * MainDashboard - Giao diện điều khiển chính của hệ thống
 * Quản lý chuyển đổi giữa các chức năng: Nhân viên, Biểu giá, Sơ đồ, Giao dịch
 */
public class MainDashboard extends JFrame {

    private JPanel pnlSidebar;
    private JPanel pnlContent;
    private CardLayout cardLayout;
    
    // ĐỊNH NGHĨA BẢNG MÀU ĐỒNG BỘ VỚI CÁC PANEL CON[cite: 22]
    private final Color COLOR_SIDEBAR = new Color(33, 37, 41);
    private final Color COLOR_BTN_NORMAL = new Color(52, 58, 64);
    private final Color COLOR_BTN_HOVER = new Color(73, 80, 87);
    private final Color COLOR_ACCENT = new Color(0, 123, 255);

    public MainDashboard() {
        initComponents();
    }

    private void initComponents() {
        setTitle("PBL3 - HỆ THỐNG QUẢN LÝ BÃI XE THÔNG MINH");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. SIDEBAR (THANH ĐIỀU HƯỚNG BÊN TRÁI) ---
        pnlSidebar = new JPanel();
        pnlSidebar.setBackground(COLOR_SIDEBAR);
        pnlSidebar.setPreferredSize(new Dimension(260, 0));
        pnlSidebar.setLayout(new BoxLayout(pnlSidebar, BoxLayout.Y_AXIS));
        pnlSidebar.setBorder(new EmptyBorder(0, 10, 0, 10));

        // Logo
        JLabel lblTitle = new JLabel("PARKING SYSTEM");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setBorder(new EmptyBorder(40, 0, 40, 0));
        pnlSidebar.add(lblTitle);

        // Tạo các nút điều hướng
        JButton btnStaff = createSidebarButton("Quản lý Nhân viên");
        JButton btnPrice = createSidebarButton("Cấu hình Biểu giá");
        JButton btnSlots = createSidebarButton("Sơ đồ Vị trí (Slots)");
        JButton btnParking = createSidebarButton("Giao dịch Vào/Ra");

        pnlSidebar.add(btnStaff);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlSidebar.add(btnPrice);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlSidebar.add(btnSlots);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlSidebar.add(btnParking);

        add(pnlSidebar, BorderLayout.WEST);

        // --- 2. CONTENT PANEL (VÙNG HIỂN THỊ CHÍNH) ---
     // Trong hàm initComponents() của MainDashboard
        cardLayout = new CardLayout();
        pnlContent = new JPanel(cardLayout);

        // 1. Nạp các Panel vào "bộ nhớ" giao diện
        pnlContent.add(new StaffPanel(), "StaffPage");
        pnlContent.add(new PriceConfigPanel(), "PricePage");
        pnlContent.add(createHomePanel(), "HomePage"); // Trang chào mừng

        add(pnlContent, BorderLayout.CENTER);

        // 2. Gán sự kiện cho các nút Sidebar
        btnStaff.addActionListener(e -> cardLayout.show(pnlContent, "StaffPage"));
        btnPrice.addActionListener(e -> cardLayout.show(pnlContent, "PricePage"));

        // Mặc định hiện trang Home
        cardLayout.show(pnlContent, "HomePage");
    }

    private JPanel createHomePanel() {
        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setBackground(Color.WHITE);
        JLabel lbl = new JLabel("Chào mừng đến với Hệ thống Quản lý Bãi xe");
        lbl.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        lbl.setForeground(Color.GRAY);
        pnl.add(lbl);
        return pnl;
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setBackground(COLOR_BTN_NORMAL);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 20, 0, 0));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(COLOR_BTN_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(COLOR_BTN_NORMAL);
            }
        });
        return btn;
    }
}