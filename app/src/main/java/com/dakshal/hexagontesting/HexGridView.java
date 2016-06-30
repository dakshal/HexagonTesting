package com.dakshal.hexagontesting;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by dakshal on 28/05/16.
 */

public class HexGridView extends View {
    private static final String TAG = "HexGridView";//implements View.OnTouchListener{

//    private GestureDetector gestureDetector;


    private int numColumns, numRows;
    private int cellWidth, cellHeight;
    private int height, width;
    float swipDistance = 0.0f;
    Context context;
    private Paint servifyPrimaryColor = new Paint();
    private Paint whitePaint = new Paint();
    //    public int[] selectedDevices;
    private int isCalibration = -1;
    int count = 0;
    public int subCategory;
    Intent intent;
    private Point p1, p2;
    private ScaleGestureDetector mScaleDetector;
    private float mLastTouchX, mLastTouchY;
    private float mPosX, mPosY;
    private static final int INVALID_POINTER_ID = -1;
    private float mScaleFactor = 1.f;
    int imageSize;
    String[] images;
    Rect rect;

    Paint textPaint;

    private int mActivePointerId = INVALID_POINTER_ID;
    private int minGridSize;
    Bitmap scaledImagePurple, scaledImageGreen;
    ArrayList<Bitmap> resizedIcon = new ArrayList<>();
    private boolean setImageFetched = false;
    private Bitmap scaledImageLightGreen;
    private boolean selected;
    private int IMAGE_QUALITY = 50;

    private Bitmap[] scaledBitmap = new Bitmap[IMAGE_QUALITY];

    int sizeArray[];

//    LinearLayout linearLayout;
//    ArrayList<TextView> textViews;


    public HexGridView(Context context) {
        super(context);
    }
//
//    public HexGridView(Context context, Intent intent) {
//        this(context, intent, null);
//    }

    public HexGridView(Context context, int subCategory, int rows) {
        super(context);

//        height = canvas.getHeight();
//        width = canvas.getWidth();


        this.context = context;
        this.subCategory = subCategory;
        Log.d(TAG, "subCategory: " + subCategory);
        numColumns = subCategory;
//        selectedDevices = new int[subCategory];
        this.numRows = rows;
//        whitePaint.setStyle(Paint.Style.STROKE);
        whitePaint.setColor(Color.WHITE);
        whitePaint.setTextSize(convertDpToPixel(15, context));
        images = new String[subCategory];
        sizeArray = new int[subCategory];

        rect = new Rect();

//        linearLayout = new LinearLayout(context);

//        textViews = new ArrayList<>();

        this.intent = intent;

//        setNumColumns(5);
//        setNumRows(5);
        calculateDimensions();
        servifyPrimaryColor.setColor(getResources().getColor(R.color.colorServifyPrimary));
        servifyPrimaryColor.setStyle(Paint.Style.FILL_AND_STROKE);

        for (int i = 0; i < sizeArray.length; i++) {
            sizeArray[i] = IMAGE_QUALITY;
        }

//        invalidate();
//        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

//        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
        calculateDimensions();
    }

    public int getNumColumns() {
        return numColumns;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
        calculateDimensions();
    }

    public int getNumRows() {
        return numRows;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateDimensions();
    }

    private void calculateDimensions() {

//        Logger.d("noOfRows" + numRows);

        for (int i = 0; i < subCategory; i++) {
//                if (i == 0 || j == 0 || i == (numRows - 1) || j == (numColumns - 1) || i == j || i == (numColumns - j - 1) || i==(numRows/2) || j==(numColumns/2)) {
            OnBoardingNew.selectedDevices[i] = 0;
        }

        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Compute the height required to render the view
        // Assume Width will always be MATCH_PARENT.
        float columns = 0.00f;
        columns = (float) subCategory / numRows;
        Log.d(TAG, "onDraw: product: " + columns);
        Log.d(TAG, "onDraw: " + Math.ceil(columns));
        height = MeasureSpec.getSize(heightMeasureSpec); // Since 3000 is bottom of last Rect to be drawn added and 50 for padding.
        int hexSize = (int) ((height / numRows) * Math.ceil(columns));
        int extraPadding = (int) ((height * Math.ceil((columns * 5) / 4 + numRows)) / (8 * numRows));
        width = hexSize + extraPadding;
        Log.d("height and width", "onMeasure: height: " + height + " width: " + width + " hexSize: " + hexSize + " extraPadding: " + extraPadding);

//        setUpImages(width, height);

        setMeasuredDimension(width, height);
    }

