package org.deb.SFM;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFileCreater {
    SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss");

    String[] poems = new String[]{"Stopping by Woods on a Snowy Evening" +
            " BY ROBERT FROST" +
            " Whose woods these are I think I know." +
            " His house is in the village though;" +
            " He will not see me stopping here " +
            " To watch his woods fill up with snow." +

            " My little horse must think it queer" +
            " To stop without a farmhouse near" +
            " Between the woods and frozen lake" +
            " The darkest evening of the year." +
            " He gives his harness bells a shake" +
            " To ask if there is some mistake." +
            " The only other sound’s the sweep" +
            " Of easy wind and downy flake." +
            " The woods are lovely, dark and deep," +
            " But I have promises to keep," +
            " And miles to go before I sleep," +
            " And miles to go before I sleep."};

    @Test
    public void createLogFile() throws FileNotFoundException {
        String file = "src"+ File.separator+"test"+File.separator+"resources"+File.separator+"logs"+File.separator+"string-generation-"+sdf.format(new Date())+".log";
        try (PrintWriter printWriter = new PrintWriter(file)) {

            for (int i = 0; i < 6; i++) {
                for (String each : poems) {
                    printWriter.println(System.currentTimeMillis()+","+each);
                }
            }
        }

    }

}
