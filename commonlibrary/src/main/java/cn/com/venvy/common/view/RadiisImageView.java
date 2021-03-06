package cn.com.venvy.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.widget.FrameLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * 实现圆角图片的容器(内部有一ImageView)
 * Created by John on 2016/12/15.
 * //渲染设置4半径的布局
 */

public class RadiisImageView extends FrameLayout{
    private float[] radius;

    private Paint roundPaint;
    private Paint imagePaint;

    private SimpleDraweeView v;

    private Context mContext;
    public RadiisImageView(Context context){
        super(context);
        mContext=context;
        //创建一个集合
        radius=new float[8];

        roundPaint = new Paint();
        roundPaint.setColor(Color.WHITE);
        roundPaint.setAntiAlias(true);
        roundPaint.setStyle(Paint.Style.FILL);
        roundPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        imagePaint = new Paint();
        imagePaint.setXfermode(null);

        v=new SimpleDraweeView(mContext);
        FrameLayout.LayoutParams mParams=new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        addView(v,mParams);
    }
    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), imagePaint, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        drawTopLeft(canvas);
        drawTopRight(canvas);
        drawBottomLeft(canvas);
        drawBottomRight(canvas);
        canvas.restore();
    }

    private void drawTopLeft(Canvas canvas) {
        if ((radius[0] > 0)||(radius[1] > 0)) {
            Path path = new Path();
            path.moveTo(0, radius[1]);
            path.lineTo(0, 0);
            path.lineTo(radius[0], 0);
            path.arcTo(new RectF(0, 0, radius[0] * 2, radius[1] * 2),
                    -90, -90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }

    private void drawTopRight(Canvas canvas) {
        if ((radius[2] > 0)||(radius[3] > 0)) {
            int width = getWidth();
            Path path = new Path();
            path.moveTo(width - radius[2], 0);
            path.lineTo(width, 0);
            path.lineTo(width, radius[3]);
            path.arcTo(new RectF(width - 2 * radius[2], 0, width,
                    radius[3] * 2), 0, -90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }

    private void drawBottomLeft(Canvas canvas) {
        if ((radius[6] > 0)||(radius[7] > 0)) {
            int height = getHeight();
            Path path = new Path();
            path.moveTo(0, height - radius[7]);
            path.lineTo(0, height);
            path.lineTo(radius[6], height);
            path.arcTo(new RectF(0, height - 2 * radius[7],
                    radius[6] * 2, height), 90, 90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }

    private void drawBottomRight(Canvas canvas) {
        if ((radius[4] > 0)||(radius[5] > 0)) {
            int height = getHeight();
            int width = getWidth();
            Path path = new Path();
            path.moveTo(width - radius[4], height);
            path.lineTo(width, height);
            path.lineTo(width, height - radius[5]);
            path.arcTo(new RectF(width - 2 * radius[4], height - 2
                    * radius[5], width, height), 0, 90);
            path.close();
            canvas.drawPath(path, roundPaint);
        }
    }
    public void setRadii(float[] radius){
        this.radius=radius;
    }

    public void setCircle(float radius){
        this.radius =  new float[]{radius,radius,radius,radius,radius,radius,radius,radius};
    }
    public void setScaleType(ScalingUtils.ScaleType mScaleType){
        if(v==null)
            return;

        v.getHierarchy().setActualImageScaleType(mScaleType);
    }
    public SimpleDraweeView getImageView(){
        if(v!=null)
            return v;
        else
            return new SimpleDraweeView(mContext);

    }

    public void showImg(String url){
        DraweeController draweeController =
                Fresco.newDraweeControllerBuilder()
                        .setUri(url)
                        .setAutoPlayAnimations(true) // 设置加载图片完成后是否直接进行播放
                        .build();
        v.setController(draweeController);
    }

    public void setHierarchy(GenericDraweeHierarchy hierarchy) {
        v.setHierarchy(hierarchy);
    }
}
