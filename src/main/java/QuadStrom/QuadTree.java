package QuadStrom;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Comparator;

public class QuadTree<T extends QuadTreeObject & Comparable<QuadTreeObject>> {

    private Node<T> root;

    private int maximumTreeHeight;

    public QuadTree(double x1, double y1, double x2, double y2, int maximumTreeHeight) {
        root = new Node<>(x1,y1,x2,y2, null);
        root.setRoot(true);
        this.maximumTreeHeight = maximumTreeHeight;
    }

    public void add(T object) {
        int healOfTree = this.checkHealthOfTree();
        if (healOfTree <= 70 && healOfTree >= 0) {
            this.changeQuardTreeHieght(this.maximumTreeHeight + 1);
        }

        ArrayList<Node<T>> listOfNodes = new ArrayList<>();
        listOfNodes.add(this.root);
        while(true){
            for (Node<T> n : listOfNodes) {
                if (n.contains(object)) {
                    if ((n.getList().isEmpty() && !n.hasSons()) || n.getHeight() == maximumTreeHeight) {
                        n.getList().add(object);
                        return;
                    } else {
                        if (n.isConflict(object)) {
                            n.getList().add(object);
                            return;
                        }
                        if (!n.hasSons()) {
                            n.createSons();
                        }
                        listOfNodes.clear();
                        for (Node<T> son : n.getSons()) {
                            listOfNodes.add(son);
                        }
                        break;
                    }
                }
            }
        }
    }

    public ArrayList<T> find(double x1, double y1) {
        ArrayList<Node<T>> listOfNodes = new ArrayList<>();
        ArrayList<Node<T>> listOfNodesToSearch = new ArrayList<>();
        ArrayList<Node<T>> tempNodes = new ArrayList<>();
        listOfNodes.add(this.root);
        while(true){
            for (Node n : listOfNodes) {
                if(n.contains(x1,y1,x1,y1)){
                    listOfNodesToSearch.add(n);
                    if (n.hasSons()) {
                        for (Node<T> son : n.getSons()) {
                            tempNodes.add(son);
                        }
                    }
                }
            }
            listOfNodes.clear();
            listOfNodes.addAll(tempNodes);
            tempNodes.clear();
            if (listOfNodes.isEmpty()) {
                break;
            }
        }

        ArrayList<T> objects = new ArrayList<>();

        for (Node n : listOfNodesToSearch) {
            ArrayList<T> nodeObjects = n.getList();
            for (T object : nodeObjects){
                if (object.getPosition1().getX() <= x1
                        && object.getPosition1().getY() <= y1
                        && object.getPosition2().getX() >= x1
                        && object.getPosition2().getY() >= y1)
                {
                    objects.add(object);
                }
            }
        }
        return objects;
    }

    public ArrayList<T> find(double x1, double y1, double x2, double y2) {
        ArrayList<Node<T>> listOfNodes = new ArrayList<>();
        ArrayList<Node<T>> listOfNodesToSearch = new ArrayList<>();
        ArrayList<Node<T>> tempNodes = new ArrayList<>();
        listOfNodes.add(this.root);
        while(true){
            for (Node n : listOfNodes) {
                if (n.hasSons()){
                    tempNodes.addAll(n.whereToSearch(x1, y1, x2, y2));
                    listOfNodesToSearch.add(n);
                } else {
                    listOfNodesToSearch.add(n);
                }
            }
            listOfNodes.clear();
            listOfNodes.addAll(tempNodes);
            tempNodes.clear();
            if (listOfNodes.isEmpty()) {
                break;
            }
        }

        ArrayList<T> objects = new ArrayList<>();
        Node<T> searchNode = new Node<>(x1,y1,x2,y2,null);

        for (Node n : listOfNodesToSearch) {
            ArrayList<T> nodeObjects = n.getList();
            for (T object : nodeObjects){
                if (searchNode.contains(object.getPosition1().getX(),object.getPosition1().getY(),object.getPosition1().getX(),object.getPosition1().getY())
                        || searchNode.contains(object.getPosition2().getX(),object.getPosition1().getY(),object.getPosition2().getX(),object.getPosition1().getY())
                        || searchNode.contains(object.getPosition2().getX(),object.getPosition2().getY(),object.getPosition2().getX(),object.getPosition2().getY())
                        || searchNode.contains(object.getPosition1().getX(),object.getPosition2().getY(),object.getPosition1().getX(),object.getPosition2().getY())
                        || searchNode.contains(object.getPosition1().getX(),object.getPosition1().getY(),object.getPosition2().getX(),object.getPosition2().getY()))
                {
                    objects.add(object);
                }
            }
        }
        return objects;
    }

