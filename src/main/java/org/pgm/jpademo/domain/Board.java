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
public class Board extends BaseEntity { //BaseEntity를 상속받아서 생성일, 수정일을 자동으로 처리
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long bno;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, length = 1000)
    private String content;
    @Column(nullable = false, length = 50)
    private String writer;

    @ColumnDefault("0")
    private int visitcount;

    public void updateVisitCount() {
        this.visitcount++;
    }

}
