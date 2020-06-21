package com.example.arrangeme.menu.myprofile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> fragmentList= new ArrayList<>();
    private final List<String> FragmentListTitles = new ArrayList<>();

    /**
     * @param fm
     */
    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    /**
     * @param position
     * @return
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    /**
     * @param object
     * @return
     */
    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    /**
     * @return
     */
    @Override
    public int getCount() {
        return FragmentListTitles.size();
    }

    /**
     * @param position
     * @return
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return FragmentListTitles.get(position);
    }

    /**
     * @param fragment
     * @param title
     */
    public void AddFragment(Fragment fragment, String title){
        fragmentList.add(fragment);
        FragmentListTitles.add(title);
    }

}
