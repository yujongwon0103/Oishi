package com.example.oishi.favorite_fragment;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;

import com.example.oishi.FoodDetailActivity;
import com.example.oishi.RequestHttpURLConnection;
import com.example.oishi.food_brand.FoodBrandAdapter;
import com.example.oishi.food_brand.FoodBrandListViewItem;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyOrderListFragment extends ListFragment {
    private ArrayList<String> order_store_list = new ArrayList<>();
    private FoodBrandAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inquiries_order_list();

        String url = "http://ykh3587.dothome.co.kr/???.php";

        ContentValues values = new ContentValues();
        for(int i = 0 ; i < order_store_list.size() ; i++) {
            values.put("order_store_"+i, order_store_list.get(i));
        }

        BackgroundTask task = new BackgroundTask(url, values);
        task.execute();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //ListView 클릭 이벤트 처리
    @Override
    public void onListItemClick (ListView listView, @NonNull View view, int position, long id) {
        // get TextView's Text.
        FoodBrandListViewItem item = (FoodBrandListViewItem) listView.getItemAtPosition(position);

        String food_brand_name = item.getMain_Store_Name();

        //use item data.
        Intent intent = new Intent(getActivity(), FoodDetailActivity.class);
        intent.putExtra("brand_name", food_brand_name);
        startActivity(intent);
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
            adapter = new FoodBrandAdapter();
        }

        @Override
        protected String doInBackground(Void... params) {
            String result;
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values);
            return result;
        }

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
                    FoodBrandListViewItem foodBrandListViewItem = gson.fromJson(jsonArray.get(index).toString(), FoodBrandListViewItem.class);
                    adapter.addItem(foodBrandListViewItem.getMain_Store_Name(), foodBrandListViewItem.getMain_Store_Phone_Number(), foodBrandListViewItem.getMain_Store_Icon(), foodBrandListViewItem.getMain_Store_menu());
                    index++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void inquiries_order_list(){
        try {
            String uriString = "content://com.example.sql_db/User_Order";
            Uri uri = new Uri.Builder().build().parse(uriString);

            String[] columns = new String[] {"phone_order"};
            if(getActivity() != null) {
                Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(uri, columns, null, null, "_id ASC");

                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String phone_order = cursor.getString(cursor.getColumnIndex(columns[0]));
                        order_store_list.add(phone_order);
                    }
                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}