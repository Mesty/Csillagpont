package hu.reformatus.csillagpont.model.programs.databases;

import java.util.Date;
import java.util.List;

public class EventObjects {

    private int id;

    private String title;

    private String description;

    public enum Category{
        NONE,
        HIGHSCHOOL,
        CULTURAL,
        ECO,
        UNIVERSITY,
        SENSITIZING,
        RELATIONSHIP,
        SPORT;

        public static Category getCategory(String s){
            return getCategory(Integer.parseInt(s));
        }

        public static Category getCategory(int x){
            switch (x){
                case 0: return NONE;
                case 1: return HIGHSCHOOL;
                case 2: return CULTURAL;
                case 3: return ECO;
                case 4: return UNIVERSITY;
                case 5: return SENSITIZING;
                case 6: return RELATIONSHIP;
                case 7: return SPORT;
                default:return NONE;
            }
        }
    }

    private Category category;

    private Boolean isWheelAccessible;

    private Boolean isOnlyRegistered;

    private String location;

    private Date date;

    private Date end;

    public EventObjects(String title, String description, Category category,
                        Boolean isWheelAccessible, Boolean isOnlyRegistered, String location,
                        Date date, Date end) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.isWheelAccessible = isWheelAccessible;
        this.isOnlyRegistered = isOnlyRegistered;
        this.location = location;
        this.date = date;
        this.end = end;
    }

    public EventObjects(int id, String title, String description, Category category,
                        Boolean isWheelAccessible, Boolean isOnlyRegistered, String location,
                        Date date, Date end) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.category = category;
        this.isWheelAccessible = isWheelAccessible;
        this.isOnlyRegistered = isOnlyRegistered;
        this.location = location;
        this.id = id;
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription(){
        return description;
    }

    public Category getCategory(){
        return category;
    }

    public String getLocation(){
        return location;
    }

    public Date getStartDate() {
        return date;
    }

    public Date getEndDate(){
        return end;
    }

    public int getId() {
        return id;
    }

    public static int getCollideLevel(final List<EventObjects> dailyEvent, final EventObjects eObject, final boolean total){
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
    public static int getCollideLevel(List<EventObjects> dailyEvent, EventObjects eObject){
        return getCollideLevel(dailyEvent, eObject, false);
    }

}