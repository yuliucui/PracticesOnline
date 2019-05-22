package net.lzzy.practicesonline.activities.fragments;
import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import net.lzzy.practicesonline.R;
import net.lzzy.practicesonline.activities.models.UserCookies;
import net.lzzy.practicesonline.activities.models.view.QuestionResult;
import net.lzzy.practicesonline.activities.models.view.WrongType;
import net.lzzy.practicesonline.activities.utils.ViewUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author lzzy_gxy
 * @date 2019/5/13
 * Description:
 */
public class CharFragment extends BaseFragment {
    public static final String COLOR_GREEN="#629755";
    public static final String COLOR_RED="#D81b60";
    public static final String COLOR_PRIMARY="#008577";
    public static final String COLOR_BROWN="#00574B";
    public static final float MIN_DISTANCE = 10f;
    List<QuestionResult> results;
    private OnCharSelectedListener listenter;
    public static final  String ARGS_RESULT="argResult";
    private PieChart pChart;
    private LineChart lChart;
    private BarChart bChart;
    private Chart[] charts;
    private View[] dots;
    private String[] titles=new String[]{"正确错误比例（单位%）","题目阅读量统计","题目错误类型量统计"};
    private int rightCount=0;
    private float touchX1;
    private int chartIndex=0;




    public static  CharFragment newInstance(List<QuestionResult> results){
        CharFragment fragment=new CharFragment();
        Bundle args=new Bundle();
        args.putParcelableArrayList(ARGS_RESULT, (ArrayList<? extends Parcelable>) results);
        fragment.setArguments(args);
        return  fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            results=getArguments().getParcelableArrayList(ARGS_RESULT);

        }
       for (QuestionResult questionResult:results){
            if (questionResult.isRight()){
                rightCount++;
            }
        }
    }
    @Override
    protected void populate() {
        TextView tvView = find(R.id.fragment_chart_tv_go);
        tvView.setOnClickListener(v -> listenter.gotoGrid());
        initCharts();
        configPieChart();
        displayPieChart();
        configBarLineChart(bChart);
        /**
         * 圆饼图
         */
        displayBarChart();
        /**
         * 折线图
         */
        displayLineChart();

        pChart.setVisibility(View.VISIBLE);
        View dot1 = find(R.id.fragment_chart_dot1);
        View dot2 = find(R.id.fragment_chart_dot2);
        View dot3 = find(R.id.fragment_chart_dot3);
        dots = new View[]{dot1, dot2, dot3};
        find(R.id.fragment_chart_container).setOnTouchListener(new ViewUtils.AbstractTouchHandler() {
            @Override
            public boolean handleTouch(MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    touchX1 = event.getX();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float touchX2 = event.getX();
                    if (Math.abs(touchX2 - touchX1) > MIN_DISTANCE) {
                        if (touchX2 < touchX1) {
                            if (chartIndex < charts.length - 1) {
                                chartIndex++;
                            } else {
                                chartIndex = 0;
                            }
                        } else {
                            if (chartIndex > 0) {
                                chartIndex--;
                            } else {
                                chartIndex = charts.length - 1;
                            }
                        }
                        switchChart();
                    }
                }
                return true;
            }

        });
    }

    private void displayLineChart() {
        ValueFormatter xFormatter=new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "Q." +(int)value;
            }
        };
        lChart.getXAxis().setValueFormatter(xFormatter);
        List<Entry> entries=new ArrayList<>();
        for (int i= 0;i < results.size(); i++){
            int times=UserCookies.getInstance().getReadCount(results.get(i).getQuestionId().toString());
            entries.add(new Entry(i + 1,times));
        }
        LineDataSet dataSet=new LineDataSet(entries,"查看次数");
        dataSet.setColor(Color.parseColor(COLOR_RED));
        dataSet.setLineWidth(1f);
        dataSet.setDrawCircles(true);
        dataSet.setCircleColor(Color.parseColor(COLOR_PRIMARY));
        dataSet.setValueTextSize(9f);
        LineData data=new LineData(dataSet);
        lChart.setData(data);
        lChart.invalidate();
    }

    private void displayBarChart() {
        ValueFormatter xFormatter=new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return WrongType.getInstance((int)value).toString();
            }
        };
        bChart.getXAxis().setValueFormatter(xFormatter);


        int right=0,miss=0,extra=0,wrong=0;
        for (QuestionResult questionResults:results){
            switch (questionResults.getType()){
                case RIGHT_OPTIONS:
                    right++;
                    break;
                case MISS_OPTIONS:
                    miss++;
                    break;
                case EXTRA_OPTIONS:
                    extra++;
                    break;
                case WRONG_OPTIONS:
                    wrong++;
                    break;
                default:
                    break;
            }
        }
        List<BarEntry> entries=new ArrayList<>();
        entries.add(new BarEntry(0,right));
        entries.add(new BarEntry(1,miss));
        entries.add(new BarEntry(2,extra));
        entries.add(new BarEntry(3,wrong));

        BarDataSet dataSet=new BarDataSet(entries,"查看类型");
        dataSet.setColors(Color.GREEN,Color.MAGENTA,Color.RED,Color.LTGRAY);
        ArrayList<IBarDataSet> dataSets=new ArrayList<>();
        dataSets.add(dataSet);
        BarData data = new BarData(dataSets);
        data.setBarWidth(0.8f);
        bChart.setData(data);
        bChart.invalidate();

    }

    private void configBarLineChart(BarLineChartBase chart) {
        XAxis xAxis=chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(11f);
        xAxis.setGranularity(1f);

        /**  Y 轴 **/
        YAxis yAxis=chart.getAxisLeft();
        yAxis.setLabelCount(8,false);
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setTextSize(11f);
        yAxis.setGranularity(1f);
        yAxis.setAxisMinimum(0);

        /** chart属性 **/
        chart.getLegend().setEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.setPinchZoom(false);
    }
    private void switchChart() {
        for (int i =0;i < charts.length;i++) {
            if (chartIndex == i) {
                charts[i].setVisibility(View.VISIBLE);
                dots[i].setBackgroundResource(R.drawable.dot_fill_style);
            }else {
                charts[i].setVisibility(View.GONE);
                dots[i].setBackgroundResource(R.drawable.dot_style);
            }
        }
    }

    private void displayPieChart() {
        List<PieEntry> entries=new ArrayList<>();
        entries.add(new PieEntry((float) rightCount /results.size(),"正确"));
        entries.add(new PieEntry((float)( results.size()-rightCount)/results.size(),"错误"));
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        List<Integer> colors=new ArrayList<>();
        colors.add(Color.GREEN);
        colors.add(Color.RED);
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        pChart.setData(data);
        pChart.invalidate();



    }

    private void configPieChart() {
        pChart.setUsePercentValues(true);
        pChart.setDrawHoleEnabled(false);
        pChart.getLegend().setOrientation(Legend.LegendOrientation.HORIZONTAL);
        pChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        pChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);


    }

    private void initCharts() {
        pChart=find(R.id.fragment_chart_pie);
        lChart=find(R.id.fragment_chart_line);
        bChart=find(R.id.fragment_chart_bar);
        charts=new Chart[]{pChart,lChart,bChart};
        int i=0;
        for (Chart chart:charts){
            chart.setTouchEnabled(false);
            chart.setVisibility(View.GONE);
            Description desc=new Description();
            desc.setText(titles[i++]);
            chart.setDescription(desc);
            chart.setNoDataText("获取数据中....");
            chart.setExtraOffsets(5,10,5,25);
        }

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_chart;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listenter= (OnCharSelectedListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"必须实现OnCharSelectedListener接口");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listenter=null;

    }

    @Override
    public void search(String kw) {

    }
    public interface  OnCharSelectedListener{
        void gotoGrid();
    }

}
