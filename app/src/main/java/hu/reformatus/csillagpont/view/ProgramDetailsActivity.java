package hu.reformatus.csillagpont.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import hu.reformatus.csillagpont.R;
import hu.reformatus.csillagpont.model.programs.databases.DatabaseQuery;
import hu.reformatus.csillagpont.model.programs.databases.EventObjects;

public class ProgramDetailsActivity extends AppCompatActivity {
    private static final String TAG = ProgramsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.star_grey));
        fab.setOnClickListener(new View.OnClickListener() {
            boolean toogle = false;
            @Override
            public void onClick(View view) {
                if(toogle) {
                    ((FloatingActionButton) view).setImageDrawable(getResources()
                            .getDrawable(R.drawable.star_grey));

                    Snackbar.make(view, R.string.removed_from_favorites, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else{
                    ((FloatingActionButton)view).setImageDrawable(getResources()
                            .getDrawable(R.drawable.star_yellow));
                    Snackbar.make(view, R.string.add_to_favorites, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                toogle = !toogle;

            }
        });
        DatabaseQuery mQuery = new DatabaseQuery(this);
        Intent intent = getIntent();
        int eventId = intent.getIntExtra("EVENT_ID", -1);

        if(eventId >= 0){
            EventObjects event = mQuery.getEventById(eventId);
            TextView title = findViewById(R.id.tvPdTitle);
            title.setText(event.getTitle());
            TextView description = findViewById(R.id.tvPdDescriptions);
            description.setText(event.getDescription());
            TextView category = findViewById(R.id.tvPdCategory);
            category.setText(event.getCategory().toString());
            TextView location = findViewById(R.id.tvPdLocation);
            location.setText(event.getLocation());
            SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy.MM.dd. HH:mm");
            TextView startTime = findViewById(R.id.tvPdStartTime);
            startTime.setText(timeFormatter.format(event.getStartDate()));
            TextView endTime = findViewById(R.id.tvPdEndTime);
            endTime.setText(timeFormatter.format(event.getEndDate()));
        }
        else{
            Log.e(TAG, "Unknown Id in database!");
        }
    }

}
