package com.soma.yampp;



import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import com.soma.synge.R;
import com.soma.util.CommonFunction;
import com.soma.util.ServiceWork;
import com.soma.util.CommonFunction.FragmentCallback;



public class MapInfoViewFactory extends MainPageDrawerAcitivity
{
    private static int a;
   View view;
 ListView mCommentList;

    @SuppressLint("NewApi")
    public  View generateHelperInfoView(Context context) throws Exception {

       

        view = MainPageDrawerAcitivity.inflater.inflate(R.layout.commnet_list_xml, null);
        mCommentList=(ListView)view.findViewById(R.id.listview);
        //CommonFunction.sActivityName="CommentList";
       // mURL=CommonFunction.host+"recentYampps";
       // methodThatStartsTheAsyncTask(context) ;
        CommentListAdapter adapter=new CommentListAdapter(context);
        mCommentList.setAdapter(adapter);
        

        return view;


    }

    public void methodThatStartsTheAsyncTask(Context c) 
    {

        ServiceWork testAsyncTask = new ServiceWork(mURL,c,new FragmentCallback() {

            @Override
            public void onTaskDone() 
            {
                CommentListAdapter adapter=new CommentListAdapter(MapInfoViewFactory.this);
                mCommentList.setAdapter(adapter);
            }
        });testAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);;
    }
}
