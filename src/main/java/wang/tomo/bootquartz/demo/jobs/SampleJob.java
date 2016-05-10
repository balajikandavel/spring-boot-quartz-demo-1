package wang.tomo.bootquartz.demo.jobs;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import wang.tomo.bootquartz.demo.service.SampleService;

/**
 * Created by tomo on 16-5-6.
 */
public class SampleJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(SampleJob.class);

    @Autowired
    SampleService sampleService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        logger.info("start sample job");
        sampleService.ping();
    }
}
