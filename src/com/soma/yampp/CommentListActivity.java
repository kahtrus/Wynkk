package com.soma.yampp;

import com.soma.synge.R;
import com.soma.util.CommonFunction;
import com.soma.util.ServiceWork;
import com.soma.util.CommonFunction.FragmentCallback;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ListView;

public class CommentListActivity extends Activity{
    String mURL;
    ListView mCommentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
      // getWindow().setFlags(300, 300);
        
        setContentView(R.layout.commnet_list_xml);
    //  getWindow().setBackgroundDrawable(new ColorDrawable(0));
    //    getWindow().clearFlags(LayoutParams.FLAG_DIM_BEHIND);
        mCommentList=(ListView)findViewById(R.id.listview);
        CommonFunction.sActivityName="CommentList";
        mURL=CommonFunction.host+"recentYampps";
        methodThatStartsTheAsyncTask() ;
        
    }
    
    
    public void methodThatStartsTheAsyncTask() 
    {

        ServiceWork testAsyncTask = new ServiceWork(mURL,CommentListActivity.this,new FragmentCallback() {

            @Override
            public void onTaskDone() 
            {
                CommentListAdapter adapter=new CommentListAdapter(CommentListActivity.this);
                mCommentList.setAdapter(adapter);
            }
        });testAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);;
    }
}
