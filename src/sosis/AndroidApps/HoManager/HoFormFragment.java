package sosis.AndroidApps.HoManager;

import java.util.ArrayList;

import sosis.AndroidApps.Provider.HoProvider;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import database.HoTable;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class HoFormFragment extends Fragment {
	private static final long VIEW_ID = 2;
	
	private ShowcaseView lockInstructions;
	private ShowcaseView showcaseView;
	
	private EditText firstNameView;
	private EditText lastNameView;
	private EditText phoneNumberView;
	private Spinner ethnicitySpinner;
	private RatingBar faceBar;
	private RatingBar bodyBar;
	private RatingBar personalityBar;
	private RatingBar humorBar;
	private RatingBar bitchyBar;
	private Switch fuckableSwitch;
	private Switch friendableSwitch;
	private Switch dateableSwitch;	
	private ScrollView scrollview;
	private ImageButton lockButton;
	private ImageButton infoButton;
	private ImageButton takeASelfieButton;
	private View secretViews;
	
	private boolean isLocked;
		
	private boolean secretInfoHidden;
    private GestureDetector mDetector; 
    private MainHoActivity activity;
	
    public static final String HIDDEN_KEY = "Hidden";
    public static final String LOCKED_KEY = "Locked";
	private static final String HO_ADDED_TEXT = "Another Ho Has Been Added, Good Work!";

	
	public static HoFormFragment newInstance(){
		return new HoFormFragment();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		activity = (MainHoActivity) getActivity();
		
		isLocked = false;
		secretInfoHidden = false;
		setHasOptionsMenu(true);
		
		if (null!= savedInstanceState)
		{
			isLocked = savedInstanceState.getBoolean(LOCKED_KEY);
			secretInfoHidden = savedInstanceState.getBoolean(HIDDEN_KEY);
		}		
	
	}
	
	public class ShowSecretOnDoubleTap extends GestureDetector.SimpleOnGestureListener{
		@Override
	    public boolean onDown(MotionEvent event) { 
	        return true;
	    }
		
		/*
		@Override
		public boolean onScroll(MotionEvent firstEvent, MotionEvent secondEvent, float distanceX, float distanceY){
			/*if (isLocked && isItDiagonalSwipeUpRight(distanceX, distanceY)){
				isLocked = false;
				activity.getActionBar().show();
				activity.getPager().setPagingEnabled(true);			
				lockButton.setVisibility(View.VISIBLE);
			}
			return true;
			
		}*/
		
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			if (!isLocked){
				
				if (null!= showcaseView && showcaseView.getVisibility() == View.VISIBLE){
					showcaseView.hide();
					showcaseView.hideButton();
					activity.setFocusableTextViewsOnTouch(true);
					if (secretInfoHidden){
						secretViews.setVisibility(View.VISIBLE);
						secretInfoHidden = false;
					}
					else {
						secretViews.setVisibility(View.GONE);
						secretInfoHidden = true;
					}
				}
				
				else if (null!= activity.getShowcaseView() && 
						activity.getShowcaseView().getVisibility() == View.VISIBLE){
					activity.getShowcaseView().hideButton();
					activity.getShowcaseView().hide();
					activity.setFocusableTextViewsOnTouch(true);
					
					if (secretInfoHidden){
						secretViews.setVisibility(View.VISIBLE);
						secretInfoHidden = false;
					}
					else {
						secretViews.setVisibility(View.GONE);
						secretInfoHidden = true;
					}
				}
				
				else {
					if (secretViews.getVisibility() == View.GONE){
						secretViews.setVisibility(View.VISIBLE);
						secretInfoHidden = false;
					}
					else {
						secretViews.setVisibility(View.GONE);
						secretInfoHidden = true;
					}
					
				}
				
			}
			else {
				if (null!= lockInstructions && lockInstructions.getVisibility() == View.VISIBLE){
					lockInstructions.hide();
					lockInstructions.hideButton();
					activity.setFocusableTextViewsOnTouch(true);
				}
				
				isLocked = false;
				activity.getActionBar().show();
				activity.getPager().setPagingEnabled(true);			
				lockButton.setVisibility(View.VISIBLE);
				infoButton.setVisibility(View.VISIBLE);
				if (secretInfoHidden){
					secretViews.setVisibility(View.GONE);
				}
				else {
					secretViews.setVisibility(View.VISIBLE);
				}
			}
			
			return true;
		}
	}
	public boolean isSecretInfoHidden() {
		return secretInfoHidden;
	}
	public void hideSecretInfo(){
		secretViews.setVisibility(View.GONE);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.new_ho_fragment, container, false);
		
		setUpViews(rootView);
		
		if (isLocked){
			activity.getActionBar().hide();
			activity.getPager().setPagingEnabled(false);			
			lockButton.setVisibility(View.GONE);
			infoButton.setVisibility(View.GONE);
			secretViews.setVisibility(View.GONE);
		}
		
		mDetector = new GestureDetector(activity, new ShowSecretOnDoubleTap());

		return rootView;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){		
		ContentValues contentValues = new ContentValues();
		if (MainHoActivity.SAVE_BUTTON_TITLE.equals(item.getTitle())) {
			if(!areTextFieldsEmpty(firstNameView) && !areTextFieldsEmpty(phoneNumberView)){

				String firstName = firstNameView.getText().toString().trim();
				if (Character.isLowerCase(firstName.charAt(0))){
					firstName = firstName.substring(0,1).toUpperCase() + firstName.substring(1);
				}
				
				String lastName = lastNameView.getText().toString().trim();
				if (!lastName.equals("") && lastName.length()>2 && Character.isLowerCase(lastName.charAt(0))){
					lastName = lastName.substring(0,1).toUpperCase() + lastName.substring(1);
				}
				
				contentValues.put(HoTable.COLUMN_NAME, firstName + " " + lastName);
				
				String phoneNumber = phoneNumberView.getText().toString().trim();
				contentValues.put(HoTable.COLUMN_NUMBER, phoneNumber);
				
				ContentProviderResult [] res = addToExternalContactList(firstName, lastName, phoneNumber);		
			
				
				if (3 == res.length){
					String rawContactID = res[0].uri.getLastPathSegment();
					contentValues.put(HoTable.COLUMN_CONTACTS_ID, rawContactID);
					
					String phoneDataID = res[1].uri.getLastPathSegment();
					contentValues.put(HoTable.COLUMN_CONTACTS_NUMBER_ID, phoneDataID);
					
					String nameDataID = res[2].uri.getLastPathSegment();
					contentValues.put(HoTable.COLUMN_CONTACTS_DISPLAY_ID, nameDataID);
				}
				
				
				float faceRating = faceBar.getRating();
				contentValues.put(HoTable.COLUMN_FACE_RATING, faceRating);
				
				float bodyRating = bodyBar.getRating();
				contentValues.put(HoTable.COLUMN_BODY_RATING, bodyRating);
				
				float personalityRating = personalityBar.getRating();
				contentValues.put(HoTable.COLUMN_PERSONALITY_RATING, personalityRating);
				
				float humorRating = humorBar.getRating();
				contentValues.put(HoTable.COLUMN_HUMOR_RATING, humorRating);
				
				float bitchyRating = bitchyBar.getRating();
				contentValues.put(HoTable.COLUMN_BITCHINESS_RATING, bitchyRating);
				
				boolean isFuckable = fuckableSwitch.isChecked();
				contentValues.put(HoTable.COLUMN_FUCKABLE, isFuckable ? 1 : 0);
				
				boolean isFriendable = friendableSwitch.isChecked();
				contentValues.put(HoTable.COLUMN_FRIENDABLE, isFriendable ? 1 : 0);
				
				boolean isDateable = dateableSwitch.isChecked();
				contentValues.put(HoTable.COLUMN_DATEABLE, isDateable ? 1 : 0);
				
				int position = ethnicitySpinner.getSelectedItemPosition();
				String ethnicity = position != -1 ? getResources().getStringArray(R.array.ethnicity_spinner_choices)[position] : "";
				contentValues.put(HoTable.COLUMN_ETHNICITY, ethnicity);
			
				activity.getContentResolver().
				insert(HoProvider.CONTENT_URI, contentValues);
								
				clearFields();
				
				activity.getPager().setCurrentItem(2, true);
				Toast.makeText(activity, HO_ADDED_TEXT , Toast.LENGTH_LONG).show();
			}
			else {
				scrollview.fullScroll(ScrollView.FOCUS_UP);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private ContentProviderResult [] addToExternalContactList(String firstName, String lastName, String phoneNumber){
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		int rawContactInsertIndex = ops.size();
		
		ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
			.withValue(RawContacts.ACCOUNT_TYPE, null)
			.withValue(RawContacts.ACCOUNT_NAME, null)
			.build());
		
		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
			.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
			.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
			.withValue(Phone.TYPE, Phone.TYPE_MOBILE)
			.withValue(Phone.NUMBER, phoneNumber)
			.build());

		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
			.withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)
			.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
			.withValue(StructuredName.DISPLAY_NAME, firstName + " " + lastName)
			.build());
		
		try{
			return activity.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		}
		catch(OperationApplicationException e){
			e.printStackTrace();
		}
		catch(RemoteException e){
			e.printStackTrace();
		}
		return null;
	}
		
	private void clearFields() {
		firstNameView.getText().clear();
		lastNameView.getText().clear();
		phoneNumberView.getText().clear();
		ethnicitySpinner.setSelection(-1);
		faceBar.setRating(0);
		bodyBar.setRating(0);
		personalityBar.setRating(0);
		humorBar.setRating(0);
		bitchyBar.setRating(0);
		fuckableSwitch.setChecked(false);
		friendableSwitch.setChecked(false);
		dateableSwitch.setChecked(false);	
		ethnicitySpinner.setSelection(0);
	}
	
	private boolean areTextFieldsEmpty(EditText mandatoryTextField){
		boolean isEmpty = (mandatoryTextField.getText().toString().trim()).equals("");
		if (isEmpty){
			mandatoryTextField.setError("This field is required!" );
		}
		return isEmpty;
	}
	
	public void setSecretInfoHidden(boolean hidden){
		this.secretInfoHidden = hidden;
	}
	@Override
	public void onSaveInstanceState(Bundle out){
		super.onSaveInstanceState(out);
		out.putBoolean(HIDDEN_KEY,secretInfoHidden );
		out.putBoolean(LOCKED_KEY, isLocked);
	}
	
	private void setUpViews(ViewGroup root){
		takeASelfieButton = (ImageButton) root.findViewById(R.id.take_a_selfie);
		takeASelfieButton.setColorFilter(getResources().getColor(R.color.homanagergold_color));
		takeASelfieButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, SelfieActivity.class);
				startActivityForResult(intent, SelfieActivity.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
				
			}
		});
		
		infoButton = (ImageButton) root.findViewById(R.id.info_button);
		infoButton.setColorFilter(getResources().getColor(R.color.homanagergold_color));
		infoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activity.hideSoftKeyboard();
				secretViews.setVisibility(View.GONE);
				if (null == showcaseView){
					showcaseView = new ShowcaseView.Builder(activity)
					
					.setTarget(new ViewTarget(R.id.lock_button, activity))
					.setContentTitle("Locking and Hiding")
					.setContentText("Enable lock mode before handing your phone off to a Ho.\nSecret Gesture:" +
							" Double tap to hide/show secret elements.")								 
							 .build();
					activity.setFocusableTextViews(false);		
				}
				
				else {
					showcaseView.show();
					showcaseView.showButton();
					activity.setFocusableTextViews(false);		
				}
				
				
			    showcaseView.overrideButtonClick(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								showcaseView.hideButton();
								showcaseView.hide();
								activity.setFocusableTextViewsOnTouch(true);
								if  (secretInfoHidden){
									secretViews.setVisibility(View.GONE);
								}
								else {
									secretViews.setVisibility(View.VISIBLE);
								}

							}
						});
				
			}
		});
		
		lockButton = (ImageButton) root.findViewById(R.id.lock_button);
		lockButton.setColorFilter(getResources().getColor(R.color.homanagergold_color));
		lockButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (null!= activity.getShowcaseView() && 
						activity.getShowcaseView().getVisibility() == View.VISIBLE){
					activity.getShowcaseView().hide();
					activity.getShowcaseView().hideButton();
					activity.setFocusableTextViewsOnTouch(true);
				}
				if (null!= showcaseView && showcaseView.getVisibility() == View.VISIBLE){
					activity.setFocusableTextViewsOnTouch(true);
					showcaseView.hide();
					showcaseView.hideButton();
				}
				secretViews.setVisibility(View.GONE);
				isLocked = true;
				activity.getActionBar().hide();
				activity.getPager().setPagingEnabled(false);	
				infoButton.setVisibility(View.GONE);
				v.setVisibility(View.GONE);
				firstNameView.setError(null);
				phoneNumberView.setError(null);
				if (null == lockInstructions){
					lockInstructions = new ShowcaseView.Builder(activity)
					.setTarget(new ViewTarget(R.id.ratings_header, activity))
					.setContentText("Double tap to disable lock mode, once the Ho has returned back your phone.")
					.singleShot(VIEW_ID)
						.build();
				
					int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
					DisplayMetrics metrics = new DisplayMetrics();
					activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
					if (screenSize == Configuration.SCREENLAYOUT_SIZE_NORMAL && metrics.densityDpi == DisplayMetrics.DENSITY_HIGH){
					   
						Button button = (Button) lockInstructions.getChildAt(0);
						android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) button.getLayoutParams();
						params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
						params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
						params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
						params.bottomMargin = metrics.heightPixels/7;
						button.setLayoutParams(params);
											
					}
					if (lockInstructions.getVisibility() == View.VISIBLE){
						activity.hideSoftKeyboard();
						activity.setFocusableTextViews(false);
					}
					
					lockInstructions.overrideButtonClick(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							lockInstructions.hide();
							lockInstructions.hideButton();
							activity.setFocusableTextViewsOnTouch(true);			
						}
					});
					
				}
				
			}
		});
		
		firstNameView = (EditText) root.findViewById(R.id.firstName);
		lastNameView = (EditText) root.findViewById(R.id.lastName);
		phoneNumberView = (EditText) root.findViewById(R.id.telephone_number);
		faceBar = (RatingBar) root.findViewById(R.id.face_rating_bar);
		bodyBar = (RatingBar) root.findViewById(R.id.body_rating_bar);
		personalityBar = (RatingBar) root.findViewById(R.id.personality_rating_bar);
		humorBar = (RatingBar) root.findViewById(R.id.humor_rating_bar);
		bitchyBar = (RatingBar) root.findViewById(R.id.bitch_rating_bar);
		
		fuckableSwitch = (Switch) root.findViewById(R.id.fuckableSwitch);
		fuckableSwitch.setSwitchTextAppearance(activity, R.style.switchTextColor);
		
		friendableSwitch = (Switch) root.findViewById(R.id.friendableswitch);
		friendableSwitch.setSwitchTextAppearance(activity, R.style.switchTextColor);
		
		dateableSwitch = (Switch) root.findViewById(R.id.dateableswitch);
		dateableSwitch.setSwitchTextAppearance(activity, R.style.switchTextColor);
		
		ethnicitySpinner = (Spinner) root.findViewById(R.id.ethnicity_view);
		String [] list = getResources().getStringArray(R.array.ethnicity_spinner_choices);
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(activity, R.layout.spinner_item, 
				list);
		ethnicitySpinner.setAdapter(spinnerAdapter);
		
		secretViews = root.findViewById(R.id.secret_layout);
		if (secretInfoHidden){
			secretViews.setVisibility(View.GONE);
		}
		else {
			secretViews.setVisibility(View.VISIBLE);
		}
		
		scrollview = (ScrollView) root.findViewById(R.id.scrollview);
		scrollview.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mDetector.onTouchEvent(event);
				return false;
			}
		});
	
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == SelfieActivity.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

	    }
	}
	
	/*public boolean isItDiagonalSwipeUpRight(float distanceX, float distanceY){
		return distanceX < 0 && distanceY > 0 && Math.abs(Math.abs(distanceX) - Math.abs(distanceY)) < 10;
	}*/

}
