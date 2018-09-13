package votive.com.appuaet10.Adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import votive.com.appuaet10.Beans.SplashBean;
import votive.com.appuaet10.R;

public class CoverFlowAdapter extends BaseAdapter {

    private ArrayList<SplashBean> data;
    private AppCompatActivity activity;
    private int mSelectedIndex;

    public CoverFlowAdapter(AppCompatActivity context, ArrayList<SplashBean> objects) {
        this.activity = context;
        this.data = objects;
        mSelectedIndex = 0;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public SplashBean getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_flow_view, null, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        viewHolder.gameImage.setImageResource(data.get(position).getImageSource());


       /* if (mSelectedIndex == position) {

        } else {
            viewHolder.gameImage.setImageResource(R.mipmap.ic_launcher);

        }*/

        return convertView;
    }


    /*public void notifyData(int index) {
        mSelectedIndex = index;
        notifyDataSetChanged();
    }
*/

    private static class ViewHolder {
        private TextView gameName;
        private ImageView gameImage;

        public ViewHolder(View v) {
            gameImage = (ImageView) v.findViewById(R.id.image);
            gameName = (TextView) v.findViewById(R.id.name);
        }
    }
}