package ru.kuznetsov.entity;

public class EmployeeToProject {
    private Integer id;
    private Integer employee_id;
    private Integer project_id;

    public EmployeeToProject() {
    }

    public EmployeeToProject(Integer id, Integer employee_id, Integer project_id) {
        this.id = id;
        this.employee_id = employee_id;
        this.project_id = project_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Integer employee_id) {
        this.employee_id = employee_id;
    }

    public Integer getProject_id() {
        return project_id;
    }

    public void setProject_id(Integer project_id) {
        this.project_id = project_id;
    }


}
