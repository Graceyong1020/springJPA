package org.pgm.jpademo.repository;

import org.pgm.jpademo.domain.Board;
import org.pgm.jpademo.repository.search.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {
    @EntityGraph(attributePaths = {"imageSet"})
    @Query("select b from Board b where b.bno = :bno")
    Optional<Board> findByIdWithImages(Long bno); //findByIdWithImage 메서드는 bno에 해당하는 Board 객체를 가져오는데, 이미지 정보도 함께 가져오는 메서드


   //@Query("select b from Board b where b.title like concat('%',:keyword, '%') order by b.bno desc")
       // Board b entity를 가져오는데 title에 keyword가 포함된 데이터를 가져오는 쿼리
    //Page<Board> searchAll(String keyword, Pageable pageable); //title에 keyword가 포함된 데이터를 가져오는 메서드
    /*Page<Board> findByTitle(String title, Pageable pageable);* */
    // finaByTitle은 Entity에 존재하기 때문에 자동으로 메서드가 생성. select * from board where title = ? 자동으로 생성

}
