package util;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "logs")
public class LogList implements Serializable {
    private List<LogEntry> logs = new ArrayList<>();

    @XmlElement(name = "log")
    public List<LogEntry> getLogs() { return logs; }

    public void add(LogEntry entry) { logs.add(entry); }
}
