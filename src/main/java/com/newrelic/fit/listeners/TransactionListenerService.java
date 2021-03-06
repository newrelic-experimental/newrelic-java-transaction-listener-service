package com.newrelic.fit.listeners;

import com.newrelic.agent.Agent;
import com.newrelic.agent.ExtendedTransactionListener;
import com.newrelic.agent.Transaction;
import com.newrelic.agent.TransactionData;
import com.newrelic.agent.config.AgentConfig;
import com.newrelic.agent.config.AgentConfigListener;
import com.newrelic.agent.config.ConfigService;
import com.newrelic.agent.service.AbstractService;
import com.newrelic.agent.service.ServiceFactory;
import com.newrelic.agent.stats.TransactionStats;
import com.newrelic.api.agent.NewRelic;

import java.util.HashMap;
import java.util.Map;

public class TransactionListenerService extends AbstractService implements ExtendedTransactionListener, AgentConfigListener {

    private final Map<String, Object> labelsMap = new HashMap<String, Object>();

    public TransactionListenerService() {
        super("TransactionListenerService");
        Agent.LOG.info("Loading: " + this);
    }

    protected TransactionListenerService(String name) {
        super(name);
        Agent.LOG.info("Loading: " + this);
    }

    @Override
    public void configChanged(String applicationName, AgentConfig agentConfig) {
        logger.debug("configChanged: enter: labelsMap: " + labelsMap);
        labelsMap.clear();
        Map<String, String> ymlLabels = agentConfig.getLabelsConfig().getLabels();

        StringBuffer labelBuffer = new StringBuffer();
        for (String key : ymlLabels.keySet()) {
            labelBuffer.append(key);
            labelBuffer.append(";");
        }

        String labels = agentConfig.getValue("transaction_listener_service_labels", labelBuffer.toString());
        // Ensure we ALWAYS look for deployment version
        if (!labels.toLowerCase().contains("deployment_version"))
            labels = labels + ";deployment_version";

        String[] labelArray = labels.split(";");
        for (String key : labelArray) {
            String value = System.getenv(key.toUpperCase());
            if (value == null || value.isEmpty())
                value = ymlLabels.get(key);
            if (value == null || value.isEmpty())
                continue;
            labelsMap.put(key, value);
        }
        for (String key : labelsMap.keySet()) {
            logger.debug("configChanged: key: " + key + " value: " + labelsMap.get(key));
        }
        logger.debug("configChanged: exit: labelsMap: " + labelsMap);
    }

    @Override
    public void dispatcherTransactionCancelled(Transaction transaction) {
        logger.debug("dispatcherTransactionCancelled: enter: labelsMap: " + labelsMap);
        logger.debug("dispatcherTransactionCancelled: exit: labelsMap: " + labelsMap);
    }

    @Override
    public void dispatcherTransactionFinished(TransactionData td, TransactionStats ts) {
        logger.debug("dispatcherTransactionFinished: enter: labelsMap: " + labelsMap);
//        // Use this for Java Agents prior to 5.12.1
        logger.finer("dispatcherTransactionFinished: td.getUserAttributes().putAll(labelsMap);");
        td.getUserAttributes().putAll(labelsMap);
//        // Use this for Java Agents 5.12.1 and above
//        logger.finer("dispatcherTransactionFinished: NewRelic.addCustomParameters(labelsMap);");
//        NewRelic.addCustomParameters(labelsMap);
//        NewRelic.getAgent().getTracedMethod().addCustomAttributes(labelsMap);
        logger.debug("dispatcherTransactionFinished: exit: labelsMap: " + labelsMap);
    }

    @Override
    public void dispatcherTransactionStarted(Transaction transaction) {
        logger.debug("dispatcherTransactionStarted: enter: labelsMap: " + labelsMap);
        logger.finer("dispatcherTransactionStarted: testing txn");
        if (transaction != null) {
            if (transaction.isStarted()) {
                logger.finer("dispatcherTransactionStarted: transaction.getUserAttributes().putAll(labelsMap)");
                transaction.getUserAttributes().putAll(labelsMap);
                //transaction.getRootTracer().getCustomAttributes().putAll(labelsMap);
            } else {
                logger.finer("dispatcherTransactionStarted: txn is not started");
            }
        } else {
            logger.finer("dispatcherTransactionStarted: null txn");
        }
        logger.debug("dispatcherTransactionStarted: exit: labelsMap: " + labelsMap);
    }

    @Override
    protected void doStart() {
        // Ensure everything is started before we try to start
        logger.debug("doStart: enter");
        final ExtendedTransactionListener svc = this;
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                ServiceFactory.getTransactionService().addTransactionListener(svc);
                logger.info("TransactionListenerService: started");
            }
        }, 60000);

        ConfigService configService = ServiceFactory.getConfigService();
        AgentConfig agentConfig = configService.getDefaultAgentConfig();

        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                ServiceFactory.getConfigService().addIAgentConfigListener((AgentConfigListener) svc);
                logger.info("AgentConfigListenerService: started");
            }
        }, 60000);

        this.configChanged("", agentConfig);
        logger.debug("doStart: exit");
    }

    @Override
    protected void doStop() {
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}