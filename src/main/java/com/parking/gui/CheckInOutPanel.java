package com.parking.gui;

import com.parking.enums.VehicleType;
import com.parking.enums.SlotStatus;
import javax.swing.*;
import java.awt.*;

public class CheckInOutPanel extends JPanel {
    private JTextField txtInLicensePlate;
    private JComboBox<VehicleType> cbInVehicleType;
    private JComboBox<String> cbInSlot;
    private JButton btnCheckIn;
    private JLabel lblInStatus;

    private JTextField txtOutSearchPlate;
    private JButton btnSearch;
    private JLabel lblOutInfo;
    private JButton btnCheckOut;
    private JLabel lblOutStatus;

    public CheckInOutPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Theme.BG_SECONDARY); 
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel mainContent = new JPanel(new GridLayout(1, 2, 20, 0));
        mainContent.setOpaque(false);

        mainContent.add(createCheckInForm());
        mainContent.add(createCheckOutForm());

        add(mainContent, BorderLayout.CENTER);

        btnCheckIn.addActionListener(e -> processCheckIn());
        btnSearch.addActionListener(e -> processSearchCheckOut());
        btnCheckOut.addActionListener(e -> processConfirmCheckOut());
        
        loadEmptySlots();
    }
    
    private JPanel createCheckInForm() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JLabel lblTitle = new JLabel("Xe vào");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.25;
        formPanel.add(new JLabel("Biển số xe *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.75;
        txtInLicensePlate = new JTextField();
        txtInLicensePlate.setPreferredSize(new Dimension(0, 30));
        txtInLicensePlate.setFont(Theme.FONT_BODY);
        formPanel.add(txtInLicensePlate, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.25;
        formPanel.add(new JLabel("Loại xe"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.75;
        cbInVehicleType = new JComboBox<>(VehicleType.values());
        cbInVehicleType.setPreferredSize(new Dimension(0, 30));
        cbInVehicleType.setFont(Theme.FONT_BODY);
        formPanel.add(cbInVehicleType, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.25;
        formPanel.add(new JLabel("Vị trí đỗ"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.75;
        cbInSlot = new JComboBox<>(new String[]{"Chọn chỗ trống"});
        cbInSlot.setPreferredSize(new Dimension(0, 30));
        cbInSlot.setFont(Theme.FONT_BODY);
        formPanel.add(cbInSlot, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 15, 5, 15);
        btnCheckIn = new JButton("Ghi nhận xe vào");
        btnCheckIn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnCheckIn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCheckIn.setPreferredSize(new Dimension(0, 35));
        formPanel.add(btnCheckIn, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(5, 15, 20, 15);
        lblInStatus = new JLabel(" ");
        lblInStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblInStatus, gbc);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(formPanel, BorderLayout.NORTH); // Ép form dính chặt lên trên

        panel.add(wrapper, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCheckOutForm() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JLabel lblTitle = new JLabel("Xe ra");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.25;
        formPanel.add(new JLabel("Biển số xe *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.75;
        txtOutSearchPlate = new JTextField();
        txtOutSearchPlate.setPreferredSize(new Dimension(0, 30));
        txtOutSearchPlate.setFont(Theme.FONT_BODY);
        formPanel.add(txtOutSearchPlate, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 15, 15, 15);
        btnSearch = new JButton("Tra cứu thông tin");
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSearch.setPreferredSize(new Dimension(0, 35));
        formPanel.add(btnSearch, gbc);

        gbc.gridy = 2;
        lblOutInfo = new JLabel("<html><i>Nhập biển số và nhấn Tra cứu...</i></html>");
        lblOutInfo.setFont(Theme.FONT_BODY);
        lblOutInfo.setForeground(Color.DARK_GRAY);
        lblOutInfo.setPreferredSize(new Dimension(0, 100)); 
        lblOutInfo.setVerticalAlignment(SwingConstants.TOP); // Ép chữ dính lên trên
        formPanel.add(lblOutInfo, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(10, 15, 5, 15);
        btnCheckOut = new JButton("Thanh toán & Ra xe");
        btnCheckOut.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btnCheckOut.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCheckOut.setPreferredSize(new Dimension(0, 35));
        btnCheckOut.setEnabled(false); 
        formPanel.add(btnCheckOut, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(5, 15, 20, 15);
        lblOutStatus = new JLabel(" ");
        lblOutStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(lblOutStatus, gbc);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.add(formPanel, BorderLayout.NORTH);

        panel.add(wrapper, BorderLayout.CENTER);
        return panel;
    }

    private void processCheckIn() {
        String bienSo = txtInLicensePlate.getText().trim();
        com.parking.enums.VehicleType loaiXe = (com.parking.enums.VehicleType) cbInVehicleType.getSelectedItem();
        
        if (bienSo.isEmpty()) {
            showInError("Biển số xe không được để trống!");
            return;
        }

        if (cbInSlot.getSelectedIndex() <= 0) {
            showInError("Vui lòng chọn một chỗ đỗ!");
            return;
        }

        String selectedSlotStr = cbInSlot.getSelectedItem().toString();
        Long slotId = Long.parseLong(selectedSlotStr.split(" - ")[0]);

        com.parking.bll.ParkingRecordBLL recordBLL = new com.parking.bll.ParkingRecordBLL();
        String result = recordBLL.checkIn(bienSo, loaiXe, slotId);

        if ("SUCCESS".equals(result)) {
            lblInStatus.setForeground(new Color(39, 174, 96)); 
            lblInStatus.setText("[Thành công] Xe " + bienSo + " đã vào ô " + selectedSlotStr);
            
            txtInLicensePlate.setText("");
            cbInVehicleType.setSelectedIndex(0);
            loadEmptySlots(); 
        } else {
            showInError(result);
        }
    }

    private com.parking.entity.ParkingRecord currentOutRecord = null;

    private void processSearchCheckOut() {
        String bienSo = txtOutSearchPlate.getText().trim();
        if(bienSo.isEmpty()) {
            showOutError("Vui lòng nhập biển số xe!");
            return;
        }
        
        try {
            com.parking.bll.ParkingRecordBLL recordBLL = new com.parking.bll.ParkingRecordBLL();       
            currentOutRecord = recordBLL.calculateInfo(bienSo);
            
            String tenViTriDo = "Không xác định";
            com.parking.bll.SlotBLL slotBLL = new com.parking.bll.SlotBLL();
            for (com.parking.entity.Slot slot : slotBLL.getAllSlots()) {
                if (slot.getId().equals(currentOutRecord.getSlotId())) {
                    tenViTriDo = slot.getSlotNumber(); 
                    break;
                }
            }
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy");
            long durationMinutes = java.time.Duration.between(currentOutRecord.getTimeIn(), currentOutRecord.getTimeOut()).toMinutes();
            
            java.text.DecimalFormat df = new java.text.DecimalFormat("#,### VNĐ");
            String feeText = currentOutRecord.getFee().compareTo(java.math.BigDecimal.ZERO) == 0 ? "0 đ (Vé tháng)" : df.format(currentOutRecord.getFee());

            lblOutInfo.setText("<html><b>Giờ vào:</b> " + currentOutRecord.getTimeIn().format(formatter) +
            				   "<br><b>Vị trí đỗ:</b> " + tenViTriDo +
                               "<br><b>Thời gian đỗ:</b> " + (durationMinutes / 60) + " giờ " + (durationMinutes % 60) + " phút" +
                               "<br><b>Số tiền cần thu:</b> <span style='color:red; font-size:14px;'>" + feeText + "</span></html>");
            
            lblOutStatus.setText(" "); 
            btnCheckOut.setEnabled(true); 
            
        } catch (Exception ex) {
            showOutError(ex.getMessage());
            btnCheckOut.setEnabled(false);
        }
    }

    private void processConfirmCheckOut() {
    	
        if (currentOutRecord != null) {
        	com.parking.bll.ParkingRecordBLL recordBLL = new com.parking.bll.ParkingRecordBLL();
        	if (recordBLL.confirmCheckOut(currentOutRecord)) {
        		java.text.DecimalFormat df = new java.text.DecimalFormat("#,### đ");
            
        		lblOutStatus.setForeground(new Color(39, 174, 96)); 
        		lblOutStatus.setText("[Hoàn tất] Xe " + txtOutSearchPlate.getText().trim() + " đã ra. Thu: " + df.format(currentOutRecord.getFee()));
            
        		txtOutSearchPlate.setText("");
        		lblOutInfo.setText("<html><i>Nhập biển số và nhấn Tra cứu...</i></html>");
        		btnCheckOut.setEnabled(false);
            
        		currentOutRecord = null;
        		loadEmptySlots(); 
        	} else {
        		showOutError("Lỗi hệ thống khi xác nhận xe ra!");
        	}
        }
    }

    public void loadEmptySlots() {
        cbInSlot.removeAllItems(); 
        cbInSlot.addItem("Chọn chỗ trống"); 
        
        com.parking.bll.SlotBLL slotBLL = new com.parking.bll.SlotBLL();
        java.util.List<com.parking.entity.Slot> allSlots = slotBLL.getAllSlots();
        
        for (com.parking.entity.Slot slot : allSlots) {
            if (slot.getStatus() == com.parking.enums.SlotStatus.EMPTY) {
                cbInSlot.addItem(slot.getSlotNumber());
            }
        }
    }

    private void showInError(String msg) {
        lblInStatus.setForeground(Color.RED);
        lblInStatus.setText("[Lỗi] " + msg);
    }
    
    private void showOutError(String msg) {
        lblOutStatus.setForeground(Color.RED);
        lblOutStatus.setText("[Lỗi] " + msg);
        lblOutInfo.setText("<html><i>Nhập biển số và nhấn Tra cứu...</i></html>");
    }
}