package com.android.airjoy.app.pcfile.listview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.android.airjoy.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 一个允许不同模块的Adapter类
 * @author 贾豆
 *
 */
public class ListAdapter extends BaseAdapter
{
	private List<ListItem> mData;
	private LayoutInflater mInflater;
	private ArrayList<Integer> TypeList = new ArrayList<Integer>();
	private Context _context;
	public void AddType(int mResource)
	{
		TypeList.add(mResource);
	}



	public ListAdapter(Context context, List<ListItem> data)
	{
		this._context=context;
		mData = data;
		mInflater = LayoutInflater.from(context);
//		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
	public int getItemViewType(int position) {
		return mData.get(position).mType;
	}

	@Override
	public int getViewTypeCount() {
		if(TypeList.size()==0)
			return 1;
		else
			return TypeList.size();
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	public ListItem getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
//		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		int type = getItemViewType(position);
		if (convertView == null)
		{
			holder = new ViewHolder();
			ListItem item = getItem(position);
			convertView = mInflater.inflate(TypeList.get(type), null);
			for (Iterator<Integer> it = item.mMap.keySet().iterator(); it.hasNext();) // 遍历
			{
				int id = it.next();
				Object obj = convertView.findViewById(id);
				if(obj!=null)
				{
					holder.List_Object.add(obj);
					holder.List_id.add(id);
				}
			}
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		holder.SetValue(mData.get(position));
		return convertView;
	}


	public static class ViewHolder
	{
		ArrayList<Object> List_Object = new ArrayList<Object>();
		ArrayList<Integer> List_id = new ArrayList<Integer>();

		public boolean SetValue(ListItem item)
		{
			int i = 0;
			Object oV;
			for(Object obj:List_Object)
			{
//				try
				{
					int id = List_id.get(i);
					oV = item.mMap.get(id);

					if(obj.getClass().equals(TextView.class))
					{
						((TextView)obj).setText(oV.toString());
					}

					if(obj.getClass().equals(ImageView.class))
					{
						if(oV.getClass().equals(Integer.class))
						{
							((ImageView)obj).setImageResource((Integer)oV);
						}
					}

					if(obj.getClass().equals(ImageButton.class))
					{
						if(oV.getClass().equals(Integer.class))
						{
							((ImageButton)obj).setImageResource((Integer)oV);
						}

						if(oV.getClass().equals(OnClickListener.class))
						{
							((ImageButton)obj).setOnClickListener((OnClickListener)oV);
						}
					}
//				}catch (Exception e) {
//					// TODO: handle exception
//					e.printStackTrace();
				}

				i++;
			}
			return false;
		}
	}



}
