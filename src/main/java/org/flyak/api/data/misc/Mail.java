package org.flyak.api.data.misc;

import java.util.List;
import java.util.Map;

public class Mail {
    private String mailTo;
    private String subject;
    private List<Object> attachments;
    private Map<String, Object> props;
    private String template;

    public Mail(String mailTo, String subject, String template) {
        this.mailTo = mailTo;
        this.subject = subject;
        this.template = template;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<Object> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Object> attachments) {
        this.attachments = attachments;
    }

    public Map<String, Object> getProps() {
        return props;
    }

    public void setProps(Map<String, Object> props) {
        this.props = props;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
