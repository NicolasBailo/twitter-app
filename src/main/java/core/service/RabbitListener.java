package core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import core.db.model.GeneratedTweetDto;
import core.db.model.SearchedTweetDto;
import core.db.model.TweetJsonDto;
import core.utils.Encryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.util.MimeTypeUtils;

import java.util.HashMap;
import java.util.Map;

public class RabbitListener implements Runnable{


    private EncryptService encryptService;

    private final String ENV_AMQPURL_NAME = "CLOUDAMQP_URL";

    public RabbitListener(SimpMessageSendingOperations sender, String exchangeName, EncryptService encryptService) {
        this.sender = sender;
        this.exchangeName = exchangeName;
        this.encryptService = encryptService;
    }

    private SimpMessageSendingOperations sender;
    private String exchangeName;



    @Override
    public void run() {
        // Conexión al broker RabbitMQ broker (prueba en la URL de
        // la variable de entorno que se llame como diga ENV_AMQPURL_NAME
        // o sino en localhost)
        ConnectionFactory factory = new ConnectionFactory();
        String amqpURL = System.getenv().get(ENV_AMQPURL_NAME) != null ? System.getenv().get(ENV_AMQPURL_NAME) : "amqp://localhost";
        try {
            factory.setUri(amqpURL);
        } catch (Exception e) {
            System.out.println(" [*] AQMP broker not found in " + amqpURL);
            System.exit(-1);
        }
        System.out.println(" [*] AQMP broker found in " + amqpURL);
        try{
            Connection connection = factory.newConnection();
            // Con un solo canal
            Channel channel = connection.createChannel();

            // Declaramos una centralita de tipo fanout llamada EXCHANGE_NAME
            channel.exchangeDeclare(exchangeName, "fanout",true);
            // Creamos una nueva cola temporal (no durable, exclusiva y
            // que se borrará automáticamente cuando nos desconectemos
            // del servidor de RabbitMQ). El servidor le dará un
            // nombre aleatorio que guardaremos en queueName
            String queueName = channel.queueDeclare().getQueue();
            // E indicamos que queremos que la centralita EXCHANGE_NAME
            // envíe los mensajes a la cola recién creada. Para ello creamos
            // una unión (binding) entre ellas (la clave de enrutado
            // la ponemos vacía, porque se va a ignorar)
            channel.queueBind(queueName, exchangeName, "");


            System.out.println(" [*] Esperando mensajes. CTRL+C para salir");

            // El objeto consumer guardará los mensajes que lleguen
            // a la cola queueName hasta que los usemos
            QueueingConsumer consumer = new QueueingConsumer(channel);
            // autoAck a true

            channel.basicConsume(queueName, true, consumer);

            while (true) {
                // bloquea hasta que llege un mensaje
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                String message = new String(delivery.getBody());
                System.out.println(" [x] Recibido '" + message + "'");

                Map<String, Object> map = new HashMap<>();
                map.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
                ObjectMapper mapper = new ObjectMapper();



                if(message!=null){
                    GeneratedTweetDto generatedTweetDto = encryptService.encryptTweet(mapper.readValue(message, SearchedTweetDto.class));
                    //sender.convertAndSend("/queue/search/" + exchangeName, generatedTweetDto);
                }

            }
        }catch(Exception e){
            e.printStackTrace();
        }


    }
}
