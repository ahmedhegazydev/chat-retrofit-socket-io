package com.kadirkuruca.newsapp.di;

import android.app.Application;
import android.provider.SyncStateContract;

import androidx.room.Room;


import com.kadirkuruca.newsapp.data.local.ChatDao;
import com.kadirkuruca.newsapp.data.local.ChatDatabase;
import com.kadirkuruca.newsapp.util.ConstantsKt;

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
