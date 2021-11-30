package com.ktsnvt.ktsnvt.unit.service;

import com.ktsnvt.ktsnvt.exception.InvalidEmployeeTypeException;
import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.model.InventoryItem;
import com.ktsnvt.ktsnvt.model.MenuItem;
import com.ktsnvt.ktsnvt.model.OrderItem;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import com.ktsnvt.ktsnvt.service.EmployeeQueryService;
import com.ktsnvt.ktsnvt.service.impl.EmployeeOrderServiceImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
public class EmployeeOrderServiceTest {

    @Mock
    private EmployeeQueryService employeeQueryService;

    @InjectMocks
    private EmployeeOrderServiceImpl employeeOrderService;

    private static Stream<Arguments> provideParamsForThrowIfNotResponsible() {
        return Stream.of(
                Arguments.of("0000", 1),
                Arguments.of("1111", 2)
        );
    }

    private static Stream<Arguments> provideParamsForThrowIfNotValidEmployeeType() {
        Employee e1 = new Employee();
        e1.setType(EmployeeType.CHEF);
        Employee e2 = new Employee();
        e2.setType(EmployeeType.BARTENDER);
        OrderItem orderFoodItem = new OrderItem();
        MenuItem menuFoodItem = new MenuItem();
        InventoryItem foodItem = new InventoryItem();
        foodItem.setCategory(ItemCategory.FOOD);
        menuFoodItem.setItem(foodItem);
        orderFoodItem.setItem(menuFoodItem);

        OrderItem orderDrinkItem = new OrderItem();
        MenuItem menuDrinkItem = new MenuItem();
        InventoryItem drinkItem = new InventoryItem();
        drinkItem.setCategory(ItemCategory.DRINK);
        menuDrinkItem.setItem(drinkItem);
        orderDrinkItem.setItem(menuDrinkItem);

        return Stream.of(
            Arguments.of(e1, orderDrinkItem),
            Arguments.of(e2, orderFoodItem)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParamsForThrowIfNotResponsible")
    public void throwIfWaiterNotResponsible_waiterNotResponsible_throwsInvalidEmployeeTypeException(String pin, Integer waiterId) {
        // GIVEN
        Employee employee1 = new Employee();
        employee1.setType(EmployeeType.CHEF);
        employee1.setPin("0000");
        employee1.setId(1);
        Employee employee2 = new Employee();
        employee2.setType(EmployeeType.WAITER);
        employee2.setPin("1111");
        employee2.setId(1);

        // WHEN
        doReturn(employee1).when(employeeQueryService).findByPin(employee1.getPin());
        doReturn(employee2).when(employeeQueryService).findByPin(employee2.getPin());

        // THEN
        assertThrows(InvalidEmployeeTypeException.class, () -> employeeOrderService.throwIfWaiterNotResponsible(pin, waiterId));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForThrowIfNotValidEmployeeType")
    public void throwIfNotValidEmployeeType_employeeNotValid_throwsInvalidEmployeeTypeException(Employee employee, OrderItem item) {
        assertThrows(InvalidEmployeeTypeException.class, () -> employeeOrderService.throwIfNotValidEmployeeType(employee, item));
    }

}
