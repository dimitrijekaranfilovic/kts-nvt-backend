package com.ktsnvt.ktsnvt.unit.service;

import com.ktsnvt.ktsnvt.exception.IllegalAmountException;
import com.ktsnvt.ktsnvt.exception.InvalidEmployeeTypeException;
import com.ktsnvt.ktsnvt.exception.NotFoundException;
import com.ktsnvt.ktsnvt.exception.OrderItemInvalidStatusException;
import com.ktsnvt.ktsnvt.model.*;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import com.ktsnvt.ktsnvt.model.enums.OrderItemGroupStatus;
import com.ktsnvt.ktsnvt.model.enums.OrderItemStatus;
import com.ktsnvt.ktsnvt.repository.MenuItemRepository;
import com.ktsnvt.ktsnvt.repository.OrderItemGroupRepository;
import com.ktsnvt.ktsnvt.repository.OrderItemRepository;
import com.ktsnvt.ktsnvt.service.EmployeeOrderService;
import com.ktsnvt.ktsnvt.service.EmployeeQueryService;
import com.ktsnvt.ktsnvt.service.LocalDateTimeService;
import com.ktsnvt.ktsnvt.service.impl.OrderItemServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderItemServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderItemGroupRepository orderItemGroupRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private LocalDateTimeService dateTimeService;

    @Mock
    private EmployeeQueryService employeeQueryService;

    @Mock
    private EmployeeOrderService employeeOrderService;

    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    @Test
    public void takeItemRequest_calledWithValidParams_isSuccess() {
        // GIVEN
        Employee employee = new Employee();
        employee.setId(1);
        OrderItem orderItem = new OrderItem();
        orderItem.setId(2);

        LocalDateTime takenAt = LocalDateTime.of(2021, 1, 1, 1, 1);

        // WHEN
        doReturn(employee).when(employeeQueryService).findByPin(anyString());
        doReturn(Optional.of(orderItem)).when(orderItemRepository).findOneByIdWithItemReference(orderItem.getId());
        doNothing().when(employeeOrderService).throwIfNotValidEmployeeType(employee, orderItem);
        doReturn(takenAt).when(dateTimeService).currentTime();

        orderItemService.takeItemRequest(orderItem.getId(), "");

        //THEN
        assertEquals(employee, orderItem.getPreparedBy());
        assertEquals(OrderItemStatus.PREPARING, orderItem.getStatus());
        assertEquals(takenAt, orderItem.getTakenAt());
        verify(orderItemRepository, times(1)).save(orderItem);
    }

    @Test
    public void finishItemRequest_calledOnSentItem_IsSuccess() {
        //GIVEN
        LocalDateTime takenAt = LocalDateTime.of(2021, 1, 1, 1, 1);
        Employee employee = new Employee();
        employee.setId(1);
        employee.setPin("0000");
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1);
        OrderItemGroup group = new OrderItemGroup();
        group.addItem(orderItem);

        // WHEN
        doReturn(employee).when(employeeQueryService).findByPin(employee.getPin());
        doReturn(Optional.of(orderItem)).when(orderItemRepository).findOneInProgressByIdWithItemReference(orderItem.getId());
        doNothing().when(employeeOrderService).throwIfNotValidEmployeeType(employee, orderItem);
        doReturn(group.getOrderItems()).when(orderItemRepository).getAllFromOneGroup(orderItem.getOrderItemGroup().getId());
        doReturn(takenAt).when(dateTimeService).currentTime();

        orderItemService.finishItemRequest(orderItem.getId(), employee.getPin());

        // THEN
        assertEquals(OrderItemStatus.DONE, orderItem.getStatus());
        assertEquals(takenAt, orderItem.getTakenAt());
        assertEquals(takenAt, orderItem.getPreparedAt());
        assertEquals(employee, orderItem.getPreparedBy());
        assertEquals(OrderItemGroupStatus.DONE, group.getStatus());
        verify(orderItemRepository, times(1)).save(orderItem);
        verify(orderItemGroupRepository, times(1)).save(group);
    }

    @Test
    public void finishItemRequest_calledOnTakenItem_IsSuccess() {
        // GIVEN
        LocalDateTime takenAt = LocalDateTime.of(2021, 1, 1, 1, 1);
        Employee employee = new Employee();
        employee.setId(1);
        employee.setPin("0000");
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1);
        orderItem.setPreparedBy(employee);
        OrderItemGroup group = new OrderItemGroup();
        group.addItem(orderItem);

        // WHEN
        doReturn(employee).when(employeeQueryService).findByPin(employee.getPin());
        doReturn(Optional.of(orderItem)).when(orderItemRepository).findOneInProgressByIdWithItemReference(orderItem.getId());
        doNothing().when(employeeOrderService).throwIfNotValidEmployeeType(employee, orderItem);
        doReturn(group.getOrderItems()).when(orderItemRepository).getAllFromOneGroup(orderItem.getOrderItemGroup().getId());
        doReturn(takenAt).when(dateTimeService).currentTime();

        orderItemService.finishItemRequest(orderItem.getId(), employee.getPin());

        // THEN
        assertEquals(OrderItemStatus.DONE, orderItem.getStatus());
        assertEquals(takenAt, orderItem.getPreparedAt());
        assertEquals(employee, orderItem.getPreparedBy());
        assertEquals(OrderItemGroupStatus.DONE, group.getStatus());
        verify(orderItemRepository, times(1)).save(orderItem);
        verify(orderItemGroupRepository, times(1)).save(group);
    }

    @Test
    public void finishItemRequest_calledAsNotLastInGroup_DoesNotUpdateGroup() {
        // GIVEN
        LocalDateTime takenAt = LocalDateTime.of(2021, 1, 1, 1, 1);
        Employee employee = new Employee();
        employee.setId(1);
        employee.setPin("0000");
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1);
        orderItem.setPreparedBy(employee);
        OrderItemGroup group = new OrderItemGroup();
        group.setStatus(OrderItemGroupStatus.SENT);
        group.addItem(orderItem);

        // WHEN
        doReturn(employee).when(employeeQueryService).findByPin(employee.getPin());
        doReturn(Optional.of(orderItem)).when(orderItemRepository).findOneInProgressByIdWithItemReference(orderItem.getId());
        doNothing().when(employeeOrderService).throwIfNotValidEmployeeType(employee, orderItem);
        doReturn(new ArrayList<OrderItem>()).when(orderItemRepository).getAllFromOneGroup(orderItem.getOrderItemGroup().getId());
        doReturn(takenAt).when(dateTimeService).currentTime();

        orderItemService.finishItemRequest(orderItem.getId(), employee.getPin());

        // THEN
        assertEquals(OrderItemGroupStatus.SENT, group.getStatus());
        verify(orderItemGroupRepository, times(0)).save(group);
    }

    @Test
    public void finishItemRequest_takenByOtherEmployee_throwsInvalidEmployeeTypeException() {
        // GIVEN
        Employee employee1 = new Employee();
        employee1.setId(1);
        employee1.setPin("0000");
        Employee employee2 = new Employee();
        employee2.setId(2);
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1);
        orderItem.setPreparedBy(employee2);

        // WHEN
        doReturn(employee1).when(employeeQueryService).findByPin(employee1.getPin());
        doReturn(Optional.of(orderItem)).when(orderItemRepository).findOneInProgressByIdWithItemReference(orderItem.getId());

        // THEN
        assertThrows(InvalidEmployeeTypeException.class, () -> orderItemService.finishItemRequest(orderItem.getId(), employee1.getPin()));

    }


    @Test
    void addOrderItem_withValidData_isSuccess(){
        //data setup
        var order = new Order();
        order.setId(1);

        var waiter = new Employee();
        waiter.setType(EmployeeType.WAITER);
        waiter.setId(1);
        waiter.setPin("1234");

        order.setWaiter(waiter);

        var orderItemGroup = new OrderItemGroup();
        orderItemGroup.setId(1);
        orderItemGroup.setName("group 1");
        orderItemGroup.setStatus(OrderItemGroupStatus.NEW);
        orderItemGroup.setOrder(order);

        var menuItem = new MenuItem();
        menuItem.setId(1);
        menuItem.setPrice(BigDecimal.ZERO);

        var inventoryItem = new InventoryItem();
        inventoryItem.setId(1);
        inventoryItem.setCurrentBasePrice(BigDecimal.ZERO);

        menuItem.setItem(inventoryItem);

        var orderItem = new OrderItem();
        orderItem.setOrderItemGroup(orderItemGroup);
        orderItem.setId(1);

        //set up mock and spy objects behaviour
        Mockito.doReturn(Optional.of(orderItemGroup)).when(orderItemGroupRepository).findById(1);
        Mockito.doReturn(orderItem).when(orderItemRepository).save(orderItem);
        Mockito.doReturn(Optional.of(menuItem)).when(menuItemRepository).findById(1);


        Assertions.assertDoesNotThrow(() -> orderItemService.addOrderItem(order.getId(), menuItem.getId(), 1, waiter.getPin()));
    }


    @Test
    void addOrderItem_whenGroupDoesNotExist_throwsException(){
        //data setup
        var order = new Order();
        order.setId(1);

        var waiter = new Employee();
        waiter.setType(EmployeeType.WAITER);
        waiter.setId(1);
        waiter.setPin("1234");

        order.setWaiter(waiter);

        var orderItemGroup = new OrderItemGroup();
        orderItemGroup.setId(1);
        orderItemGroup.setName("group 1");
        orderItemGroup.setStatus(OrderItemGroupStatus.NEW);
        orderItemGroup.setOrder(order);

        var menuItem = new MenuItem();
        menuItem.setId(1);
        menuItem.setPrice(BigDecimal.ZERO);

        var inventoryItem = new InventoryItem();
        inventoryItem.setId(1);
        inventoryItem.setCurrentBasePrice(BigDecimal.ZERO);

        menuItem.setItem(inventoryItem);

        var orderItem = new OrderItem();
        orderItem.setOrderItemGroup(orderItemGroup);
        orderItem.setId(1);

        //set up mock and spy objects behaviour
        Mockito.doReturn(Optional.empty()).when(orderItemGroupRepository).findById(1);
        Mockito.doReturn(orderItem).when(orderItemRepository).save(orderItem);
        Mockito.doReturn(Optional.of(menuItem)).when(menuItemRepository).findById(1);

        Assertions.assertThrows(NotFoundException.class, () -> orderItemService.addOrderItem(order.getId(), menuItem.getId(), 1, waiter.getPin()));
    }


    @Test
    void addOrderItem_whenMenuItemDoesNotExist_throwsException(){
        //data setup
        var order = new Order();
        order.setId(1);

        var waiter = new Employee();
        waiter.setType(EmployeeType.WAITER);
        waiter.setId(1);
        waiter.setPin("1234");

        order.setWaiter(waiter);

        var orderItemGroup = new OrderItemGroup();
        orderItemGroup.setId(1);
        orderItemGroup.setName("group 1");
        orderItemGroup.setStatus(OrderItemGroupStatus.NEW);
        orderItemGroup.setOrder(order);

        var menuItem = new MenuItem();
        menuItem.setId(1);
        menuItem.setPrice(BigDecimal.ZERO);

        var inventoryItem = new InventoryItem();
        inventoryItem.setId(1);
        inventoryItem.setCurrentBasePrice(BigDecimal.ZERO);

        menuItem.setItem(inventoryItem);

        var orderItem = new OrderItem();
        orderItem.setOrderItemGroup(orderItemGroup);
        orderItem.setId(1);

        //set up mock and spy objects behaviour
        Mockito.doReturn(Optional.of(orderItemGroup)).when(orderItemGroupRepository).findById(1);
        Mockito.doReturn(orderItem).when(orderItemRepository).save(orderItem);
        Mockito.doReturn(Optional.empty()).when(menuItemRepository).findById(1);

        Assertions.assertThrows(NotFoundException.class, () -> orderItemService.addOrderItem(order.getId(), menuItem.getId(), 1, waiter.getPin()));

    }


    @Test
    void addOrderItem_whenAmountIsNotPositive_throwsException(){
        //data setup
        var order = new Order();
        order.setId(1);

        var waiter = new Employee();
        waiter.setType(EmployeeType.WAITER);
        waiter.setId(1);
        waiter.setPin("1234");

        order.setWaiter(waiter);

        var orderItemGroup = new OrderItemGroup();
        orderItemGroup.setId(1);
        orderItemGroup.setName("group 1");
        orderItemGroup.setStatus(OrderItemGroupStatus.NEW);
        orderItemGroup.setOrder(order);

        var menuItem = new MenuItem();
        menuItem.setId(1);
        menuItem.setPrice(BigDecimal.ZERO);

        var inventoryItem = new InventoryItem();
        inventoryItem.setId(1);
        inventoryItem.setCurrentBasePrice(BigDecimal.ZERO);

        menuItem.setItem(inventoryItem);

        var orderItem = new OrderItem();
        orderItem.setOrderItemGroup(orderItemGroup);
        orderItem.setId(1);

        var invalidAmount = -1;

        //set up mock and spy objects behaviour
        Mockito.doReturn(Optional.of(orderItemGroup)).when(orderItemGroupRepository).findById(1);
        Mockito.doReturn(orderItem).when(orderItemRepository).save(orderItem);
        Mockito.doReturn(Optional.of(menuItem)).when(menuItemRepository).findById(1);

        Assertions.assertThrows(IllegalAmountException.class, () -> orderItemService.addOrderItem(order.getId(), menuItem.getId(), invalidAmount, waiter.getPin()));
    }


    @Test
    void addOrderItem_whenNotResponsibleEmployeeTriesToAdd_throwsException(){
        //data setup
        var order = new Order();
        order.setId(1);

        var waiter = new Employee();
        waiter.setType(EmployeeType.WAITER);
        waiter.setId(1);
        waiter.setPin("1234");

        order.setWaiter(waiter);

        var orderItemGroup = new OrderItemGroup();
        orderItemGroup.setId(1);
        orderItemGroup.setName("group 1");
        orderItemGroup.setStatus(OrderItemGroupStatus.NEW);
        orderItemGroup.setOrder(order);

        var menuItem = new MenuItem();
        menuItem.setId(1);
        menuItem.setPrice(BigDecimal.ZERO);

        var inventoryItem = new InventoryItem();
        inventoryItem.setId(1);
        inventoryItem.setCurrentBasePrice(BigDecimal.ZERO);

        menuItem.setItem(inventoryItem);

        var orderItem = new OrderItem();
        orderItem.setOrderItemGroup(orderItemGroup);
        orderItem.setId(1);

        var invalidPin = "1111";

        //set up mock and spy objects behaviour
        Mockito.doReturn(Optional.of(orderItemGroup)).when(orderItemGroupRepository).findById(1);
        Mockito.doReturn(orderItem).when(orderItemRepository).save(orderItem);
        Mockito.doReturn(Optional.of(menuItem)).when(menuItemRepository).findById(1);
        Mockito.doThrow(new InvalidEmployeeTypeException(invalidPin)).when(employeeOrderService).throwIfWaiterNotResponsible(invalidPin, waiter.getId());

        Assertions.assertThrows(InvalidEmployeeTypeException.class, () -> orderItemService.addOrderItem(order.getId(), menuItem.getId(), 1, invalidPin));
    }


    @Test
    void updateOrderItem_withValidData_isSuccess(){
        //data setup
        var waiter = new Employee();
        waiter.setPin("1234");
        waiter.setType(EmployeeType.WAITER);

        var order = new Order();
        order.setId(1);
        order.setWaiter(waiter);

        var orderItemGroup = new OrderItemGroup();
        orderItemGroup.setOrder(order);
        orderItemGroup.setId(1);



        var orderItem = new OrderItem();
        orderItem.setId(1);
        orderItem.setStatus(OrderItemStatus.NEW);
        orderItem.setAmount(1);
        orderItem.setOrderItemGroup(orderItemGroup);

        //mock and spy
        Mockito.doReturn(Optional.of(orderItem)).when(orderItemRepository).findById(1);

        //method call and assertion
        orderItemService.updateOrderItem(orderItem.getId(), 2, waiter.getPin());
        assertEquals(2, orderItem.getAmount());

    }


    @Test
    void updateOrderItem_whenOrderItemDoesNotExist_throwsException(){
        //data setup
        var waiter = new Employee();
        waiter.setPin("1234");
        waiter.setType(EmployeeType.WAITER);

        var order = new Order();
        order.setId(1);
        order.setWaiter(waiter);

        var orderItemGroup = new OrderItemGroup();
        orderItemGroup.setOrder(order);
        orderItemGroup.setId(1);



        var orderItem = new OrderItem();
        orderItem.setId(1);
        orderItem.setStatus(OrderItemStatus.NEW);
        orderItem.setAmount(1);
        orderItem.setOrderItemGroup(orderItemGroup);

        //mock and spy
        Mockito.doReturn(Optional.empty()).when(orderItemRepository).findById(1);

        //method call and assertion
        assertThrows(NotFoundException.class, () -> orderItemService.updateOrderItem(orderItem.getId(), 2, waiter.getPin()));
    }

    @Test
    void updateOrderItem_whenItemStatusIsNotNew_throwsException(){
        //data setup
        var waiter = new Employee();
        waiter.setPin("1234");
        waiter.setType(EmployeeType.WAITER);

        var order = new Order();
        order.setId(1);
        order.setWaiter(waiter);

        var orderItemGroup = new OrderItemGroup();
        orderItemGroup.setOrder(order);
        orderItemGroup.setId(1);

        var orderItem = new OrderItem();
        orderItem.setId(1);
        orderItem.setStatus(OrderItemStatus.SENT);
        orderItem.setAmount(1);
        orderItem.setOrderItemGroup(orderItemGroup);

        //mock and spy
        Mockito.doReturn(Optional.of(orderItem)).when(orderItemRepository).findById(1);

        //method call and assertion
        assertThrows(OrderItemInvalidStatusException.class, () -> orderItemService.updateOrderItem(orderItem.getId(), 2, waiter.getPin()));
    }

    @Test
    void updateOrderItem_whenNotResponsibleEmployeeTriesToUpdate_throwsException(){
        //data setup
        var waiter = new Employee();
        waiter.setPin("1234");
        waiter.setType(EmployeeType.WAITER);

        var order = new Order();
        order.setId(1);
        order.setWaiter(waiter);

        var orderItemGroup = new OrderItemGroup();
        orderItemGroup.setOrder(order);
        orderItemGroup.setId(1);

        var orderItem = new OrderItem();
        orderItem.setId(1);
        orderItem.setStatus(OrderItemStatus.NEW);
        orderItem.setAmount(1);
        orderItem.setOrderItemGroup(orderItemGroup);

        var invalidPin = "1111";

        //mock and spy
        Mockito.doReturn(Optional.of(orderItem)).when(orderItemRepository).findById(1);
        Mockito.doThrow(new InvalidEmployeeTypeException(invalidPin)).when(employeeOrderService).throwIfWaiterNotResponsible(invalidPin, waiter.getId());

        //method call and assertion
        assertThrows(InvalidEmployeeTypeException.class, () -> orderItemService.updateOrderItem(orderItem.getId(), 2,invalidPin));

    }



    @Test
    void updateOrderItem_whenAmountIsNotPositive_throwsException(){
        //data setup
        var waiter = new Employee();
        waiter.setPin("1234");
        waiter.setType(EmployeeType.WAITER);

        var order = new Order();
        order.setId(1);
        order.setWaiter(waiter);

        var orderItemGroup = new OrderItemGroup();
        orderItemGroup.setOrder(order);
        orderItemGroup.setId(1);

        var orderItem = new OrderItem();
        orderItem.setId(1);
        orderItem.setStatus(OrderItemStatus.NEW);
        orderItem.setAmount(1);
        orderItem.setOrderItemGroup(orderItemGroup);

        //mock and spy
        Mockito.doReturn(Optional.of(orderItem)).when(orderItemRepository).findById(1);

        //method call and assertion
        assertThrows(IllegalAmountException.class, () -> orderItemService.updateOrderItem(orderItem.getId(), -2, waiter.getPin()));

    }


    @Test
    void deleteOrderItem_withValidData_isSuccess(){
        //data setup
        var waiter = new Employee();
        waiter.setPin("1234");
        waiter.setType(EmployeeType.WAITER);

        var order = new Order();
        order.setId(1);
        order.setWaiter(waiter);

        var orderItemGroup = new OrderItemGroup();
        orderItemGroup.setOrder(order);
        orderItemGroup.setId(1);

        var orderItem = new OrderItem();
        orderItem.setId(1);
        orderItem.setStatus(OrderItemStatus.NEW);
        orderItem.setAmount(1);
        orderItem.setOrderItemGroup(orderItemGroup);

        //mock and spy
        Mockito.doReturn(Optional.of(orderItem)).when(orderItemRepository).findById(1);

        //method call and assertion
        orderItemService.deleteOrderItem(orderItem.getId(), waiter.getPin());
        assertFalse(orderItem.getIsActive());
    }

    @Test
    void deleteOrderItem_whenItemStatusIsNotNew_throwsException(){
        //data setup
        var waiter = new Employee();
        waiter.setPin("1234");
        waiter.setType(EmployeeType.WAITER);

        var order = new Order();
        order.setId(1);
        order.setWaiter(waiter);

        var orderItemGroup = new OrderItemGroup();
        orderItemGroup.setOrder(order);
        orderItemGroup.setId(1);

        var orderItem = new OrderItem();
        orderItem.setId(1);
        orderItem.setStatus(OrderItemStatus.SENT);
        orderItem.setAmount(1);
        orderItem.setOrderItemGroup(orderItemGroup);

        //mock and spy
        Mockito.doReturn(Optional.of(orderItem)).when(orderItemRepository).findById(1);

        //method call and assertion
        assertThrows(OrderItemInvalidStatusException.class, () -> orderItemService.deleteOrderItem(orderItem.getId(), waiter.getPin()));

    }


    @Test
    void deleteOrderItem_whenItemDoesNotExist_throwsException(){
        //data setup
        var waiter = new Employee();
        waiter.setPin("1234");
        waiter.setType(EmployeeType.WAITER);

        var order = new Order();
        order.setId(1);
        order.setWaiter(waiter);

        var orderItemGroup = new OrderItemGroup();
        orderItemGroup.setOrder(order);
        orderItemGroup.setId(1);

        var orderItem = new OrderItem();
        orderItem.setId(1);
        orderItem.setStatus(OrderItemStatus.SENT);
        orderItem.setAmount(1);
        orderItem.setOrderItemGroup(orderItemGroup);

        //mock and spy
        Mockito.doReturn(Optional.empty()).when(orderItemRepository).findById(1);

        //method call and assertion
        assertThrows(NotFoundException.class, () -> orderItemService.deleteOrderItem(orderItem.getId(), waiter.getPin()));

    }


    @Test
    void deleteOrderItem_whenNotResponsibleEmployeeTriesToDelete_throwsException(){
        //data setup
        var waiter = new Employee();
        waiter.setPin("1234");
        waiter.setType(EmployeeType.WAITER);

        var order = new Order();
        order.setId(1);
        order.setWaiter(waiter);

        var orderItemGroup = new OrderItemGroup();
        orderItemGroup.setOrder(order);
        orderItemGroup.setId(1);

        var orderItem = new OrderItem();
        orderItem.setId(1);
        orderItem.setStatus(OrderItemStatus.NEW);
        orderItem.setAmount(1);
        orderItem.setOrderItemGroup(orderItemGroup);

        var invalidPin = "1111";

        //mock and spy
        Mockito.doReturn(Optional.of(orderItem)).when(orderItemRepository).findById(1);
        Mockito.doThrow(new InvalidEmployeeTypeException(invalidPin)).when(employeeOrderService).throwIfWaiterNotResponsible(invalidPin, waiter.getId());

        //method call and assertion
        assertThrows(InvalidEmployeeTypeException.class, () -> orderItemService.deleteOrderItem(orderItem.getId(), invalidPin));

    }






}
