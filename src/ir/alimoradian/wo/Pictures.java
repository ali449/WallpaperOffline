package ir.alimoradian.wo;

import android.app.Activity;
import android.os.Bundle;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class Pictures extends Activity{
	public static final Integer[] flowersId = {
		R.drawable.flower1, R.drawable.flower2, R.drawable.flower3, R.drawable.flower4, R.drawable.flower5,
		R.drawable.flower6, R.drawable.flower7, R.drawable.flower8, R.drawable.flower9, R.drawable.flower10,
		R.drawable.flower11, R.drawable.flower12, R.drawable.flower13, R.drawable.flower14, R.drawable.flower15,
		R.drawable.flower16, R.drawable.flower17, R.drawable.flower18, R.drawable.flower19, R.drawable.flower20
	};
	public static final Integer[] natureId = {
			R.drawable.nature1, R.drawable.nature2, R.drawable.nature3, R.drawable.nature4, R.drawable.nature5,
			R.drawable.nature6, R.drawable.nature7, R.drawable.nature8, R.drawable.nature9, R.drawable.nature10,
			R.drawable.nature11, R.drawable.nature12, R.drawable.nature13, R.drawable.nature14
		};
	public static final Integer[] cityId = {
			R.drawable.city1, R.drawable.city2, R.drawable.city3, R.drawable.city4, R.drawable.city5,
			R.drawable.city6, R.drawable.city7, R.drawable.city8, R.drawable.city9, R.drawable.city10,
			R.drawable.city11, R.drawable.city12, R.drawable.city13, R.drawable.city14, R.drawable.city15,
			R.drawable.city16, R.drawable.city17, R.drawable.city18
		};
	
	public static int selectedCategory;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pictures);
		selectedCategory =  getIntent().getIntExtra("category", 0);
		Log.d("category is: ", Integer.toString(selectedCategory));
		GridView gridView = (GridView)findViewById(R.id.gridView);
		ImageAdapter ia = new ImageAdapter(getApplicationContext());
		gridView.setAdapter(ia);
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				try{
        			Intent intent = new Intent(Pictures.this, ImageShow.class);
        			intent.putExtra("selectedCategory", selectedCategory);
        			intent.putExtra("selectedPosition", position);
        			if(selectedCategory == 0)
        				intent.putExtra("idsLen", flowersId.length);
        			else if(selectedCategory == 1)
        				intent.putExtra("idsLen", cityId.length);
        			else
        				intent.putExtra("idsLen", natureId.length);
            		startActivity(intent);
        		}catch(ActivityNotFoundException e){
        			e.printStackTrace();
        		}catch(Exception e){
        			e.printStackTrace();
        		} 
			}
		});
	}
	
	@Override
	public void onBackPressed()
	{
		finish();
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	
	public static int convertDpToPixls(float dp, Context c)
	{
		Resources res = c.getResources();
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
	}
	
	
	public class ImageAdapter extends BaseAdapter
	{
		private Context context;
		ImageView imageView;
		LayoutInflater inflater;
		
		public ImageAdapter(Context c)
		{
			context = c;
			inflater = (LayoutInflater.from(c));
		}
		
		public int getCount()
		{
			if(selectedCategory == 0)
				return flowersId.length;
			else if(selectedCategory == 1)
				return cityId.length;
			else
				return natureId.length;
		}
		
		public Object getItem(int position)
		{
			return null;
		}
		
		public long getItemId(int position)
		{
			return 0;
		}
		
		public View getView(int position, View convertView, ViewGroup parent)
		{
			convertView = inflater.inflate(R.layout.activity_gridview, null);
			ImageView icon = (ImageView)convertView.findViewById(R.id.icon);
			
			if(selectedCategory == 0)
				icon.setImageResource(flowersId[position]);
			else if(selectedCategory == 1)
				icon.setImageResource(cityId[position]);
			else
				icon.setImageResource(natureId[position]);
		
			return convertView;
			/*if(convertView == null)
			{
				imageView = new ImageView(context);
				float w = 100, h = 100;
				int wPixel = convertDpToPixls(w, getBaseContext());
				int hPixel = convertDpToPixls(h, getBaseContext());
				imageView.setLayoutParams(new GridView.LayoutParams(wPixel, hPixel));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setMaxHeight(60);
			}
			else
				imageView = (ImageView)convertView;
			if(selectedCategory == 0)
				imageView.setImageResource(flowersId[position]);
			else if(selectedCategory == 1)
				imageView.setImageResource(cityId[position]);
			else
				imageView.setImageResource(natureId[position]);
			return imageView;*/
			
		}
		
	}
	
	
	
	
}
