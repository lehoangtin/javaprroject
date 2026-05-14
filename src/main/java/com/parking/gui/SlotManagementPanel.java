package com.parking.gui;

import com.parking.bll.SlotBLL;
import com.parking.entity.Slot;
import com.parking.enums.SlotStatus;
import com.parking.enums.VehicleType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SlotManagementPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtFloorId, txtSlotNumber;
    private JComboBox<VehicleType> cbVehicleType;
    private JComboBox<SlotStatus> cbStatus;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    
    private SlotBLL bll = new SlotBLL();
    private Long selectedId = null;

    public SlotManagementPanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setBackground(Theme.BG_SECONDARY);
        setBorder(Theme.sectionPadding());
        setLayout(new BorderLayout(15, 15));

        JLabel lblTitle = new JLabel("Quản Lý Sơ Đồ Chỗ Đỗ (Slots)");
        lblTitle.setFont(Theme.FONT_HEADER);
        lblTitle.setForeground(Theme.TEXT_PRIMARY);
        add(lblTitle, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Mã Tầng", "Số Hiệu", "Loại Xe", "Trạng Thái"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(Theme.FONT_BODY);
        table.setRowHeight(30);
        table.getTableHeader().setFont(Theme.FONT_TITLE);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(Theme.BG_PRIMARY);
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
                Theme.cardBorder(), BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JPanel formPanel = new JPanel(new GridLayout(2, 4, 15, 15));
        formPanel.setBackground(Theme.BG_PRIMARY);

        formPanel.add(new JLabel("Mã Tầng (Floor ID):"));
        txtFloorId = new JTextField();
        formPanel.add(txtFloorId);

        formPanel.add(new JLabel("Số hiệu (VD: A-01):"));
        txtSlotNumber = new JTextField();
        formPanel.add(txtSlotNumber);

        formPanel.add(new JLabel("Loại xe:"));
        cbVehicleType = new JComboBox<>(VehicleType.values());
        formPanel.add(cbVehicleType);

        formPanel.add(new JLabel("Trạng thái:"));
        cbStatus = new JComboBox<>(SlotStatus.values());
        
        cbStatus.setRenderer(new DefaultListCellRenderer() {
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof com.parking.enums.SlotStatus) {
                    setText(((com.parking.enums.SlotStatus) value).getDisplayName()); // Gọi getDisplayName()
                }
                return this;
            }
        });
        
        formPanel.add(cbStatus);

        bottomPanel.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(Theme.BG_PRIMARY);
        
        btnAdd = new JButton("Thêm");
        btnAdd.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnAdd.setFont(Theme.FONT_TITLE);
        btnAdd.setBackground(Theme.ACCENT_TEAL);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false); 
        btnAdd.setOpaque(true);         
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setPreferredSize(new Dimension(130, 35));

        btnUpdate = new JButton("Sửa");
        btnUpdate.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnUpdate.setFont(Theme.FONT_TITLE);
        btnUpdate.setBackground(Theme.ACCENT_BLUE);
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFocusPainted(false);
        btnUpdate.setBorderPainted(false);
        btnUpdate.setOpaque(true);
        btnUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnUpdate.setPreferredSize(new Dimension(130, 35));

        btnDelete = new JButton("Xóa");
        btnDelete.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnDelete.setFont(Theme.FONT_TITLE);
        btnDelete.setBackground(Theme.ACCENT_RED);
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.setBorderPainted(false);
        btnDelete.setOpaque(true);
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.setPreferredSize(new Dimension(130, 35));

        btnClear = new JButton("Làm mới");
        btnClear.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnClear.setFont(Theme.FONT_TITLE);
        btnClear.setBackground(Theme.TEXT_MUTED); 
        btnClear.setForeground(Color.WHITE);
        btnClear.setFocusPainted(false);
        btnClear.setBorderPainted(false);
        btnClear.setOpaque(true);
        btnClear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClear.setPreferredSize(new Dimension(130, 35));

        btnPanel.add(btnAdd); btnPanel.add(btnUpdate); btnPanel.add(btnDelete); btnPanel.add(btnClear);
        bottomPanel.add(btnPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (table.getSelectedRow() >= 0) {
                int r = table.getSelectedRow();
                selectedId = (Long) tableModel.getValueAt(r, 0);
                txtFloorId.setText(tableModel.getValueAt(r, 1).toString());
                txtSlotNumber.setText(tableModel.getValueAt(r, 2).toString());
                cbVehicleType.setSelectedItem(VehicleType.valueOf(tableModel.getValueAt(r, 3).toString()));
                
                // Cập nhật cách đọc Enum khi click vào bảng
                String statusStr = tableModel.getValueAt(r, 4).toString();
                for (SlotStatus status : SlotStatus.values()) {
                    if (status.getDisplayName().equals(statusStr)) {
                        cbStatus.setSelectedItem(status);
                        break;
                    }
                }
            }
        });

        btnAdd.addActionListener(e -> {
            try {
                String result = bll.addSlot(Long.parseLong(txtFloorId.getText().trim()), txtSlotNumber.getText().trim(), 
                                            (VehicleType) cbVehicleType.getSelectedItem(), (SlotStatus) cbStatus.getSelectedItem());
                if ("SUCCESS".equals(result)) {
                    loadData(); clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, result, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Mã tầng phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnUpdate.addActionListener(e -> {
            if (selectedId == null) return;
            try {
                String result = bll.updateSlot(selectedId, Long.parseLong(txtFloorId.getText().trim()), txtSlotNumber.getText().trim(), 
                                               (VehicleType) cbVehicleType.getSelectedItem(), (SlotStatus) cbStatus.getSelectedItem());
                if ("SUCCESS".equals(result)) {
                    loadData(); clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, result, "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Mã tầng phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            if (selectedId != null && bll.deleteSlot(selectedId)) {
                loadData(); clearForm();
            }
        });

        btnClear.addActionListener(e -> clearForm());
    }

    private void loadData() {
        tableModel.setRowCount(0);
        for (Slot s : bll.getAllSlots()) {
            tableModel.addRow(new Object[]{s.getId(), s.getFloorId(), s.getSlotNumber(), s.getVehicleType(), s.getStatus().getDisplayName()});
        }
    }

    private void clearForm() {
        selectedId = null; table.clearSelection();
        txtFloorId.setText(""); txtSlotNumber.setText("");
        cbVehicleType.setSelectedIndex(0); cbStatus.setSelectedIndex(0);
    }
}