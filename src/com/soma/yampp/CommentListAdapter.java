package com.soma.yampp;

import com.soma.synge.R;
import com.soma.util.CommonFunction;
import com.soma.util.ServiceWork;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentListAdapter extends BaseAdapter{

    Context activity;
    public CommentListAdapter(Context activity) 
    {
  this.activity=activity;
        // TODO Auto-generated constructor stub
  }
    
  @Override
  public int getCount() {
      // TODO Auto-generated method stub
      return CommonFunction.sSearchYammp.size();
  }

  @Override
  public Object getItem(int arg0) {
      // TODO Auto-generated method stub
      return null;
  }

  @Override
  public long getItemId(int arg0) {
      // TODO Auto-generated method stub
      return 0;
  }

  @Override
  public View getView(int pos, View convertView, ViewGroup arg2) {
      // TODO Auto-generated method stub
      LayoutInflater inflator = ((Activity) activity).getLayoutInflater();
      convertView = inflator.inflate(R.layout.comment_row_in_list, null);
      ImageView mImage=(ImageView)convertView.findViewById(R.id.monkey);
      TextView place=(TextView)convertView.findViewById(R.id.commentPlace);
      TextView time=(TextView)convertView.findViewById(R.id.mTime);
      if(pos<10)
      {
      place.setText("@"+CommonFunction.sSearchYammp.get(pos).get_location());
      }
      return convertView;
      
  }
}
