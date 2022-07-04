package br.com.jbsneto.math;

import br.com.jbsneto.exceptions.UnsupportedMathOperationException;
import org.springframework.web.bind.annotation.PathVariable;

import static br.com.jbsneto.converters.NumberConverter.convertToDouble;
import static br.com.jbsneto.converters.NumberConverter.isNumeric;

public class SimpleMath {

    public Double sum(Double numberOne, Double numberTwo) {
        return numberOne + numberTwo;
    }

    public Double subtraction(Double numberOne, Double numberTwo) {
        return numberOne - numberTwo;
    }

    public Double multiplication(Double numberOne, Double numberTwo) {
        return numberOne * numberTwo;
    }

    public Double division(Double numberOne, Double numberTwo) {
        return numberOne / numberTwo;
    }

    public Double mean(Double numberOne, Double numberTwo) {
        return (numberOne + numberTwo) / 2;
    }

    public Double sqrt(Double number) {
        return Math.sqrt(number);
    }

}
