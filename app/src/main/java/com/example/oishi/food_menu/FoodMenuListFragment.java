package com.example.oishi.food_menu;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.ListFragment;

import com.example.oishi.menu_sort.FoodMenuSortActivity;
import com.example.oishi.RequestHttpURLConnection;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

public class FoodMenuListFragment extends ListFragment {
    private FoodMenuAdapter adapter;
    private String Food_Type, Food_Type2;

    public FoodMenuListFragment(String food_type, String food_Type2) {
        Food_Type = food_type;
        Food_Type2 = food_Type2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String url = "http://ykh3587.dothome.co.kr/" + Food_Type2 + ".php";

        ContentValues values = new ContentValues();
        values.put("Food_Type", Food_Type);

        BackgroundTask task = new BackgroundTask(url, values);
        task.execute();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //ListView 클릭 이벤트 처리
    @Override
    public void onListItemClick (ListView listView, View view, int position, long id) {
        // get TextView's Text.
        FoodMenuListViewItem item = (FoodMenuListViewItem) listView.getItemAtPosition(position);

        String food_menu_name = item.getParents_Name();

        //use item data.
        Intent intent = new Intent(getActivity(), FoodMenuSortActivity.class);
        intent.putExtra("menu_name", food_menu_name);
        startActivity(intent);
    }

    public void addItem(String image, String name, String description) {
        adapter.addItem(image, name, description) ;
    }

    public class BackgroundTask extends AsyncTask<Void, Void, String> {
        ProgressDialog asyncDialog;

        String url;
        ContentValues values;

        BackgroundTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            asyncDialog = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
            adapter = new FoodMenuAdapter();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result;
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values);
            return result;
        }

        //다운로드 중 프로그레스바 업데이트
        @Override
        protected void onProgressUpdate(Void ... voids) {
            super.onProgressUpdate(voids);
        }

        @Override
        protected void onPostExecute(String results) {
            super.onPostExecute(results);
            asyncDialog.dismiss();

            setListAdapter(adapter);

            Gson gson = new Gson();
            try {
                JSONObject jsonObject = new JSONObject(results);
                JSONArray jsonArray = jsonObject.getJSONArray("response");

                int index = 0;
                while(index < jsonArray.length()) {
                    FoodMenuListViewItem foodMenuListViewItem = gson.fromJson(jsonArray.get(index).toString(), FoodMenuListViewItem.class);
                    adapter.addItem(foodMenuListViewItem.getParents_Img(), foodMenuListViewItem.getParents_Name(), foodMenuListViewItem.getParents_Content());
                    index++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}