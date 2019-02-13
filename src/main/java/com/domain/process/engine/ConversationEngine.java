package com.domain.process.engine;

import com.domain.ports.outgoing.communicator.OnNewOutgoingMessageListener;
import com.domain.ports.outgoing.context.Context;
import com.domain.ports.outgoing.context.ContextManager;
import com.domain.process.engine.machine.Flow;
import com.domain.process.engine.machine.MachineState;
import com.domain.process.engine.machine.TransitionListener;
import com.domain.process.engine.machine.dispatcher.StateDispatcher;
import com.domain.process.engine.message.IncomingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashMap;

import static com.domain.process.engine.machine.MachineState.INIT;
import static com.domain.process.engine.machine.MachineState.WELCOME;

/**
 * Created by Piotr on 07.08.2017.
 */
public class ConversationEngine extends AbstractEngine {

    private static final Logger logger = LoggerFactory.getLogger(ConversationEngine.class);
    private ContextManager contextRepositoryManager;

    private HashMap<MachineState, StateDispatcher> dispatchers;
    private Flow machineFlow;

    /**
     * id of the user who is currently being served.
     */
    private String currentId;

    /**
     * this is how other objects can communicate with the engine; the function to the right receives input to the engine
     */
    private final OnNewOutgoingMessageListener listener;

    public ConversationEngine (
            ContextManager contextRepository,
            @Qualifier("dispatchers") HashMap<MachineState, StateDispatcher> dispatchers,
            OnNewOutgoingMessageListener outgoingMessageListener,
            Flow machineFlow
    ) {
        this.contextRepositoryManager = contextRepository;
        this.listener = outgoingMessageListener;
        this.dispatchers = dispatchers;
        this.machineFlow = machineFlow;

        logger.debug("Created Conversation Engine");
    }

    public String getCurrentId() {
        return currentId;
    }

    /**
     * Resets state of the current conversation, so it can be cleaned and started anew.
     */
    public boolean resetState() {
        logger.debug("Clearing context for user " + currentId);

        return contextRepositoryManager.deleteContext(currentId);
    }

    /**
     * Prompts the conversation engine to respond to stimuli.
     *
     * @param incomingMessage
     * @return
     */
    @Override
    public void doAction(IncomingMessage incomingMessage) {
        logger.debug(incomingMessage.toString());

        currentId = incomingMessage.getSenderId();

        if (incomingMessage.hasPayload() && incomingMessage.getPayload().equals("INIT")) {
            resetState();
        }

        MachineState to = null;
        Context context = contextRepositoryManager.getOrCreateContext(currentId);

        logger.debug("Context: {}" , context);

        context.setLastText(incomingMessage.getText());
        context.setLastAttachment(incomingMessage.getAttachment(0));

        if (context.getState() == INIT) {
            to = WELCOME;
            logger.debug("{} => {}", context.getState(), to);
        } else {
            StateDispatcher stateDispatcher = dispatchers.get(context.getState());
            if (stateDispatcher != null) {
                to = stateDispatcher.dispatch(context, incomingMessage);
                logger.debug("DISPATCH: {} => {}", context.getState(), to);
            }
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
