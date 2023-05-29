package com.campus.pbotodo.feedback;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campus.pbotodo.exception.BadRequestException;
import com.campus.pbotodo.user.UserRepo;

@RestController
@RequestMapping(path = "/api/feedback")
public class UserFeedbackController {

    private final UserFeedbackRepo userFeedbackRepo;

    private final UserRepo userRepo;

    public UserFeedbackController(UserFeedbackRepo userFeedbackRepo, UserRepo userRepo) {
        this.userFeedbackRepo = userFeedbackRepo;
        this.userRepo = userRepo;
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> submitFeedback(@PathVariable String user,
            @Valid @RequestBody UserFeedbackEntity userFeedback) {
        if (userRepo.existsByUsername(user)) {
            userFeedbackRepo.save(userFeedback);
            return ResponseEntity.ok("Feedback submitted successfully.");
        } else {
            throw new BadRequestException("Invalid request.");
        }
    }

}
