package sosis.AndroidApps.HoManager;

import java.util.ArrayList;

import sosis.AndroidApps.Provider.HoProvider;

import database.HoTable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


public class HoListFragment extends ListFragment{
	
	private AlertDialog hoInfoDialog;
	private TextView hoCount;
	
	private Cursor cursor;
	private ListAdapter adapter;
	
	
	public static HoListFragment newInstance(){
		return new HoListFragment();
	}
	

	@Override 
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		cursor = getActivity().getContentResolver().query
				(HoProvider.CONTENT_URI, new String[]{HoTable.COLUMN_ID, HoTable.COLUMN_CONTACTS_DISPLAY_ID, HoTable.COLUMN_CONTACTS_ID,
						HoTable.COLUMN_CONTACTS_NUMBER_ID, HoTable.COLUMN_NAME,HoTable.COLUMN_NUMBER,HoTable.COLUMN_BITCHINESS_RATING,
						HoTable.COLUMN_BODY_RATING,HoTable.COLUMN_ETHNICITY,HoTable.COLUMN_FACE_RATING,HoTable.COLUMN_DATEABLE,
						HoTable.COLUMN_FRIENDABLE,HoTable.COLUMN_FUCKABLE,HoTable.COLUMN_HUMOR_RATING,HoTable.COLUMN_PERSONALITY_RATING},
						null,  null, HoTable.COLUMN_NAME + " ASC" );
		
