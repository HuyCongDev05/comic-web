package com.example.projectrestfulapi.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class EmailVerificationService {
    private final String email;
    private final JavaMailSender mailSender;
    private final StringRedisTemplate stringRedisTemplate;

    public EmailVerificationService(@Value("${spring.mail.username}") String email, JavaMailSender mailSender, StringRedisTemplate stringRedisTemplate) {
        this.email = email;
        this.mailSender = mailSender;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void handleSave(String toEmail, String otpCode) {
        stringRedisTemplate.opsForValue().set(toEmail, otpCode, 90, TimeUnit.SECONDS);
    }

    public boolean handleExistsByEmail(String toEmail) {
        return stringRedisTemplate.hasKey(toEmail);
    }

    public boolean handleVerification(String toEmail, String otpCode) {
        String otp = stringRedisTemplate.opsForValue().get(toEmail);
        if (otpCode.equals(otp)) {
            stringRedisTemplate.delete(toEmail);
            return true;
        }
        return false;
    }

    @Async
    public void sendOTP(String toEmail, String otpCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(email);
        helper.setTo(toEmail);
        helper.setSubject("Mã OTP xác thực tài khoản");

        String htmlContent = String.format("""
                <div style="font-family: 'Segoe UI', Arial, sans-serif; background: #ffffff; padding: 30px; text-align: center; color: #333; max-width: 420px; margin: auto; border-radius: 16px; box-shadow: 0 4px 20px rgba(0,0,0,0.08);">
                
                        <h2 style="margin-bottom: 8px; font-size: 24px; font-weight: 600; color: #4A90E2;">Mã Xác Thực</h2>
                        <p style="margin: 0; font-size: 15px; color: #555;">Sử dụng mã dưới đây để xác thực tài khoản của bạn</p>
                
                        <button id="copyCodeBtn"
                                style="margin-top: 20px; padding: 10px 20px; background: linear-gradient(135deg, #4A90E2, #50E3C2);
                                       color: white; border: none; border-radius: 8px; font-size: 20px; font-weight: 600;
                                       letter-spacing: 4px; cursor: pointer; box-shadow: 0 4px 8px rgba(0,0,0,0.15); transition: transform 0.1s;">
                             %s
                        </button>
                
                        <div id="copyMsg" style="margin-top: 12px; font-size: 13px; color: #28a745; display: none;">
                            Bạn đã sao chép mã!
                        </div>
                
                        <div style="margin: 25px 0;">
                            <img src="https://i.pinimg.com/400x300/30/f5/92/30f592cda96b9db4c92cddd47b473780.jpg"
                                 alt="Xác Thực" style="width: 200px; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);"/>
                        </div>
                        <p style="font-size: 14px; color: #999; margin-top: 15px;">
                            Mã này có hiệu lực trong 1 phút 30 giây
                        </p>
                        <p style="font-size: 12px; color: #999; margin-top: 15px;">
                            Nếu bạn không yêu cầu mã này, bạn có thể bỏ qua email này.
                        </p>
                    </div>
                
                    <script>
                        const copyBtn = document.getElementById('copyCodeBtn');
                        const copyMsg = document.getElementById('copyMsg');
                
                        copyBtn.addEventListener('click', async () => {
                            const code = copyBtn.textContent.trim();
                
                            try {
                                if (navigator.clipboard && navigator.clipboard.writeText) {
                                    await navigator.clipboard.writeText(code);
                                } else {
                                    // fallback cho các trình duyệt cũ
                                    const textArea = document.createElement('textarea');
                                    textArea.value = code;
                                    document.body.appendChild(textArea);
                                    textArea.select();
                                    document.execCommand('copy');
                                    document.body.removeChild(textArea);
                                }
                
                                copyMsg.style.display = 'block';
                                copyBtn.style.transform = 'scale(0.95)';
                
                                setTimeout(() => {
                                    copyMsg.style.display = 'none';
                                    copyBtn.style.transform = 'scale(1)';
                                }, 1500);
                            } catch (err) {
                                alert('Không thể sao chép mã. Hãy thử lại!');
                                console.error(err);
                            }
                        });
                    </script>
                """, otpCode);

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
