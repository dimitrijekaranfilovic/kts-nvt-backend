package com.ktsnvt.ktsnvt.unit.service;

import com.ktsnvt.ktsnvt.exception.InvalidEmployeeTypeException;
import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.model.OrderItem;
import com.ktsnvt.ktsnvt.model.OrderItemGroup;
import com.ktsnvt.ktsnvt.model.enums.OrderItemGroupStatus;
import com.ktsnvt.ktsnvt.model.enums.OrderItemStatus;
import com.ktsnvt.ktsnvt.repository.MenuItemRepository;
import com.ktsnvt.ktsnvt.repository.OrderItemGroupRepository;
import com.ktsnvt.ktsnvt.repository.OrderItemRepository;
import com.ktsnvt.ktsnvt.service.EmployeeOrderService;
import com.ktsnvt.ktsnvt.service.EmployeeQueryService;
import com.ktsnvt.ktsnvt.service.LocalDateTimeService;
import com.ktsnvt.ktsnvt.service.impl.OrderItemServiceImpl;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

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
        Employee employee = new Employee();
        employee.setId(1);
        OrderItem orderItem = new OrderItem();
        orderItem.setId(2);

        LocalDateTime takenAt = LocalDateTime.of(2021, 1, 1, 1, 1);

        doReturn(employee).when(employeeQueryService).findByPin(anyString());
        doReturn(Optional.of(orderItem)).when(orderItemRepository).findOneByIdWithItemReference(orderItem.getId());
        doNothing().when(employeeOrderService).throwIfNotValidEmployeeType(employee, orderItem);
        doReturn(takenAt).when(dateTimeService).currentTime();

        orderItemService.takeItemRequest(orderItem.getId(), "");

        assertEquals(employee, orderItem.getPreparedBy());
        assertEquals(OrderItemStatus.PREPARING, orderItem.getStatus());
        assertEquals(takenAt, orderItem.getTakenAt());
        verify(orderItemRepository, times(1)).save(orderItem);
    }

    @Test
    public void finishItemRequest_calledOnSentItem_IsSuccess() {
        LocalDateTime takenAt = LocalDateTime.of(2021, 1, 1, 1, 1);
        Employee employee = new Employee();
        employee.setId(1);
        employee.setPin("0000");
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1);
        OrderItemGroup group = new OrderItemGroup();
        group.addItem(orderItem);

        doReturn(employee).when(employeeQueryService).findByPin(employee.getPin());
        doReturn(Optional.of(orderItem)).when(orderItemRepository).findOneInProgressByIdWithItemReference(orderItem.getId());
        doNothing().when(employeeOrderService).throwIfNotValidEmployeeType(employee, orderItem);
        doReturn(group.getOrderItems()).when(orderItemRepository).getAllFromOneGroup(orderItem.getOrderItemGroup().getId());
        doReturn(takenAt).when(dateTimeService).currentTime();

        orderItemService.finishItemRequest(orderItem.getId(), employee.getPin());

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
        LocalDateTime takenAt = LocalDateTime.of(2021, 1, 1, 1, 1);
        Employee employee = new Employee();
        employee.setId(1);
        employee.setPin("0000");
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1);
        orderItem.setPreparedBy(employee);
        OrderItemGroup group = new OrderItemGroup();
        group.addItem(orderItem);

        doReturn(employee).when(employeeQueryService).findByPin(employee.getPin());
        doReturn(Optional.of(orderItem)).when(orderItemRepository).findOneInProgressByIdWithItemReference(orderItem.getId());
        doNothing().when(employeeOrderService).throwIfNotValidEmployeeType(employee, orderItem);
        doReturn(group.getOrderItems()).when(orderItemRepository).getAllFromOneGroup(orderItem.getOrderItemGroup().getId());
        doReturn(takenAt).when(dateTimeService).currentTime();

        orderItemService.finishItemRequest(orderItem.getId(), employee.getPin());

        assertEquals(OrderItemStatus.DONE, orderItem.getStatus());
        assertEquals(takenAt, orderItem.getPreparedAt());
        assertEquals(employee, orderItem.getPreparedBy());
        assertEquals(OrderItemGroupStatus.DONE, group.getStatus());
        verify(orderItemRepository, times(1)).save(orderItem);
        verify(orderItemGroupRepository, times(1)).save(group);
    }

    @Test
    public void finishItemRequest_calledAsNotLastInGroup_DoesNotUpdateGroup() {
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

        doReturn(employee).when(employeeQueryService).findByPin(employee.getPin());
        doReturn(Optional.of(orderItem)).when(orderItemRepository).findOneInProgressByIdWithItemReference(orderItem.getId());
        doNothing().when(employeeOrderService).throwIfNotValidEmployeeType(employee, orderItem);
        doReturn(new ArrayList<OrderItem>()).when(orderItemRepository).getAllFromOneGroup(orderItem.getOrderItemGroup().getId());
        doReturn(takenAt).when(dateTimeService).currentTime();

        orderItemService.finishItemRequest(orderItem.getId(), employee.getPin());

        assertEquals(OrderItemGroupStatus.SENT, group.getStatus());
        verify(orderItemGroupRepository, times(0)).save(group);
    }

    @Test
    public void finishItemRequest_takenByOtherEmployee_throwsInvalidEmployeeTypeException() {
        Employee employee1 = new Employee();
        employee1.setId(1);
        employee1.setPin("0000");
        Employee employee2 = new Employee();
        employee2.setId(2);
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1);
        orderItem.setPreparedBy(employee2);

        doReturn(employee1).when(employeeQueryService).findByPin(employee1.getPin());
        doReturn(Optional.of(orderItem)).when(orderItemRepository).findOneInProgressByIdWithItemReference(orderItem.getId());

        assertThrows(InvalidEmployeeTypeException.class, () -> orderItemService.finishItemRequest(orderItem.getId(), employee1.getPin()));

    }
}
