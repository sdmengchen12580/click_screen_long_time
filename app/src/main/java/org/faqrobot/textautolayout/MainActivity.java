package org.faqrobot.textautolayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    void 注释 ()
    {
        /**
         * Android提供了GestureDetector类来处理一些常用的手势操作，比如说 onLongPress,onFling 等。
         * 但这里不使用GestureDetector，而是直接在自定义View重写的onTouchEvent中进行处理。
         * 欲实现的效果是：当手机按住屏幕时，如果在指定的时间内没有移动（如500毫秒）,那么进入长按模式，此时手指在屏幕上移动都算作长按模式。
         * 如果手机按住屏幕就立马移动，那么就算作移动模式。
         * MotionEvent 类提供了记录当前坐标的函数（getX(),getY()）和当前事件产生的时间的函数（getEventTime()）以及按下时间（getDowntime()）。
         * MotionEvent同时也提供了当前的操作类型，按下（ACTION_DOWN）、 移动 （ACTION_MOVE）、弹起 （ACTION_UP）。
         * 有了这些参数，我们便可以轻易的实现想要的效果了。
         *大概思路如下：在按下时记录x,y坐标以及按下时间，当第一次移动的时候获取移动的时间，如果大于指定的长按时间，那么进入长按模式，
         * 否则就是普通的移动模式。
         * 很容易，在模拟器里面实现了这个效果，但是当在真机里面运行时，却无法实现这样的效果。
         * 原来模拟器点击的时候能够保证在不移动鼠标的情况下不触发ACTION_MOVE，
         * 但是真机却很敏感，几乎在ACTION_DOWN后的几毫秒之后就立马不停的ACTION_MOVE了。
         * 想了一下，其实只要稍微变通下变可以在真机上也实现相同的效果了。
         * 那就是判断ACTION_MOVE后的坐标和ACTION_DOWN的坐标的偏移值是否小于我们指定的偏移像素，如果在指定值内，那么认为没有移动。
         * */
    }

    private long lastDownTime = 0;
    private long thisEventTime = 0;
    private int lastX= 0;
    private int lastY= 0;
    private int thisX= 0;
    private int thisY= 0;
    private long longPressTime= 2000;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                /**获取按下时的x、y*/
                lastX= (int) event.getX();
                lastY= (int) event.getY();
                lastDownTime = event.getDownTime();
                break;
            case MotionEvent.ACTION_MOVE:
                thisX= (int) event.getX();
                thisY= (int) event.getY();
                thisEventTime = event.getEventTime();
                if(isLongPressed(lastX,lastY,thisX,thisY,lastDownTime,thisEventTime,longPressTime)){
                    findViewById(R.id.button).setVisibility(View.VISIBLE);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 判断是否有长按动作发生
     * @param lastX 按下时X坐标
     * @param lastY 按下时Y坐标
     * @param thisX 移动时X坐标
     * @param thisY 移动时Y坐标
     * @param lastDownTime 按下时间
     * @param thisEventTime 移动时间
     * @param longPressTime 判断长按时间的阀值
     */
    static boolean isLongPressed(float lastX, float lastY, float thisX,
                                 float thisY, long lastDownTime, long thisEventTime,
                                 long longPressTime) {
        float offsetX = Math.abs(thisX - lastX);
        float offsetY = Math.abs(thisY - lastY);
        long intervalTime = thisEventTime - lastDownTime;
        /**长按模式*/
        if (offsetX <= 10 && offsetY <= 10 && intervalTime >= longPressTime) {
            return true;
        }
        return false;
    }


}
