/** 
 *  Copyright (C) 2012  Just Do One More
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */package com.jdom.util.email;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * This class provides emailing capabilities.
 * 
 * @author djohnson
 * 
 */
public class Emailer {

    public static boolean email(String mailServer, String mailUsername, String mailPassword,
            List<String> emailAddresses, String emailSubject, String emailMessage) {
        return email(mailServer, 25, mailUsername, mailPassword, emailAddresses, emailSubject, emailMessage);
    }

    public static boolean email(String mailServer, int port, String mailUsername, String mailPassword,
            List<String> emailAddresses, String emailSubject, String emailMessage) {

        return Emailer.email(new Email(mailServer, port, mailUsername, mailPassword, emailAddresses, emailSubject,
                emailMessage));

    }

    public static boolean email(Email email) {
        Properties mailProperties = new Properties();
        Session sMail = null;
        MimeMessage mMessage = null;

        String port = email.getPort() + "";

        // Set properties
        mailProperties.setProperty("mail.transport.protocol", "smtp");
        mailProperties.setProperty("mail.smtp.port", port);
        mailProperties.setProperty("mail.host", email.getServer());
        mailProperties.setProperty("mail.user", email.getServerUser());
        mailProperties.setProperty("mail.password", email.getServerPass());
        mailProperties.setProperty("mail.from", email.getServerUser());

        try {
            // Make session
            sMail = Session.getDefaultInstance(mailProperties, null);

            mMessage = new MimeMessage(sMail);

            mMessage.setSubject(email.getSubject());

            mMessage.setText(email.getBody());

            mMessage.setSentDate(new Date());

            mMessage.setFrom(new InternetAddress(email.getServerUser()));

            for (String address : email.getEmailAddresses()) {
                mMessage.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(address));
            }

            Transport.send(mMessage);
        } catch (Exception eError) {
            return false;
        }

        return true;
    }

}
