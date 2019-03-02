package hu.reformatus.csillagpont.model.programs.databases;

import java.util.Date;

public class EventObjects {

    private int id;

    private String title;

    private String description;

    public enum Category{
        HIGHSCOOL,
        CULTURAL,
        ECO,
        UNIVERSITY,
        SENSITIZING,
        NOT_WHEELCHAIR_ACCESSIBLE,
        RELATIONSHIP,
        SPORT,
        FOR_REGISTRATED_MEMBERS,
        NONE;

        public static Category fromInteger(int x){
            switch (x){
                case 0: return NONE;
                case 1: return HIGHSCOOL;
                case 2: return CULTURAL;
                case 3: return ECO;
                case 4: return UNIVERSITY;
                case 5: return SENSITIZING;
                case 6: return NOT_WHEELCHAIR_ACCESSIBLE;
                case 7: return RELATIONSHIP;
                case 8: return SPORT;
                case 9: return FOR_REGISTRATED_MEMBERS;
                default:return NONE;
            }
        }
    }
    private Category category;
    private String location;

    private Date date;

    private Date end;

    public EventObjects(String title, String description, Category category, String location, Date date, Date end) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
        this.date = date;
        this.end = end;
    }

    public EventObjects(int id, String title, String description, Category category, String location, Date date, Date end) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
        this.id = id;
        this.end = end;
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

    public String getTitle() {
        return title;
    }
}