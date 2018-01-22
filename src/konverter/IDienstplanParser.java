package konverter;

import java.io.File;
import java.util.ArrayList;

public interface IDienstplanParser {
    public ArrayList<Employee> parse(File dp);
}
