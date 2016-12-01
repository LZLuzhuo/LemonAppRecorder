package me.luzhuo.lemonapprecorder.widget.piechart;

import java.util.ArrayList;

import org.xclcharts.chart.PieChart;
import org.xclcharts.chart.PieData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.event.click.ArcPosition;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotLegend;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2016-1-6 下午9:56:41
 * 
 * 描述:饼图
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class PieChartView extends MChartView implements Runnable{
	private static final String TAG = "PieChartView";
	private ArrayList<PieData> chartData = new ArrayList<PieData>(); //数据
	private String TITLE = "";
	private PieChart chart = new PieChart();
	private int mSelectedID = -1;
	private OnClickItem onClickItem;

	public PieChartView(Context context) {
		super(context, null);
	}

	public PieChartView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}

	public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	/**
	 * 设置数据
	 * @param data
	 */
	public void setData(ArrayList<PieData> data){
		if(data !=null) chartData = data;
	}
	
	/**
	 * 设置标题
	 * @param title
	 */
	public void setTitle(String title){
		TITLE = title;
	}
	
	/**
	 * 显示
	 */
	public void show(){
		chartRender();
		this.bindTouch(this,chart);
		new Thread(this).start();
	}
	
	private void chartRender(){
		try {					
			//设置绘图区默认缩进px值
			int [] ltrb = getPieDefaultSpadding();
			float right = DensityUtil.dip2px(getContext(), 100);
			chart.setPadding(ltrb[0], ltrb[1], right, ltrb[3]);
			
			//设置起始偏移角度(即第一个扇区从哪个角度开始绘制)
			chart.setInitialAngle(0);
			
			//标签显示(隐藏，显示在中间，显示在扇区外面)
			chart.setLabelStyle(XEnum.SliceLabelStyle.INSIDE);
			chart.getLabelPaint().setColor(Color.WHITE);
			
			//标题
			if(!TextUtils.isEmpty(TITLE))chart.setTitle(TITLE);
			chart.setTitleVerticalAlign(XEnum.VerticalAlign.BOTTOM);	

			//激活点击监听
			chart.ActiveListenItemClick();
			chart.showClikedFocus();
			chart.disablePanMode();
			
			//显示图例
			PlotLegend legend = chart.getPlotLegend();	
			legend.show();
			legend.setType(XEnum.LegendType.COLUMN);
			legend.setHorizontalAlign(XEnum.HorizontalAlign.RIGHT);
			legend.setVerticalAlign(XEnum.VerticalAlign.MIDDLE);
			legend.showBox();
			
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
	}

	@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
       //图所占范围大小
        chart.setChartRange(w,h);
    }
	
	@Override
    public void render(Canvas canvas) {
        try{
            chart.render(canvas);
        } catch (Exception e){
        	Log.e(TAG, e.toString());
        }
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		if(event.getAction() == MotionEvent.ACTION_UP){
			if(chart.isPlotClickArea(event.getX(),event.getY())){
				triggerClick(event.getX(),event.getY());					
			}
		}
		return true;
	}
	
	//触发监听
	private void triggerClick(float x,float y){	
		if(!chart.getListenItemClickStatus())return;
		
		ArcPosition record = chart.getPositionRecord(x,y);
		if( null == record) return;
		
		//用于处理点击时弹开，再点时弹回的效果
		PieData pData = chartData.get(record.getDataID());
		if(record.getDataID() == mSelectedID ){ //record.getDataID()是当前被点击的数据编号  //关(关的时候显示总数量)
			boolean bStatus = chartData.get(mSelectedID).getSelected(); //bStatus位false时打开,true时关闭
			chartData.get(mSelectedID).setSelected(!bStatus);
			if(onClickItem != null) onClickItem.onClick(record.getDataID(), bStatus == false ? true : false);
		}else{  //开(开的时候显示子条目数量)
			if(mSelectedID >= 0)
				chartData.get(mSelectedID).setSelected(false);
			pData.setSelected(true);
			if(onClickItem != null) onClickItem.onClick(record.getDataID(), true);
		}
		mSelectedID = record.getDataID();
		this.refreshChart();		
	}
	
	/**
	 * 绘制图形
	 */
	private void chartAnimation(){
		  try {
			    chart.setDataSource(chartData);
			  	int count = 360 / 10;
			  	
	          	for(int i=1;i<count ;i++){
	          		chart.setTotalAngle(10 * i);
	          		
	          		//激活点击监听
	    			if(count - 1 == i){
	    				chart.setTotalAngle(360);
	    				
	    				chart.ActiveListenItemClick();
	    			}
	          		postInvalidate();
	          }
          }
          catch(Exception e) {
              Thread.currentThread().interrupt();
          }
	}
	
	@Override
	public void run() {
		try {
         	chartAnimation();
         }catch(Exception e) {
             Thread.currentThread().interrupt();
         }
	}
	
	public void setOnClickItemLinstener(OnClickItem onClickItem){
		this.onClickItem = onClickItem;
	}
	
	public interface OnClickItem{
		/**
		 * 点击回调
		 * @param position 第几个数据
		 * @param isOpen 执行图形的打开/关闭
		 */
		void onClick(int position, boolean isOpen);
	}
}
