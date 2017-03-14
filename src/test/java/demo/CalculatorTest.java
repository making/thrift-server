package demo;

import demo.calculator.TCalculatorService;
import demo.calculator.TDivisionByZeroException;
import demo.calculator.TOperation;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CalculatorTest {
    @Autowired
    TProtocolFactory protocolFactory;
    TCalculatorService.Client client;
    TTransport transport;


    @Before
    public void setUp() throws Exception {
        transport = new TSocket("localhost", 8080);
        transport.open();
        TProtocol protocol = protocolFactory.getProtocol(transport);
        this.client = new TCalculatorService.Client(protocol);
    }

    @After
    public void tearDown() throws Exception {
        transport.close();
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