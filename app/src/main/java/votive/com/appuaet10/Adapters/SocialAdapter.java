package votive.com.appuaet10.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import votive.com.appuaet10.Interface.IRowClickListener;
import votive.com.appuaet10.R;

public class SocialAdapter extends RecyclerView.Adapter<SocialAdapter.MyViewHolder> {

    private Context mContext;
    private IRowClickListener mCallBack;
    private String[] mTitleListArr;
    private int [] mSocialIdsArr;



    public SocialAdapter(Context aContext,
                         String[] aTitleList,
                         int[] aDrawableIdList,
                         IRowClickListener aCallBack) {
        this.mContext = aContext;
        mCallBack = aCallBack;
        mTitleListArr = aTitleList;
        mSocialIdsArr = aDrawableIdList;
    }

    @Override
    public SocialAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row_social_layout, parent, false);
        return new SocialAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SocialAdapter.MyViewHolder aHolder, final int aPosition) {

        aHolder.mRlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onRowClick(aPosition, false);
            }
        });
        aHolder.mTitleTv.setText(mTitleListArr[aPosition]);
        aHolder.mIconIv.setImageResource(mSocialIdsArr[aPosition]);
    }

    @Override
    public int getItemCount() {
        return mTitleListArr.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIconIv;
        public RelativeLayout mRlMain;
        public TextView mTitleTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTitleTv = (TextView) itemView.findViewById(R.id.tv_share_row_item);
            mIconIv = (ImageView) itemView.findViewById(R.id.iv_share_row_item);
            mRlMain = (RelativeLayout) itemView.findViewById(R.id.rl_share_row_item);
        }
    }
}