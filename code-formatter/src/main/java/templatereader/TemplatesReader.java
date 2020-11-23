package templatereader;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;
import formatter.dirtree.FileManager;
import formatter.exceptions.DialogException;
import javafx.util.Pair;
import templatereader.util.BracesPlacement;
import templatereader.util.TemplateTypes;

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
            System.out.println("Exception in reading property file.");
            System.out.println("Improper key or value.");
            ex.printStackTrace();
        }

        return templateProperties;
    }

    private static TemplateProperties getTemplate(Properties propertiesFile) throws NoSuchFieldException, IllegalAccessException {
        TemplateProperties templateProperties = new TemplateProperties();

        // tested, pass
//        Set keys = propertiesFile.keySet();
//        Iterator it = keys.iterator();
//        while(it.hasNext()){
//            String key = (String) it.next();
//            setTemplateFieldFromProperties(templateProperties, propertiesFile, key);
//        }

        templateProperties.before_while_parentheses = true;
        templateProperties.before_left_brace_do = true;
        templateProperties.before_while = true;

        templateProperties.before_for_parentheses = true; // +
        templateProperties.before_left_brace_for = true;
        templateProperties.within_for_parenth = true;
        templateProperties.around_unary_ops = false;
        templateProperties.around_assignment_ops = true;
        templateProperties.other_before_for_semicolon = true;
        templateProperties.other_after_for_semicolon = true;

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

        templateProperties.class_structure_bf_base_class_colon = true;
        templateProperties.class_structure_af_base_class_colon = true;
        templateProperties.other_before_comma = true;
        templateProperties.other_after_comma = true;

        templateProperties.within_function_declaration_parenth = true;
        templateProperties.before_function_declaration_parentheses = true;
        templateProperties.within_empty_function_declaration_parenth = true;

        templateProperties.minimum_blank_lines_before_includes = 0;
        templateProperties.minimum_blank_lines_after_includes = 0;
        templateProperties.minimum_blank_lines_around_class_structure = 4;
        templateProperties.minimum_blank_lines_before_function_body = 1;

        templateProperties.keep_max_blank_lines_in_declarations = 0;
        templateProperties.keep_max_blank_lines_in_code = 0;
        templateProperties.keep_max_blank_lines_in_before_right_brace = 0;

        templateProperties.minimum_blank_lines_after_class_structure_header = 0;
        templateProperties.minimum_blank_lines_around_field = 0;
        templateProperties.minimum_blank_lines_around_global_variable = 0;
        templateProperties.minimum_blank_lines_around_function_declaration = 0;
        templateProperties.minimum_blank_lines_around_function_definition = 0;

        return templateProperties;
    }

    private static void setTemplateFieldFromProperties(TemplateProperties templateProperties, Properties properties, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = templateProperties.getClass().getDeclaredField(fieldName);
        String strValue = properties.getProperty(fieldName);

        TemplateTypes tt = getTemplateType(strValue);
        if (tt == TemplateTypes.BOOLEAN){
            field.setBoolean(templateProperties, stringToBoolean(strValue));
        } else if (tt == TemplateTypes.BRACE_PLACEMENT){
            field.set(templateProperties, BracesPlacement.valueOf(strValue));
        } else if (tt == TemplateTypes.INTEGER){
            field.setInt(templateProperties, Integer.parseInt(strValue));
        }
    }

    private static boolean stringToBoolean(String string){
        if (string == null){
            return false;
        }
        if (string.toLowerCase().equals("true")){
            return true;
        }

        return false;
    }

    private static TemplateTypes getTemplateType(String value){
        if (value == null){
            return TemplateTypes.NONE;
        }

        if (value.toLowerCase().equals("true") || value.toLowerCase().equals("false")){
            return TemplateTypes.BOOLEAN;
        }

        BracesPlacement bp[] = BracesPlacement.values();
        for (int i=0; i<bp.length; i++){
            if (bp[i].name().equals(value)){
                return TemplateTypes.BRACE_PLACEMENT;
            }
        }

        try{
            Integer.parseInt(value);
            return TemplateTypes.INTEGER;
        } catch (Exception e){}

        return TemplateTypes.NONE;
    }

}
