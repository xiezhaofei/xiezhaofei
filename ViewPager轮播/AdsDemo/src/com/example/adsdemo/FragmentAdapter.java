package com.example.adsdemo;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {

	List<Fragment> fragments;

	/**
	 * Fragment实际只有Zero、One、Two这三张
	 */
	public FragmentAdapter(FragmentManager fm) {
		super(fm);
		fragments = new ArrayList<Fragment>();
		/*fragments.add(new FragmentTwo());
		fragments.add(new FragmentZero());
		fragments.add(new FragmentOne());
		fragments.add(new FragmentTwo());
		fragments.add(new FragmentZero());*/
		Fragment f1 = new FragmentOne();
		fragments.add(new FragmentOne());
		fragments.add(new FragmentZero());
		fragments.add(new FragmentOne());
		fragments.add(new FragmentZero());
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
}