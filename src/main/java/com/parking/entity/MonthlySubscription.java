package com.parking.entity;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.parking.enums.SubscriptionStatus; 

public class MonthlySubscription {
    private Long id;
    private Long vehicleId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal amountPaid;
    private SubscriptionStatus status;
    public MonthlySubscription() {}

	public Long getId() {
		return id;
	}

	public SubscriptionStatus getStatus() {
		return status;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVehicleId() {
		return vehicleId;
	}

	public void setStatus(SubscriptionStatus status) {
		this.status = status;
	}

	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}


}