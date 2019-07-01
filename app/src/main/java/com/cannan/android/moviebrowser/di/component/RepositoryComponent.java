package com.cannan.android.moviebrowser.di.component;

import com.cannan.android.moviebrowser.di.module.DatabaseModule;
import com.cannan.android.moviebrowser.viewmodels.MovieViewModel;

import dagger.Component;

/**
 * @ClassName: RepositoryComponent
 * @Description:
 * @author: Cannan
 * @date: 2019-07-01 22:31
 */
@Component(modules = DatabaseModule.class)
public interface RepositoryComponent {

    void inject(MovieViewModel viewModel);
}
