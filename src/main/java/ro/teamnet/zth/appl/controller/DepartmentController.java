package ro.teamnet.zth.appl.controller;

import ro.teamnet.zth.api.annotations.MyController;
import ro.teamnet.zth.api.annotations.MyRequestMethod;
import ro.teamnet.zth.appl.domain.Department;
import ro.teamnet.zth.appl.service.DepartmentService;

import java.util.ArrayList;

/**
 * Created by Ramona.Raducu on 7/20/2017.
 */
@MyController(urlPath = "/departments")
public
class DepartmentController {
    DepartmentService departmentService;
    private String allDepartments;

    public
    DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @MyRequestMethod(urlPath = "/all", methodType = "GET")
    public
    ArrayList<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    // SAU
    //    @MyRequestMethod(urlPath = "/all", methodType = "GET")
    //    public
    //    String getAllDepartments() {
    //        return allDepartments;
    //    }


}
