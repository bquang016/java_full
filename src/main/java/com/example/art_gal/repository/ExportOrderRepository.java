package com.example.art_gal.repository;

import com.example.art_gal.entity.ExportOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExportOrderRepository extends JpaRepository<ExportOrder, Long> {
    @Query("SELECT SUM(e.totalAmount) FROM ExportOrder e WHERE e.status = 'COMPLETED'")
    BigDecimal sumTotalRevenue();

    @Query("SELECT SUM(d.quantity * p.importPrice) FROM ExportOrder e JOIN e.orderDetails d JOIN d.painting p WHERE e.status = 'COMPLETED'")
    BigDecimal sumTotalCostOfGoodsSold();

    // SỬA LỖI Ở ĐÂY: Dùng CAST(... AS date) thay vì FUNCTION('DATE', ...)
    @Query("SELECT CAST(e.orderDate AS date), SUM(e.totalAmount) FROM ExportOrder e WHERE e.orderDate BETWEEN :startDate AND :endDate AND e.status = 'COMPLETED' GROUP BY CAST(e.orderDate AS date) ORDER BY CAST(e.orderDate AS date)")
    List<Object[]> findRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    // SỬA LỖI Ở ĐÂY: Dùng EXTRACT(YEAR FROM ...) và EXTRACT(MONTH FROM ...)
    @Query("SELECT EXTRACT(YEAR FROM e.orderDate), EXTRACT(MONTH FROM e.orderDate), SUM(e.totalAmount) FROM ExportOrder e WHERE EXTRACT(YEAR FROM e.orderDate) = :year AND e.status = 'COMPLETED' GROUP BY EXTRACT(YEAR FROM e.orderDate), EXTRACT(MONTH FROM e.orderDate) ORDER BY EXTRACT(MONTH FROM e.orderDate)")
    List<Object[]> findMonthlyRevenueByYear(int year);

    // SỬA LỖI Ở ĐÂY: Dùng EXTRACT(YEAR FROM ...)
    @Query("SELECT EXTRACT(YEAR FROM e.orderDate), SUM(e.totalAmount) FROM ExportOrder e WHERE e.status = 'COMPLETED' GROUP BY EXTRACT(YEAR FROM e.orderDate) ORDER BY EXTRACT(YEAR FROM e.orderDate)")
    List<Object[]> findYearlyRevenue();

    // SỬA LỖI Ở ĐÂY: Dùng EXTRACT(HOUR FROM ...)
    @Query("SELECT EXTRACT(HOUR FROM e.orderDate), SUM(e.totalAmount) FROM ExportOrder e WHERE e.orderDate BETWEEN :startDate AND :endDate AND e.status = 'COMPLETED' GROUP BY EXTRACT(HOUR FROM e.orderDate) ORDER BY EXTRACT(HOUR FROM e.orderDate)")
    List<Object[]> findHourlyRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    List<ExportOrder> findByCustomerId(Long customerId);

    @Query("SELECT e FROM ExportOrder e WHERE e.status = 'COMPLETED' AND CAST(e.orderDate AS date) BETWEEN :startDate AND :endDate ORDER BY e.orderDate ASC")
    List<ExportOrder> findCompletedOrdersByDateRange(LocalDate startDate, LocalDate endDate);

}