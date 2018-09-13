package votive.com.appuaet10.Adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import votive.com.appuaet10.Beans.CategoryItemBean;
import votive.com.appuaet10.Interface.ICategoryImageClickCallback;
import votive.com.appuaet10.R;


public class SimilarCategoryAdapter extends RecyclerView.Adapter<SimilarCategoryAdapter.MyViewHolder> {

    private Context mContext;
    private List<CategoryItemBean> mImageList;
    private ICategoryImageClickCallback mCallBack;
    private int mSelectedIndex;

    public SimilarCategoryAdapter(Context aContext, ArrayList<CategoryItemBean> aImageList, ICategoryImageClickCallback aCallBack) {
        this.mContext = aContext;
        this.mImageList = aImageList;
        mCallBack = aCallBack;
        mSelectedIndex = 0;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.similar_categories_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CategoryItemBean curBean = mImageList.get(position);
        holder.mTitleTv.setText(curBean.getCategoryItemName().toUpperCase());

        try {
            String imagePath = curBean.getCategoryItemFullImgUrl().trim();
            String newPathStr = imagePath.replaceAll(" ", "%20");
            holder.ProductImageView.setImageURI(Uri.parse(newPathStr));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("imageError " + e);
        }
        holder.mRowMainRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.itemClicked(position, curBean);
                mSelectedIndex = position;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ProductImageView;
        RelativeLayout mRowMainRl;
        TextView mTitleTv;

        public MyViewHolder(View view) {
            super(view);
            ProductImageView = (ImageView) view.findViewById(R.id.iv_category_item_image);
            mTitleTv = (TextView) view.findViewById(R.id.tv_similar_item_row);
            mRowMainRl = (RelativeLayout) view.findViewById(R.id.rl_product_details_image_row_main);
        }
    }
}
