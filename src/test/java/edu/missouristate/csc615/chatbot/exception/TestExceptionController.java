package edu.missouristate.csc615.chatbot.exception;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@ActiveProfiles("test")
class TestExceptionController {

    static class TestRequest {
        @NotBlank(message = "name must not be blank")
        public String name;
    }

    @PostMapping("/validation")
    public String validationEndpoint(@Valid @RequestBody TestRequest request) {
        return "OK";
    }

    @PostMapping("/runtime")
    public String runtimeEndpoint() {
        throw new RuntimeException("Something went wrong");
    }
}