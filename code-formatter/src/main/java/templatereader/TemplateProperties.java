package templatereader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TemplateProperties {
    public TemplateProperties(String pathToTemplate) {
        this.readProps();
    }

    private void readProps(){
        try (InputStream input = new FileInputStream("path/to/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            System.out.println(prop.getProperty("db.url"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
