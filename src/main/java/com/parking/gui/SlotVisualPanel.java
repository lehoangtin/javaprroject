package com.parking.gui;

import com.parking.bll.FloorBLL;
import com.parking.bll.SlotBLL;
import com.parking.entity.Floor;
import com.parking.entity.Slot;
import com.parking.enums.SlotStatus;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class SlotVisualPanel extends JPanel {
    private JTabbedPane tabbedPane;
    private FloorBLL floorBLL;
    private SlotBLL slotBLL;

    public SlotVisualPanel() {
        floorBLL = new FloorBLL();
        slotBLL = new SlotBLL();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Theme.BG_SECONDARY);
        setBorder(Theme.sectionPadding());

        // Tiêu đề
        JLabel lblTitle = new JLabel("Sơ Đồ Bãi Đỗ Xe");
        lblTitle.setFont(Theme.FONT_HEADER);
        lblTitle.setForeground(Theme.TEXT_PRIMARY);
        add(lblTitle, BorderLayout.NORTH);

        // Tab chứa các tầng
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(Theme.FONT_TITLE);
        add(tabbedPane, BorderLayout.CENTER);

        // Chú thích (Legend) ở dưới cùng
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        legendPanel.setBackground(Theme.BG_SECONDARY);
        legendPanel.add(createLegendItem("Trống", Theme.SLOT_EMPTY, Theme.SLOT_BORDER_EMPTY));
        legendPanel.add(createLegendItem("Đang đỗ", Theme.SLOT_TAKEN, Theme.SLOT_BORDER_TAKEN));
        add(legendPanel, BorderLayout.SOUTH);

        // Nạp dữ liệu vẽ sơ đồ
        loadMap();
    }

    // Hàm này sẽ vẽ toàn bộ sơ đồ
    public void loadMap() {
        tabbedPane.removeAll(); // Xóa các tab cũ đi để vẽ lại
        List<Floor> floors = floorBLL.getAllFloors();

        for (Floor floor : floors) {
            // Tạo 1 Panel cho mỗi tầng
            // Dùng FlowLayout để các ô xe tự rớt dòng khi hết chỗ (Wrap)
            JPanel floorMapPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
            floorMapPanel.setBackground(Theme.BG_PRIMARY);
            floorMapPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Lấy danh sách chỗ đỗ của tầng này
            List<Slot> slots = slotBLL.getSlotsByFloor(floor.getId());
            
            for (Slot slot : slots) {
                // Tạo một ô đỗ xe (Nút bấm)
                JButton btnSlot = createSlotButton(slot);
                floorMapPanel.add(btnSlot);
            }

            // Gắn Panel tầng này vào Tab
            // Ví dụ: Tầng 1, Tầng 2
         // Hiển thị trực tiếp "Tầng 1", "Tầng 2"...
            String tabTitle = "Tầng " + floor.getFloorNumber();
             if (floor.getDescription() != null && !floor.getDescription().trim().isEmpty()) {
                 tabTitle += " (" + floor.getDescription() + ")";
             }
                               
            // Thêm thanh cuộn đề phòng tầng có quá nhiều xe
            JScrollPane scrollPane = new JScrollPane(floorMapPanel);
            scrollPane.setBorder(null);
            
            tabbedPane.addTab(tabTitle, scrollPane);
        }
    }

    // Hàm tạo giao diện cho từng ô xe (Dựa theo thiết kế trong ảnh)
    private JButton createSlotButton(Slot slot) {
        JButton btn = new JButton("<html><center>" + slot.getSlotNumber() + "<br><br><span style='font-size:8px;'>" + slot.getVehicleType() + "</span></center></html>");
        btn.setPreferredSize(new Dimension(80, 100)); // Kích thước ô xe (Hình chữ nhật đứng)
        btn.setFont(Theme.FONT_BODY);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);

        // Xét trạng thái màu sắc dựa vào DB
        if (slot.getStatus() == SlotStatus.EMPTY) {
            btn.setBackground(Theme.SLOT_EMPTY);
            btn.setForeground(Theme.TEXT_PRIMARY);
            btn.setBorder(BorderFactory.createLineBorder(Theme.SLOT_BORDER_EMPTY, 2));
        } else if (slot.getStatus() == SlotStatus.OCCUPIED) {
            btn.setBackground(Theme.SLOT_TAKEN);
            btn.setForeground(Theme.TEXT_PRIMARY);
            btn.setBorder(BorderFactory.createLineBorder(Theme.SLOT_BORDER_TAKEN, 2));
        } else {
            // Trạng thái bảo trì / khóa
            btn.setBackground(Color.LIGHT_GRAY);
            btn.setForeground(Color.DARK_GRAY);
            btn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        }

        // Sự kiện khi click vào ô xe
        btn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Bạn vừa chọn ô: " + slot.getSlotNumber());
            // TODO: Mở form Nhận xe hoặc Trả xe tại đây
        });

        return btn;
    }

    // Tạo chú thích màu
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