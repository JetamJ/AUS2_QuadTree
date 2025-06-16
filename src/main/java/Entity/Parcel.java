package Entity;

import QuadStrom.QuadTreeObject;

import java.util.ArrayList;

public class Parcel implements QuadTreeObject, Comparable<QuadTreeObject> {

    private int id;
    private String desc;
    private ArrayList<Property> listOfProperties;
    private GPS gps1;
    private GPS gps2;

    public Parcel(int id, String desc, GPS gps1, GPS gps2){
        this.id = id;
        this.desc = desc;
        this.gps1 = gps1;
        this.gps2 = gps2;
        this.listOfProperties = new ArrayList<>();
    }

    public Parcel() {};

    public ArrayList<Property> getListOfProperties() {
        return this.listOfProperties;
    }

    @Override
    public GPS getPosition1() {
        return gps1;
    }

    @Override
    public GPS getPosition2() {
        return gps2;
    }

    @Override
    public String toString() {
        return "Parcela  " + this.id + " GPS1: " + gps1.getX() + " , " + gps1.getY() + " GPS2: " + gps2.getX() + " , " + gps2.getY();
    }
    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getDescription() {
        return this.desc;
    }

    @Override
    public void setDescription(String desc) {
        this.desc = desc;
    }

//    @Override
//    public boolean compareTo(QuadTreeObject object) {
//        if (this.id == object.getId()
//                && this.desc.equals(object.getDescription())
//                && this.gps1.equals(object.getPosition1())
//                && this.gps2.equals(object.getPosition2())){
//            return true;
//        }
//        return false;
//    }

    @Override
    public String toFileString() {
        String line = "Entity.Parcel;" + this.id + ";" + this.desc + ";"
                + this.gps1.getWidth() + ";" + this.gps1.getX() + ";"
                + this.gps1.getHeight() + ";" + this.gps1.getY() + ";"
                + this.gps2.getWidth() + ";" + this.gps2.getX() + ";"
                + this.gps2.getHeight() + ";" + this.gps2.getY();
        return line;
    }

    @Override
    public void parseFileLine(String line) {
        String[] variables = line.split(";");
        this.id = Integer.parseInt(variables[1]);
        this.desc = variables[2];
        this.gps1 = new GPS(variables[3].toCharArray()[0], Double.valueOf(variables[4]), variables[5].toCharArray()[0], Double.valueOf(variables[6]));
        this.gps2 = new GPS(variables[7].toCharArray()[0], Double.valueOf(variables[8]), variables[9].toCharArray()[0], Double.valueOf(variables[10]));
    }

    @Override
    public int compareTo(QuadTreeObject o) {
        if (this.id == o.getId()
                && this.desc.equals(o.getDescription())
                && this.gps1.equals(o.getPosition1())
                && this.gps2.equals(o.getPosition2())){
            return 0;
        }
        return -1;
    }
}
