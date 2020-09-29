package dhu.cst.yinqingbo416.cashbook;

import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PieChartUtil {
    public final int[]PIE_COLORS={
            Color.rgb(181, 194, 202), Color.rgb(129, 216, 200), Color.rgb(241, 214, 145),
            Color.rgb(108, 176, 223), Color.rgb(195, 221, 155), Color.rgb(251, 215, 191),
            Color.rgb(237, 189, 189), Color.rgb(172, 217, 243),
            Color.rgb(255,225,224),Color.rgb(238,238,209),Color.rgb(205,205,180),Color.rgb(139,139,122),
            Color.rgb(255,215,0),Color.rgb(238,201,0),Color.rgb(205,173,0),Color.rgb(139,117,0),
            Color.rgb(255,211,155),Color.rgb(205,170,125),
            Color.rgb(255,193,193),Color.rgb(238,180,180),Color.rgb(205,155,155),Color.rgb(139,105,105),
            Color.rgb(255,130,71),Color.rgb(238,121,66),Color.rgb(205,104,57),Color.rgb(139,71,38),
            Color.rgb(187,255,255),Color.rgb(174,238,238),Color.rgb(150,205,205),Color.rgb(102,139,139),
            Color.rgb(127,255,212),Color.rgb(118,238,198),Color.rgb(102,205,170),Color.rgb(69,139,116),
            Color.rgb(255,246,143),Color.rgb(238,230,133),Color.rgb(205,198,115),Color.rgb(139,134,78)
    };
    private static PieChartUtil pieChartUtil;
    private List<PieEntry>entries;
    public static  PieChartUtil getPitChart(){
        if( pieChartUtil==null){
            pieChartUtil=new PieChartUtil();
        }
        return  pieChartUtil;
    }
    public void setPieChart(PieChart pieChart, Map<String,Double>pieValues,String title,boolean showLegend){
        pieChart.setUsePercentValues(true);//设置使用百分比模式
        pieChart.getDescription().setEnabled(false);//设置描述
        pieChart.setRotationEnabled(true);//设置可以旋转
        pieChart.setHighlightPerTapEnabled(true);//设置点击可以放大
        pieChart.setDrawCenterText(true);//设置环中文字
        pieChart.setDrawEntryLabels(true);
        pieChart.setDrawHoleEnabled(true);//环形
        pieChart.setExtraOffsets(0,0,0,0);//设置边距
        pieChart.setDragDecelerationFrictionCoef(0.35f);//设置摩擦系数
        pieChart.setCenterText(title);//设置环中文字
        pieChart.setCenterTextSize(15f);//设置环中文字大小
        pieChart.setCenterTextColor(Color.rgb(241,214,145));//设置环中文字颜色
        pieChart.setRotationAngle(120f);//设置旋转角度
        pieChart.setTransparentCircleRadius(61f);//设置半透明圆环的半径
        //设置环形中间空白颜色是白色
        pieChart.setHoleColor(Color.TRANSPARENT);
        //设置半透明圆环的颜色
        pieChart.setTransparentCircleColor(Color.WHITE);
        //设置半透明圆环的透明度
        pieChart.setTransparentCircleAlpha(110);
        //图例设置
        Legend legend = pieChart.getLegend();
        if (showLegend) {
            legend.setEnabled(true);//是否显示图例
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);//图例相对于图表横向的位置
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);//图例相对于图表纵向的位置
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);//图例显示的方向
            legend.setDrawInside(false);
            legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);//方向
        } else {
            legend.setEnabled(false);
        }
        setPieChartData(pieChart,pieValues);
        pieChart.animateX(1500, Easing.EasingOption.EaseInOutQuad);//数据显示动画
    }
    //设置饼图数据
    private void setPieChartData(PieChart pieChart, Map<String, Double> pieValues) {
        //遍历HashMap
        Set set = pieValues.entrySet();
        Iterator it = set.iterator();//得到适配器
        entries=new ArrayList<>();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            entries.add(new PieEntry(Float.valueOf(entry.getValue().toString()), entry.getKey().toString()));
        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);//设置饼块之间的间隔
        dataSet.setSelectionShift(6f);//设置饼块选中时偏离饼图中心的距离
        dataSet.setColors(PIE_COLORS);//设置饼块的颜色
        dataSet.setValueTextSize(5f);
        //设置数据显示方式有见图
        dataSet.setValueLinePart1OffsetPercentage(80f);//数据连接线距图形片内部边界的距离，为百分数
        dataSet.setValueLinePart1Length(0.3f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setValueLineColor( Color.rgb(108,176,223));//设置连接线的颜色
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);//y轴数据显示在饼图内/外
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);//x轴数据显示在饼图内/外
        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.DKGRAY);

        pieChart.setData(pieData);
        pieChart.highlightValues(null);
        pieChart.invalidate();
    }
}
