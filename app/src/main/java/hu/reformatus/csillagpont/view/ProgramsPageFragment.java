package hu.reformatus.csillagpont.view;


import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hu.reformatus.csillagpont.R;
import hu.reformatus.csillagpont.model.programs.databases.DatabaseQuery;
import hu.reformatus.csillagpont.model.programs.databases.EventObjects;

import static hu.reformatus.csillagpont.model.programs.databases.EventObjects.getCollideLevel;

public class ProgramsPageFragment extends Fragment {
    private static final String TAG = ProgramsActivity.class.getSimpleName();
    private Calendar cal = Calendar.getInstance();
    private RelativeLayout mLayout;
    private DatabaseQuery mQuery;
    List<EventObjects> dailyEvent;
    private static final int StartingHour = 6;
    private static final int screenWidth = 600;
    private static final int leftMargin = 24;
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static ProgramsPageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ProgramsPageFragment fragment = new ProgramsPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_programs, container, false);
        //Set calendar date
        cal.set(Calendar.MONTH, Calendar.JULY); //TODO: set correctly
        cal.set(Calendar.DAY_OF_MONTH, 23+mPage);
        //Create database connection
        mQuery = new DatabaseQuery(getActivity().getApplicationContext());
        mLayout = view.findViewById(R.id.left_event_column);
        displayDailyEvents();
        mQuery.closeDbConnection();
        return view;
    }

    private void displayDailyEvents() {
        Date calendarDate = cal.getTime();
        dailyEvent = mQuery.getAllFutureEvents(calendarDate);
        for (EventObjects eObject : dailyEvent) {
            Date eventDate = eObject.getStartDate();
            Date endDate = eObject.getEndDate();
            String eventMessage = eObject.getTitle();
            int eventBlockHeight = getEventTimeFrame(eventDate, endDate);
            int eventCollideLevel = getCollideLevel(dailyEvent, eObject);
            int eventCollideLevelTotal = getCollideLevel(dailyEvent, eObject, true);
            int id = eObject.getId();
            displayEventSection(id, eventDate, eventBlockHeight, eventCollideLevel,
                    eventCollideLevelTotal, eventMessage);
        }
    }

    private int getEventTimeFrame(Date start, Date end) {
        long timeDifference = end.getTime() - start.getTime();
        Calendar mCal = Calendar.getInstance();
        mCal.setTimeInMillis(timeDifference);
        int hours = mCal.get(Calendar.HOUR) - 1;
        int minutes = mCal.get(Calendar.MINUTE);
        return (hours * 60) + minutes;
    }

    private void displayEventSection(final int id, Date eventDate, int height, int CollideLevel,
                                     int CollideLevelTotal, String message) {
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        String displayValue = timeFormatter.format(eventDate);
        String[] hourMinutes = displayValue.split(":");
        int hours = Integer.parseInt(hourMinutes[0]) - StartingHour;
        int minutes = Integer.parseInt(hourMinutes[1]);
        int topViewMargin = (hours * 60) + minutes;
        createEventView(id, topViewMargin,
                leftMargin + screenWidth / CollideLevelTotal * CollideLevel,
                CollideLevelTotal, height, message);
    }

    private void createEventView(final int id, int topMargin, int leftMargin, int width, int height,
                                 final String message) {
        final TextView mEventView = new TextView(getActivity().getApplicationContext());
        RelativeLayout.LayoutParams lParam = new RelativeLayout.LayoutParams(screenWidth / width,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lParam.topMargin = topMargin * 2;
        lParam.leftMargin = leftMargin;
        mEventView.setLayoutParams(lParam);
        mEventView.setPadding(24, 0, 24, 0);
        mEventView.setHeight(height * 2);
        mEventView.setGravity(0x11);
        mEventView.setTextColor(Color.parseColor("#ffffff"));
        mEventView.setText(message);
        mEventView.setTag("event");
        Drawable shape = getResources().getDrawable(R.drawable.shape);
        mEventView.setBackground(shape);
        TypedArray colorArray = getResources().obtainTypedArray(R.array.chart);
        mEventView.getBackground().setColorFilter(
                colorArray.getColor((id - 1) % colorArray.length(), 0),
                PorterDuff.Mode.DARKEN);
        colorArray.recycle();
        mLayout.addView(mEventView);
        mEventView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ProgramDetailsActivity.class);
                intent.putExtra("EVENT_ID", id);
                startActivity(intent);
            }
        });
    }
}
