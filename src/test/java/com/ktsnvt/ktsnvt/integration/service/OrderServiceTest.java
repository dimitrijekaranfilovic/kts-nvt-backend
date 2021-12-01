package com.ktsnvt.ktsnvt.integration.service;


import com.ktsnvt.ktsnvt.exception.*;
import com.ktsnvt.ktsnvt.model.enums.OrderStatus;
import com.ktsnvt.ktsnvt.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import javax.transaction.Transactional;

@Transactional
@SpringBootTest
class OrderServiceTest {
    @Autowired
    private OrderServiceImpl orderService;

    @Test
    void getOrder_whenCalledWithValidId_isSuccess() {
        var order = orderService.getOrder(1);
        assertEquals(1, order.getId());
    }

    @Test
    void getOrder_whenCalledWithInvalidId_throwsException() {
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrder(15));
    }

    @Test
    void createOrder_whenCalledWithValidData_isSuccess() {
        var createdOrder = orderService.createOrder(2, "4321");
        assertTrue(createdOrder.getId() > 0);
        assertEquals(OrderStatus.CREATED, createdOrder.getStatus());
        assertEquals("4321", createdOrder.getWaiter().getPin());
    }

    @Test
    void createOrder_whenCalledWithBusyTable_throwsException() {
        assertThrows(OccupiedTableException.class, () -> orderService.createOrder(10, "4321"));
    }

    @Test
    void createGroupForOrder_withValidData_isSuccess(){
        Assertions.assertDoesNotThrow(() -> orderService.createGroupForOrder(3, "some group", "4321"));
    }

    @Test
    void createGroupForOrder_whenGroupNameIsTaken_throwsException(){
        Assertions.assertThrows(OrderItemGroupInvalidStatusException.class, () -> orderService.createGroupForOrder(3, "Group 3", "4321"));
    }

    @Test
    void createGroupForOrder_whenGroupStatusIsChargedOrCancelled(){
        Assertions.assertThrows(IllegalOrderStateException.class, () -> orderService.createGroupForOrder(1, "some group", "4321"));
    }


    @Test
    void createGroupForOrder_whenNotResponsibleWaiterTriesToCreate(){
        Assertions.assertThrows(InvalidEmployeeTypeException.class, () -> orderService.createGroupForOrder(3, "some group", "1234"));
    }


    @Test
    void deleteOrderItemGroup_withValidData_isSuccess(){
        Assertions.assertDoesNotThrow(() -> orderService.deleteOrderItemGroup(1, 2, "4321"));
    }

    @Test
    void deleteOrderItemGroup_whenGroupDoesNotExist_throwsException(){
        Assertions.assertThrows(NotFoundException.class, () -> orderService.deleteOrderItemGroup(1, 2222, "4321"));
    }

    @Test
    void deleteOrderItemGroup_whenGroupStatusIsNotNew_throwsException(){
        Assertions.assertThrows(OrderItemGroupInvalidStatusException.class, () -> orderService.deleteOrderItemGroup(1, 1, "4321"));

    }

    @Test
    void deleteOrderItemGroup_whenNotResponsibleEmployeeTriesToDelete_throwsException(){
        Assertions.assertThrows(InvalidEmployeeTypeException.class, () -> orderService.deleteOrderItemGroup(1, 2, "1234"));
    }

    @Test
    void sendOrderItemGroup_withValidData_isSuccess(){
        Assertions.assertDoesNotThrow(() -> orderService.sendOrderItemGroup(1, 2, "4321"));
    }

    @Test
    void sendOrderItemGroup_whenGroupDoesNotExist_throwsException(){
        Assertions.assertThrows(NotFoundException.class, () -> orderService.sendOrderItemGroup(1, 2222, "4321"));
    }

    @Test
    void sendOrderItemGroup_whenGroupStatusIsNotNew_throwsException(){
        Assertions.assertThrows(OrderItemGroupInvalidStatusException.class, () -> orderService.sendOrderItemGroup(1, 1, "4321"));

    }

    @Test
    void sendOrderItemGroup_whenNotResponsibleEmployeeTriesToDelete_throwsException(){
        Assertions.assertThrows(InvalidEmployeeTypeException.class, () -> orderService.sendOrderItemGroup(1, 2, "1234"));
    }


}
