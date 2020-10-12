package formatter.dirtree;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class FileManager {

    private static final ArrayList<String> cppProjectBasicExtensions = new ArrayList<>(Arrays.asList(
            "cpp",
            "c",
            "h"
    ));

    public static ArrayList<String> getCppLanguageFilePathes(String dirname){
        return getFilesFromDirWithExts(dirname, cppProjectBasicExtensions, true);
    }

    /**
     * @param deep to search in subdirectories
     * */
    public static ArrayList<String> getFilesFromDirWithExts(String filename, ArrayList<String> extensions,
                                                            boolean deep) {
        ArrayList<String> pathes = new ArrayList<>();

        File file = new File(filename);
        if (file.isFile()){
            String ext = getFileExtension(file.getName());

            for (String fileExt : extensions) {
                if (fileExt.equals(ext)){
                    pathes.add(filename);
                    break;
                }
            }
        } else if (file.isDirectory()) {
            for (File f : file.listFiles()){
                if (deep) {
                    pathes.addAll(getFilesFromDirWithExts(f.getAbsolutePath(), extensions, true));
                } else {
                    if (f.isFile()){
                        pathes.addAll(getFilesFromDirWithExts(f.getAbsolutePath(), extensions, false));
                    }
                }
            }
        }

        return pathes;
    }

    public static String getFileExtension(String filename) {
        String exts[] = filename.split("\\.");
        if (exts.length == 0) {
            return "";
        } else {
            return exts[exts.length - 1];
        }
    }
}
