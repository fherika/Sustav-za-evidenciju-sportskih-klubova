package util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;

public class XmlLog {

    private static final String LOG_FILE = "doc/log.xml";

    public static void addLog(String action, LogList logList) {
        logList.add(new LogEntry(action));
        saveLogs(logList);
    }

    public static void saveLogs(LogList logList) {
        try {
            JAXBContext context = JAXBContext.newInstance(LogList.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(logList, new File(LOG_FILE));
        } catch (Exception e) {
            System.err.println("Greška pri spremanju XML loga: " + e.getMessage());
        }
    }

    public static LogList loadLogs() {
        try {
            File file = new File(LOG_FILE);
            if (!file.exists()) return new LogList();
            JAXBContext context = JAXBContext.newInstance(LogList.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (LogList) unmarshaller.unmarshal(file);
        } catch (Exception e) {
            System.err.println("Greška pri učitavanju XML loga: " + e.getMessage());
            return new LogList();
        }
    }

    public static void printLogsWithoutTags(LogList logList) {
        logList.getLogs().forEach(entry ->
                System.out.println(entry.getTimestamp() + " - " + entry.getAction())
        );
    }
}
