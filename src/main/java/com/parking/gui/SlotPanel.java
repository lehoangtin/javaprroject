package com.parking.gui;

import com.parking.bll.SlotBLL;
import com.parking.entity.Slot;
import com.parking.enums.SlotStatus; // Import Enum
import com.parking.enums.VehicleType; // Import Enum
import com.parking.gui.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class SlotPanel extends JPanel {
    private JPanel pnlGrid;
    private SlotBLL slotBLL = new SlotBLL();

    public SlotPanel() {
        setLayout(new BorderLayout(0, 16));
        setBackground(Theme.BG_SECONDARY);
        setBorder(Theme.sectionPadding());

        // 1. Phần Header: Tiêu đề và Chú thích (Legend)
        add(buildHeader(), BorderLayout.NORTH);

        // 2. Phần Sơ đồ (Grid)
        pnlGrid = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        pnlGrid.setBackground(Theme.BG_PRIMARY);
        pnlGrid.setBorder(Theme.cardBorder());

        JScrollPane scrollPane = new JScrollPane(pnlGrid);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);

        // Tải dữ liệu các ô đỗ lên giao diện
        loadSlots();
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel lblTitle = new JLabel("Sơ đồ Bãi Đỗ Xe");
        lblTitle.setFont(Theme.FONT_HEADER);
        lblTitle.setForeground(Theme.TEXT_PRIMARY);
        header.add(lblTitle, BorderLayout.WEST);

        // Chú thích màu sắc
        JPanel pnlLegend = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        pnlLegend.setOpaque(false);
        
        pnlLegend.add(createLegendItem("Trống", Theme.SLOT_EMPTY, Theme.SLOT_BORDER_EMPTY));
        pnlLegend.add(createLegendItem("Đang có xe", Theme.SLOT_TAKEN, Theme.SLOT_BORDER_TAKEN));
        
        // Nút Làm mới
        JButton btnRefresh = new JButton("Làm mới sơ đồ");
        btnRefresh.setFont(Theme.FONT_BODY);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadSlots());
        pnlLegend.add(btnRefresh);

        header.add(pnlLegend, BorderLayout.EAST);
        return header;
    }

    private JPanel createLegendItem(String text, Color bg, Color border) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        p.setOpaque(false);
        
        JPanel colorBox = new JPanel();
        colorBox.setPreferredSize(new Dimension(20, 20));
        colorBox.setBackground(bg);
        colorBox.setBorder(BorderFactory.createLineBorder(border, 2));
        
        JLabel lbl = new JLabel(text);
        lbl.setFont(Theme.FONT_BODY);
        
        p.add(colorBox);
        p.add(lbl);
        return p;
    }

    public void loadSlots() {
        pnlGrid.removeAll();
        List<Slot> slots = slotBLL.getAllSlots();
        for (Slot s : slots) {
            pnlGrid.add(createSlotCard(s));
        }
        pnlGrid.revalidate();
        pnlGrid.repaint();
    }

    private JPanel createSlotCard(Slot slot) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(130, 110));
        
        // Sử dụng Enum SlotStatus để kiểm tra[cite: 14]
        boolean isEmpty = (slot.getStatus() == SlotStatus.EMPTY);
        
        Color bgColor = isEmpty ? Theme.SLOT_EMPTY : Theme.SLOT_TAKEN;
        Color borderColor = isEmpty ? Theme.SLOT_BORDER_EMPTY : Theme.SLOT_BORDER_TAKEN;
        Color textColor = isEmpty ? Theme.ACCENT_TEAL : Theme.ACCENT_RED;

        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createLineBorder(borderColor, 2, true));

        // Tên ô đỗ (sử dụng getSlotCode thay vì getSlotName)[cite: 14]
        JLabel lblName = new JLabel(slot.getSlotCode(), SwingConstants.CENTER);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblName.setForeground(textColor);
        
        // Loại xe (Sử dụng Enum VehicleType)[cite: 14]
        String typeIcon = (slot.getVehicleType() == VehicleType.CAR) ? "🚗 Ô tô" : "🏍 Xe máy";
        String statusText = isEmpty ? "Còn trống" : "Đã có xe";
        
        JLabel lblInfo = new JLabel(typeIcon + " - " + statusText, SwingConstants.CENTER);
        lblInfo.setFont(Theme.FONT_SMALL);
        lblInfo.setForeground(Theme.TEXT_MUTED);
        lblInfo.setBorder(new EmptyBorder(0, 0, 10, 0));

        card.add(lblName, BorderLayout.CENTER);
        card.add(lblInfo, BorderLayout.SOUTH);

        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return card;
    }
}