package com.example.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwitterUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);

        listView = findViewById(R.id.listViewUsers);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, this.arrayList);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(this);

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());

        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if(e == null ){
                    if(users.size() > 0 ){
                        for(ParseUser user : users){
                            arrayList.add(user.getUsername());
                        }
                        listView.setAdapter(arrayAdapter);
                        for (String fans : arrayList){
                            if (ParseUser.getCurrentUser().getList("fanOf") != null){
                                if (ParseUser.getCurrentUser().getList("fanOf").contains(fans)){
                                    listView.setItemChecked(arrayList.indexOf(fans), true);
                                }
                            }
                        }
                    }
                }
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout){
            ParseUser.getCurrentUser().logOut();
            finish();
            Intent intent = new Intent(TwitterUsers.this, Login.class);
            startActivity(intent);

        }else if(item.getItemId() == R.id.send){
            Intent intent = new Intent(TwitterUsers.this, SendTweet.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CheckedTextView checkedTextView = (CheckedTextView) view;

        if(checkedTextView.isChecked()){
            FancyToast.makeText(TwitterUsers.this, "is now followed " + arrayList.get(i),
                    FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
            ParseUser.getCurrentUser().add("fanOf", arrayList.get(i));
        }else{
            FancyToast.makeText(TwitterUsers.this, "is now unfollowed " + arrayList.get(i),
                    FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();

            ParseUser.getCurrentUser().getList("fanOf").remove(arrayList.get(i));
            List currentUserFanOfList = ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf", currentUserFanOfList);

        }

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    FancyToast.makeText(TwitterUsers.this, "saved",
                            FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
                }else {
                    FancyToast.makeText(TwitterUsers.this, "noy saved " + e.getMessage(),
                            FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                }
            }
        });
    }
}