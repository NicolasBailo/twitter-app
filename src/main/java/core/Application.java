package core;


import core.tweetsaver.TweetSaver;
import org.springframework.amqp.core.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import core.service.Receiver;
@SpringBootApplication
public class Application {




    public static final String TWITTER_FANOUT_EXCHANGE = "tweets";

    static final String QUEUE_NAME_PROCESSOR_1= "QUEUE_PROCESSOR_1";
    static final String QUEUE_NAME_TWEET_SAVER = "QUEUE_TWEET_SAVER";



    @Bean
    Queue queueProcessor1() {
        return new Queue(QUEUE_NAME_PROCESSOR_1, true);
    }

    @Bean
    FanoutExchange twitterFanoutExchange() {
        return new FanoutExchange(TWITTER_FANOUT_EXCHANGE);
    }
    @Bean
    Binding bindingProcessor1() {
        return BindingBuilder.bind(queueProcessor1()).to(
                twitterFanoutExchange());
    }

    @Bean
    SimpleMessageListenerContainer containerProcessor1(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapterProcessor1) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME_PROCESSOR_1);
        container.setMessageListener(listenerAdapterProcessor1);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapterProcessor1(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }


    @Bean
    Queue queueTweetSaver() {
        return new Queue(QUEUE_NAME_TWEET_SAVER, true);
    }

    @Bean
    FanoutExchange twitterFanoutExchange2() {
        return new FanoutExchange(TWITTER_FANOUT_EXCHANGE);
    }
    @Bean
    Binding binding2() {
        return BindingBuilder.bind(queueTweetSaver()).to(
                twitterFanoutExchange2());
    }

    @Bean
    SimpleMessageListenerContainer containerTweetSaver(ConnectionFactory connectionFactory2,
                                             MessageListenerAdapter listenerAdapterTweetSaver) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory2);
        container.setQueueNames(QUEUE_NAME_TWEET_SAVER);
        container.setMessageListener(listenerAdapterTweetSaver);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapterTweetSaver(TweetSaver tweetSaver) {
        return new MessageListenerAdapter(tweetSaver, "receiveMessage");
    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

//            System.out.println("Let's inspect the beans provided by Spring Boot:");
//            String[] beanNames = ctx.getBeanDefinitionNames();
//            Arrays.sort(beanNames);
//            for (String beanName : beanNames) {
//                System.out.println(beanName);
//            }

        };
    }

}