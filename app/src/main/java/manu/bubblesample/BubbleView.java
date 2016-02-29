package manu.bubblesample;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by emmanuelchagnas on 25/02/16.
 * <p/>
 * Uses custom attributes :
 * <p/>
 * <declare-styleable name="BubbleView">
 * <attr name="arrow_height" format="dimension" />
 * <attr name="arrow_position" format="enum">
 * <enum name="Left" value="0"/>
 * <enum name="Top" value="1"/>
 * <enum name="Right" value="2"/>
 * <enum name="Bottom" value="3"/>
 * </attr>
 * </declare-styleable>
 */
public class BubbleView extends View {

    private static final String TAG = "BubbleView";

    private int arrowDimension = 0;
    private ARROWPOSITION arrowPosition = ARROWPOSITION.LEFT;
    private float roundRectSize = 0f;
    private int color = 0;
    private int textColor = 0;
    private String text;
    private int textSize = 20;
    private int xTarget = 0;
    private int yTarget = 0;
    private int bubbleSize = 0;

    /**
     * Possibles positions of the arrow
     */
    private enum ARROWPOSITION {
        LEFT(0),
        TOP(1),
        RIGHT(2),
        BOTTOM(3);

        private int position = 0;

        ARROWPOSITION(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }
    }

    public BubbleView(Context context) {
        super(context);
    }

