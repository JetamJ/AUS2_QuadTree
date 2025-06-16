package base;

import Entity.Parcel;
import QuadStrom.QuadTreeObject;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

public class CSV<T extends QuadTreeObject> {

    public CSV() {
    }

    public void writeToCSV(String filePath, ArrayList<String> data) {
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (String line : data) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();

            System.out.println("Data has been written to " + filePath);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public ArrayList<String> loadFromCSV(String filePath) {
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            ArrayList<String> data = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                data.add(line);
            }
            bufferedReader.close();
            System.out.println("Data has been loaded from " + filePath);
            return data;
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }
}
