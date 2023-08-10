package pl.kalksztejn.mateusz.httpmethodstester.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kalksztejn.mateusz.httpmethodstester.service.Interface.MathService;

@RestController
@RequestMapping("/math")
@CrossOrigin(origins = "*", maxAge = 7200)
public class MathController {
    private final MathService mathService;

    @Autowired
    public MathController(MathService mathService) {
        this.mathService = mathService;
    }

    @GetMapping("sqrt")
    public ResponseEntity<String> calculateSqrt() {
        String result = mathService.calculateSqrt();
        return ResponseEntity.ok(result);
    }

    @GetMapping("fact")
    public ResponseEntity<String> calculateFactorial() {
        String result = mathService.calculateFactorial();
        return ResponseEntity.ok(result);
    }
    @GetMapping("blocking/{threadPool}")
    public ResponseEntity<String> blockingMethod(@PathVariable int threadPool) {
        String result = mathService.blockingMethod(threadPool);
        return ResponseEntity.ok(result);
    }


}
