package wang.tomo.bootquartz.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by tomo on 16-5-10.
 */
@RestController
public class DemoController {
    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);
    @RequestMapping(value = "/ping")
    public String checkReportToRun() {
        return "pong";
    }
}
