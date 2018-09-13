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

import votive.com.appuaet10.Beans.BlogBean;
import votive.com.appuaet10.Interface.IBlogRowClick;
import votive.com.appuaet10.R;


public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.MyViewHolder> {

    private List<BlogBean> mBlogList;
    private Context mContext;
    private IBlogRowClick mCallBack;

    public BlogAdapter(Context aContext,
                       ArrayList<BlogBean> aDataList, IBlogRowClick aCallBack) {
        this.mContext = aContext;
        this.mBlogList = aDataList;
        mCallBack = aCallBack;
    }

    @Override
    public BlogAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_blog_row, parent, false);
        return new BlogAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BlogAdapter.MyViewHolder holder, final int position) {
        final BlogBean curBean = mBlogList.get(position);
        holder.mTvBlogName.setText(curBean.getmBlogName().toUpperCase());

        try {
            holder.mRlMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallBack.onBlogRowClick(position, curBean);
                }
            });
            if(curBean.getmBlogImageUrl() != null){
                Uri uriImages = Uri.parse(curBean.getmBlogImageUrl());
                if (uriImages != null) {
                    holder.mTvBlogImage.setImageURI(uriImages);
                } else {
                }
            }else {
                String strDrawable = String.valueOf(R.drawable.ic_placeholder);
                holder.mTvBlogImage.setImageURI(Uri.parse(strDrawable));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("e = " + e);

        }
    }

    @Override
    public int getItemCount() {
        return mBlogList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvBlogName;
        public SimpleDraweeView mTvBlogImage;
        public LinearLayout mRlMain;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTvBlogName = (TextView) itemView.findViewById(R.id.tv_blog_name);
            mTvBlogImage = (SimpleDraweeView) itemView.findViewById(R.id.iv_blog);
            mRlMain = (LinearLayout) itemView.findViewById(R.id.ll_blog_row_main);
        }
    }
}
