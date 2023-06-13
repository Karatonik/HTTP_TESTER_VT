package pl.kalksztejn.mateusz.httpmethodstester.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }
    @GetMapping("/1")
    public StringBuilder one() {
        StringBuilder test = new StringBuilder();
        for (long i = 0; i <9999999; i++){
            test.append('1');
        }
        return test;
    }
}
