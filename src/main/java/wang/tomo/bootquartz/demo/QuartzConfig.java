package wang.tomo.bootquartz.demo;

import wang.tomo.bootquartz.demo.jobs.SampleJob;
import wang.tomo.bootquartz.demo.spring.AutowiringSpringBeanJobFactory;
import org.quartz.*;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
//import org.springframework.scheduling.support.CronTrigger;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;

/**
 * Created by tomo on 16-5-6.
 */
@Configuration
public class QuartzConfig {
    @Autowired
    DataSource dataSource;

    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory,
                                                     @Qualifier("sampleJobTrigger") Trigger sampleJobTrigger,
                                                     @Qualifier("cronJobTrigger") Trigger cronJobTrigger) throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        // this allows to update triggers in DB when updating settings in config file:
        factory.setOverwriteExistingJobs(true);
        factory.setDataSource(dataSource);
        factory.setJobFactory(jobFactory);
        
        factory.setTriggers(sampleJobTrigger, cronJobTrigger);

        return factory;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    @Bean
    public JobDetailFactoryBean sampleJobDetail() {
        return createJobDetail(SampleJob.class);
    }

    @Bean(name = "sampleJobTrigger")
    public SimpleTriggerFactoryBean sampleJobTrigger(@Qualifier("sampleJobDetail") JobDetail jobDetail) {
        return createTrigger(jobDetail, 2000);
    }

    @Bean(name = "cronJobTrigger")
    public CronTriggerFactoryBean cronJobTrigger(@Qualifier("sampleJobDetail") JobDetail jobDetail) {
        return createCronTrigger(jobDetail, "0 * * * * ?");
    }

    private static JobDetailFactoryBean createJobDetail(Class jobClass) {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(jobClass);
        // job has to be durable to be stored in DB:
        factoryBean.setDurability(true);
        return factoryBean;
    }

    private static CronTriggerFactoryBean createCronTrigger(JobDetail jobDetail, String cron) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setStartDelay(0L);
        factoryBean.setCronExpression(cron);
        return factoryBean;
    }

    private static SimpleTriggerFactoryBean createTrigger(JobDetail jobDetail, long pollFrequencyMs) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setStartDelay(0L);
        factoryBean.setRepeatInterval(pollFrequencyMs);
        factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        // in case of misfire, ignore all missed triggers and continue :
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
        return factoryBean;
    }
}
