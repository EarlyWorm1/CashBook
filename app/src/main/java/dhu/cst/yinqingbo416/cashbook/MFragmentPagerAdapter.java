package dhu.cst.yinqingbo416.cashbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<String>TitleList = new ArrayList<>();
    private ArrayList<Fragment>FragmentList = new ArrayList<>();
    public MFragmentPagerAdapter(FragmentManager fragmentManager,ArrayList<String>TitleList,ArrayList<Fragment>FragmentList){
        super(fragmentManager);
        this.TitleList = TitleList;
        this.FragmentList = FragmentList;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return FragmentList.get(position);
    }

    @Override
    public int getCount() {
        return TitleList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TitleList.get(position);
    }
}
