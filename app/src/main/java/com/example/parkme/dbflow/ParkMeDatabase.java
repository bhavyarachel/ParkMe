package com.example.parkme.dbflow;


import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = ParkMeDatabase.NAME, version = ParkMeDatabase.VERSION)
public class ParkMeDatabase {

    public static final String NAME = "ParkMeDatabase";
    public static final int VERSION = 1;

}
