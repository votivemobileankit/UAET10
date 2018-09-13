package votive.com.appuaet10.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import votive.com.appuaet10.Adapters.CategoryListAdapter;
import votive.com.appuaet10.Beans.CategoryBean;
import votive.com.appuaet10.Interface.ICategory;
import votive.com.appuaet10.R;
import votive.com.appuaet10.Utilities.Constant;

public class CategoryGridFragment extends Fragment implements ICategory {


    public static final String INTENT_GRID_CATEGORY_LIST_KEY = "grid_cat_list";
    public static final String INTENT_GRID_CATEGORY_PAGE_NO_KEY = "grid_cat_page_no";

    private RecyclerView mGridRv;
    private View mMainView;
    private int mPageNo;
    private ArrayList<CategoryBean> mCategoryList;


    public CategoryGridFragment() {
        // Required empty public constructor
    }

    public static CategoryGridFragment newInstance(int aPage, ArrayList<CategoryBean> aDataList) {
        CategoryGridFragment fragmentFirst = new CategoryGridFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(INTENT_GRID_CATEGORY_LIST_KEY, aDataList);
        args.putInt(INTENT_GRID_CATEGORY_PAGE_NO_KEY, aPage);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_category_grid, container, false);
        return mMainView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        mCategoryList = getArguments().getParcelableArrayList(INTENT_GRID_CATEGORY_LIST_KEY);
        mPageNo = getArguments().getInt(INTENT_GRID_CATEGORY_PAGE_NO_KEY);
        mGridRv = (RecyclerView) mMainView.findViewById(R.id.rv_category_grid);
        GridLayoutManager lLayout = new GridLayoutManager(getActivity(), 3);
        mGridRv.setHasFixedSize(true);
        mGridRv.setLayoutManager(lLayout);
        mGridRv.addItemDecoration(new ItemDecorationAlbumColumns(20, 3));
        CategoryListAdapter mCategoryListAdapter = new CategoryListAdapter(getActivity(), mCategoryList, this);
        mGridRv.setAdapter(mCategoryListAdapter);
    }

    @Override
    public void clickCategoryItem(int aPosition, int aId, String strName) {
        Intent intent = new Intent(getActivity(), CategoryItemListActivity.class);
        intent.putExtra(Constant.INTENT_CATEGORY_ITEM_ID, String.valueOf(aId));
        intent.putExtra(Constant.INTENT_CATEGORY_NAME, strName);
        startActivity(intent);
    }
}
