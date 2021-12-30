package com.ktsnvt.ktsnvt.integration.service;


import com.ktsnvt.ktsnvt.exception.*;
import com.ktsnvt.ktsnvt.model.enums.OrderStatus;
import com.ktsnvt.ktsnvt.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.*;

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
    void chargeOrder_whenCalledWithValidOrder_isSuccess() {
        assertDoesNotThrow(() -> orderService.chargeOrder(3, "4321"));
    }

    @Test
    void chargeOrder_whenCalledWithEmptyOrder_isSuccess() {
        assertDoesNotThrow(() -> orderService.chargeOrder(8, "4321"));
    }

    @Test
    void chargeOrder_whenCalledWithNotInProgressOrder_throwsException() {
        assertThrows(IllegalOrderStateException.class, () -> orderService.chargeOrder(2, "4321"));
    }

    @Test
    void chargeOrder_whenCalledWithNotFinishedOrder_throwsException() {
        assertThrows(IllegalOrderStateException.class, () -> orderService.chargeOrder(6, "4321"));
    }

    @Test
    void cancelOrder_whenCalledWithValidOrder_isSuccess() {
        assertDoesNotThrow(() -> orderService.cancelOrder(2, "4321"));
    }

    @Test
    void cancelOrder_whenCalledWithChargedOrder_throwsException() {
        assertThrows(IllegalOrderStateException.class, () -> orderService.cancelOrder(1, "4321"));
    }

    @Test
    void cancelOrder_whenCalledWithCancelledOrder_throwsException() {
        assertThrows(IllegalOrderStateException.class, () -> orderService.cancelOrder(7, "4321"));
    }

    @Test
    void cancelOrder_whenCalledWithStartedOrder_throwsException() {
        assertThrows(IllegalOrderStateException.class, () -> orderService.cancelOrder(6, "4321"));
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

    @Test
    void getOrderIdForTable_withValidData_isSuccess(){
        Assertions.assertEquals(9, orderService.getOrderIdForTable(2));
    }

    @Test
    void getOrderIdForTable_whenOrderDoesNotExist_returnsMinusOne() {
        Assertions.assertNull(orderService.getOrderIdForTable(800));
    }





}
