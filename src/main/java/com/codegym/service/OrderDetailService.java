package com.codegym.service;

import com.codegym.model.Order;
import com.codegym.model.OrderDetail;
import com.codegym.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;


    public List<OrderDetail> findByOrder(Order order) {
        return orderDetailRepository.findByOrder(order);
    }


    public OrderDetail save(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }


    public void delete(Long id) {
        orderDetailRepository.deleteById(id);
    }
}
