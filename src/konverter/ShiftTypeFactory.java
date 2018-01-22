package konverter;


public class ShiftTypeFactory implements IShiftTypeBuilder {

    public ShiftTypeFactory(){}

    public ShiftType getShift(String selector){

        switch (selector) {
            case "AFW":
                return new ShiftType(8,462,"");
            case "ASD":
                return new ShiftType(8,1,"");
            case "ASK":
                return new ShiftType(8,1,"");
            case "ASR":
                return new ShiftType(8,1,"");
            case "B":
                return new ShiftType(8,1,"");
            case "BA":
                return new ShiftType(8,120,"");
            case "BR":
                return new ShiftType(8,1,"");
            case "D":
                return new ShiftType(6,1440, "");
            case "DB":
                return new ShiftType(7,120,"");
            case "DP":
            case "+D,DP":
                return new ShiftType(8,480,"");
            case "FB":
                return new ShiftType(8,480,"");
            case "FD":
                return new ShiftType(8,480,"");
            case "FFB":
                return new ShiftType(8,0,"");
            case "HYR":
                return new ShiftType(8,1,"");
            case "KFZ":
                return new ShiftType(8,1,"");
            case "KLI":
                return new ShiftType(8,442,"");
            case "KOP":
            case "+D,KOP":
                return new ShiftType(7,660, "Osterode");
            case "KSP":
            case "+D,KSP":
                return new ShiftType(7,660,"Bad Sachsa");
            case "MH":
            case "+D,MH":
                return new ShiftType(10,600, "Herzberg");
            case "MPG":
                return new ShiftType(8,1, "Herzberg");
            case "NH":
            case "+D,NH":
                return new ShiftType(19,720, "Herzberg");
            case "NN":
            case "+D,NN":
                return new ShiftType(19,720, "Osterode");
            case "NO":
            case "+D,NO":
                return new ShiftType(19,720, "Osterode");
            case "NS":
            case "+D,NS":
                return new ShiftType(19,720,"Bad Sachsa");
            case "NT":
            case "+D,NT":
            case "+D,NT,DP":
                return new ShiftType(7,720,"Osterode");
            case "QM":
                return  new ShiftType(8,360,"");
            case "RDL":
                return  new ShiftType(8,1,"");
            case "RS":
                return  new ShiftType(8,462,"");
            case "RW":
                return  new ShiftType(8,462,"");
            case "RWL":
                return  new ShiftType(8,1,"");
            case "TH":
            case "+D,TH":
                return new ShiftType(7, 720, "Herzberg");
            case "TO":
            case "+D,TO":
                return new ShiftType(7,720, "Osterode");
            case "TS":
            case "+D,TS":
                return new ShiftType(7,720,"Bad Sachsa");
            case "-":
            case "-,-":
            case "FF":
            case "K":
            case "U":
            case "EZ":
            case "BV":
            case "UU":
            case "FZÜ":
                return null;
            default:
                throw new IllegalArgumentException("Diese Schicht existiert nicht: " + selector);
        }
    }
}
