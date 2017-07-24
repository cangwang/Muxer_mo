package cangwang.com.record_read.read;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cangwang.com.base.basecomponent.BaseFragment;
import cangwang.com.base.basecomponent.BasePresenter;
import cangwang.com.record_read.R;
import cangwang.com.record_read.adapter.ReadRecylerAdapter;

/**
 * Created by air on 16/7/6.
 */
public class ReadFragment extends BaseFragment implements IReadView{
    private Context mContext;

    private SwipeRefreshLayout readSwipeRefreshLayout;
    private RecyclerView readRecyclerView;
    private ReadRecylerAdapter adapter;
    private ReadPresenter readPresenter;

//    private int page = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.read_layout,container,false);
        mContext = view.getContext();
        if(readPresenter ==null)
            readPresenter = new ReadPresenter(getContext(),this);
        initUI(view);
        return view;

    }

    private void initUI(View view){
        readRecyclerView = (RecyclerView) view.findViewById(R.id.read_recylerview);
//        LinearLayoutManager mLinearManager = new LinearLayoutManager(readRecyclerView.getContext());
//        readRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//        readRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        adapter = new ReadRecylerAdapter(mContext);
//        readRecyclerView.setAdapter(adapter);
        readPresenter.RecycleViewInit(readRecyclerView);

        readSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.read_swipe_refresh_layout);
        readSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        readSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                page = 1;
                startTask();
            }
        });
    }

    private void startTask(){
        if(readSwipeRefreshLayout.isRefreshing()){
            readPresenter.refreshData();
            readSwipeRefreshLayout.setRefreshing(false);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        readPresenter.Destroy();
        super.onDestroyView();
    }

    @Override
    public BasePresenter getPresenter() {
        return readPresenter;
    }

}
