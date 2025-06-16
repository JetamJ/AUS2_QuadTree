import static org.junit.Assert.*;

import Entity.GPS;
import Entity.Parcel;
import QuadStrom.QuadTree;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

public class QuadTreeTests {

    QuadTree<Parcel> quadTree = new QuadTree<>(0, 0, 100, 100, 5);
    Random random = new Random();
    int numberOfIterations = 1000;

    @Test
    public void noDataTest() {
        Parcel object = new Parcel(0, "Parcela cislo: 0", new GPS('X', 10,'Y', 10), new GPS('X', 50,'Y', 50));
        assertEquals(5, quadTree.getMaximumTreeHeight());
        assertFalse(quadTree.delete(object));
        assertEquals(quadTree.find(50, 50).size(), 0);
        assertEquals(quadTree.find(10,10,50, 50).size(), 0);
    }


    @Test
    public void insertTest() {
        double x;
        double y;
        GPS gps1;
        GPS gps2;
        Parcel parcel;
        for (int i = 0; i < numberOfIterations; i++) {
            x = random.nextDouble() * (quadTree.getRoot().getX2() - 50);
            y = random.nextDouble() * (quadTree.getRoot().getY2() - 50);
            gps1 = new GPS('X', x,'Y', y);
            x = x + random.nextDouble() * (50);
            y = y + random.nextDouble() * (50);
            gps2 = new GPS('X', x,'Y', y);
            parcel = new Parcel(i, "Parcela cislo: " + i, gps1, gps2);
            quadTree.add(parcel);
            assertEquals(i + 1, quadTree.getAllObjectOfTree().size());
        }

        assertNotNull(quadTree);
        assertNotNull(quadTree.getAllObjectOfTree());
        assertEquals(numberOfIterations, quadTree.getAllObjectOfTree().size());
    }

    @Test
    public void findTest() {
        this.insertTest();
        double x;
        double y;
        for (int i = 1; i < numberOfIterations; i++) {
            x = random.nextDouble() * (quadTree.getRoot().getX2());
            y = random.nextDouble() * (quadTree.getRoot().getY2());
            assertNotNull(quadTree.find(x, y));
        }
    }

    @Test
    public void changeHeightTest() {
        this.insertTest();
        int newHeight = quadTree.getMaximumTreeHeight() + 2;
        quadTree.changeQuardTreeHieght(newHeight);
        assertEquals(newHeight, quadTree.getMaximumTreeHeight());
        assertEquals(numberOfIterations, quadTree.getAllObjectOfTree().size());
        newHeight = newHeight - 4;
        quadTree.changeQuardTreeHieght(newHeight);
        assertEquals(newHeight, quadTree.getMaximumTreeHeight());
        assertEquals(numberOfIterations, quadTree.getAllObjectOfTree().size());
    }

    @Test
    public void deleteTes() {
        this.insertTest();
        for (int i = numberOfIterations - 1; i >= 0; i--) {
            ArrayList<Parcel> objects = quadTree.getAllObjectOfTree();
            Parcel object = objects.get(random.nextInt(objects.size()));
            quadTree.delete(object);
            assertEquals(i, quadTree.getAllObjectOfTree().size());
        }
        assertNotNull(quadTree);
        assertEquals(0, quadTree.getAllObjectOfTree().size());
        assertEquals(1, quadTree.getAllNodesOfTree().size());
    }
}
