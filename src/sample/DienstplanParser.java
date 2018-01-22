package sample;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

public class DienstplanParser implements IDienstplanParser {

    public DienstplanParser(){}

    public ArrayList<Employee> parse(File dp){

        ArrayList<Employee> employees = new ArrayList<>();

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(dp);

            doc.getDocumentElement().normalize();

            NodeList employeesNodeList = this.getEmployeesNodeListFromDocument(doc);

            for(int i = 0; i < employeesNodeList.getLength(); i++) {

                Employee e = this.parseEmployee(employeesNodeList.item(i));
                employees.add(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return employees;
    }

    private NodeList getEmployeesNodeListFromDocument(Document d){

        NodeList employees = d.getElementsByTagName("Group");

        System.out.println(employees.getLength() + " Mitarbeiter gefunden!");

        return employees;
    }

    private int getDayNumFromElement(Element e){
        String original = e.getAttribute("FieldName");
        String tagFast = original.replace("{smdp1.ZEITBLOCK","");
        int dayNum = Integer.parseInt(tagFast.replaceAll("[^0-9]", ""));
        return dayNum;
    }

    private Employee parseEmployee(Node employee){

        NodeList nList = employee.getChildNodes();

        String name = "";

        String[] mainShifts = new String[31];
        String[] replacementShifts = new String[31];

        ArrayList<ShiftDate> finalShifts = new ArrayList<>();

        boolean firstDetailsNode = false;
        boolean secondDetailsNode = false;

        //find all detailNodes
        for(int i = 0; i < nList.getLength(); i++){
            if(nList.item(i).getNodeName() == "Details" && nList.item(i).getNodeType() == Node.ELEMENT_NODE){
                //set actual detailNode
                if(firstDetailsNode) {
                    secondDetailsNode = true;
                } else {
                    firstDetailsNode = true;
                }
                Element section = (Element) nList.item(i);
                NodeList fieldList = section.getElementsByTagName("Field");

                //parse all fields and find interesting data
                for(int field = 0; field < fieldList.getLength(); field++){
                    Element singleField = (Element) fieldList.item(field);
                    //find name
                    if(singleField.getAttribute("FieldName").equals("{smdp1.BEZPERSONAL}")){
                        name = singleField.getElementsByTagName("Value").item(0).getTextContent();
                    }
                    //parse all time block fields
                    if(singleField.getAttribute("FieldName").indexOf("ZEITBLOCK") > 0){

                        int dayNum = this.getDayNumFromElement(singleField);

                        String shiftName = singleField.getElementsByTagName("Value").item(0).getTextContent();

                        if(shiftName != null && !shiftName.isEmpty()){
                            if(secondDetailsNode){
                                replacementShifts[dayNum-1] = shiftName;
                            } else {
                                mainShifts[dayNum-1] = shiftName;
                            }
                        }
                    }
                }
            }
        }

        //check for replacement shifts
        for(int t = 0; t < replacementShifts.length; t++){
            if(replacementShifts[t] != null){
                mainShifts[t] = replacementShifts[t];
            }
        }

        //build final shifts
        for(int o = 0; o < mainShifts.length; o++){
            //remove non existing days
            if(mainShifts[o] == null)
                continue;

            int day = o+1;
            ShiftDate sd = new ShiftDate(day, mainShifts[o]);
            finalShifts.add(sd);
        }

        Employee e = new Employee(name, finalShifts);

        return e;


    }

}
