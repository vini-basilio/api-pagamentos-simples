package com.pagamentos.simplificado.service;
import com.pagamentos.simplificado.DTO.NotificationDTO;
import com.pagamentos.simplificado.domain.NotificationQueue;
import com.pagamentos.simplificado.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class NotificationService {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.notification}")
    private String notificationUrl;
    private final Queue<NotificationQueue> retryQueue = new ConcurrentLinkedQueue<>();

    @Async
    public void sendNotification(User user, String message) {
        NotificationQueue request = new NotificationQueue(user , message);
        sendWithRetry(request, 0);
    }

    private void sendWithRetry(NotificationQueue request, int attempt){
        try {

            var response = restTemplate.postForEntity(notificationUrl, request, NotificationDTO.class);
            log.info("Notificação enviada com sucesso para {}", request.user().getId());

        } catch (Exception ex) {
            var id = request.user().getId();
            if (attempt < 3) {
                log.warn("Tentativa {} falhou para {}, tentando novamente...", attempt + 1, id) ;
                long calculedDelay = (long) Math.pow(2, attempt);

                CompletableFuture
                        .delayedExecutor(calculedDelay, TimeUnit.MINUTES)
                        .execute(() -> sendWithRetry(request, attempt + 1));

            } else {
                log.error("Falha definitiva na notificação para {}", id);
            }
        }
    }
}
