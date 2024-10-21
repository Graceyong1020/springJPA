package org.pgm.jpademo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDTO<E> { //제네릭 타입 E를 사용하여 다양한 타입의 DTO를 처리할 수 있도록 함

    private int page;
    private int size;
    private int total;
    private int start;
    private int end;
    private boolean prev;
    private boolean next;
    private List<E> dtoList;

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO,
                           List<E> dtoList, int total) {

        if(total <= 0) {
            return;
        }


        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();

        this.total = total;
        this.dtoList = dtoList;

        //paging 처리 계산
        this.end =(int)(Math.ceil((double) this.page/this.size))*this.size;
        this.start = this.end - (this.size - 1);
        int last = (int) (Math.ceil((total / (double)size)));
        this.end = end > last ? last : end; //end가 last보다 크면 last를 end로 지정
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }
}