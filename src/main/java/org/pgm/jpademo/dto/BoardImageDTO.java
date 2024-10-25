package org.pgm.jpademo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardImageDTO { // 게시글 이미지 처리하기 위한 DTO

    private String uuid;
    private String fileName;
    private int ord;

}
