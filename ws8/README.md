# Employee API Workshop

A Spring Boot 3.5.x RESTful Employee Management API using a layered architecture:

- Model: `Employee`
- Repository: `EmployeeRepository`
- Service: `EmployeeService`
- Controller: `EmployeeController`
- Bootstrap: `EmployeeDataInitializer`

## Run

```bash
mvn spring-boot:run
```

## Test CRUD endpoints

Create:

```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Alice","lastName":"Smith","email":"alice@example.com"}'
```

List:

```bash
curl http://localhost:8080/api/employees
```

Get by ID:

```bash
curl http://localhost:8080/api/employees/1
```

Update:

```bash
curl -X PUT http://localhost:8080/api/employees/1 \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Alice","lastName":"Johnson","email":"alice.johnson@example.com"}'
```

Delete:

```bash
curl -X DELETE http://localhost:8080/api/employees/1
```

## Notes

- Java package names cannot contain hyphens, so the code uses `hk.edu.hkiit.itp4511.employeeapi` instead of `hk.edu.hkiit.itp4511.employee-api`.
- H2 console is enabled at `/h2-console`.
