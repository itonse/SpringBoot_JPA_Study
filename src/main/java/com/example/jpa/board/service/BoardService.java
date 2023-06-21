package com.example.jpa.board.service;

import com.example.jpa.board.model.BoardTypeInput;
import com.example.jpa.board.model.ServiceResult;
import org.springframework.stereotype.Service;

public interface BoardService {

    ServiceResult addBoard(BoardTypeInput boardTypeInput);
}
