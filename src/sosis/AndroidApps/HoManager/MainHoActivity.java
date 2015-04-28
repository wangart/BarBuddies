package sosis.AndroidApps.HoManager;


import sosis.AndroidApps.Provider.HoProvider;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import database.HoTable;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainHoActivity extends FragmentActivity {
	private final static long SHOT_ID = 1;
	private Intent lastIntent;
		
	private ShowcaseView showcaseView;
	
	private HoViewPager mPager;
	private  HoFragmentAdapter pagerAdapter;

	private MenuItem saveItem;
	public final static String SAVE_BUTTON_TITLE = "Save";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		//TODO: Neeed to put instructions here before dismiss
		
		final ActionBar bar = getActionBar();
		bar.setDisplayShowTitleEnabled(false);
		bar.setDisplayShowHomeEnabled(false);
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		setContentView(R.layout.main_activity);

		pagerAdapter = new HoFragmentAdapter(getSupportFragmentManager());

		mPager = (HoViewPager) findViewById(R.id.pager);
		mPager.setAdapter(pagerAdapter);        
		mPager.setOffscreenPageLimit(2);
		
		
		final Activity activity = this;
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			
			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				if (0 == tab.getPosition()){
					hideSoftKeyboard();
					if (null!= saveItem){
						saveItem.setVisible(false);
					}
					hideErrorMessages();
				}				

			}
			
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				int tabPosition = tab.getPosition();
				mPager.setCurrentItem(tabPosition);
					if (0 == tabPosition && null != saveItem){
						saveItem.setVisible(true);
						if (null == showcaseView) {
							showcaseView = new ShowcaseView.Builder(activity)
							
							.setTarget(new ViewTarget(R.id.lock_button, activity))
							.setContentTitle("Locking and Hiding")
							.setContentText("Enable lock mode before handing your phone off to a Ho.\nSecret Gesture:" +
									" Double tap to hide/show secret elements.")	
									 .singleShot(SHOT_ID)
									 .build();
						    if (showcaseView.getVisibility() == View.VISIBLE){
						    	setFocusableTextViews(false);
						    	pagerAdapter.getFormFragment().hideSecretInfo();
						    }
						    showcaseView.overrideButtonClick(new View.OnClickListener() {
										
										@Override
										public void onClick(View v) {
											showcaseView.hideButton();
											showcaseView.hide();
											setFocusableTextViewsOnTouch(true);
											LinearLayout secretLayout = (LinearLayout) activity.findViewById(R.id.secret_layout);
											secretLayout.setVisibility(View.VISIBLE);

										}
									});	
						}
					    

					}
					if (2 == tabPosition){
						TextView hoCount = (TextView) findViewById(R.id.ho_count_view);
						Cursor c = getContentResolver().query(HoProvider.CONTENT_URI, new String[] {HoTable.COLUMN_NAME}, null, null, null);
						hoCount.setText("Ho Count: " + c.getCount());
					}
			}
			
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
			}
		};
		
		mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				getActionBar().setSelectedNavigationItem(position);
			}
			
			@Override
			public void onPageScrolled(int page1, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
		  for (int i = 0; i < 3; i++) {
			  switch(i){
			  case 0:
				  bar.addTab(bar.newTab().setIcon(R.drawable.ic_action_add_person).setTabListener(tabListener));
				  break;
			  case 1:
				  bar.addTab(bar.newTab().setIcon(R.drawable.ic_action_cloud).setTabListener(tabListener));
				  break;
			  case 2:
				  bar.addTab(bar.newTab().setIcon(R.drawable.ic_action_view_as_list).setTabListener(tabListener));
				  break;
			  }
		        
		  }
		  
		  mPager.setCurrentItem(1);
	}
		  
	public ShowcaseView getShowcaseView(){
		return showcaseView;
	}
	
	@Override
	public void onBackPressed() {
		if (fieldsFilled()){
			new AlertDialog.Builder(this)
	        .setTitle("Delete Ho?")
	        .setMessage("If you exit now, you will lose that ho forever!")
	        .setNegativeButton("Don't Exit", null)
	        .setPositiveButton("Exit", new OnClickListener() {

	            public void onClick(DialogInterface arg0, int arg1) {
	                MainHoActivity.super.onBackPressed();
	                // TODO: if ho info written and back pressed and yes pressed, toast message --> "that ho is officially lost"
	            }
	        }).create().show();
		}
		else {
			MainHoActivity.super.onBackPressed();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (RESULT_OK == resultCode){
			String imagePath = data.getStringExtra(SelfieActivity.IMAGE_PATH);
			ImageView mImageView = (ImageView) findViewById(R.id.take_a_selfie);
			mImageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));		
		}
		
	}
	
	private boolean fieldsFilled(){
		EditText [] mandatoryFields = new EditText [3];
		mandatoryFields[0]= (EditText)findViewById(R.id.firstName);
		mandatoryFields[1]= (EditText)findViewById(R.id.lastName);
		mandatoryFields[2]= (EditText)findViewById(R.id.telephone_number);
		for (int i = 0; i < 3; ++i){
			if (mandatoryFields[i] != null && !mandatoryFields[i].getText().toString().trim().equals("")){
				return true;
			}
		} 
		return false;
	}
	
	@Override 
	protected void onNewIntent(Intent intent){
		super.onNewIntent(intent);
		
		this.lastIntent = intent;
	}
	
	public Intent getLastIntent(){
		return lastIntent;
	}
	
	@Override
	public boolean onCreateOptionsMenu (Menu menu){		
		saveItem = menu.add(SAVE_BUTTON_TITLE).setIcon(R.drawable.ic_action_save);
		saveItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT | 
				MenuItem.SHOW_AS_ACTION_ALWAYS);
		if (0 != mPager.getCurrentItem()){
			saveItem.setVisible(false);
		}
		saveItem.setOnMenuItemClickListener(new OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				return false;
			}
			
		});
		return super.onCreateOptionsMenu(menu);
	}

	public HoViewPager getPager(){
		return mPager;
	}	
	
	private void hideErrorMessages(){
		EditText firstNameView = (EditText) findViewById(R.id.firstName);
		if (null != firstNameView){
			firstNameView.setError(null);
		}
		EditText numberView = (EditText) findViewById(R.id.telephone_number);
		if (null != numberView){
			numberView.setError(null);
		}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	

	
	public void hideSoftKeyboard(){
		EditText tmpEditableView = (EditText)findViewById(R.id.firstName);
		if (null!= tmpEditableView){
			InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			mgr.hideSoftInputFromWindow(tmpEditableView.getWindowToken(), 0);					
			}
		tmpEditableView = (EditText) findViewById(R.id.lastName);
		if (null!= tmpEditableView){
			InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			mgr.hideSoftInputFromWindow(tmpEditableView.getWindowToken(), 0);
			}			
		tmpEditableView = (EditText) findViewById(R.id.telephone_number);
		if (null!= tmpEditableView){
			InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			mgr.hideSoftInputFromWindow(tmpEditableView.getWindowToken(), 0);					
			}
	}

	public void setFocusableTextViewsOnTouch(boolean focusable){
		EditText tmpEditableView = (EditText)findViewById(R.id.firstName);
		tmpEditableView.setFocusableInTouchMode(focusable);
		
		tmpEditableView = (EditText) findViewById(R.id.lastName);
		tmpEditableView.setFocusableInTouchMode(focusable);
		
		tmpEditableView = (EditText) findViewById(R.id.telephone_number);
		tmpEditableView.setFocusableInTouchMode(focusable);
		
	}
	
	public void setFocusableTextViews(boolean focusable){
		EditText tmpEditableView = (EditText)findViewById(R.id.firstName);
		tmpEditableView.setFocusable(focusable);
		
		tmpEditableView = (EditText) findViewById(R.id.lastName);
		tmpEditableView.setFocusable(focusable);
		
		tmpEditableView = (EditText) findViewById(R.id.telephone_number);
		tmpEditableView.setFocusable(focusable);


	}
	
}