    private void setUpImages() {
        Log.d("height", "HexGridView: height: " + height);
        Log.d("width", "HexGridView: width: " + width);
        minGridSize = (height / (3 * numRows));
        imageSize = minGridSize * 3;


        Bitmap imageBack = BitmapFactory.decodeResource(getResources(), R.drawable.hex, null);
        scaledImagePurple = Bitmap.createScaledBitmap(imageBack, (minGridSize * 11 / 4), minGridSize * 3, false);

        OnBoardingNew.hexSize = (minGridSize * 11 / 4);

        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.hex_green, null);
        scaledImageGreen = Bitmap.createScaledBitmap(image, (minGridSize * 3), (minGridSize * 13 / 4), false);

        for (int i = IMAGE_QUALITY - 1; i >= 0; i--) {
            scaledBitmap[i] = Bitmap.createScaledBitmap(image, ((minGridSize * 3) * (i + 1)) / IMAGE_QUALITY, ((minGridSize * 13 / 4) * (i + 1)) / IMAGE_QUALITY, false);
        }

        Bitmap imageLight = BitmapFactory.decodeResource(getResources(), R.drawable.hex_light_green, null);
        scaledImageLightGreen = Bitmap.createScaledBitmap(imageLight, (minGridSize * 3), (minGridSize * 13 / 4), false);

        for (int i = 0; i < subCategory; i++) {
            int picId = context.getResources().getIdentifier(OnBoardingNew.images[i], "drawable", context.getPackageName());

            Log.d(TAG, "setUpImages: " + picId);
            Bitmap icon = BitmapFactory.decodeResource(getResources(), picId, null);
            resizedIcon.add(Bitmap.createScaledBitmap(icon, (minGridSize * 21 / 12), minGridSize * 19 / 12, false));
        }

//        for (int i = 0; i < subCategory; i++) {
//
////            textView.setText(OnBoardingNew.productSubCategories.get(i).getDisplayName());
////            textView
//
//            String[] temp = OnBoardingNew.images[i].split("_");
//            images[i] = "";
//            for (int j = 1; j < temp.length; j++) {
//                images[i] = images[i] + temp[j] + "\n";
//            }
////            textView.setText(images[i]);
//
////            textViews.add(textView);
//
////            textView
//        }


//        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        textPaint.setARGB(255, 0, 0, 0);
//        textPaint.setTextAlign(Paint.Align.CENTER);
//        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
//        textPaint.setTextSize(convertDpToPixel(10, context));

        setImageFetched = true;

    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        selected = false;

