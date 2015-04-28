package sosis.AndroidApps.HoManager;

import database.HoTable;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

// Takes care of bridge between contact info DB and UI
public class HoAdapter extends CursorAdapter{
	private static final float EXTERNAL_FACTORS_WEIGHT = 0.66f;
	private static final float INTERNAL_FACTORS_WEIGHT = 0.34f;

	private LayoutInflater mInflater;
	
	public static final float NOT_ENOUGH_INFO = 100f;
	
	public HoAdapter(Context context, Cursor c) {
		super(context, c);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView nameView = (TextView) view.findViewById(R.id.name);
		String name = cursor.getString(cursor.getColumnIndex(HoTable.COLUMN_NAME));
		nameView.setText(name);
		
		String ethnicity = cursor.getString(cursor.getColumnIndex(HoTable.COLUMN_ETHNICITY));
		view.setTag(R.id.ethnicity_focus, ethnicity);
		
		view.setTag(R.id.contact_id, 
				cursor.getString(cursor.getColumnIndex(HoTable.COLUMN_CONTACTS_ID)));
		view.setTag(R.id.phone_contact_id,
				cursor.getString(cursor.getColumnIndex(HoTable.COLUMN_CONTACTS_NUMBER_ID)));
		view.setTag(R.id.name_contact_id, 
				cursor.getString(cursor.getColumnIndex(HoTable.COLUMN_CONTACTS_DISPLAY_ID)));
		
		view.setTag(R.id.telephone_numberEdit, 
				cursor.getString(cursor.getColumnIndex(HoTable.COLUMN_NUMBER))); 
		
		ImageButton hoPictureButton = (ImageButton) view.findViewById(R.id.picture_button);
		hoPictureButton.setColorFilter(context.getResources().getColor(R.color.homanagergold_color));
		// TODO: set onclick listener for this guy
		
		/* ---Code for Text button and starting Texting App
		ImageButton textButton = (ImageButton) view.findViewById(R.id.text_button);
		textButton.setTag(cursor.getString(cursor.getColumnIndex(HoTable.COLUMN_NUMBER)));
		textButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent textingIntent = new Intent(Intent.ACTION_SENDTO);
				textingIntent.setData(Uri.parse("smsto:" + Uri.encode((String) v.getTag())));
				v.getContext().startActivity(textingIntent);
				}
		});*/
		
		
		/* --- Code for Phone button and starting calling App
		ImageButton callButton = (ImageButton) view.findViewById(R.id.call_button);
		callButton.setTag(cursor.getString(cursor.getColumnIndex(HoTable.COLUMN_NUMBER)));
		callButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent callingIntent = new Intent(Intent.ACTION_CALL);
				callingIntent.setData(Uri.parse("tel:" + Uri.encode((String) v.getTag())));
				v.getContext().startActivity(callingIntent);
				}
		}); 
		*/

		float faceRating = cursor.getFloat(cursor.getColumnIndex(HoTable.COLUMN_FACE_RATING));
		view.setTag(R.id.face_rating_focus, faceRating);
		
		float bodyRating = cursor.getFloat(cursor.getColumnIndex(HoTable.COLUMN_BODY_RATING));
		view.setTag(R.id.body_rating_indicator, bodyRating);
		
		float bitchyRating = cursor.getFloat(cursor.getColumnIndex(HoTable.COLUMN_BITCHINESS_RATING));
		view.setTag(R.id.bitch_rating_indicator, bitchyRating);
		
		float personalityRating = cursor.getFloat(cursor.getColumnIndex(HoTable.COLUMN_PERSONALITY_RATING));
		view.setTag(R.id.personality_rating_indicator, personalityRating);
		
		float humorRating = cursor.getFloat(cursor.getColumnIndex(HoTable.COLUMN_HUMOR_RATING));
		view.setTag(R.id.humor_rating_indicator, humorRating);
		
		String tags = "This Ho is: ";
		
		boolean isFuckable = cursor.getInt(cursor.getColumnIndex(HoTable.COLUMN_FUCKABLE)) == 1 ? true : false;
		if (isFuckable){
			tags+="Fuckable, ";
		}
		boolean isFriendable =  cursor.getInt(cursor.getColumnIndex(HoTable.COLUMN_FRIENDABLE)) == 1 ? true : false;
		if (isFriendable){
			tags+="Friendable, ";
		}
		boolean isDateable =  cursor.getInt(cursor.getColumnIndex(HoTable.COLUMN_DATEABLE)) == 1 ? true : false;
		if (isDateable){
			tags+="Dateable, ";
		}
		
		if (!isFuckable && !isFriendable && !isDateable){
			tags+= " Whack";
		}
		
		tags = tags.trim();
		
		if (tags.charAt(tags.length()-1) == ','){
			tags = tags.substring(0, tags.length()-1);
		}
		
		view.setTag(R.id.tagView, tags);
		
		float displayRating = calculateStarRating(faceRating, bodyRating, bitchyRating, personalityRating, humorRating);
		
		RatingBar averageRating = (RatingBar) view.findViewById(R.id.averageRating);
		TextView emptyText = (TextView) view.findViewById(R.id.noRatingText);
		
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) nameView.getLayoutParams();		
		if (NOT_ENOUGH_INFO != displayRating) {
			averageRating.setRating(displayRating);
			if (isEmptyTextShowing(emptyText, averageRating)){
				emptyText.setVisibility(View.GONE);
				averageRating.setVisibility(View.VISIBLE);
				
				params.setMargins(5, 10, 0, 0);
			}
		} else if (!isEmptyTextShowing(emptyText, averageRating)) {
			averageRating.setVisibility(View.GONE);
			emptyText.setVisibility(View.VISIBLE);
			params.setMargins(80, 35, 0, 5);
		}
		nameView.setLayoutParams(params);
	}

	@Override
	public View newView(Context context, Cursor c, ViewGroup parent) {
		return mInflater.inflate(R.layout.ho_row,parent,false);
	}
	
	// Used to calculate start display for each contact in list
	private float calculateStarRating(float faceRating, float bodyRating, float bitchyRating, float personalityRating,
			float humorRating) {
		float externalRating;
		if (0 == faceRating && 0 == bodyRating) {
			return NOT_ENOUGH_INFO;
		} else if (0 == faceRating) {
			externalRating = bodyRating;
		} else if (0 == bodyRating) {
			externalRating = faceRating;
		} else {
			externalRating = (faceRating + bodyRating)/2;
		}
		
		float internalRating;
		
		if (0 == personalityRating && 0 == humorRating) {
			internalRating = 0f;
		} else if (0 == personalityRating) {
			internalRating = humorRating;
		} else if (0 == humorRating) {
			internalRating = personalityRating;
		} else {
			internalRating = ( personalityRating + humorRating)/ 2;
		}

		float finalRating;
		
		if (0 == internalRating) {
			finalRating = (externalRating * 0.83f);
		} else {
			finalRating = (externalRating * EXTERNAL_FACTORS_WEIGHT) + (internalRating * INTERNAL_FACTORS_WEIGHT);
		} if (bitchyRating>= 2f && bitchyRating <= 4f){
			finalRating +=0.15f;
		}
		else if (bitchyRating == 5f) {
			finalRating += 0.25f;
		}
		return finalRating;
	}
	
	private boolean isEmptyTextShowing(View textView, View ratingBar){
		if (textView.getVisibility() == View.VISIBLE 
				&& ratingBar.getVisibility() == View.GONE) {
			return true;
		} else if (textView.getVisibility() == View.GONE 
				&& ratingBar.getVisibility() == View.VISIBLE){
			return false; 
		} else {
			return false;
		}
	}

}
