package com.parking.bll;

import com.parking.dao.PriceConfigDAO;
import com.parking.entity.PriceConfig;
import com.parking.enums.VehicleType;
import java.math.BigDecimal;
import java.util.List;

public class PriceConfigBLL {
    private PriceConfigDAO priceConfigDAO;

    public PriceConfigBLL() {
        this.priceConfigDAO = new PriceConfigDAO();
    }

    public List<PriceConfig> getAllConfigs() {
        return priceConfigDAO.getAllPriceConfigs();
    }

    private boolean isValidPrice(BigDecimal price) {
        return price != null && price.compareTo(BigDecimal.ZERO) >= 0;
    }

    public boolean addConfig(VehicleType vehicleType, BigDecimal baseFee, BigDecimal extraFee, BigDecimal monthlyPrice) {
    	// KIỂM TRA RÀNG BUỘC SỐ ÂM
        if (baseFee.compareTo(BigDecimal.ZERO) < 0 || 
            extraFee.compareTo(BigDecimal.ZERO) < 0 || 
            monthlyPrice.compareTo(BigDecimal.ZERO) < 0) {
            System.err.println("Lỗi nghiệp vụ: Giá tiền không được là số âm!");
            return false;
        }
        PriceConfig config = new PriceConfig();
        config.setVehicleType(vehicleType);
        config.setBaseFee(baseFee);
        config.setExtraFeePerHour(extraFee);
        config.setMonthlyPrice(monthlyPrice);
        
        return priceConfigDAO.addPriceConfig(config);
    }

    public boolean updateConfig(Long id, VehicleType vehicleType, BigDecimal baseFee, BigDecimal extraFee, BigDecimal monthlyPrice) {
    	if (baseFee.compareTo(BigDecimal.ZERO) < 0 || 
                extraFee.compareTo(BigDecimal.ZERO) < 0 || 
                monthlyPrice.compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }
        PriceConfig config = new PriceConfig();
        config.setId(id);
        config.setVehicleType(vehicleType);
        config.setBaseFee(baseFee);
        config.setExtraFeePerHour(extraFee);
        config.setMonthlyPrice(monthlyPrice);
        
        return priceConfigDAO.updatePriceConfig(config);
    }

    public boolean deleteConfig(Long id) {
        return priceConfigDAO.deletePriceConfig(id);
    }
 // Viết sẵn hàm này cho Phi tính tiền
    public PriceConfig getConfigByVehicleType(VehicleType type) {
        return priceConfigDAO.getConfigByType(type); 
    }
}