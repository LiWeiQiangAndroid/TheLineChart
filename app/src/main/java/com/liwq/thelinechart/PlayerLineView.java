package com.liwq.thelinechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by： lwq.
 * Created Time: 2019/3/28
 * Description：不带阴影折线图
 */
public class PlayerLineView extends View {

    /**
     * 坐标轴画笔
     */
    private Paint mAxisPaint;
    /**
     * 刻度文字画笔
     */
    private Paint mScaleTextPaint;

    /**
     * 图表左内边距，留出空间标注说明
     */
    private int mChartPaddingLeft;
    /**
     * 图表左内边距，留出空间标注说明
     */
    private int mChartPaddingRight;
    /**
     * 图表底部，留出空间标注说明
     */
    private int mChartPaddingBottom;
    /**
     * 图表底部，留出空间标注说明
     */
    private int mChartPaddingTop;

    /**
     * X轴长度
     */
    private int mXAxisLength;
    /**
     * Y轴长度
     */
    private int mYAxisLength;
    /**
     * X轴单位长度
     */
    private int mXUnitLength;
    /**
     * Y轴单位长度
     */
    private int mYUnitLength;

    private int mMaxRate = 100;//最大
    private int mMinRate = 0;//最小


    int mXLength = 6;//X轴最大次数
    int mYLength = 5;//Y轴最大次数

    private Context mContext;
    private List<Float> mDatalist;
    private float rateOnePx;
    private Canvas mCanvas;
    private Paint paint;
    private Paint redpointPoint;
    private String mType = "";

    public PlayerLineView(Context context) {
        super(context);
        init(context);
    }

