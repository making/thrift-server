package demo;

import demo.calculator.TCalculatorService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    TProtocolFactory protocolFactory() {
        return new TBinaryProtocol.Factory();
    }

    @Bean /* Register Thrift Service */
    TServlet calculator(TCalculatorService.Iface calcService) {
        return new TServlet(new TCalculatorService.Processor<>(calcService), protocolFactory());
    }
}