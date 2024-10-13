package by.it_academy.jd2.golubev_107.mail_sender.service.job.impl;

import by.it_academy.jd2.golubev_107.mail_sender.service.IMailSenderService;
import by.it_academy.jd2.golubev_107.mail_sender.service.IMailService;
import by.it_academy.jd2.golubev_107.mail_sender.service.dto.EmailOutDto;
import by.it_academy.jd2.golubev_107.mail_sender.storage.entity.EmailStatus;

import java.util.List;

public class MailSendJob implements Runnable {

    private final IMailService mailService;
    private final IMailSenderService mailSenderService;

    public MailSendJob(IMailService mailService, IMailSenderService mailSenderService) {
        this.mailService = mailService;
        this.mailSenderService = mailSenderService;
    }

    @Override
    public void run() {
        List<EmailOutDto> allByStatus = mailService.getAllByStatus(EmailStatus.EStatus.LOADED);
        for (EmailOutDto email : allByStatus) {
            try {
                mailService.updateStatus(email.getId(), EmailStatus.EStatus.SENDING);
                mailSenderService.send(email);
                mailService.updateStatus(email.getId(), EmailStatus.EStatus.FINISH);
            } catch (Exception e) {
                mailService.updateStatus(email.getId(), EmailStatus.EStatus.ERROR);
                System.out.println("~~~~~ERROR~~~~~\n" + e.getMessage());
            }
        }
    }
}
