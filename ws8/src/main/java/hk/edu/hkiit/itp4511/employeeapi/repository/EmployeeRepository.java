package hk.edu.hkiit.itp4511.employeeapi.repository;

import hk.edu.hkiit.itp4511.employeeapi.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
