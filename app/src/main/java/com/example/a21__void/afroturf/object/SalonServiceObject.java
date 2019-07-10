package com.example.a21__void.afroturf.object;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ASANDA on 2018/12/25.
 * for Pandaphic
 */
public class SalonServiceObject {
    private String name;
    private ArrayList<ServiceAfroObject> subServices;

    public SalonServiceObject(String pName, ServiceAfroObject... pSubServices){
        this.name = pName;
        this.subServices = new ArrayList<>();
        this.subServices.addAll(Arrays.asList(pSubServices));
    }

    public String getName() {
        return name;
    }

    public ArrayList<ServiceAfroObject> getSubServices() {
        return subServices;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addSubService(ServiceAfroObject serviceAfroObject){
        this.subServices.add(serviceAfroObject);
    }
}
