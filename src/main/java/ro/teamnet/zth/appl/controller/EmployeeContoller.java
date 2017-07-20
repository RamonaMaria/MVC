package ro.teamnet.zth.appl.controller;

import ro.teamnet.zth.api.annotations.MyController;
import ro.teamnet.zth.api.annotations.MyRequestMethod;
import ro.teamnet.zth.appl.domain.Employee;
import ro.teamnet.zth.appl.service.EmployeeService;

import java.util.ArrayList;

/**
 * Created by Ramona.Raducu on 7/20/2017.
 */
@MyController(urlPath = "/employees")
public
class EmployeeContoller {
    EmployeeService employeeService;
    private String allEmployees;
    private String oneRandomEmployee;

    public
    EmployeeContoller(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @MyRequestMethod(urlPath = "/all", methodType = "GET")
    public
    ArrayList<Employee> getAllEmployees() {
        return employeeService.findAllEmployees();
    }

    // SAU
    //    @MyRequestMethod(urlPath = "/all", methodType = "GET")
    //    public
    //    String getAllEmployees() {
    //        return allEmployees;
    //    }

    @MyRequestMethod(urlPath = "/one", methodType = "GET")
    public Employee getOneEmployee () {
        return employeeService.getOneRandomEmployee();
    }

    // SAU
    //    @MyRequestMethod(urlPath = "/one", methodType = "GET")
    //    public String getOneEmployee () {
    //        return oneRandomEmployee;
    //    }


}
