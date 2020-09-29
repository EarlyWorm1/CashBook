package dhu.cst.yinqingbo416.cashbook;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Fragment2 extends Fragment {
    Spinner spinner1;
    Spinner spinner2;
    int kind;//支出-1，收入1
    int sign;//日视图1，月视图2，年视图3
    int mYear;//年
    int mMonth;//月
    int mDay;//日
    TextView textDate;
    TextView hinttext;
    List<Bill>bills;//账单数据
    PieChart pieChart;
    LineChart lineChart;
    LineChartManager lineChartManager;
    String s1,s2,s3,s4;
    TextView t1,t2,t3;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2,container,false);
        spinner1 = view.findViewById(R.id.fragment2_spinner1);
        spinner2 = view.findViewById(R.id.fragment2_spinner2);
        textDate = view.findViewById(R.id.fragment2_date);
        hinttext = view.findViewById(R.id.hint_textView2);
        pieChart = view.findViewById(R.id.pie_chart);
        lineChart = view.findViewById(R.id.line_chart);
        textDate.setOnClickListener(new mClick());
        lineChartManager = new LineChartManager(lineChart);
        t1 = view.findViewById(R.id.xinxi1);
        t2 = view.findViewById(R.id.xinxi2);
        t3 = view.findViewById(R.id.xinxi3);
        //添加spinner数据
        addSpinnerData();
        //设置默认日期为当前日期
        setDate0();
        getDatas();
        return view;
    }
    private void setDate0() {
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
    }
    private void showDate(int y,int m,int d){
        textDate.setText(y+"年"+(m+1)+"月"+d+"日");
        t1.setText(y+"年"+(m+1)+"月"+d+"日账单");
    }
    private void showDate(int y,int m){
        textDate.setText(y+"年"+(m+1)+"月");
        t1.setText(y+"年"+(m+1)+"月账单");
    }
    private void showDate(int y){
        textDate.setText(y+"年");
        t1.setText(y+"年账单");
    }
    private void addSpinnerData() {
        final String[] spinnerDatas = {"支出","收入"};
        final String[] spinnerDatas2 ={"日账单","月账单","年账单"};
        //建立适配器
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(getContext(),R.layout.spinner_item,spinnerDatas);
        ArrayAdapter<String>adapter2 = new ArrayAdapter<String>(getContext(),R.layout.spinner_item2,spinnerDatas2);
        adapter.setDropDownViewResource(R.layout.dropdown_stytle);
        adapter2.setDropDownViewResource(R.layout.dropdown_stytle2);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter2);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (spinnerDatas[position]) {
                    case "支出":
                        kind = -1;
                        getDatas();
                        break;
                    case "收入":
                        kind = 1;
                        getDatas();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (spinnerDatas2[position]){
                    case "日账单":
                        sign = 1;
                        showDate(mYear,mMonth,mDay);
                        getDatas();
                        break;
                    case "月账单":
                        sign = 2;
                        showDate(mYear,mMonth);
                        getDatas();
                        break;
                    case "年账单":
                        sign = 3;
                        showDate(mYear);
                        getDatas();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private class mClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(sign == 1){
                //日历选择对话框
                showDatePicker1();
            }
            else if(sign == 2){
                //屏蔽日
                showDatePicker2();
            }else{
                //屏蔽日月
                showDatePicker3();
            }
        }
    }
    private void showDatePicker1() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
                showDate(mYear,mMonth,mDay);
                getDatas();
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),DatePickerDialog.THEME_HOLO_LIGHT,dateSetListener,mYear,mMonth,mDay);
        datePickerDialog.show();
    }
    private void showDatePicker2() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
                showDate(mYear,mMonth);
                getDatas();
            }
        };
        mDatePickerDialog mdatePickerDialog = new mDatePickerDialog(getContext(),DatePickerDialog.THEME_HOLO_LIGHT,dateSetListener,mYear,mMonth,mDay);
        mdatePickerDialog.show();
    }
    private void showDatePicker3() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
                showDate(mYear);
                getDatas();
            }
        };
        mDatePickerDialog2 mdatePickerDialog2 = new mDatePickerDialog2(getContext(),DatePickerDialog.THEME_HOLO_LIGHT,dateSetListener,mYear,mMonth,mDay);
        mdatePickerDialog2.show();
    }
    private void getDatas(){
        s1 = String.valueOf(kind);
        s2 = String.valueOf(mYear);
        s3 = String.valueOf(mMonth);
        s4 = String.valueOf(mDay);
        if(sign == 1){
            bills = DataSupport.select("name","imageId","money").where("kind = ? and year = ? and month = ? and day = ?",s1,s2,s3,s4).order("name").find(Bill.class);
            if(bills.size() > 0){
                hinttext.setVisibility(View.GONE);
                lineChart.setVisibility(View.GONE);
                //数据处理一
                HashMap dataMap = detalBillDatas1();
                if(kind == -1)
                    PieChartUtil.getPitChart().setPieChart(pieChart,dataMap,"支出占比",true);
                else
                    PieChartUtil.getPitChart().setPieChart(pieChart,dataMap,"收入占比",true);
                Huizong(mYear,mMonth,mDay);
            }
            else{
                hinttext.setVisibility(View.VISIBLE);
                lineChart.setVisibility(View.VISIBLE);
            }
        }else if(sign == 2){
            bills = DataSupport.select("name","imageId","money").where("kind = ? and year = ? and month = ?",s1,s2,s3).order("name").find(Bill.class);
            if(bills.size()>0){
                hinttext.setVisibility(View.GONE);
                lineChart.setVisibility(View.VISIBLE);
                HashMap dataMap1 = detalBillDatas1();
                if(kind == -1)
                    PieChartUtil.getPitChart().setPieChart(pieChart,dataMap1,"支出占比",true);
                else
                    PieChartUtil.getPitChart().setPieChart(pieChart,dataMap1,"收入占比",true);
                //数据处理2
                detalBillDatas2();
                Huizong(mYear,mMonth);
            }
            else {
                hinttext.setVisibility(View.VISIBLE);
            }
        }else {
            bills = DataSupport.select("name","imageId","money").where("kind = ? and year = ?",s1,s2).order("name").find(Bill.class);
            if(bills.size()>0){
                hinttext.setVisibility(View.GONE);
                lineChart.setVisibility(View.VISIBLE);
                HashMap dataMap2 = detalBillDatas1();
                if(kind == -1)
                    PieChartUtil.getPitChart().setPieChart(pieChart,dataMap2,"支出占比",true);
                else
                    PieChartUtil.getPitChart().setPieChart(pieChart,dataMap2,"收入占比",true);
                //数据处理3
                detalBillDatas3();
                Huizong(mYear);
            }
            else {
                hinttext.setVisibility(View.VISIBLE);
            }
        }

    }

    private void detalBillDatas3() {
        ArrayList<String> xValues = new ArrayList<>();
        List<Float> yValues = new ArrayList<>();
        double max = 0;
        for(int i = 1;i <= 12;i++){
            xValues.add(i+"月");
            List<Bill>b = DataSupport.select("name","imageId","money").where("kind = ? and year = ? and month = ?",s1,mYear+"",i+"").order("month").find(Bill.class);
            if(b.size()==0){
                yValues.add(0f);
            }else {
                double sum = 0;
                for(int j = 0;j < b.size();j++){
                    sum += b.get(j).getMoney();
                }
                if(sum > max)
                    max =sum;
                yValues.add((float)sum);
            }
        }
        lineChartManager.showLineChart(xValues,yValues,Color.BLACK);
        lineChartManager.setYAxis((int)max, 0, 10);
        lineChartManager.setDescription("Month");
    }

    private void detalBillDatas2() {
        //获取天数
        Calendar c = Calendar.getInstance();
        c.set(mYear,mMonth,0);
        int len = c.get(Calendar.DAY_OF_MONTH);
        //设置x轴的数据
        ArrayList<String> xValues = new ArrayList<>();
        List<Float> yValues = new ArrayList<>();
        double max = 0;
        for(int i = 1;i<=len;i++){
            if(i % 2 == 0)
                xValues.add("");
            else
                xValues.add(i+"");
            List<Bill>b = DataSupport.select("name","imageId","money").where("kind = ? and year = ? and month = ? and day = ?",s1,mYear+"",mMonth+"",i+"").order("day").find(Bill.class);
            if(b.size()==0){
                yValues.add(0f);
            }else {
                double sum = 0;
                for(int j = 0;j < b.size();j++){
                    sum += b.get(j).getMoney();
                }
                if(sum > max)
                    max =sum;
                yValues.add((float)sum);
            }
        }
        lineChartManager.showLineChart(xValues,yValues,Color.BLACK);
        lineChartManager.setYAxis((int)max, 0, 10);
        lineChartManager.setDescription("Day");
    }
    private HashMap detalBillDatas1() {
        Bill bill = bills.get(0);
        HashMap map = new HashMap();
        for(int i = 1;i < bills.size();i++){
            if(bill.getName().equals(bills.get(i).getName())){
                bill.setMoney(bill.getMoney()+bills.get(i).getMoney());
            }
            else{
                map.put(bill.getName(),String.valueOf(bill.getMoney()));
                bill = bills.get(i);
            }
        }
        map.put(bill.getName(),String.valueOf(bill.getMoney()));
        return map;
    }
    //汇总
    private void Huizong(int y,int m,int d){
        List<Bill>b1 = DataSupport.select("name","money").where("kind = ? and year = ? and month = ? and day = ?",-1+"",y+"",m+"",d+"").find(Bill.class);
        List<Bill>b2 = DataSupport.select("name","money").where("kind = ? and year = ? and month = ? and day = ?",1+"",y+"",m+"",d+"").find(Bill.class);
        int Num1 = b1.size();
        int Num2 = b2.size();
        double sum1 = 0;
        double sum2 = 0;
        for(int i = 0;i < b1.size();i++){
            sum1 += b1.get(i).getMoney();
        }
        for(int i = 0;i < b2.size();i++){
            sum2 += b2.get(i).getMoney();
        }
        String s = String.format("%.2f",sum1);
        t2.setText("共"+Num1+"笔支出，￥"+s);
        s = String.format("%.2f",sum2);
        t3.setText("共"+Num2+"笔收入，￥"+s);
    }
    private void Huizong(int y,int m){
        List<Bill>b1 = DataSupport.select("name","money").where("kind = ? and year = ? and month = ?",-1+"",y+"",m+"").find(Bill.class);
        List<Bill>b2 = DataSupport.select("name","money").where("kind = ? and year = ? and month = ?",1+"",y+"",m+"").find(Bill.class);
        int Num1 = b1.size();
        int Num2 = b2.size();
        double sum1 = 0;
        double sum2 = 0;
        for(int i = 0;i < b1.size();i++){
            sum1 += b1.get(i).getMoney();
        }
        for(int i = 0;i < b2.size();i++){
            sum2 += b2.get(i).getMoney();
        }
        String s = String.format("%.2f",sum1);
        t2.setText("共"+Num1+"笔支出，￥"+s);
        s = String.format("%.2f",sum2);
        t3.setText("共"+Num2+"笔收入，￥"+s);
    }
    private void Huizong(int y){
        List<Bill>b1 = DataSupport.select("name","money").where("kind = ? and year = ?",-1+"",y+"").find(Bill.class);
        List<Bill>b2 = DataSupport.select("name","money").where("kind = ? and year = ?",1+"",y+"").find(Bill.class);
        int Num1 = b1.size();
        int Num2 = b2.size();
        double sum1 = 0;
        double sum2 = 0;
        for(int i = 0;i < b1.size();i++){
            sum1 += b1.get(i).getMoney();
        }
        for(int i = 0;i < b2.size();i++){
            sum2 += b2.get(i).getMoney();
        }
        String s = String.format("%.2f",sum1);
        t2.setText("共"+Num1+"笔支出，￥"+s);
        s = String.format("%.2f",sum2);
        t3.setText("共"+Num2+"笔收入，￥"+s);
    }
}
