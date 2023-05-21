package com.campus.pbotodo.config;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.campus.pbotodo.credentials.ConfigCredentials;
import com.campus.pbotodo.credentials.ConfigCredentialsRepo;

import io.jsonwebtoken.SignatureAlgorithm;

@Configuration
public class Config {

    private final ConfigCredentialsRepo configCredentialsRepo;

    public Config(ConfigCredentialsRepo configCredentialsRepo) {
        this.configCredentialsRepo = configCredentialsRepo;
    }

    @Lazy
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * @apiNote A cached thread pool based ExecutorService, which run task with
     *          cached
     *          threads, that is, it will create thread only needed, detail is as
     *          follows:
     * 
     *          Creates a thread pool that creates new threads as needed, but will
     *          reuse
     *          previously constructed threads when they are available. These pools
     *          will
     *          typically improve the performance of programs that execute many
     *          short-lived
     *          asynchronous tasks. Calls to execute will reuse previously
     *          constructed
     *          threads if available. If no existing thread is available, a new
     *          thread will
     *          be created and added to the pool. Threads that have not been used
     *          for sixty
     *          seconds are terminated and removed from the cache. Thus, a pool that
     *          remains
     *          idle for long enough will not consume any resources. Note that pools
     *          with
     *          similar properties but different details (for example, timeout
     *          parameters)
     *          may be created using ThreadPoolExecutor constructors.
     */
    @Lazy
    @Bean("cachedThreadPool")
    public ExecutorService cachedThreadPool() {
        return Executors.newCachedThreadPool();
    }

    @Lazy
    @Bean
    public KeyPair rsaKeyPair() throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Get the key entity from the database
        ConfigCredentials keyEntity = configCredentialsRepo.findByName(ConfigCredentials.DEFAULT_NAME);

        // Create a new key pair if the key entity doesn't exist
        if (keyEntity == null) {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(SignatureAlgorithm.RS256.getFamilyName());
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            keyEntity = new ConfigCredentials();
            keyEntity.setName(ConfigCredentials.DEFAULT_NAME);
            keyEntity.setAlgorithm(keyPairGenerator.getAlgorithm());
            keyEntity.setEncoded(keyPair.getPublic().getEncoded());
            keyEntity.setEncodedPrivateKey(keyPair.getPrivate().getEncoded());
            keyEntity.setCreatedAt(LocalDateTime.now());
            keyEntity.setUpdatedAt(LocalDateTime.now());
            keyEntity.setCreatedBy("System");
            keyEntity.setUpdatedBy("System");
            configCredentialsRepo.save(keyEntity);
        }

        // Get the public and private keys from the key entity
        String algorithm = keyEntity.getAlgorithm();
        byte[] encodedKey = keyEntity.getEncoded();
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        KeySpec keySpec = new X509EncodedKeySpec(encodedKey);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        PrivateKey privateKey = null; // Set to null by default

        // If private key is also stored in the database, retrieve and set it
        if (keyEntity.getEncodedPrivateKey() != null) {
            keySpec = new PKCS8EncodedKeySpec(keyEntity.getEncodedPrivateKey());
            privateKey = keyFactory.generatePrivate(keySpec);
        }

        // Create and return the KeyPair instance
        return new KeyPair(publicKey, privateKey);
    }

    @Lazy
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Lazy
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
