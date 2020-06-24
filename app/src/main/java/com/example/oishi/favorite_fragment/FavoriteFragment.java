package com.example.oishi.favorite_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.oishi.FoodPagerAdapter;
import com.example.oishi.R;
import com.google.android.material.tabs.TabLayout;

public class FavoriteFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        FoodPagerAdapter adapter = new FoodPagerAdapter(getFragmentManager());

        ViewPager favorite_Container = view.findViewById(R.id.favorite_Container);

        adapter.addFragment("찜한가게", new MyFavoriteListFragment());
        adapter.addFragment("바로결제", new MyPaymentListFragment());
        adapter.addFragment("전화주문", new MyOrderListFragment());

        favorite_Container.setAdapter(adapter);

        TabLayout favorite_TabLayout = view.findViewById(R.id.favorite_TabLayout);
        favorite_TabLayout.setupWithViewPager(favorite_Container);

        return view;
    }
}