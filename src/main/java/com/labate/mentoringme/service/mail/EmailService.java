package com.labate.mentoringme.service.mail;

import com.labate.mentoringme.dto.context.AbstractEmailContext;

import javax.mail.MessagingException;

public interface EmailService {

  void sendMail(final AbstractEmailContext email) throws MessagingException;
}
