package sosis.AndroidApps.HoManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class SelfieActivity extends Activity {
	private Handler handler;
	private static String picturePath;
	
	public static final String IMAGE_PATH = "Image Path";
	
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final int MEDIA_TYPE_VIDEO = 0;
	
	final Runnable runnable = new Runnable()
	{
	    public void run() 
	    {
	    	startCamera();
	    }
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handler = new Handler();
		setContentView(R.layout.selfie_activity);
		handler.postDelayed(runnable, 2000);
	}
	
	private void startCamera(){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

	    Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

	    // start the image capture Intent
	    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

	}
	
	

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	        	Intent imageData = new Intent();
	        	if (!picturePath.equals("") || picturePath!=null){
	        		imageData.putExtra(IMAGE_PATH, picturePath);
		        	setResult(RESULT_OK, imageData);
	        	}
	        	
	        } else if (resultCode == RESULT_CANCELED) {
	            setResult(RESULT_CANCELED);
	        } else {
	        	Toast.makeText(getApplicationContext(), "There was an error capturing/saving the selfie.", Toast.LENGTH_SHORT);
	        	setResult(RESULT_CANCELED);
	        }
	        finish();
	  }

}

	
	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "Take-A-Selfie");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }
	    picturePath = mediaFile.getPath();
	    return mediaFile;
	}
}
