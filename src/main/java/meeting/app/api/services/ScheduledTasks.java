package meeting.app.api.services;

import lombok.extern.slf4j.Slf4j;
import meeting.app.api.model.event.EventItem;
import meeting.app.api.model.user.UserEntity;
import meeting.app.api.repositories.EventItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

@Transactional
@Component
@Slf4j
@EnableAsync
public class ScheduledTasks {

    @Autowired
    private EventItemRepository eventItemRepository;

    @Autowired
    EmailService emailService;

    @Async
    @Scheduled(cron = "0 0 */12 * * *") // 0 0 */12 * * * <- every 12h  ---  */10 * * * * * <- every 10 sec
    public void sendIncomingEventsNotifications() {
        Iterable<EventItem> events = eventItemRepository.findAll();
        for (EventItem eventItem : events) {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());
            Calendar tomorrow = Calendar.getInstance();
            tomorrow.setTime(new Date());
            tomorrow.add(Calendar.DATE, 1);
            Calendar eventTime = Calendar.getInstance();
            eventTime.setTime(eventTime.getTime());
            if ((now.before(eventTime) || now.equals(eventTime)) && (tomorrow.after(eventTime) || tomorrow.equals(eventTime))) {
                if (!eventItem.getActiveParticipants().isEmpty() && eventItem.isActive()) {
                    String[] address = new String[eventItem.getActiveParticipants().size()];
                    int i = 0;
                    for (UserEntity userEntity : eventItem.getActiveParticipants()) {
                        address[i++] = userEntity.getEmail();
                    }
                    emailService.sendEventNotification(address, eventItem);
                }
            }
        }
    }
}
