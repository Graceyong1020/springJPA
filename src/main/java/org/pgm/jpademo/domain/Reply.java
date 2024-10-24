package org.pgm.jpademo.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board") //toString을 호출할 때 board를 제외하고 호출
@Entity
@Table(name = "Reply", indexes = {
        @Index(name = "idx_reply_board_bno", columnList = "board_bno")
})
public class Reply extends BaseEntity { //BaseEntity를 상속받아야 하는 이유는 생성일자와 수정일자를 사용하기 위해서

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    private String replyText;

    private String replyer;

    public void changeText(String text) {
        this.replyText = text;
    }
}
