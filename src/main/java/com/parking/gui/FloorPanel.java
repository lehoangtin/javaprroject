package com.parking.gui;

import com.parking.bll.FloorBLL;
import com.parking.entity.Floor;
import com.parking.gui.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FloorPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private FloorBLL bll = new FloorBLL();

    public FloorPanel() {
        setLayout(new BorderLayout(0, 16));
        setBackground(Theme.BG_SECONDARY);
        setBorder(Theme.sectionPadding());

        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(Theme.BG_PRIMARY);
        card.setBorder(Theme.cardBorder());

        // --- Header ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        JLabel lblTitle = new JLabel("Quản lý Tầng Bãi Xe");
        lblTitle.setFont(Theme.FONT_HEADER);
        lblTitle.setForeground(Theme.TEXT_PRIMARY);
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        card.add(pnlHeader, BorderLayout.NORTH);

        // --- Table ---
        model = new DefaultTableModel(new String[]{"ID Tầng", "Mã Bãi Xe", "Số Tầng", "Tổng Số Chỗ", "Mô Tả"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        styleTable(table);
        card.add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Footer Buttons ---
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlFooter.setOpaque(false);

        JButton btnAdd = createFlatButton("Thêm tầng", Theme.ACCENT_BLUE);
        JButton btnEdit = createFlatButton("Sửa thông tin", Theme.ACCENT_AMBER);
        JButton btnDelete = createFlatButton("Xóa tầng", Theme.ACCENT_RED);

        btnAdd.addActionListener(e -> showFloorDialog(null));
        btnEdit.addActionListener(e -> handleEdit());
        btnDelete.addActionListener(e -> handleDelete());

        pnlFooter.add(btnAdd);
        pnlFooter.add(btnEdit);
        pnlFooter.add(btnDelete);
        
        card.add(pnlFooter, BorderLayout.SOUTH);
        add(card, BorderLayout.CENTER);

        loadData();
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row != -1) {
            Long id = (Long) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa tầng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (bll.deleteFloor(id)) loadData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tầng cần xóa!");
        }
    }

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row != -1) {
            Floor f = new Floor();
            f.setId((Long) model.getValueAt(row, 0));
            f.setLotId((Long) model.getValueAt(row, 1));
            f.setFloorNumber((Integer) model.getValueAt(row, 2));
            f.setTotalSlots((Integer) model.getValueAt(row, 3));
            f.setDescription((String) model.getValueAt(row, 4));
            showFloorDialog(f);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tầng cần sửa!");
        }
    }

    private void showFloorDialog(Floor floor) {
        boolean isAdd = (floor == null);
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), isAdd ? "Thêm Tầng Mới" : "Sửa Tầng", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel pnlForm = new JPanel(new GridLayout(4, 2, 10, 15));
        pnlForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlForm.setBackground(Color.WHITE);

        JTextField txtLotId = new JTextField(isAdd ? "" : floor.getLotId().toString());
        JTextField txtFloorNum = new JTextField(isAdd ? "" : floor.getFloorNumber().toString());
        JTextField txtTotalSlots = new JTextField(isAdd ? "" : floor.getTotalSlots().toString());
        JTextField txtDesc = new JTextField(isAdd ? "" : (floor.getDescription() == null ? "" : floor.getDescription()));

        pnlForm.add(new JLabel("Mã Bãi Xe (Lot ID):")); pnlForm.add(txtLotId);
        pnlForm.add(new JLabel("Tầng số (VD: 1, 2):")); pnlForm.add(txtFloorNum);
        pnlForm.add(new JLabel("Tổng Số Chỗ Đỗ:")); pnlForm.add(txtTotalSlots);
        pnlForm.add(new JLabel("Mô Tả (Tùy chọn):")); pnlForm.add(txtDesc);

        JButton btnSave = createFlatButton("Lưu thông tin", Theme.ACCENT_GREEN);
        btnSave.addActionListener(e -> {
            try {
                Floor f = isAdd ? new Floor() : floor;
                f.setLotId(Long.parseLong(txtLotId.getText().trim()));
                f.setFloorNumber(Integer.parseInt(txtFloorNum.getText().trim()));
                f.setTotalSlots(Integer.parseInt(txtTotalSlots.getText().trim()));
                f.setDescription(txtDesc.getText().trim());

                if (bll.saveFloor(f)) {
                    loadData();
                    dialog.dispose();
                    JOptionPane.showMessageDialog(this, "Lưu thành công!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đúng định dạng số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel pnlBot = new JPanel();
        pnlBot.setBackground(Color.WHITE);
        pnlBot.add(btnSave);

        dialog.add(pnlForm, BorderLayout.CENTER);
        dialog.add(pnlBot, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void loadData() {
        model.setRowCount(0);
        List<Floor> list = bll.getAllFloors();
        for (Floor f : list) {
            model.addRow(new Object[]{ f.getId(), f.getLotId(), f.getFloorNumber(), f.getTotalSlots(), f.getDescription() });
        }
    }

    private void styleTable(JTable t) {
        t.setFont(Theme.FONT_BODY);
        t.setRowHeight(32);
        t.getTableHeader().setFont(Theme.FONT_SMALL);
        t.getTableHeader().setBackground(Theme.BG_SECONDARY);
        t.setGridColor(Theme.BORDER);
        t.setSelectionBackground(new Color(230, 241, 251));
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < t.getColumnCount(); i++) t.getColumnModel().getColumn(i).setCellRenderer(center);
    }

    private JButton createFlatButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.FONT_BODY);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 36));
        return btn;
    }
}