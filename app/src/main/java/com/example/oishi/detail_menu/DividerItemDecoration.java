package com.example.oishi.detail_menu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oishi.R;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable parentDivider;
    private Drawable subDivider;


    public DividerItemDecoration(Context context) {
        parentDivider = context.getDrawable(R.drawable.parent_divider);
        subDivider = context.getDrawable(R.drawable.sub_divider);
    }

    // canvas에 적합한 decoration을 draw
    // item view가 그려지기전에 decoration이 먼저 그려지며, view밑에 나타난다.
    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.State state) {

        super.onDraw(c, recyclerView, state);
        int left = recyclerView.getPaddingLeft();
        int right = recyclerView.getWidth() - recyclerView.getPaddingRight();

        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = recyclerView.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(child);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;

            if(viewHolder instanceof ItemAdapter.ParentItemVH){
                int bottom = top + parentDivider.getIntrinsicHeight();
                parentDivider.setBounds(left, top, right, bottom);
                parentDivider.draw(c);
            }else{
                int bottom = top + subDivider.getIntrinsicHeight();
                subDivider.setBounds(left, top, right, bottom);
                subDivider.draw(c);
            }
        }
    }


    // (이름이 왜 getItemOffset일까. setPixelToRect 같은 느낌인데..)
    // outRect를 인자로 보내면, outRect에 값들을 넣어줌. (call by reference)
    // outRect의 각 자리는 각 item의 padding의 정도를 정해줘야함.
    // default는 다 0
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.set(0, 1, 0, 1);
    }
}