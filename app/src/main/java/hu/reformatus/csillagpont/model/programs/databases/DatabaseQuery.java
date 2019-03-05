package hu.reformatus.csillagpont.model.programs.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import hu.reformatus.csillagpont.model.programs.databases.EventObjects.Category;

import static android.content.ContentValues.TAG;

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
                if(cat == null || cat.equals("") || cat.equals("null"))
                    cat = "0";
                Category category = Category.getCategory(Integer.parseInt(cat));
                String location = cursor.getString(cursor.getColumnIndexOrThrow("location"));
                String startDate = cursor.getString(cursor.getColumnIndexOrThrow("start_time"));
                String endDate = cursor.getString(cursor.getColumnIndexOrThrow("end_time"));
                Boolean isWheelAccessible = Boolean.valueOf(cursor.getString(
                        cursor.getColumnIndexOrThrow("wheel_accessible")));
                Boolean isOnlyRegistered = Boolean.valueOf(cursor.getString(
                        cursor.getColumnIndexOrThrow("for_registered")));
                    Log.d("TAG", startDate);
                    Date reminderDate = convertStringToDate(startDate);
                    Date end = convertStringToDate(endDate);
                    Log.d("TAG2", reminderDate.toString());
                    dDate.setTime(reminderDate);
                    int dDay = dDate.get(Calendar.DAY_OF_MONTH);
                    int dMonth = dDate.get(Calendar.MONTH) + 1;
                    int dYear = dDate.get(Calendar.YEAR);

                    if (calDay == dDay && calMonth == dMonth && calYear == dYear) {
                        events.add(new EventObjects(id, title, description, category ,isWheelAccessible,
                                isOnlyRegistered, location, reminderDate, end));
                    }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return events;
    }

    public EventObjects getEventById(int id){
        String query = "select * from events where id="+String.valueOf(id);
        Cursor cursor = this.getDbConnection().rawQuery(query, null);
        EventObjects event;
        if(cursor.moveToFirst()){
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            String cat = cursor.getString(cursor.getColumnIndexOrThrow("category"));
            if(cat == null || cat.equals("") || cat.equals("null"))
                cat = "0";
            Category category = Category.getCategory(cat);
            String location = cursor.getString(cursor.getColumnIndexOrThrow("location"));
            String startDate = cursor.getString(cursor.getColumnIndexOrThrow("start_time"));
            String endDate = cursor.getString(cursor.getColumnIndexOrThrow("end_time"));
            Boolean isWheelAccessible = Boolean.valueOf(cursor.getString(
                    cursor.getColumnIndexOrThrow("wheel_accessible")));
            Boolean isOnlyRegistered = Boolean.valueOf(cursor.getString(
                    cursor.getColumnIndexOrThrow("for_registered")));

            Date reminderDate = convertStringToDate(startDate);
            Date end = convertStringToDate(endDate);
            event = new EventObjects(id, title, description, category ,isWheelAccessible,
                    isOnlyRegistered, location, reminderDate, end);
        }
        else
            return null;
        cursor.close();
        this.closeDbConnection();
        return event;
    }

    public void updateDatabase(JSONArray jArr){
        this.getDbConnection().execSQL("DELETE FROM events;");
        this.getDbConnection().execSQL("VACUUM");
        this.getDbConnection().execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = 'events'");


        try{
            for(int i = 0; i< jArr.length(); i++){
                JSONObject jsonObj = jArr.getJSONObject(i);
                String title = jsonObj.getString("title");
                String description = jsonObj.getString("description");
                String category = jsonObj.getString("category");
                String location = jsonObj.getString("location");
                String start_time = jsonObj.getString("start_time");
                String end_time = jsonObj.getString("end_time");
                String wheel_accessible = jsonObj.getString("wheel_accessible");
                String for_registered = jsonObj.getString("for_registered");

                Log.d(TAG, title+", "+description+", "+category+", "+location+", "+
                        start_time+", "+end_time+", "+wheel_accessible+", "+for_registered);
                /*query = "INSERT INTO events VALUES (null,'"+title+"', '"+description+"', '"+
                        category+"', '"+location+"', '"+start_time+"', '"+end_time+"', '"+
                        wheel_accessible+"', '"+for_registered+"');";
                this.getDbConnection().execSQL(query);
                this.getDbConnection().insert("events",)*/
                ContentValues insertValues = new ContentValues();
                insertValues.put("title", title);
                insertValues.put("description", description);
                insertValues.put("category", category);
                insertValues.put("location", location);
                insertValues.put("start_time", start_time);
                insertValues.put("end_time", end_time);
                insertValues.put("wheel_accessible", wheel_accessible);
                insertValues.put("for_registered", for_registered);
                this.getDbConnection().insert("events", null, insertValues);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
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