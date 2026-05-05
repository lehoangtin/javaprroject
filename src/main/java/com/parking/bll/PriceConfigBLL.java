package com.parking.bll;

import com.parking.dao.PriceConfigDAO;
import com.parking.entity.PriceConfig;
import java.math.BigDecimal;
import java.util.List;

public class PriceConfigBLL {
    private PriceConfigDAO priceDAO = new PriceConfigDAO();

    public List<PriceConfig> getAllConfigs() {
        return priceDAO.getAll();
    }

    public boolean saveConfig(PriceConfig config) {
        // Kiểm tra logic: phí không được âm
        if (config.getBaseFee().compareTo(BigDecimal.ZERO) < 0 || 
            config.getExtraFeePerHour().compareTo(BigDecimal.ZERO) < 0 ||
            config.getMonthlyPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Giá tiền không được là số âm!");
        }
        
        // Nếu ID null hoặc bằng 0 thì là Thêm mới, ngược lại là Cập nhật
        if (config.getId() == null || config.getId() == 0) {
            return priceDAO.insert(config);
        } else {
            return priceDAO.update(config);
        }
    }

    public boolean deleteConfig(Long id) {
        if (id == null) throw new IllegalArgumentException("Không tìm thấy cấu hình cần xóa!");
        return priceDAO.delete(id);
    }
}