package com.example.oishi.food_brand;

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

import com.example.oishi.FoodDetailActivity;
import com.example.oishi.RequestHttpURLConnection;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

public class FoodBrandListFragment extends ListFragment {
    private FoodBrandAdapter adapter;
    public String Food_Type;
    public String Food_Type2;

    public FoodBrandListFragment(String food_type, String food_Type2)
    {
        Food_Type = food_type;
        Food_Type2 = food_Type2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String url = "http://ykh3587.dothome.co.kr/food_brand_list.php";

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
        FoodBrandListViewItem item = (FoodBrandListViewItem) listView.getItemAtPosition(position);

        String food_brand_name = item.getMain_Store_Name();

        //use item data.
        Intent intent = new Intent(getActivity(), FoodDetailActivity.class);
        intent.putExtra("brand_name", food_brand_name);
        intent.putExtra("foodtype", Food_Type2);
        startActivity(intent);
    }

    public void addItem(String name, String order_call, String logo, String representative_menu) {
        adapter.addItem(name, order_call, logo, representative_menu) ;
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
}