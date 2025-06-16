package QuadStrom;

import java.util.ArrayList;

public class Node<T extends QuadTreeObject> {

    private boolean root = false;
    private ArrayList<T> list;
    private Node<T>[] sons;
    private Node<T> parent;
    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private int height = 0;


    public Node(double x1, double y1, double x2, double y2, Node<T> parent) {
        this.list = new ArrayList<T>();
        this.sons = new Node[4];
        this.parent = parent;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        if (parent != null){
            this.height = parent.getHeight() + 1;
        } else {
            this.height = 1;
        }
    }

    public void createSons() {
        double width = this.x2 - this.x1;
        double height = this.y2 - this.y1;

        this.sons[0] = new Node<>(this.x1, this.y1, this.x1 + width / 2, this.y1 + height / 2, this);
        this.sons[1] = new Node<>(this.x1 + width /2, this.y1, this.x2, this.y1 + height / 2, this);
        this.sons[2] = new Node<>(this.x1 + width /2, this.y1 + height /2, this.x2, this.y2, this);
        this.sons[3] = new Node<>(this.x1, this.y1 + height /2, this.x1 + width /2, this.y2, this);

        ArrayList<T> itemsToDelete = new ArrayList<>();
        for (T object : this.list){
            for (Node<T> n : this.sons) {
                if(n.contains(object)) {
                    n.getList().add(object);
                    itemsToDelete.add(object);
                }
            }
        }

        for (T object : itemsToDelete){
            this.list.remove(object);
        }
    }

    public void createSonsWithDifferentSize(double x, double y) {

        this.sons[0] = new Node<>(this.x1, this.y1, x, y, this);
        this.sons[1] = new Node<>(x, this.y1, this.x2, y, this);
        this.sons[2] = new Node<>(x,  y, this.x2, this.y2, this);
        this.sons[3] = new Node<>(this.x1, y, x, this.y2, this);

        ArrayList<T> itemsToDelete = new ArrayList<>();
        for (T object : this.list){
            for (Node<T> n : this.sons) {
                if(n.contains(object)) {
                    n.getList().add(object);
                    itemsToDelete.add(object);
                }
            }
        }

        for (T object : itemsToDelete){
            this.list.remove(object);
        }
    }

    public boolean shouldCreateSons2(T object) {
        if (!this.hasSons()) {
            ArrayList<Node<T>> nodes = new ArrayList<>();
            nodes.add(new Node<>(this.x1, this.y1, object.getPosition1().getX(), object.getPosition1().getY(), this));
            nodes.add(new Node<>(object.getPosition1().getX(), this.y1, this.x2, object.getPosition1().getY(), this));
            nodes.add(new Node<>(object.getPosition1().getX(),  object.getPosition1().getY(), this.x2, this.y2, this));
            nodes.add(new Node<>(this.x1, object.getPosition1().getY(), object.getPosition1().getX(), this.y2, this));
            for (Node<T> node : nodes) {
                if (node.contains(object)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean shouldCreateSons() {
        if (!this.hasSons()) {
            ArrayList<Node<T>> nodes = new ArrayList<>();
            double width = this.x2 - this.x1;
            double height = this.y2 - this.y1;
            nodes.add(new Node<>(this.x1, this.y1, this.x1 + width / 2, this.y1 + height / 2, this));
            nodes.add(new Node<>(this.x1 + width /2, this.y1, this.x2, this.y1 + height / 2, this));
            nodes.add(new Node<>(this.x1 + width /2, this.y1 + height /2, this.x2, this.y2, this));
            nodes.add(new Node<>(this.x1, this.y1 + height /2, this.x1 + width /2, this.y2, this));
            for (Node<T> node : nodes) {
                for (T object: this.list) {
                    if (node.contains(object)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void deleteSons() {
        for (int i = 0; i < 4; i++) {
            this.sons[i] = null;
        }
    }

    public Node<T>[] getSons(){
        return sons;
    }

    public boolean hasSons() {
        if (this.sons[0] != null
            && this.sons[1] != null
            && this.sons[2] != null
            && this.sons[3] != null) {
            return true;
        }
        return false;
    }

    public boolean areSonsLists() {
        for (int i = 0; i < 4; i++) {
            if (this.sons[i].hasSons()) {
                return false;
            }
        }
        return true;
    }

    public void pointsToString(){
        System.out.println(this.x1 + " , " + this.y1 + " -- " + this.x2 + " , " + this.y2);
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public ArrayList<T> getList() {
        return this.list;
    }

    public Node<T> getParent() {
        return this.parent;
    }

    public boolean contains(T object) {
        if (object.getPosition1().getX() >= this.x1
            && object.getPosition1().getY() >= this.y1
            && object.getPosition2().getX() <= this.x2
            && object.getPosition2().getY() <= this.y2)
        {
            return true;
        }
        return false;
    }

    public boolean contains(double x1, double y1, double x2, double y2) {
        if (x1 >= this.x1
            && y1 >= this.y1
            && x2 <= this.x2
            && y2 <= this.y2)
        {
            return true;
        }
        return false;
    }

    public boolean isConflict(T object) {
        double width = this.x2 - this.x1;
        double height = this.y2 - this.y1;
        double conflcitX = this.x1 + width / 2;
        double conflictY = this.y1 + height / 2;

        if ((object.getPosition1().getX() < conflcitX
                && object.getPosition2().getX() > conflcitX)
                || (object.getPosition1().getY() < conflictY
                && object.getPosition2().getY() > conflictY)) {
                return true;
        }

        //ked su rovnake

        return false;
    }

    public boolean isConflict2(T object) {
        if (this.hasSons()) {
            if ((object.getPosition1().getX() < this.sons[0].getX2()
                    && object.getPosition2().getX() > this.sons[0].getX2())
                    || (object.getPosition1().getY() < this.sons[0].getY2()
                    && object.getPosition2().getY() > this.sons[0].getY2())) {
                return true;
            }
        }
        //ked su rovnake

        return false;
    }

    public ArrayList<Node<T>> whereToSearch(double x1, double y1, double x2, double y2) {
        ArrayList<Node<T>> nodesToSearch = new ArrayList<>();

        for (Node<T> node : this.sons) {
            if (node.contains(x1,y1,x1,y1)
                || node.contains(x2,y1,x2,y1)
                || node.contains(x2,y2,x2,y2)
                || node.contains(x1,y2,x1,y2)
                || (node.getX1() >= x1
                    && node.getY1() >= y1
                    && node.getX2() <= x2
                    && node.getY2() <= y2)){
                nodesToSearch.add(node);
            }
        }
        return nodesToSearch;
    }

    public ArrayList<T> getObjectsOfSons(){
        ArrayList<T> objects = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (this.sons[i] != null) {
                objects.addAll(sons[i].getList());
            }
        }
        return objects;
    }

    public String toString() {
        StringBuilder result = new StringBuilder("Node vo vrstve: " + getHeight() + " Hranice: " + x1 + " , " + y1 + " , " + x2 + " , " + y2 + " , " + " obsahuje: ");
        //String result = "Node vo vrstve: " + getHeightOfNode() + " Hranice: " + x1 + " , " + y1 + " , " + x2 + " , " + y2 + " , " + " obsahuje: ";
        for (T object : getList()) {
            result.append(object.toString());
        }
        return result.toString();
    }

    public int getHeight() {
        return height;
    }

    public double getX1() {
        return x1;
    }

    public double getY1() {
        return y1;
    }

    public double getX2() {
        return x2;
    }

    public double getY2() {
        return y2;
    }
}
