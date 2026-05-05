package com.parking.entity;
import com.parking.enums.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Ticket {
    private Long id;
    private String rfidCode;
    private TicketType type;     // MONTHLY hoặc GUEST[cite: 20]
    private TicketStatus status; // ACTIVE, INACTIVE, LOST[cite: 19]
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal price;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRfidCode() {
		return rfidCode;
	}
	public void setRfidCode(String rfidCode) {
		this.rfidCode = rfidCode;
	}
	public TicketType getType() {
		return type;
	}
	public void setType(TicketType type) {
		this.type = type;
	}
	public TicketStatus getStatus() {
		return status;
	}
	public void setStatus(TicketStatus status) {
		this.status = status;
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
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}