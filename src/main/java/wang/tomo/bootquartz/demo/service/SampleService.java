package wang.tomo.bootquartz.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by tomo on 16-5-6.
 */
@Service
public class SampleService {
    private static final Logger logger = LoggerFactory.getLogger(SampleService.class);

    public void ping() {
        RestTemplate restTemplate = new RestTemplate();
        logger.info("ping");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String url = "http://127.0.0.1:8080/ping";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        logger.info("response status code: " + response.getStatusCode() + ", response body: " + response.getBody());
    }
}
