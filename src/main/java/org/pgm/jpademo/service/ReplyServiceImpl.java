package org.pgm.jpademo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.pgm.jpademo.domain.Reply;
import org.pgm.jpademo.dto.PageRequestDTO;
import org.pgm.jpademo.dto.PageResponseDTO;
import org.pgm.jpademo.dto.ReplyDTO;
import org.pgm.jpademo.repository.ReplyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReplyServiceImpl implements ReplyService {
    private final ReplyRepository replyRepository;
    private final ModelMapper modelMapper;

    @Override
    public Long register(ReplyDTO replyDTO) {
        Reply reply = modelMapper.map(replyDTO, Reply.class); //ReplyDTO를 Reply entity로 변환
        Long rno = replyRepository.save(reply).getRno(); //ReplyRepository를 통해 저장한 reply entity의 rno를 반환
        return rno;
    }

    @Override
    public ReplyDTO read(Long rno) {
        Optional<Reply> replyOptional = replyRepository.findById(rno);
        Reply reply = replyOptional.orElseThrow();
        return modelMapper.map(reply, ReplyDTO.class);
    }

    @Override
    public void modify(ReplyDTO replyDTO) {  //1. ReplyDTO를 이용하여 수정할 댓글을 조회
        Optional<Reply> replyOptional = replyRepository.findById(replyDTO.getRno()); //2. Reply entity를 ReplyDTO로 변환
        Reply reply = replyOptional.orElseThrow();
        reply.changeText(replyDTO.getReplyText()); //3. changeText 메서드를 이용하여 댓글 내용만 수정
        replyRepository.save(reply); //4. 수정된 댓글을 저장
    }

    @Override
    public void remove(Long rno) {
        replyRepository.deleteById(rno);
    } //ReplyRepository를 통해 rno에 해당하는 댓글을 삭제

    @Override
    public PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() //PageRequestDTO를 이용하여 페이징 처리
                        <=0? 0: pageRequestDTO.getPage() -1,
                pageRequestDTO.getSize(),
                Sort.by("rno").ascending());

        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);

        List<ReplyDTO> dtoList =
                result.getContent().stream().map(reply -> modelMapper.map(reply, ReplyDTO.class))
                        .collect(Collectors.toList());

        return PageResponseDTO.<ReplyDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }
}
