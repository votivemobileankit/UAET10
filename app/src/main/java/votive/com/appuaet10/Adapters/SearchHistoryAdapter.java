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

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.MyViewHolder> {

    private List<SearchBean> mHistoryItemList;
    private ISearchCategory mCallBack;

    public SearchHistoryAdapter(List<SearchBean> aSearchItemList, ISearchCategory aCallBack) {
        this.mHistoryItemList = aSearchItemList;
        this.mCallBack= aCallBack;
    }

    @Override
    public SearchHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_search_list, parent, false);
        return new SearchHistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchHistoryAdapter.MyViewHolder holder, final int position) {
        final SearchBean searchBean = mHistoryItemList.get(position);
        holder.title.setText(searchBean.getmSearchLabel());
        holder.rowMainRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallBack.historyItemClicked(position, searchBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHistoryItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rowMainRl;
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_searchItem);
            rowMainRl = (RelativeLayout) view.findViewById(R.id.rl_search_item_row);
        }
    }
}
