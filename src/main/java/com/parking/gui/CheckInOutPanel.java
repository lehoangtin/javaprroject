package com.parking.gui;

import com.parking.bll.ParkingRecordBLL;
import com.parking.entity.ParkingRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CheckInOutPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private ParkingRecordBLL bll = new ParkingRecordBLL();
    private Runnable onDataChanged; // Callback để báo MainFrame refresh sơ đồ xe

    public CheckInOutPanel(Runnable onDataChanged) {
        this.onDataChanged = onDataChanged;
        setLayout(new BorderLayout(16, 16));
        setBackground(Theme.BG_SECONDARY);
        setBorder(Theme.sectionPadding());

        // 1. PANEL BÊN TRÁI: FORM NHẬN XE
        add(buildCheckInForm(), BorderLayout.WEST);

        // 2. PANEL BÊN PHẢI: BẢNG DANH SÁCH XE ĐANG TRONG BÃI
        add(buildActiveRecordsTable(), BorderLayout.CENTER);

        loadData();
    }

    private JPanel buildCheckInForm() {
        JPanel pnlForm = new JPanel(new BorderLayout(0, 10));
        pnlForm.setBackground(Theme.BG_PRIMARY);
        pnlForm.setBorder(Theme.cardBorder());
        pnlForm.setPreferredSize(new Dimension(300, 0));

        JLabel lblTitle = new JLabel("🚗 Nhận Xe (Check-in)");
        lblTitle.setFont(Theme.FONT_HEADER);
        pnlForm.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlFields = new JPanel(new GridLayout(6, 1, 0, 10));
        pnlFields.setOpaque(false);

        JTextField txtVehicleId = new JTextField();
        JTextField txtSlotId = new JTextField();
        JTextField txtTicketId = new JTextField();

        pnlFields.add(new JLabel("Mã Xe (Vehicle ID):"));
        pnlFields.add(txtVehicleId);
        pnlFields.add(new JLabel("Mã Ô đỗ (Slot ID):"));
        pnlFields.add(txtSlotId);
        pnlFields.add(new JLabel("Mã Vé tháng (Để trống nếu vé lượt):"));
        pnlFields.add(txtTicketId);

        JButton btnCheckIn = new JButton("Xác nhận Cho Xe Vào");
        btnCheckIn.setBackground(Theme.ACCENT_GREEN);
        btnCheckIn.setForeground(Color.WHITE);
        btnCheckIn.setFont(Theme.FONT_BODY);
        btnCheckIn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnCheckIn.addActionListener(e -> {
            try {
                Long vId = Long.parseLong(txtVehicleId.getText().trim());
                Long sId = Long.parseLong(txtSlotId.getText().trim());
                Long tId = txtTicketId.getText().trim().isEmpty() ? null : Long.parseLong(txtTicketId.getText().trim());

                if (bll.processCheckIn(vId, sId, tId)) {
                    JOptionPane.showMessageDialog(this, "Nhận xe thành công!");
                    txtVehicleId.setText(""); txtSlotId.setText(""); txtTicketId.setText("");
                    loadData();
                    if (onDataChanged != null) onDataChanged.run(); // Cập nhật lại sơ đồ bãi xe
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID phải là số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        pnlForm.add(pnlFields, BorderLayout.CENTER);
        pnlForm.add(btnCheckIn, BorderLayout.SOUTH);
        return pnlForm;
    }

    private JPanel buildActiveRecordsTable() {
        JPanel pnlTable = new JPanel(new BorderLayout(0, 10));
        pnlTable.setBackground(Theme.BG_PRIMARY);
        pnlTable.setBorder(Theme.cardBorder());

        JLabel lblTitle = new JLabel("📋 Xe Đang Trong Bãi");
        lblTitle.setFont(Theme.FONT_HEADER);
        pnlTable.add(lblTitle, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID Giao dịch", "Mã Xe", "Mã Ô Đỗ", "Giờ Vào", "Trạng Thái"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        styleTable(table);
        pnlTable.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnCheckOut = new JButton("Thanh toán & Cho Xe Ra");
        btnCheckOut.setBackground(Theme.ACCENT_RED);
        btnCheckOut.setForeground(Color.WHITE);
        btnCheckOut.setFont(Theme.FONT_BODY);
        btnCheckOut.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCheckOut.setPreferredSize(new Dimension(200, 40));

        btnCheckOut.addActionListener(e -> handleCheckOut());

        JPanel pnlBot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBot.setOpaque(false);
        pnlBot.add(btnCheckOut);
        pnlTable.add(pnlBot, BorderLayout.SOUTH);

        return pnlTable;
    }

    private void handleCheckOut() {
        int row = table.getSelectedRow();
        if (row != -1) {
            Long recordId = (Long) model.getValueAt(row, 0);
            Long slotId = (Long) model.getValueAt(row, 2);

            // Bật Popup xác nhận tính tiền (Giả lập tiền phí là 5000)
            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận thu tiền và cho xe ra?", "Check-out", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                ParkingRecord record = new ParkingRecord();
                record.setId(recordId);
                record.setSlotId(slotId);

                // Tín có thể dùng PriceConfigBLL ở đây để tính phí thật nhé!
                BigDecimal fee = new BigDecimal("5000"); 

                if (bll.processCheckOut(record, fee)) {
                    JOptionPane.showMessageDialog(this, "Trả xe thành công! Thu: " + fee + " VND");
                    loadData();
                    if (onDataChanged != null) onDataChanged.run(); // Cập nhật lại sơ đồ bãi xe
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn xe cần Check-out!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void loadData() {
        model.setRowCount(0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        List<ParkingRecord> list = bll.getActiveRecords();
        for (ParkingRecord pr : list) {
            model.addRow(new Object[]{
                pr.getId(), pr.getVehicleId(), pr.getSlotId(), 
                pr.getTimeIn().format(dtf), pr.getStatus().name()
            });
        }
    }

    private void styleTable(JTable t) {
        t.setFont(Theme.FONT_BODY);
        t.setRowHeight(32);
        t.getTableHeader().setFont(Theme.FONT_SMALL);
        t.getTableHeader().setBackground(Theme.BG_SECONDARY);
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < t.getColumnCount(); i++) t.getColumnModel().getColumn(i).setCellRenderer(center);
    }
}