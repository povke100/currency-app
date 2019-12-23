package lt.povilass.currencyapp.schedule;

import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import lombok.extern.slf4j.Slf4j;
import lt.povilass.currencyapp.data.CurrencyRate;
import lt.povilass.currencyapp.servlet.Servlet;
import lt.povilass.currencyapp.util.JobUtil;

@Slf4j
public class ScheduledRatesUpdate implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		try {
			
			log.debug("execute. executing scheduled job.");
		 	JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			
		 	@SuppressWarnings("unchecked")
			Map<String, CurrencyRate> rates = (Map<String, CurrencyRate>) dataMap.get("rates");
		 	
		 	rates = JobUtil.processRates(rates);
		 	Servlet.setNewestRates(rates);
		 	
		} catch (Exception e) {
			log.error("execute. Execution of scheduled job has failed.", e);
		}
	}

}
