package QuadStrom;

import Entity.GPS;

public interface QuadTreeObject {

    public GPS getPosition1();
    public GPS getPosition2();
    public String toString();
    public int getId();
    public void setId(int id);
    public String getDescription();
    public void setDescription(String desc);
    public String toFileString();
    public void parseFileLine(String line);
}
