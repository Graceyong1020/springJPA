package org.pgm.jpademo.dto.upload;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadResultDTO { //업로드 결과를 담는 DTO

    private String uuid;
    private String filename;
    private boolean image;

    public String getLink() {
        if (image) {
            return "s_" + uuid + "_" + filename;
        } else {
            return uuid + "_" + filename;
        }

    }


}
