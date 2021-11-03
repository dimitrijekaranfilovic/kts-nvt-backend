package com.ktsnvt.ktsnvt.scripts;

import com.ktsnvt.ktsnvt.model.*;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import com.ktsnvt.ktsnvt.model.enums.SuperUserType;
import com.ktsnvt.ktsnvt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashSet;

@Component
public class DbInitialiser implements ApplicationRunner {

    @Autowired
    private AuthorityRepository authorityRepo;
    @Autowired
    private EmployeeRepository employeeRepo;
    @Autowired
    private InventoryItemRepository inventoryItemRepo;
    @Autowired
    private MenuItemRepository menuItemRepo;
    @Autowired
    private OrderItemGroupRepository orderItemGroupRepo;
    @Autowired
    private OrderItemRepository orderItemRepo;
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private SalaryRepository salaryRepo;
    @Autowired
    private SectionRepository sectionRepo;
    @Autowired
    private SuperUserRepository superUserRepo;
    @Autowired
    private TableRepository tableRepo;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        Authority chef = new Authority("CHEF");
        Authority bartender = new Authority("BARTENDER");
        Authority waiter = new Authority("WAITER");
        Authority manager = new Authority("MANAGER");
        Authority admin = new Authority("ADMIN");
        authorityRepo.save(chef);
        authorityRepo.save(bartender);
        authorityRepo.save(waiter);
        authorityRepo.save(manager);
        authorityRepo.save(admin);

        Salary salary1 = new Salary(LocalDate.parse("2021-01-01"), null, 500.00,null);
        Salary salary2 = new Salary(LocalDate.parse("2021-01-01"), null, 500.00,null);
        Salary salary3 = new Salary(LocalDate.parse("2021-01-01"), null, 500.00,null);
        Salary salary4 = new Salary(LocalDate.parse("2021-01-01"), null, 500.00,null);
        Salary salary5 = new Salary(LocalDate.parse("2021-01-01"), null, 500.00,null);
        salaryRepo.save(salary1);
        salaryRepo.save(salary2);
        salaryRepo.save(salary3);
        salaryRepo.save(salary4);
        salaryRepo.save(salary5);

        Employee employee1 = new Employee("Jovan", "Jovovic", chef, 1234, EmployeeType.CHEF);
        employee1.addSalary(salary1);
        Employee employee2 = new Employee("Svetlana", "Markovic", bartender, 5678, EmployeeType.CHEF);
        employee2.addSalary(salary2);
        Employee employee3 = new Employee("Marko", "Kovacevic", waiter, 4321, EmployeeType.CHEF);
        employee3.addSalary(salary3);
        employeeRepo.save(employee1);
        employeeRepo.save(employee2);
        employeeRepo.save(employee3);

        SuperUser manager1 = new SuperUser("Jasna", "Perovic", manager, "email1@email.com", "password", SuperUserType.MANAGER);
        manager1.addSalary(salary4);
        SuperUser admin1 = new SuperUser("Nikola", "Stankovic", admin, "email2@email.com", "password", SuperUserType.ADMIN);
        admin1.addSalary(salary5);
        superUserRepo.save(manager1);
        superUserRepo.save(admin1);

        InventoryItem item1 = new InventoryItem("Ice cream", 70.00, "Description", "image", "Allergies", ItemCategory.FOOD);
        InventoryItem item2 = new InventoryItem("T-bone steak", 440.00, "Description", "image", "Allergies", ItemCategory.FOOD);
        InventoryItem item3 = new InventoryItem("Orange juice", 50.00, "Description", "image", "Allergies", ItemCategory.DRINK);
        inventoryItemRepo.save(item1);
        inventoryItemRepo.save(item2);
        inventoryItemRepo.save(item3);

        MenuItem menuItem1 = new MenuItem(100.00, item1);
        MenuItem menuItem2 = new MenuItem(500.00, item2);
        MenuItem menuItem3 = new MenuItem(100.00, item3);
        menuItemRepo.save(menuItem1);
        menuItemRepo.save(menuItem2);
        menuItemRepo.save(menuItem3);

        Menu menu1 = new Menu(LocalDate.parse("2021-01-01"), null, new HashSet<>());
        menu1.getMenuItems().add(menuItem1);
        menu1.getMenuItems().add(menuItem2);
        menu1.getMenuItems().add(menuItem3);


    }
}
