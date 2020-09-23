package message_processor_notifications;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.mule.runtime.api.component.location.LocationPart;
import org.mule.runtime.api.event.EventContext;
import org.mule.runtime.api.notification.MessageProcessorNotification;
import org.mule.runtime.api.notification.MessageProcessorNotificationListener;

public class MuleComponentProcessListener
		implements MessageProcessorNotificationListener<MessageProcessorNotification> {

	private Logger log = Logger.getLogger(getClass().getName());
	private long startTime = 0;
	private HashMap<String, Long> componentStartTimeHolder = new HashMap<String, Long>();

	@SuppressWarnings("deprecation")
	@Override
	public void onNotification(MessageProcessorNotification notification) {
		EventContext process = notification.getEventContext();
		String className = process.getClass().getSimpleName();
		// boolean isOutbound = process instanceof DefaultHttpRequester;
		// if (className.equalsIgnoreCase("DefaultHttpRequester") && isOutbound) {
		// try {
		// DefaultHttpRequester def = (DefaultHttpRequester) process;
		if (notification.getAction().getActionId() == MessageProcessorNotification.MESSAGE_PROCESSOR_PRE_INVOKE) {
			startTime = System.currentTimeMillis();
			componentStartTimeHolder.put(process.toString(), startTime);
		}
		if (notification.getAction().getActionId() == MessageProcessorNotification.MESSAGE_PROCESSOR_POST_INVOKE) {
			long executionTime = System.currentTimeMillis()
					- (long) componentStartTimeHolder.put(process.toString(), startTime);
			/*
			 * log.warn("HTTP Request Call To : " + def.getHost() + " " + def.getPath() +
			 * " took " + executionTime + "ms response time.");
			 */
			log.warn(computeDetails(notification.getComponent().getLocation().getParts()) + "\ncomponent took: "
					+ executionTime + "ms response time.");
		}
		// } catch (Exception e) {
	}

	/*
	 * } else if
	 * (className.equalsIgnoreCase("SubflowInterceptingChainLifecycleWrapper")) {
	 * SubflowInterceptingChainLifecycleWrapper subFlow =
	 * (SubflowInterceptingChainLifecycleWrapper) process; if
	 * (notification.getAction().getActionId() ==
	 * MessageProcessorNotification.MESSAGE_PROCESSOR_PRE_INVOKE) { startTime =
	 * System.currentTimeMillis();
	 * componentStartTimeHolder.put(subFlow.getSubFlowName(), startTime); } if
	 * (notification.getAction().getActionId() ==
	 * MessageProcessorNotification.MESSAGE_PROCESSOR_POST_INVOKE) { long
	 * executionTime = System.currentTimeMillis() - (long)
	 * componentStartTimeHolder.get(subFlow.getSubFlowName());
	 * log.warn("Sub-flow Processing Time: " + subFlow.getSubFlowName() + " took " +
	 * executionTime + "ms response time."); } } else if
	 * (className.equalsIgnoreCase("WeaveMessageProcessor")) { if
	 * (notification.getAction().getActionId() ==
	 * MessageProcessorNotification.MESSAGE_PROCESSOR_PRE_INVOKE) { startTime =
	 * System.currentTimeMillis(); componentStartTimeHolder.put(process.toString(),
	 * startTime); } if (notification.getAction().getActionId() ==
	 * MessageProcessorNotification.MESSAGE_PROCESSOR_POST_INVOKE) { long
	 * executionTime = System.currentTimeMillis() - (long)
	 * componentStartTimeHolder.get(process.toString());
	 * log.warn("Dataweave Processing Time: " + process.toString() + " took " +
	 * executionTime + "ms response time."); } } else if
	 * (className.equalsIgnoreCase("ScriptTransformer")) { if
	 * (notification.getAction().getActionId() ==
	 * MessageProcessorNotification.MESSAGE_PROCESSOR_PRE_INVOKE) { startTime =
	 * System.currentTimeMillis(); componentStartTimeHolder.put(process.toString(),
	 * startTime); } if (notification.getAction().getActionId() ==
	 * MessageProcessorNotification.MESSAGE_PROCESSOR_POST_INVOKE) { long
	 * executionTime = System.currentTimeMillis() - (long)
	 * componentStartTimeHolder.get(process.toString());
	 * log.warn("ScriptTransformer Processing Time: " + process.toString() +
	 * " took " + executionTime + "ms response time.");
	 * log.warn(computeDetails(notification.getComponent().getLocation().getParts())
	 * + "\ncomponent took: " + executionTime + "ms response time."); } }
	 */
	// }

	private StringBuilder computeDetails(List<LocationPart> parts) {
		StringBuilder computeTime = new StringBuilder("");

		computeTime.append("\nflow = " + parts.get(0).getPartPath() + "\nkind " + parts.get(1).getPartPath()
				+ "\nmodule in flow = " + parts.get(2).getPartPath() + "\nmodule name = "
				+ parts.get(2).getPartIdentifier().get().getIdentifier() + "\nline in file = "
				+ parts.get(2).getLineInFile());
		return computeTime;
	}
}