    public PlayerLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayerLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mChartPaddingLeft = dip2px(mContext, 50);
        mChartPaddingRight = dip2px(mContext, 50);
        mChartPaddingBottom = dip2px(mContext, 20);
        mChartPaddingTop = dip2px(mContext, 40);
        initPaint();
    }

    private void initPaint() {
        //初始化坐标轴画笔
        mAxisPaint = new Paint();
        mAxisPaint.setStrokeWidth(dip2px(mContext, 1));
        mAxisPaint.setStyle(Paint.Style.STROKE);
        mAxisPaint.setAntiAlias(true);            //去锯齿
        mAxisPaint.setColor(Color.parseColor("#b8b8b8"));
        //初始化坐标轴画笔
        mScaleTextPaint = new Paint();
        mScaleTextPaint.setStrokeWidth(dip2px(getContext(), 0.5f));
        mScaleTextPaint.setAntiAlias(true);            //去锯齿
        mScaleTextPaint.setColor(Color.parseColor("#ff333333"));
        mScaleTextPaint.setTextSize(dip2px(mContext, 10));

        redpointPoint = new Paint();
        redpointPoint.setStrokeWidth(dip2px(getContext(), 4f));
        redpointPoint.setStyle(Paint.Style.STROKE);
        redpointPoint.setAntiAlias(true);            //去锯齿
        redpointPoint.setColor(Color.parseColor("#F45A5A"));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mXAxisLength = w - mChartPaddingLeft - mChartPaddingRight;
        mYAxisLength = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;
        canvas.translate(mChartPaddingLeft, mYAxisLength - mChartPaddingBottom);
        //绘制XY轴
        drawXYAxis(canvas);


        /**
         * X轴坐标值
         */
        List<String> xList = new ArrayList<>();

            xList.add("速度");
            xList.add("射门");
            xList.add("传球");
            xList.add("盘带");
            xList.add("防守");

        for (int i = 0; i < xList.size(); i++) {
            //开始画xList文字
            canvas.drawText(xList.get(i), (mXAxisLength / (xList.size() - 1) * i - dip2px(mContext, 10)),  dip2px(mContext, 14), mScaleTextPaint);
            //开始画Y坐标刻度
            canvas.drawLine(mXAxisLength / (xList.size() - 1) * i, 0f, mXAxisLength / (xList.size() - 1) * i, -mYAxisLength,
                    mAxisPaint);
        }

        rateOnePx = 20f / dip2px(mContext, 20f);


        if (mDatalist != null && mDatalist.size() > 0) {
            drawReturnRate();
        }
    }

    /**
     * 画图
     */
    private void drawReturnRate() {
        Paint pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.STROKE);
        pointPaint.setColor(Color.parseColor("#E63A53"));
        pointPaint.setStrokeWidth(dip2px(getContext(), 1f));

        mXUnitLength = mXAxisLength / (mDatalist.size() - 1);   //默认X轴单位长度为X轴长度
        Path path = new Path();

        for (int i = 0; i < mDatalist.size(); i++) {
            int x1 = i * mXUnitLength;
            float different = (mDatalist.get(i));//获取差值
            float y1 = -(different / rateOnePx) - dip2px(mContext, 10);
            if (i == 0) {
                path.moveTo(dip2px(mContext, 4), y1);
            } else {
                path.lineTo(x1 - dip2px(mContext, 3), y1);
                if (i < mDatalist.size()) {
                    path.moveTo(x1 + dip2px(mContext, 3), y1);
                }
            }

            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#E63A53"));
            paint.setStyle(Paint.Style.STROKE);//设置画笔颜色
            paint.setStrokeWidth(dip2px(mContext, 2));//设置画笔颜色
            mCanvas.drawCircle(x1, y1, dip2px(mContext, 4), paint);
            drawFloatTextBox(mCanvas, x1, (y1 - dip2px(mContext, 8)), mDatalist.get(i));
            drawFloatTextBox(mCanvas, x1, (y1 - dip2px(mContext, 8)), mDatalist.get(i));
        }


        ///绘制折线
        mCanvas.drawPath(path, pointPaint);


    }

    /**
     * 绘制XY轴
     *
     * @param canvas 绘制布
     */
    private void drawXYAxis(Canvas canvas) {
        mAxisPaint.setColor(Color.parseColor("#EEEEEE"));
        canvas.drawLine(0, 0, mXAxisLength, 0, mAxisPaint);    //绘制X轴
        canvas.drawLine(0, -dip2px(mContext, 20), mXAxisLength, -dip2px(mContext, 20), mAxisPaint);    //绘制X轴
        canvas.drawLine(0, -dip2px(mContext, 40), mXAxisLength, -dip2px(mContext, 40), mAxisPaint);    //绘制X轴
        canvas.drawLine(0, -dip2px(mContext, 60), mXAxisLength, -dip2px(mContext, 60), mAxisPaint);    //绘制X轴
        canvas.drawLine(0, -dip2px(mContext, 80), mXAxisLength, -dip2px(mContext, 80), mAxisPaint);    //绘制X轴
        canvas.drawLine(0, -dip2px(mContext, 100), mXAxisLength, -dip2px(mContext, 100), mAxisPaint);    //绘制X轴
    }

    /**
     * 初始化数据
     */
    public void initData(List<Float> list, String type) {
        mDatalist = list;
        mType = type;
        postInvalidate();
    }


    /**
     * 绘制显示Y值的浮动框
     *
     * @param canvas
     * @param x
     * @param y
     * @param text
     */
    private void drawFloatTextBox(Canvas canvas, float x, float y, float text) {
        String content= String.valueOf(text);
        int padding = dp2px(getContext(), 5);
        int boxWidth = dp2px(getContext(), 24) + padding * 2;
        int boxHeight;
        int indicatorHeight = dp2px(getContext(), 3);
        int cornerRadius = dp2px(getContext(), 5);
        if (text == -1f) {
            content = "未知";
        }
        Rect rect = getTextBounds(content, redpointPoint);
        if (boxWidth < (rect.width() + padding * 2))
            boxWidth = rect.width() + padding * 2;
        boxHeight = rect.height() + padding * 2;
        Path path = new Path();
        path.moveTo(x, y);
        path.lineTo(x - indicatorHeight, y - indicatorHeight); //倒三角左边的线
        path.lineTo(x - boxWidth / 2 + cornerRadius, y - indicatorHeight); //长方形底部左边的线
        path.quadTo(x - boxWidth / 2, y - indicatorHeight, x - boxWidth / 2, y - indicatorHeight - cornerRadius); //长方形底部左边的线
        path.lineTo(x - boxWidth / 2, y - indicatorHeight - boxHeight + cornerRadius);  //长方形左边的线
        path.quadTo(x - boxWidth / 2, y - indicatorHeight - boxHeight, x - boxWidth / 2 + cornerRadius, y - indicatorHeight - boxHeight);  //长方形左边的线
        path.lineTo(x + boxWidth / 2 - cornerRadius, y - indicatorHeight - boxHeight);  //长方形顶部的线
        path.quadTo(x + boxWidth / 2, y - indicatorHeight - boxHeight, x + boxWidth / 2, y - indicatorHeight - boxHeight + cornerRadius);  //长方形顶部的线
        path.lineTo(x + boxWidth / 2, y - indicatorHeight - cornerRadius);  //长方形右边的线
        path.quadTo(x + boxWidth / 2, y - indicatorHeight, x + boxWidth / 2 - cornerRadius, y - indicatorHeight);  //长方形右边的线
        path.lineTo(x + indicatorHeight, y - indicatorHeight); //长方形底部右边的线
        path.lineTo(x, y); //倒三角右边的线
        redpointPoint = new Paint();
        redpointPoint.setStyle(Paint.Style.FILL);
        redpointPoint.setColor(Color.parseColor("#E63A53"));
        canvas.drawPath(path, redpointPoint);

//        canvas.drawPath(path, getShadeColorPaint(0,y,x+mXAxisLength,y));

        redpointPoint.setTextSize(dp2px(getContext(), 10));
        redpointPoint.setColor(Color.WHITE);

        canvas.drawText(content, x - rect.width() / 2, y - indicatorHeight - padding, redpointPoint);

    }

    /**
     * dp转化为px工具
     */
    private int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * sp转化为px工具
     */
    private int sp2px(Context context, int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * 获取丈量文本的矩形
     *
     * @param text
     * @param paint
     * @return
     */
    private Rect getTextBounds(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
