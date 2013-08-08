package com.pk.pad.imagedisplay;


/***************************
 *  Kai and Peng 's codes
 ***************************/
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
//import com.example.android.apis.R;


public class ImageDisplay extends Activity  {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i(TAG, "onCreate()");

    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.activity_image_display);


    	splashImage=(ImageView)findViewById(R.id.imagedsp);
    	imageTop = splashImage.getTop();
    	imageLeft = splashImage.getLeft();
    	
    	animSet = new AnimatorSet();
        animSet.setDuration(ANIMATION_DURATION_SCALE);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
            	splashImage.setAlpha(1f);
                //expandedImageView.setVisibility(View.GONE);
            	mCurrentAnim = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            	splashImage.setAlpha(1f);
                //expandedImageView.setVisibility(View.GONE);
            	mCurrentAnim = null;
            }
        });
    	
//    	mMatrix = new Matrix();
//    	savedMatrix = new Matrix();
//    	matrixValues = new float[9];
    	//for debugging purpose only 
    	mRendering = true;

    	
    	renderThread = new Thread()
    	{
    		@Override
            public void run() {
    			while(mRendering && (splashImage != null)) {
	
	    			
	                Message msg = new Message();
	                
	                Bundle b = new Bundle();
	                b.putInt(mKey, KEY_SWITCH);
	                msg.setData(b);
	                mHandler.sendMessage(msg);
	                try {
	                	
	                	
	                    Thread.sleep(SPLASH_DISPLAY_LENGTH);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	                
	                
	                /* for debug purpose */
	                //mRendering = false;
    			}
                

            }
        };
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

	}    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_display, menu);
		return true;
	}
	
	
    
    @Override    
    public void onStart() {
    	Log.i(TAG, "onStart()");
    	super.onStart();

    	renderThread.start();
   	    
    }
    

    /**
     * Called after onRestoreInstanceState(Bundle), onRestart(), or onPause(),
     * for your activity to start interacting with the user.  This is a good
     * place to begin animations, open exclusive-access devices (such as the
     * camera), etc.
     * 
     * Derived classes must call through to the super class's implementation
     * of this method.  If they do not, an exception will be thrown.
     */
    @Override
    protected void onResume() {
        Log.i(TAG, "onResume()");

        /* start the render thread */
        //renderThread.start();

        super.onResume();

        

    }
    
    /**
     * Called as part of the activity lifecycle when an activity is going
     * into the background, but has not (yet) been killed.  The counterpart
     * to onResume(). 
     */
    @Override
    protected void onPause() {
        Log.i(TAG, "onPause()");
        
        super.onPause();
 
    }


    /**
     * Called when you are no longer visible to the user.  You will next
     * receive either {@link #onStart}, {@link #onDestroy}, or nothing,
     * depending on later user activity.
     */
    @Override
    protected void onStop() {
        Log.i(TAG, "onStop()");
        super.onStop();

        
        if(renderThread!=null)
        {
        	//renderThread.stop();
        }

    }
    
    private Handler mHandler = new Handler() {
    	
        @Override
        public void handleMessage(Message msg) {
        	int mode = msg.getData().getInt(mKey);
        	Log.i(TAG, "mHandler with msg " + mode);

        	synchronized(splashImage) {
	        	switch(mode) {
	        	case KEY_SWITCH:
	        		if(++splashImageCounter == mImageIds.length)
	            		splashImageCounter = 0;
	                
	                
	        		splashImage.setImageResource(mImageIds[splashImageCounter]);
//	        		splashImage.setTop(imageTop);
//	        		splashImage.setLeft(imageLeft);
	        		
	        		imageAnimation();
//	        		 
//	        		 /* scale the image a little bit */
//	        		 float mImageHeight = splashImage.getDrawable().getIntrinsicHeight();
//	        		 float mImageWidth = splashImage.getDrawable().getIntrinsicWidth();
//	        		 mMatrix = splashImage.getImageMatrix();
//		             mMatrix.getValues(matrixValues);
//		             float scale = 1.5f;//rp to add some randomness
//		             
//		             mMatrix.postScale(scale, scale, mImageWidth/2, mImageHeight/2);
//		             
//		             splashImage.setImageMatrix(mMatrix);
//		             splashImage.setScaleType(ScaleType.MATRIX);
//		             splashImage.invalidate();
		             
	        		break;
	        	case KEY_SCALE:

	        		
	                
	                
	        		break;
	        	
	        	
	           
	        	}
        	}
        }
    };
    
    private void imageAnimation() {

        //float scale = matrixValues[Matrix.MSCALE_X];
        float scaleUp = 1.25f; //rp to use some randomness 
        float top     = imageTop+100f; //rp to use some randomness
        float left    = imageLeft+100f; //rp to use some randomness
        float alpha   = splashImage.getAlpha() / 2;//rp to use some randomness
        
        
        if (mCurrentAnim != null) {
        	animSet.cancel();
        }
 
        animSet.play(ObjectAnimator.ofFloat(splashImage, View.SCALE_X, scaleUp))
               .with(ObjectAnimator.ofFloat(splashImage, View.SCALE_Y, scaleUp))
               .with(ObjectAnimator.ofFloat(splashImage, View.X, left))
               .with(ObjectAnimator.ofFloat(splashImage, View.Y, top));
               //.with(ObjectAnimator.ofFloat(splashImage, View.ALPHA, alpha));
 
        
        animSet.start();
        mCurrentAnim = animSet;
    	
    }

    


    private Integer[] mImageIds = {
            R.drawable.sample_0, R.drawable.sample_1, R.drawable.sample_2,
            R.drawable.sample_3, R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7};
    
    private static final long SPLASH_DISPLAY_LENGTH = 12000; 
    private static final long ANIMATION_DURATION_SCALE = SPLASH_DISPLAY_LENGTH - 500;    

    
    private Thread renderThread;
    private boolean mRendering;
    
    private ImageView splashImage= null;
    private int splashImageCounter   = 0;
    private static final String mKey = "HANDLER_MODE";
    private static final int KEY_SWITCH = 0;
    private static final int KEY_SCALE   = 1;    
    
//    private Matrix mMatrix;
//    private float[] matrixValues;
//    private float mImageHeight = 0;
//    private float mImageWidth = 0;
    
    private AnimatorSet mCurrentAnim = null;
    private int imageTop;
    private int imageLeft;
//    private float imageScaleX, imageScaleY;
    private float imageAlpha;
    
    private AnimatorSet animSet;

    

    // Debugging tag.
    private static final String TAG = "ImageDisplay";

    
    
    


}
