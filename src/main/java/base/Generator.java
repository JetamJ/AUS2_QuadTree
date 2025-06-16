package base;

import Entity.GPS;
import Entity.Parcel;
import Entity.Property;
import QuadStrom.QuadTree;
import QuadStrom.QuadTreeObject;
import java.util.ArrayList;
import java.util.Random;

public class Generator<T extends QuadTreeObject & Comparable<QuadTreeObject>> {

    private QuadTree<T> quadTree;
    private Random random;
    private Class<T> type;

    public Generator(QuadTree<T> qd, Class<T> type) {
        this.quadTree = qd;
        random = new Random();
        this.type = type;
    }

    public void insertObjects(int numberToInsert) {
        T object;
        GPS gps1;
        GPS gps2;
        double x;
        double y;
        for (int i = 0; i < numberToInsert; i++) {
            x = random.nextDouble() * (quadTree.getRoot().getX2() - 50);
            y = random.nextDouble() * (quadTree.getRoot().getY2() - 50);
            gps1 = new GPS('X', x,'Y', y);
            x = x + random.nextDouble() * (50);
            y = y + random.nextDouble() * (50);
            gps2 = new GPS('X', x,'Y', y);
            if (this.type == Property.class) {
                object = (T) new Property(i, "Nehnutelnost cislo: " + i, gps1, gps2);
            } else {
                object = (T) new Parcel(i, "Parcela cislo: " + i, gps1, gps2);
            }

            quadTree.add((T) object);
        }
    }

    public void deleteObjects() {
        for (T object : quadTree.getAllObjectOfTree()) {
            quadTree.delete(object);
        }
    }

    public void find(int numberToFind){
        double x;
        double y;
        for (int i = 0; i < numberToFind; i++) {
            x = random.nextDouble() * (quadTree.getRoot().getX2());
            y = random.nextDouble() * (quadTree.getRoot().getY2());
            quadTree.find(x,y);
        }
    }

    public void findInterval(int numberToFind){
        double x1;
        double y1;
        double x2;
        double y2;
        for (int i = 0; i < numberToFind; i++) {
            x1 = random.nextDouble() * (quadTree.getRoot().getX2() - 200);
            y1 = random.nextDouble() * (quadTree.getRoot().getY2() - 200);
            x2 = x1 + random.nextDouble() * (200);
            y2 = y1 + random.nextDouble() * (200);
            quadTree.find(x1,y1,x2,y2);
        }
    }

    public void random() {
        double x;
        double y;
        T object;
        for (int i = 0; i < 10000; i++) {
            int number = random.nextInt(3);
            switch (number) {
                case 0 :
                    x = random.nextDouble() * (quadTree.getRoot().getX2() - 100);
                    y = random.nextDouble() * (quadTree.getRoot().getY2() - 100);
                    GPS gps1 = new GPS('X', x,'Y', y);
                    x = x + random.nextDouble() * (100);
                    y = y + random.nextDouble() * (100);
                    GPS gps2 = new GPS('X', x,'Y', y);
                    if (this.type == Property.class) {
                        object = (T) new Property(i, "Nehnutelnost cislo: " + i, gps1, gps2);
                    } else {
                        object = (T) new Parcel(i, "Parcela cislo: " + i, gps1, gps2);
                    }
                    quadTree.add(object);
                    break;
                case 1 :
                    x = random.nextDouble() * (quadTree.getRoot().getX2());
                    y = random.nextDouble() * (quadTree.getRoot().getY2());
                    quadTree.find(x,y);
                    break;
                case 2 :
                    ArrayList<T> objects = quadTree.getAllObjectOfTree();
                    if (!objects.isEmpty()) {
                        int randomObject = random.nextInt(objects.size());
                        quadTree.delete(objects.get(randomObject));
                    }
                    break;
            }
        }
    }
}
