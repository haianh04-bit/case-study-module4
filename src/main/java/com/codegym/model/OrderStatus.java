package com.codegym.model;

public enum OrderStatus {
    PENDING,      // chờ xử lý
    PROCESSING,   // đang xử lý
    SHIPPED,      // đã giao cho đơn vị vận chuyển
    DELIVERED,    // đã giao thành công
    CANCELED      // đã hủy
}
