package votive.com.appuaet10.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import votive.com.appuaet10.Beans.SearchBean;
import votive.com.appuaet10.Interface.ISearchCategory;
import votive.com.appuaet10.R;


public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.MyViewHolder> {

    private List<SearchBean> mSearchList;
    private ISearchCategory mICategoryCallback;

    public SearchItemAdapter(List<SearchBean> aSearchItemList,ISearchCategory aICategory) {
        this.mSearchList = aSearchItemList;
        this.mICategoryCallback = aICategory;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final SearchBean searchBean = mSearchList.get(position);
        holder.title.setText(searchBean.getmSearchLabel());
        holder.rowMainRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mICategoryCallback.searchItemClicked(position, searchBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSearchList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rowMainRl;
        public TextView title;
        //private TextView mTvSubTitle;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_search_item_row);
            rowMainRl = (RelativeLayout) view.findViewById(R.id.rl_search_item_row);

        }
    }
}
