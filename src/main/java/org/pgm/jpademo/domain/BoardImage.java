package org.pgm.jpademo.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardImage implements Comparable<BoardImage> {
    @Id
    private String uuid;
    private String fileName;
    private int ord;

    @ManyToOne // BoardImage : Board = N : 1
    private Board board;


    @Override
    public int compareTo(BoardImage other) { // image 정렬을 위한 메서드
        return this.ord - other.ord;

    }

    public void changeBoard(Board board) {
        this.board = board;
    }
}
