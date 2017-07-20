package ro.teamnet.zth.appl.service;

import ro.teamnet.zth.appl.domain.Employee;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ramona.Raducu on 7/20/2017.
 */
public
interface EmployeeService {
        ArrayList<Employee> findAllEmployees();
        Employee getOneRandomEmployee();

}
