package ir.alimoradian.wo;

import java.util.Calendar;

import android.content.Context;
import android.content.pm.FeatureInfo;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class TouchImageView extends ImageView{

	Matrix matrix;
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;
	
	PointF last = new PointF();
	PointF start = new PointF();
	float minScale = 1f;
	float maxScale = 3f;
	float[] m;
	
	int viewWidth, viewheight;
	static final int CLICK = 3;
	float saveScale = 1f;
	protected float origWidth, origHeight;
	int oldMeasuredWidth, oldMeasuredheight;
	
	ScaleGestureDetector mScaleGestureDetector;
	
	Context context;
	
	//--For swipe--
	float x1, x2;
	Animation anim;
	long startClickTime;
	static final int MIN_DISTANCE = 150;
	static final int MAX_SWIPE_TIME = 200;
	
	public TouchImageView(Context c)
	{
		super(c);
		sharedConstructing(c);
	}
	
	public TouchImageView(Context c, AttributeSet attrs)
	{
		super(c, attrs);
		sharedConstructing(c);
	}
	
	public void sharedConstructing(Context c)
	{
		super.setClickable(true);
		context = c;
		mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
		matrix = new Matrix();
		m = new float[9];
		setImageMatrix(matrix);
		setScaleType(ScaleType.MATRIX);
		
		setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				// TODO Auto-generated method stub
				mScaleGestureDetector.onTouchEvent(event);
				PointF curr = new PointF(event.getX(), event.getY());
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					last.set(curr);
					start.set(last);
					mode = DRAG;
					
					startClickTime = Calendar.getInstance().getTimeInMillis();
					x1 = event.getX();
					break;
				case MotionEvent.ACTION_MOVE:
					if(mode == DRAG)
					{
						float deltaX = curr.x - last.x;
						float deltaY = curr.y - last.y;
						float fixTransX = getFixDragTrans(deltaX, viewWidth, origWidth * saveScale);
						float fixTransY = getFixDragTrans(deltaY, viewheight, origHeight * saveScale);
						matrix.postTranslate(fixTransX, fixTransY);
						fixTrans();
					}
					break;
				case MotionEvent.ACTION_UP:
					mode = NONE;
					int xDiff = (int)Math.abs(curr.x - start.x);
					int yDiff = (int)Math.abs(curr.y - start.y);
					if(xDiff < CLICK && yDiff < CLICK)
						performClick();
					
					long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
					x2 = event.getX();
					if(Math.abs(x2 - x1) > MIN_DISTANCE && clickDuration < MAX_SWIPE_TIME)
					{
						if(x2 > x1)
						{
							ImageShow.pos--;
							anim = AnimationUtils.loadAnimation(context, R.anim.push_right_in) ;
							ImageShow.tiv.startAnimation(anim);
							ImageShow.setImage();
						}
						else if(x2 < x1)
						{
							ImageShow.pos++;
							anim = AnimationUtils.loadAnimation(context, R.anim.push_left_in);
							ImageShow.tiv.startAnimation(anim);
							ImageShow.setImage();
						}
					}
					break;
				case MotionEvent.ACTION_POINTER_UP:
					mode = NONE;
					break;
				default:
					break;
				}
				setImageMatrix(matrix);
				invalidate();
				return true;
			}
		});
	}
	
	public void setMaxZoom(float x)
	{
		maxScale = x;
	}
	
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
	{
		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector)
		{
			mode = ZOOM;
			return true;
		}
		
		@Override
		public boolean onScale(ScaleGestureDetector detector)
		{
			float mScaleFactor = detector.getScaleFactor();
			float origScale = saveScale;
			saveScale *= mScaleFactor;
			if(saveScale > maxScale)
			{
				saveScale = maxScale;
				mScaleFactor = maxScale / origScale;
			}
			else if(saveScale < minScale)
			{
				saveScale = minScale;
				mScaleFactor = minScale / origScale;
			}
			
			if(origWidth * saveScale <= viewWidth || origHeight * saveScale <= viewheight)
				matrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2, viewheight / 2);
			else
				matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());
			fixTrans();
			return true;
		}
	}
	
	void fixTrans()
	{
		matrix.getValues(m);
		float transX = m[Matrix.MTRANS_X];
		float transY = m[Matrix.MTRANS_Y];
		
		float fixTransX = getFixTrans(transX, viewWidth, origWidth * saveScale);
		float fixTransy = getFixTrans(transY, viewheight, origHeight * saveScale);
		
		if(fixTransX != 0 || fixTransy != 0 )
			matrix.postTranslate(fixTransX, fixTransy);
	}
	
	float getFixTrans(float trans, float viewSize, float contentSize)
	{
		float minTrans, maxTrans;
		if(contentSize <= viewSize)
		{
			minTrans = 0;
			maxTrans = viewSize - contentSize;
		}
		else
		{
			minTrans = viewSize - contentSize;
			maxTrans = 0;
		}
		
		if(trans < minTrans)
			return -trans + minTrans;
		if(trans > maxTrans)
			return -trans + maxTrans;
		return 0;
	}
	
	float getFixDragTrans(float delta, float viewSize, float contentSize)
	{
		if(contentSize <= viewSize)
			return 0;
		return delta;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		viewWidth = MeasureSpec.getSize(widthMeasureSpec);
		viewheight = MeasureSpec.getSize(heightMeasureSpec);
		
		if(oldMeasuredWidth == viewWidth && oldMeasuredheight == viewheight || viewWidth == 0 || viewheight == 0)
		 return;
		oldMeasuredheight = viewheight;
		oldMeasuredWidth = viewWidth;
		if(saveScale == 1)
		{
			//--Fit to screen--
			float scale;
			
			Drawable drawable = getDrawable();
			if(drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0)
				return;
			int bmWidth = drawable.getIntrinsicWidth();
			int bmHeight = drawable.getIntrinsicHeight();
			
			Log.d("bmSize", "bnWidth: " + bmWidth + " bmHeight:" + bmHeight);
			
			float scaleX = (float)viewWidth / (float)bmWidth;
			float scaleY = (float)viewheight / (float)bmHeight;
			scale = Math.min(scaleX, scaleY);
			matrix.setScale(scale, scale);
			
			//--Center the Image--
			float redundantYSpace = (float)viewheight - (scale * (float)bmHeight);
			float redundantXSpace = (float)viewWidth - (scale * (float)bmWidth);
			redundantYSpace /= (float)2;
			redundantXSpace /= (float)2;
			
			matrix.postTranslate(redundantXSpace, redundantYSpace);
			
			origWidth = viewWidth - 2 * redundantXSpace;
			origHeight = viewheight - 2 * redundantYSpace;
			setImageMatrix(matrix);
		}
		fixTrans();
		
	}
	
}
