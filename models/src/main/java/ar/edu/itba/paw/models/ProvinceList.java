package ar.edu.itba.paw.models;

import java.util.AbstractList;
import java.util.List;
import java.util.TreeSet;

public class ProvinceList extends AbstractList<Province> {

    private final Province[] provinceList;

    public ProvinceList(List<Province> provinceArray) {
        super();
        provinceList = provinceArray.toArray(new Province[0]);
    }

    public ProvinceList(DepartmentList departmentList) {
        super();
        TreeSet<Province> provinceSet = new TreeSet<>();
        departmentList.forEach(department -> {
            provinceSet.add(department.getProvince());
        });
        provinceList = provinceSet.toArray(new Province[0]);
    }

    public Province get(int index) {
        return provinceList[index];
    }

    public Province set(int index, Province province) {
        Province oldValue = provinceList[index];
        provinceList[index] = province;
        return oldValue;
    }

    public int size() {
        return provinceList.length;
    }
}
