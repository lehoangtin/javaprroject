package com.parking.gui;

import com.parking.bll.ParkingLotBLL;
import com.parking.entity.ParkingInfo;
import com.parking.gui.Theme;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ParkingLotPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private ParkingLotBLL bll = new ParkingLotBLL();

    public ParkingLotPanel() {
        setLayout(new BorderLayout(0, 16));
        setBackground(Theme.BG_SECONDARY);
        setBorder(Theme.sectionPadding());

        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(Theme.BG_PRIMARY);
        card.setBorder(Theme.cardBorder());

        // --- Header ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        JLabel lblTitle = new JLabel("Quản lý Cơ sở Bãi Đỗ Xe");
        lblTitle.setFont(Theme.FONT_HEADER);
        lblTitle.setForeground(Theme.TEXT_PRIMARY);
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        card.add(pnlHeader, BorderLayout.NORTH);

        // --- Bảng dữ liệu ---
        model = new DefaultTableModel(new String[]{"ID", "Tên Bãi Xe", "Địa Chỉ", "SĐT Liên Hệ", "Tổng Số Tầng", "Ngày Tạo"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        styleTable(table);
        card.add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Footer Buttons ---
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlFooter.setOpaque(false);

        JButton btnAdd = createFlatButton("Thêm bãi xe", Theme.ACCENT_BLUE);
        JButton btnEdit = createFlatButton("Sửa thông tin", Theme.ACCENT_AMBER);
        JButton btnDelete = createFlatButton("Xóa bãi xe", Theme.ACCENT_RED);

        btnAdd.addActionListener(e -> showLotDialog(null));
        btnEdit.addActionListener(e -> handleEdit());
        btnDelete.addActionListener(e -> handleDelete());

        pnlFooter.add(btnAdd);
        pnlFooter.add(btnEdit);
        pnlFooter.add(btnDelete);
        
        card.add(pnlFooter, BorderLayout.SOUTH);
        add(card, BorderLayout.CENTER);

        loadData();
    }

    // ================= XỬ LÝ SỰ KIỆN =================

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row != -1) {
            Long id = (Long) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa bãi xe này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (bll.deleteParkingLot(id)) loadData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Không thể xóa bãi xe do đang có dữ liệu liên kết (Tầng/Ô đỗ)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bãi xe cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row != -1) {
            ParkingInfo p = new ParkingInfo();
            p.setId((Long) model.getValueAt(row, 0));
            p.setName((String) model.getValueAt(row, 1));
            p.setAddress((String) model.getValueAt(row, 2));
            p.setPhone((String) model.getValueAt(row, 3));
            
            Object floorsObj = model.getValueAt(row, 4);
            if (floorsObj != null && !floorsObj.toString().isEmpty()) {
                p.setTotalFloors(Integer.parseInt(floorsObj.toString()));
            }
            showLotDialog(p);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bãi xe cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    // --- Popup Dialog nhập liệu ---
    private void showLotDialog(ParkingInfo lot) {
        boolean isAdd = (lot == null);
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), isAdd ? "Thêm Bãi Xe Mới" : "Sửa Thông Tin Bãi Xe", true);
        dialog.setSize(400, 320);
        dialog.setLocationRelativeTo(this);
        
        JPanel pnlForm = new JPanel(new GridLayout(4, 2, 10, 15));
        pnlForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlForm.setBackground(Color.WHITE);

        JTextField txtName = new JTextField(isAdd ? "" : lot.getName());
        JTextField txtAddress = new JTextField(isAdd ? "" : lot.getAddress());
        JTextField txtPhone = new JTextField(isAdd ? "" : (lot.getPhone() == null ? "" : lot.getPhone()));
        JTextField txtFloors = new JTextField(isAdd ? "" : (lot.getTotalFloors() == null ? "" : lot.getTotalFloors().toString()));

        pnlForm.add(new JLabel("Tên Bãi Xe (*):")); pnlForm.add(txtName);
        pnlForm.add(new JLabel("Địa Chỉ:")); pnlForm.add(txtAddress);
        pnlForm.add(new JLabel("SĐT Liên Hệ:")); pnlForm.add(txtPhone);
        pnlForm.add(new JLabel("Tổng Số Tầng:")); pnlForm.add(txtFloors);

        JButton btnSave = createFlatButton("Lưu thông tin", Theme.ACCENT_GREEN);
        btnSave.addActionListener(e -> {
            try {
                ParkingInfo p = isAdd ? new ParkingInfo() : lot;
                p.setName(txtName.getText().trim());
                p.setAddress(txtAddress.getText().trim());
                p.setPhone(txtPhone.getText().trim());
                
                String floorsStr = txtFloors.getText().trim();
                if (!floorsStr.isEmpty()) {
                    p.setTotalFloors(Integer.parseInt(floorsStr));
                } else {
                    p.setTotalFloors(null);
                }

                if (bll.saveParkingLot(p)) {
                    loadData();
                    dialog.dispose();
                    JOptionPane.showMessageDialog(this, "Lưu thành công!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Tổng số tầng phải là một số hợp lệ!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
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

    // ================= TIỆN ÍCH UI =================

    private void loadData() {
        model.setRowCount(0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        List<ParkingInfo> list = bll.getAllParkingLots();
        for (ParkingInfo p : list) {
            String createdAt = p.getCreatedAt() != null ? p.getCreatedAt().format(dtf) : "";
            model.addRow(new Object[]{
                p.getId(), p.getName(), p.getAddress(), p.getPhone(), p.getTotalFloors(), createdAt
            });
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