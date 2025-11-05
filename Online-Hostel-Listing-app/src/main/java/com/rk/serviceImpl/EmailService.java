package com.rk.serviceImpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import java.io.File;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender mailSender;

	public void sendOtpEmail(String to, String otp) throws MessagingException {

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

			String htmlMsg = "<!DOCTYPE html>" + "<html>" + "<head>" + "<meta charset='UTF-8'>"
					+ "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
					+ "<title>HostelHub</title>" + "</head>"
					+ "<body style='font-family: Arial, sans-serif; margin:0; padding:0; background-color:#f3f4f6;'>" +

					"<table width='100%' cellpadding='0' cellspacing='0' style='padding:20px 0;'>" + "  <tr>"
					+ "    <td align='center'>" +

					"      <table width='600' cellpadding='0' cellspacing='0' style='background-color:#ffffff; border-radius:12px; overflow:hidden; box-shadow:0 2px 10px rgba(0,0,0,0.1);'>"
					+

					"        <!-- Header -->" + "        <tr>"
					+ "          <td align='center' style='background-color:#3b82f6; padding:20px;'>"
					+ "            <h1 style='color:#ffffff; margin:0; font-size:24px;'>HostelHub</h1>"
					+ "          </td>" + "        </tr>" +

					"        <!-- Body -->" + "        <tr>" + "          <td style='padding:30px; color:#111827;'>"
					+ "            <p style='font-size:16px;'>👋 Hello,</p>"
					+ "            <p style='font-size:16px;'>You requested a password reset for <strong>HostelHub</strong>. Use the OTP below to reset your password. This OTP is valid for <strong>5 minutes</strong>.</p>"
					+

					"            <div style='text-align:center; margin:25px 0;'>"
					+ "              <span style='font-size:32px; font-weight:bold; color:#ef4444; letter-spacing:2px;'>"
					+ otp + "</span>" + "            </div>" +

					"            <p style='font-size:16px;'>Or click the button below to reset your password:</p>"
					+ "            <div style='text-align:center; margin:20px 0;'>"
					+ "              <a href='https://yourhostelhubapp.com/reset-password' "
					+ "                 style='text-decoration:none; padding:12px 25px; background-color:#3b82f6; color:white; border-radius:8px; font-weight:bold; display:inline-block;'>Reset Password 🔒</a>"
					+ "            </div>" +

					"            <p style='font-size:14px; color:#6b7280;'>If you did not request this, please ignore this email.</p>"
					+ "          </td>" + "        </tr>" +

					"        <!-- Footer -->" + "        <tr>"
					+ "          <td align='center' style='background-color:#f9fafb; padding:15px; font-size:12px; color:#9ca3af;'> "
					+ "            © 2025 HostelHub. All rights reserved." + "          </td>" + "        </tr>" +

					"      </table>" + "    </td>" + "  </tr>" + "</table>" + "</body>" + "</html>";

			helper.setText(htmlMsg, true); // true = HTML
			helper.setTo(to);
			helper.setSubject("HostelHub 🔑 Password Reset OTP");
			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void sendInvoice(String to, String filePath) throws MessagingException {

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setTo(to);
			helper.setSubject("Your Payment Invoice");
			helper.setText("Thank you for your payment. Please find your invoice attached");

			FileSystemResource file = new FileSystemResource(new File(filePath));
			helper.addAttachment("invoice.pdf", file);

			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
