package com.domain;

import com.adapters.outgoing.facebook.FacebookMessageSender;
import com.adapters.outgoing.pola.PolaConfiguration;
import com.adapters.outgoing.redis.RedisConfiguration;
import com.domain.ports.incoming.communicator.FeatureConfiguration;
import com.domain.ports.outgoing.communicator.OnNewOutgoingMessageListener;
import com.domain.ports.outgoing.context.ContextManager;
import com.domain.ports.outgoing.productinformation.ProductInformationService;
import com.domain.process.engine.AbstractEngine;
import com.domain.process.engine.ConversationEngine;
import com.domain.process.engine.machine.Flow;
import com.domain.process.engine.machine.MachineState;
import com.domain.process.engine.machine.controller.ControllerExceptionHandler;
import com.domain.process.engine.machine.dispatcher.DispatcherHelper;
import com.domain.process.engine.machine.dispatcher.StateDispatcher;
import com.domain.process.engine.machine.dispatcher.home.WaitForActionDispatcher;
import com.domain.process.engine.machine.dispatcher.report.WaitForDecisionDispatcher;
import com.domain.process.engine.machine.dispatcher.report.WaitForDecisionOrAction1Dispatcher;
import com.domain.process.engine.machine.dispatcher.report.WaitForImageOrSubmissionDispatcher;
import com.domain.process.engine.machine.dispatcher.report.WaitForText1Dispatcher;
import com.domain.process.engine.machine.dispatcher.suggestion.WaitForDecisionOrAction2Dispatcher;
import com.domain.process.engine.machine.dispatcher.suggestion.WaitForText2Dispatcher;
import com.domain.process.service.BarCodeService;
import com.github.messenger4j.Messenger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.HashMap;

import static com.domain.process.engine.machine.MachineState.*;


@Import({PolaConfiguration.class, RedisConfiguration.class})
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

        StateDispatcher waitForActionDispatcher = new WaitForActionDispatcher(dispatcherHelper);
        StateDispatcher waitForDecisionOrAction1Dispatcher = new WaitForDecisionOrAction1Dispatcher(dispatcherHelper, homeDispatcher);
        StateDispatcher waitForDecisionOrAction2Dispatcher = new WaitForDecisionOrAction2Dispatcher(dispatcherHelper, homeDispatcher);
        StateDispatcher waitForImageOrSubmissionDispatcher = new WaitForImageOrSubmissionDispatcher(dispatcherHelper);
        StateDispatcher waitForText1Dispatcher = new WaitForText1Dispatcher(dispatcherHelper);
        StateDispatcher waitForText2Dispatcher = new WaitForText2Dispatcher(dispatcherHelper);
        StateDispatcher waitForDecisionDispatcher = new WaitForDecisionDispatcher(dispatcherHelper);

        HashMap<MachineState, StateDispatcher> dispatchers = new HashMap<>();
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
    public Flow machineFlow(OnNewOutgoingMessageListener listener, ProductInformationService productInformationService, BarCodeService barCodeService) {
        return new Flow(listener, productInformationService, barCodeService);
    }

    @Bean
    public OnNewOutgoingMessageListener onNewOutgoingMessageListener(Messenger messenger) {
        return new FacebookMessageSender(messenger);
    }

    @Bean
    AbstractEngine abstractEngine(
            FeatureConfiguration featureConfiguration,
            ContextManager contextRepository,
            @Qualifier("dispatchers") HashMap<MachineState, StateDispatcher> dispatchers,
            Flow machineFlow) {
        return new ConversationEngine(contextRepository, featureConfiguration, dispatchers, machineFlow);
    }

    @Bean
    public ControllerExceptionHandler controllerExceptionHandler(OnNewOutgoingMessageListener onNewOutgoingMessageListener, AbstractEngine abstractEngine) {
        return new ControllerExceptionHandler(onNewOutgoingMessageListener, abstractEngine);
    }

}

