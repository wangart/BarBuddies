package sosis.AndroidApps.HoManager;

import java.util.ArrayList;

import sosis.AndroidApps.Provider.HoProvider;

import database.HoTable;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;

// Activity where user edits information of a contact
public class EditHoActivity extends Activity {
	public static final String EDITED = "edited";
	public static final String ACTIVITY_TITLE = "   Edit Ho";
	public static final String BANG = "Fuckable";
	public static final String FRIEND = "Friendable";
	public static final String DATE = "Dateable";
	public static final String CANCEL = "Cancel";
	public static final String SAVE = "Save";
	
	private String mOriginalDisplayName;
	private String mOriginalNumber;
	
	// IDs used to access get/set values in DB
	private String mHoID;
	private String mContactHoID;
	private String mContactNumberID;
	private String mContactNameID;
	
	// UI components the user interacts with
	private EditText mFirstNameEditText;
	private EditText mLastNameEditText;
	private EditText mPhoneNumberEditText;
	private RatingBar mFaceRatingBar;
	private RatingBar mBodyRatingBar;
	private RatingBar mPersonalityRatingBar;
	private RatingBar mHumorRatingBar;
	private RatingBar mBitchRatingBar;
	private Switch mFuckableSwitch;
	private Switch mFriendableSwitch;
	private Switch mDateableSwitch;
	private Spinner mEthnicitySpinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_ho_activity);
		
		getActionBar().setTitle(ACTIVITY_TITLE);
		setMemberViews();
		applyIntentToUI(getIntent());		
	}
	
	private void setMemberViews() {
		mFirstNameEditText = (EditText) findViewById(R.id.firstNameEdit);
		mLastNameEditText = (EditText) findViewById(R.id.lastNameEdit);
		mPhoneNumberEditText = (EditText) findViewById(R.id.telephone_numberEdit);
		
		mFaceRatingBar = (RatingBar) findViewById(R.id.face_rating_barEdit);
		mBodyRatingBar = (RatingBar) findViewById(R.id.body_rating_barEdit);
		mPersonalityRatingBar = (RatingBar) findViewById(R.id.personality_rating_barEdit);
		mHumorRatingBar = (RatingBar) findViewById(R.id.humor_rating_barEdit);
		mBitchRatingBar = (RatingBar) findViewById(R.id.bitch_rating_barEdit);
		
		mFuckableSwitch = (Switch) findViewById(R.id.fuckableSwitchEdit);
		mFriendableSwitch = (Switch) findViewById(R.id.friendableswitchEdit);
		mDateableSwitch = (Switch) findViewById(R.id.dateableswitchEdit);
		
		mEthnicitySpinner = (Spinner)findViewById(R.id.ethnicity_viewEdit);
		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
				R.array.ethnicity_spinner_choices, android.R.layout.simple_spinner_item);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mEthnicitySpinner.setAdapter(spinnerAdapter);
	}
	
	// Gets contact information from intent and displays it on UI
	private void applyIntentToUI(Intent intent){
		mContactHoID = intent.getStringExtra(HoTable.COLUMN_CONTACTS_ID);
		mContactNumberID = intent.getStringExtra(HoTable.COLUMN_CONTACTS_NUMBER_ID);
		mContactNameID = intent.getStringExtra(HoTable.COLUMN_CONTACTS_DISPLAY_ID);
		
		mOriginalDisplayName = intent.getStringExtra(HoTable.COLUMN_NAME);
		
		int nameMidpoint = mOriginalDisplayName.indexOf(" ");
		
		mFirstNameEditText.setText(mOriginalDisplayName.substring(0, nameMidpoint));
		mLastNameEditText.setText(mOriginalDisplayName.substring(nameMidpoint+1));
		
		Number id  = intent.getIntExtra(HoTable.COLUMN_ID, 0);
		mHoID = id.toString();
		
		mOriginalNumber = intent.getStringExtra(HoTable.COLUMN_NUMBER);
		mPhoneNumberEditText.setText(mOriginalNumber);
		
		mFaceRatingBar.setRating(intent.getFloatExtra(HoTable.COLUMN_FACE_RATING, 0f));
		mBodyRatingBar.setRating(intent.getFloatExtra(HoTable.COLUMN_BODY_RATING, 0f));
		mPersonalityRatingBar.setRating(intent.getFloatExtra(HoTable.COLUMN_PERSONALITY_RATING, 0f));
		mHumorRatingBar.setRating(intent.getFloatExtra(HoTable.COLUMN_HUMOR_RATING, 0f));
		mBitchRatingBar.setRating(intent.getFloatExtra(HoTable.COLUMN_BITCHINESS_RATING, 0f));
	
		String tags = intent.getStringExtra(HoTable.COLUMN_DATEABLE);
		
		if (tags.contains(BANG)){
			mFuckableSwitch.setChecked(true);
		}
		if (tags.contains(DATE)){
			mDateableSwitch.setChecked(true);
		}
		if (tags.contains(FRIEND)){
			mFriendableSwitch.setChecked(true);
		}
		
		int position = ((ArrayAdapter) mEthnicitySpinner.getAdapter())
				.getPosition(intent.getStringExtra(HoTable.COLUMN_ETHNICITY));
		mEthnicitySpinner.setSelection(position);		
		
	}
	
	@Override
	 public boolean onCreateOptionsMenu (Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.edit_ho_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		String itemTitle = (String) item.getTitle();
		Intent intent = new Intent(this, MainHoActivity.class);
		if (itemTitle.equals(CANCEL)) {
			hideSoftKeyboard();
			startActivity(intent);
		} else if (itemTitle.equals(SAVE)) {
			hideSoftKeyboard();
			if (!isManditoryTextFieldEmpty(mFirstNameEditText) && 
					!isManditoryTextFieldEmpty(mPhoneNumberEditText)) {
								
				// Capitalize first letter of first and last name
				String firstNameString = mFirstNameEditText.getText().toString().trim();
				if (Character.isLowerCase(firstNameString.charAt(0))){
					firstNameString = firstNameString.substring(0,1).toUpperCase() + firstNameString.substring(1);
				}
				
				String lastNameString = mLastNameEditText.getText().toString().trim();
				if (!lastNameString.equals("") && lastNameString.length()>2 && 
						Character.isLowerCase(lastNameString.charAt(0))){
					lastNameString = lastNameString.substring(0,1).toUpperCase() + lastNameString.substring(1);
				}
				
				ContentValues contentValues = new ContentValues();
				String fullNameString = firstNameString + " " + lastNameString;
				contentValues.put(HoTable.COLUMN_NAME, fullNameString);
				
				String phoneNumberString = mPhoneNumberEditText.getText().toString().trim();
				contentValues.put(HoTable.COLUMN_NUMBER, phoneNumberString);
				
				float faceRatingValue = mFaceRatingBar.getRating();
				contentValues.put(HoTable.COLUMN_FACE_RATING, faceRatingValue);
				
				float bodyRatingValue = mBodyRatingBar.getRating();
				contentValues.put(HoTable.COLUMN_BODY_RATING, bodyRatingValue);
				
				float personalityRatingValue = mPersonalityRatingBar.getRating();
				contentValues.put(HoTable.COLUMN_PERSONALITY_RATING, personalityRatingValue);
				
				float humorRatingValue = mHumorRatingBar.getRating();
				contentValues.put(HoTable.COLUMN_HUMOR_RATING, humorRatingValue);
				
				float bitchyRatingValue = mBitchRatingBar.getRating();
				contentValues.put(HoTable.COLUMN_BITCHINESS_RATING, bitchyRatingValue);
				
				boolean isFuckable = mFuckableSwitch.isChecked();
				contentValues.put(HoTable.COLUMN_FUCKABLE, isFuckable ? 1 : 0);
				
				boolean isFriendable = mFriendableSwitch.isChecked();
				contentValues.put(HoTable.COLUMN_FRIENDABLE, isFriendable ? 1 : 0);
				
				boolean isDateable = mDateableSwitch.isChecked();
				contentValues.put(HoTable.COLUMN_DATEABLE, isDateable ? 1 : 0);
				
				int position = mEthnicitySpinner.getSelectedItemPosition();
				String mEthnicitySpinner = position != -1 ? getResources().getStringArray(R.array.ethnicity_spinner_choices)[position] : "";
				contentValues.put(HoTable.COLUMN_ETHNICITY, mEthnicitySpinner);
			
				// Update the App's Contact Info DB
				getContentResolver().
				update(Uri.withAppendedPath(HoProvider.CONTENT_URI, mHoID), contentValues, "", null);
				
				updateExternalContact(fullNameString, phoneNumberString);
				
				intent.putExtra(EDITED, true);
				startActivity(intent);
				}
		}		
		return true;	
	}
	
	// Update the phone's default contact DB
	private void updateExternalContact (String newDisplayName, String newPhoneNumber){
		try {
			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
			if (!newDisplayName.equals(mOriginalDisplayName)) {
				ops.add(ContentProviderOperation.newUpdate(Data.CONTENT_URI)
						.withSelection(Data.RAW_CONTACT_ID + " = ? AND  " + Data._ID + " = ? ", 
								new String [] {mContactHoID, mContactNameID})
								.withValue(StructuredName.DISPLAY_NAME, newDisplayName)
								.build());
			}
			if (!newPhoneNumber.equals(mOriginalNumber)){
				ops.add(ContentProviderOperation.newUpdate(Data.CONTENT_URI)
						.withSelection(Data.RAW_CONTACT_ID + " = ? AND  " + Data._ID + " = ? ", 
								new String [] {mContactHoID, mContactNumberID})
								.withValue(Phone.NUMBER, newPhoneNumber)
								.build());
			}
			getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	private void hideEditTextWindow(int editTextID) {
		EditText editText = (EditText) findViewById(editTextID);
		
		if (null!= editText) {
			InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);					
		}
		
	}
	
	private void hideSoftKeyboard(){
		hideEditTextWindow(R.id.firstNameEdit);
		hideEditTextWindow(R.id.lastNameEdit);
		hideEditTextWindow(R.id.telephone_numberEdit);
	}
	
	private boolean isManditoryTextFieldEmpty(EditText mandatoryTextField){
		boolean isEmpty = (mandatoryTextField.getText().toString().trim()).equals("");
		if (isEmpty){
			mandatoryTextField.setError( "This field is required!" );
		}
		return isEmpty;
	}
}
