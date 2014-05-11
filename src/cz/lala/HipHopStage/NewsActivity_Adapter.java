package cz.lala.HipHopStage;

import java.util.ArrayList;


import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsActivity_Adapter extends BaseAdapter {
	
	 static final String KEY_TEXT = "text";
	 static final String KEY_TITLE = "title";
	
	ArrayList<Bitmap> images;
	String[] titles;
	String[] texts;
	private LayoutInflater inflater = null;
	ArrayList<HashMap<String, String>> menuItems;
	
	public NewsActivity_Adapter(Context context,ArrayList<Bitmap> images, String[] titles , String[] texts )
	{
		this.images = images;
		this.titles = titles;
		this.texts = texts;
		inflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public NewsActivity_Adapter(Context context,
			ArrayList<Bitmap> images,
			ArrayList<HashMap<String, String>> menuItems) {
	inflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	this.images = images;	
	this.menuItems = menuItems;
	}

	@Override
	public int getCount() {
		return menuItems.size();
	
	}

	@Override
	public Object getItem(int position) {
		return menuItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 View layout = convertView;
	        if (layout == null)
	        layout = inflater.inflate(R.layout.act_newsactivity_row, parent,false);
	        TextView title = (TextView)layout.findViewById(R.id.act_newsactivity_row_title);
	        TextView text = (TextView)layout.findViewById(R.id.act_newsactivity_row_text);
	        ImageView image = (ImageView)layout.findViewById(R.id.act_newsactivity_row_image);
	        
	        text.setText(menuItems.get(position).get(KEY_TEXT));
	        title.setText(menuItems.get(position).get(KEY_TITLE));
	        image.setImageBitmap(images.get(position));
	        
	        return layout;
	}

}
