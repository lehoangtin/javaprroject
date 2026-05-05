package com.parking.gui;

import com.parking.bll.PriceConfigBLL;
import com.parking.entity.PriceConfig;
import com.parking.gui.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class PriceConfigPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private PriceConfigBLL bll = new PriceConfigBLL();

    public PriceConfigPanel() {
        setLayout(new BorderLayout(0, 16));
        setBackground(Theme.BG_SECONDARY);
        setBorder(Theme.sectionPadding());

        JPanel card = new JPanel(new BorderLayout(0, 10));
        card.setBackground(Theme.BG_PRIMARY);
        card.setBorder(Theme.cardBorder());

        // --- Header ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setOpaque(false);
        JLabel lblTitle = new JLabel("Cấu hình Biểu giá");
        lblTitle.setFont(Theme.FONT_HEADER);
        lblTitle.setForeground(Theme.TEXT_PRIMARY);
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        card.add(pnlHeader, BorderLayout.NORTH);

        // --- Bảng dữ liệu ---
        model = new DefaultTableModel(new String[]{"ID", "Loại Xe", "Giá Gốc (₫)", "Giá Thêm/h (₫)", "Giá Tháng (₫)"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        styleTable(table);
        card.add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Footer Buttons ---
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlFooter.setOpaque(false);

        JButton btnAdd = createFlatButton("Thêm mới", Theme.ACCENT_BLUE);
        JButton btnEdit = createFlatButton("Sửa mức giá", Theme.ACCENT_AMBER);
        JButton btnDelete = createFlatButton("Xóa biểu giá", Theme.ACCENT_RED);

        btnAdd.addActionListener(e -> showConfigDialog(null));
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
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa cấu hình này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (bll.deleteConfig(id)) loadData();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn cấu hình cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row != -1) {
            PriceConfig c = new PriceConfig();
            c.setId((Long) model.getValueAt(row, 0));
            c.setVehicleType((String) model.getValueAt(row, 1));
            c.setBaseFee((BigDecimal) model.getValueAt(row, 2));
            c.setExtraFeePerHour((BigDecimal) model.getValueAt(row, 3));
            c.setMonthlyPrice((BigDecimal) model.getValueAt(row, 4));
            showConfigDialog(c);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn cấu hình cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    // --- Popup Dialog nhập liệu ---
    private void showConfigDialog(PriceConfig config) {
        boolean isAdd = (config == null);
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), isAdd ? "Thêm Biểu Giá Mới" : "Sửa Cấu Hình Giá", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel pnlForm = new JPanel(new GridLayout(4, 2, 10, 15));
        pnlForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlForm.setBackground(Color.WHITE);

        JComboBox<String> cbType = new JComboBox<>(new String[]{"CAR", "MOTORCYCLE"});
        JTextField txtBase = new JTextField(isAdd ? "0" : config.getBaseFee().toString());
        JTextField txtExtra = new JTextField(isAdd ? "0" : config.getExtraFeePerHour().toString());
        JTextField txtMonth = new JTextField(isAdd ? "0" : config.getMonthlyPrice().toString());

        if (!isAdd) {
            cbType.setSelectedItem(config.getVehicleType());
            cbType.setEnabled(false); // Không cho đổi loại xe khi đang sửa giá
        }

        pnlForm.add(new JLabel("Loại xe:")); pnlForm.add(cbType);
        pnlForm.add(new JLabel("Giá cơ bản (VND):")); pnlForm.add(txtBase);
        pnlForm.add(new JLabel("Giá thêm mỗi giờ:")); pnlForm.add(txtExtra);
        pnlForm.add(new JLabel("Giá vé tháng:")); pnlForm.add(txtMonth);

        JButton btnSave = createFlatButton("Lưu cấu hình", Theme.ACCENT_GREEN);
        btnSave.addActionListener(e -> {
            try {
                PriceConfig c = isAdd ? new PriceConfig() : config;
                c.setVehicleType((String) cbType.getSelectedItem());
                
                // Parse dữ liệu từ Textfield sang BigDecimal
                c.setBaseFee(new BigDecimal(txtBase.getText().trim()));
                c.setExtraFeePerHour(new BigDecimal(txtExtra.getText().trim()));
                c.setMonthlyPrice(new BigDecimal(txtMonth.getText().trim()));

                if (bll.saveConfig(c)) {
                    loadData();
                    dialog.dispose();
                    JOptionPane.showMessageDialog(this, "Lưu thành công!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đúng định dạng số!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
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
        List<PriceConfig> list = bll.getAllConfigs();
        for (PriceConfig c : list) {
            model.addRow(new Object[]{c.getId(), c.getVehicleType(), c.getBaseFee(), c.getExtraFeePerHour(), c.getMonthlyPrice()});
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