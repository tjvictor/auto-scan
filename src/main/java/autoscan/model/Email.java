package autoscan.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Email {
    private String from;
    private String fromDisplay;
    private String[] to;
    private String[] cc;
    private String[] bcc;
    private String subject;
    private String content;
    private String replyTo;
    private String replyToDisplay;
    private boolean html = true;

    public Email() {
    }

    public Email(String[] to, String subject, String content) {
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    public Email(String from, String[] to, String subject, String content) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String[] getTo() {
        return this.to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String[] getCc() {
        return this.cc;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public String[] getBcc() {
        return this.bcc;
    }

    public void setBcc(String[] bcc) {
        this.bcc = bcc;
    }

    public boolean isHtml() {
        return this.html;
    }

    public void setHtml(boolean html) {
        this.html = html;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFromDisplay() {
        return this.fromDisplay;
    }

    public void setFromDisplay(String fromDisplay) {
        this.fromDisplay = fromDisplay;
    }

    public String getReplyTo() {
        return this.replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getReplyToDisplay() {
        return this.replyToDisplay;
    }

    public void setReplyToDisplay(String replyToDisplay) {
        this.replyToDisplay = replyToDisplay;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
