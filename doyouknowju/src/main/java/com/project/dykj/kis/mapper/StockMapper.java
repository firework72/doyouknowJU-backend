package com.project.dykj.kis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.project.dykj.kis.model.vo.StockSuggestItem;
import com.project.dykj.kis.model.vo.StockUpsertRequest;

/**
 * 주식(STOCKS) 마스터 테이블 관련 MyBatis Mapper.
 *
 * <p>XML mapper(`stock-mapper.xml`)의 namespace는 이 인터페이스 FQCN과 동일해야 한다.</p>
 * <p>메서드명은 상관없고, XML의 id와만 연결된다(예: id="suggest" ↔ suggest(...)).</p>
 * <p>@Param("...")은 XML의 #{...} 바인딩 이름을 지정할 때 사용한다.</p>
 */
@Mapper
public interface StockMapper {

	int mergeStock(@Param("req") StockUpsertRequest req);

	List<StockSuggestItem> suggest(@Param("q") String q, @Param("limit") int limit);

	StockUpsertRequest selectById(@Param("stockId") String stockId);
}
