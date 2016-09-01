package material.com.muxer.fragment.read;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import material.com.muxer.R;

/**
 * Created by air on 16/7/6.
 */
public class ReadFragment extends Fragment {
    private Context mContext;

    private SwipeRefreshLayout readSwipeRefreshLayout;
    private RecyclerView readRecyclerView;

    private int page = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.read_layout,container,false);
        mContext = view.getContext();
        initUI(view);
        return view;

    }

    private void initUI(View view){
        readRecyclerView = (RecyclerView) view.findViewById(R.id.read_recylerview);

        readSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.read_swipe_refresh_layout);
        readSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        readSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                page = 1;
                startTask();
            }
        });
    }

    private void startTask(){
        if(readSwipeRefreshLayout.isRefreshing()){
            readSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
