package votive.com.appuaet10.Adapters;


import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import votive.com.appuaet10.Beans.NotificationBean;
import votive.com.appuaet10.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<NotificationBean> mNotificationList;

    public NotificationAdapter(Context aContext, ArrayList<NotificationBean> aData) {
        this.mContext = aContext;
        this.mNotificationList = aData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_list_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (holder != null){
            NotificationBean curBean = mNotificationList.get(position);

            holder.mTvTitle.setText(curBean.getTitle());
            holder.mTvDate.setText(curBean.getDate());
            holder.mTvDescription.setText(curBean.getDescription());

            try {
                if (curBean.getImageUrl() != null) {
                    Uri uriImages = Uri.parse(curBean.getImageUrl());
                    if (uriImages != null) {
                        holder.mSdvImage.setImageURI(uriImages);
                        holder.mSdvImage.setVisibility(View.VISIBLE);
                    } else {
                        holder.mSdvImage.setVisibility(View.GONE);
                    }
                }  else {
                    holder.mSdvImage.setVisibility(View.GONE);
                }
            }catch (Exception e){
                System.out.println(""+e);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mNotificationList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView mTvTitle;
        private TextView mTvDate;
        private TextView mTvDescription;
        private SimpleDraweeView mSdvImage;


        public MyViewHolder(View view) {
            super(view);
            mTvTitle = (TextView) view.findViewById(R.id.tv_notify_title);
            mTvDate = (TextView) view.findViewById(R.id.tv_notify_date);
            mTvDescription = (TextView) view.findViewById(R.id.tv_notify_description);
            mSdvImage = (SimpleDraweeView) view.findViewById(R.id.sdv_notify_image);
        }
    }
}
