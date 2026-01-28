package com.project.dykj.domain.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.project.dykj.domain.board.model.vo.Reply;

/**
 * 댓글(Reply)용 MyBatis Mapper.
 *
 * <p>@Mapper 인터페이스 메서드 ↔ XML mapper의 id가 연결되며,
 * 서비스/DAO에서는 SqlSessionTemplate 없이 이 인터페이스를 주입받아 호출한다.</p>
 */
@Mapper
public interface ReplyMapper {

	/** 댓글 등록. (XML: insertReply) */
	int insertReply(Reply reply);

	/** 댓글 목록 조회. (XML: selectReplies) */
	List<Reply> selectReplies(@Param("postId") long postId);

	/** 댓글 소프트 삭제. (XML: softDeleteReply) */
	int softDeleteReply(@Param("replyId") long replyId);
}
