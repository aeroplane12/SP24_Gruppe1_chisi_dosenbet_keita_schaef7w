package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

public class Manager {


    public void csvReader(String path){
        //TODO CSV. Reader
        Scanner scanner;
        try{
            scanner = new Scanner(new File(path), StandardCharsets.UTF_8);
            String[] input = new String[15];
            while (scanner.hasNext()){
                input = scanner.nextLine().split("[,\n]");



            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
