package com.abby.jiaqing;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class JiaQingApplication {
    static Logger logger= LoggerFactory.getLogger(JiaQingApplication.class);
    public static void main(String[] args){
        SpringApplication.run(JiaQingApplication.class);
    }
}
