package com.cannan.android.moviebrowser.di.component;

import com.cannan.android.moviebrowser.data.MovieRoomDatabase;
import com.cannan.android.moviebrowser.di.module.NetModule;
import com.cannan.android.moviebrowser.net.Download;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @ClassName: NetComponent
 * @Description:
 * @author: Cannan
 * @date: 2019-07-02 00:16
 */
@Singleton
@Component(modules = NetModule.class)
public interface NetComponent {

    void inject(Download download);

    void inject(MovieRoomDatabase.PopulateDbAsync async);
}
