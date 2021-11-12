package com.ktsnvt.ktsnvt;

import com.ktsnvt.ktsnvt.model.*;
import com.ktsnvt.ktsnvt.model.enums.*;
import com.ktsnvt.ktsnvt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.ktsnvt.ktsnvt.model.enums.OrderStatus.*;

@Component
public class DbInitializer implements ApplicationRunner {
    private final AuthorityRepository authorityRepo;
    private final EmployeeRepository employeeRepo;
    private final InventoryItemRepository inventoryItemRepo;
    private final MenuItemRepository menuItemRepo;
    private final OrderItemGroupRepository orderItemGroupRepo;
    private final OrderItemRepository orderItemRepo;
    private final OrderRepository orderRepo;
    private final SalaryRepository salaryRepo;
    private final SectionRepository sectionRepo;
    private final SuperUserRepository superUserRepo;
    private final UserRepository userRepo;
    private final RestaurantTableRepository tableRepo;
    private final BasePriceRepository basePriceRepo;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    public DbInitializer(AuthorityRepository authorityRepo, EmployeeRepository employeeRepo,
                         InventoryItemRepository inventoryItemRepo, MenuItemRepository menuItemRepo,
                         OrderItemGroupRepository orderItemGroupRepo,
                         OrderItemRepository orderItemRepo, OrderRepository orderRepo, SalaryRepository salaryRepo,
                         SectionRepository sectionRepo, SuperUserRepository superUserRepo, UserRepository userRepo,
                         RestaurantTableRepository tableRepo, BasePriceRepository basePriceRepo) {
        this.authorityRepo = authorityRepo;
        this.employeeRepo = employeeRepo;
        this.inventoryItemRepo = inventoryItemRepo;
        this.menuItemRepo = menuItemRepo;
        this.orderItemGroupRepo = orderItemGroupRepo;
        this.orderItemRepo = orderItemRepo;
        this.orderRepo = orderRepo;
        this.salaryRepo = salaryRepo;
        this.sectionRepo = sectionRepo;
        this.superUserRepo = superUserRepo;
        this.userRepo = userRepo;
        this.tableRepo = tableRepo;
        this.basePriceRepo = basePriceRepo;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        var chef = new Authority("CHEF");
        var bartender = new Authority("BARTENDER");
        var waiter = new Authority("WAITER");
        var manager = new Authority("MANAGER");
        var admin = new Authority("ADMIN");
        authorityRepo.save(chef);
        authorityRepo.save(bartender);
        authorityRepo.save(waiter);
        authorityRepo.save(manager);
        authorityRepo.save(admin);

        var salary1 = new Salary(LocalDate.parse("2021-01-01"), null, new BigDecimal("500.00"), null);
        var salary2 = new Salary(LocalDate.parse("2021-01-01"), null, new BigDecimal("512.00"), null);
        var salary3 = new Salary(LocalDate.parse("2021-01-01"), null, new BigDecimal("500.00"), null);
        var salary4 = new Salary(LocalDate.parse("2021-01-01"), null, new BigDecimal("550.00"), null);
        var salary5 = new Salary(LocalDate.parse("2021-01-01"), null, new BigDecimal("500.00"), null);
        salaryRepo.save(salary1);
        salaryRepo.save(salary2);
        salaryRepo.save(salary3);
        salaryRepo.save(salary4);
        salaryRepo.save(salary5);

        var employee1 = new Employee("Jovan", "Jovovic", chef, "1234", EmployeeType.CHEF);
        employee1.addSalary(salary1);
        var employee2 = new Employee("Svetlana", "Markovic", bartender, "5678", EmployeeType.BARTENDER);
        employee2.addSalary(salary2);
        var employee3 = new Employee("Marko", "Kovacevic", waiter, "4321", EmployeeType.WAITER);
        employee3.addSalary(salary3);
        employeeRepo.save(employee1);
        employeeRepo.save(employee2);
        employeeRepo.save(employee3);

        var manager1 = new SuperUser("Jasna", "Perovic", manager, "email1@email.com", "password", SuperUserType.MANAGER);
        manager1.addSalary(salary4);
        var admin1 = new SuperUser("Nikola", "Stankovic", admin, "email2@email.com", "password", SuperUserType.ADMIN);
        admin1.addSalary(salary5);
        superUserRepo.save(manager1);
        superUserRepo.save(admin1);

        var basePrice1 = new BasePrice(LocalDateTime.parse("2021-01-01 12:12", formatter), null, BigDecimal.valueOf(30.00), null);
        var basePrice2 = new BasePrice(LocalDateTime.parse("2021-01-02 12:12", formatter), null, BigDecimal.valueOf(440.00), null);
        var basePrice3 = new BasePrice(LocalDateTime.parse("2021-01-03 12:12", formatter), null, BigDecimal.valueOf(50.00), null);
        basePriceRepo.save(basePrice1);
        basePriceRepo.save(basePrice2);
        basePriceRepo.save(basePrice3);

        var item1 = new InventoryItem("Ice cream", "Description", "image", "Allergies", ItemCategory.FOOD);
        item1.addBasePrice(basePrice1);
        var item2 = new InventoryItem("T-bone steak", "Description", "image", "Allergies", ItemCategory.FOOD);
        item2.addBasePrice(basePrice2);
        var item3 = new InventoryItem("Orange juice", "Description", "image", "Allergies", ItemCategory.DRINK);
        item3.addBasePrice(basePrice3);
        inventoryItemRepo.save(item1);
        inventoryItemRepo.save(item2);
        inventoryItemRepo.save(item3);

        var menuItem1 = new MenuItem(BigDecimal.valueOf(100.00), LocalDateTime.parse("2021-01-01 12:12", formatter), null, item1);
        var menuItem2 = new MenuItem(BigDecimal.valueOf(500.00), LocalDateTime.parse("2021-01-01 12:12", formatter), null, item2);
        var menuItem3 = new MenuItem(BigDecimal.valueOf(100.00), LocalDateTime.parse("2021-01-01 12:12", formatter), null, item3);
        menuItemRepo.save(menuItem1);
        menuItemRepo.save(menuItem2);
        menuItemRepo.save(menuItem3);

        var groundFloor = new Section("Ground Floor");
        var firstFloor = new Section("1st Floor");
        var terrace = new Section("Terrace");
        var secondFloor = new Section("2nd Floor");
        sectionRepo.save(groundFloor);
        sectionRepo.save(firstFloor);
        sectionRepo.save(terrace);
        sectionRepo.save(secondFloor);

        var restaurantTable1 = new RestaurantTable(1, 20, 20, 5, groundFloor);
        var restaurantTable2 = new RestaurantTable(2, 0, 0, 5, groundFloor);
        var restaurantTable3 = new RestaurantTable(3, 80, 80, 5, groundFloor);
        var restaurantTable4 = new RestaurantTable(1, 0, 0, 5, firstFloor);
        var restaurantTable5 = new RestaurantTable(2, 0, 0, 5, firstFloor);
        var restaurantTable6 = new RestaurantTable(3, 0, 0, 5, firstFloor);
        var restaurantTable7 = new RestaurantTable(1, 0, 0, 5, terrace);
        var restaurantTable8 = new RestaurantTable(2, 0, 0, 5, terrace);
        var restaurantTable9 = new RestaurantTable(3, 0, 0, 5, terrace);
        var restaurantTable10 = new RestaurantTable(3, 0, 0, 5, secondFloor);
        restaurantTable10.setAvailable(false);
        tableRepo.save(restaurantTable1);
        tableRepo.save(restaurantTable2);
        tableRepo.save(restaurantTable3);
        tableRepo.save(restaurantTable4);
        tableRepo.save(restaurantTable5);
        tableRepo.save(restaurantTable6);
        tableRepo.save(restaurantTable7);
        tableRepo.save(restaurantTable8);
        tableRepo.save(restaurantTable9);
        tableRepo.save(restaurantTable10);

        var order1 = new Order(CHARGED, LocalDateTime.parse("2021-01-01 12:12", formatter), LocalDateTime.parse("2021-01-01 12:15", formatter), restaurantTable1, employee3);
        var order2 = new Order(CREATED, LocalDateTime.now(), null, restaurantTable1, employee3);
        var order3 = new Order(IN_PROGRESS, LocalDateTime.now(), null, restaurantTable1, employee3);
        orderRepo.save(order1);
        orderRepo.save(order2);
        orderRepo.save(order3);

        var orderGroup1 = new OrderItemGroup("Group 1", OrderItemGroupStatus.SENT, null);
        order1.addOrderItemGroup(orderGroup1);
        orderItemGroupRepo.save(orderGroup1);

        var orderGroup2 = new OrderItemGroup("Group 2", OrderItemGroupStatus.NEW, null);
        order1.addOrderItemGroup(orderGroup2);
        orderItemGroupRepo.save(orderGroup2);

        var orderGroup3 = new OrderItemGroup("Group 1", OrderItemGroupStatus.SENT, null);
        order2.addOrderItemGroup(orderGroup3);
        orderItemGroupRepo.save(orderGroup3);


        var orderItem1 = new OrderItem(2, orderGroup1, null, menuItem1, OrderItemStatus.SENT);
        var orderItem2 = new OrderItem(1, orderGroup1, null, menuItem2, OrderItemStatus.SENT);
        var orderItem3 = new OrderItem(1, orderGroup1, null, menuItem3, OrderItemStatus.SENT);
        var orderItem4 = new OrderItem(1, orderGroup1, null, menuItem3, OrderItemStatus.SENT);

        var orderItem5 = new OrderItem(1, orderGroup2, null, menuItem3, OrderItemStatus.NEW);
        var orderItem6 = new OrderItem(1, orderGroup2, null, menuItem3, OrderItemStatus.NEW);
        var orderItem7 = new OrderItem(1, orderGroup2, null, menuItem3, OrderItemStatus.NEW);


        // orderItem1.setPreparedBy(employee2);
        orderGroup1.addItem(orderItem1);
        orderItem1.setSentAt(LocalDateTime.now());
        orderGroup1.addItem(orderItem2);
        orderItem2.setSentAt(LocalDateTime.now());
        orderGroup1.addItem(orderItem3);
        orderItem3.setSentAt(LocalDateTime.now());
        orderGroup1.addItem(orderItem4);
        orderItem4.setSentAt(LocalDateTime.now());

        orderGroup2.addItem(orderItem5);
        orderGroup2.addItem(orderItem6);
        orderGroup2.addItem(orderItem7);

        orderItemRepo.save(orderItem1);
        orderItemRepo.save(orderItem2);
        orderItemRepo.save(orderItem3);
        orderItemRepo.save(orderItem4);
        orderItemRepo.save(orderItem5);
        orderItemRepo.save(orderItem6);
        orderItemRepo.save(orderItem7);


    }
}