    public BubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * Get attributes
     *
     * @param attrs
     */
    private void init(AttributeSet attrs) {
        if (attrs != null) {
            //get the attributes specified in attrs.xml using the name we included
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs,
                    R.styleable.BubbleView, 0, 0);

            try {
                //Arrow dimension
                arrowDimension = (int)a.getDimension(R.styleable.BubbleView_bubble_arrow_height, 0f);

                //Text size
                textSize = (int)a.getDimension(R.styleable.BubbleView_bubble_text_size, 20f);

                //Arrow position
                int pos = a.getInt(R.styleable.BubbleView_bubble_arrow_position, 0);
                ARROWPOSITION[] values = ARROWPOSITION.values();
                for (int i = 0; i < values.length; i++) {
                    if (values[i].getPosition() == pos) {
                        arrowPosition = values[i];
                    }
                }

                //Round Rect size
                roundRectSize = a.getDimension(R.styleable.BubbleView_bubble_roundrect_size, 0f);

                //Bubble color
                int colorId = a.getColor(R.styleable.BubbleView_bubble_color, 0);
                if (colorId == 0) {
                    //Color by default
                    this.color = getContext().getResources().getColor(android.R.color.white);
                } else {
                    this.color = colorId;
                }

                //Text
                text = a.getString(R.styleable.BubbleView_bubble_text);

                //Text color
                int colorTextId = a.getColor(R.styleable.BubbleView_bubble_text_color, 0);
                if (colorTextId == 0) {
                    //Color by default
                    this.textColor = getContext().getResources().getColor(android.R.color.black);
                } else {
                    this.textColor = colorTextId;
                }

                //Bubble size
                bubbleSize = (int)a.getDimension(R.styleable.BubbleView_bubble_size, 0f);

            } finally {
                a.recycle();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getMeasuredWidth();
        int h = getMeasuredHeight();

        Log.d(TAG, "onDraw: w=" + w + " h=" + h + " arrowDimension=" + arrowDimension + " arrowPosition=" + arrowPosition);

        //TODO : for tests, to remove
        switch (arrowPosition) {
            case LEFT:
                setTarget(0, h/2);
                break;
            case TOP:
                setTarget(w/2, 0);
                break;
            case RIGHT:
                setTarget(0, h/2);
                break;
            case BOTTOM:
                setTarget(w/2, 0);
                break;
        }

        drawBubbleWithArrow(canvas, xTarget, yTarget, w, h);
    }

    public void setTarget(int xTarget, int yTarget){
        this.xTarget = xTarget;
        this.yTarget = yTarget;
    }

    private void drawBubbleWithArrow(Canvas canvas, int xTarget, int yTarget, int w, int h){

        //Draw bubble
        drawBubble(canvas, xTarget, yTarget, w, h);

        //Draw arrow
        drawArrow(canvas, xTarget, yTarget, w, h);

        //Draw text
        drawText(canvas, xTarget, yTarget, w, h);
    }

    /**
     * Draw bubble
     *
     * @param canvas
     * @param w
     * @param h
     */
    private void drawBubble(Canvas canvas, int xTarget, int yTarget, int w, int h) {
        RectF rectBubble = getRectBubble(xTarget, yTarget, w, h);
        Paint paintBubble = new Paint();
        paintBubble.setColor(color);
        canvas.drawRoundRect(rectBubble, roundRectSize, roundRectSize, paintBubble);
    }

    /**
     * Draw arrow
     *
     * @param canvas
     * @param w
     * @param h
     */
    private void drawArrow(Canvas canvas, int xTarget, int yTarget, int w, int h) {
        int x1Arrow = 0;
        int y1arrow = 0;
        int x2Arrow = 0;
        int y2Arrow = 0;
        int x3Arrow = 0;
        int y3Arrow = 0;

        switch (arrowPosition) {
            case LEFT:
                x1Arrow = 0;
                y1arrow = yTarget;
                x2Arrow = x1Arrow + arrowDimension;
                y2Arrow = y1arrow - arrowDimension / 2;
                x3Arrow = x1Arrow + arrowDimension;
                y3Arrow = y1arrow + arrowDimension / 2;
                break;
            case TOP:
                x1Arrow = xTarget;
                y1arrow = 0;
                x2Arrow = x1Arrow + arrowDimension / 2;
                y2Arrow = y1arrow + arrowDimension;
                x3Arrow = x1Arrow - arrowDimension / 2;
                y3Arrow = y1arrow + arrowDimension;
                break;
            case RIGHT:
                x1Arrow = w;
                y1arrow = yTarget;
                x2Arrow = x1Arrow - arrowDimension;
                y2Arrow = y1arrow + arrowDimension / 2;
                x3Arrow = x1Arrow - arrowDimension;
                y3Arrow = y1arrow - arrowDimension / 2;
                break;
            case BOTTOM:
                x1Arrow = xTarget;
                y1arrow = h;
                x2Arrow = x1Arrow - arrowDimension / 2;
                y2Arrow = y1arrow - arrowDimension;
                x3Arrow = x1Arrow + arrowDimension / 2;
                y3Arrow = y1arrow - arrowDimension;
                break;
        }

        //Create paint
        Paint paintArrow = new Paint();
        paintArrow.setStyle(Paint.Style.FILL_AND_STROKE);
        paintArrow.setAntiAlias(true);
        paintArrow.setColor(color);

        //Draw path
        Path path = new Path();
        path.moveTo(x1Arrow, y1arrow);
        path.lineTo(x2Arrow, y2Arrow);
        path.lineTo(x3Arrow, y3Arrow);
        canvas.drawPath(path, paintArrow);
    }

    /**
     * Get the rectangle to draw the bubble
     *
     * @param w
     * @param h
     * @return
     */
    private RectF getRectBubble(int xTarget, int yTarget, int w, int h) {

        Log.d(TAG, "getRectBubble: xTarget="+xTarget+" yTarget="+yTarget+" w="+w+" h="+h);

        int leftBubble = 0;
        int topBubble = 0;
        int rightBubble = 0;
        int bottomBubble = 0;

        switch (arrowPosition) {
            case LEFT:
                leftBubble = arrowDimension;
                int iLeft = yTarget - (bubbleSize / 2);
                topBubble = iLeft > 0 ? iLeft : 0;
                rightBubble = w;
                int jLeft = yTarget + (bubbleSize / 2);
                if(iLeft > 0) {
                    bottomBubble = jLeft < h ? jLeft : h;
                }else{
                    bottomBubble = bubbleSize;
                }
                break;
            case TOP:
                int iTop = xTarget - (bubbleSize / 2);
                leftBubble = iTop > 0 ? iTop : 0;
                topBubble = arrowDimension;
                int jTop = xTarget + (bubbleSize / 2);
                if(iTop > 0) {
                    rightBubble = jTop < w ? jTop : w;
                }else{
                    rightBubble = bubbleSize;
                }
                bottomBubble = h;
                break;
            case RIGHT:
                leftBubble = 0;
                int iRight = yTarget - (bubbleSize / 2);
                topBubble = iRight > 0 ? iRight : 0;
                rightBubble = w - arrowDimension;
                int jRight = yTarget + (bubbleSize / 2);
                if(iRight > 0) {
                    bottomBubble = jRight < h ? jRight : h;
                }else{
                    bottomBubble = bubbleSize;
                }
                break;
            case BOTTOM:
                int iBottom = xTarget - (bubbleSize / 2);
                leftBubble = iBottom > 0 ? iBottom : 0;
                topBubble = 0;
                int jBottom = xTarget + (bubbleSize / 2);
                if(iBottom > 0) {
                    rightBubble = jBottom < w ? jBottom : w;
                }else{
                    rightBubble = bubbleSize;
                }
                bottomBubble = h - arrowDimension;

                Log.d(TAG, "getRectBubble: iBottom="+iBottom+" jBottom="+jBottom);
                break;
        }
        return new RectF(leftBubble, topBubble, rightBubble, bottomBubble);
    }

    private void drawText(Canvas canvas, int xTarget, int yTarget, int w, int h){

        Paint paintText = new Paint();
        paintText.setColor(textColor);
        paintText.setTextSize(textSize);

        //Calculate text bounds
        Rect textBounds = new Rect();
        paintText.getTextBounds(text, 0, text.length(), textBounds);

        //Log.d(TAG, "drawText: text="+text+" left="+textBounds.left+" top="+textBounds.top+" right="+textBounds.right+" bottom="+textBounds.bottom);

        int wBubble = 0;
        int hBubble = 0;

        int wSpace = 0;
        int hSpace = 0;

        int xText = 0;
        int yText = 0;
        int wText = textBounds.right - textBounds.left;
        int hText = textBounds.bottom - textBounds.top;

        //Log.d(TAG, "drawText: text w="+wText+" h="+hText);

        switch (arrowPosition) {
            case LEFT:
                wBubble = w - arrowDimension;
                hBubble = h;
                wSpace = (wBubble - wText)/2;
                hSpace = (hBubble - hText)/2;
                xText = arrowDimension + wSpace;

                int iLeft = yTarget + (bubbleSize / 2);
                if(iLeft < h){
                    iLeft = yTarget - (bubbleSize / 2);
                    if(iLeft > 0) {
                        yText = yTarget + (hText / 2);
                    }else{
                        yText = (bubbleSize / 2) + (hText / 2);
                    }
                }else{
                    yText = h - (bubbleSize / 2) + (hText / 2);
                }
                break;
            case TOP:
                wBubble = w;
                hBubble = h - arrowDimension;
                wSpace = (wBubble - wText)/2;
                hSpace = (hBubble - hText)/2;

                int iTop = xTarget + (bubbleSize / 2);
                if(iTop < w){
                    iTop = xTarget - (bubbleSize / 2);
                    if(iTop > 0) {
                        xText = xTarget - (wText / 2);
                    }else{
                        xText = (bubbleSize / 2) - (wText / 2);
                    }
                }else{
                    xText = w - (bubbleSize / 2) - (wText / 2);
                }
                yText = h - hSpace;
                break;
            case RIGHT:
                wBubble = w - arrowDimension;
                hBubble = h;
                wSpace = (wBubble - wText)/2;
                hSpace = (hBubble - hText)/2;
                xText = wSpace;

                int iRight = yTarget + (bubbleSize / 2);
                if(iRight < h){
                    iRight = yTarget - (bubbleSize / 2);
                    if(iRight > 0) {
                        yText = yTarget + (hText / 2);
                    }else{
                        yText = (bubbleSize / 2) + (hText / 2);
                    }
                }else{
                    yText = h - (bubbleSize / 2) + (hText / 2);
                }
                break;
            case BOTTOM:
                wBubble = w;
                hBubble = h - arrowDimension;
                wSpace = (wBubble - wText)/2;
                hSpace = (hBubble - hText)/2;

                int iBottom = xTarget + (bubbleSize / 2);
                if(iBottom < w){
                    iBottom = xTarget - (bubbleSize / 2);
                    if(iBottom > 0){
                        xText = xTarget - (wText / 2);
                    }else{
                        xText = (bubbleSize / 2) - (wText / 2);
                    }
                }else{
                    xText = w - (bubbleSize / 2) - (wText / 2);
                }
                yText = hSpace + hText;
                break;
        }

        canvas.drawText(text, xText, yText, paintText);
    }
}
