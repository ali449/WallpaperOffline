package ir.alimoradian.wo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

public class ImageShow extends Activity{
	
	static TouchImageView tiv;
	static int cate;
	static int pos;
	int maxLen;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageshow);
		Intent i = getIntent();
		cate = i.getIntExtra("selectedCategory", 0);
		pos = i.getIntExtra("selectedPosition", 1);
		maxLen = i.getIntExtra("idsLen", 2);
		tiv = (TouchImageView)findViewById(R.id.imgshow);
		setImage();
	}
	
	public static void setImage()
	{	
		try{
			if(cate == 0)
				tiv.setImageResource(Pictures.flowersId[pos]);
			else if(cate == 1)
				tiv.setImageResource(Pictures.cityId[pos]);
			else
				tiv.setImageResource(Pictures.natureId[pos]);
			tiv.setMaxZoom(4f);
		}
		catch(Exception e)
		{
			Log.w("res", "Error in set rsourece");
		}
		
	}
	
	public void on_btnSave_Click(View v)
	{
		File root = Environment.getExternalStorageDirectory();
		File dir = new File(root.getAbsolutePath() + "/WallpaperOffline");
		if(!dir.exists())
			dir.mkdirs(); //--Create Folder--
		int resId;
		if(cate == 0)
			resId = Pictures.flowersId[pos];
		else if(cate == 1)
			resId = Pictures.cityId[pos];
		else
			resId = Pictures.natureId[pos];
		Bitmap bm = BitmapFactory.decodeResource(getResources(), resId);
		try{
			FileOutputStream out = new FileOutputStream(dir + "/img" + System.currentTimeMillis() + ".jpg");
			bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
			Toast.makeText(getApplicationContext(), "Saved Successfully!", Toast.LENGTH_SHORT).show();
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + dir)));
			out.flush();
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void on_btnSet_click(View v)
	{
		int resId;
		if(cate == 0)
			resId = Pictures.flowersId[pos];
		else if(cate == 1)
			resId = Pictures.cityId[pos];
		else
			resId = Pictures.natureId[pos];
		WallpaperManager wpm = WallpaperManager.getInstance(getApplicationContext());
		try{
			wpm.setResource(resId);
			Toast.makeText(getApplicationContext(), "Set Successfully!", Toast.LENGTH_SHORT).show();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	

}
