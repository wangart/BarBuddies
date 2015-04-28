package sosis.AndroidApps.HoManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomepageFragment extends Fragment {
	public static HomepageFragment newInstance(){
		return new HomepageFragment();
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.home_fragment, container, false);

        return rootView;
    }

	/*
	public int getRandomBackgroundColor(){
		Random rnd = new Random(); 
		return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
	}*/
	
}
