package org.flyak.api.service;

import org.flyak.api.data.misc.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@EnableAsync
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;
    private Logger log = LoggerFactory.getLogger(EmailService.class);

    public EmailService(JavaMailSender javaMailSender, SpringTemplateEngine springTemplateEngine) {
        this.javaMailSender = javaMailSender;
        this.springTemplateEngine = springTemplateEngine;
    }

    @Async
    public void generateEmailRequest(String to, String subject, String template, Map<String,Object> props) {
        Mail mail = new Mail(to, subject, template);
        mail.setProps(props);
        try {
            this.sendEmail(mail);
        } catch(MessagingException e) {
            log.error(String.format("Caught MessagingException during registration of %s, %s", to, e.getLocalizedMessage()));
        } catch(IOException e) {
            log.error(String.format("Caught IOException during registration of %s, %s", to, e.getLocalizedMessage()));
        }
    }

    @Async
    public void sendEmail(Mail mail) throws MessagingException, IOException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        Context context = new Context();
        context.setVariables(mail.getProps());

        String html = springTemplateEngine.process(mail.getTemplate(), context);

        mimeMessageHelper.setTo(mail.getMailTo());
        mimeMessageHelper.setFrom(new InternetAddress("FlyAK <no-reply@flyak.org>"));
        mimeMessageHelper.setSubject(mail.getSubject());
        mimeMessageHelper.setText(html, true);

        javaMailSender.send(mimeMessage);
    }
}
