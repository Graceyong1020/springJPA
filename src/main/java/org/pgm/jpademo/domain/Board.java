package org.pgm.jpademo.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageSet")
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

    public void change(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @ColumnDefault("0")
    private int visitcount;

    public void updateVisitCount() {
        this.visitcount++;
    }


    @OneToMany(mappedBy = "board", // board와 image는 1:N 관계
            fetch = FetchType.LAZY,  //지연로딩
            cascade = {CascadeType.ALL}, //cascade는 연관관계에 있는 엔티티를 같이 처리할 때 사용
            orphanRemoval = true) //cascade는 연관관계에 있는 엔티티를 같이 처리할 때 사용

    @Builder.Default
    @BatchSize(size = 20)
    private Set<BoardImage> imageSet = new HashSet<>(); // imageset을 사용해서 이미지를 관리

    //board 이미지 추가
    public void addImage(String uuid, String fileName) { //uuid와 filename을 받아서 이미지를 추가
        BoardImage image = BoardImage.builder() //BoardImage 객체 생성
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imageSet.size()) //이미지 순서
                .build();
        imageSet.add(image); //이미지 추가할때 마다 imageSet에 추가
    }

    //board 이미지 삭제
    public void clearImages() { //이미지 삭제
        imageSet.forEach(boardImage -> boardImage.changeBoard(null));
        this.imageSet.clear();
    }


}
