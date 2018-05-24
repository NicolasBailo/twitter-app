package core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
public class SessionUnsubscribeEventListener implements ApplicationListener<SessionUnsubscribeEvent> {

    @Autowired
    TwitterLookupService twitter;

    @Override
    public void onApplicationEvent(SessionUnsubscribeEvent sessionUnsubscribeEvent) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionUnsubscribeEvent.getMessage());
        twitter.cancelSearch(headerAccessor);
    }

}