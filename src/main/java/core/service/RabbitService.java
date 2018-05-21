package core.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.stereotype.Service;


@Service
public class RabbitService {

    //private final String exchangeName = "processor1";
    private final String ENV_AMQPURL_NAME = "CLOUDAMQP_URL";

    private ConnectionFactory factory;
    public RabbitService(){
        factory = new ConnectionFactory();
        String amqpURL = System.getenv().get(ENV_AMQPURL_NAME) != null ? System.getenv().get(ENV_AMQPURL_NAME) : "amqp://localhost";
        try {
            factory.setUri(amqpURL);
        } catch (Exception e) {
            System.out.println(" [*] AQMP broker not found in " + amqpURL);
            System.exit(-1);
        }

        System.out.println(" [*] AQMP broker found in " + amqpURL);





    }

    public void publish(String message, String exchangeName) throws Exception{
        // Conexión al broker RabbitMQ broker (prueba en la URL de
        // la variable de entorno que se llame como diga ENV_AMQPURL_NAME
        // o sino en localhost)


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
