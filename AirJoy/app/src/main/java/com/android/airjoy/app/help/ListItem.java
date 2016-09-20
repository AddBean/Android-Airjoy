package com.android.airjoy.app.help;

import java.util.HashMap;
import java.util.Map;

/**
 * listview部件类
 *
 * @author 贾豆
 *
 */
public class ListItem
{
	/**类型*/
	public int mType;
	/**键值对应Map*/
	public Map<Integer, ?> mMap ;

	public ListItem(int type, HashMap<Integer, ?> map)
	{
		mType = type;
		mMap = map;
	}
}
