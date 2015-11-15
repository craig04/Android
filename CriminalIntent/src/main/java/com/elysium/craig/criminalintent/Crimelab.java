package com.elysium.craig.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

public class CrimeLab {

    private static CrimeLab sInstance;
    private Context mAppContext;

    private ArrayList<Crime> mCrimes;

    private CrimeLab(Context appContext) {

        mAppContext = appContext;
        mCrimes = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Crime c = new Crime();
            c.setTitle("Crime #" + i);
            c.setSolved(i % 2 == 0);
            mCrimes.add(c);
        }
    }

    public static CrimeLab getsInstance(Context c) {

        if (sInstance == null) {
            synchronized (CrimeLab.class) {
                if (sInstance == null) {
                    sInstance = new CrimeLab(c.getApplicationContext());
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
}
