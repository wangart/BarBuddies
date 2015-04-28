package sosis.AndroidApps.HoManager;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NavigationView extends LinearLayout {
	private ImageView arrow;
	private TextView navigationTextView;
	
	public static final int LEFT_ARROW = 0;
	public static final int RIGHT_ARROW = 1;


	public NavigationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray a = context.obtainStyledAttributes(attrs,
		        R.styleable.NavigationView, 0, 0);
		int arrowDirection = a.getInteger(R.styleable.NavigationView_arrowDirection, 0);
		
		LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 inflater.inflate(R.layout.navigation_view_layout, this, true);
		 
		arrow = (ImageView) getChildAt(0);
		arrow.setColorFilter(getResources().getColor(R.color.homanagergold_color));
		navigationTextView = (TextView) getChildAt(1);
		 
		if (arrowDirection == LEFT_ARROW){
			arrow.setImageResource(R.drawable.left_arrow);
			navigationTextView.setText("Swipe Left to See Your Hoes");
		}
		 else if (arrowDirection == RIGHT_ARROW){
			 arrow.setImageResource(R.drawable.right_arrow);
		 	 navigationTextView.setText("Swipe Right to Add a Ho"); 
		 }
		
	    /*  if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)){
	    	  LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,60f);
	    	  params.setMargins(0, 10, 0, 0);
	    	  arrow.setLayoutParams(params);
	    	  
	    	  LayoutParams params1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	    	  params1.setMargins(0, 0, 0, 10);
	    	  navigationTextView.setLayoutParams(params1);
	    	  

	    	  
	      }
*/
	}

		 
	}
