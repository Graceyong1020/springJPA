package org.pgm.jpademo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


@Data //getter, setter, toString, equals, hashCode를 자동 생성
@AllArgsConstructor //모든 필드 값을 파라미터로 받는 생성자를 생성
@NoArgsConstructor //기본 생성자를 생성
@Builder
public class PageRequestDTO { //pagerequest 목적은 parameter를 받아서 쿼리스트링을 만들어주는 것

    @Builder.Default
    private int size = 3;

    @Builder.Default
    private int page = 1;

    private String type; // 종류: T, W, C, TC, TW, TCW
    private String keyword;


    public String[] getTypes() {
        if (type == null || type.isEmpty()) { //type이 비어있거나 null이면 null을 반환
            return null;
        }
        return type.split(""); //else: type을 쪼개서 글자 한자씩("") 반환해서 배열로 만들어 반환
    }


    public Pageable getPageable(String... props) {
        return (Pageable) PageRequest.of(this.page - 1, this.size, Sort.by(props).descending());
    } //pageable을 만들어주는 메서드, 데이터를 가져와서 내림차순으로 정렬해주는 메서드, 한 페이지에 size 만큼 데이터를 보여주는 메서드

    private String link;

    public String getLink() {

        if (link == null) {
            StringBuilder builder = new StringBuilder();

            builder.append("page=" + this.page);
            builder.append("&size=" + this.size);

            if (type != null && type.length() > 0) {
                builder.append("&type=" + type);
            }
            if (keyword != null) {
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
                } catch (UnsupportedEncodingException e) {

                }
            }
            link = builder.toString();
        }
        return link;
}


}
