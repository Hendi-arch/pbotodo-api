package com.campus.pbotodo.feedback;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFeedbackRepo extends JpaRepository<UserFeedbackEntity, Long> {

}