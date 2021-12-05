package com.ktsnvt.ktsnvt;

import com.ktsnvt.ktsnvt.model.*;
import com.ktsnvt.ktsnvt.model.enums.*;
import com.ktsnvt.ktsnvt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final RestaurantTableRepository tableRepo;
    private final BasePriceRepository basePriceRepo;
    private final PasswordEncoder passwordEncoder;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    public DbInitializer(AuthorityRepository authorityRepo, EmployeeRepository employeeRepo,
                         InventoryItemRepository inventoryItemRepo, MenuItemRepository menuItemRepo,
                         OrderItemGroupRepository orderItemGroupRepo,
                         OrderItemRepository orderItemRepo, OrderRepository orderRepo, SalaryRepository salaryRepo,
                         SectionRepository sectionRepo, SuperUserRepository superUserRepo,
                         RestaurantTableRepository tableRepo, BasePriceRepository basePriceRepo, PasswordEncoder passwordEncoder) {
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
        this.tableRepo = tableRepo;
        this.basePriceRepo = basePriceRepo;
        this.passwordEncoder = passwordEncoder;
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

        var salary1 = new Salary(LocalDate.parse("2021-01-01"), null, BigDecimal.valueOf(500), null);
        var salary2 = new Salary(LocalDate.parse("2021-01-01"), null, BigDecimal.valueOf(512), null);
        var salary3 = new Salary(LocalDate.parse("2021-01-01"), null, BigDecimal.valueOf(500), null);
        var salary4 = new Salary(LocalDate.parse("2021-12-01"), null, BigDecimal.valueOf(550), null);
        var salary5 = new Salary(LocalDate.parse("2021-12-01"), null, BigDecimal.valueOf(500), null);
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


        var manager1 = new SuperUser("Jasna", "Perovic", manager, "email1@email.com", passwordEncoder.encode("password"), SuperUserType.MANAGER);
        manager1.addSalary(salary4);
        var admin1 = new SuperUser("Nikola", "Stankovic", admin, "email2@email.com", passwordEncoder.encode("password"), SuperUserType.ADMIN);
        admin1.addSalary(salary5);
        var admin2 = new SuperUser("Vidoje", "Gavrilovic", admin, "vidojegavrilovic@email.com", passwordEncoder.encode("password"), SuperUserType.ADMIN);
        superUserRepo.save(manager1);
        superUserRepo.save(admin1);
        superUserRepo.save(admin2);

        var employee4 = new Employee("Rajka", "Jovanovic", bartender, "1212", EmployeeType.BARTENDER);
        employeeRepo.save(employee4);

        var basePrice1 = new BasePrice(LocalDateTime.parse("2021-01-01 12:12", formatter), null, BigDecimal.valueOf(30.00), null);
        var basePrice2 = new BasePrice(LocalDateTime.parse("2021-01-02 12:12", formatter), null, BigDecimal.valueOf(440.00), null);
        var basePrice3 = new BasePrice(LocalDateTime.parse("2021-01-03 12:12", formatter), null, BigDecimal.valueOf(50.00), null);
        var basePrice4 = new BasePrice(LocalDateTime.parse("2021-01-03 12:12", formatter), null, BigDecimal.valueOf(42.00), null);
        basePriceRepo.save(basePrice1);
        basePriceRepo.save(basePrice2);
        basePriceRepo.save(basePrice3);
        basePriceRepo.save(basePrice4);

        var item1 = new InventoryItem("Ice cream", "Description", "image", "Allergies", ItemCategory.FOOD);
        item1.addBasePrice(basePrice1);
        var item2 = new InventoryItem("T-bone steak", "Description", "image", "Allergies", ItemCategory.FOOD);
        item2.addBasePrice(basePrice2);
        var item3 = new InventoryItem("Orange juice", "Description", "image", "Allergies", ItemCategory.DRINK);
        item3.addBasePrice(basePrice3);
        var item4 = new InventoryItem("Pizza", "Description", "image", "Allergies", ItemCategory.FOOD);
        item4.addBasePrice(basePrice4);
        var item5 = new InventoryItem("Steak", "Description", "image", "Allergies", ItemCategory.FOOD);
        var item6 = new InventoryItem("Water", "Description", "image", "Allergies", ItemCategory.DRINK);
        var item7 = new InventoryItem("Pancake", "Description", "image", "Allergies", ItemCategory.FOOD);
        var item8 = new InventoryItem("Lemonade", "Description", "image", "Allergies", ItemCategory.DRINK);
        inventoryItemRepo.save(item1);
        inventoryItemRepo.save(item2);
        inventoryItemRepo.save(item3);
        inventoryItemRepo.save(item4);
        inventoryItemRepo.save(item5);
        inventoryItemRepo.save(item6);
        inventoryItemRepo.save(item7);
        inventoryItemRepo.save(item8);

        var menuItem1 = new MenuItem(BigDecimal.valueOf(100.00), LocalDateTime.parse("2021-01-01 12:12", formatter), null, item1);
        item1.addMenuItem(menuItem1);
        var menuItem2 = new MenuItem(BigDecimal.valueOf(500.00), LocalDateTime.parse("2021-01-01 12:12", formatter), null, item2);
        item2.addMenuItem(menuItem2);
        var menuItem3 = new MenuItem(BigDecimal.valueOf(100.00), LocalDateTime.parse("2021-01-01 12:12", formatter), null, item3);
        item3.addMenuItem(menuItem3);
        var menuItem4 = new MenuItem(BigDecimal.valueOf(322.00), LocalDateTime.parse("2021-01-01 12:12", formatter), null, item4);
        item4.addMenuItem(menuItem4);
        menuItemRepo.save(menuItem1);
        menuItemRepo.save(menuItem2);
        menuItemRepo.save(menuItem3);
        menuItemRepo.save(menuItem4);

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
        var restaurantTable11 = new RestaurantTable(4, 10, 10, 5, secondFloor);
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
        tableRepo.save(restaurantTable11);

        var order1 = new Order(CHARGED, LocalDateTime.parse("2021-01-01 12:12", formatter), LocalDateTime.parse("2021-01-01 12:15", formatter), restaurantTable1, employee3);
        var order2 = new Order(CREATED, LocalDateTime.parse("2021-02-02 15:15", formatter), null, restaurantTable1, employee3);
        var order3 = new Order(IN_PROGRESS, LocalDateTime.parse("2021-02-03 16:16", formatter), null, restaurantTable1, employee3);
        var order4 = new Order(CHARGED, LocalDateTime.parse("2021-11-14 12:12", formatter), LocalDateTime.parse("2021-11-14 13:15", formatter), restaurantTable1, employee3);
        var order5 = new Order(CHARGED, LocalDateTime.parse("2021-11-13 13:12", formatter), LocalDateTime.parse("2021-11-14 14:15", formatter), restaurantTable2, employee3);
        var order6 = new Order(IN_PROGRESS, LocalDateTime.parse("2021-02-03 16:16", formatter), null, restaurantTable1, employee3);
        var order7 = new Order(CANCELLED, LocalDateTime.parse("2021-02-03 16:16", formatter), null, restaurantTable1, employee3);
        var order8 = new Order(CREATED, LocalDateTime.parse("2021-02-03 16:16", formatter), null, restaurantTable1, employee3);
        orderRepo.save(order1);
        orderRepo.save(order2);
        orderRepo.save(order3);
        orderRepo.save(order4);
        orderRepo.save(order5);
        orderRepo.save(order6);
        orderRepo.save(order7);
        orderRepo.save(order8);

        var orderGroup1 = new OrderItemGroup("Group 1", OrderItemGroupStatus.SENT, null);
        order1.addOrderItemGroup(orderGroup1);
        orderItemGroupRepo.save(orderGroup1);

        var orderGroup2 = new OrderItemGroup("Group 2", OrderItemGroupStatus.NEW, null);
        order1.addOrderItemGroup(orderGroup2);
        orderItemGroupRepo.save(orderGroup2);

        var orderGroup3 = new OrderItemGroup("Group 3", OrderItemGroupStatus.DONE, null);
        order3.addOrderItemGroup(orderGroup3);
        orderItemGroupRepo.save(orderGroup3);

        var orderGroup4 = new OrderItemGroup("Group 4", OrderItemGroupStatus.DONE, null);
        order4.addOrderItemGroup(orderGroup4);
        orderItemGroupRepo.save(orderGroup4);

        var orderGroup5 = new OrderItemGroup("Group 5", OrderItemGroupStatus.NEW, null);
        order2.addOrderItemGroup(orderGroup5);
        orderItemGroupRepo.save(orderGroup5);

        var orderGroup6 = new OrderItemGroup("Group 6", OrderItemGroupStatus.DONE, null);
        order3.addOrderItemGroup(orderGroup6);
        orderItemGroupRepo.save(orderGroup6);

        var orderGroup7 = new OrderItemGroup("Group 7", OrderItemGroupStatus.SENT, null);
        order6.addOrderItemGroup(orderGroup7);
        orderItemGroupRepo.save(orderGroup7);

//        var orderGroup5 = new OrderItemGroup("Group 5", OrderItemGroupStatus.SENT, null);
//        order6.addOrderItemGroup(orderGroup5);
//        orderItemGroupRepo.save(orderGroup5);


        var orderItem1 = new OrderItem(2, orderGroup1, null, menuItem1, OrderItemStatus.SENT);
        var orderItem2 = new OrderItem(1, orderGroup1, null, menuItem2, OrderItemStatus.SENT);
        var orderItem3 = new OrderItem(1, orderGroup1, null, menuItem3, OrderItemStatus.SENT);
        var orderItem4 = new OrderItem(1, orderGroup1, null, menuItem3, OrderItemStatus.SENT);

        var orderItem5 = new OrderItem(1, orderGroup3, null, menuItem3, OrderItemStatus.DONE);
        var orderItem6 = new OrderItem(1, orderGroup3, null, menuItem3, OrderItemStatus.DONE);
        var orderItem7 = new OrderItem(1, orderGroup3, null, menuItem3, OrderItemStatus.DONE);

        var orderItem8 = new OrderItem(1, orderGroup4, null, menuItem1, OrderItemStatus.DONE);
        var orderItem9 = new OrderItem(1, orderGroup4, null, menuItem3, OrderItemStatus.DONE);

        var orderItem10 = new OrderItem(1, orderGroup1, employee1, menuItem1, OrderItemStatus.PREPARING);
        var orderItem11 = new OrderItem(1, orderGroup1, employee1, menuItem3, OrderItemStatus.PREPARING);

        var orderItem12 = new OrderItem(1, orderGroup2, null, menuItem1, OrderItemStatus.NEW);

        var orderItem13 = new OrderItem(1, orderGroup1, null, menuItem3, OrderItemStatus.SENT);
        var orderItem14 = new OrderItem(1, orderGroup1, employee4, menuItem3, OrderItemStatus.PREPARING);

        // orderItem1.setPreparedBy(employee2);
        orderGroup1.addItem(orderItem1);
        orderItem1.setSentAt(LocalDateTime.of(2021, 1, 15, 12, 12));
        orderGroup1.addItem(orderItem2);
        orderItem2.setSentAt(LocalDateTime.of(2021, 1, 15, 12, 12));
        orderGroup1.addItem(orderItem3);
        orderItem3.setSentAt(LocalDateTime.of(2021, 1, 15, 12, 12));
        orderGroup1.addItem(orderItem4);
        orderItem4.setSentAt(LocalDateTime.of(2021, 1, 15, 12, 12));
        orderItem4.setTakenAt(LocalDateTime.of(2021, 1, 15, 12, 20));

        orderGroup1.addItem(orderItem13);
        orderItem13.setSentAt(LocalDateTime.of(2021, 1, 15, 12, 12));
        orderGroup1.addItem(orderItem14);
        orderItem14.setSentAt(LocalDateTime.of(2021, 1, 15, 12, 12));

        orderGroup1.addItem(orderItem10);
        orderItem10.setSentAt(LocalDateTime.of(2021, 1, 15, 12, 20));
        orderItem10.setTakenAt(LocalDateTime.of(2021, 1, 15, 12, 20));
        orderGroup1.addItem(orderItem11);
        orderItem11.setSentAt(LocalDateTime.of(2021, 1, 15, 12, 20));
        orderItem11.setTakenAt(LocalDateTime.of(2021, 1, 15, 12, 20));

        orderGroup2.addItem(orderItem12);

        orderGroup3.addItem(orderItem5);
        orderGroup3.addItem(orderItem6);
        orderGroup3.addItem(orderItem7);

        orderGroup4.addItem(orderItem8);
        orderGroup4.addItem(orderItem9);

        // for testing reports
        order4.setTotalCost(BigDecimal.valueOf(120));
        order4.setTotalIncome(BigDecimal.valueOf(322));
        order5.setTotalCost(BigDecimal.valueOf(42));
        order5.setTotalIncome(BigDecimal.valueOf(3140));


        orderItemRepo.save(orderItem1);
        orderItemRepo.save(orderItem2);
        orderItemRepo.save(orderItem3);
        orderItemRepo.save(orderItem4);
        orderItemRepo.save(orderItem5);
        orderItemRepo.save(orderItem6);
        orderItemRepo.save(orderItem7);
        orderItemRepo.save(orderItem8);
        orderItemRepo.save(orderItem9);
        orderItemRepo.save(orderItem10);
        orderItemRepo.save(orderItem11);
        orderItemRepo.save(orderItem12);
        orderItemRepo.save(orderItem13);
        orderItemRepo.save(orderItem14);
    }
}
