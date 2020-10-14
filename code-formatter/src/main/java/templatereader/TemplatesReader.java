package templatereader;

import formatter.dirtree.FileManager;
import formatter.exceptions.DialogException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

public class TemplatesReader {

    public static final String welcomeBanner = "Enter the full path to template or directory with templates";
    public static final String unsuccessfulSearchBanner = "Nothing was found";
    public static final String welcomeBanner2 = "Enter the option (number) [0...]";
    public static final String defaultTemplateFileExtension = "properties";
    public static final String defaultTemplateFilePath = "";

    public static String dialog(boolean deep) throws DialogException {
        System.out.println(welcomeBanner);
        System.out.println("Path: ");

        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();

        ArrayList<String> allTemplatePathes = FileManager.getFilesFromDirWithExts(path,
                new ArrayList<>(Arrays.asList(defaultTemplateFileExtension)), deep);

        if (allTemplatePathes.size() == 0) {
            System.out.println(unsuccessfulSearchBanner);
        }
        int i = 0;
        for (; i < allTemplatePathes.size(); i++) {
            System.out.println(i + ". " + allTemplatePathes.get(i));
        }
        System.out.println(i + ". " + "default");

        System.out.println(welcomeBanner2 + ": ");
        int choice = scanner.nextInt();
        if (choice >= 0 && choice < allTemplatePathes.size()){
            if (choice == allTemplatePathes.size()){
                return defaultTemplateFilePath;
            } else {
                return allTemplatePathes.get(choice);
            }
        } else {
            throw new DialogException("Incorrect input");
        }
    }

    public static TemplateProperties getTemplate(String templateFileName){
        TemplateProperties templateProperties = new TemplateProperties();

        try (InputStream input = new FileInputStream(templateFileName)) {
            Properties prop = new Properties();
            prop.load(input);
            templateProperties = getTemplate(prop);
        } catch (IOException ex) {
            System.out.println("EXCEPZTION!!!!");
            ex.printStackTrace();
        }

        return templateProperties;
    }

    private static TemplateProperties getTemplate(Properties propertiesFile) {
        TemplateProperties templateProperties = new TemplateProperties();

        templateProperties.while_parentheses = true;
        templateProperties.before_left_brace_do = true;
        templateProperties.before_while = true;
//        templateProperties.before_while = propertiesFile.getProperty();
//        templateProperties.before_while = propertiesFile.getProperty();
//        templateProperties.before_while = propertiesFile.getProperty();

        return templateProperties;
    }
}
