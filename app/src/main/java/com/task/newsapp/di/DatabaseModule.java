package com.task.newsapp.di;

import android.app.Application;

import androidx.room.Room;


import com.task.newsapp.data.local.ChatDao;
import com.task.newsapp.data.local.ChatDatabase;
import com.task.newsapp.util.ConstantsKt;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {


    @Provides
    @Singleton
    ChatDatabase provideMovieDatabase(Application application){
        return Room.databaseBuilder(application,ChatDatabase.class, ConstantsKt.DataBaseName)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @Provides
    @Singleton
    ChatDao provideFavoriteDao(ChatDatabase movieDatabase){
        return movieDatabase.getArticleDao();
    }

}
