package util;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@XmlRootElement(name = "log")
public class LogEntry implements Serializable {
    private String timestamp;
    private String action;

    public LogEntry() {}

    public LogEntry(String action) {
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.action = action;
    }

    @XmlElement
    public String getTimestamp() { return timestamp; }

    @XmlElement
    public String getAction() { return action; }

    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public void setAction(String action) { this.action = action; }
}
