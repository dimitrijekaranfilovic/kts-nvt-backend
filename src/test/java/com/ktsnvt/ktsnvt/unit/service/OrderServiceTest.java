package com.ktsnvt.ktsnvt.unit.service;


import com.ktsnvt.ktsnvt.exception.InvalidEmployeeTypeException;
import com.ktsnvt.ktsnvt.exception.OrderItemGroupInvalidStatusException;
import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.model.Order;
import com.ktsnvt.ktsnvt.model.OrderItemGroup;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import com.ktsnvt.ktsnvt.model.enums.OrderItemGroupStatus;
import com.ktsnvt.ktsnvt.model.enums.OrderStatus;
import com.ktsnvt.ktsnvt.repository.OrderItemGroupRepository;
import com.ktsnvt.ktsnvt.service.EmployeeOrderService;
import com.ktsnvt.ktsnvt.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderServiceTest {

    @Mock
    private OrderItemGroupRepository orderItemGroupRepository;

    @Mock
    private EmployeeOrderService employeeOrderService;

    @InjectMocks
    private OrderServiceImpl orderService;



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

}
