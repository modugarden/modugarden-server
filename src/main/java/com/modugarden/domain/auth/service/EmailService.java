package com.modugarden.domain.auth.service;

import com.modugarden.common.error.exception.custom.BusinessException;
import com.modugarden.domain.auth.dto.request.EmailAuthRequestDto;
import com.modugarden.domain.auth.dto.response.EmailAuthResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import static com.modugarden.common.error.enums.ErrorMessage.SEND_EMAIL_ERROR;

@Service
@RequiredArgsConstructor
public class EmailService {
    //의존성 주입을 통해서 필요한 객체를 가져온다.
    private final JavaMailSender emailSender;
    // 타임리프를사용하기 위한 객체를 의존성 주입으로 가져온다
    private final SpringTemplateEngine templateEngine;
    private String authNum; //랜덤 인증 코드

    //랜덤 인증 코드 생성
    private void createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i=0;i<8;i++) { // 8자리
            int index = random.nextInt(3);

            switch (index) {
                case 0 : // 소문자
                    key.append((char) ((int)random.nextInt(26) + 97));
                    break;
                case 1: // 대문자
                    key.append((char) ((int)random.nextInt(26) + 65));
                    break;
                case 2: // 0-9 숫자
                    key.append(random.nextInt(9));
                    break;
            }
        }
        authNum = key.toString();
    }

    //메일 양식 작성
    private MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException {

        createCode(); //인증 코드 생성
        String setFrom = "modugarden2023@gmail.com"; //email-config에 설정한 자신의 이메일 주소(보내는 사람)
        String toEmail = email; //받는 사람
        String title = "모두의 정원 회원가입 인증 번호입니다."; //제목

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, toEmail); //보낼 이메일 설정
        message.setSubject(title); //제목 설정
        message.setFrom(setFrom); //보내는 이메일
        message.setText(setContext(authNum), "utf-8", "html");

        return message;
    }

    //실제 메일 전송
    public EmailAuthResponseDto sendEmail(EmailAuthRequestDto requestDto) throws BusinessException {
        try{
            //메일전송에 필요한 정보 설정
            MimeMessage emailForm = createEmailForm(requestDto.getEmail());
            //실제 메일 전송
            emailSender.send(emailForm);

            return new EmailAuthResponseDto(authNum); //인증 코드 반환
        }catch (UnsupportedEncodingException | MessagingException e){
            throw new BusinessException(SEND_EMAIL_ERROR);
        }

    }

    //타임리프를 이용한 context 설정
    private String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("mail", context); //mail.html
    }
}
