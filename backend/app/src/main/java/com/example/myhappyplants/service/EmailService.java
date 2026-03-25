package com.example.myhappyplants.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.myhappyplants.entity.User;
import com.example.myhappyplants.entity.UserPlant;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String toEmail, String token) {
        String verifyUrl = "http://localhost:5173/verify?token=" + token;

        String html = """
                <!DOCTYPE html>
                <html>
                <body style="margin:0;padding:0;background:#f4f4f4;font-family:Arial,sans-serif;">
                  <table width="100%%" cellpadding="0" cellspacing="0">
                    <tr><td align="center" style="padding:40px 0;">
                      <table width="520" cellpadding="0" cellspacing="0"
                             style="background:#ffffff;border-radius:8px;overflow:hidden;">

                        <tr>
                          <td style="background:#2e7d32;padding:32px;text-align:center;">
                            <h1 style="margin:0;color:#ffffff;font-size:24px;">🌱 MyHappyPlants</h1>
                          </td>
                        </tr>

                        <tr>
                          <td style="padding:40px 32px;">
                            <h2 style="margin:0 0 12px;color:#1b5e20;">Verify your account</h2>
                            <p style="margin:0 0 24px;color:#555;line-height:1.6;">
                              Thanks for signing up! Click the button below to verify your
                              email address and activate your account.
                            </p>
                            <table cellpadding="0" cellspacing="0">
                              <tr>
                                <td style="background:#2e7d32;border-radius:6px;">
                                  <a href="%s"
                                     style="display:inline-block;padding:14px 32px;
                                            color:#ffffff;text-decoration:none;
                                            font-weight:bold;font-size:15px;">
                                    Verify Email
                                  </a>
                                </td>
                              </tr>
                            </table>
                          </td>
                        </tr>

                        <tr>
                          <td style="background:#f9f9f9;padding:20px 32px;
                                     border-top:1px solid #eee;text-align:center;">
                            <p style="margin:0;color:#aaa;font-size:12px;">
                              © MyHappyPlants — keeping your plants happy 🌿
                            </p>
                          </td>
                        </tr>

                      </table>
                    </td></tr>
                  </table>
                </body>
                </html>
                """.formatted(verifyUrl);

        sendHtml(toEmail, "Verify your MyHappyPlant account", html,
                "Could not send verification email, please try again later");
    }

    public void sendPasswordResetEmail(String toEmail, String token) {
        String resetUrl = "http://localhost:5173/reset-password?token=" + token;

        String html = """
            <!DOCTYPE html>
            <html>
            <body style="margin:0;padding:0;background:#f4f4f4;font-family:Arial,sans-serif;">
              <table width="100%%" cellpadding="0" cellspacing="0">
                <tr><td align="center" style="padding:40px 0;">
                  <table width="520" cellpadding="0" cellspacing="0"
                         style="background:#ffffff;border-radius:8px;overflow:hidden;">
                    <tr>
                      <td style="background:#2e7d32;padding:32px;text-align:center;">
                        <h1 style="margin:0;color:#ffffff;font-size:24px;">🌱 MyHappyPlants</h1>
                      </td>
                    </tr>
                    <tr>
                      <td style="padding:40px 32px;">
                        <h2 style="margin:0 0 12px;color:#1b5e20;">Reset your password</h2>
                        <p style="margin:0 0 24px;color:#555;line-height:1.6;">
                          We received a request to reset your password. Click the button below to choose a new one.
                          This link will expire in 15 minutes.
                        </p>
                        <table cellpadding="0" cellspacing="0">
                          <tr>
                            <td style="background:#2e7d32;border-radius:6px;">
                              <a href="%s"
                                 style="display:inline-block;padding:14px 32px;
                                        color:#ffffff;text-decoration:none;
                                        font-weight:bold;font-size:15px;">
                                Reset Password
                              </a>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </td></tr>
              </table>
            </body>
            </html>
            """.formatted(resetUrl);

        sendHtml(toEmail, "Reset your MyHappyPlants password", html,
                "Could not send password reset email");
    }

    public void sendWaterNotification(User user, List<UserPlant> plants) {
        String plantRows = plants.stream()
                .map(p -> """
                        <tr>
                          <td style="padding:12px 0;border-bottom:1px solid #e8f5e9;">
                            <strong style="color:#1b5e20;">%s</strong><br>
                            <span style="color:#777;font-size:13px;">
                              Water every %d days &nbsp;·&nbsp; due: %s
                            </span>
                          </td>
                        </tr>
                        """.formatted(p.getNickname(), p.getWaterFrequency(), p.getNextWaterDate()))
                .collect(Collectors.joining());

        String html = """
                <!DOCTYPE html>
                <html>
                <body style="margin:0;padding:0;background:#f4f4f4;font-family:Arial,sans-serif;">
                  <table width="100%%" cellpadding="0" cellspacing="0">
                    <tr><td align="center" style="padding:40px 0;">
                      <table width="520" cellpadding="0" cellspacing="0"
                             style="background:#ffffff;border-radius:8px;overflow:hidden;">

                        <tr>
                          <td style="background:#2e7d32;padding:32px;text-align:center;">
                            <h1 style="margin:0;color:#ffffff;font-size:24px;">🌱 MyHappyPlants</h1>
                          </td>
                        </tr>

                        <tr>
                          <td style="padding:40px 32px;">
                            <h2 style="margin:0 0 8px;color:#1b5e20;">Time to water! 💧</h2>
                            <p style="margin:0 0 24px;color:#555;line-height:1.6;">
                              Hi <strong>%s</strong>, the following plants need watering today:
                            </p>
                            <table width="100%%" cellpadding="0" cellspacing="0">
                              %s
                            </table>
                          </td>
                        </tr>

                        <tr>
                          <td style="background:#f9f9f9;padding:20px 32px;
                                     border-top:1px solid #eee;text-align:center;">
                            <p style="margin:0;color:#aaa;font-size:12px;">
                              © MyHappyPlants — keeping your plants happy 🌿
                            </p>
                          </td>
                        </tr>

                      </table>
                    </td></tr>
                  </table>
                </body>
                </html>
                """.formatted(user.getUsername(), plantRows);

        sendHtml(user.getEmail(), "Your plants need watering today 💧", html,
                "Could not send the watering notification");
    }

    private void sendHtml(String to, String subject, String html, String errorMessage) {
        try {
            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, false, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(mime);
        } catch (MessagingException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, errorMessage);
        }
    }
}
