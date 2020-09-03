import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
// Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
// SPDX-License-Identifier: MIT-0
public class App {


    public static void main(String[] args) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        Random random = new Random();

        int count;
        int startIndex = 1;
        while(true) {
            try {
                System.out.println("Enter the number of records to generate(1-10000):");
                count = Integer.parseInt(in.readLine());
                if(count > 10000) throw new NumberFormatException("invalid");
                System.out.println("Enter the start index:");
                startIndex = Integer.parseInt(in.readLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input");
            }
        }

        StringBuilder dummyData = new StringBuilder("INSERT INTO `testdb`.`orders` (`orderId`, `source`, `amount`, `state`) values \n");
        String[] arrStates = new String[]{ ",'California'),", ",'Texas'),", ",'Florida'),", ",'New York'),", ",'Ohio'),"};
        String[] arrSources = new String[]{",'Andriod',", ",'iOS',", ",'Windows',", ",'MacOS',"};


        for(int i=0;i<count-1; i++){
            int orderId  = startIndex + i;
            String str = "(" + orderId + arrSources[ new Random().nextInt(3)] + random.nextInt(4000) + arrStates[ new Random().nextInt(5)] + "\n";
            dummyData.append(str);
        }

        //gen hardcoded last row
        dummyData.append("(" + (startIndex + count) + ",'iOS',2,'Florida');");

        String filename = "data-" + java.util.UUID.randomUUID() + ".sql";
        FileWriter writer = new FileWriter(filename);

        writer.write(dummyData.toString());

        writer.close();

        System.out.println(filename + " file created with dummy data!!!");


    }


}
