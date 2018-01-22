package sample;

public class ShiftDate {

    protected int day;
    protected String name;

    public ShiftDate(int day, String name){
        this.day = day;
        this.name = name;
    }

    public int getDay(){
        return this.day;
    }

    public String getName() {
        return name;
    }
}
