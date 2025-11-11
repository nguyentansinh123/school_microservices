package com.caffein.studentservice.service.emailService;

import com.caffein.studentservice.model.AttendanceRecord;
import com.caffein.studentservice.model.Student;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendAbsenceNotification(Student student, String courseName, String courseCode,
                                        AttendanceRecord.AttendanceStatus status, String date) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(student.getEmail());
            helper.setSubject("Attendance Alert: Absence Recorded - " + courseName);
            helper.setFrom("nsinh2881@gmail.com");

            Context context = new Context();
            context.setVariable("studentName", student.getFirstName() + " " + student.getLastName());
            context.setVariable("courseName", courseName);
            context.setVariable("courseCode", courseCode);
            context.setVariable("date", date);
            context.setVariable("status", status.name());
            context.setVariable("registrationId", student.getRegistrationId());

            String htmlContent = templateEngine.process("absence-notification", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Absence notification email sent to {} for course {}", student.getEmail(), courseName);

        } catch (MessagingException e) {
            log.error("Failed to send absence notification email to {}: {}", student.getEmail(), e.getMessage());
        }
    }

    @Async
    public void sendMultipleAbsencesWarning(Student student, String courseName, long absentCount, long totalClasses) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(student.getEmail());
            helper.setSubject("⚠️ Attendance Warning: Multiple Absences - " + courseName);
            helper.setFrom("nsinh2881@gmail.com");

            Context context = new Context();
            context.setVariable("studentName", student.getFirstName() + " " + student.getLastName());
            context.setVariable("courseName", courseName);
            context.setVariable("absentCount", absentCount);
            context.setVariable("totalClasses", totalClasses);
            context.setVariable("attendanceRate", Math.round((totalClasses - absentCount) * 100.0 / totalClasses));

            String htmlContent = templateEngine.process("multiple-absences-warning", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Multiple absences warning email sent to {} for course {}", student.getEmail(), courseName);

        } catch (MessagingException e) {
            log.error("Failed to send multiple absences warning email to {}: {}", student.getEmail(), e.getMessage());
        }
    }
}