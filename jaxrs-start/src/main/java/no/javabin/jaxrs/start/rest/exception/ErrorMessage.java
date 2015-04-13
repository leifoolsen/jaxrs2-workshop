package no.javabin.jaxrs.start.rest.exception;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

public class ErrorMessage {
    private String code;
    private String title;
    private String message;

    public ErrorMessage(String code, String title, String message) {
        this.code = code;
        this.title = title;
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "code='" + code + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
