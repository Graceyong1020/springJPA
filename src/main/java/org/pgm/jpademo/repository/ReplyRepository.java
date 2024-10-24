package org.pgm.jpademo.repository;

import org.pgm.jpademo.domain.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    @Query("select r from Reply r where r.board.bno = :bno")
        //paging해서 게시물 번호에 맞는 댓글 목록을 가져오는 메서드
    Page<Reply> listOfBoard(Long bno, Pageable pageable);

    void deleteByBoard_Bno(Long bno); //Board 객체 안에있는 Bno라는 필드
        //게시물 번호에 맞는 댓글을 삭제하는 메서드



}
