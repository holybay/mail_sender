package by.it_academy.jd2.golubev_107.mail_sender.service.job;

import by.it_academy.jd2.golubev_107.mail_sender.service.IMailSenderService;
import by.it_academy.jd2.golubev_107.mail_sender.service.IMailService;
import by.it_academy.jd2.golubev_107.mail_sender.service.job.impl.MailSendJob;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulerService implements AutoCloseable {

    private final ScheduledExecutorService executorService;

    public SchedulerService(int threadCount, TimeUnit measure, int timeAmount, IMailService mailService,
                            IMailSenderService mailSenderService) {
        executorService = Executors.newScheduledThreadPool(threadCount);
        executorService.scheduleWithFixedDelay(new MailSendJob(mailService, mailSenderService), 0,
                timeAmount, measure);
    }

    @Override
    public void close() throws Exception {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
