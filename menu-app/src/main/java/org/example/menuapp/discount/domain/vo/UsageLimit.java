package org.example.menuapp.discount.domain.vo;

public record UsageLimit(
        Integer maxCount
) {

    public UsageLimit(Integer maxCount) {
        if (maxCount ==  null || maxCount <= 0) {
            throw new IllegalArgumentException("Usage limit must be greater than zero");
        }
        this.maxCount = maxCount;
    }

    public boolean isExceeded(Integer currentCount) {
        return maxCount != null && currentCount >= maxCount;
    }

    public boolean hasLimit() {
        return maxCount != null;
    }
}
