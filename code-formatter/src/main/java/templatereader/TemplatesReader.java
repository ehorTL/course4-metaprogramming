package templatereader;

import formatter.dirtree.FileManager;
import formatter.exceptions.DialogException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

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

    public static TemplateProperties getTemplate(String templateFileName) throws NoSuchFieldException, IllegalAccessException {
        TemplateProperties templateProperties = new TemplateProperties();

        try (InputStream input = new FileInputStream(templateFileName)) {
            Properties prop = new Properties();
            prop.load(input);
            templateProperties = getTemplate(prop);
        } catch (IOException ex) {
            System.out.println("Exception in reading property file");
            ex.printStackTrace();
        }

        return templateProperties;
    }

    private static TemplateProperties getTemplate(Properties propertiesFile) throws NoSuchFieldException, IllegalAccessException {
        TemplateProperties templateProperties = new TemplateProperties();

//        Set keys = propertiesFile.keySet();
//        Iterator it = keys.iterator();
//        while(it.hasNext()){
//            String key = (String) it.next();
//            setTemplateFieldFromProperties(templateProperties, propertiesFile, key);
//        }

        templateProperties.before_while_parentheses = true;
        templateProperties.before_left_brace_do = true;
        templateProperties.before_while = true;

        templateProperties.in_tern_op_bf_colon = true;
        templateProperties.in_tern_op_af_colon = true;
        templateProperties.in_tern_op_bf_qm = true;
        templateProperties.in_tern_op_af_qm = true;


        templateProperties.template_within_brackets = true;
        templateProperties.template_within_empty_brackets = true;
        templateProperties.template_before_left_angle_bracket = true;

        templateProperties.before_left_brace_namespace = true;

        templateProperties.around_shift_ops = true;
        templateProperties.around_relational_ops = true;
        templateProperties.around_equality_ops = true;

        return templateProperties;
    }

    private static void setTemplateFieldFromProperties(TemplateProperties templateProperties, Properties properties, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = templateProperties.getClass().getDeclaredField(fieldName);
        boolean val = stringToBoolean(properties.getProperty(fieldName));
        field.setBoolean(templateProperties, val);
    }

    private static boolean stringToBoolean(String string){
        if (string == null){
            return false;
        }
        if (string.equals("1") || string.toLowerCase().equals("true")){
            return true;
        }

        return false;
    }
}
