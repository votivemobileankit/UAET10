package votive.com.appuaet10.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import votive.com.appuaet10.Beans.CategoryBean;
import votive.com.appuaet10.Interface.ICategory;
import votive.com.appuaet10.R;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

        private List<CategoryBean> mCategoryList;
        private Context mContext;
        private ICategory mICategoryItemClickCallback;

        public CategoryAdapter(Context aContext, ArrayList<CategoryBean> aDatalist, ICategory aICategoryItemClickCallback) {
            this.mContext = aContext;
            this.mCategoryList = aDatalist;
            this.mICategoryItemClickCallback = aICategoryItemClickCallback;
        }

        @Override
        public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_category_row, parent, false);

            return new CategoryAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CategoryAdapter.MyViewHolder holder, final int position) {

            final CategoryBean curBean = mCategoryList.get(position);

            holder.mTvCategoryName.setText(curBean.getmCategoryName().toUpperCase());
            holder.mIvCategoryImage.setImageURI(Uri.parse(curBean.getmCategoryImageUrl()));

            holder.mRlMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mICategoryItemClickCallback.clickCategoryItem(position, curBean.getmCategoryId(),curBean.getmCategoryName());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mCategoryList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView mTvCategoryName;
            public SimpleDraweeView mIvCategoryImage;
            public LinearLayout mRlMain;

            public MyViewHolder(View itemView) {
                super(itemView);
                mTvCategoryName = (TextView) itemView.findViewById(R.id.tv_category_name);
                mIvCategoryImage = (SimpleDraweeView) itemView.findViewById(R.id.iv_category);
                mRlMain = (LinearLayout) itemView.findViewById(R.id.ll_blog_row_main);
            }
        }
}
