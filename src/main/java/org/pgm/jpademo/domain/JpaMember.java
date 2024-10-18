package org.pgm.jpademo.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data // getter, setter, toString, equals, hashCode
@Table(name="tbl_member") // table name 바꾸기
public class JpaMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,length = 50)
    private String name;
    @Column(nullable = false,length = 50)
    private String password;
    @Column(nullable = false,length = 100)
    private String email;
    private String memo;
    @Column(name="addr")
    private String address;



}
