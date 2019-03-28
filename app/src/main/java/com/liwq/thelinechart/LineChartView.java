package com.liwq.thelinechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by： lwq.
 * Created Time: 2019/3/28
 * Description：带阴影折线图
 */
public class LineChartView extends View {

    private Context context;
    private Paint chartLinePaint;
    private Paint textPaint;
    private Paint pointPaint;
    private Paint lastPointPaint;
    private Paint shadePaint;
    private Paint redpointPoint;
    private int lineColor = Color.parseColor("#E63A53");
    private int textColor = Color.parseColor("#333333");
    private int screenWidth;
    private int screenHeight;
    private int width;
    private int height;
    /**
     * X轴长度
     */
    private int contentWidth;
    /**
     * Y轴长度
     */
    private int contentHeight;

    private int topY;
    private int bottomY;
    private int leftX;
    private int rightX;

    private String[] xAisx = new String[]{"评分", "技术", "防守", "进攻", "状态"};

    private String[] yAisx = new String[]{"125","100","75", "50", "25", "0"};


    private String[] xAisxValues;
    private String[] yAisxValues;
    private int radius;//圆的半径

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        init();
    }

    private void init() {
        radius = dip2px(context, 4);

        chartLinePaint = new Paint();
        chartLinePaint.setAntiAlias(true);//设置抗锯齿
        chartLinePaint.setColor(Color.parseColor("#EEEEEE"));
        chartLinePaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(textColor);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(32);
        textPaint.setTextAlign(Paint.Align.CENTER);

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setColor(Color.parseColor("#E63A53"));
        pointPaint.setStyle(Paint.Style.STROKE);
        pointPaint.setStrokeWidth(dip2px(context, 2));

        lastPointPaint = new Paint();
        lastPointPaint.setAntiAlias(true);
        lastPointPaint.setColor(Color.parseColor("#ffffff"));
        lastPointPaint.setStyle(Paint.Style.FILL);

        redpointPoint = new Paint();
        redpointPoint.setStrokeWidth(dip2px(context, 4f));
        redpointPoint.setStyle(Paint.Style.STROKE);
        redpointPoint.setAntiAlias(true);            //去锯齿
        redpointPoint.setColor(Color.parseColor("#F45A5A"));

        shadePaint = new Paint();
        shadePaint.setAntiAlias(true);
        shadePaint.setStyle(Paint.Style.FILL);

        topY = dip2px(context, 16);
        bottomY = dip2px(context, 30);
        leftX = dip2px(context, 50);
        rightX = dip2px(context, 50);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = screenWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = screenHeight * 5 / 8;
        }

        contentWidth = width - leftX-rightX;
        contentHeight = height - topY - bottomY;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        chartLinePaint.setStrokeWidth(dip2px(context, 1));
        float xtemp = contentWidth * 1.0f / (xAisx.length - 1);//水平分成几等分
        float ytemp = contentHeight * 1.0f / (yAisx.length - 1);//竖直方向分成几等分

        //画Y轴横线和文本
        for (int i = 0; i <= (yAisx.length - 1); i++) {
            canvas.drawLine(leftX, topY + ytemp * i, leftX + contentWidth, topY + ytemp * i, chartLinePaint);
            canvas.drawText(yAisx[i], leftX / 2, topY + i * ytemp + dip2px(context, 3), textPaint);
        }
        //画Y轴
        for (int i = 0; i <= xAisx.length - 1; i++) {
            //canvas.drawLine(leftX, 0, leftX, topY + contentHeight, chartLinePaint);
            canvas.drawLine(leftX+ xtemp * i, topY, leftX+xtemp * i, topY + contentHeight, chartLinePaint);
        }

        //画X轴
        canvas.drawLine(leftX, topY + contentHeight, leftX + contentWidth, topY + contentHeight, chartLinePaint);

        //画X轴坐标
        for (int i = 0; i <= xAisx.length - 1; i++) {
            canvas.drawLine(leftX + xtemp * i, topY + contentHeight, leftX + xtemp * i, topY + contentHeight - dip2px(context, 5), chartLinePaint);
        }

        //画X轴文本
        textPaint.setColor(textColor);
        for (int i = 0; i <= xAisx.length - 1; i++) {
            canvas.drawText(xAisx[i], leftX + i * xtemp, topY + contentHeight + bottomY / 2, textPaint);
        }

        Paint LinePointPaint = new Paint();
        LinePointPaint.setAntiAlias(true);
        LinePointPaint.setStyle(Paint.Style.STROKE);
        LinePointPaint.setColor(lineColor);
        LinePointPaint.setStrokeWidth(dip2px(context, 1f));

        if (xAisxValues != null && xAisxValues.length > 0 && yAisxValues != null && yAisxValues.length > 0) {
            ArrayList<MyPoint> myPoints = values2Point(xAisx, yAisx, xAisxValues, yAisxValues);
            if (myPoints.size() > 0) {

                //画折线
                for (int i = 0; i < myPoints.size() - 1; i++) {
                    canvas.drawLine(myPoints.get(i).x, myPoints.get(i).y, myPoints.get(i + 1).x, myPoints.get(i + 1).y, LinePointPaint);
                }
                //画阴影
                Path path = new Path();
                path.moveTo(leftX, topY + contentHeight);
                path.lineTo(myPoints.get(0).x, myPoints.get(0).y);
                Shader mShader = new LinearGradient(0, contentHeight-topY, 0, contentHeight + topY,
                        new int[]{Color.parseColor("#99fbdfdf"), Color.TRANSPARENT}, null, Shader.TileMode.CLAMP);
                shadePaint.setShader(mShader);
                for (int i = 0; i < myPoints.size() - 1; i++) {
                    path.lineTo(myPoints.get(i + 1).x, myPoints.get(i + 1).y);
                }
                path.lineTo(myPoints.get(myPoints.size() - 1).x, topY + contentHeight);
                canvas.drawPath(path, shadePaint);
                //描点
                for (int i = 0; i < myPoints.size(); i++) {
                    canvas.drawCircle(myPoints.get(i).x, myPoints.get(i).y, radius, pointPaint);//画边框圆
                    canvas.drawCircle(myPoints.get(i).x, myPoints.get(i).y, radius - dip2px(context, 2) / 2, lastPointPaint);//画填充圆
                }

                for (int i = 0; i < myPoints.size(); i++) {
                    drawFloatTextBox(canvas, myPoints.get(i).x, (myPoints.get(i).y - dip2px(context, 8)), yAisxValues[i]);
                    drawFloatTextBox(canvas, myPoints.get(i).x, (myPoints.get(i).y - dip2px(context, 8)), yAisxValues[i]);
                }
            }
        }

    }


    private void drawFloatTextBox(Canvas canvas, float x, float y, String text) {
        String content= String.valueOf(text);
        int padding = dip2px(context, 5);
        int boxWidth = dip2px(context, 24) + padding * 2;
        int boxHeight;
        int indicatorHeight = dip2px(context, 3);
        int cornerRadius = dip2px(context, 5);
        if ("-1".equals(text)) {
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

        redpointPoint.setTextSize(dip2px(context, 10));
        redpointPoint.setColor(Color.WHITE);

        canvas.drawText(content, x - rect.width() / 2, y - indicatorHeight - padding, redpointPoint);

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
     * xAisx X轴
     * yAisx Y轴
     * xAisxValues x轴坐标点
     * yAisxValues x轴坐标点
     */
    private ArrayList<MyPoint> values2Point(String[] xAisx, String[] yAisx, String[] xAisxValues, String[] yAisxValues) {
        ArrayList<MyPoint> myPoints = new ArrayList<>();
        int xtemp = contentWidth / (xAisx.length - 1);//水平分成几等分
        int ytemp = contentHeight / (yAisx.length - 1);//竖直方向分成几等分
        if (xAisxValues.length > 0) {
            for (int i = 0; i <= xAisxValues.length - 1; i++) {
                MyPoint myPoint = new MyPoint();
                myPoint.x = leftX + i * xtemp;
                int y = topY + contentHeight - Integer.parseInt(yAisxValues[i]) * contentHeight / Integer.parseInt(yAisx[0]);
                myPoint.y = y;
                myPoints.add(myPoint);
            }
        }
        return myPoints;
    }

    /**
     * 设置数据源
     * @param xAisx
     * @param yAisx
     * @param xAisxValues
     * @param yAisxValues
     */
    public void setData(String[] xAisx, String[] yAisx, String[] xAisxValues, String[] yAisxValues) {
        this.xAisx = xAisx;
        this.yAisx = yAisx;
        this.xAisxValues = xAisxValues;
        this.yAisxValues = yAisxValues;
        invalidate();
    }

    public class MyPoint {
        private int x;
        private int y;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
