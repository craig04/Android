package com.elysium.craig.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

public class CrimeModel {

    private static CrimeModel sInstance;
    private Context mAppContext;

    private ArrayList<Crime> mCrimes;

    private CrimeModel(Context appContext) {

        mAppContext = appContext;
        mCrimes = new ArrayList<>();
    }

    public static CrimeModel getsInstance(Context c) {

        if (sInstance == null) {
            synchronized (CrimeModel.class) {
                if (sInstance == null) {
                    sInstance = new CrimeModel(c.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    public ArrayList<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {

        for (Crime c : mCrimes) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public void addCrime(Crime c) {
        mCrimes.add(c);
    }
}
