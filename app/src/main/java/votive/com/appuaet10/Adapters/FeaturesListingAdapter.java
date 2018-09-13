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
import votive.com.appuaet10.Interface.IFeatureListing;
import votive.com.appuaet10.R;


public class FeaturesListingAdapter extends RecyclerView.Adapter<FeaturesListingAdapter.MyViewHolder> {

    private List<CategoryBean> mCategoryList;
    private Context mContext;
    private IFeatureListing mIFeatureListingCallBack;

    public FeaturesListingAdapter(Context aContext, ArrayList<CategoryBean> aDatalist, IFeatureListing aFeatureListing) {
        this.mContext = aContext;
        this.mCategoryList = aDatalist;
        this.mIFeatureListingCallBack = aFeatureListing;
    }

    @Override
    public FeaturesListingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_features_listing_row, parent, false);

        return new FeaturesListingAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeaturesListingAdapter.MyViewHolder holder, final int position) {

        final CategoryBean curBean = mCategoryList.get(position);
        holder.mTvCategoryName.setText(curBean.getmCategoryName());
        holder.mIvCategoryImage.setImageURI(Uri.parse(curBean.getmCategoryRectImageUrl()));
        holder.mRlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIFeatureListingCallBack.clickFeatureListingItem(curBean.getmCategoryId(), curBean.getmCategoryName());
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
            mTvCategoryName = (TextView) itemView.findViewById(R.id.tv_feature_listing_name);
            mIvCategoryImage = (SimpleDraweeView) itemView.findViewById(R.id.iv_feature_listing);
            mRlMain = (LinearLayout) itemView.findViewById(R.id.ll_blog_row_main);
        }
    }
}
