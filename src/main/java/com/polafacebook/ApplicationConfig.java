package com.polafacebook;

import com.adapters.dto.ContextEntity;
import com.adapters.dto.converters.ContextToRedisContextConverter;
import com.adapters.dto.converters.RedisContextToContextConverter;
import com.adapters.dto.converters.UrlAttachmentEntityToUrlAttachmentConverter;
import com.adapters.dto.converters.UrlAttachmentToUrlAttachmentEntityConverter;
import com.adapters.incoming.facebook.ControllerExceptionHandler;
import com.adapters.incoming.facebook.FacebookEventHandler;
import com.adapters.incoming.facebook.FacebookEventHelper;
import com.adapters.outgoing.facebook.FacebookMessageSender;
import com.github.messenger4j.MessengerPlatform;
import com.github.messenger4j.receive.MessengerReceiveClient;
import com.github.messenger4j.send.MessengerSendClient;
import com.github.messenger4j.setup.MessengerSetupClient;
import com.polafacebook.model.Context;
import com.polafacebook.model.repositories.ContextManager;
import com.polafacebook.model.repositories.RedisContextManager;
import com.polafacebook.model.repositories.RedisRepository;
import com.polafacebook.polapi.Pola;
import com.polafacebook.ports.outgoing.OnNewOutgoingMessageListener;
import com.polafacebook.process.engine.ConversationEngine;
import com.polafacebook.process.engine.machine.Flow;
import com.polafacebook.process.engine.machine.MachineState;
import com.polafacebook.process.engine.machine.dispatcher.*;
import com.polafacebook.process.service.BarCodeService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import static com.polafacebook.process.engine.machine.MachineState.*;

@Configuration
@EnableRedisRepositories
public class ApplicationConfig {


