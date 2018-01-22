package konverter;

public class ShiftType {
    protected int start;
    protected int duration;
    protected String place;

    public ShiftType(int start, int duration, String place){
        this.start = start;
        this.duration = duration;
        this.place = place;
    }

    public int getDuration(){
        return this.duration;
    }

    public int getStartHour(){
        return this.start;
    }

    public String getPlace(){
        return this.place;
    }

}
