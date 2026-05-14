package com.parking.gui;

import com.parking.bll.ParkingRecordBLL;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class HistoryPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnSearch, btnRefresh;
    private ParkingRecordBLL bll;

    public HistoryPanel() {
        bll = new ParkingRecordBLL();
        initComponents();
        loadData("");
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Theme.BG_SECONDARY);
        setBorder(Theme.sectionPadding());

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Lịch Sử Giao Dịch Ra / Vào");
        lblTitle.setFont(Theme.FONT_HEADER);
        lblTitle.setForeground(Theme.TEXT_PRIMARY);
        topPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setOpaque(false);

        searchPanel.add(new JLabel("Tìm biển số:"));
        txtSearch = new JTextField(20);
        txtSearch.setFont(Theme.FONT_BODY);
        searchPanel.add(txtSearch);

        btnSearch = new JButton("Tìm Kiếm");
        styleButton(btnSearch, Theme.ACCENT_BLUE);
        searchPanel.add(btnSearch);

        btnRefresh = new JButton("Làm Mới");
        styleButton(btnRefresh, Theme.TEXT_MUTED);
        searchPanel.add(btnRefresh);

        topPanel.add(searchPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Biển Số", "Loại Xe", "Vị Trí Đỗ", "Giờ Vào", "Giờ Ra", "Phí (VNĐ)", "Nhân Viên", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(tableModel);
        table.setFont(Theme.FONT_BODY);
        table.setRowHeight(35);
        table.getTableHeader().setFont(Theme.FONT_TITLE);
        setupTableRenderer();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(Theme.cardBorder());
        add(scrollPane, BorderLayout.CENTER);

        btnSearch.addActionListener(e -> loadData(txtSearch.getText().trim()));
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadData("");
        });
    }

    private void setupTableRenderer() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.getColumnModel().getColumn(8).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (value != null) {
                    if (value.toString().equals("Trong bãi")) {
                        c.setForeground(new Color(0, 153, 51));
                        c.setFont(Theme.FONT_TITLE); 
                    } else {
                        if (!isSelected) {
                            c.setForeground(Theme.TEXT_PRIMARY); 
                        }
                        c.setFont(Theme.FONT_BODY);
                    }
                }
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });
    }

    private void loadData(String keyword) {
        tableModel.setRowCount(0);
        List<Map<String, Object>> list = bll.getAllHistory();
        
        for (Map<String, Object> row : list) {
            String plate = row.get("license_plate").toString();
            
            if (!keyword.isEmpty() && !plate.toLowerCase().contains(keyword.toLowerCase())) {
                continue;
            }

            tableModel.addRow(new Object[]{
                row.get("id"),
                plate,
                row.get("vehicle_type"),
                row.get("slot_number"),
                row.get("time_in"),
                row.get("time_out") != null ? row.get("time_out") : "---",
                String.format("%,.0f", row.get("fee")),
                row.get("staff_name"),
                row.get("status").toString().equals("COMPLETED") ? "Đã xong" : "Trong bãi"
            });
        }
    }

    private void styleButton(JButton btn, Color bg) {
    	btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setFont(Theme.FONT_TITLE);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 35));
    }
}