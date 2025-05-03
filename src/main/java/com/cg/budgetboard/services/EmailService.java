package com.cg.budgetboard.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;

import java.io.File;

@Controller
public class EmailService {

    @Autowired
    JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("Budget Board <budgetboard04@gmail.com>");
            helper.setTo(toEmail);
            helper.setSubject("Forget Password OTP");

            String content = "<html>" +
                    "<body style='font-family:Arial, sans-serif; background-color:#f9f9f9; margin:0; padding:0;'>" +
                    "  <table width='100%' style='max-width:600px; margin:auto; background-color:#ffffff; border:1px solid #ddd;'>" +
                    "    <tr>" +
                    "      <td style='text-align:center; padding:10px 0;'>" +
                    "        <img src='cid:bannerImage' alt='Budget Board Banner' style='width:100%; max-width:600px; height:auto;'/>" +
                    "      </td>" +
                    "    </tr>" +
                    "    <tr>" +
                    "      <td style='padding:20px;'>" +
                    "        <p style='font-size:16px; color:#333;'>Hello From <strong>Budget Board</strong>!</p>" +
                    "        <p style='font-size:16px; color:#333;'>Your OTP for resetting your password:</p>" +
                    "        <h2 style='color:#1976d2; font-size:28px;'>" + otp + "</h2>" +
                    "        <p style='font-size:16px; color:#333;'>Please enter this OTP to reset your account password.</p>" +
                    "      </td>" +
                    "    </tr>" +
                    "  </table>" +
                    "</body>" +
                    "</html>";

            helper.setText(content, true);
            FileSystemResource image = new FileSystemResource(new File("src/main/resources/banner/BudgetboardBanner.png"));
            helper.addInline("bannerImage", image);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    public void sendAlertEmail(String toEmail, String percentage, String category) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("Budget Board <budgetboard04@gmail.com>");
            helper.setTo(toEmail);
            helper.setSubject("Expense Alert From Budget Board");

            String content = "<html>" +
                    "<body style='font-family:Arial, sans-serif; background-color:#f9f9f9; margin:0; padding:0;'>" +
                    "  <table width='100%' style='max-width:600px; margin:auto; background-color:#ffffff; border:1px solid #ddd;'>" +
                    "    <tr>" +
                    "      <td style='text-align:center; padding:10px 0;'>" +
                    "        <img src='cid:bannerImage' alt='Budget Board Banner' style='width:100%; max-width:600px; height:auto;'/>" +
                    "      </td>" +
                    "    </tr>" +
                    "    <tr>" +
                    "      <td style='padding:20px;'>" +
                    "        <p style='font-size:16px; color:#333;'>Hello From <strong>Budget Board</strong>!</p>" +
                    "        <p style='font-size:16px; color:#333;'>You have crossed your <span style='color:red;'>" + percentage + "</span> of budget for category <span style='color:blue;'>" + category + "</span></p>" +
                    "        <p style='font-size:16px; color:#333;'>Please manage your expenses accordingly.</p>" +
                    "      </td>" +
                    "    </tr>" +
                    "  </table>" +
                    "</body>" +
                    "</html>";

            helper.setText(content, true);
            FileSystemResource image = new FileSystemResource(new File("src/main/resources/banner/BudgetboardBanner.png"));
            helper.addInline("bannerImage", image);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    public void sendReportEmail(String to, String subject, byte[] pdfBytes) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("Budget Board <budgetboard04@gmail.com>");
            helper.setTo(to);
            helper.setSubject(subject);

            String content = "<html>" +
                    "<body style='font-family:Arial, sans-serif; background-color:#f9f9f9; margin:0; padding:0;'>" +
                    "  <table width='100%' style='max-width:600px; margin:auto; background-color:#ffffff; border:1px solid #ddd;'>" +
                    "    <tr>" +
                    "      <td style='text-align:center; padding:10px 0;'>" +
                    "        <img src='cid:bannerImage' alt='Budget Board Banner' style='width:100%; max-width:600px; height:auto;'/>" +
                    "      </td>" +
                    "    </tr>" +
                    "    <tr>" +
                    "      <td style='padding:20px;'>" +
                    "        <p style='font-size:16px; color:#333;'>Hello From <strong>Budget Board</strong>!</p>" +
                    "        <p style='font-size:16px; color:#333;'>Please find the report attached.</p>" +
                    "      </td>" +
                    "    </tr>" +
                    "  </table>" +
                    "</body>" +
                    "</html>";

            helper.setText(content, true);
            FileSystemResource image = new FileSystemResource(new File("src/main/resources/banner/BudgetboardBanner.png"));
            helper.addInline("bannerImage", image);

            helper.addAttachment("report.pdf", new ByteArrayResource(pdfBytes));
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}

