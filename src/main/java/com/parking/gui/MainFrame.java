package com.parking.gui;

import com.parking.gui.Theme; // Đã bỏ comment để nhận diện màu sắc

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private JPanel contentArea;
    private CardLayout cards;
    
    private CheckInOutPanel checkPanel;
    private SlotPanel slotPanel;
    private ParkingLotPanel lotPanel;
    private FloorPanel floorPanel;
    private PriceConfigPanel pricePanel;
    private StaffPanel staffPanel;

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

        // ── Top bar ──────────────────────────────────────────
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
        JLabel badge = new JLabel("● Live");
        badge.setFont(Theme.FONT_SMALL);
        badge.setForeground(Theme.ACCENT_GREEN);
        lbClock = new JLabel();
        lbClock.setFont(Theme.FONT_SMALL);
        lbClock.setForeground(Theme.TEXT_MUTED);
        right.add(badge); right.add(lbClock);
        topBar.add(right, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // ── Sidebar ──────────────────────────────────────────
        JPanel sidebar = new JPanel();
        sidebar.setBackground(Theme.BG_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(12, 8, 12, 8));

        // Cập nhật Menu theo đúng các chức năng đã làm
        String[][] items = {
            {"checkin",    "🚗  Vào / Ra"},
            {"slots",      "🅿  Sơ đồ xe"},
            {"lots",       "🏢  Quản lý Bãi xe"},
            {"floors",     "🏢  Quản lý Tầng"},
            {"price",      "💰  Biểu giá"},
            {"staff",      "👥  Nhân sự"}
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

        // ── Content ──────────────────────────────────────────
        cards = new CardLayout();
        contentArea = new JPanel(cards);
        contentArea.setBackground(Theme.BG_SECONDARY);

        // Khởi tạo các Panel
        slotPanel   = new SlotPanel();
        // Truyền hàm callback để CheckInPanel gọi loadSlots() cập nhật lại màu sắc bãi xe
        checkPanel  = new CheckInOutPanel(() -> slotPanel.loadSlots()); 
        lotPanel    = new ParkingLotPanel();
        floorPanel  = new FloorPanel();
        pricePanel  = new PriceConfigPanel();
        staffPanel  = new StaffPanel();

        contentArea.add(checkPanel, "checkin");
        contentArea.add(slotPanel,  "slots");
        contentArea.add(lotPanel,   "lots");
        contentArea.add(floorPanel, "floors");
        contentArea.add(pricePanel, "price");
        contentArea.add(staffPanel, "staff");
        add(contentArea, BorderLayout.CENTER);

        switchTo("checkin"); // Mặc định mở trang Nhận/Trả xe
    }

    private void switchTo(String key) {
        cards.show(contentArea, key);
        navBtns.forEach(b -> {
            boolean active = b.getActionCommand().equals(key);
            b.setBackground(active ? new Color(50, 50, 55) : Theme.BG_SIDEBAR);
            b.setForeground(active ? Color.WHITE : new Color(180, 178, 170));
        });
        
        // Làm mới dữ liệu Sơ đồ xe mỗi khi click vào Tab này
        if ("slots".equals(key)) {
            slotPanel.loadSlots(); 
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
        return btn;
    }

    private void startClock() {
        Timer t = new Timer(1000, e -> lbClock.setText(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
        t.start();
        lbClock.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    // ── Entry point ───────────────────────────────────────────
    public static void main(String[] args) {
        try { 
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}
        
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}