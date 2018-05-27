package core.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitService {

    //private final String exchangeName = "processor1";

    private ConnectionFactory factory;

    @Value("${spring.rabbitmq.host}")
    private String rabbitHost;

    @Value("${spring.rabbitmq.username}")
    private String rabbitUsername;

    @Value("${spring.rabbitmq.password}")
    private String rabbitPassword;

    @Value("${spring.rabbitmq.virtual-host}")
    private String rabbitVHost;




    public void createConnectionFactory() {
        factory = new ConnectionFactory();
        String amqpURL = "amqp://" + rabbitVHost + ":" + rabbitPassword + "@" + rabbitHost + "/" + rabbitUsername;
        try {
            factory.setUri(amqpURL);
        } catch (Exception e) {
            System.out.println(" [*] AQMP broker NOT found in " + amqpURL);
            System.exit(-1);
        }
        System.out.println(" [*] AQMP broker found in " + amqpURL);
    }

    public void publish(String message, String exchangeName) throws Exception{
        // Conexión al broker RabbitMQ broker (prueba en la URL de
        // la variable de entorno que se llame como diga ENV_AMQPURL_NAME
        // o sino en localhost)

        if(factory == null){
            createConnectionFactory();
        }

        Connection connection = factory.newConnection();
        // Con un solo canal
        Channel channel = connection.createChannel();

        // Declaramos una centralita de tipo fanout llamada EXCHANGE_NAME


        channel.exchangeDeclare(exchangeName, "fanout",true);

        channel.basicPublish(exchangeName, "", null, message.getBytes());
        //System.out.println(" [x] Enviado '" + message + "'");

        channel.close();
        connection.close();
    }
}
