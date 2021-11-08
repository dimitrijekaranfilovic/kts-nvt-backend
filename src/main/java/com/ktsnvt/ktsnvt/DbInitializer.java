package com.ktsnvt.ktsnvt;

import com.ktsnvt.ktsnvt.model.*;
import com.ktsnvt.ktsnvt.model.enums.EmployeeType;
import com.ktsnvt.ktsnvt.model.enums.ItemCategory;
import com.ktsnvt.ktsnvt.model.enums.SuperUserType;
import com.ktsnvt.ktsnvt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
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
    public DbInitializer(AuthorityRepository authorityRepo, EmployeeRepository employeeRepo, InventoryItemRepository inventoryItemRepo, MenuItemRepository menuItemRepo, MenuRepository menuRepo, OrderItemGroupRepository orderItemGroupRepo, OrderItemRepository orderItemRepo, OrderRepository orderRepo, SalaryRepository salaryRepo, SectionRepository sectionRepo, SuperUserRepository superUserRepo, UserRepository userRepo, TableRepository tableRepo) {
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

        var salary1 = new Salary(LocalDate.parse("2021-01-01"), null, 500.00, null);
        var salary2 = new Salary(LocalDate.parse("2021-01-01"), null, 500.00, null);
        var salary3 = new Salary(LocalDate.parse("2021-01-01"), null, 500.00, null);
        var salary4 = new Salary(LocalDate.parse("2021-01-01"), null, 500.00, null);
        var salary5 = new Salary(LocalDate.parse("2021-01-01"), null, 500.00, null);
        salaryRepo.save(salary1);
        salaryRepo.save(salary2);
        salaryRepo.save(salary3);
        salaryRepo.save(salary4);
        salaryRepo.save(salary5);

        var employee1 = new Employee("Jovan", "Jovovic", chef, 1234, EmployeeType.CHEF);
        employee1.addSalary(salary1);
        var employee2 = new Employee("Svetlana", "Markovic", bartender, 5678, EmployeeType.BARTENDER);
        employee2.addSalary(salary2);
        var employee3 = new Employee("Marko", "Kovacevic", waiter, 4321, EmployeeType.WAITER);
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

        var item1 = new InventoryItem("Ice cream", 70.00, "Description", "image", "Allergies", ItemCategory.FOOD);
        var item2 = new InventoryItem("T-bone steak", 440.00, "Description", "image", "Allergies", ItemCategory.FOOD);
        var item3 = new InventoryItem("Orange juice", 50.00, "Description", "image", "Allergies", ItemCategory.DRINK);
        inventoryItemRepo.save(item1);
        inventoryItemRepo.save(item2);
        inventoryItemRepo.save(item3);

        var menuItem1 = new MenuItem(100.00, item1);
        var menuItem2 = new MenuItem(500.00, item2);
        var menuItem3 = new MenuItem(100.00, item3);
        menuItemRepo.save(menuItem1);
        menuItemRepo.save(menuItem2);
        menuItemRepo.save(menuItem3);

        var menu1 = new Menu(LocalDate.parse("2021-01-01"), null);
        menu1.getMenuItems().add(menuItem1);
        menu1.getMenuItems().add(menuItem2);
        menu1.getMenuItems().add(menuItem3);
        menuRepo.save(menu1);

        var groundFloor = new Section("Ground Floor");
        var firstFloor = new Section("1st Floor");
        var terrace = new Section("Terrace");
        sectionRepo.save(groundFloor);
        sectionRepo.save(firstFloor);
        sectionRepo.save(terrace);

        var restaurantTable1 = new RestaurantTable(1, 0, 0, 0, groundFloor);
        var restaurantTable2 = new RestaurantTable(2, 0, 0, 0, groundFloor);
        var restaurantTable3 = new RestaurantTable(3, 0, 0, 0, groundFloor);
        var restaurantTable4 = new RestaurantTable(1, 0, 0, 0, firstFloor);
        var restaurantTable5 = new RestaurantTable(2, 0, 0, 0, firstFloor);
        var restaurantTable6 = new RestaurantTable(3, 0, 0, 0, firstFloor);
        var restaurantTable7 = new RestaurantTable(1, 0, 0, 0, terrace);
        var restaurantTable8 = new RestaurantTable(2, 0, 0, 0, terrace);
        var restaurantTable9 = new RestaurantTable(3, 0, 0, 0, terrace);
        tableRepo.save(restaurantTable1);
        tableRepo.save(restaurantTable2);
        tableRepo.save(restaurantTable3);
        tableRepo.save(restaurantTable4);
        tableRepo.save(restaurantTable5);
        tableRepo.save(restaurantTable6);
        tableRepo.save(restaurantTable7);
        tableRepo.save(restaurantTable8);
        tableRepo.save(restaurantTable9);

        var order1 = new Order(CHARGED, LocalDateTime.parse("2021-01-01 12:12", formatter), LocalDateTime.parse("2021-01-01 12:15", formatter), restaurantTable1, employee3);
        var order2 = new Order(CREATED, LocalDateTime.now(), null, restaurantTable1, employee3);
        var order3 = new Order(IN_PROGRESS, LocalDateTime.now(), null, restaurantTable1, employee3);
        orderRepo.save(order1);
        orderRepo.save(order2);
        orderRepo.save(order3);

        var page = orderRepo.examplePage(PageRequest.of(0, 2));
        System.out.println(page.getTotalPages());

    }
}
