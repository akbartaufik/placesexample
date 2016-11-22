package com.opika.placeautocomplete.dagger;


import com.opika.placeautocomplete.dagger.modules.AppModule;
import com.opika.placeautocomplete.dagger.modules.NetworkModule;
import com.opika.placeautocomplete.presenters.NearbyPresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Taufik on 1/18/2016.
 */
@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {
    void inject(App app);
    void inject(NearbyPresenter app);

}
