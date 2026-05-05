package com.parking.entity;
import com.parking.enums.RecordStatus; //[cite: 17]
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ParkingRecord {
    private Long id;
    private LocalDateTime timeIn;
    private LocalDateTime timeOut;
    private BigDecimal fee;
    private Long vehicleId;
    private Long slotId;
    private Long ticketId;
    private RecordStatus status; // Đã đổi từ String sang Enum[cite: 17]
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDateTime getTimeIn() {
		return timeIn;
	}
	public void setTimeIn(LocalDateTime timeIn) {
		this.timeIn = timeIn;
	}
	public LocalDateTime getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(LocalDateTime timeOut) {
		this.timeOut = timeOut;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	public Long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public Long getSlotId() {
		return slotId;
	}
	public void setSlotId(Long slotId) {
		this.slotId = slotId;
	}
	public Long getTicketId() {
		return ticketId;
	}
	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}
	public RecordStatus getStatus() {
		return status;
	}
	public void setStatus(RecordStatus status) {
		this.status = status;
	}

}