    public Node<T> find(T object){
        ArrayList<Node<T>> listOfNodes = new ArrayList<>();
        ArrayList<Node<T>> tempNodes = new ArrayList<>();
        listOfNodes.add(this.root);
        while(true){
            for (Node n : listOfNodes) {
                if (n.contains(object)){
                    if(n.getList().contains(object)) {
                        return n;
                    } else {
                        if (n.hasSons()) {
                            for (Node<T> son : n.getSons()) {
                                tempNodes.add(son);
                            }
                        }
                    }
                }
            }
            listOfNodes.clear();
            listOfNodes.addAll(tempNodes);
            tempNodes.clear();
            if (listOfNodes.isEmpty()) {
                return null;
            }
        }
    }

    public boolean delete(T object) {
        Node<T> node = find(object);
        if (node != null) {
            T objectToDelete = null;
            for ( T o : node.getList()) {
                if (o.compareTo(object) == 0){
                    objectToDelete = o;
                }
            }
            if (objectToDelete == null) {
                System.out.println(object + " is not present in QuadTree.");
                return false;
            }
            node.getList().remove(objectToDelete);
            while (true) {
                Node<T> parent = node.getParent();
                if (!node.hasSons() && parent != null) {
                    ArrayList<T> objectsOfSons = parent.getObjectsOfSons();
                    if (objectsOfSons.size() <= 1 && parent.getList().isEmpty() && parent.areSonsLists()) {
                        parent.deleteSons();
                        parent.getList().addAll(objectsOfSons);
                    } else {
                        if (objectsOfSons.isEmpty() && parent.areSonsLists()) {
                            parent.deleteSons();
                        }
                        break;
                    }
                    node = parent;
                } else {
                    break;
                }
            }
            return true;
        }
        System.out.println(object + " is not present in QuadTree.");
        return false;
    }

    public boolean edit(T objectToEdit, T newObject) {
        Node<T> node = find(objectToEdit);
        if (node != null) {
            T object = null;
            for (T o : node.getList()) {
                if (o.compareTo(objectToEdit) == 0) {
                    object = o;
                }
            }
            if (object == null) {
                System.out.println(objectToEdit + " is not present in QuadTree.");
                return false;
            }
            if (object.compareTo(newObject) == 0 || newObject == null) {
                System.out.println("No changes or new object is null!");
                return false;
            } else {
                if (object.getId() != newObject.getId()) {
                    object.setId(newObject.getId());
                }
                if (!object.getDescription().equals(newObject.getDescription())) {
                    object.setDescription(newObject.getDescription());
                }
                if (object.getPosition1() != newObject.getPosition1() || object.getPosition1() != newObject.getPosition1()) {
                    this.delete(object);
                    this.add(newObject);
                }
                return true;
            }
        }
        System.out.println(objectToEdit + " is not present in QuadTree.");
        return false;
    }

    public ArrayList<Node<T>> getAllNodesOfTree() {
        ArrayList<Node<T>> resultList = new ArrayList<>();
        ArrayList<Node<T>> listOfNodes = new ArrayList<>();
        ArrayList<Node<T>> listOfSons = new ArrayList<>();
        listOfNodes.add(this.root);
        while(true){

            for (Node<T> n : listOfNodes) {
                resultList.add(n);
                if(n.hasSons()) {
                    for (Node son : n.getSons()){
                        listOfSons.add(son);
                    }
                }
            }
            if(listOfSons.isEmpty()) {
                return  resultList;
            }
            listOfNodes.clear();
            listOfNodes.addAll(listOfSons);
            listOfSons.clear();
        }
    }

