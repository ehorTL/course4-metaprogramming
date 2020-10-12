package formatter.dirtree;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class FileManagerTest {
    @Test
    public void getCppLanguageFilePathes() {

    }

    @Test
    public void getFilesFromDirByExt() {
    }

    @Test
    public void getFilesFromDirWithExts() {
        ArrayList<String> testext = new ArrayList<>(Arrays.asList(
                "java"
        ));

        String testpath = "C:\\Users\\user\\Desktop\\course4\\metaprogramming\\code-formatter\\src";
        ArrayList<String> arrayList = FileManager.getFilesFromDirWithExts(testpath, testext);

        for (String s : arrayList) {
            System.out.println(s);
        }
    }
}
