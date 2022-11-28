package com.kotdev.food.room;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Database {

    private final RecipeDatabase database;

    @Inject
    public Database(Context context) {
        database = Room
                .databaseBuilder(context, RecipeDatabase.class, "database")
                .build();
    }

    public RecipeDatabase getDatabase() {
        return database;
    }

}