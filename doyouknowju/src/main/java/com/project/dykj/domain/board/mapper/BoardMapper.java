package com.project.dykj.domain.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.project.dykj.domain.board.model.vo.Board;

/**
 * MyBatis Mapper 인터페이스.
 *
 * - @Mapper가 붙으면 Spring이 이 인터페이스를 빈(Bean)으로 등록하고, MyBatis가 프록시 구현체를 생성해 주입합니다.
 * - XML mapper의 namespace는 이 인터페이스의 FQCN(패키지+클래스명)과 정확히 일치해야 합니다.
 * - 메서드 이름은 XML의 id와 1:1로 매핑됩니다. (예: insertPost ↔ <insert id="insertPost">)
 */
@Mapper
public interface BoardMapper {

    /** 게시글 등록 (XML: insertPost) */
    int insertPost(Board board);

    /** 조회수 증가 (XML: incrementViewCnt) */
    int incrementViewCnt(@Param("postId") long postId);

    /** 게시글 상세 조회 (XML: selectPostDetail) */
    Board selectPostDetail(@Param("postId") long postId);

    /**
     * 게시글 목록 조회 (페이징)
     * - @Param 이름은 XML의 #{boardType}, #{stockId}, #{offset}, #{size}와 연결됩니다.
     */
    List<Board> selectPostList(
            @Param("boardType") String boardType,
            @Param("stockId") String stockId,
            @Param("offset") int offset,
            @Param("size") int size
    );

    /** 게시글 수정 (XML: updatePost) */
    int updatePost(Board board);

    /** 게시글 소프트 삭제 (XML: softDeletePost) */
    int softDeletePost(@Param("postId") long postId);
}