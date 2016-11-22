package com.opika.placeautocomplete.dagger;

import android.app.Application;

import com.opika.placeautocomplete.dagger.modules.AppModule;
import com.opika.placeautocomplete.dagger.modules.NetworkModule;

public class App extends Application {

    private static AppComponent appComponent;


    public static AppComponent appComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this))
                .networkModule(new NetworkModule()).build();
        appComponent.inject(this);
    }


}