package com.ktsnvt.ktsnvt.unit.service;


import com.ktsnvt.ktsnvt.exception.IllegalOrderStateException;
import com.ktsnvt.ktsnvt.exception.InvalidEmployeeTypeException;
import com.ktsnvt.ktsnvt.exception.OccupiedTableException;
import com.ktsnvt.ktsnvt.exception.OrderItemGroupInvalidStatusException;
import com.ktsnvt.ktsnvt.model.*;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import com.ktsnvt.ktsnvt.model.enums.OrderItemGroupStatus;
import com.ktsnvt.ktsnvt.model.enums.OrderItemStatus;
import com.ktsnvt.ktsnvt.model.enums.OrderStatus;
import com.ktsnvt.ktsnvt.repository.OrderItemGroupRepository;
import com.ktsnvt.ktsnvt.repository.OrderRepository;
import com.ktsnvt.ktsnvt.service.*;
import com.ktsnvt.ktsnvt.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;

@SpringBootTest
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemGroupRepository orderItemGroupRepository;
    @Mock
    private EmployeeQueryService employeeQueryService;
    @Mock
    private EmployeeOrderService employeeOrderService;
    @Mock
    private RestaurantTableService restaurantTableService;
    @Mock
    private LocalDateTimeService localDateTimeService;
    @InjectMocks
    private OrderServiceImpl orderService;

    private final LocalDate currentDate = LocalDate.of(2021, 11, 30);
    private final LocalDateTime currentDateTime = LocalDateTime.of(currentDate, LocalTime.of(12, 0));

    @BeforeEach
    void setupLocalDateTimeService() {
        doReturn(currentDate).when(localDateTimeService).currentDate();
        doReturn(currentDateTime).when(localDateTimeService).currentTime();
    }

    @Test
    void createOrder_whenCalledWithValidData_isSuccess() {
        // GIVEN
        var table = new RestaurantTable(123, 10, 10, 10, new Section("something"));
        var tableId = 999;
        table.setId(tableId);
        var waiterPin = "1234";
        var waiter = new Employee("name", "surname", new Authority("WAITER"), waiterPin, EmployeeType.WAITER);
        doReturn(table).when(restaurantTableService).readForUpdate(tableId);
        doReturn(waiter).when(employeeQueryService).findByPinForUpdate(waiterPin, EmployeeType.WAITER);
        doAnswer(returnsFirstArg()).when(orderRepository).save(any());

        // WHEN
        var createdOrder = orderService.createOrder(tableId, waiterPin);

        // THEN
        assertFalse(table.getAvailable());
        assertEquals(table, createdOrder.getRestaurantTable());
        assertEquals(waiter, createdOrder.getWaiter());
        assertEquals(currentDateTime, createdOrder.getCreatedAt());
        assertEquals(OrderStatus.CREATED, createdOrder.getStatus());
        verify(orderRepository, times(1)).save(createdOrder);
    }

    @Test
    void createOrder_whenCalledWithTableWhichIsAlreadyOccupied_throwsException() {
        // GIVEN
        var table = new RestaurantTable(123, 10, 10, 10, new Section("something"));
        var tableId = 999;
        table.setId(tableId);
        table.setAvailable(false);
        var waiterPin = "1234";
        var waiter = new Employee("name", "surname", new Authority("WAITER"), waiterPin, EmployeeType.WAITER);
        doReturn(table).when(restaurantTableService).readForUpdate(tableId);
        doReturn(waiter).when(employeeQueryService).findByPinForUpdate(waiterPin, EmployeeType.WAITER);
        doAnswer(returnsFirstArg()).when(orderRepository).save(any());

        // WHEN
        assertThrows(OccupiedTableException.class, () -> orderService.createOrder(tableId, waiterPin));

        // THEN
        assertFalse(table.getAvailable());
        verify(orderRepository, times(0)).save(any());
    }

    @Test
    void cancelOrder_whenCalledWithGivenData_isSuccess() {
        // GIVEN
        var order = new Order();
        var orderId = 999;
        order.setId(orderId); order.setStatus(OrderStatus.CREATED);
        var waiterPin = "1234";
        var waiterId = 999;
        var waiter = new Employee("name", "surname", new Authority("WAITER"), waiterPin, EmployeeType.WAITER);
        waiter.setId(waiterId);
        order.setWaiter(waiter);
        var table = new RestaurantTable();
        table.setAvailable(false);
        order.setRestaurantTable(table);
        var og1 = new OrderItemGroup("first", OrderItemGroupStatus.NEW);
        var og2 = new OrderItemGroup("second", OrderItemGroupStatus.NEW);
        order.getItemGroups().add(og1); order.getItemGroups().add(og2);
        OrderService orderServiceSpy = spy(orderService);
        doNothing().when(employeeOrderService).throwIfWaiterNotResponsible(waiterPin, waiterId);
        doReturn(order).when(orderServiceSpy).getOrder(orderId);

        // WHEN
        orderServiceSpy.cancelOrder(orderId, waiterPin);

        // THEN
        assertTrue(table.getAvailable());
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        assertFalse(og1.getIsActive());
        assertFalse(og2.getIsActive());
        verifyNoInteractions(orderRepository);
    }

    @Test
    void cancelOrder_whenCalledWithNoItems_isSuccess() {
        // GIVEN
        var order = new Order();
        var orderId = 999;
        order.setId(orderId); order.setStatus(OrderStatus.CREATED);
        var waiterPin = "1234";
        var waiterId = 999;
        var waiter = new Employee("name", "surname", new Authority("WAITER"), waiterPin, EmployeeType.WAITER);
        waiter.setId(waiterId);
        order.setWaiter(waiter);
        var table = new RestaurantTable();
        table.setAvailable(false);
        order.setRestaurantTable(table);
        OrderService orderServiceSpy = spy(orderService);
        doNothing().when(employeeOrderService).throwIfWaiterNotResponsible(waiterPin, waiterId);
        doReturn(order).when(orderServiceSpy).getOrder(orderId);

        // WHEN
        orderServiceSpy.cancelOrder(orderId, waiterPin);

        // THEN
        assertTrue(table.getAvailable());
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        verifyNoInteractions(orderRepository);
    }

    @Test
    void cancelOrder_whenCalledWithInProgressItems_throwsException() {
        // GIVEN
        var order = new Order();
        var orderId = 999;
        order.setId(orderId); order.setStatus(OrderStatus.IN_PROGRESS);
        var waiterPin = "1234";
        var waiterId = 999;
        var waiter = new Employee("name", "surname", new Authority("WAITER"), waiterPin, EmployeeType.WAITER);
        waiter.setId(waiterId);
        order.setWaiter(waiter);
        var table = new RestaurantTable();
        table.setAvailable(false);
        order.setRestaurantTable(table);
        var og1 = new OrderItemGroup("first", OrderItemGroupStatus.SENT);
        var og2 = new OrderItemGroup("second", OrderItemGroupStatus.DONE);
        order.getItemGroups().add(og1); order.getItemGroups().add(og2);
        OrderService orderServiceSpy = spy(orderService);
        doNothing().when(employeeOrderService).throwIfWaiterNotResponsible(waiterPin, waiterId);
        doReturn(order).when(orderServiceSpy).getOrder(orderId);

        // WHEN
        assertThrows(IllegalOrderStateException.class, () -> orderServiceSpy.cancelOrder(orderId, waiterPin));

        // THEN
        assertFalse(table.getAvailable());
        assertEquals(OrderStatus.IN_PROGRESS, order.getStatus());
        assertEquals(OrderItemGroupStatus.SENT, og1.getStatus());
        assertEquals(OrderItemGroupStatus.DONE, og2.getStatus());
        verifyNoInteractions(orderRepository);
    }

    @Test
    void cancelOrder_whenCalledNonNewOrCreatedOrder_throwsException() {
        // GIVEN
        var order = new Order();
        var orderId = 999;
        order.setId(orderId); order.setStatus(OrderStatus.CHARGED);
        var waiterPin = "1234";
        var waiterId = 999;
        var waiter = new Employee("name", "surname", new Authority("WAITER"), waiterPin, EmployeeType.WAITER);
        waiter.setId(waiterId);
        order.setWaiter(waiter);
        var table = new RestaurantTable();
        table.setAvailable(false);
        order.setRestaurantTable(table);
        OrderService orderServiceSpy = spy(orderService);
        doNothing().when(employeeOrderService).throwIfWaiterNotResponsible(waiterPin, waiterId);
        doReturn(order).when(orderServiceSpy).getOrder(orderId);

        // WHEN
        assertThrows(IllegalOrderStateException.class, () -> orderServiceSpy.cancelOrder(orderId, waiterPin));

        // THEN
        assertFalse(table.getAvailable());
        assertEquals(OrderStatus.CHARGED, order.getStatus());
        verifyNoInteractions(orderRepository);
    }

    @Test
    void chargeOrder_calledWithValidData_isSuccess() {
        // GIVEN
        var order = new Order();
        var orderId = 999;
        order.setId(orderId); order.setStatus(OrderStatus.IN_PROGRESS);
        var waiterPin = "1234";
        var waiterId = 999;
        var waiter = new Employee("name", "surname", new Authority("WAITER"), waiterPin, EmployeeType.WAITER);
        waiter.setId(waiterId);
        order.setWaiter(waiter);
        var table = new RestaurantTable(123, 10, 10, 10, new Section("something"));
        var tableId = 999;
        table.setId(tableId); table.setAvailable(false);
        order.setRestaurantTable(table);
        var og1 = new OrderItemGroup("first", OrderItemGroupStatus.DONE);
        makeOrderItem(og1, 2, 30, 15);
        makeOrderItem(og1, 1, 40, 12);
        var og2 = new OrderItemGroup("second", OrderItemGroupStatus.DONE);
        makeOrderItem(og2, 1, 20, 8);
        makeOrderItem(og2, 3, 50, 25);
        order.getItemGroups().add(og1); order.getItemGroups().add(og2);
        OrderService orderServiceSpy = spy(orderService);
        doReturn(order).when(orderServiceSpy).getOrder(orderId);
        doNothing().when(employeeOrderService).throwIfWaiterNotResponsible(waiterPin, waiterId);

        // WHEN
        orderServiceSpy.chargeOrder(orderId, waiterPin);

        // THEN
        assertTrue(table.getAvailable());
        assertEquals(BigDecimal.valueOf(270), order.getTotalIncome());
        assertEquals(BigDecimal.valueOf(125), order.getTotalCost());
        assertEquals(OrderStatus.CHARGED, order.getStatus());
        verifyNoInteractions(orderRepository);
    }

    @Test
    void chargeOrder_calledWithNotInProgress_throwsException() {
        // GIVEN
        var order = new Order();
        var orderId = 999;
        order.setId(orderId); order.setStatus(OrderStatus.CANCELLED);
        var waiterPin = "1234";
        var waiterId = 999;
        var waiter = new Employee("name", "surname", new Authority("WAITER"), waiterPin, EmployeeType.WAITER);
        waiter.setId(waiterId);
        order.setWaiter(waiter);
        var table = new RestaurantTable(123, 10, 10, 10, new Section("something"));
        var tableId = 999;
        table.setId(tableId); table.setAvailable(false);
        order.setRestaurantTable(table);
        OrderService orderServiceSpy = spy(orderService);
        doReturn(order).when(orderServiceSpy).getOrder(orderId);
        doNothing().when(employeeOrderService).throwIfWaiterNotResponsible(waiterPin, waiterId);

        // WHEN
        assertThrows(IllegalOrderStateException.class, () -> orderServiceSpy.chargeOrder(orderId, waiterPin));

        // THEN
        assertFalse(table.getAvailable());
        assertNotEquals(OrderStatus.CHARGED, order.getStatus());
        verifyNoInteractions(orderRepository);
    }

    @Test
    void chargeOrder_calledWithNoDoneItems_throwsException() {
        // GIVEN
        var order = new Order();
        var orderId = 999;
        order.setId(orderId); order.setStatus(OrderStatus.IN_PROGRESS);
        var waiterPin = "1234";
        var waiterId = 999;
        var waiter = new Employee("name", "surname", new Authority("WAITER"), waiterPin, EmployeeType.WAITER);
        waiter.setId(waiterId);
        order.setWaiter(waiter);
        var table = new RestaurantTable(123, 10, 10, 10, new Section("something"));
        var tableId = 999;
        table.setId(tableId); table.setAvailable(false);
        order.setRestaurantTable(table);
        var og1 = new OrderItemGroup("first", OrderItemGroupStatus.SENT);
        var og2 = new OrderItemGroup("second", OrderItemGroupStatus.DONE);
        order.getItemGroups().add(og1); order.getItemGroups().add(og2);
        OrderService orderServiceSpy = spy(orderService);
        doReturn(order).when(orderServiceSpy).getOrder(orderId);
        doNothing().when(employeeOrderService).throwIfWaiterNotResponsible(waiterPin, waiterId);

        // WHEN
        assertThrows(IllegalOrderStateException.class, () -> orderServiceSpy.chargeOrder(orderId, waiterPin));

        // THEN
        assertFalse(table.getAvailable());
        assertNotEquals(OrderStatus.CHARGED, order.getStatus());
        verifyNoInteractions(orderRepository);
    }

    @Test
    void sendOrderItemGroup_withValidData_isSuccess(){
        //data setup
        var waiter = new Employee();
        waiter.setType(EmployeeType.WAITER);
        waiter.setId(1);
        waiter.setPin("1234");

        var order = new Order();
        order.setId(1);
        order.setWaiter(waiter);
        order.setStatus(OrderStatus.IN_PROGRESS);

        var orderItemGroup = new OrderItemGroup();
        orderItemGroup.setId(1);
        orderItemGroup.setName("group 1");
        orderItemGroup.setOrder(order);
        orderItemGroup.setStatus(OrderItemGroupStatus.NEW);

        //set up mock and spy objects behaviour
        var orderServiceSpy = Mockito.spy(orderService);
        Mockito.doReturn(orderItemGroup).when(orderServiceSpy).getOrderItemGroup(1, 1);


        //call the method to be tested
        orderServiceSpy.sendOrderItemGroup(order.getId(), orderItemGroup.getId(), waiter.getPin());

        Assertions.assertEquals(OrderItemGroupStatus.SENT, orderItemGroup.getStatus());
        Mockito.verify(orderItemGroupRepository, Mockito.times(1)).save(orderItemGroup);
        Mockito.verify(employeeOrderService, Mockito.times(1)).throwIfWaiterNotResponsible(waiter.getPin(), waiter.getId());


    }


    @Test
    void sendOrderItemGroup_whenGroupStatusIsNotNew_throwsException(){
        //data setup
        var waiter = new Employee();
        waiter.setType(EmployeeType.WAITER);
        waiter.setId(1);
        waiter.setPin("1234");

        var order = new Order();
        order.setId(1);
        order.setWaiter(waiter);
        order.setStatus(OrderStatus.IN_PROGRESS);

        var orderItemGroup = new OrderItemGroup();
        orderItemGroup.setId(1);
        orderItemGroup.setName("group 1");
        orderItemGroup.setOrder(order);
        orderItemGroup.setStatus(OrderItemGroupStatus.SENT);

        //set up mock and spy objects behaviour
        var orderServiceSpy = Mockito.spy(orderService);
        Mockito.doReturn(orderItemGroup).when(orderServiceSpy).getOrderItemGroup(1, 1);


        //call the method to be tested
        Assertions.assertThrows(OrderItemGroupInvalidStatusException.class, ()-> orderServiceSpy.sendOrderItemGroup(order.getId(), orderItemGroup.getId(), waiter.getPin()));

    }


    @Test
    void sendOrderItemGroup_whenNotResponsibleWaiterTriesToSend_throwsException(){
        //data setup
        var waiter = new Employee();
        waiter.setType(EmployeeType.WAITER);
        waiter.setId(1);
        waiter.setPin("1234");

        var order = new Order();
        order.setId(1);
        order.setWaiter(waiter);
        order.setStatus(OrderStatus.IN_PROGRESS);

        var orderItemGroup = new OrderItemGroup();
        orderItemGroup.setId(1);
        orderItemGroup.setName("group 1");
        orderItemGroup.setOrder(order);
        orderItemGroup.setStatus(OrderItemGroupStatus.NEW);

        var invalidWaiterPin = "1111";

        //set up mock and spy objects behaviour
        var orderServiceSpy = Mockito.spy(orderService);
        Mockito.doReturn(orderItemGroup).when(orderServiceSpy).getOrderItemGroup(1, 1);
        Mockito.doThrow(new InvalidEmployeeTypeException(invalidWaiterPin)).when(employeeOrderService).throwIfWaiterNotResponsible(invalidWaiterPin, waiter.getId());
        Mockito.doReturn(orderItemGroup).when(orderItemGroupRepository).save(orderItemGroup);


        //call the method to be tested
        Assertions.assertThrows(InvalidEmployeeTypeException.class, ()-> orderServiceSpy.sendOrderItemGroup(order.getId(), orderItemGroup.getId(), invalidWaiterPin));


    }



    @Test
    void deleteOrderItemGroup_withValidData_isSuccess(){
        //data setup
        var waiter = new Employee();
        waiter.setType(EmployeeType.WAITER);
        waiter.setId(1);
        waiter.setPin("1234");

        var order = new Order();
        order.setId(1);
        order.setWaiter(waiter);
        order.setStatus(OrderStatus.IN_PROGRESS);

        var orderItemGroup = new OrderItemGroup();
        orderItemGroup.setId(1);
        orderItemGroup.setName("group 1");
        orderItemGroup.setOrder(order);
        orderItemGroup.setStatus(OrderItemGroupStatus.NEW);


        //set up mock and spy objects behaviour
        var orderServiceSpy = Mockito.spy(orderService);
        Mockito.doReturn(orderItemGroup).when(orderServiceSpy).getOrderItemGroup(1, 1);

        orderServiceSpy.deleteOrderItemGroup(order.getId(), orderItemGroup.getId(), waiter.getPin());

        Assertions.assertFalse(orderItemGroup.getIsActive());

        Mockito.verify(employeeOrderService, Mockito.times(1)).throwIfWaiterNotResponsible(waiter.getPin(), orderItemGroup.getOrder().getWaiter().getId());
        Mockito.verify(orderItemGroupRepository, Mockito.times(1)).save(orderItemGroup);
    }


    @Test
    void deleteOrderItemGroup_whenGroupStatusIsNotNew_throwsException(){
        //data setup
        var waiter = new Employee();
        waiter.setType(EmployeeType.WAITER);
        waiter.setId(1);
        waiter.setPin("1234");

        var order = new Order();
        order.setId(1);
        order.setWaiter(waiter);
        order.setStatus(OrderStatus.IN_PROGRESS);

        var orderItemGroup = new OrderItemGroup();
        orderItemGroup.setId(1);
        orderItemGroup.setName("group 1");
        orderItemGroup.setOrder(order);
        orderItemGroup.setStatus(OrderItemGroupStatus.SENT);


        //set up mock and spy objects behaviour
        var orderServiceSpy = Mockito.spy(orderService);
        Mockito.doReturn(orderItemGroup).when(orderServiceSpy).getOrderItemGroup(1, 1);

        Assertions.assertThrows(OrderItemGroupInvalidStatusException.class, ()->orderServiceSpy.deleteOrderItemGroup(order.getId(), orderItemGroup.getId(), waiter.getPin()));
    }


    @Test
    void deleteOrderItemGroup_whenNotResponsibleEmployeeTriesToDelete_throwsException(){
        //data setup
        var waiter = new Employee();
        waiter.setType(EmployeeType.WAITER);
        waiter.setId(1);
        waiter.setPin("1234");

        var order = new Order();
        order.setId(1);
        order.setWaiter(waiter);
        order.setStatus(OrderStatus.IN_PROGRESS);

        var orderItemGroup = new OrderItemGroup();
        orderItemGroup.setId(1);
        orderItemGroup.setName("group 1");
        orderItemGroup.setOrder(order);
        orderItemGroup.setStatus(OrderItemGroupStatus.NEW);

        var invalidEmployeePin = "1111";

        //set up mock and spy objects behaviour
        var orderServiceSpy = Mockito.spy(orderService);
        Mockito.doReturn(orderItemGroup).when(orderServiceSpy).getOrderItemGroup(1, 1);
        Mockito.doThrow(new InvalidEmployeeTypeException(invalidEmployeePin)).when(employeeOrderService).throwIfWaiterNotResponsible(invalidEmployeePin, waiter.getId());

        Assertions.assertThrows(InvalidEmployeeTypeException.class, ()->orderServiceSpy.deleteOrderItemGroup(order.getId(), orderItemGroup.getId(), invalidEmployeePin));


    }


    @Test
    void createGroupForOrder_withValidData_isSuccess(){
        //data setup
        var waiter = new Employee();
        waiter.setType(EmployeeType.WAITER);
        waiter.setId(1);
        waiter.setPin("1234");

        var order = new Order();
        order.setId(1);
        order.setWaiter(waiter);
        order.setStatus(OrderStatus.IN_PROGRESS);

        var orderItemGroup = new OrderItemGroup();
        orderItemGroup.setId(1);
        orderItemGroup.setName("group 1");
        orderItemGroup.setStatus(OrderItemGroupStatus.NEW);


        //set up mock and spy objects behaviour
        var orderServiceSpy = Mockito.spy(orderService);
        Mockito.doReturn(order).when(orderServiceSpy).getOrder(1);
        Mockito.doReturn(Optional.empty()).when(orderServiceSpy).getOrderItemGroup(1, "group 1");

        orderServiceSpy.createGroupForOrder(order.getId(), "group 1", waiter.getPin());

        Assertions.assertEquals("group 1", orderItemGroup.getName());

        Mockito.verify(orderServiceSpy, Mockito.times(1)).getOrder(1);
        Mockito.verify(orderServiceSpy, Mockito.times(1)).getOrderItemGroup(1, "group 1");

    }


    @Test
    void createGroupForOrder_whenGroupNameExists_throwsException(){
        //data setup
        var waiter = new Employee();
        waiter.setType(EmployeeType.WAITER);
        waiter.setId(1);
        waiter.setPin("1234");

        var order = new Order();
        order.setId(1);
        order.setWaiter(waiter);
        order.setStatus(OrderStatus.IN_PROGRESS);

        var orderItemGroup = new OrderItemGroup();
        orderItemGroup.setId(1);
        orderItemGroup.setName("group 1");
        orderItemGroup.setStatus(OrderItemGroupStatus.NEW);

        //set up mock and spy objects behaviour
        var orderServiceSpy = Mockito.spy(orderService);
        Mockito.doReturn(order).when(orderServiceSpy).getOrder(1);
        Mockito.doReturn(Optional.of(orderItemGroup)).when(orderServiceSpy).getOrderItemGroup(1, "group 1");

        Assertions.assertThrows(OrderItemGroupInvalidStatusException.class, ()->orderServiceSpy.createGroupForOrder(order.getId(), "group 1", waiter.getPin()));

    }


    @Test
    void createGroupForOrder_whenNotResponsibleEmployeeTriesToCreate_throwsException(){
        //data setup
        var waiter = new Employee();
        waiter.setType(EmployeeType.WAITER);
        waiter.setId(1);
        waiter.setPin("1234");

        var order = new Order();
        order.setId(1);
        order.setWaiter(waiter);
        order.setStatus(OrderStatus.IN_PROGRESS);

        var orderItemGroup = new OrderItemGroup();
        orderItemGroup.setId(1);
        orderItemGroup.setName("group 1");
        orderItemGroup.setStatus(OrderItemGroupStatus.NEW);

        var invalidPin = "1111";


        //set up mock and spy objects behaviour
        var orderServiceSpy = Mockito.spy(orderService);
        Mockito.doReturn(order).when(orderServiceSpy).getOrder(1);
        Mockito.doReturn(Optional.empty()).when(orderServiceSpy).getOrderItemGroup(1, "group 1");
        Mockito.doThrow(new InvalidEmployeeTypeException(invalidPin)).when(employeeOrderService).throwIfWaiterNotResponsible(invalidPin, waiter.getId());

        Assertions.assertThrows(InvalidEmployeeTypeException.class, ()->orderServiceSpy.createGroupForOrder(order.getId(), "group 1", invalidPin));


    }

    private void makeOrderItem(OrderItemGroup group, int amount, long price, long cost) {
        OrderItem item = new OrderItem();
        item.setAmount(amount);
        item.setCurrentMenuPrice(BigDecimal.valueOf(price));
        item.setCurrentBasePrice(BigDecimal.valueOf(cost));
        group.addItem(item);
    }

    @Test
    void createGroupForOrder_whenOrderStatusIsChargedOrCancelled_throwsException(){
        //data setup
        var waiter = new Employee();
        waiter.setType(EmployeeType.WAITER);
        waiter.setId(1);
        waiter.setPin("1234");

        var order = new Order();
        order.setId(1);
        order.setWaiter(waiter);
        order.setStatus(OrderStatus.CHARGED);

        var orderItemGroup = new OrderItemGroup();
        orderItemGroup.setId(1);
        orderItemGroup.setName("group 1");
        orderItemGroup.setStatus(OrderItemGroupStatus.NEW);

        //set up mock and spy objects behaviour
        var orderServiceSpy = Mockito.spy(orderService);
        Mockito.doReturn(order).when(orderServiceSpy).getOrder(1);
        Mockito.doReturn(Optional.of(orderItemGroup)).when(orderServiceSpy).getOrderItemGroup(1, "group 1");

        Assertions.assertThrows(IllegalOrderStateException.class, ()->orderServiceSpy.createGroupForOrder(order.getId(), "group 1", waiter.getPin()));

    }


    @Test
    void getOrderIdForTable_withValidData_isSuccess(){
        Mockito.doReturn(1).when(orderRepository).getOrderIdForTableId(1, Arrays.asList(OrderStatus.CANCELLED, OrderStatus.CHARGED));
        Assertions.assertEquals(1, orderService.getOrderIdForTable(1));
    }

    @Test
    void getOrderIdForTable_whenOrderDoesNotExist_returnsNull() {
        Mockito.doReturn(null).when(orderRepository).getOrderIdForTableId(1, Arrays.asList(OrderStatus.CANCELLED, OrderStatus.CHARGED));
        Assertions.assertNull(orderService.getOrderIdForTable(1));
    }








}
