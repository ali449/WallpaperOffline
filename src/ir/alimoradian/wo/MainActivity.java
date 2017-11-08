package ir.alimoradian.wo;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	ListView lv;
	ArrayList<String> categories = new ArrayList<String>();
	int[] logoes = {
			R.drawable.logo1,
			R.drawable.logo2,
			R.drawable.logo3
	};
	
	AlertDialog ad;//--about dialog--
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        categories.add("Flower");
        categories.add("City");
        categories.add("Nature");
        
        setTitle("Offline Wallpapers");
        
        MyAdapter a = new MyAdapter(getBaseContext(), categories, logoes);
        
        lv = (ListView)findViewById(R.id.listView1);
        lv.setAdapter(a);
        lv.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> av, View view, int i, long l)
        	{
        		try{
        			Intent intent = new Intent(MainActivity.this, Pictures.class);
        			intent.putExtra("category", i);
            		startActivity(intent);
            		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        		}catch(ActivityNotFoundException e){
        			e.printStackTrace();
        		}catch(Exception e){
        			e.printStackTrace();
        		}
        	}
		});
        
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	super.onCreateOptionsMenu(menu);
    	MenuItem menuItem = menu.add(0, 0, 0, "About");
    	menuItem.setIcon(R.drawable.info);
    	menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	if(item.getItemId() != 0)
    		return false;
    	
    	ad = new AlertDialog.Builder(this).create();
    	ad.setTitle("About");
    	ad.setIcon(R.drawable.info);
    	ad.setMessage("alimoradian449@yahoo.com");
    	ad.setButton("close", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				ad.dismiss();
			}
		});
    	ad.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    	ad.show();
    	
    	return true;
    }

    class MyAdapter extends BaseAdapter implements ListAdapter
    {
    	private ArrayList<String> list = new ArrayList<String>();
    	private Context context;
    	private int[] images;
    	
    	public MyAdapter(Context co, ArrayList<String> titles, int[] imageIDs)
    	{
    		this.context = co;
    		this.list = titles;
    		this.images = imageIDs;
    	}
    	
    	@Override
    	public int getCount()
    	{
    		return list.size();
    	}
    	
    	@Override
    	public Object getItem(int pos)
    	{
    		return list.get(pos);
    	}
    	
    	@Override
    	public long getItemId(int pos)
    	{
    		return 0;
    	}
    	
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent)
    	{
    		View view = convertView;
    		if(view == null)
    		{
    			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    			view = inflater.inflate(R.layout.custom_list_view, null);
    		}
    		TextView tv = (TextView)view.findViewById(R.id.category);
    		tv.setText(list.get(position));
    		
    		ImageView iv = (ImageView)view.findViewById(R.id.imgLogo);
    		iv.setImageResource(images[position]);
    		
    		return view;
    		
    	}
    	
    }
}