    public ArrayList<T> getAllObjectOfTree() {
        ArrayList<Node<T>> nodes = getAllNodesOfTree();
        ArrayList<T> objects = new ArrayList<>();
        for (Node<T> node : nodes){
            objects.addAll(node.getList());
        }
        return objects;
    }

    public Node<T> getRoot() {
        return this.root;
    }

    public int getMaximumTreeHeight() {
        return this.maximumTreeHeight;
    }

    public void changeQuardTreeHieght(int maximumTreeHeight) {
        if (this.maximumTreeHeight > maximumTreeHeight) {
            for (Node<T> node : this.getAllNodesOfTree()) {
                if (node.getHeight() == maximumTreeHeight) {
                    if (node.hasSons()) {
                        ArrayList<T> data = new ArrayList<>();
                        ArrayList<Node<T>> sons = new ArrayList<>(Arrays.stream(node.getSons()).toList());
                        ArrayList<Node<T>> temp = new ArrayList<>();
                        while (true) {
                            for (Node<T> n : sons) {
                                data.addAll(n.getList());
                                if (n.hasSons()) {
                                    temp.addAll(Arrays.stream(n.getSons()).toList());
                                }
                            }
                            if (temp.isEmpty()) {
                                break;
                            }
                            sons.clear();
                            sons.addAll(temp);
                            temp.clear();
                        }
                        node.getList().addAll(data);
                        node.deleteSons();
                    }
                }
            }
        } else if (this.maximumTreeHeight < maximumTreeHeight) {
            for (Node<T> node : this.getAllNodesOfTree()) {
                if (node.getHeight() == this.maximumTreeHeight) {
                    ArrayList<Node<T>> newSons = new ArrayList<>();
                    ArrayList<Node<T>> nodes = new ArrayList<>();
                    nodes.add(node);
                    while(true) {
                        for (Node<T> n : nodes) {
                            if (!n.getList().isEmpty() && n.getHeight() < maximumTreeHeight && n.shouldCreateSons()) {
                                n.createSons();
                                newSons.addAll(Arrays.stream(n.getSons()).toList());
                            }
                        }
                        if (newSons.isEmpty()) {
                            break;
                        }
                        nodes.clear();
                        nodes.addAll(newSons);
                        newSons.clear();
                    }
                }
            }
        }
        this.maximumTreeHeight = maximumTreeHeight;
    }

