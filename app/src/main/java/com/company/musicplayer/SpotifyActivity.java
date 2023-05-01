package com.company.musicplayer;

import static com.spotify.sdk.android.auth.LoginActivity.REQUEST_CODE;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SpotifyActivity extends AppCompatActivity {
    private static final String CLIENT_ID = "d316e378398649858891bbf8cdc7950f";
    private static final String REDIRECT_URI = "com.company.musicplayer://callback";
    private static final int REQUEST_CODE = 1337;
    private static final String SCOPES = "user-read-recently-played,user-library-modify,user-read-email,user-read-private";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify);


    }
}