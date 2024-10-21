package org.pgm.jpademo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDateTime;
import java.util.Date;
@Getter
@MappedSuperclass // 부모 클래스로만 사용하기 위한 어노테이션
@EntityListeners(value = {AuditingEntityListener.class}) // JPA 내부에서 엔티티 객체가 생성/변경되는 것을 감지하는 리스너
abstract public class BaseEntity { // 모든 엔티티의 공통 매핑 정보를 정의하기 위한 클래스를 만들어 상속한다.
    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "regdate", updatable = false)
    private LocalDateTime regDate; //update 되면 안됨

    @UpdateTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "moddate")
    private LocalDateTime modDate;

}