    @Bean
    JedisPoolConfig poolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(1);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        return poolConfig;
    }

    @Bean
    public JedisConnectionFactory jedisConnFactory(JedisPoolConfig poolConfig) {
        try {
            URI redistogoUri = new URI(System.getenv("REDIS_URL"));
            JedisConnectionFactory jedisConnFactory = new JedisConnectionFactory(poolConfig);

            jedisConnFactory.setUsePool(true);
            jedisConnFactory.setHostName(redistogoUri.getHost());
            jedisConnFactory.setPort(redistogoUri.getPort());
            jedisConnFactory.setTimeout(Protocol.DEFAULT_TIMEOUT);
            jedisConnFactory.setPassword(redistogoUri.getUserInfo().split(":", 2)[1]);

            return jedisConnFactory;

        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    @Bean
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    @Bean
    public Jackson2JsonRedisSerializer<ContextEntity> jacksonJsonRedisJsonSerializer() {
        Jackson2JsonRedisSerializer<ContextEntity> jacksonJsonRedisJsonSerializer = new Jackson2JsonRedisSerializer<>(ContextEntity.class);
        return jacksonJsonRedisJsonSerializer;
    }

    @Bean
    public RedisTemplate<String, Context> redisTemplate(JedisConnectionFactory jedisConnFactory) {
        RedisTemplate<String, Context> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnFactory);
        redisTemplate.setKeySerializer(stringRedisSerializer());
        redisTemplate.setValueSerializer(jacksonJsonRedisJsonSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisContextManager redisContextManager(RedisRepository redisRepository, RedisContextToContextConverter incomingConverter, ContextToRedisContextConverter outgoingConverter) {
        return new RedisContextManager(redisRepository, incomingConverter, outgoingConverter);
    }

    @Bean
    public RedisContextToContextConverter redisContextToContextConverter(UrlAttachmentEntityToUrlAttachmentConverter attachmentConverter) {
        return new RedisContextToContextConverter(attachmentConverter);
    }

    @Bean
    public ContextToRedisContextConverter redisContextConverter(UrlAttachmentToUrlAttachmentEntityConverter attachmentConverter) {
        return new ContextToRedisContextConverter(attachmentConverter);
    }

    @Bean
    public UrlAttachmentEntityToUrlAttachmentConverter urlAttachmentEntityToUrlAttachmentConverter() {
        return new UrlAttachmentEntityToUrlAttachmentConverter();
    }

    @Bean
    public UrlAttachmentToUrlAttachmentEntityConverter urlAttachmentToUrlAttachmentEntityConverter() {
        return new UrlAttachmentToUrlAttachmentEntityConverter();
    }

    @Bean
    public BarCodeService barCodeService() {
        return new BarCodeService();
    }

    @Bean(name = "dispatchers")
    public HashMap<MachineState, StateDispatcher> dispatcherHashMap() {
        DispatcherHelper dispatcherHelper = new DispatcherHelper();
        StateDispatcher homeDispatcher = new WaitForActionDispatcher(dispatcherHelper);

        StateDispatcher waitForActionDispatcher = new WaitForActionDispatcher(dispatcherHelper);
        StateDispatcher waitForDecisionOrAction1Dispatcher = new WaitForDecisionOrAction1Dispatcher(dispatcherHelper, homeDispatcher);
        StateDispatcher waitForDecisionOrAction2Dispatcher = new WaitForDecisionOrAction2Dispatcher(dispatcherHelper, homeDispatcher);
        StateDispatcher waitForImageOrSubmissionDispatcher = new WaitForImageOrSubmissionDispatcher(dispatcherHelper);
        StateDispatcher waitForText1Dispatcher = new WaitForText1Dispatcher(dispatcherHelper);
        StateDispatcher waitForText2Dispatcher = new WaitForText2Dispatcher(dispatcherHelper);

        HashMap<MachineState, StateDispatcher> dispatchers = new HashMap<>();
        dispatchers.put(WAIT_FOR_ACTION, waitForActionDispatcher);
        dispatchers.put(WAIT_FOR_DECISION_OR_ACTION_1, waitForDecisionOrAction1Dispatcher);
        dispatchers.put(WAIT_FOR_DECISION_OR_ACTION_2, waitForDecisionOrAction2Dispatcher);
        dispatchers.put(WAIT_FOR_IMAGE_OR_SUBMISSION, waitForImageOrSubmissionDispatcher);
        dispatchers.put(WAIT_FOR_TEXT_1, waitForText1Dispatcher);
        dispatchers.put(WAIT_FOR_TEXT_2, waitForText2Dispatcher);

        return dispatchers;
    }

    @Bean
    public Flow machineFlow(OnNewOutgoingMessageListener listener, Pola polaService, BarCodeService barCodeService) {
        return new Flow(listener, polaService, barCodeService);
    }

/*
    @Bean
    public MessengerSendClient messengerSendClient(@Value("${messenger4j.pageAccessToken}") final String pageAccessToken) {
        return MessengerPlatform.newSendClientBuilder(pageAccessToken).build();
    }
*/

    @Bean
    public MessengerSendClient messengerSendClient() {
        return MessengerPlatform.newSendClientBuilder(System.getenv("messenger4j.pageAccessToken")).build();
    }


    @Bean
    public OnNewOutgoingMessageListener onNewOutgoingMessageListener(MessengerSendClient messengerSendClient) {
        return new FacebookMessageSender(messengerSendClient);
    }

    @Bean
    public Pola pola() {
        return new Pola();
    }

    @Bean
    ConversationEngine conversationEngine(
            ContextManager contextRepository,
            @Qualifier("dispatchers") HashMap<MachineState, StateDispatcher> dispatchers,
            OnNewOutgoingMessageListener outgoingMessageListener,
            Flow machineFlow) {
        return new ConversationEngine(contextRepository, dispatchers, outgoingMessageListener, machineFlow);
    }


    /*
        @Bean
        public MessengerReceiveClient receiveClient(@Value("${messenger4j.appSecret}") String appSecret, @Value("${messenger4j.verifyToken}") String verifyToken, FacebookEventHandler handler) {
            return MessengerPlatform.newReceiveClientBuilder(appSecret, verifyToken)
                    .onTextMessageEvent(handler::onTextMessageEvent)
                    .onAttachmentMessageEvent(handler::onAttachmentMessageEvent)
                    .onQuickReplyMessageEvent(handler::onQuickReplyMessageEvent)
                    .onPostbackEvent(handler::onPostbackEvent)
                    .build();
        }
        */
    @Bean
    public MessengerReceiveClient receiveClient(FacebookEventHandler handler) {
        return MessengerPlatform.newReceiveClientBuilder(System.getenv("messenger4j.appSecret"), System.getenv("messenger4j.verifyToken"))
                .onTextMessageEvent(handler::onTextMessageEvent)
                .onAttachmentMessageEvent(handler::onAttachmentMessageEvent)
                .onQuickReplyMessageEvent(handler::onQuickReplyMessageEvent)
                .onPostbackEvent(handler::onPostbackEvent)
                .build();
    }

    @Bean
    public FacebookEventHandler facebookEventHandler(ConversationEngine conversationEngine, MessengerSendClient sendClient) {
        return new FacebookEventHandler(conversationEngine, sendClient, new FacebookEventHelper());
    }

    @Bean
    ControllerExceptionHandler controllerExceptionHandler(MessengerSendClient sendClient, ConversationEngine conversationEngine) {
        return new ControllerExceptionHandler(sendClient, conversationEngine);
    }

    /*
    @Bean
    public MessengerSetupClient messengerSetupClient(@Value("${messenger4j.pageAccessToken}") final String pageAccessToken) {
        return MessengerPlatform.newSetupClientBuilder(pageAccessToken).build();
    }
    */
    @Bean
    public MessengerSetupClient messengerSetupClient() {
        return MessengerPlatform.newSetupClientBuilder(System.getenv("messenger4j.pageAccessToken")).build();
    }

/*    @Bean
    public ServletContextInitializer sentryServletContextInitializer() {
        return new io.sentry.spring.SentryServletContextInitializer();
    }
*/
}

