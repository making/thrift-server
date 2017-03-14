package demo;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import demo.calculator.TCalculatorService;

@SpringBootApplication
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Bean
	TProtocolFactory protocolFactory() {
		return new TBinaryProtocol.Factory();
	}

	ExecutorService executor = Executors.newSingleThreadExecutor();

	@Bean(destroyMethod = "stop")
	TServer calculator(TCalculatorService.Iface calcService) throws TException {
		int port = Optional.ofNullable(System.getenv("PORT")).map(Integer::parseInt)
				.orElse(8080);
		TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(port);
		TServer server = new TThreadedSelectorServer(
				new TThreadedSelectorServer.Args(serverTransport)
						.processor(new TCalculatorService.Processor<>(calcService))
						.protocolFactory(protocolFactory()));
		executor.execute(server::serve);
		System.out.println("Started! on " + port);
		return server;
	}
}