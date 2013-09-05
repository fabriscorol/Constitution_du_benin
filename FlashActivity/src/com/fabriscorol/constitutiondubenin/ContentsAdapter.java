package com.fabriscorol.constitutiondubenin;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ContentsAdapter extends FragmentStatePagerAdapter {
	private BookContents contents = null;
	
	public ContentsAdapter (SherlockFragmentActivity ctxt, BookContents contents) {
		super(ctxt.getSupportFragmentManager());
		this.contents = contents;
	}

	@Override
	public Fragment getItem(int position) {
		return (SimpleContentFragment.newInstance(
				contents.getChapterFile(position)));
	}

	@Override
	public int getCount() {
		return (contents.getChapterCount());
	}

}
