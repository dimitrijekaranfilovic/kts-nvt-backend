package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.repository.OrderRepository;
import com.ktsnvt.ktsnvt.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean hasAssignedActiveOrders(Employee employee) {
        return orderRepository.streamAssignedActiveOrdersForEmployee(employee.getId()).findAny().isPresent();
    }
}
