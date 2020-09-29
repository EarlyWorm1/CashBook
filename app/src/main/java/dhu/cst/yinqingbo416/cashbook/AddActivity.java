package dhu.cst.yinqingbo416.cashbook;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {
    private TabLayout addActivityTabLayout;
    private ViewPager addActivityViewPager;
    private ArrayList<String>TitleList = new ArrayList<>();//标题集合
    private ArrayList<Fragment>ViewList = new ArrayList<>();//视图集合
    private Fragment expendFragment,incomeFragment;//视图
    private MFragmentPagerAdapter adapter;//适配器
    private TextView cancel_textView;//取消textView
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        addActivityTabLayout = findViewById(R.id.addActivity_tabLayout);
        addActivityViewPager = findViewById(R.id.addActivity_viewPager);
        expendFragment = new ExpendFragment();
        incomeFragment = new IncomeFragment();
        ViewList.add(expendFragment);
        ViewList.add(incomeFragment);
        TitleList.add("支出");
        TitleList.add("收入");
        //设置显示模式
        addActivityTabLayout.setTabMode(addActivityTabLayout.MODE_FIXED);
        //设置标题
        addActivityTabLayout.addTab(addActivityTabLayout.newTab().setText(TitleList.get(0)));
        addActivityTabLayout.addTab(addActivityTabLayout.newTab().setText(TitleList.get(1)));
        adapter = new MFragmentPagerAdapter(getSupportFragmentManager(),TitleList,ViewList);
        addActivityViewPager.setAdapter(adapter);
        addActivityTabLayout.setupWithViewPager(addActivityViewPager);
        cancel_textView = findViewById(R.id.addActivity_cancel);
        cancel_textView.setOnClickListener(new CancelOnClick());
    }

    private class CancelOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(AddActivity.this,MainActivity.class));
            finish();
        }
    }
    //按手机Back键返回
    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddActivity.this,MainActivity.class));
        finish();
    }
}
