package votive.com.appuaet10.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import votive.com.appuaet10.Beans.OfferBean;
import votive.com.appuaet10.Interface.IOfferList;

import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.AppData;

public class OfferItemAdapter extends RecyclerView.Adapter<OfferItemAdapter.MyViewHolder> {

    private List<OfferBean> mOfferList;
    private Context mContext;
    private IOfferList mCallBack;

    public OfferItemAdapter(Context mContext, List<OfferBean> homeList, IOfferList aCallBack) {
        this.mOfferList = homeList;
        this.mContext = mContext;
        mCallBack = aCallBack;
    }

    @Override
    public OfferItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_offer_list_row, parent, false);

        return new OfferItemAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OfferItemAdapter.MyViewHolder aHolder, final int position) {

        final OfferBean curBean = mOfferList.get(position);

//         aHolder.tvOfferTitleName.setText(curBean.getmStrOfferTitleName());
//        aHolder.tvOfferTitleName.setTypeface(AppData.getInstance().getTypeFaceForType(mContext, AppData.FONT_EA_SPORTS_COVERS));
//
//        aHolder.tvOfferDesc.setText(curBean.getmStrOfferDesc());
//        aHolder.tvOfferDesc.setTypeface(AppData.getInstance().getTypeFaceForType(mContext, AppData.FONT_EA_SPORTS_15));

        aHolder.IvOfferImage.setMaxHeight(getScreenHeight(mContext));
//        aHolder.IvOfferImage.setImageResource(R.drawable.week_offer);
//        aHolder.IvOfferImage.setImageURI(Uri.parse(curBean.getmStrOfferImg()));

        Glide.with(mContext)
                .load(curBean.getmStrOfferImg()).placeholder(R.drawable.ic_placeholder)
                .override(getScreenWidth(mContext),getScreenHeight(mContext))
                .error(R.drawable.ic_placeholder)
                .into(aHolder.IvOfferImage);

        Log.e("StrOfferImg","--------> "+curBean.getmStrOfferImg());

        aHolder.mMainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.clickOfferItem(curBean, Integer.parseInt(curBean.getmStrOfferId()));
            }
        });
    }


    @Override
    public int getItemCount() {
        return mOfferList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
//        private TextView tvOfferTitleName;
//        private TextView tvOfferDesc;
        private ImageView IvOfferImage;
        private LinearLayout mMainView;


        private MyViewHolder(View view) {
            super(view);
            mMainView = (LinearLayout) view.findViewById(R.id.ll_main);
//            tvOfferTitleName = (TextView) view.findViewById(R.id.tv_offer_title_name);
//            tvOfferDesc = (TextView) view.findViewById(R.id.tv_offer_desc);
            IvOfferImage = (ImageView) view.findViewById(R.id.iv_offer_image);
        }
    }

    private static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int height = metrics.heightPixels;
        Log.e("Screen Height", "{" + height + "}");
        return height ;
    }

    private static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        Log.e("Screen Width", "{" + width + "}");
        return width ;
    }
}
