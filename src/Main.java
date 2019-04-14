import loggerService.LogReader;
import loggerService.impl.LogReaderImpl;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        LogReader logReader = new LogReaderImpl();
        logReader.readLogFile(Arrays.asList(("./src/testdata/t1.txt"),"./src/testdata/t2.txt"));
    }
}


