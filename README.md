<a href="https://opensource.newrelic.com/oss-category/#new-relic-experimental"><picture><source media="(prefers-color-scheme: dark)" srcset="https://github.com/newrelic/opensource-website/raw/main/src/images/categories/dark/Experimental.png"><source media="(prefers-color-scheme: light)" srcset="https://github.com/newrelic/opensource-website/raw/main/src/images/categories/Experimental.png"><img alt="New Relic Open Source experimental project banner." src="https://github.com/newrelic/opensource-website/raw/main/src/images/categories/Experimental.png"></picture></a>


# New Relic Java Agent - Transaction Listener Service 

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

To install:

1. Download the latest release jar files.
2. In the New Relic Java directory (the one containing newrelic.jar), create a directory named extensions if it does not already exist.
3. Copy the downloaded jars into the extensions directory.
4. Restart the application.

## Support

New Relic has open-sourced this project. This project is provided AS-IS WITHOUT WARRANTY OR SUPPORT, although you can report issues and contribute to the project here on GitHub.

_Please do not report issues with this software to New Relic Global Technical Support._

## Contributing
We encourage your contributions to improve [Transaction Listener Service]! Keep in mind when you submit your pull request, you'll need to sign the CLA via the click-through using CLA-Assistant. You only have to sign the CLA one time per project.
If you have any questions, or to execute our corporate CLA, required if your contribution is on behalf of a company,  please drop us an email at opensource@newrelic.com.

## License
[Transaction Listener Service] is licensed under the [Apache 2.0](http://apache.org/licenses/LICENSE-2.0.txt) License.
