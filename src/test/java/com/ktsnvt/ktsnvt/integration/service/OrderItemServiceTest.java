package com.ktsnvt.ktsnvt.integration.service;

import com.ktsnvt.ktsnvt.exception.*;
import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.model.InventoryItem;
import com.ktsnvt.ktsnvt.model.MenuItem;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import com.ktsnvt.ktsnvt.model.enums.OrderItemStatus;
import com.ktsnvt.ktsnvt.service.OrderItemService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.stream.Stream;

@SpringBootTest
public class OrderItemServiceTest {

    @Autowired
    private OrderItemService orderItemService;

    public static Stream<Arguments> provideFor_getAllItemRequests() {
        Pageable pageable = PageRequest.of(0, 1, Sort.unsorted());
        return Stream.of(
            Arguments.of(pageable, OrderItemStatus.SENT, ItemCategory.DRINK),
            Arguments.of(pageable, OrderItemStatus.SENT, ItemCategory.FOOD),
            Arguments.of(pageable, OrderItemStatus.PREPARING, ItemCategory.DRINK),
            Arguments.of(pageable, OrderItemStatus.PREPARING, ItemCategory.FOOD)
        );
    }

    public static Stream<Arguments> provideFor_finishItemRequest_success() {
        return Stream.of(
            Arguments.of(2, "1234"),
            Arguments.of(11, "5678")
        );
    }

    public static Stream<Arguments> provideFor_finishItemRequest_throws() {
        return Stream.of(
                Arguments.of(3, "1234"),
                Arguments.of(10, "5678"),
                Arguments.of(3, "4321")
        );
    }

    @ParameterizedTest
    @MethodSource("provideFor_getAllItemRequests")
    void getAllItemRequests_calledWithValidParams_isSuccess(Pageable pageable, OrderItemStatus status, ItemCategory category) {
        assertTrue(orderItemService.getAllItemRequests(pageable, status, category).getTotalElements() >= 1);
    }

    @Test
    void takeItemRequest_calledWithInvalidOrderId_throwsOrderItemNotFoundException() {
        assertThrows(OrderItemNotFoundException.class, () -> orderItemService.takeItemRequest(0, "1234"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"4321", "5678"})
    void takeItemRequest_calledWithWrongEmployeeType_throwsInvalidEmployeeTypeException(String employeePin) {
        assertThrows(InvalidEmployeeTypeException.class, () -> orderItemService.takeItemRequest(1, employeePin));
    }

    @ParameterizedTest
    @MethodSource("provideFor_finishItemRequest_success")
    void finishItemRequest_calledWithValidData_isSuccess(Integer itemId, String employeePin) {
        assertDoesNotThrow(() -> orderItemService.finishItemRequest(itemId, employeePin));
    }

    @ParameterizedTest
    @MethodSource("provideFor_finishItemRequest_throws")
    void finishItemRequest_calledWithInvalidEmployeeType_throwsInvalidEmployeeTypeException(Integer itemId, String employeePin) {
        assertThrows(InvalidEmployeeTypeException.class, () -> orderItemService.finishItemRequest(itemId, employeePin));
    }

    @Test
    void hasActiveOrderItems_calledWithValidEmployee_isSuccess() {
        Employee employee1 = new Employee();
        employee1.setId(1);
        Employee employee2 = new Employee();
        employee2.setId(2);
        assertTrue(orderItemService.hasActiveOrderItems(employee1));
        assertFalse(orderItemService.hasActiveOrderItems(employee2));
    }

    @Test
    void hasActiveOrderItems_calledWithValidMenuItem_isSuccess() {
        MenuItem menuItem1 = new MenuItem();
        menuItem1.setId(1);
        MenuItem menuItem2 = new MenuItem();
        menuItem2.setId(4);
        assertTrue(orderItemService.hasActiveOrderItems(menuItem1));
        assertFalse(orderItemService.hasActiveOrderItems(menuItem2));
    }

    @Test
    void hasActiveOrderItems_calledWithValidInventoryItem_isSuccess() {
        InventoryItem inventoryItem1 = new InventoryItem();
        inventoryItem1.setId(1);
        InventoryItem inventoryItem2 = new InventoryItem();
        inventoryItem2.setId(4);
        assertTrue(orderItemService.hasActiveOrderItems(inventoryItem1));
        assertFalse(orderItemService.hasActiveOrderItems(inventoryItem2));
    }

    @Test
    void addOrderItem_withValidData_isSuccess(){
        assertDoesNotThrow(() -> orderItemService.addOrderItem(2, 1, 2, "4321"));
    }

    @Test
    void addOrderItem_whenGroupDoesNotExist_throwsException(){
        assertThrows(NotFoundException.class, () -> orderItemService.addOrderItem(2222, 1, 2, "4321"));
    }

    @Test
    void addOrderItem_whenMenuItemDoesNotExist_throwsException(){
        assertThrows(NotFoundException.class, () -> orderItemService.addOrderItem(2, 2222, 2, "4321"));
    }

    @Test
    void addOrderItem_whenGroupStatusIsNotNew_throwsException(){
        assertThrows(OrderItemGroupInvalidStatusException.class, () -> orderItemService.addOrderItem(1, 1, 2, "4321"));
    }

    @Test
    void addOrderItem_whenAmountIsNotPositive_throwsException(){
        assertThrows(IllegalAmountException.class, () -> orderItemService.addOrderItem(2, 1, -2, "4321"));
    }

    @Test
    void addOrderItem_whenNotResponsibleEmployeeTriesToAdd_throwsException(){
        assertThrows(InvalidEmployeeTypeException.class, () -> orderItemService.addOrderItem(2, 1, 2, "1234"));
    }

    @Test
    void updateOrderItem_withValidData_isSuccess(){
        assertDoesNotThrow(() -> orderItemService.updateOrderItem(12, 2, "4321"));
    }

    @Test
    void updateOrderItem_whenItemDoesNotExist_throwsException(){
        assertThrows(NotFoundException.class, () -> orderItemService.updateOrderItem(222, 2, "4321"));
    }

    @Test
    void updateOrderItem_whenItemStatusIsNotNew_throwsException(){
        assertThrows(OrderItemInvalidStatusException.class , () -> orderItemService.updateOrderItem(10, 2, "4321"));
    }

    @Test
    void updateOrderItem_whenAmountIsNotPositive_throwsException(){
        assertThrows(IllegalAmountException.class , () -> orderItemService.updateOrderItem(12, -2, "4321"));
    }

    @Test
    void updateOrderItem_whenNotResponsibleEmployeeTriesToUpdate_throwsException(){
        assertThrows(InvalidEmployeeTypeException.class , () -> orderItemService.updateOrderItem(12, 2, "1234"));

    }

    @Test
    void deleteOrderItem_withValidData_isSuccess(){
        assertDoesNotThrow(() -> orderItemService.deleteOrderItem(12,  "4321"));
    }

    @Test
    void delete_whenItemDoesNotExist_throwsException(){
        assertThrows(NotFoundException.class, () -> orderItemService.deleteOrderItem(222, "4321"));
    }

    @Test
    void deleteOrderItem_whenItemStatusIsNotNew_throwsException(){
        assertThrows(OrderItemInvalidStatusException.class , () -> orderItemService.deleteOrderItem(10, "4321"));
    }

    @Test
    void deleteOrderItem_whenNotResponsibleEmployeeTriesToDelete_throwsException(){
        assertThrows(InvalidEmployeeTypeException.class , () -> orderItemService.deleteOrderItem(12,  "1234"));

    }



}