        try {

            if (!setImageFetched) {
                Log.d(TAG, "onDraw: setting up image");
                setUpImages();
            }

            subCategory = OnBoardingNew.productSubCategory.size();

            height = canvas.getHeight();
            width = canvas.getWidth();

//        Log.d("height", "onDraw: " + height);
//        Log.d("width", "onDraw: " + width);
            minGridSize = (height / (3 * numRows));
            imageSize = minGridSize * 3;


            float columns = 0.00f;
            columns = (float) subCategory / numRows;
//        Log.d(TAG, "onDraw: product: " + columns);
//        Log.d(TAG, "onDraw: " + Math.ceil(columns));
            for (int rows = 0; rows < numRows; rows++) {
                for (int column = 0; column < Math.ceil(columns); column++) {
//                canvas.drawColor(Color.BLACK);
                    int currPos = column * numRows + rows;
//                    Log.d("drawing position", "onDraw: " + column + "" + rows + "" + currPos);
                    if (currPos < subCategory) {
                        int displayPosition = currPos;
                        currPos = OnBoardingNew.productSubCategory.get(column * numRows + rows) - 6;
//                    String res = "R.drawable." + OnBoardingNew.images[currPos];
//                    int picId = getResources().getIdentifier(res, "drawable", context.getPackageName());

//                    Log.d(TAG, "onDraw: imageName = " + res);
//                    Log.d(TAG, "onDraw: picID = " + picId);

//                    linearLayout.removeAllViews();
                        boolean check = false;
//                        Log.d(TAG, "check state: " + subCategory + " " + numRows + " " + displayPosition + " " + subCategory + " ");
                        if (subCategory % numRows != 0 && subCategory % numRows <= numRows / 2 && displayPosition == subCategory - 1 && subCategory != 1) {
//                            Log.d(TAG, "onDraw: checked true" + column);
                            check = true;
                            selected = true;
                            rows = rows + 1;
                        } else {

                        }
                        if (OnBoardingNew.selectedDevices[currPos] == 4) {
                            if (rows % 2 == 0) {
                                canvas.drawBitmap(scaledBitmap[sizeArray[currPos]], (((((minGridSize * 5) / 8 + imageSize / 2) + column * (imageSize + (minGridSize / 4))) - (minGridSize / 8))*IMAGE_QUALITY)/(sizeArray[currPos]), (((minGridSize / 2) + rows * (minGridSize * 11 / 4)) - (minGridSize / 8)*IMAGE_QUALITY)/(sizeArray[currPos]), servifyPrimaryColor);
                                canvas.drawBitmap(resizedIcon.get(currPos), ((minGridSize * 5) / 8 + imageSize / 2) + column * (imageSize + (minGridSize / 4)) + (minGridSize * 6 / 12), (minGridSize / 2) + rows * (minGridSize * 11 / 4) + minGridSize * 5 / 12, servifyPrimaryColor);
                                setUpTextView(canvas, currPos, (((minGridSize * 5) / 8 + imageSize / 2) + column * (imageSize + (minGridSize / 4))) - (minGridSize / 8) + (minGridSize * 3) / 2, (minGridSize / 2) + rows * (minGridSize * 11 / 4) + minGridSize * 6 / 12 + minGridSize * 22 / 12, rows);
                            } else {
                                canvas.drawBitmap(scaledBitmap[sizeArray[currPos]], (((minGridSize / 2) + column * (imageSize + (minGridSize / 4))) - (minGridSize / 8)*IMAGE_QUALITY)/(sizeArray[currPos]+1), (((minGridSize / 2) + rows * (minGridSize * 11 / 4)) - (minGridSize / 8)*IMAGE_QUALITY)/(sizeArray[currPos]+1), servifyPrimaryColor);
                                canvas.drawBitmap(resizedIcon.get(currPos), ((minGridSize / 2) + column * (imageSize + (minGridSize / 4))) + (minGridSize * 6 / 12), (minGridSize / 2) + rows * (minGridSize * 11 / 4) + minGridSize * 5 / 12, servifyPrimaryColor);
                                setUpTextView(canvas, currPos, ((minGridSize / 2) + column * (imageSize + (minGridSize / 4))) - (minGridSize / 8) + (minGridSize * 3) / 2, (minGridSize / 2) + rows * (minGridSize * 11 / 4) + minGridSize * 6 / 12 + minGridSize * 22 / 12, rows);
                            }
                            continue;
                        }

                        if (rows % 2 == 0) {
                            if (OnBoardingNew.selectedDevices[currPos] == 0) {
                                canvas.drawBitmap(scaledImagePurple, ((minGridSize * 5) / 8 + imageSize / 2) + column * (imageSize + (minGridSize / 4)), (minGridSize / 2) + rows * (minGridSize * 11 / 4), servifyPrimaryColor);
                            } else if (OnBoardingNew.selectedDevices[currPos] == 1) {
                                canvas.drawBitmap(scaledBitmap[sizeArray[currPos]], ((((minGridSize * 5) / 8 + imageSize / 2) + column * (imageSize + (minGridSize / 4))) - (minGridSize / 8)*IMAGE_QUALITY)/(sizeArray[currPos]), (((minGridSize / 2) + rows * (minGridSize * 11 / 4)) - (minGridSize / 8)*IMAGE_QUALITY)/(sizeArray[currPos]), servifyPrimaryColor);
                            } else {
                                canvas.drawBitmap(scaledImageLightGreen, (((minGridSize * 5) / 8 + imageSize / 2) + column * (imageSize + (minGridSize / 4))) - (minGridSize / 8), ((minGridSize / 2) + rows * (minGridSize * 11 / 4)) - (minGridSize / 8), servifyPrimaryColor);
                            }
                            canvas.drawBitmap(resizedIcon.get(currPos), ((minGridSize * 5) / 8 + imageSize / 2) + column * (imageSize + (minGridSize / 4)) + (minGridSize * 6 / 12), (minGridSize / 2) + rows * (minGridSize * 11 / 4) + minGridSize * 5 / 12, servifyPrimaryColor);

                            setUpTextView(canvas, currPos, (((minGridSize * 5) / 8 + imageSize / 2) + column * (imageSize + (minGridSize / 4))) - (minGridSize / 8) + (minGridSize * 3) / 2, (minGridSize / 2) + rows * (minGridSize * 11 / 4) + minGridSize * 6 / 12 + minGridSize * 22 / 12, rows);

//                        int yPos = ((minGridSize / 2) + rows * (minGridSize * 11 / 4)) - (minGridSize / 8) +;
//                                (int) ((canvas.getHeight() / 2) - ((textPaint.descent() +
//                                textPaint.ascent()) / 2));

//                        canvas.drawText(images[currPos], xPos, yPos, textPaint);

                        } else {
                            if (OnBoardingNew.selectedDevices[currPos] == 0) {
                                canvas.drawBitmap(scaledImagePurple, ((minGridSize / 2) + column * (imageSize + (minGridSize / 4))), ((minGridSize / 2) + rows * (minGridSize * 11 / 4)), servifyPrimaryColor);
                            } else if (OnBoardingNew.selectedDevices[currPos] == 1) {
                                canvas.drawBitmap(scaledBitmap[sizeArray[currPos]], (((minGridSize / 2) + column * (imageSize + (minGridSize / 4))) - (minGridSize / 8)*IMAGE_QUALITY)/(sizeArray[currPos]), (((minGridSize / 2) + rows * (minGridSize * 11 / 4)) - (minGridSize / 8)*IMAGE_QUALITY)/(sizeArray[currPos]), servifyPrimaryColor);
                            } else {
                                canvas.drawBitmap(scaledImageLightGreen, ((minGridSize / 2) + column * (imageSize + (minGridSize / 4))) - (minGridSize / 8), ((minGridSize / 2) + rows * (minGridSize * 11 / 4)) - (minGridSize / 8), servifyPrimaryColor);
                            }
                            canvas.drawBitmap(resizedIcon.get(currPos), ((minGridSize / 2) + column * (imageSize + (minGridSize / 4))) + (minGridSize * 6 / 12), (minGridSize / 2) + rows * (minGridSize * 11 / 4) + minGridSize * 5 / 12, servifyPrimaryColor);

                            setUpTextView(canvas, currPos, ((minGridSize / 2) + column * (imageSize + (minGridSize / 4))) - (minGridSize / 8) + (minGridSize * 3) / 2, (minGridSize / 2) + rows * (minGridSize * 11 / 4) + minGridSize * 6 / 12 + minGridSize * 22 / 12, rows);

//                        int yPos = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() +
//                                textPaint.ascent()) / 2));

//                        canvas.drawText(images[currPos], xPos, yPos, textPaint);

                        }
                        if (sizeArray[currPos] == 0 || sizeArray[currPos] == IMAGE_QUALITY) {
                        } else {
                            Log.d(TAG, "onDraw: " + sizeArray[currPos]);
                            sizeArray[currPos]--;
                            if (sizeArray[currPos] == 1) {
                                OnBoardingNew.selected.add(OnBoardingNew.productSubCategory.get(displayPosition));
                                OnBoardingNew.productSubCategory.remove(displayPosition);
                            }
                        }
                        if (check) {
                            check = false;
                            rows--;
                        }
                    }
                }
            }

/*

        if (numColumns == 0 || numRows == 0)
            return;

        int width = getWidth();
        int height = getHeight();

        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                if (selectedDevices[i][j] == 0)
                    canvas.drawRect(i * cellWidth, j * cellHeight, (i + 1) * cellWidth, (j + 1) * cellHeight, whitePaint);
                if (selectedDevices[i][j] == 1)
                    canvas.drawRect(i * cellWidth, j * cellHeight, (i + 1) * cellWidth, (j + 1) * cellHeight, servifyPrimaryColor);
            }
        }

        for (int i = 1; i < numColumns; i++) {
            canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, servifyPrimaryColor);
        }

        for (int i = 1; i < numRows; i++) {
            canvas.drawLine(0, i * cellHeight, width, i * cellHeight, servifyPrimaryColor);
        }

        if (noOfDeviceSelected >= (numColumns * numRows) - ((numColumns / 2) + 1) * ((numRows / 2) + 1)) {

        }
*/

        } catch (Exception e) {
            Toast.makeText(context, "something went wrong during view setup", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        postInvalidateDelayed(100);
    }

    private void setUpTextView(Canvas canvas, int position, int x, int y, int j) {
        String[] splitName = OnBoardingNew.images[position].split("_");
//        canvas.drawColor(Color.WHITE);
        int height = (((minGridSize * 33) / 12) - y) / splitName.length;
        whitePaint.setTextSize(height);

//        Log.d(TAG, "setUpTextView: whitePaint textSize---> " + whitePaint.getTextSize());

        if (splitName.length > 1) {


            for (int i = 0; i < splitName.length; i++) {
                whitePaint.getTextBounds(splitName[i], 0, splitName[i].length(), rect);

                int text_width = rect.width();
                int text_height = rect.height();

                canvas.drawText(splitName[i], x - (text_width / 2), y + i * (text_height * 3 / 2), whitePaint);

            }

        } else {
            float size = (float) splitName[0].length() / 4;
            Log.d(TAG, "setUpTextView: " + size + "\t 1" + splitName[0] + "1");

//            whitePaint.setTextSize(convertDpToPixel(15, context));

            whitePaint.getTextBounds(splitName[0], 0, splitName[0].length(), rect);

            int text_width = rect.width();

            canvas.drawText(splitName[0], x - (text_width / 2), y, whitePaint);
        }
    }

    public void transformColor(float scrollX, float scrollY) {
        int posX;
        if (scrollY < (minGridSize / 2)) {
            return;
        }
        int posY = (int) (((scrollY - (minGridSize / 2)) / (minGridSize * 11 / 4)));

        if ((minGridSize / 2) + (minGridSize * 11 / 4) > scrollY) {
            if (scrollX < ((minGridSize * 5) / 8 + imageSize / 2)) {
                return;
            }
            posX = (int) (((scrollX - ((minGridSize * 5) / 8 + imageSize / 2))) / (imageSize + (minGridSize / 4)));
        } else if ((minGridSize / 2) + 2 * (minGridSize * 11 / 4) < scrollY && (minGridSize / 2) + 3 * (minGridSize * 11 / 4) > scrollY) {
            if (scrollX < ((minGridSize * 5) / 8 + imageSize / 2)) {
                return;
            }
            posX = (int) (((scrollX - ((minGridSize * 5) / 8 + imageSize / 2))) / (imageSize + (minGridSize / 4)));
        } else {
            posX = (int) (((scrollX - (minGridSize / 2))) / (imageSize + (minGridSize / 4)));
        }

        if (posY >= numRows) {
            posY = numRows - 1;
        }

        int currPos = posX * numRows + posY;

        if (currPos > subCategory) {
            return;
        }

        if (OnBoardingNew.selectedDevices[currPos] == 4) {
            invalidate();
            return;
        }

        Log.d("tag", "transformColor: " + currPos);
        if (currPos < subCategory) {
            int myClickPosition = currPos;
            currPos = OnBoardingNew.productSubCategory.get(currPos) - 6;
            if (OnBoardingNew.selectedDevices[currPos] == 0 || OnBoardingNew.selectedDevices[currPos] == 2) {
                OnBoardingNew.noOfDeviceSelected++;
                OnBoardingNew.selectedDevices[currPos] = 1;
                sizeArray[currPos]--;
//                OnBoardingNew.selected.add(OnBoardingNew.productSubCategory.get(myClickPosition));
//                OnBoardingNew.productSubCategory.remove(myClickPosition);
            } else {
//                OnBoardingNew.noOfDeviceSelected--;
//                OnBoardingNew.selectedDevices[currPos] = 0;
            }
            OnBoardingNew.setSelectedDeviceCount();
        } else if (currPos == subCategory && selected) {
            currPos = currPos - 1;
            int myClickPosition = currPos;
            currPos = OnBoardingNew.productSubCategory.get(currPos) - 6;
            if (OnBoardingNew.selectedDevices[currPos] == 0 || OnBoardingNew.selectedDevices[currPos] == 2) {
                OnBoardingNew.noOfDeviceSelected++;
                OnBoardingNew.selectedDevices[currPos] = 1;
                sizeArray[currPos]--;
//                OnBoardingNew.selected.add(OnBoardingNew.productSubCategory.get(myClickPosition));
//                OnBoardingNew.productSubCategory.remove(myClickPosition);
            } else {
//                OnBoardingNew.noOfDeviceSelected--;
//                OnBoardingNew.selectedDevices[currPos] = 0;
            }
            OnBoardingNew.setSelectedDeviceCount();
        }
        getBacktoDetault(scrollX, scrollY);

        Log.d(TAG, "transformColor: productSubCategory size: " + OnBoardingNew.productSubCategory.size());
//        invalidate();
    }

    public void addDefaultDevices() {

        OnBoardingNew.setSelectedDeviceCount();
//        invalidate();
    }

    public void itemSelected(float scrollX, float scrollY) {
        int posX, posY;
        if (scrollY < (minGridSize / 2)) {
            return;
        }
        posY = (int) (((scrollY - (minGridSize / 2)) / (minGridSize * 11 / 4)));

        if ((minGridSize / 2) + (minGridSize * 11 / 4) > scrollY) {
            if (scrollX < ((minGridSize * 5) / 8 + imageSize / 2)) {
                return;
            }
            posX = (int) (((scrollX - ((minGridSize * 5) / 8 + imageSize / 2))) / (imageSize + (minGridSize / 4)));
//            posY = (int) (((scrollY - (minGridSize / 2)) / (minGridSize * 11 / 4)));
        } else if ((minGridSize / 2) + 2 * (minGridSize * 11 / 4) < scrollY && (minGridSize / 2) + 3 * (minGridSize * 11 / 4) > scrollY) {
            if (scrollX < ((minGridSize * 5) / 8 + imageSize / 2)) {
                return;
            }
            posX = (int) (((scrollX - ((minGridSize * 5) / 8 + imageSize / 2))) / (imageSize + (minGridSize / 4)));
        } else {
            posX = (int) (((scrollX - (minGridSize / 2))) / (imageSize + (minGridSize / 4)));
        }

        if (posY >= numRows) {
            posY = numRows - 1;
        }

        int currPos = posX * numRows + posY;

        Log.d(TAG, "itemSelected: x: " + posX + " y: " + posY);

        if (currPos > subCategory) {
            return;
        }
        Log.d("tag", "itemSelected: " + currPos);
        if (currPos < subCategory) {
            int myClickPosition = currPos;
            currPos = OnBoardingNew.productSubCategory.get(currPos) - 6;
            if (OnBoardingNew.selectedDevices[currPos] == 0) {
                OnBoardingNew.selectedDevices[currPos] = 2;
            } else if (OnBoardingNew.selectedDevices[currPos] == 1) {
                OnBoardingNew.selectedDevices[currPos] = 3;
            }
        } else if (currPos == subCategory && selected) {
            currPos = OnBoardingNew.productSubCategory.get(currPos - 1) - 6;
            if (OnBoardingNew.selectedDevices[currPos] == 0) {
                OnBoardingNew.selectedDevices[currPos] = 2;
            } else if (OnBoardingNew.selectedDevices[currPos] == 1) {
                OnBoardingNew.selectedDevices[currPos] = 3;
            }
        }
//        invalidate();
    }

    public void getBacktoDetault(float scrollX, float scrollY) {
//        int posX;
//        if (scrollY < (minGridSize / 2)) {
//            return;
//        }
//        int posY = (int) (((scrollY - (minGridSize / 2)) / (minGridSize * 11 / 4)));
//
//        if ((minGridSize / 2) + (minGridSize * 11 / 4) > scrollY) {
//            if (scrollX < ((minGridSize * 5) / 8 + imageSize / 2)) {
//                return;
//            }
//            posX = (int) (((scrollX - ((minGridSize * 5) / 8 + imageSize / 2))) / (imageSize + (minGridSize / 4)));
//        } else if ((minGridSize / 2) + 2 * (minGridSize * 11 / 4) < scrollY && (minGridSize / 2) + 3 * (minGridSize * 11 / 4) > scrollY) {
//            if (scrollX < ((minGridSize * 5) / 8 + imageSize / 2)) {
//                return;
//            }
//            posX = (int) (((scrollX - ((minGridSize * 5) / 8 + imageSize / 2))) / (imageSize + (minGridSize / 4)));
//        } else {
//            posX = (int) (((scrollX - (minGridSize / 2))) / (imageSize + (minGridSize / 4)));
//        }
//        if (posX >= (subCategory / numRows) || posY >= numRows) {
//            return;
//        }
//
//        int currPos = posX * numRows + posY;
//        Log.d("tag", "getBacktoDetault: " + currPos);
//        if (currPos < subCategory) {
//            if (selectedDevices[currPos] == 2) {
//                selectedDevices[currPos] = 0;
//            } else if (selectedDevices[currPos] == 3) {
//                selectedDevices[currPos] = 1;
//            }
//        }
        subCategory = OnBoardingNew.productSubCategory.size();
        for (int i = 0; i < subCategory; i++) {
            int pos = OnBoardingNew.productSubCategory.get(i) - 6;
            if (OnBoardingNew.selectedDevices[pos] == 2) {
                OnBoardingNew.selectedDevices[pos] = 0;
            } else if (OnBoardingNew.selectedDevices[pos] == 3) {
                OnBoardingNew.selectedDevices[pos] = 1;
            }
        }
//        invalidate();
    }

/*
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev);

        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();

                mLastTouchX = x;
                mLastTouchY = y;
                mActivePointerId = ev.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                final float x = ev.getX(pointerIndex);
                final float y = ev.getY(pointerIndex);

                // Only move if the ScaleGestureDetector isn't processing a gesture.
                if (!mScaleDetector.isInProgress()) {
                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;

                    mPosX += dx;
                    mPosY += dy;
                    swipDistance = swipDistance + dx;
                    Log.d("HexGridView", "onTouch: distance= " + swipDistance + "");
//                    invalidate();
                }

                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }
                break;
            }
        }

        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));

            invalidate();
            return true;
        }
    }*/
//    @Override
//    public boolean onTouch(View view, MotionEvent event) {
//
//        Log.d("HexGridView", "onTouchEvent: "+ "touch listner working");
//        if (gestureDetector.onTouchEvent(event)) {
//             single tap
//            float x = event.getX() + swipDistance;
//            float y = event.getY();
//            Log.d("x and y", "onTouchEvent: column is: " + x + " row is: " + y);
//            return true;
//        } else {
//             your code for move and drag
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    p1 = new Point((int) event.getX(), (int) event.getY());
//                    break;
//                case MotionEvent.ACTION_UP:
//                    p2 = new Point((int) event.getX(), (int) event.getY());
//                    swipDistance = p1.x - p2.x;
//                    Log.d("HexGridView", "onTouch: distance= " + swipDistance + "");
//                    break;
//            }
//        }
//
//        return false;

//        int column = (int) (event.getX() / cellWidth);
//        int row = (int) (event.getY() / cellHeight);
//        Log.d("x and y", "onTouchEvent: column is: " + column + " row is: " + row);
//        if (column < numColumns && row < numRows) {
//            if (selectedDevices[column][row] < 1) {
//                noOfDeviceSelected++;
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        selectedDevices[column][row] = 1;
//                        invalidate();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        selectedDevices[column][row] = 1;
//                        invalidate();
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        selectedDevices[column][row] = 1;
//                        invalidate();
//                        break;
//                }
//            Logger.d("noOfDeviceSelected"+noOfDeviceSelected);
//            }
//    }

//    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {
//
//        @Override
//        public boolean onSingleTapConfirmed(MotionEvent event) {
//            return true;
//        }
//    }

//    return true;
//}

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        return gestureDetector.onTouchEvent(event);
//    }
//
//    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
//
//        private static final int SWIPE_THRESHOLD = 100;
//        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
//
//        @Override
//        public boolean onDown(MotionEvent e) {
//            return true;
//        }
//
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            boolean result = false;
//            try {
//                float diffY = e2.getY() - e1.getY();
//                float diffX = e2.getX() - e1.getX();
//                if (Math.abs(diffX) > Math.abs(diffY)) {
//                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
//                        swipDistance = swipDistance + diffX;
//                        Log.d("swipe", "onFling: "+ diffX);
//
//                        if (diffX > 0) {
//                            onSwipeRight();
//                        } else {
//                            onSwipeLeft();
//                        }
//                    }
//                    result = true;
//                }
//                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
//                    if (diffY > 0) {
//                        onSwipeBottom();
//                    } else {
//                        onSwipeTop();
//                    }
//                }
//                result = true;
//
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
//            return result;
//        }
//    }
//
//    public void onSwipeRight() {
//    }
//
//    public void onSwipeLeft() {
//    }
//
//    public void onSwipeTop() {
//    }
//
//    public void onSwipeBottom() {
}