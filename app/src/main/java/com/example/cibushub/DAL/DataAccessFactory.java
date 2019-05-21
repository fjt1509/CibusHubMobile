package com.example.cibushub.DAL;

import android.content.Context;

import com.example.cibushub.Interfaces.IDataAccess;

public class DataAccessFactory {
    public static IDataAccess getInstance(Context c)
    { return new DataAccess(c); }
}
