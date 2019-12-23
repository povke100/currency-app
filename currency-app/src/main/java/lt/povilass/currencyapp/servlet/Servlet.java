package lt.povilass.currencyapp.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import lombok.extern.slf4j.Slf4j;
import lt.povilass.currencyapp.data.CurrencyRate;
import lt.povilass.currencyapp.db.query.DBQueries;
import lt.povilass.currencyapp.schedule.ScheduledRatesUpdate;

@Slf4j
@WebServlet(urlPatterns = { "/fxrates.do", "/calculator.do", "/history.do" }, loadOnStartup = 1)
public class Servlet extends HttpServlet {

	// private ScheduledRatesUpdate newestRates = new ScheduledRatesUpdate();

	private static Map<String, CurrencyRate> newestRates;

	private Scheduler scheduler;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init() {
		try {
			newestRates = DBQueries.getNewestfromDB();
			this.initializeScheduler();
			log.info("Servlet. initialized.");
		} catch (ClassNotFoundException | SQLException | SchedulerException e) {
			log.error("Servlet. initialization has failed.", e);
		}

	}

	public void initializeScheduler() throws SchedulerException {

		JobDetail dailyRateUpdate = JobBuilder.newJob(ScheduledRatesUpdate.class).withIdentity("dailyUpdate").build();

		dailyRateUpdate.getJobDataMap().put("rates", newestRates);

		//Rate update is made at 17:15, because at this time LB updates its database (around 17:00)
		Trigger trigger = TriggerBuilder.newTrigger()
				.withSchedule(CronScheduleBuilder.cronSchedule("0 10 2 * * ?")).build();

		scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.scheduleJob(dailyRateUpdate, trigger);
		scheduler.start();
		log.info("initializeScheduler. initializing scheduler!");
	}

	@Override
	public void destroy() {
		try {
			scheduler.clear();
			scheduler.shutdown(true);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String pathInfo = request.getRequestURI();
		log.debug("doGet. Path info. {}", pathInfo);

		if (pathInfo.contains("fxrates.do") || pathInfo.contains("calculator.do")) {

			String answer = "";

			if (pathInfo.contains("calculator.do")) {
				String curr = request.getParameter("currency");
				String amtS = request.getParameter("amount");
				
				log.debug("doGet. Amount: {}", amtS); 
				BigDecimal amt = !amtS.isEmpty() ? new BigDecimal(amtS, MathContext.DECIMAL32) : new BigDecimal("0.0");
				
				log.debug("doGet. Dividend: {}",amt);

				CurrencyRate rate = newestRates.get(curr);
				log.debug("doGet. Picked currency rate: {}", rate);
				BigDecimal quotient = rate != null ? amt.divide(rate.getAmount(),  MathContext.DECIMAL32)
						: new BigDecimal("0.0");
				
				answer = amt.stripTrailingZeros().toPlainString() + " " + curr + " / "
						+ rate.getAmount().stripTrailingZeros().toPlainString() + " = "
						+ quotient.stripTrailingZeros().toPlainString() + " EUR";

				log.debug("doGet: Answer string of calculator {}", answer);
			}

			request.setAttribute("answer", answer);

			Set<String> keySet = new TreeSet<>();
			List<CurrencyRate> rates = new LinkedList<CurrencyRate>();

			keySet.addAll(newestRates.keySet());
			rates.addAll(newestRates.values());

			Collections.sort(rates);

			request.setAttribute("rates", rates);
			request.setAttribute("codes", keySet);

			request.getRequestDispatcher("WEB-INF/views/todayrates.jsp").forward(request, response);
		} else if (pathInfo.contains("history.do")) {

			String code = request.getParameter("currcode");
			try {
				List<CurrencyRate> history = DBQueries.getRateHistoryForCurrency(code);

				request.setAttribute("hist", history);
				request.setAttribute("currcode", code);
				request.getRequestDispatcher("WEB-INF/views/rateshistory.jsp").forward(request, response);
			} catch (ClassNotFoundException | SQLException e) {
				log.error("doGet. Aqcuiring rate history has failed.", e);
			}

		}

	}
	
	public static void setNewestRates (Map<String, CurrencyRate> newestRates) {
		Servlet.newestRates = newestRates;
	}

}
