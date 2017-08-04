package com.example.abhishek.sensorrecorder;

import android.content.Context;

/**
 * Created by Abhishek on 7/23/2017.
 */

public class ParamsAsync {
    int rawIdentifier;
    Context contxt;

    ParamsAsync(Context c, int i){
        rawIdentifier = i;
        contxt = c;
    }
}
