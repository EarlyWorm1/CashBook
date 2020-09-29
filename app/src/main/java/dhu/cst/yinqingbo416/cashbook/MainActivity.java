package dhu.cst.yinqingbo416.cashbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private Toolbar toolbar;
    private myAlertDialog dialog;
    private ArrayList<ArrayList<String>> recordList;
    private List<Bill> bills;
    private static String[] title = { "编号","类别","名称","金额","图标id","年","月","日","备注" };
    private File file;
    private String fileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        bills = DataSupport.findAll(Bill.class);
    }
    //初始化函数
    private void Init(){
        //创建数据库
        LitePal.initialize(this);
        //初始化标题栏
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("没钱记账");
        setSupportActionBar(toolbar);
        //初始化底部导航栏
        radioGroup = findViewById(R.id.bottomBar);
        dialog = new myAlertDialog(MainActivity.this);
        //默认显示第一个fragment
        replacefragment(new Fragment1());
        //底部标题栏绑定响应事件
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.bottom1:
                        replacefragment(new Fragment1());
                        break;
                    case R.id.bottom2:
                        replacefragment(new Fragment2());
                        break;
                    case R.id.bottom3:
                        replacefragment(new Fragment3());
                        break;
                    case R.id.bottom4:
                        replacefragment(new Fragment4());
                        break;
                    default:
                        break;
                }
            }
        });
        //记账图标绑定响应事件
        findViewById(R.id.sign_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到记账页面
                Intent intent6 = new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent6);
                finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }
    //标题菜单项
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_btn:
                dialog.show();
                break;
            case R.id.clould_btn:
                exportExcel();
                break;
            default:
                break;
        }
        return true;
    }
    //替换frangment函数
    private void replacefragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_content,fragment);
        transaction.commit();
    }
    //导出excel
    public void exportExcel() {
        file = new File(getSDPath() + "/Record");
        makeDir(file);
        ExcelUtils.initExcel(file.toString() + "/账单.xls", title);
        fileName = getSDPath() + "/Record/账单.xls";
        ExcelUtils.writeObjListToExcel(getRecordData(), fileName, this);
    }

    private ArrayList<ArrayList<String>> getRecordData() {
        recordList = new ArrayList<>();
        for (int i = 0; i <bills.size(); i++) {
            Bill bill = bills.get(i);
            ArrayList<String> beanList = new ArrayList<String>();
            beanList.add(bill.getId()+"");
            beanList.add(bill.getKind()+"");
            beanList.add(bill.getName());
            beanList.add(bill.getMoney()+"");
            beanList.add(bill.getImageId()+"");
            beanList.add(bill.getYear()+"年");
            beanList.add((bill.getMonth()+1)+"月");
            beanList.add(bill.getDay()+"天");
            beanList.add(bill.getRemark());
            recordList.add(beanList);
        }
        return recordList;
    }

    private void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }

    private String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        String dir = sdDir.toString();
        return dir;
    }
}
