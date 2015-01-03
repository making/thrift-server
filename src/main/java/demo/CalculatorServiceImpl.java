package demo;

import demo.calculator.TCalculatorService;
import demo.calculator.TDivisionByZeroException;
import demo.calculator.TOperation;
import org.apache.thrift.TException;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.function.IntBinaryOperator;

@Component
public class CalculatorServiceImpl implements TCalculatorService.Iface {
    private static final EnumMap<TOperation, IntBinaryOperator> opMap = new EnumMap<TOperation, IntBinaryOperator>(TOperation.class) {{
        put(TOperation.ADD, (x, y) -> x + y);
        put(TOperation.SUBTRACT, (x, y) -> x - y);
        put(TOperation.MULTIPLY, (x, y) -> x * y);
        put(TOperation.DIVIDE, (x, y) -> x / y);
    }};

    @Override
    public int calculate(int num1, int num2, TOperation op) throws TException {
        if (opMap.containsKey(op)) {
            try {
                return opMap.get(op).applyAsInt(num1, num2);
            } catch (ArithmeticException e) {
                throw new TDivisionByZeroException();
            }
        } else {
            throw new TException("Unknown operation " + op);
        }
    }
}
