package com.mayurit.hakahaki.Helpers;

import android.app.Application;


/**
 * Created by Krevilraj on 11/1/2017.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/ek-mukta.light.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/ek-mukta.extralight.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/ek-mukta.light.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/ek-mukta.extralight.ttf");


    }

}