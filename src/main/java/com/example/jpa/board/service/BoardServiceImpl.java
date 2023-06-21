package com.example.jpa.board.service;

import com.example.jpa.board.entity.BoardType;
import com.example.jpa.board.model.BoardTypeInput;
import com.example.jpa.board.model.ServiceResult;
import com.example.jpa.board.repository.BoardTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    // 레파지토리
    private final BoardTypeRepository boardTypeRepository;

    @Override
    public ServiceResult addBoard(BoardTypeInput boardTypeInput) {   // 게시판 추가

        BoardType boardType = boardTypeRepository.findByBoardName(boardTypeInput.getName());
        if (boardType != null && boardTypeInput.getName().equals(boardType.getBoardName())) {  // 동일한 게시판 이름이 있는 경우
            return ServiceResult.fail("이미 동일한 게시판이 존재합니다.");
        }

        BoardType addboardType = BoardType.builder()  // 없는 경우 새로운 게시판 만들기
                .boardName(boardTypeInput.getName())
                .regDate(LocalDateTime.now())
                .build();

        boardTypeRepository.save(addboardType);   // 레파지토리에 저장

        return ServiceResult.success();
    }
}
