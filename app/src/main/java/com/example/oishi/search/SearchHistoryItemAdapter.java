package com.example.oishi.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oishi.R;
import com.example.oishi.detail_menu.ItemAdapter;

import java.util.ArrayList;

public class SearchHistoryItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int PARENT_ITEM_VIEW = 0;
    private final int CHILD_ITEM_VIEW = 1;

    private ArrayList<SearchHistoryItem> visibleItems = new ArrayList<>();

    SearchHistoryItemAdapter(){
        int PARENT_ITEM_VIEW = 0;
        SearchHistoryItem searchItem1 = new SearchHistoryItem("치킨", PARENT_ITEM_VIEW);
        visibleItems.add(searchItem1);
        SearchHistoryItem searchItem2 = new SearchHistoryItem("치킨", PARENT_ITEM_VIEW);
        visibleItems.add(searchItem2);
        SearchHistoryItem searchItem3 = new SearchHistoryItem("치킨", PARENT_ITEM_VIEW);
        visibleItems.add(searchItem3);
        SearchHistoryItem searchItem4 = new SearchHistoryItem("치킨", PARENT_ITEM_VIEW);
        visibleItems.add(searchItem4);
        SearchHistoryItem searchItem5 = new SearchHistoryItem("치킨", PARENT_ITEM_VIEW);
        visibleItems.add(searchItem5);
        SearchHistoryItem clearItem = new SearchHistoryItem(null, CHILD_ITEM_VIEW);
        visibleItems.add(clearItem);
    }

    @Override
    public int getItemCount() {
        return visibleItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return visibleItems.get(position).getViewType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        switch(viewType){
            case PARENT_ITEM_VIEW:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_search_history_item, parent, false);
                viewHolder = new ParentItemVH(view);
                break;
            case CHILD_ITEM_VIEW:
                View subview = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_clear_all_item, parent, false);
                viewHolder = new ChildItemVH(subview);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ParentItemVH){
            final ParentItemVH parentItemVH = (ParentItemVH)holder;

            parentItemVH.search_history.setText(visibleItems.get(position).getName());
            parentItemVH.search_history.setTag(holder);

            parentItemVH.search_history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int holderPosition = ((ParentItemVH)v.getTag()).getAdapterPosition();
                }
            });

            parentItemVH.delete_search_history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int holderPosition = ((ParentItemVH)v.getTag()).getAdapterPosition();
                }
            });

            parentItemVH.itemView.setTag(holder);
        }
        else if(holder instanceof ItemAdapter.ChildItemVH){
            final ChildItemVH childItemVH = (ChildItemVH)holder;
            childItemVH.clear_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i=0; i<visibleItems.size();i++) {
                        notifyItemRemoved(i);
                        System.out.println(visibleItems.get(i));
                    }
                }
            });
        }
    }

    static class ParentItemVH extends RecyclerView.ViewHolder {
        TextView search_history;
        ImageButton delete_search_history;

        ParentItemVH(View itemView) {
            super(itemView);
            search_history = itemView.findViewById(R.id.search_history);
            delete_search_history = itemView.findViewById(R.id.delete_search_history);
        }
    }

    static class ChildItemVH extends RecyclerView.ViewHolder {
        TextView clear_all;

        ChildItemVH(View itemView) {
            super(itemView);
            clear_all = itemView.findViewById(R.id.clear_all);
        }
    }
}