		adapter = new HoAdapter(getActivity(), cursor);
		setListAdapter(adapter);
		
	}
	
	@Override
	public void onResume(){
		super.onResume();

		Intent startingActivityIntent = ((MainHoActivity) getActivity()).getLastIntent();
		if (null!=startingActivityIntent){
			if (startingActivityIntent.hasExtra(EditHoActivity.EDITED) && null!= hoInfoDialog 
					&& hoInfoDialog.isShowing()){
				hoInfoDialog.dismiss();
				Toast.makeText(getActivity(), "Ho Info Updated!", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		
		View view = inflater.inflate(R.layout.ho_list_fragment, container, false);
		
		hoCount = (TextView) view.findViewById(R.id.ho_count_view);
		
		return view;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);
		View cardView = createFrontOfCard(view, id);
		hoInfoDialog = showHoCard(cardView);	
	}

	private View createFrontOfCard(View view, long id){
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.ho_focus_layout, null);
		
		ImageButton hoPictureButton = (ImageButton) dialogView.findViewById(R.id.ho_focus_picture);
		hoPictureButton.setColorFilter(getResources().getColor(R.color.homanagergold_color));
		// TODO: Set onclick listener
		
		final String number = (String) view.getTag(R.id.telephone_numberEdit);
		
		TextView ethnicity = (TextView) dialogView.findViewById(R.id.ethnicity_focus);
		final String ethnicityText = (String)view.getTag(R.id.ethnicity_focus);
		ethnicity.setText(ethnicityText);
		
		RatingBar face = (RatingBar) dialogView.findViewById(R.id.face_rating_focus);
		final Number faceRating = (Number)view.getTag(R.id.face_rating_focus);
		face.setRating(faceRating.floatValue());
		
		RatingBar body = (RatingBar) dialogView.findViewById(R.id.body_rating_indicator);
		final Number bodyRating = (Number) view.getTag(R.id.body_rating_indicator);
		body.setRating(bodyRating.floatValue());
		
		RatingBar personality = (RatingBar) dialogView.findViewById(R.id.personality_rating_indicator);
		final Number personalityRating = (Number) view.getTag(R.id.personality_rating_indicator);
		personality.setRating(personalityRating.floatValue());
		
		RatingBar humor = (RatingBar) dialogView.findViewById(R.id.humor_rating_indicator);
		final Number humorRating = (Number) view.getTag(R.id.humor_rating_indicator);
		humor.setRating(humorRating.floatValue());
		
		RatingBar bitch = (RatingBar) dialogView.findViewById(R.id.bitch_rating_indicator);
		final Number bitchyRating = (Number) view.getTag(R.id.bitch_rating_indicator);
		bitch.setRating(bitchyRating.floatValue());
		
		TextView tags = (TextView) dialogView.findViewById(R.id.tagView);
		final String tagString = (String) view.getTag(R.id.tagView);
		tags.setText(tagString);
		
		final CharSequence fullName = ((TextView) view.findViewById(R.id.name)).getText();

		setCardName(fullName, dialogView);
		setCardBackground(view, dialogView);
		
		ImageButton editButton = (ImageButton) dialogView.findViewById(R.id.edit_ho_button);
		editButton.setColorFilter(getResources().getColor(R.color.homanagergold_color));
		
		final String contactID = (String) view.getTag(R.id.contact_id);
		final String phoneID = (String) view.getTag(R.id.phone_contact_id);
		final String nameID = (String) view.getTag(R.id.name_contact_id);
		
		final int idText = (int) id;
		
		editButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View editButton) {
				Intent startEditActivity = new Intent(getActivity(), EditHoActivity.class);
				startEditActivity.putExtra(HoTable.COLUMN_CONTACTS_ID, contactID);
				startEditActivity.putExtra(HoTable.COLUMN_CONTACTS_DISPLAY_ID, nameID);
				startEditActivity.putExtra(HoTable.COLUMN_CONTACTS_NUMBER_ID, phoneID);
				startEditActivity.putExtra(HoTable.COLUMN_NAME, fullName);
				startEditActivity.putExtra(HoTable.COLUMN_ID, idText);
				startEditActivity.putExtra(HoTable.COLUMN_NUMBER, number );
				startEditActivity.putExtra(HoTable.COLUMN_ETHNICITY, ethnicityText);
				startEditActivity.putExtra(HoTable.COLUMN_FACE_RATING, faceRating);
				startEditActivity.putExtra(HoTable.COLUMN_BODY_RATING, bodyRating);
				startEditActivity.putExtra(HoTable.COLUMN_PERSONALITY_RATING, personalityRating);
				startEditActivity.putExtra(HoTable.COLUMN_HUMOR_RATING, humorRating);
				startEditActivity.putExtra(HoTable.COLUMN_BITCHINESS_RATING, bitchyRating);
				startEditActivity.putExtra(HoTable.COLUMN_DATEABLE, tagString);
				startActivity(startEditActivity);
			}
			
		});
		
		ImageButton deleteButton = (ImageButton) dialogView.findViewById(R.id.remove_ho_button);
		deleteButton.setColorFilter(getResources().getColor(R.color.homanagergold_color));
		deleteButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog confirmationDialog = new AlertDialog.Builder(getActivity()).create();
				confirmationDialog.setMessage("Are you sure you want to delete this Ho?");
				confirmationDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						deleteExternalContact(contactID,nameID,phoneID);
						Uri uri = Uri.withAppendedPath(HoProvider.CONTENT_URI, String.valueOf(idText));
						getActivity().getContentResolver().delete(uri, null, null);
						dialog.dismiss();
						hoInfoDialog.dismiss();
						updateHoCount();
						Toast.makeText(getActivity(), "That Ho is Officially CUT!", Toast.LENGTH_LONG).show();
					}
				});
				
				confirmationDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				confirmationDialog.show();
			}
		});

		return dialogView;
		
	}
	
	private static void setCardName(CharSequence fullName, View cardView){
		TextView nameView = (TextView) cardView.findViewById(R.id.name_text);
		nameView.setText(fullName);
	}
	
	private static void setCardBackground(View selectedView, View cardView){
		cardView.setBackgroundResource(R.drawable.card_background_default);
		View noRatingTextView = selectedView.findViewById(R.id.noRatingText);
		if (null != noRatingTextView && !noRatingTextView.isShown() ){
			RatingBar averageRatingBar = (RatingBar) selectedView.findViewById(R.id.averageRating);
			if (null !=averageRatingBar && averageRatingBar.isShown()){
				float rating = averageRatingBar.getRating();
				if (rating>0 && rating<=1.99){
					cardView.setBackgroundResource(R.drawable.card_background_bronze);
				}
				else if (rating>1.99 && rating<=3.99){
					cardView.setBackgroundResource(R.drawable.card_background_silver);
				}
				else if (rating>3.99 && rating<=5){
					cardView.setBackgroundResource(R.drawable.card_background_gold);
				}
				
			}
			
		}
		
		
		
		
	}
	private void deleteExternalContact(String contactID, String nameID, String phoneID){
		try {
			Cursor c = getActivity().getContentResolver().query(RawContacts.CONTENT_URI, new String[]{RawContacts.CONTACT_ID},
					RawContacts._ID + " = ? ", new String[] {contactID}, null);
			if (c.moveToFirst()){
				String id = c.getString(c.getColumnIndex(RawContacts.CONTACT_ID));
	            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
	            
	            ops.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
	            		.withSelection(Data.RAW_CONTACT_ID + " = ? AND  " + Data._ID + " = ? ", new String[]{contactID,nameID})
	            		.build());
	            
	            ops.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
	            		.withSelection(Data.RAW_CONTACT_ID + " = ? AND  " + Data._ID + " = ? ", new String[]{contactID,phoneID})
	            		.build());
	            
	            ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
	            		.withSelection(RawContacts._ID + " = ? AND  " + RawContacts.CONTACT_ID + " = ? ", new String[]{contactID,id})
	            		.build());
	            
	            ops.add(ContentProviderOperation.newDelete(Contacts.CONTENT_URI)
	            		.withSelection(Contacts._ID + " = ? " , new String[]{id})
	            		.build());
	           getActivity().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
			}

			
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	private void updateHoCount(){
		String text = (String) hoCount.getText();
		String count = text.substring(text.lastIndexOf(":")+1).trim();
		int countInt = Integer.parseInt(count);
		if (countInt > 1){
			hoCount.setText("Ho Count: " + (countInt-1));
		}
	}

	private AlertDialog showHoCard (View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		hoInfoDialog = builder.create();
		hoInfoDialog.setView(view);
		hoInfoDialog.show();
		return hoInfoDialog;
		
	}
}
