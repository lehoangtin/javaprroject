package com.parking.gui;

import com.parking.bll.StatisticBLL;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class DashboardPanel extends JPanel {
    private StatisticBLL bll;

    private JLabel lblParkedCount;
    private JLabel lblEmptySlots;
    private JLabel lblActiveSubs;
    private JLabel lblTotalRevenue;

    public DashboardPanel() {
        bll = new StatisticBLL();
        initComponents();
    }

    private void initComponents() {
        setBackground(Theme.BG_SECONDARY);
        setBorder(Theme.sectionPadding());
        setLayout(new BorderLayout(20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel lblTitle = new JLabel("Tổng Quan Hệ Thống (Dashboard)");
        lblTitle.setFont(Theme.FONT_HEADER);
        lblTitle.setForeground(Theme.TEXT_PRIMARY);
        
        JButton btnRefresh = new JButton("Làm Mới Dữ Liệu");
        btnRefresh.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnRefresh.setFont(Theme.FONT_TITLE);
        btnRefresh.setBackground(Theme.TEXT_MUTED);
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBorderPainted(false);
        btnRefresh.setOpaque(true);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> refreshData());

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(btnRefresh, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
        JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        cardsPanel.setOpaque(false);

        lblParkedCount = createDataLabel();
        lblEmptySlots = createDataLabel();
        lblActiveSubs = createDataLabel();
        lblTotalRevenue = createDataLabel();

        cardsPanel.add(createCard("Xe Đang Trong Bãi", lblParkedCount, Theme.ACCENT_TEAL));
        cardsPanel.add(createCard("Chỗ Đỗ Còn Trống", lblEmptySlots, Theme.ACCENT_GREEN));
        cardsPanel.add(createCard("Vé Tháng Hoạt Động", lblActiveSubs, Theme.ACCENT_BLUE));
        cardsPanel.add(createCard("Tổng Doanh Thu (VNĐ)", lblTotalRevenue, Theme.ACCENT_AMBER));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(cardsPanel, BorderLayout.NORTH);
        
        add(wrapper, BorderLayout.CENTER);

        refreshData();
    }

    private JPanel createCard(String title, JLabel dataLabel, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setPreferredSize(new Dimension(300, 150));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(255, 255, 255, 200)); 

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(dataLabel, BorderLayout.CENTER);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        return card;
    }

    private JLabel createDataLabel() {
        JLabel label = new JLabel("0");
        label.setFont(new Font("Segoe UI", Font.BOLD, 36));
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setVerticalAlignment(SwingConstants.BOTTOM);
        return label;
    }

    public void refreshData() {
        int parkedCount = bll.getParkedVehicleCount();
        int emptySlots = bll.getEmptySlotCount();
        int activeSubs = bll.getActiveSubscriptionCount();
        BigDecimal revenue = bll.getTotalRevenue();

        DecimalFormat formatter = new DecimalFormat("#,###");

        lblParkedCount.setText(String.valueOf(parkedCount));
        lblEmptySlots.setText(String.valueOf(emptySlots));
        lblActiveSubs.setText(String.valueOf(activeSubs));
        lblTotalRevenue.setText(formatter.format(revenue) + " đ");
    }
}