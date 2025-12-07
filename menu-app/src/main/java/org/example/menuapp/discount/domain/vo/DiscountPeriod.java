package org.example.menuapp.discount.domain.vo;

import java.time.LocalDateTime;

public record DiscountPeriod(
        LocalDateTime startDate,
        LocalDateTime endDate
) {
    public DiscountPeriod(
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        if (startDate == null) {
            throw new IllegalArgumentException("Start date and end date are required");
        }
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isActive(LocalDateTime now) {
        return !now.isBefore(startDate) && (endDate == null || !now.isAfter(endDate));
    }
}

