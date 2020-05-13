package ar.edu.itba.paw.models;

import java.util.AbstractList;
import java.util.List;
import java.util.TreeSet;

public class DepartmentList extends AbstractList<Department> {

    private final Department[] departmentList;
    private final ProvinceList provinceList;

    public DepartmentList(List<Department> departments) {
        super();
        TreeSet<Department> departmentSet = new TreeSet<>();
        departments.forEach(department -> {
            departmentSet.add(department);
        });
        departmentList = departmentSet.toArray(new Department[0]);
        provinceList = new ProvinceList(this);
    }

    public Department get(int index) {
        return departmentList[index];
    }

    public Department set(int index, Department department) {
        Department oldValue = departmentList[index];
        departmentList[index] = department;
        return oldValue;
    }

    public int size() {
        return departmentList.length;
    }

    public ProvinceList getProvinceList() {
        return provinceList;
    }

}
