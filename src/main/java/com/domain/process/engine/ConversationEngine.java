package com.domain.process.engine;

import com.domain.ports.incoming.communicator.CommunicatorConfigurationProvider;
import com.domain.ports.incoming.communicator.FeatureConfiguration;
import com.domain.ports.incoming.communicator.IncomingMessage;
import com.domain.ports.outgoing.context.Context;
import com.domain.ports.outgoing.context.ContextManager;
import com.domain.process.engine.machine.MachineFlow;
import com.domain.process.engine.machine.MachineState;
import com.domain.process.engine.machine.TransitionListener;
import com.domain.process.engine.machine.dispatcher.StateDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Map;

/**
 * Created by Piotr on 07.08.2017.
 */
public class ConversationEngine extends AbstractEngine implements CommunicatorConfigurationProvider {

    private static final Logger logger = LoggerFactory.getLogger(ConversationEngine.class);
    private ContextManager contextRepositoryManager;

    private final FeatureConfiguration featureConfiguration;
    private final Map<MachineState, StateDispatcher> dispatchers;
    private final MachineFlow machineFlow;

    /**
     * id of the user who is currently being served.
     */
    private String currentUserId;

    public ConversationEngine(
            ContextManager contextRepository,
            FeatureConfiguration featureConfiguration,
            @Qualifier("dispatchers") Map<MachineState, StateDispatcher> dispatchers,
            MachineFlow machineFlow
    ) {
        this.contextRepositoryManager = contextRepository;
        this.featureConfiguration = featureConfiguration;
        this.dispatchers = dispatchers;
        this.machineFlow = machineFlow;

        logger.debug("Created Conversation Engine");
    }

    @Override
    public String getCurrentUserId() {
        return currentUserId;
    }


    @Override
    public void resetState() {
        logger.debug("Clearing context for user " + currentUserId);

        contextRepositoryManager.deleteContext(currentUserId);
    }

    @Override
    public FeatureConfiguration getFeatureConfiguration() {
        return featureConfiguration;
    }

    @Override
    public void onNewMessage(IncomingMessage incomingMessage) {
        logger.debug(incomingMessage.toString());

        currentUserId = incomingMessage.getSenderId();

        if (incomingMessage.hasPayload() && incomingMessage.getPayload().equals(featureConfiguration.getInitializationPayload())) {
            resetState();
        }

        MachineState to = null;
        Context context = contextRepositoryManager.getOrCreateContext(currentUserId);

        logger.debug("Context: {}", context);

        context.setLastText(incomingMessage.getText());
        context.setLastAttachment(incomingMessage.getAttachment(0));

        StateDispatcher stateDispatcher = dispatchers.get(context.getState());
        if (stateDispatcher != null) {
            to = stateDispatcher.dispatch(context, incomingMessage);
            logger.debug("DISPATCH: {} => {}", context.getState(), to);
        }

        processIntermediateStates(context, to);

        logger.debug("Saving context: {}", context);
        contextRepositoryManager.saveContext(context);
    }

    private void processIntermediateStates(Context context, MachineState to) {
        while (!to.blocking) {
            MachineState currentState = context.getState();

            logger.debug("Process: {}  => []", currentState, to);

            TransitionListener action = machineFlow.getTransition(currentState, to);
            context.setState(to);

            if (action != null) {
                context.setState(to);
                to = action.onTransition(currentState, to, context);
                logger.debug("Performed {}: => {}", context.getState(), to);
            }
        }
        context.setState(to);
        logger.debug("Waiting at: {}, ", context.getState());
    }
}
