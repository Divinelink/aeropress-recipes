package aeropresscipe.divinelink.aeropress.features;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import aeropresscipe.divinelink.aeropress.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public abstract class SwipeHelper extends ItemTouchHelper.SimpleCallback {

    public static final int BUTTON_WIDTH = 150;
    public static final int TEXT_SIZE = 14;


    private RecyclerView recyclerView;
    private List<UnderlayButton> buttons;
    private GestureDetector gestureDetector;
    private int swipedPos = -1;
    private float swipeThreshold = 0.5f;
    private Map<Integer, List<UnderlayButton>> buttonsBuffer;
    private Queue<Integer> recoverQueue;

    private ArrayList<Integer> openPositions;


    private Context mContext;
    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            for (UnderlayButton button : buttons) {
                if (button.onClick(e.getX(), e.getY()))
                    break;
            }
            return true;
        }
    };


    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent e) {
            if (swipedPos < 0) return false;
            Point point = new Point((int) e.getRawX(), (int) e.getRawY());
            //We should reduce the amount of openPositions each time we remove an item.
            //Figure out how to do that
            for (int i : openPositions) {
                RecyclerView.ViewHolder swipedViewHolder = recyclerView.findViewHolderForAdapterPosition(i);
                View swipedItem = swipedViewHolder.itemView;
                Rect rect = new Rect();
                swipedItem.getGlobalVisibleRect(rect);

                if (e.getAction() == MotionEvent.ACTION_DOWN || e.getAction() == MotionEvent.ACTION_UP || e.getAction() == MotionEvent.ACTION_MOVE) {
                    if (rect.top < point.y && rect.bottom > point.y)
                        gestureDetector.onTouchEvent(e);
                    else {
              //          recoverQueue.add(swipedPos);
                //        swipedPos = -1;
                        recoverSwipedItem();
                        //    openPositions.remove(openPositions.indexOf(i));
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
        buttonsBuffer = new HashMap<>();
        this.openPositions = new ArrayList<>();
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

    // Figure out how to remove old positions when moving recyclerView

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
        /*
        if (!openPositions.contains(pos))
            openPositions.add(pos);

         */
        swipedPos = pos;


        if (buttonsBuffer.containsKey(swipedPos)) {
            buttons = buttonsBuffer.get(pos);
        } else {

            buttons.clear();
            //  openPositions.remove(openPositions.indexOf(pos));
        }

        if (openPositions.contains(pos)) {
            Log.d("CONTAINS THAT ", Integer.toString(pos));
//           openPositions.remove(pos);
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

        if (pos < 0) {
            swipedPos = pos;
            return;
        }
//Maybe here
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                List<UnderlayButton> buffer = new ArrayList<>();

                if (!openPositions.contains(pos))
                    openPositions.add(pos);

                if (!buttonsBuffer.containsKey(pos)) {
                    instantiateUnderlayButton(viewHolder, buffer);
                    //FIXME this is needed but make it dynamic
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
            } else {
                if (openPositions.contains(pos)) {
                    openPositions.remove(openPositions.indexOf(pos));
                    buttonsBuffer.remove(pos);
                }
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive);
    }

    private synchronized void recoverSwipedItem() {
        while (!recoverQueue.isEmpty()) {
            int pos = recoverQueue.poll();

            //openPositions.remove(openPositions.indexOf(pos));
            //Remove closed buttons
            recoverQueue.poll();
            //   if (pos > -1) {
            //    recyclerView.getAdapter().notifyItemChanged(pos); //FIXME check this here for swiping another item
            recyclerView.getAdapter();

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
                    mContext
            );
            right = left;
        }
    }

    public void attachSwipe(Context ctx) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        this.mContext = ctx;
    }

    public abstract void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons);

    public static class UnderlayButton {
        private String text;
        private int imageResId;
        private int color;
        private int pos;
        private int buttonMargin;
        private RectF clickRegion;
        private UnderlayButtonClickListener clickListener;

        private ArrayList<RectF> clickRegions = new ArrayList<>();
        private ArrayList<Integer> clickPositions = new ArrayList<>();


        public UnderlayButton(String text, int imageResId, int color, UnderlayButtonClickListener clickListener, int buttonMargin) {
            this.text = text;
            this.imageResId = imageResId;
            this.color = color;
            this.clickListener = clickListener;
            this.buttonMargin = buttonMargin;
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


        public boolean onClick(float x, float y) {
            for (RectF i : clickRegions) {
                if (i != null && i.contains(x, y)) {
                    clickListener.onClick(clickPositions.get(clickRegions.indexOf(i)));
                    return true;
                }
            }
            /*
            if (clickRegion != null && clickRegion.contains(x, y)) {
                clickListener.onClick(pos);
                return true;
            }
            */

            return false;
        }

        public void onDraw(Canvas c, RectF rect, int pos, Context mContext) {
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
            clickRegions.add(clickRegion);
            clickPositions.add(pos);
            this.pos = pos;
        }
    }

    public interface UnderlayButtonClickListener {
        void onClick(int pos);
    }

}