package demo;

import demo.calculator.TCalculatorService;
import demo.calculator.TDivisionByZeroException;
import demo.calculator.TOperation;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = App.class)
@WebAppConfiguration
@IntegrationTest({"server.port:0"})
public class CalculatorTest {
    @Autowired
    TProtocolFactory protocolFactory;
    @Value("${local.server.port}")
    int port;
    TCalculatorService.Client client;

    @Before
    public void setUp() throws Exception {
        TTransport transport = new THttpClient("http://localhost:" + port + "/calculator");
        TProtocol protocol = protocolFactory.getProtocol(transport);
        this.client = new TCalculatorService.Client(protocol);
    }

    @Test
    public void testAdd() throws Exception {
        assertThat(client.calculate(2, 3, TOperation.ADD), is(5));
    }

    @Test
    public void testSubtract() throws Exception {
        assertThat(client.calculate(2, 3, TOperation.SUBTRACT), is(-1));
    }

    @Test
    public void testMultiply() throws Exception {
        assertThat(client.calculate(2, 3, TOperation.MULTIPLY), is(6));
    }

    @Test
    public void testDivide() throws Exception {
        assertThat(client.calculate(10, 5, TOperation.DIVIDE), is(2));
    }

    @Test(expected = TDivisionByZeroException.class)
    public void testDivisionByZero() throws Exception {
        client.calculate(10, 0, TOperation.DIVIDE);
    }
}