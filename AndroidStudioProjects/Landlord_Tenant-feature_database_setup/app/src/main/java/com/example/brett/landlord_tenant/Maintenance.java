package com.example.brett.landlord_tenant;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Brett on 12/9/2017.
 */

public class Maintenance {
    private String name;
    private String title;
    private String description;

    Maintenance(){
        name="";
        title="";
        description="";
    }
    Maintenance(String name, String title, String description){
        this.name = name;
        this.title = title;
        this.description = description;
    }

    public String getName(){
        return name;
    }
    public String getTitle(){
        return title;
    }
    public String getDescription(){
        return description;
    }
}
