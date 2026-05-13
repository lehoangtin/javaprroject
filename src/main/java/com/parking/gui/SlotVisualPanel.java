package com.parking.gui;

import com.parking.bll.FloorBLL;
import com.parking.bll.ParkingRecordBLL;
import com.parking.bll.SlotBLL;
import com.parking.entity.Floor;
import com.parking.entity.Slot;
import com.parking.enums.SlotStatus;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class SlotVisualPanel extends JPanel {
    private JTabbedPane tabbedPane;
    private FloorBLL floorBLL;
    private SlotBLL slotBLL;
    private ParkingRecordBLL recordBLL = new ParkingRecordBLL();
    public SlotVisualPanel() {
        floorBLL = new FloorBLL();
        slotBLL = new SlotBLL();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.BG_SECONDARY);
        setBorder(Theme.sectionPadding());

        JLabel lblTitle = new JLabel("Sơ Đồ Bãi Đỗ Xe");
        lblTitle.setFont(Theme.FONT_HEADER);
        lblTitle.setForeground(Theme.TEXT_PRIMARY);
        add(lblTitle, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(Theme.FONT_TITLE);
        add(tabbedPane, BorderLayout.CENTER);

        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        legendPanel.setBackground(Theme.BG_SECONDARY);
        legendPanel.add(createLegendItem("Trống", Theme.SLOT_EMPTY, Theme.SLOT_BORDER_EMPTY));
        legendPanel.add(createLegendItem("Đang đỗ", Theme.SLOT_TAKEN, Theme.SLOT_BORDER_TAKEN));
        legendPanel.add(createLegendItem("Bảo trì", Color.LIGHT_GRAY, Color.GRAY));
        add(legendPanel, BorderLayout.SOUTH);

        loadMap();
    }

    public void loadMap() {
        tabbedPane.removeAll(); 
        List<Floor> floors = floorBLL.getAllFloors();

        for (Floor floor : floors) {
            JPanel floorMapPanel = new JPanel();
            floorMapPanel.setLayout(new BoxLayout(floorMapPanel, BoxLayout.Y_AXIS));
            floorMapPanel.setBackground(Theme.BG_PRIMARY);
            floorMapPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

            List<Slot> slots = slotBLL.getSlotsByFloor(floor.getId());
            
            JPanel currentRowPanel = null;
            for (int i = 0; i < slots.size(); i++) {
                if (i % 10 == 0) {
                    currentRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
                    currentRowPanel.setBackground(Theme.BG_PRIMARY);
                    currentRowPanel.setAlignmentX(Component.LEFT_ALIGNMENT); 
                    floorMapPanel.add(currentRowPanel);
                }
                
                JButton btnSlot = createSlotButton(slots.get(i));
                currentRowPanel.add(btnSlot);
            }

            String tabTitle = "Tầng " + floor.getFloorNumber();
            
            if (floor.getCapacity() != null) {
                tabTitle += " (Capacity:" + floor.getCapacity() + ")";
            }
            
            if (floor.getDescription() != null && !floor.getDescription().trim().isEmpty()) {
                 tabTitle += " - " + floor.getDescription();
            }
                               
            JPanel wrapPanel = new JPanel(new BorderLayout());
            wrapPanel.setBackground(Theme.BG_PRIMARY);
            wrapPanel.add(floorMapPanel, BorderLayout.NORTH);
            JScrollPane scrollPane = new JScrollPane(wrapPanel);
            scrollPane.setBorder(null);
            
            tabbedPane.addTab(tabTitle, scrollPane);
        }
    }

    private JButton createSlotButton(Slot slot) {
        JButton btn = new JButton("<html><center>" + slot.getSlotNumber() + "<br><br><span style='font-size:8px;'>" + slot.getVehicleType() + "</span></center></html>");
        btn.setPreferredSize(new Dimension(80, 100)); 
        btn.setFont(Theme.FONT_BODY);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);

        if (slot.getStatus() == SlotStatus.EMPTY) {
            btn.setBackground(Theme.SLOT_EMPTY);
            btn.setForeground(Theme.TEXT_PRIMARY);
            btn.setBorder(BorderFactory.createLineBorder(Theme.SLOT_BORDER_EMPTY, 2));
        } else if (slot.getStatus() == SlotStatus.OCCUPIED) {
            btn.setBackground(Theme.SLOT_TAKEN);
            btn.setForeground(Theme.TEXT_PRIMARY);
            btn.setBorder(BorderFactory.createLineBorder(Theme.SLOT_BORDER_TAKEN, 2));
        } else {
            btn.setBackground(Color.LIGHT_GRAY);
            btn.setForeground(Color.DARK_GRAY);
            btn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        }

        btn.addActionListener(e -> {
            if (slot.getStatus() == SlotStatus.OCCUPIED) {
                Map<String, String> info = recordBLL.getVehicleInfoInSlot(slot.getSlotNumber());
                
                if (info != null) {
                    String message = String.format(
                        "📍 Vị trí: %s\n" +
                        "🚗 Biển số: %s\n" +
                        "👤 Chủ xe: %s\n" +
                        "🕒 Giờ vào: %s",
                        slot.getSlotNumber(), 
                        info.get("plate"), 
                        info.get("owner") != null ? info.get("owner") : "Khách vãng lai",
                        info.get("timeIn")
                    );
                    JOptionPane.showMessageDialog(this, message, "Thông tin xe đang đỗ", JOptionPane.INFORMATION_MESSAGE);
                }
            } else if (slot.getStatus() == SlotStatus.EMPTY) {
                JOptionPane.showMessageDialog(this, "Ô đỗ này còn trống.");
            } else {
                JOptionPane.showMessageDialog(this, "Ô đỗ đang bảo trì.");
            }
        });

        return btn;
    }

    private JPanel createLegendItem(String text, Color bgColor, Color borderColor) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setOpaque(false);
        
        JLabel colorBox = new JLabel();
        colorBox.setPreferredSize(new Dimension(15, 15));
        colorBox.setOpaque(true);
        colorBox.setBackground(bgColor);
        colorBox.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        
        JLabel label = new JLabel(text);
        label.setFont(Theme.FONT_BODY);
        
        panel.add(colorBox);
        panel.add(label);
        return panel;
    }
}