package org.pgm.jpademo.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Board {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long bno;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, length = 1000)
    private String content;
    @Column(nullable = false, length = 50)
    private String writer;
    @CreationTimestamp // 자동으로 현재 시간이 들어감
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date postdate;
    @ColumnDefault("0")
    private int visitcount;

}
