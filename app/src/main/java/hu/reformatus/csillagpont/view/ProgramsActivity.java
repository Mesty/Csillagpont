package hu.reformatus.csillagpont.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hu.reformatus.csillagpont.R;
import hu.reformatus.csillagpont.model.programs.databases.DatabaseQuery;
import hu.reformatus.csillagpont.model.programs.databases.EventObjects;


public class ProgramsActivity extends AppCompatActivity {
    private static final String TAG = ProgramsActivity.class.getSimpleName();
    private ImageView previousDay;
    private ImageView nextDay;
    private TextView currentDate;
    private Calendar cal = Calendar.getInstance();
    private DatabaseQuery mQuery;
    private RelativeLayout mLayout;
    private int eventIndex;
    List<EventObjects> dailyEvent;
    private static final int StartingHour = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programs);
        mQuery = new DatabaseQuery(this);
        mLayout = findViewById(R.id.left_event_column);
        eventIndex = mLayout.getChildCount();
        currentDate = findViewById(R.id.display_current_date);
        cal.set(Calendar.MONTH, Calendar.JULY);
        cal.set(Calendar.DAY_OF_MONTH, 23);
        currentDate.setText(displayDateInString(cal.getTime()));
        displayDailyEvents();
        previousDay = findViewById(R.id.previous_day);
        nextDay = findViewById(R.id.next_day);
        previousDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousCalendarDate();
                setVisibilities();
            }
        });
        nextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextCalendarDate();
                setVisibilities();
            }
        });
    }

    private void setVisibilities(){
        if(cal.get(Calendar.DATE) == 23)
            previousDay.setVisibility(View.INVISIBLE);
        else
            previousDay.setVisibility(View.VISIBLE);
        if(cal.get(Calendar.DATE) == 27)
            nextDay.setVisibility(View.INVISIBLE);
        else
            nextDay.setVisibility(View.VISIBLE);
    }

    private void previousCalendarDate(){
        for(int i = 0; i < mLayout.getChildCount(); i++){
            Object a = mLayout.getChildAt(i).getTag();
            if(a != null && a.equals("event")) {
                mLayout.removeViewAt(i);
                i--;
            }
        }
        cal.add(Calendar.DAY_OF_MONTH, -1);
        currentDate.setText(displayDateInString(cal.getTime()));
        displayDailyEvents();
    }
    private void nextCalendarDate(){
        for(int i = 0; i < mLayout.getChildCount(); i++) {
            Object a = mLayout.getChildAt(i).getTag();
            if(a != null && a.equals("event")) {
                mLayout.removeViewAt(i);
                i--;
            }
        }
        cal.add(Calendar.DAY_OF_MONTH, 1);
        currentDate.setText(displayDateInString(cal.getTime()));
        displayDailyEvents();
    }
    private String displayDateInString(Date mDate){
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM d. EEEE");
        return formatter.format(mDate);
    }
    private void displayDailyEvents(){
        Date calendarDate = cal.getTime();
        dailyEvent = mQuery.getAllFutureEvents(calendarDate);
        for(EventObjects eObject : dailyEvent){
            Date eventDate = eObject.getStartDate();
            Date endDate = eObject.getEndDate();
            String eventMessage = eObject.getTitle();
            int eventBlockHeight = getEventTimeFrame(eventDate, endDate);
            Log.d(TAG, "Height " + eventBlockHeight);
            int eventCollideLevel = getCollideLevel(dailyEvent, eObject);
            int eventCollideLevelTotal = getCollideLevel(dailyEvent, eObject,true);
            displayEventSection(eventDate, eventBlockHeight, eventCollideLevel, eventCollideLevelTotal, eventMessage);
        }
    }
    private int getEventTimeFrame(Date start, Date end){
        long timeDifference = end.getTime() - start.getTime();
        Calendar mCal = Calendar.getInstance();
        mCal.setTimeInMillis(timeDifference);
        int hours = mCal.get(Calendar.HOUR)-1;
        int minutes = mCal.get(Calendar.MINUTE);
        return (hours * 60) + ((minutes * 60) / 100);
    }
    private void displayEventSection(Date eventDate, int height, int CollideLevel, int CollideLevelTotal,  String message){
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        String displayValue = timeFormatter.format(eventDate);
        String[]hourMinutes = displayValue.split(":");
        int hours = Integer.parseInt(hourMinutes[0])-StartingHour;
        int minutes = Integer.parseInt(hourMinutes[1]);
        Log.d(TAG, "Hour value " + hours);
        Log.d(TAG, "Minutes value " + minutes);
        int topViewMargin = (hours * 60) + ((minutes * 60) / 100);
        Log.d(TAG, "Margin top " + topViewMargin);
        //TODO: C type collision
        createEventView(topViewMargin,24 + 500/CollideLevelTotal * CollideLevel, CollideLevelTotal,  height, message);
    }
    private void createEventView(int topMargin, int leftMargin, int width, int height, final String message){
        final TextView mEventView = new TextView(ProgramsActivity.this);
        RelativeLayout.LayoutParams lParam = new RelativeLayout.LayoutParams(500/width, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lParam.topMargin = topMargin * 2;
        lParam.leftMargin = leftMargin;
        mEventView.setLayoutParams(lParam);
        mEventView.setPadding(24, 0, 24, 0);
        mEventView.setHeight(height * 2);
        mEventView.setGravity(0x11);
        mEventView.setTextColor(Color.parseColor("#ffffff"));
        mEventView.setText(message);
        mEventView.setBackgroundColor(Color.parseColor("#3F51B5"));
        mEventView.setTag("event");
        mLayout.addView(mEventView, eventIndex - 1);
        mEventView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(ProgramsActivity.this, "Clicked " + message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private int getCollideLevel(List<EventObjects> dailyEvent, EventObjects eObject, boolean total){
        int collideLevel = 0;
        for(EventObjects e : dailyEvent){
            if(!total && e.getId() == eObject.getId())
                break;
            if(
                    (e.getStartDate().compareTo(eObject.getStartDate()) >= 0 &&
                            e.getStartDate().compareTo(eObject.getEndDate()) <= 0) ||
                            (e.getStartDate().compareTo(eObject.getStartDate()) <= 0 &&
                                    e.getEndDate().compareTo(eObject.getStartDate()) >= 0)
            )
                collideLevel++;
        }
        return collideLevel;
    }
    private int getCollideLevel(List<EventObjects> dailyEvent, EventObjects eObject){
        return getCollideLevel(dailyEvent, eObject, false);
    }



}
