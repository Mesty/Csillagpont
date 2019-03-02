package hu.reformatus.csillagpont.model.programs.databases;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import hu.reformatus.csillagpont.model.programs.databases.EventObjects.Category;

public class DatabaseQuery extends DatabaseObject {

    private static final String TAG = Database.class.getSimpleName();

    public DatabaseQuery(Context context) {
        super(context);
    }

    public List<EventObjects> getAllFutureEvents(Date mDate){
        Calendar calDate = Calendar.getInstance();
        Calendar dDate = Calendar.getInstance();
        calDate.setTime(mDate);

        int calDay = calDate.get(Calendar.DAY_OF_MONTH);
        int calMonth = calDate.get(Calendar.MONTH) + 1;
        int calYear = calDate.get(Calendar.YEAR);

        List<EventObjects> events = new ArrayList<>();
        String query = "select * from events";
        Cursor cursor = this.getDbConnection().rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String cat = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                if(cat == null || cat.equals(""))
                    cat = "0";
                Category category = Category.fromInteger(Integer.parseInt(cat));
                String location = cursor.getString(cursor.getColumnIndexOrThrow("location"));
                String startDate = cursor.getString(cursor.getColumnIndexOrThrow("start_time"));
                String endDate = cursor.getString(cursor.getColumnIndexOrThrow("end_time"));
                //convert start date to date object

                //if(startDate != null && !startDate.isEmpty() &&
                //endDate != null && !endDate.isEmpty()) {
                    Log.d("TAG", startDate);
                    Date reminderDate = convertStringToDate(startDate);
                    //if(reminderDate != null) {
                        Date end = convertStringToDate(endDate);
                        Log.d("TAG2", reminderDate.toString());
                        dDate.setTime(reminderDate);
                        int dDay = dDate.get(Calendar.DAY_OF_MONTH);
                        int dMonth = dDate.get(Calendar.MONTH) + 1;
                        int dYear = dDate.get(Calendar.YEAR);

                        if (calDay == dDay && calMonth == dMonth && calYear == dYear) {
                            events.add(new EventObjects(id, title, description, category, location,
                                    reminderDate, end));
                        }
                    //}
                //}
            }while (cursor.moveToNext());
        }
        cursor.close();
        return events;
    }

    private Date convertStringToDate(String dateInString){
        DateFormat format = new SimpleDateFormat("yyyy.MM.dd. HH:mm", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}