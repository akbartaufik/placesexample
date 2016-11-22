package com.opika.placeautocomplete.dagger.modules;

import android.app.Application;
import android.content.Context;

import com.opika.placeautocomplete.dagger.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


/**
 * Created by Taufik on 1/18/2016.
 */
@Module
public class AppModule {
    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return app;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return app;
    }
}