    public void save(String filePath) {
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            ArrayList<T> data = this.getAllObjectOfTree();

            if (data.isEmpty()) {
                System.out.println("V strome nie su ziadne data");
                return;
            }

            for (T object : data) {
                bufferedWriter.write(object.toFileString());
                bufferedWriter.newLine();
            }
            bufferedWriter.close();

            System.out.println("Objekty boli zapisane do suboru: " + filePath);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void load(String filePath) {
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            ArrayList<String> data = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                data.add(line);
            }
            bufferedReader.close();

            if (data.isEmpty()) {
                System.out.println("Ziadne udaje sa nenachadazuje v sobure: " + filePath);
                return;
            }

            for(String dataLine : data){
                String[] c = dataLine.split(";");
                String className = c[0];

                Class<?> objectClass = Class.forName(className);
                Constructor<?> constructor = objectClass.getConstructor();
                T object = (T) constructor.newInstance();
                object.parseFileLine(dataLine);
                this.add(object);
            }
            System.out.println("Udaje boli uspesne nacitane zo suboru: " + filePath);
        } catch (IOException e) {
            System.out.println(e);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> checkTree(){
        ArrayList<Node<T>> nodes = getAllNodesOfTree();
        ArrayList<T> objects = getAllObjectOfTree();
        ArrayList<String> result = new ArrayList<>();
        for (Node<T> node : nodes){
            System.out.println(node.toString());
            result.add(node.toString());
        }
        System.out.println("Quad strom obsahuje: " + nodes.size() + " vrcholov a " + objects.size() + " objektov. Zdravie je " + this.checkHealthOfTree() + " % a vyska stromu: " + this.getMaximumTreeHeight());
        result.add("Quad strom obsahuje: " + nodes.size() + " vrcholov a " + objects.size() + " objektov. Zdravie je " + this.checkHealthOfTree() + " % a vyska stromu: " + this.getMaximumTreeHeight());
        return result;
    }

    public int checkHealthOfTree() {
        ArrayList<Node<T>> nodes = this.getAllNodesOfTree();
        if (nodes.isEmpty() || this.getAllObjectOfTree().size() < 10) {
            return -1;
        }
        int numberOfBadlyStoresObjects = 0;
        for (Node<T> node : nodes) {
            if (node.shouldCreateSons()) {
                numberOfBadlyStoresObjects++;
            }
        }

        int health = 100 * (this.getAllObjectOfTree().size() - numberOfBadlyStoresObjects) / this.getAllObjectOfTree().size();
        return health;
    }

    public int checkHealthOfTree2() {
        ArrayList<Node<T>> nodes = this.getAllNodesOfTree();
        if (nodes.isEmpty() || this.getAllObjectOfTree().isEmpty()) {
            return -1;
        }
        int numberOfBadlyStoresObjects = 0;
        for (Node<T> node : nodes) {
            for (T object : node.getList()) {
                if (node.shouldCreateSons2(object)) {
                    numberOfBadlyStoresObjects++;
                }
            }
        }

        int health = 100 * (this.getAllObjectOfTree().size() - numberOfBadlyStoresObjects) / this.getAllObjectOfTree().size();
        return health;
    }

    public Node<T> findFullNode() {
        ArrayList<Node<T>> nodes = this.getAllNodesOfTree();
        ArrayList<T> objects = this.getAllObjectOfTree();
        for (Node<T> node : nodes) {
            if (node.getList().size() > (0.1 * objects.size())) {
                return node;
            }
        }
        return null;
    }

    public void optimize(Node<T> node) {
        if (node == null) {
            return;
        }
        ArrayList<T> objects = new ArrayList<>();
        ArrayList<Node<T>> nodes = new ArrayList<>();
        ArrayList<Node<T>> temp = new ArrayList<>();
        nodes.add(node);
        while (true) {
            for (Node<T> n : nodes) {
                objects.addAll(n.getList());
                if (n.hasSons()) {
                    temp.addAll(Arrays.stream(n.getSons()).toList());
                }
            }
            if (temp.isEmpty()) {
                break;
            }
            nodes.clear();
            nodes.addAll(temp);
            temp.clear();
        }
        node.deleteSons();
        node.getList().clear();

        objects.sort(new Comparator<T>() {
            public int compare(T object1, T object2) {
                double area1 = (object1.getPosition2().getX() - object1.getPosition1().getX()) * (object1.getPosition2().getY() - object1.getPosition1().getY());
                double area2 = (object2.getPosition2().getX() - object2.getPosition1().getX()) * (object2.getPosition2().getY() - object2.getPosition1().getY());
                return (int) (area2 - area1);
            }
        });

        for (T object : objects) {
            this.addOpt(object);
        }
    }

    public void addOpt(T object) {
        ArrayList<Node<T>> listOfNodes = new ArrayList<>();
        listOfNodes.add(this.root);
        while(true){
            for (Node<T> n : listOfNodes) {
                if (n.contains(object)) {
                    if ((n.getList().isEmpty() && !n.hasSons()) || n.getHeight() == maximumTreeHeight) {
                        n.getList().add(object);
                        return;
                    } else {
                        if (n.isConflict2(object)) {
                            n.getList().add(object);
                            return;
                        }
                        if (!n.hasSons()) {
                            n.createSonsWithDifferentSize(object.getPosition1().getX(), object.getPosition1().getY());
                        }
                        listOfNodes.clear();
                        for (Node<T> son : n.getSons()) {
                            listOfNodes.add(son);
                        }
                        break;
                    }
                }
            }
        }
    }
}
