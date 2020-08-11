package com.example.twitterclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("GO8pJKVEbI1iS5pm6hWp0VCLIxDilCX3jJEI2PTb")
                // if defined
                .clientKey("dtHeHe8E9wtFslIFZvG9BWbQWNcYZuuxd9ixj6cM")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}
