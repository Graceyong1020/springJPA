package org.pgm.jpademo.domain;


import jakarta.persistence.*;
import lombok.*;
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

    public void change(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @ColumnDefault("0")
    private int visitcount;

    public void updateVisitCount() {
        this.visitcount++;
    }


    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            orphanRemoval = true) //cascade는 연관관계에 있는 엔티티를 같이 처리할 때 사용

    private Set<BoardImage> imageSet = new HashSet<>();

    //board 이미지 추가
    public void addImage(String uuid, String fileName) { //uuid와 파일명을 받아서 이미지를 추가
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
