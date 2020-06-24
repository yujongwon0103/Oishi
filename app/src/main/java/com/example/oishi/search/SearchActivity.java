package com.example.oishi.search;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oishi.R;
import com.example.oishi.RequestHttpURLConnection;
import com.example.oishi.food_brand.FoodBrandListFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {
    //데이터베이스
    SQLiteDatabase database;
    //검색 EditText
    SearchView search_view;
    //검색 기록 리스트뷰
    ListView search_history_ListView;

    //데이터베이스 이름
    private final String dbName = "Oishi";
    //데이터베이스 내 테이블 이름
    private final String tableName = "user_search";
    //테이블 내 속성 이름
    private String[] Search_History = new String[]{};
    //테이블 내 데이터 리스트
    ArrayList<HashMap<String, String>> userSearchList;

    //검색 기록 TAG
    private static final String TAG_NAME = "search_history";

    //리스트 어뎁터
    ListAdapter adapter;

    FoodBrandListFragment foodBrandListFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        userSearchList = new ArrayList<>();

        search_view = findViewById(R.id.search_view);
        search_view.setIconified(false);

        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_view.onActionViewExpanded();
            }
        });

        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 입력받은 문자열 처리
                Toast.makeText(getApplicationContext(), "검색어 : "+query,Toast.LENGTH_SHORT).show();
                //저장
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 입력란의 문자열이 바뀔 때 처리
                return false;
            }
        });
    }
}