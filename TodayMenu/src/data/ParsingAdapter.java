package data;

import java.util.ArrayList;

import com.parkse.todaymenu.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ParsingAdapter extends BaseAdapter implements OnClickListener{

	private Context mContext;
	private int mLayout;
	private ArrayList<String> mArray;
	private TextView textview1;
	private TextView textview2;
	private TextView textview3;
	private TextView textview4;
	private TextView textview5;
	
	public ParsingAdapter() {
		
	}

	public ParsingAdapter(Context context, int layout, ArrayList<String> items){
		mContext = context;
		mLayout = layout;
		mArray = items;
	}
	
	public int getCount() {
		return mArray.size();
	}

	public Object getItem(int position) {
		return mArray.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(view == null){
			view = LayoutInflater.from(mContext).inflate(mContext.getResources().getLayout(mLayout), null);
		}
		textview1 = (TextView)view.findViewById(R.id.textview_field1);
		textview2 = (TextView)view.findViewById(R.id.textview_field2);
		textview3 = (TextView)view.findViewById(R.id.textview_field3);
		textview4 = (TextView)view.findViewById(R.id.textview_field4);
		textview5 = (TextView)view.findViewById(R.id.textview_field5);
		
		String data = mArray.get(position);
		String[] dataSplit = data.split("#");
		if(data != null){
			textview1.setText(dataSplit[0].replace("@", "\n"));
			textview2.setText(dataSplit[1].replace("@", "\n"));
			textview3.setText(dataSplit[2].replace("@", "\n"));
			textview4.setText(dataSplit[3].replace("@", "\n"));
			textview5.setText(dataSplit[4].replace("@", "\n"));
			
		}
		return view;
	}
	
	public void refresh(){
		this.notifyDataSetChanged();
	}

	public void clear() {
		mArray.clear();
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}



}
