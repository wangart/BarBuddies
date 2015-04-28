package sosis.AndroidApps.HoManager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HoFragmentAdapter extends FragmentPagerAdapter{
	private static final int FRAGMENT_COUNT = 3;
	
	private static final int ADD_HO_FRAGMENT_INDEX = 0;
	private static final int HOMEPAGE_FRAGMENT_INDEX = 1;
	private static final int LIST_FRAGMENT_INDEX = 2;
	private FragmentManager fm;
	
	private static HoFormFragment formFragment;
	public HoFragmentAdapter(FragmentManager fm) {
		super(fm);
		
		this.fm = fm;
	}

	@Override
	public Fragment getItem(int pageIndex) {
		switch (pageIndex){
		case ADD_HO_FRAGMENT_INDEX:
			formFragment = HoFormFragment.newInstance();
			return formFragment;
		case HOMEPAGE_FRAGMENT_INDEX:
			return HomepageFragment.newInstance();
		case LIST_FRAGMENT_INDEX:
			return HoListFragment.newInstance();
		}
		return null;
	}
	
	public HoFormFragment getFormFragment(){
		return formFragment;
	}

	@Override
	public int getCount() {
		return FRAGMENT_COUNT;
	}
}
