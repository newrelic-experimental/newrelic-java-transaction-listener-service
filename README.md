[![Experimental Project header](https://github.com/newrelic/opensource-website/raw/master/src/images/categories/Experimental.png)](https://opensource.newrelic.com/oss-category/#experimental)

# Transaction Listener Service 

This lightweight Java Agent extension listens for Java Agent `configChanged` and `transactionFinished` events.

It adds the labels from the Agent's configuration yml file to each completed Agent transaction subject to the [Rules](#Rules) below. The labels are read on startup and on `configChanged` events.

The added labels appear in every Transaction Trace as user attributes.

### Rules
1. envvars *always* ovverride the configuration file
2. envvars are *always* uppercase
3. Always look for `deployment_version` label
4. If `transaction_listener_service_labels` is present in the `common` section of `newrelic.yml` as a semi-colon delimited list of labels then use it to filter which `labels` to use 
5. If `transaction_listener_service_labels` is *not* present in the `common` section of `newrelic.yml` then report all `labels`

## Installation

1. Build the project: `gradle clean build`
2. Place the resulting `build/libs/TransactionListenerService.jar` file in the [Java Agent's extensions directory](https://docs.newrelic.com/docs/agents/java-agent/instrumentation/extension-additional-instrumentation-modules)
3. Restart the Java Agent

## Support

New Relic hosts and moderates an online forum where customers can interact with New Relic employees as well as other customers to get help and share best practices. Like all official New Relic open source projects, there's a related Community topic in the New Relic Explorers Hub. You can find this project's topic/threads here:

>Add the url for the support thread here

## Contributing
We encourage your contributions to improve [Transaction Listener Service]! Keep in mind when you submit your pull request, you'll need to sign the CLA via the click-through using CLA-Assistant. You only have to sign the CLA one time per project.
If you have any questions, or to execute our corporate CLA, required if your contribution is on behalf of a company,  please drop us an email at opensource@newrelic.com.

## License
[Transaction Listener Service] is licensed under the [Apache 2.0](http://apache.org/licenses/LICENSE-2.0.txt) License.
