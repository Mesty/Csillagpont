package hu.reformatus.csillagpont.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ProgramsFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 5;
    private String tabTitles[] = new String[] { "Kedd", "Szerda", "Csütörtök", "Péntek", "Szombat" };
    private Context context;

    public ProgramsFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return ProgramsPageFragment.newInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
