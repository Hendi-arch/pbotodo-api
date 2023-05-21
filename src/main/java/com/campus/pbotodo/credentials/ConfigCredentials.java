package com.campus.pbotodo.credentials;

import java.time.LocalDateTime;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "config_credentials")
public class ConfigCredentials {

    public static final String DEFAULT_NAME = "default";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String algorithm;

    @Lob
    @Column(name = "encoded_private_key")
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] encodedPrivateKey;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] encoded;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

}
