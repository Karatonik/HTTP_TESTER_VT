package pl.kalksztejn.mateusz.httpmethodstester.service.Interface;

public interface MathService {
    String calculateSqrt();

    String calculateFactorial();

    String blockingMethod(int threadPool);
}
