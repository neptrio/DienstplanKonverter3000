package konverter;


import java.util.ArrayList;

public class Employee {
    protected String name;
    protected ArrayList<ShiftDate> dienste;

    public Employee(String name, ArrayList<ShiftDate> dienste){
        this.name = name;
        this.dienste = dienste;
    }

    public String getName(){
        return this.name;
    }

    public ArrayList<ShiftDate> getDienste(){
        return this.dienste;
    }
}
