package aeropresscipe.divinelink.aeropress.features;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public abstract class SwipeHelper extends ItemTouchHelper.SimpleCallback {

    public static final int BUTTON_WIDTH = 150;
    public static final int TEXT_SIZE = 14;

    private Map<Integer, RectF> buttonRegions;
    private Map<Integer, Map<Integer, RectF>> clickPositionsAndRegions;
    private Map<Integer, Map<Integer, RectF>> staticStartingPositions;

    private RecyclerView recyclerView;
    private List<UnderlayButton> buttons;
    private GestureDetector gestureDetector;
    private int swipedPos = -1;
    private float swipeThreshold = 0.5f;
    private Map<Integer, List<UnderlayButton>> buttonsBuffer;
    private Queue<Integer> recoverQueue;
    private ArrayList<Integer> openPositions;

    private SharedPreferencesListManager prefManagerList;

    private Context mContext;


    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            for (UnderlayButton button : buttons) {
                if (button.onClick(e.getX(), e.getY(), buttons.indexOf(button))) {
                    break;
                }
            }
            return true;
        }
    };


    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent e) {
            //   if (swipedPos < 0) return false;
            Point point = new Point((int) e.getRawX(), (int) e.getRawY());

            openPositions = getOpenPositionsFromSharedPreferences();
            for (int i : openPositions) {
                if (recyclerView.findViewHolderForAdapterPosition(i) == null) {
                    openPositions.remove(openPositions.indexOf(i));
                    buttonsBuffer.remove(i);
                    removeFromClickPositionsAndRegions(i);
                    saveToSharedPreferences();
                    return false;
                }
                RecyclerView.ViewHolder swipedViewHolder = recyclerView.findViewHolderForAdapterPosition(i);
                View swipedItem = null;
                if (swipedViewHolder != null) {
                    swipedItem = swipedViewHolder.itemView;
                    Rect rect = new Rect();
                    swipedItem.getGlobalVisibleRect(rect);

                    if (e.getAction() == MotionEvent.ACTION_DOWN || e.getAction() == MotionEvent.ACTION_UP || e.getAction() == MotionEvent.ACTION_MOVE) {
                        if (rect.top < point.y && rect.bottom > point.y)
                            gestureDetector.onTouchEvent(e);
                        else {
                            // recoverQueue.add(swipedPos);
                            // swipedPos = -1;
                            recoverSwipedItem();
                        }
                    }
                } else { // If it's null then remove it from openPositions
                    if (swipedPos > 0) {
                        openPositions.remove(openPositions.indexOf(i));
                        buttonsBuffer.remove(i);
                        removeFromClickPositionsAndRegions(i);
                        saveToSharedPreferences();
                    }

                }
            }
            return false;
        }
    };

    public SwipeHelper(Context context, RecyclerView recyclerView) {
        super(0, ItemTouchHelper.LEFT);
        this.recyclerView = recyclerView;
        this.buttons = new ArrayList<>();
        this.gestureDetector = new GestureDetector(context, gestureListener);
        this.recyclerView.setOnTouchListener(onTouchListener);
        this.buttonsBuffer = new HashMap<>();
        this.prefManagerList = new SharedPreferencesListManager();
        this.openPositions = new ArrayList<>();

        this.clickPositionsAndRegions = new HashMap<>();
        this.staticStartingPositions = new HashMap<>();

        this.buttonRegions = new HashMap<>();

        recoverQueue = new LinkedList<Integer>() {
            @Override
            public boolean add(Integer o) {
                if (contains(o))
                    return false;
                else
                    return super.add(o);
            }
        };
        attachSwipe(context);
    }


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

        return false;
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();

        if (swipedPos != pos) {
            recoverQueue.add(swipedPos);
        }
        swipedPos = pos;


        if (buttonsBuffer.containsKey(swipedPos)) {
            buttons = buttonsBuffer.get(pos);
        } else {
            buttons.clear();
        }

        buttonsBuffer.clear();

        swipeThreshold = 0.5f * buttons.size() * BUTTON_WIDTH;
        recoverSwipedItem();
    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return swipeThreshold;
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return 0.1f * defaultValue;
    }

    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        return 5.0f * defaultValue;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        int pos = viewHolder.getAdapterPosition();
        int margin;
        float translationX = dX;
        View itemView = viewHolder.itemView;
        openPositions = getOpenPositionsFromSharedPreferences();
        if (pos < 0) {
            swipedPos = pos;
            return;
        }

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE || isCurrentlyActive) {
            if (dX < 0) {

                openPositions = getOpenPositionsFromSharedPreferences();
                List<UnderlayButton> buffer = new ArrayList<>();
                if (!openPositions.contains(pos)) {
                    openPositions.add(pos);
                    saveToSharedPreferences();
                }
                if (!buttonsBuffer.containsKey(pos)) {
                    instantiateUnderlayButton(viewHolder, buffer);

                    openPositions = getOpenPositionsFromSharedPreferences();
                    for (int i : openPositions)
                        buttonsBuffer.put(i, buffer);
                } else {
                    buffer = buttonsBuffer.get(pos);
                }

                translationX = dX * buffer.size() * BUTTON_WIDTH / itemView.getWidth();
                // Sets a margin because on our view (CardView) we've set a margin so the items won't be collided.
                // This margin then goes to the dimensions of the rectange that we draw.
                margin = buffer.get(0).getButtonMargin();
                drawButtons(c, itemView, buffer, pos, translationX, mContext, margin);
            } else { // Close Swipe Action
                if (openPositions.contains(pos)) {
                    //TODO Make a method and use it in any place that we use those three lines
                    openPositions.remove(openPositions.indexOf(pos));
                    buttonsBuffer.remove(pos);
                    removeFromClickPositionsAndRegions(pos);
                    saveToSharedPreferences();
                }
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive);
    }

    private synchronized void recoverSwipedItem() {
        while (!recoverQueue.isEmpty()) {
            //Remove closed buttons
            recoverQueue.poll();
            //buttonsBuffer.clear();
            //recyclerView.getAdapter();
            //openPositions.clear();

        }
    }


    private void drawButtons(Canvas c, View itemView, List<UnderlayButton> buffer, int pos, float dX, Context mContext, int margin) {
        float right = itemView.getRight();
        float dButtonWidth = (-1) * dX / buffer.size();

        for (UnderlayButton button : buffer) {
            float left = right - dButtonWidth;
            button.onDraw(
                    c,
                    new RectF(
                            left,
                            itemView.getTop() + margin,
                            right,
                            itemView.getBottom() - margin
                    ),
                    pos,
                    mContext,
                    buffer.indexOf(button) // Index of buffer, we need it to determine which button we are drawing and later on, clicking on,
                    // could be either delete or brew
            );
            right = left;
        }
    }

    public void attachSwipe(Context ctx) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        this.mContext = ctx;
        saveToSharedPreferences();
    }

    public abstract void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons);

    public class UnderlayButton {
        private String text;
        private int imageResId;
        private int color;
        private int pos;
        private int buttonMargin;
        private RectF clickRegion;
        private UnderlayButtonClickListener clickListener;


        public UnderlayButton(String text, int imageResId, int color, UnderlayButtonClickListener clickListener, int buttonMargin) {
            this.text = text;
            this.imageResId = imageResId;
            this.color = color;
            this.clickListener = clickListener;
            this.buttonMargin = buttonMargin;

            buttonRegions = new HashMap<>();
        }

        public int getButtonMargin() {
            return buttonMargin;
        }

        public UnderlayButton(String text, int imageResId, int color, UnderlayButtonClickListener clickListener) {
            this.text = text;
            this.imageResId = imageResId;
            this.color = color;
            this.clickListener = clickListener;
        }


        public boolean onClick(float x, float y, int buttonIndex) {

            for (Map.Entry<Integer, Map<Integer, RectF>> entry : clickPositionsAndRegions.entrySet())
                // This is how we access the data
                // entry.getValue() return the values of the clickPositionsAndRegions HashMap
                // entry.getValue.get() returns a rectangle, that is the coordinates of the button
                // buttonIndex is either 0 or 1.
                // If it's 0, It means we clicked the "DELETE" Button. If it's 1, It's  the "BREW" Button.
                if (entry.getValue().containsKey(buttonIndex) &&
                        entry.getValue().get(buttonIndex).contains(x, y) &&
                        entry.getValue().get(buttonIndex) != null) {

                    clickListener.onClick(entry.getKey());
                    return true;
                }

            return false;
        }

        public void onDraw(Canvas c, RectF rect, int pos, Context mContext, int buttonIndex) {
            Paint p = new Paint();

            // Draw background
            p.setColor(color);
            c.drawRect(rect, p);

            // Draw Text
            p.setColor(Color.WHITE);
            float scaledSizeInPixels = TEXT_SIZE * mContext.getResources().getDisplayMetrics().scaledDensity;
            p.setTextSize(scaledSizeInPixels);
            Rect r = new Rect();
            float cHeight = rect.height();
            float cWidth = rect.width();
            p.setTextAlign(Paint.Align.LEFT);
            p.getTextBounds(text, 0, text.length(), r);
            float x = cWidth / 2f - r.width() / 2f - r.left;
            float y = cHeight / 2f + r.height() / 2f - r.bottom;
            c.drawText(text, rect.left + x, rect.top + y - 10, p);

            clickRegion = rect;

            buttonRegions.put(buttonIndex, clickRegion);

            // Add as a key, the position of the item view (pos)
            // and as a value a HashMap that has as
            // a key the index of the button (index = 0 -> Delete Button, index = 1 -> Brew Button) and as
            // a value the coordinates of the rectange that is generated.
            // We access this data when we click on each button
            // Check UnderlayButton's onClick method
            if (!clickPositionsAndRegions.containsKey(pos)) {
                clickPositionsAndRegions.put(pos, buttonRegions);
                staticStartingPositions.put(pos, buttonRegions);
            } else {
                //FIXME find difference and then replace top and bottom.
                float offSetDifference = clickRegion.top - staticStartingPositions.get(pos).get(0).top;

                final RectF deleteButton = clickPositionsAndRegions.get(pos).get(0);
                final RectF brewButton = clickPositionsAndRegions.get(pos).get(1);

                final float staticTop = staticStartingPositions.get(pos).get(0).top;
                final float staticBot = staticStartingPositions.get(pos).get(0).bottom;

                deleteButton.set(deleteButton.left, staticTop + offSetDifference, deleteButton.right, staticBot + offSetDifference);
                brewButton.set(brewButton.left, staticTop + offSetDifference, brewButton.right, staticBot + offSetDifference);
            }

            this.pos = pos;
        }
    }


    public interface UnderlayButtonClickListener {
        void onClick(int pos);
    }


    private void saveToSharedPreferences() {
        prefManagerList.saveArrayList(this.openPositions, "openPositions", this.mContext);
    }

    private ArrayList<Integer> getOpenPositionsFromSharedPreferences() {
        return prefManagerList.getArrayList("openPositions", this.mContext);
    }

    public void removeFromClickPositionsAndRegions(int position) {
        clickPositionsAndRegions.remove(position);
    }
}