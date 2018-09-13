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

import java.util.List;

import votive.com.appuaet10.Beans.CategoryItemBean;
import votive.com.appuaet10.Interface.ISubCategoryItemClickCallBack;
import votive.com.appuaet10.R;

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.MyViewHolder> {

    private List<CategoryItemBean> mProductDataList;
    private Context mContext;
    private ISubCategoryItemClickCallBack mCallBack;

    public CategoryItemAdapter(Context mContext, List<CategoryItemBean> homeList, ISubCategoryItemClickCallBack aCallBack) {
        this.mProductDataList = homeList;
        this.mContext = mContext;
        mCallBack = aCallBack;
    }

    @Override
    public CategoryItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sub_category_rowt, parent, false);

        return new CategoryItemAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryItemAdapter.MyViewHolder aHolder, final int position) {

        final CategoryItemBean curBean = mProductDataList.get(position);
        aHolder.tvCategoryName.setText(curBean.getCategoryItemName());
        aHolder.IvCategoryItemImg.setImageURI(Uri.parse(curBean.getCategoryItemFullImgUrl()));

        aHolder.mMainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.categoryItem(curBean, position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mProductDataList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCategoryName;
        private SimpleDraweeView IvCategoryItemImg;
        private LinearLayout mMainView;


        private MyViewHolder(View view) {
            super(view);
            mMainView = (LinearLayout) view.findViewById(R.id.rl_search_Item);
            tvCategoryName = (TextView) view.findViewById(R.id.tv_search_name);
            IvCategoryItemImg = (SimpleDraweeView) view.findViewById(R.id.iv_search_image);
        }
    }
}
