package com.domain;

import com.adapters.outgoing.facebook.FacebookMessageSender;
import com.adapters.outgoing.facebook.FacebookMessageSenderConfiguration;
import com.adapters.outgoing.pola.PolaConfiguration;
import com.adapters.outgoing.redis.RedisConfiguration;
import com.domain.ports.incoming.communicator.CommunicatorConfigurationProvider;
import com.domain.ports.incoming.communicator.FeatureConfiguration;
import com.domain.ports.incoming.communicator.OnNewIncomingMessageListener;
import com.domain.ports.outgoing.communicator.OnNewOutgoingMessageListener;
import com.domain.ports.outgoing.context.ContextManager;
import com.domain.ports.outgoing.productinformation.ProductInformationService;
import com.domain.process.engine.AbstractEngine;
import com.domain.process.engine.ConversationEngine;
import com.domain.process.engine.machine.ConversationFlow;
import com.domain.process.engine.machine.MachineFlow;
import com.domain.process.engine.machine.MachineState;
import com.domain.process.engine.machine.controller.ControllerExceptionHandler;
import com.domain.process.engine.machine.dispatcher.DispatcherHelper;
import com.domain.process.engine.machine.dispatcher.StateDispatcher;
import com.domain.process.engine.machine.dispatcher.home.InitDispatcher;
import com.domain.process.engine.machine.dispatcher.home.WaitForActionDispatcher;
import com.domain.process.engine.machine.dispatcher.report.WaitForDecisionDispatcher;
import com.domain.process.engine.machine.dispatcher.report.WaitForDecisionOrAction1Dispatcher;
import com.domain.process.engine.machine.dispatcher.report.WaitForImageOrSubmissionDispatcher;
import com.domain.process.engine.machine.dispatcher.report.WaitForText1Dispatcher;
import com.domain.process.engine.machine.dispatcher.suggestion.WaitForDecisionOrAction2Dispatcher;
import com.domain.process.engine.machine.dispatcher.suggestion.WaitForText2Dispatcher;
import com.domain.process.service.BarCodeService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.HashMap;

import static com.domain.process.engine.machine.MachineState.*;


@Import({PolaConfiguration.class, RedisConfiguration.class, FacebookMessageSenderConfiguration.class})
@Configuration
public class ApplicationConfig {

    @Bean
    public FeatureConfiguration featureConfiguration() {
        return new FeatureConfiguration("INIT", BotResponses.Setup.greetingText);
    }

    @Bean
    public BarCodeService barCodeService() {
        return new BarCodeService();
    }

    @Bean(name = "dispatchers")
    public HashMap<MachineState, StateDispatcher> dispatcherHashMap() {
        DispatcherHelper dispatcherHelper = new DispatcherHelper();
        StateDispatcher homeDispatcher = new WaitForActionDispatcher(dispatcherHelper);

        StateDispatcher initDispatcher = new InitDispatcher();
        StateDispatcher waitForActionDispatcher = new WaitForActionDispatcher(dispatcherHelper);
        StateDispatcher waitForDecisionOrAction1Dispatcher = new WaitForDecisionOrAction1Dispatcher(dispatcherHelper, homeDispatcher);
        StateDispatcher waitForDecisionOrAction2Dispatcher = new WaitForDecisionOrAction2Dispatcher(dispatcherHelper, homeDispatcher);
        StateDispatcher waitForImageOrSubmissionDispatcher = new WaitForImageOrSubmissionDispatcher(dispatcherHelper);
        StateDispatcher waitForText1Dispatcher = new WaitForText1Dispatcher(dispatcherHelper);
        StateDispatcher waitForText2Dispatcher = new WaitForText2Dispatcher(dispatcherHelper);
        StateDispatcher waitForDecisionDispatcher = new WaitForDecisionDispatcher(dispatcherHelper);

        HashMap<MachineState, StateDispatcher> dispatchers = new HashMap<>();
        dispatchers.put(INIT, initDispatcher);
        dispatchers.put(WAIT_FOR_ACTION, waitForActionDispatcher);
        dispatchers.put(WAIT_FOR_DECISION_OR_ACTION_1, waitForDecisionOrAction1Dispatcher);
        dispatchers.put(WAIT_FOR_DECISION_OR_ACTION_2, waitForDecisionOrAction2Dispatcher);
        dispatchers.put(WAIT_FOR_IMAGE_OR_SUBMISSION, waitForImageOrSubmissionDispatcher);
        dispatchers.put(WAIT_FOR_TEXT_1, waitForText1Dispatcher);
        dispatchers.put(WAIT_FOR_TEXT_2, waitForText2Dispatcher);
        dispatchers.put(WAIT_FOR_DECISION, waitForDecisionDispatcher);

        return dispatchers;
    }

    @Bean
    public MachineFlow machineFlow(OnNewOutgoingMessageListener onNewOutgoingMessageListener, ProductInformationService productInformationService, BarCodeService barCodeService) {
        return new ConversationFlow(onNewOutgoingMessageListener, productInformationService, barCodeService);
    }

    @Bean
    public OnNewOutgoingMessageListener onNewOutgoingMessageListener(FacebookMessageSender facebookMessageSender) {
        return facebookMessageSender;
    }

    @Bean
    OnNewIncomingMessageListener onNewIncomingMessageListener(AbstractEngine abstractEngine) {
        return abstractEngine;
    }

    @Bean
    AbstractEngine abstractEngine(
            ConversationEngine conversationEngine) {
        return conversationEngine;
    }

    @Bean
    CommunicatorConfigurationProvider communicatorConfigurationProvider(ConversationEngine conversationEngine) {
        return conversationEngine;
    }

    @Bean
    ConversationEngine conversationEngine(
            FeatureConfiguration featureConfiguration,
            ContextManager contextRepository,
            @Qualifier("dispatchers") HashMap<MachineState, StateDispatcher> dispatchers,
            MachineFlow machineFlow) {
        return new ConversationEngine(contextRepository, featureConfiguration, dispatchers, machineFlow);
    }

    @Bean
    public ControllerExceptionHandler controllerExceptionHandler(OnNewOutgoingMessageListener onNewOutgoingMessageListener,
                                                                 AbstractEngine abstractEngine,
                                                                 CommunicatorConfigurationProvider communicatorConfigurationProvider) {
        return new ControllerExceptionHandler(onNewOutgoingMessageListener, abstractEngine, communicatorConfigurationProvider);
    }

}

