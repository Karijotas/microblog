package lt.karijotas.microblogging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class MicrobloggingApplication{

    public static void main(String[] args) {
        SpringApplication.run(MicrobloggingApplication.class, args);
    }

}
