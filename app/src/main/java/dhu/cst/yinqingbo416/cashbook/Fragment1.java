package dhu.cst.yinqingbo416.cashbook;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.List;

public class Fragment1 extends Fragment implements View.OnClickListener {
    TextView hintTextView;
    List<Bill>bills;
    TextView textYear;
    TextView textMonth;
    TextView textExpend1;
    TextView textExpend2;
    TextView textIncome1;
    TextView textIncome2;
    TextView textMonthText;
    ImageView trangle;
    int mYear;
    int mMonth;
    int mDay;
    BillAdapter adapter;
    RecyclerView recyclerView;
    boolean sign = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1,container,false);
        hintTextView = view.findViewById(R.id.hint_textView);
        bills = DataSupport.findAll(Bill.class);
        textYear = view.findViewById(R.id.fragment1_year);
        textMonth = view.findViewById(R.id.fragment1_month);
        textExpend1 = view.findViewById(R.id.fragment1_expend1);
        textExpend2 = view.findViewById(R.id.fragment1_expend2);
        textIncome1 = view.findViewById(R.id.fragment1_income1);
        textIncome2 = view.findViewById(R.id.fragment1_income2);
        textMonthText = view.findViewById(R.id.fragment1_month_text);
        trangle = view.findViewById(R.id.fragment1_month_trangle);
        //设置日期为当前日期
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        textYear.setText(String.valueOf(mYear)+"年");
        textMonth.setText(String.valueOf(mMonth+1));
        getBills(mYear,mMonth);
        //设置recycleView
        recyclerView = view.findViewById(R.id.fragment1_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        //显示数据
        showDatas(mYear,mMonth);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textMonth.setOnClickListener(this);
        textMonthText.setOnClickListener(this);
        textYear.setOnClickListener(this);
        trangle.setOnClickListener(this);
        //判断添加账单的返回键确认键是否点击过
        SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        sign = pref.getBoolean("sign",false);
        if(sign){
            int y = DataSupport.findLast(Bill.class).getYear();
            int m = DataSupport.findLast(Bill.class).getMonth();
            if(y != mYear || m != mMonth){
                showDatas(y,m);
                mYear = y;
                mMonth = m;
                textYear.setText(String.valueOf(mYear)+"年");
                textMonth.setText(String.valueOf(mMonth+1));
            }
            SharedPreferences.Editor editor = getActivity().getSharedPreferences("data",Context.MODE_PRIVATE).edit();
            editor.putBoolean("sign",false);
            editor.apply();
        }
    }
    //获取当前月份的账单数据
    public void getBills(int y,int m){
        bills = DataSupport.where("year = ? and month = ?",String.valueOf(y),String.valueOf(m)).order("day").find(Bill.class);
    }
    //显示年月选择框
    public void showDatePicker(){
        //显示年月选择框
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
                textYear.setText(String.valueOf(mYear)+"年");
                textMonth.setText(String.valueOf(mMonth+1));
                showDatas(mYear,mMonth);
            }
        };
        mDatePickerDialog mdatePickerDialog = new mDatePickerDialog(getContext(),DatePickerDialog.THEME_HOLO_LIGHT,dateSetListener,mYear,mMonth,mDay);
        mdatePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment1_month:
            case R.id.fragment1_year:
            case R.id.fragment1_month_text:
            case R.id.fragment1_month_trangle:
                showDatePicker();
                break;
        }
    }
    //显示数据
    public void showDatas(int y,int m){
        getBills(y,m);
        if(bills.size()==0){
            hintTextView.setVisibility(View.VISIBLE);
            hintTextView.setText("暂无数据");
            hintTextView.setTextSize(20);
        }
        else {
            hintTextView.setVisibility(View.GONE);//暂无数据不可见
            adapter = new BillAdapter(getContext(),bills);
            recyclerView.setAdapter(adapter);
            double expend_sum = 0.0;
            double income_sum = 0.0;
            Bill bill;
            for(int i = 0;i < bills.size();i++){
                bill = bills.get(i);
                if(bill.getKind() == -1){
                    expend_sum += bill.getMoney();
                }
                else {
                    income_sum += bill.getMoney();
                }
            }
            String expend_str = String.format("%.2f",expend_sum);
            String income_str = String.format("%.2f",income_sum);
            String expend_str1 = expend_str.substring(0,expend_str.length()-3);
            String expend_str2 = expend_str.substring(expend_str.length()-3,expend_str.length());
            String income_str1 = income_str.substring(0,income_str.length()-3);
            String income_str2 = income_str.substring(income_str.length()-3,income_str.length());
            textExpend1.setText(expend_str1);
            textExpend2.setText(expend_str2);
            textIncome1.setText(income_str1);
            textIncome2.setText(income_str2);
        }
    }
}
