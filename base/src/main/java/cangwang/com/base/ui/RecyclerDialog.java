package cangwang.com.base.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import cangwang.com.base.R;
import cangwang.com.base.adapter.RecordDirAdapter;

/**
 * Created by air on 16/10/12.
 */

public class RecyclerDialog extends AlertDialog {
    private Context context;
    private String title;
    private String confirmButtonText;
    private String cacelButtonText;
    private ClickListenerInterface clickListenerInterface;

    private RecordDirAdapter adapter;
    private String path;

    public interface ClickListenerInterface {

        public void doConfirm(String path);

        public void doCancel();
    }

    public RecyclerDialog(Context context, String title, String confirmButtonText, String cacelButtonText) {
//        super(context, R.style.MyDialog);
        super(context);
        this.context = context;
        this.title = title;
        this.confirmButtonText = confirmButtonText;
        this.cacelButtonText = cacelButtonText;
    }

    public RecyclerDialog(Context context, String title, String confirmButtonText, String cacelButtonText,String path) {
        super(context, R.style.Dialog);
        this.context = context;
        this.title = title;
        this.confirmButtonText = confirmButtonText;
        this.cacelButtonText = cacelButtonText;
        this.path = path;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        init();
    }

    public void init() {
        setTitle(title);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycle_dialog, null);
        setContentView(view);

        RecyclerView recycler = (RecyclerView)view.findViewById(R.id.record_recyle_dir);
        Button tvConfirm = (Button) view.findViewById(R.id.confirm);
        Button tvCancel = (Button) view.findViewById(R.id.cancel);

        tvConfirm.setText(confirmButtonText);
        tvCancel.setText(cacelButtonText);

//        tvConfirm.setOnClickListener(new clickListener());
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListenerInterface.doConfirm(adapter.getChoosePath());
            }
        });
//        tvCancel.setOnClickListener(new clickListener());
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListenerInterface.doCancel();
            }
        });

        LinearLayoutManager mLinearManager = new LinearLayoutManager(recycler.getContext());
        recycler.setLayoutManager(mLinearManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        adapter = new RecordDirAdapter(context,path);
        recycler.setAdapter(adapter);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

//    private class clickListener implements View.OnClickListener {
//        @Override
//        public void onClick(View v) {
//            // TODO Auto-generated method stub
//            int id = v.getId();
//            switch (id) {
//                case R.id.confirm:
//                    clickListenerInterface.doConfirm(adapter.getChoosePath());
//                    break;
//                case R.id.cancel:
//                    clickListenerInterface.doCancel();
//                    break;
//            }
//        }
//
//    }

}
