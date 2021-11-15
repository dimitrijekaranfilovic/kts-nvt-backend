package com.ktsnvt.ktsnvt.service.impl;

import com.ktsnvt.ktsnvt.exception.InvalidEmployeeTypeException;
import com.ktsnvt.ktsnvt.model.Employee;
import com.ktsnvt.ktsnvt.model.OrderItem;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import com.ktsnvt.ktsnvt.service.EmployeeOrderService;
import com.ktsnvt.ktsnvt.service.EmployeeQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeOrderServiceImpl implements EmployeeOrderService {
    private final EmployeeQueryService employeeQueryService;

    @Autowired
    public EmployeeOrderServiceImpl(EmployeeQueryService employeeQueryService) {
        this.employeeQueryService = employeeQueryService;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void throwIfWaiterNotResponsible(String pin, Integer waiterId) {
        var employee = employeeQueryService.findByPin(pin);
        if(employee.getType() != EmployeeType.WAITER) {
            throw new InvalidEmployeeTypeException(pin);
        }
        if(!employee.getId().equals(waiterId)){
            throw new InvalidEmployeeTypeException(pin);
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void throwIfNotValidEmployeeType(Employee employee, OrderItem item) {
        var employeeType = employee.getType();
        var itemCategory = item.getItem().getItem().getCategory();
        if ((employeeType == EmployeeType.CHEF && itemCategory  == ItemCategory.DRINK)
                || (employeeType == EmployeeType.BARTENDER && itemCategory == ItemCategory.FOOD)) {
            throw new InvalidEmployeeTypeException(employee.getPin());
        }
    }
}
