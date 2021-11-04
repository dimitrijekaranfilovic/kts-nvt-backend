package com.ktsnvt.ktsnvt.scripts;

import com.ktsnvt.ktsnvt.model.*;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import com.ktsnvt.ktsnvt.model.enums.SuperUserType;
import com.ktsnvt.ktsnvt.repository.*;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DbInitialiser implements ApplicationRunner {

    private final AuthorityRepository authorityRepo;
    private final EmployeeRepository employeeRepo;
    private final InventoryItemRepository inventoryItemRepo;
    private final MenuItemRepository menuItemRepo;
    private final MenuRepository menuRepo;
    private final OrderItemGroupRepository orderItemGroupRepo;
    private final OrderItemRepository orderItemRepo;
    private final OrderRepository orderRepo;
    private final SalaryRepository salaryRepo;
    private final SectionRepository sectionRepo;
    private final SuperUserRepository superUserRepo;
    private final UserRepository userRepo;
    private final TableRepository tableRepo;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    public DbInitialiser(AuthorityRepository authorityRepo, EmployeeRepository employeeRepo, InventoryItemRepository inventoryItemRepo, MenuItemRepository menuItemRepo, MenuRepository menuRepo, OrderItemGroupRepository orderItemGroupRepo, OrderItemRepository orderItemRepo, OrderRepository orderRepo, SalaryRepository salaryRepo, SectionRepository sectionRepo, SuperUserRepository superUserRepo, UserRepository userRepo, TableRepository tableRepo) {
        this.authorityRepo = authorityRepo;
        this.employeeRepo = employeeRepo;
        this.inventoryItemRepo = inventoryItemRepo;
        this.menuItemRepo = menuItemRepo;
        this.menuRepo = menuRepo;
        this.orderItemGroupRepo = orderItemGroupRepo;
        this.orderItemRepo = orderItemRepo;
        this.orderRepo = orderRepo;
        this.salaryRepo = salaryRepo;
        this.sectionRepo = sectionRepo;
        this.superUserRepo = superUserRepo;
        this.userRepo = userRepo;
        this.tableRepo = tableRepo;
    }

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

        Salary salary1 = new Salary(LocalDate.parse("2021-01-01"), null, 500.00, null);
        Salary salary2 = new Salary(LocalDate.parse("2021-01-01"), null, 500.00, null);
        Salary salary3 = new Salary(LocalDate.parse("2021-01-01"), null, 500.00, null);
        Salary salary4 = new Salary(LocalDate.parse("2021-01-01"), null, 500.00, null);
        Salary salary5 = new Salary(LocalDate.parse("2021-01-01"), null, 500.00, null);
        salaryRepo.save(salary1);
        salaryRepo.save(salary2);
        salaryRepo.save(salary3);
        salaryRepo.save(salary4);
        salaryRepo.save(salary5);

        Employee employee1 = new Employee("Jovan", "Jovovic", chef, 1234, EmployeeType.CHEF);
        employee1.addSalary(salary1);
        Employee employee2 = new Employee("Svetlana", "Markovic", bartender, 5678, EmployeeType.BARTENDER);
        employee2.addSalary(salary2);
        Employee employee3 = new Employee("Marko", "Kovacevic", waiter, 4321, EmployeeType.WAITER);
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

        Menu menu1 = new Menu(LocalDate.parse("2021-01-01"), null);
        menu1.getMenuItems().add(menuItem1);
        menu1.getMenuItems().add(menuItem2);
        menu1.getMenuItems().add(menuItem3);
        menuRepo.save(menu1);

        Section groundFloor = new Section("Ground Floor");
        Section firstFloor = new Section("1st Floor");
        Section terrace = new Section("Terrace");
        sectionRepo.save(groundFloor);
        sectionRepo.save(firstFloor);
        sectionRepo.save(terrace);

        RestaurantTable restaurantTable1 = new RestaurantTable(1, 0, 0, 0, groundFloor);
        RestaurantTable restaurantTable2 = new RestaurantTable(2, 0, 0, 0, groundFloor);
        RestaurantTable restaurantTable3 = new RestaurantTable(3, 0, 0, 0, groundFloor);
        RestaurantTable restaurantTable4 = new RestaurantTable(1, 0, 0, 0, firstFloor);
        RestaurantTable restaurantTable5 = new RestaurantTable(2, 0, 0, 0, firstFloor);
        RestaurantTable restaurantTable6 = new RestaurantTable(3, 0, 0, 0, firstFloor);
        RestaurantTable restaurantTable7 = new RestaurantTable(1, 0, 0, 0, terrace);
        RestaurantTable restaurantTable8 = new RestaurantTable(2, 0, 0, 0, terrace);
        RestaurantTable restaurantTable9 = new RestaurantTable(3, 0, 0, 0, terrace);
        tableRepo.save(restaurantTable1);
        tableRepo.save(restaurantTable2);
        tableRepo.save(restaurantTable3);
        tableRepo.save(restaurantTable4);
        tableRepo.save(restaurantTable5);
        tableRepo.save(restaurantTable6);
        tableRepo.save(restaurantTable7);
        tableRepo.save(restaurantTable8);
        tableRepo.save(restaurantTable9);

        Order order1 = new Order(true, LocalDateTime.parse("2021-01-01 12:12", formatter), LocalDateTime.parse("2021-01-01 12:15", formatter), restaurantTable1, employee3);
        Order order2 = new Order(false, LocalDateTime.now(), null, restaurantTable1, employee3);
        Order order3 = new Order(false, LocalDateTime.now(), null, restaurantTable1, employee3);
        orderRepo.save(order1);
        orderRepo.save(order2);
        orderRepo.save(order3);

        var page = orderRepo.examplePage(PageRequest.of(0, 2));
        System.out.println(page.getTotalPages());

    }
}
