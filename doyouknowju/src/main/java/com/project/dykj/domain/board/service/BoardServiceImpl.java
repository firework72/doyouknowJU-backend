package com.project.dykj.domain.board.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.dykj.domain.board.mapper.BoardMapper;
import com.project.dykj.domain.board.mapper.ReplyMapper;
import com.project.dykj.domain.board.model.vo.Board;
import com.project.dykj.domain.board.model.vo.Reply;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;
    private final ReplyMapper replyMapper;

    public BoardServiceImpl(BoardMapper boardMapper, ReplyMapper replyMapper) {
        this.boardMapper = boardMapper;
        this.replyMapper = replyMapper;
    }

    /**
     * 게시글을 생성한다.
     * - 필수값(게시판 타입/작성자/제목/내용) 검증
     * - 종목 게시판(STOCK)인 경우 stockId 필수
     * - INSERT 후 생성된 boardId를 반환(SELECTKEY/CURRVAL)
     */
    @Transactional
    @Override
    public long createPost(Board board) {
        validateCreate(board);
        boardMapper.insertPost(board);
        return board.getBoardId();
    }

    /**
     * 게시글 상세를 조회한다.
     * - incrementView=true면 조회수 1 증가
     * - 삭제된 글(soft delete)은 조회되지 않도록 XML에서 필터링
     */
    @Transactional
    @Override
    public Board getPost(long postId, boolean incrementView) {
        if (incrementView) {
            boardMapper.incrementViewCnt(postId);
        }
        Board board = boardMapper.selectPostDetail(postId);
        if (board == null) {
            throw new IllegalArgumentException("post not found");
        }
        return board;
    }

    /**
     * 게시글 목록을 조회한다(페이징).
     * - boardType/stockId로 필터 가능
     * - page/size를 안전 범위로 보정
     */
    @Transactional(readOnly = true)
    @Override
    public List<Board> listPosts(String boardType, String stockId, int page, int size) {
        int safePage = Math.max(1, page);
        int safeSize = Math.min(50, Math.max(1, size));
        int offset = (safePage - 1) * safeSize;
        return boardMapper.selectPostList(boardType, stockId, offset, safeSize);
    }

    /**
     * 게시글을 수정한다.
     * - 제목/내용 필수
     * - 수정 대상이 없으면 예외
     */
    @Transactional
    @Override
    public void updatePost(long postId, Board board) {
        if (board == null) {
            throw new IllegalArgumentException("body is required");
        }
        if (isBlank(board.getBoardTitle()) || isBlank(board.getBoardContent())) {
            throw new IllegalArgumentException("title/content are required");
        }
        board.setBoardId((int) postId);
        int updated = boardMapper.updatePost(board);
        if (updated == 0) {
            throw new IllegalArgumentException("post not found");
        }
    }

    /**
     * 게시글을 삭제한다(소프트 삭제).
     * - DELETE_DATE를 SYSDATE로 업데이트
     */
    @Transactional
    @Override
    public void deletePost(long postId) {
        int updated = boardMapper.softDeletePost(postId);
        if (updated == 0) {
            throw new IllegalArgumentException("post not found");
        }
    }

    /**
     * 댓글을 작성한다.
     * - 게시글 존재 여부 확인
     * - INSERT 후 생성된 replyId 반환(SELECTKEY/CURRVAL)
     */
    @Transactional
    @Override
    public long addReply(long postId, Reply reply) {
        if (reply == null || isBlank(reply.getUserId()) || isBlank(reply.getReplyContent())) {
            throw new IllegalArgumentException("userId/content are required");
        }
        if (boardMapper.selectPostDetail(postId) == null) {
            throw new IllegalArgumentException("post not found");
        }

        reply.setBoardId((int) postId);
        replyMapper.insertReply(reply);
        return reply.getReplyId();
    }

    /**
     * 댓글 목록을 조회한다.
     * - 삭제된 댓글(soft delete)은 조회되지 않도록 XML에서 필터링
     */
    @Transactional(readOnly = true)
    @Override
    public List<Reply> listReplies(long postId) {
        return replyMapper.selectReplies(postId);
    }

    /**
     * 댓글을 삭제한다(소프트 삭제).
     */
    @Transactional
    @Override
    public void deleteReply(long replyId) {
        int updated = replyMapper.softDeleteReply(replyId);
        if (updated == 0) {
            throw new IllegalArgumentException("comment not found");
        }
    }

	private void validateCreate(Board board) {
		if (board == null) {
			throw new IllegalArgumentException("body is required");
		}
		if (isBlank(board.getBoardType()) || isBlank(board.getUserId()) || isBlank(board.getBoardTitle()) || isBlank(board.getBoardContent())) {
			throw new IllegalArgumentException("boardType/userId/title/content are required");
		}
		if ("STOCK".equalsIgnoreCase(board.getBoardType()) && isBlank(board.getStockId())) {
			throw new IllegalArgumentException("stockId is required for STOCK board");
		}
	}

    private static boolean isBlank(String v) {
        return v == null || v.trim().isEmpty();
    }
}
