package by.it_academy.jd2.golubev_107.mail_sender.controller;

import by.it_academy.jd2.golubev_107.mail_sender.service.IMailService;
import by.it_academy.jd2.golubev_107.mail_sender.service.dto.CreateEmailDto;
import by.it_academy.jd2.golubev_107.mail_sender.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/mail")
public class MailSenderServlet extends HttpServlet {

    private static final String RECIPIENT_TO_PARAM = "recipientsTO";
    private static final String RECIPIENT_CC_PARAM = "recipientsCC";
    private static final String RECIPIENT_BCC_PARAM = "recipientsBCC";
    private static final String TITLE_PARAM = "messageTitle";
    private static final String TEXT_BODY_PARAM = "messageBody";
    private final IMailService mailService = ServiceFactory.getInstance().getMailService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        mailService.create(toDto(req));
    }

    private CreateEmailDto toDto(HttpServletRequest req) {
        return CreateEmailDto.builder()
                             .setRecipientsTo(req.getParameterValues(RECIPIENT_TO_PARAM))
                             .setRecipientsCC(req.getParameterValues(RECIPIENT_CC_PARAM))
                             .setRecipientsBCC(req.getParameterValues(RECIPIENT_BCC_PARAM))
                             .setTitle(req.getParameter(TITLE_PARAM))
                             .setText(req.getParameter(TEXT_BODY_PARAM))
                             .build();
    }
}
