package com.campus.pbotodo.credentials;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigCredentialsRepo extends JpaRepository<ConfigCredentials, Long> {

    ConfigCredentials findByName(String name);

}
