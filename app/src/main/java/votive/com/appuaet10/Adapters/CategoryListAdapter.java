package votive.com.appuaet10.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import votive.com.appuaet10.Beans.CategoryBean;
import votive.com.appuaet10.Interface.ICategory;
import votive.com.appuaet10.R;


public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {

    private List<CategoryBean> mCategoryList;
    private Context mContext;
    private ICategory mICategoryItemClickCallback;

    public CategoryListAdapter(Context aContext, ArrayList<CategoryBean> aDatalist, ICategory aICategoryItemClickCallback) {
        this.mContext = aContext;
        this.mCategoryList = aDatalist;
        this.mICategoryItemClickCallback = aICategoryItemClickCallback;
    }

    @Override
    public CategoryListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_list_row, parent, false);

        return new CategoryListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryListAdapter.MyViewHolder holder, final int position) {

        final CategoryBean curBean = mCategoryList.get(position);
        holder.mImageSdv.setImageURI(Uri.parse(curBean.getmCategoryImageUrl()));


        holder.mMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mICategoryItemClickCallback.clickCategoryItem(position, curBean.getmCategoryId(), curBean.getmCategoryName());
            }
        });

        holder.mTvCategoryName.setText(curBean.getmCategoryName());

    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvCategoryName;
        public SimpleDraweeView mImageSdv;
        public RelativeLayout mMainLayout;


        public MyViewHolder(View itemView) {
            super(itemView);
            mTvCategoryName = (TextView) itemView.findViewById(R.id.tv_category_grit_item);
            mImageSdv = (SimpleDraweeView) itemView.findViewById(R.id.sdv_category_grit_item);
            mMainLayout = (RelativeLayout) itemView.findViewById(R.id.rl_category_grid_item_main);
        }
    }
}
