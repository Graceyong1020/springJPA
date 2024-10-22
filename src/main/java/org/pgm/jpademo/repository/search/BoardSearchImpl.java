package org.pgm.jpademo.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.pgm.jpademo.domain.Board;
import org.pgm.jpademo.domain.QBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;


public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {
   public BoardSearchImpl() {
       super(Board.class);
   }

    @Override
    public Page<Board> searchAll (String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;

        JPQLQuery<Board> query = this.from(board);
        //BooleanBuilder booleanBuilder = new BooleanBuilder();

        if((types != null && types.length > 0) && (keyword != null)) {
            BooleanBuilder builder = new BooleanBuilder();
            for(String type : types) {
                switch (type) {
                    case "t":
                        builder.or(board.title.contains(keyword)); //title에 keyword가 포함된 데이터를 가져오는 쿼리
                        break;
                    case "c":
                        builder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        builder.or(board.writer.contains(keyword));
                        break;
                }
            }
            query.where(builder);


        }
        query.where(board.bno.gt(0l));
        this.getQuerydsl().applyPagination(pageable, query);
        List<Board> list = query.fetch();
        long total = query.fetchCount();
        return new PageImpl<>(list, pageable, total);

    }
}
