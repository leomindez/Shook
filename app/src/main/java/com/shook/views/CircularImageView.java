package com.shook.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.shook.R;

/**
 * Created by leonelmendez on 28/12/14.
 */
public class CircularImageView extends ImageView{

    private Bitmap image;
    private BitmapShader shader;
    private Paint paint;
    private int viewWidth;
    private int borderWidth = 4;
    private Paint paintBorder;
    private int viewHeight;
    private Context mContext;
    private int backgroundImage;
    private int colorBorder;
    private boolean isPaintBorder;
    private float widthBorder;



    public CircularImageView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
        initAttributes(context,attrs);
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
        initAttributes(context,attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircularImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
        init();
        initAttributes(context,attrs);
    }


    @Override
    public void onDraw(Canvas canvas) {
        paintCircularImage(canvas);
    }

    private void initAttributes(Context context,AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.circularImage,0,0);

        try{
            backgroundImage = typedArray.getInt(R.styleable.circularImage_defaultBackgroundImage,R.drawable.ic_launcher);
            colorBorder = typedArray.getColor(R.styleable.circularImage_colorBorder, Color.BLACK);
            isPaintBorder = typedArray.getBoolean(R.styleable.circularImage_paintBorder,true);
            widthBorder = typedArray.getDimension(R.styleable.circularImage_widthStrokeBorder,4.0F);

        }finally {
            typedArray.recycle();
        }




    }


    /** Method to paint a circular image
     * @param canvas
     *
     * */
    private void paintCircularImage(Canvas canvas){

        BitmapDrawable bitmapDrawable = (BitmapDrawable) this.getDrawable();
        if (bitmapDrawable != null)
            image = bitmapDrawable.getBitmap();

        if (image != null) {
            shader = new BitmapShader(Bitmap.createScaledBitmap(image, canvas.getWidth(), canvas.getHeight(), false), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            int circleCenter = viewWidth / 2;

            canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter- 3f, paintBorder);
            canvas.drawCircle(circleCenter + borderWidth, circleCenter + borderWidth, circleCenter - 4.0f, paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec, widthMeasureSpec);

        viewWidth = width - (borderWidth * 2);
        viewHeight = height - (borderWidth * 2);

        setMeasuredDimension(width, height);
    }


    /**
     * Method to calculate the width from  spec width
     * @param measureSpec
     *
     * */
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = viewWidth;
        }

        return result;
    }

    /**
     * Method to calculate the height from  spec height
     * @param measureSpecHeight
     *
     * */
    private int measureHeight(int measureSpecHeight, int measureSpecWidth) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpecHeight);
        int specSize = MeasureSpec.getSize(measureSpecHeight);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = viewHeight;
        }
        return (result + 2);
    }


    /** Method to init the components
     *
     * */
    private void init(){

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setStrokeWidth(widthBorder);
        paintBorder.setAntiAlias(true);
        paintBorder.setColor(colorBorder);

    }

    public int getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(int backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public int getColorBorder() {
        return colorBorder;
    }

    public void setColorBorder(int colorBorder) {
        this.colorBorder = colorBorder;
    }

    public boolean isPaintBorder() {
        return isPaintBorder;
    }

    public void setPaintBorder(boolean isPaintBorder) {
        this.isPaintBorder = isPaintBorder;
    }

    public float getWidthBorder() {
        return widthBorder;
    }

    public void setWidthBorder(float widthBorder) {
        this.widthBorder = widthBorder;
    }



}
