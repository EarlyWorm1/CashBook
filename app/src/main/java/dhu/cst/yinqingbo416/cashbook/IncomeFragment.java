package dhu.cst.yinqingbo416.cashbook;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class IncomeFragment extends Fragment implements View.OnClickListener {
    private List<String> titles = Arrays.asList("工资","兼职","理财","其它","设置");
    private ViewPager mViewPager;
    private List<View>mPagerList;
    private List<Kind>mDatas;
    private LinearLayout mDot;
    private LayoutInflater mInflater;
    private GridViewAdapter adapter;
    private int pageNum;//总的页数
    private int pageSize = 10;//每页的个数
    private int curIndex = 0;//当前第几页
    private ImageView curImageView;//当前点击的图标
    private boolean sign = true;//标志是否已设置第一次点击的对象
    private int curPos = 0;//记录当前点击的位置
    private List<Integer>ImageResuorce1;//储存点击的图片资源
    private TextView input_textView;//金额输入框
    private TextView kind_textView;//类别输入框
    private ImageView kind_imageView;//类别图标
    //数字键盘
    private Button btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9;
    private Button btn_del,btn_back,btn_sure,btn_clean,btn_point;
    private Button date,remark;
    private boolean ispress = false;//表示键盘是否输入过
    private boolean ispoint = false;//表示未输入小数点
    double ret;//输入框数字大小
    String str;//输入框文本
    //日期选择对话框
    Calendar calendar = Calendar.getInstance();
    int mYear = calendar.get(Calendar.YEAR);
    int mMonth = calendar.get(Calendar.MONTH);
    int mDay = calendar.get(Calendar.DAY_OF_MONTH);
    //添加备注对话框
    LinearLayout remark_layout;
    String remark_string;//备注内容
    myAlertDialog dialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.expend_fragment,container,false);
        mViewPager = view.findViewById(R.id.expend_viewPager);
        mDot = view.findViewById(R.id.expend_dot);
        ImageResuorce1 = new ArrayList<>();
        input_textView = view.findViewById(R.id.expend_kind_input);
        kind_textView = view.findViewById(R.id.expend_kind_textView);
        kind_imageView = view.findViewById(R.id.expend_kind_imageView);
        kind_textView.setText("工资");
        kind_imageView.setImageResource(R.mipmap.income_kind_icon01);
        btn0 = view.findViewById(R.id.keyboard_btn0);
        btn1 = view.findViewById(R.id.keyboard_btn1);
        btn2 = view.findViewById(R.id.keyboard_btn2);
        btn3 = view.findViewById(R.id.keyboard_btn3);
        btn4 = view.findViewById(R.id.keyboard_btn4);
        btn5 = view.findViewById(R.id.keyboard_btn5);
        btn6 = view.findViewById(R.id.keyboard_btn6);
        btn7 = view.findViewById(R.id.keyboard_btn7);
        btn8 = view.findViewById(R.id.keyboard_btn8);
        btn9 = view.findViewById(R.id.keyboard_btn9);
        btn_point = view.findViewById(R.id.keyboard_btn_point);
        btn_back = view.findViewById(R.id.keyboard_btn_back);
        btn_clean = view.findViewById(R.id.keyboard_btn_clean);
        btn_del = view.findViewById(R.id.keyboard_btn_del);
        btn_sure = view.findViewById(R.id.keyboard_btn_sure);
        date = view.findViewById(R.id.keyboard_date);
        remark = view.findViewById(R.id.keyboard_remark);
        dialog = new myAlertDialog(getContext());
        //获取资源
        mDatas = new ArrayList<Kind>();
        for(int i = 0;i < titles.size();i++){
            String str0 = "income_kind_icon"+i;
            String str1 = "income_kind_icon"+i;
            str0 += "0";
            str1 += "1";
            int imageId0 = getResources().getIdentifier(str0,"mipmap",getContext().getPackageName());
            int imageId1 = getResources().getIdentifier(str1,"mipmap",getContext().getPackageName());
            mDatas.add(new Kind(titles.get(i),imageId0));
            ImageResuorce1.add(imageId1);
        }
        mInflater = LayoutInflater.from(getContext());
        pageNum = (int)Math.ceil(mDatas.size()*1.0/pageSize);
        mPagerList = new ArrayList<View>();
        for(int i = 0;i < pageNum;i++){
            GridView gridView = (GridView)mInflater.inflate(R.layout.gridview,mViewPager,false);
            adapter = new GridViewAdapter(getContext(),mDatas,i,pageSize);
            gridView.setAdapter(adapter);
            mPagerList.add(gridView);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int pos = position + curIndex * pageSize;
                    if(sign){//是第一次点击
                        curImageView = view.findViewById(R.id.gridview_imageView);
                        curImageView.setImageResource(ImageResuorce1.get(pos));
                        sign = false;
                        curPos = pos;
                    }
                    else {//不是第一次点击
                        if(curPos != pos){
                            curImageView.setImageResource(mDatas.get(curPos).iconRes);
                            curImageView = view .findViewById(R.id.gridview_imageView);
                            curImageView.setImageResource(ImageResuorce1.get(pos));
                            curPos = pos;
                        }
                    }
                    kind_imageView.setImageResource(ImageResuorce1.get(pos));
                    kind_textView.setText(mDatas.get(pos).getName());
                }
            });
        }
        //设置适配器
        mViewPager.setAdapter(new ViewPagerAdapter(mPagerList));
        //设置圆点
        for(int i = 0;i < pageNum;i++){
            mDot.addView(mInflater.inflate(R.layout.dot,null));
        }
        //默认显示第一页
        mDot.getChildAt(0).findViewById(R.id.view_dot).setBackgroundResource(R.drawable.dot_selected);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mDot.getChildAt(curIndex).findViewById(R.id.view_dot).setBackgroundResource(R.drawable.dot_noselected);
                mDot.getChildAt(position).findViewById(R.id.view_dot).setBackgroundResource(R.drawable.dot_selected);
                curIndex = position;
            }
            @Override
            public void onPageSelected(int position) {}
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        //数字键盘部分
        keyBoard();
        return view;
    }
    private void keyBoard(){
        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn_point.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        btn_clean.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        remark.setOnClickListener(this);
        date.setOnClickListener(this);
        String s = (String.valueOf(mYear)+"年"+String.valueOf(mMonth+1)+"月"+String.valueOf(mDay)+"日");
        date.setText(s);
        remark_string = "";
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onClick(View v) {
        str = input_textView.getText().toString();
        if(!ispress){ str=""; }
        switch (v.getId()){
            case R.id.keyboard_btn0:
                if(!PointNum())
                    break;
                str += "0";
                if(!ispoint)
                    Dispose();
                else
                    input_textView.setText(str);
                break;
            case R.id.keyboard_btn1:
                if(!PointNum())
                    break;
                str += "1";
                Dispose();
                break;
            case R.id.keyboard_btn2:
                if(!PointNum())
                    break;
                str += "2";
                Dispose();
                break;
            case R.id.keyboard_btn3:
                if(!PointNum())
                    break;
                str += "3";
                Dispose();
                break;
            case R.id.keyboard_btn4:
                if(!PointNum())
                    break;
                str += "4";
                Dispose();
                break;
            case R.id.keyboard_btn5:
                if(!PointNum())
                    break;
                str += "5";
                Dispose();
                break;
            case R.id.keyboard_btn6:
                if(!PointNum())
                    break;
                str += "6";
                Dispose();
                break;
            case R.id.keyboard_btn7:
                if(!PointNum())
                    break;
                str += "7";
                Dispose();
                break;
            case R.id.keyboard_btn8:
                if(!PointNum())
                    break;
                str += "8";
                Dispose();
                break;
            case R.id.keyboard_btn9:
                if(!PointNum())
                    break;
                str += "9";
                Dispose();
                break;
            case R.id.keyboard_btn_point:
                if(ispoint)
                    break;
                if(!ispress){
                    input_textView.setText("0.");
                }else {
                    str += ".";
                    input_textView.setText(str);
                }
                ispress = true;
                ispoint = true;
                break;
            case R.id.keyboard_btn_back:
                startActivity(new Intent(getActivity(),MainActivity.class));
                getActivity().finish();
                break;
            case R.id.keyboard_btn_clean:
                input_textView.setText("0.00");
                ispress = false;
                ispoint = false;
                break;
            case R.id.keyboard_btn_del:
                if(str.length() <= 1){
                    input_textView.setText("0.00");
                    ispress = false;
                    ispoint = false;
                    break;
                }
                else{
                    if(str.substring(str.length()-1,str.length()).equals(".")){
                        ispoint = false;
                    }
                    str = str.substring(0,str.length()-1);
                    input_textView.setText(str);
                }
                break;
            case R.id.keyboard_btn_sure:
                String s = input_textView.getText().toString();
                double money = Double.valueOf(s);
                if(money == 0){
                    dialog.show("金额不能为0");
                    break;
                }
                Bill bill = new Bill();
                bill.setName(kind_textView.getText().toString());
                bill.setKind(1);
                bill.setMoney(money);
                bill.setImageId(ImageResuorce1.get(curPos));
                bill.setYear(mYear);
                bill.setMonth(mMonth);
                bill.setDay(mDay);
                bill.setRemark(remark_string);
                bill.save();
                //标记确认键已经点击
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE).edit();
                editor.putBoolean("sign",true);
                editor.apply();
                startActivity(new Intent(getActivity(),MainActivity.class));
                getActivity().finish();
                break;
            case R.id.keyboard_remark:
                remark_layout = (LinearLayout) getLayoutInflater().inflate(R.layout.remark,null);
                final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("添加备注").setView(remark_layout);
                dialog.setPositiveButton("确定",new IncomeFragment.remark_sure());
                dialog.setNegativeButton("语音输入",new IncomeFragment.remark_cancle());
                final AlertDialog alertDialog = dialog.create();
                final EditText txt1 = remark_layout.findViewById(R.id.remark_input);
                if(!remark_string.equals("")){
                    txt1.setText(remark_string);
                    txt1.setSelection(txt1.length());
                }
                //自动打开键盘
                txt1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus){
                            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        }
                    }
                });
                alertDialog.show();
                break;
            case R.id.keyboard_date:
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month;
                        mDay = dayOfMonth;
                        String s1 = (String.valueOf(mYear)+"年"+String.valueOf(mMonth+1)+"月"+String.valueOf(mDay)+"日");
                        date.setText(s1);
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),R.style.ThemeDialog,dateSetListener,mYear,mMonth,mDay);
                datePickerDialog.show();
                break;
        }
    }
    //处理数字0-9
    private void Dispose(){
        ret = Double.valueOf(str);
        if(ret > 1000000){
            str = str.substring(0,str.length()-1);
            ret = Double.valueOf(str);
            dialog.show("土豪，一次最多能记一百万");
        }
        str = String.valueOf(ret);
        Remove0();
        input_textView.setText(str);
        ispress = true;
    }
    //消除后置无意义的0
    private void Remove0(){
        if(str.indexOf(".") > 0){
            //正则表达式
            str = str.replaceAll("0+?$","");
            str = str.replaceAll("[.]$","");
        }
    }
    //对小数点位数限制
    private boolean PointNum(){
        if(ispoint){
            int index = str.indexOf(".");
            int l = str.length() - index - 1;
            if(l >= 2){
                dialog.show("最小记到“分”");
                return false;
            }
        }
        return true;
    }

    private class remark_sure implements DialogInterface.OnClickListener {
        EditText txt;
        @Override
        public void onClick(DialogInterface dialog, int which) {
            txt = remark_layout.findViewById(R.id.remark_input);
            remark_string = txt.getText().toString();
            if(!remark_string.equals("")){
                if(remark_string.length()>5){
                    String s2 = remark_string.substring(0,5);
                    s2 += "...";
                    remark.setText(s2);
                }else{
                    remark.setText(remark_string);
                }
            }else{
                remark.setText("添加备注...");
            }
        }
    }

    private class remark_cancle implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            SpeechUtility.createUtility(getContext(), SpeechConstant.APPID + "=564499f1");//语音
            remark_string = "";
            btnVoice();
        }
    }
    //TODO 开始说话：
    private void btnVoice() {
        RecognizerDialog dialog = new RecognizerDialog(getContext(),null);
        dialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        dialog.setParameter(SpeechConstant.ACCENT, "mandarin");

        dialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                printResult(recognizerResult);
            }
            @Override
            public void onError(SpeechError speechError) {
            }
        });
        dialog.show();
        Toast.makeText(getContext(), "请开始说话", Toast.LENGTH_SHORT).show();
    }

    //回调结果：
    private void printResult(RecognizerResult results) {
        String text = parseIatResult(results.getResultString());
        remark_string += text;
        if(!remark_string.equals("")){
            if(remark_string.length()>5){
                String s2 = remark_string.substring(0,5);
                s2 += "...";
                remark.setText(s2);
            }else{
                remark.setText(remark_string);
            }
        }else {
            remark.setText("添加备注...");
        }
    }

    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

}
