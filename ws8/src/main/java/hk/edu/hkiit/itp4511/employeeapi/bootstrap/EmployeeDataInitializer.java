package hk.edu.hkiit.itp4511.employeeapi.bootstrap;

import hk.edu.hkiit.itp4511.employeeapi.model.Employee;
import hk.edu.hkiit.itp4511.employeeapi.repository.EmployeeRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EmployeeDataInitializer implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeDataInitializer(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

     // methods continue on next slide

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n=== 🚀 Loading Sample Employee Data ===");

        // Clear any previous data (ensures a fresh, consistent dataset on every restart)
        employeeRepository.deleteAll();

        // Create a clean list of employees
        Employee emp1 = new Employee();
        emp1.setFirstName("Alice");
        emp1.setLastName("Johnson");
        emp1.setEmail("alice.johnson@example.com");

        Employee emp2 = new Employee();
        emp2.setFirstName("Bob");
        emp2.setLastName("Smith");
        emp2.setEmail("bob.smith@example.com");

        Employee emp3 = new Employee();
        emp3.setFirstName("Charlie");
        emp3.setLastName("Brown");
        emp3.setEmail("charlie.brown@example.com");

        Employee emp4 = new Employee();
        emp4.setFirstName("Diana");
        emp4.setLastName("Prince");
        emp4.setEmail("diana.prince@example.com");

        // Save all employees at once
        employeeRepository.saveAll(List.of(emp1, emp2, emp3, emp4));

        long count = employeeRepository.count();
        System.out.println("✅ Successfully loaded " + count + " sample employees into the database.\n");
    }
}
