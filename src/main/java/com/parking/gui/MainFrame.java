package com.parking.gui;

import com.parking.utils.Session;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private JPanel contentArea;
    private CardLayout cards;
    private DashboardPanel dashboardPanel; 
    private CheckInOutPanel checkPanel;
    private ParkingInfoPanel lotPanel;
    private FloorPanel floorPanel;
    private PriceConfigPanel pricePanel;
    private StaffPanel staffPanel;
    private SlotVisualPanel slotVisualPanel; 
    private HistoryPanel historyPanel;
    private SlotManagementPanel slotManagePanel; 
    private MonthlySubscriptionPanel subPanel;

    private final List<JButton> navBtns = new ArrayList<>();
    private JLabel lbClock;

    public MainFrame() {
        setTitle("🅿 NhàXe Pro — Hệ thống quản lý nhà xe");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 750);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        buildUI();
        startClock();
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Theme.BG_PRIMARY);
        topBar.setPreferredSize(new Dimension(0, 48));
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER));

        JLabel logo = new JLabel("  🅿  NhàXe Pro");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        logo.setForeground(Theme.ACCENT_BLUE);
        topBar.add(logo, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        right.setOpaque(false);
        
        String userName = (Session.currentUser != null) ? Session.currentUser.getFullName() : "Admin";
        JLabel lblUser = new JLabel("👤 Xin chào, " + userName);
        lblUser.setFont(Theme.FONT_BODY);
        lblUser.setForeground(Theme.TEXT_PRIMARY);

        JButton btnLogout = new JButton("Đăng Xuất");
        btnLogout.setFont(Theme.FONT_SMALL);
        btnLogout.setBackground(Theme.ACCENT_RED);
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setOpaque(true);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> handleLogout());

        JLabel badge = new JLabel("● Live");
        badge.setFont(Theme.FONT_SMALL);
        badge.setForeground(Theme.ACCENT_GREEN);
        lbClock = new JLabel();
        lbClock.setFont(Theme.FONT_SMALL);
        lbClock.setForeground(Theme.TEXT_MUTED);
        
        right.add(lblUser);     
        right.add(badge); 
        right.add(lbClock);
        right.add(btnLogout);   
        
        topBar.add(right, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        JPanel sidebar = new JPanel();
        sidebar.setBackground(Theme.BG_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(12, 8, 12, 8));

        String[][] items = {
            {"dash",          "📊  Tổng quan"},
            {"checkin",       "🚗  Vào / Ra"},
            {"slots",         "🅿  Sơ đồ xe"},
            {"slot_manage",   "⚙  Quản lý chỗ đỗ"}, 
            {"lots",          "🏢  Quản lý Bãi xe"},
            {"floors",        "🏢  Quản lý Tầng"},
            {"price",         "💰  Biểu giá"},
            {"sub",           "💳  Vé tháng"},
            {"history",       "📜  Lịch sử GD"},
            {"staff",         "👥  Nhân sự"}
        };
        
        for (String[] item : items) {
            JButton btn = navButton(item[1]);
            btn.setActionCommand(item[0]);
            btn.addActionListener(e -> switchTo(e.getActionCommand()));
            navBtns.add(btn);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(2));
        }
        add(sidebar, BorderLayout.WEST);

        cards = new CardLayout();
        contentArea = new JPanel(cards);
        contentArea.setBackground(Theme.BG_SECONDARY);
        
        dashboardPanel  = new DashboardPanel(); 
        checkPanel 		= new CheckInOutPanel();
        lotPanel        = new ParkingInfoPanel();
        floorPanel      = new FloorPanel();
        pricePanel      = new PriceConfigPanel();
        staffPanel      = new StaffPanel();
        
        slotManagePanel = new SlotManagementPanel();
        subPanel        = new MonthlySubscriptionPanel();
        slotVisualPanel = new SlotVisualPanel(); 
        historyPanel    = new HistoryPanel();
        
        contentArea.add(checkPanel, "checkin");
        contentArea.add(dashboardPanel, "dash");
        contentArea.add(slotVisualPanel, "slots"); 
        contentArea.add(slotManagePanel, "slot_manage"); 
        contentArea.add(lotPanel,   "lots");
        contentArea.add(floorPanel, "floors");
        contentArea.add(pricePanel, "price");
        contentArea.add(subPanel,   "sub");             
        contentArea.add(historyPanel, "history"); 
        contentArea.add(staffPanel, "staff");
        
        add(contentArea, BorderLayout.CENTER);

        switchTo("dash");
    }

    private void switchTo(String key) {
        cards.show(contentArea, key);
        navBtns.forEach(b -> {
            boolean active = b.getActionCommand().equals(key);
            b.setBackground(active ? new Color(50, 50, 55) : Theme.BG_SIDEBAR);
            b.setForeground(active ? Color.WHITE : new Color(180, 178, 170));
        });
        
        if ("dash".equals(key)) {
            dashboardPanel.refreshData(); 
        }
        if ("slots".equals(key)) {
        	slotVisualPanel.loadMap(); 
        }
        if ("checkin".equals(key)) {
            checkPanel.loadEmptySlots();
        }
    }

    private JButton navButton(String label) {
        JButton btn = new JButton(label);
        btn.setFont(Theme.FONT_NAV);
        btn.setForeground(new Color(180, 178, 170));
        btn.setBackground(Theme.BG_SIDEBAR);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        return btn;
    }

    private void startClock() {
        Timer t = new Timer(1000, e -> lbClock.setText(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
        t.start();
        lbClock.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn đăng xuất?", 
                "Xác nhận", 
                JOptionPane.YES_NO_OPTION);
                
        if (confirm == JOptionPane.YES_OPTION) {
            Session.currentUser = null;
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                LoginDialog login = new LoginDialog();
                login.setVisible(true);
                if (login.isSucceeded()) {
                    new MainFrame().setVisible(true);
                } else {
                    System.exit(0);
                }
            });
        }
    }

    public static void main(String[] args) {
        try { 
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}
        
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}