package readers;

import java.io.*;
import java.util.Properties;

public class PropertiesReader {

    private Properties properties;
    private File propertiesFile;

    public PropertiesReader(String path) {
        this(new File(path));
    }

    public PropertiesReader(File propertiesFile) {
        this.propertiesFile = propertiesFile;
        properties = new Properties();
    }

    public Properties readProperties() throws FileNotFoundException {
        FileReader reader = new FileReader(propertiesFile);
        try {
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public void writeProperties() throws IOException {
        properties.store(new FileWriter(propertiesFile),null);
    }

    public boolean propertiesFileExists(){
        return propertiesFile.exists();

    }

    public Properties getProperties() {
        return properties;
    }

}



