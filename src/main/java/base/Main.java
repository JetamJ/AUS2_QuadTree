package base;

import Entity.GPS;
import Entity.Parcel;
import Entity.Property;
import GUI.MainWindow;
import QuadStrom.Node;
import QuadStrom.QuadTree;
import QuadStrom.QuadTreeObject;

public class Main {

    public static void main(String[] args) {

        QuadTree<Parcel> qdParcel = new QuadTree<>(0,0,500,500,10);
        QuadTree<Property> qdProperty = new QuadTree<>(0,0,500,500,10);
        Generator<Parcel> gParcel = new Generator<>(qdParcel, Parcel.class);
        Generator<Property> gProperty = new Generator<>(qdProperty, Property.class);
//        gParcel.insertObjects(10000);
//        gParcel.find(10000);
//        gParcel.findInterval(10000);
//        gParcel.deleteObjects();
        
        MainWindow<QuadTreeObject> mw = new MainWindow<>(qdParcel, gParcel, qdProperty, gProperty);
        mw.drawQuadTreeParcel();
        mw.drawQuadTreeProperty();
        mw.setVisible(true);
    }


}